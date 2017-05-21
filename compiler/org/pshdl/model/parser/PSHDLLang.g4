/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *     
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for 
 *     reasonable and customary use in describing the origin of the Work.
 * 
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/

parser grammar PSHDLLang;

options { tokenVocab = PSHDLLangLexer; }

@parser::members {
public static final String MISSING_SEMI="MISSING_SEMI";
public static final String MISSING_NAME="MISSING_NAME";
public static final String MISSING_TYPE="MISSING_TYPE";
public static final String MISSING_WIDTH="MISSING_WIDTH";
public static final String MISSING_IFPAREN="MISSING_IFPAREN";
public static final String WRONG_ORDER="WRONG_ORDER";
}

psModel :
	('package' psQualifiedName ';' )? (psUnit | psDeclaration )*
;

psUnit :
	psAnnotation* unitType=( 'module' | 'testbench' ) psInterface psExtends? '{' psImports* psBlock* '}'
	|	psAnnotation* unitType=( 'module' | 'testbench' ) psExtends? '{' psImports* psBlock* '}' {notifyErrorListeners(MISSING_NAME);}
;

psExtends:
	'extends' psQualifiedName (',' psQualifiedName)*
;

psImports :
	'import' psQualifiedNameImport ';'
	|	'import' psQualifiedNameImport {notifyErrorListeners(MISSING_SEMI);}
;

psQualifiedNameImport :
	psQualifiedName '.*'?
;

psBlock :
	(psDeclaration | psInstantiation | psStatement)
	|	('{' psBlock* '}')
;

psProcess :
	isProcess='process' '{' psBlock* '}'
;

psInstantiation :
	psAnnotation* (psInterfaceInstantiation | psDirectGeneration)
;

psInterfaceInstantiation :
	psQualifiedName psVariable psArray? psPassedArguments? ';'
	| 	psQualifiedName psVariable psArray? psPassedArguments? {notifyErrorListeners(MISSING_SEMI);}
;

psDirectGeneration :
	isInclude='include'? psInterface psVariable '=' 'generate' RULE_ID psPassedArguments? RULE_GENERATOR_CONTENT? ';'
	|	isInclude='include'? psInterface psVariable '=' 'generate' RULE_ID psPassedArguments? RULE_GENERATOR_CONTENT? {notifyErrorListeners(MISSING_SEMI);}
;

psPassedArguments :
	'(' (psArgument (',' psArgument )* )? ')'
;

psArgument :
	RULE_ID '=' psExpression
;

psCast :
	'(' psPrimitiveType psWidth? ')'
;

psExpression :
	<assoc=right> (psCast | type='!' | type='~' | type='-') psExpression  			#psManip
	| psExpression op=('**' | '*' | '/' | '%') psExpression							#psMul
	| psExpression op=('+' | '-') psExpression 										#psAdd
	| psExpression op=('<<' | '>>' | '>>>') psExpression 							#psShift
	| psExpression op=('<' | '<=' | '>' | '>=') psExpression						#psEqualityComp
	| psExpression op=('==' | '!=') psExpression									#psEquality
	| psExpression '&' psExpression													#psBitAnd
	| <assoc=right>psExpression '^' psExpression									#psBitXor
	| psExpression '|' psExpression													#psBitOr
	| psExpression '#' psExpression													#psConcat
	| psExpression '&&' psExpression												#psBitLogAnd
	| psExpression '||' psExpression												#psBitLogOr
	| psExpression '?' psExpression ':' psExpression								#psTernary
	| psValue 																		#psValueExp
	| psArrayInitSubParens 															#psArrayInitExp
	| '(' psExpression ')'															#psParens
;

psValue :
	RULE_PS_LITERAL_TERMINAL | psVariableRef | RULE_STRING 
;

psBitAccess :
	'{' psAccessRange ( ',' psAccessRange )* '}'
;

psAccessRange :
	from=psExpression (
		( ':' to=psExpression )
		| ( '+:' inc=psExpression)
		| ( '-:' dec=psExpression)
	)?
;

psVariableRef :
	psRefPart ('.' psRefPart)* |
	isClk='$clk' |
	isRst='$rst'
;

psRefPart :
	RULE_ID (
		psArray? psBitAccess? |
		psFuncArgs
	)
;

psVariable :
	RULE_ID
;

psVariableMatch: 
	psVariable ('*' | '?') psVariableMatch?
;

psStatement :
	psCompoundStatement | psProcess | psAssignmentOrFunc | psExport
;

psGroupMatch:
	'/' psVariable '/'
;

psExport :
	'export' instance=psVariable ('.' (portMatch=psVariableMatch | groupMatch=psGroupMatch))? ';'
;

psFunctionDeclaration :
	psNativeFunction | psInlineFunction | psSubstituteFunction
;

psInlineFunction :
	'inline' 'function' psFuncRecturnType psFunction psFuncParam '->' '(' psExpression ')'
;

psSubstituteFunction :
	'substitute' 'function' psFuncRecturnType? psFunction psFuncParam '{' psBlock* '}'
;

psNativeFunction :
	isSim='simulation'? 'native' 'function' psFuncRecturnType? psFunction psFuncParam';'
;

psFuncRecturnType :
	psFuncParamType dims+=psFuncOptArray*
;

psFuncParam :
	'(' ( psFuncSpec ( ',' psFuncSpec )* )? ')'
;

psFuncSpec : 
	psFuncParamWithRW RULE_ID dims+=psFuncOptArray*
;
psFuncParamWithRW: 
	(psFuncParamRWType? | constant='const'?) psFuncParamType
;
psFuncOptArray:
	('[' psExpression? ']')
;

psFuncParamRWType:
	'-' | '+' | '*'
;

psFuncParamType:
	ANY_INT | ANY_UINT | ANY_BIT | ANY_IF | ANY_ENUM | 
	BOOL | STRING | 
	(BIT  psWidth? )|
	(UINT psWidth? )|
	(INT  psWidth? )| 
	(INTERFACE '<' psQualifiedName '>')|
	(ENUM '<' psQualifiedName '>' )|
	(FUNCTION '<' (psFuncParamWithRW (',' psFuncParamWithRW )* )? ('=>' returnType=psFuncParamType)? '>')
;

psFunction :
	RULE_ID
;

psFuncArgs :
	'(' ( psExpression ( ',' psExpression )* )? ')'
;

psAssignmentOrFunc :
	psVariableRef (psAssignmentOp psExpression )? ';'
	|	psVariableRef (psAssignmentOp psExpression )? {notifyErrorListeners(MISSING_SEMI);}
;

psAssignmentOp :
	'=' | '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '^=' | '|=' | '<<=' | '>>=' | '>>>='
;

psCompoundStatement :
	psIfStatement | psForStatement | psSwitchStatement
;

psIfStatement :
	'if' '(' psExpression ')' ifBlk=psSimpleBlock ('else' elseBlk=psSimpleBlock )?
	| 'if' psExpression ifBlk=psSimpleBlock ('else' elseBlk=psSimpleBlock )? {notifyErrorListeners(MISSING_IFPAREN);}
;

psSimpleBlock : 
	'{' psBlock* '}' |
	psBlock
;

psForStatement :
	'for' '(' psVariable '=' psBitAccess ')' psSimpleBlock
;

psSwitchStatement :
	'switch' '(' psVariableRef ')' '{' psCaseStatements* '}'
;

psCaseStatements :
	('case' psValue | 'default') ':' psBlock*
;

psDeclaration :
	psAnnotation* psDeclarationType ';'?
;

psDeclarationType :
	psVariableDeclaration | psTypeDeclaration | psFunctionDeclaration
;

psTypeDeclaration :
	psInterfaceDeclaration | psEnumDeclaration
;

psEnumDeclaration :
	'enum' psEnum hasAss='='? '{' psVariable ( ',' psVariable )* '}'
;

psEnum :
	psQualifiedName
;

psVariableDeclaration :
	psDirection? psPrimitive psDeclAssignment ( ',' psDeclAssignment )* ';'
	|	psDirection psDeclAssignment ( ',' psDeclAssignment )* {notifyErrorListeners(MISSING_TYPE);} ';'
	|	psPrimitive psDirection psDeclAssignment ( ',' psDeclAssignment )* {notifyErrorListeners(WRONG_ORDER);} ';'
	|	psDirection? psPrimitive psDeclAssignment ( ',' psDeclAssignment )* {notifyErrorListeners(MISSING_SEMI);} ';'
;

psDeclAssignment :
	psAnnotation* psVariable psArray? ( '=' psArrayInit )?
;

psArrayInit :
	   psExpression | psArrayInitSubParens
;

psArrayInitSubParens:
	'{' psArrayInitSub '}'
;

psArrayInitSub :
	   psExpression (',' psExpression)*	| psArrayInitSubParens
;


psArray :
	( '[' psExpression ']' )+
;

psDirection :
	'in' | 'out' | 'inout' | 'param' | 'const'
;

psAnnotation :
	psAnnotationType ( '(' RULE_STRING ')' )?
;

psAnnotationType :
	'@' RULE_ID
;

psPrimitive :
	(isRegister='register' psPassedArguments? )? (psPrimitiveType psWidth? |  (isEnum='enum' | isRecord='record') psQualifiedName)
	|	(psPrimitiveType psWidth? |  (isEnum='enum' | isRecord='record') psQualifiedName) (isRegister='register' psPassedArguments? ) {notifyErrorListeners(WRONG_ORDER);}
;

psPrimitiveType :
	'int' | 'uint' | 'bit' | 'bool' | 'string' | ANY_INT | ANY_UINT | ANY_BIT | 'uint32' | 'int32'
;

psWidth :
	'<' psExpression '>'
;

psInterfaceDeclaration :
	'interface' psInterface ( 'extends' psInterfaceExtends )? psInterfaceDecl
;

psInterface :
	psQualifiedName
;

psInterfaceExtends :
	psQualifiedName ( ',' psQualifiedName )*
;

psInterfaceDecl :
	'{' psPortDeclaration* '}'
;

psPortDeclaration :
	psAnnotation* psVariableDeclaration
;

psQualifiedName :
	RULE_ID (DOT RULE_ID)*
;

/*
* generated by Xtext
*/
grammar PSHDLLang;

@lexer::members {
public static final int WHITESPACE = 1; 
public static final int COMMENTS = 2;
}

// Rule PSModel
psModel :
	(
		'package' psQualifiedName ';'
	)? (
		psUnit |
		psDeclaration
	)*
;

// Rule PSUnit
psUnit :
	psAnnotation* unitType=( 'module' | 'testbench' ) psInterface '{' psImports* psBlock* '}'
;

// Rule PSImports
psImports :
	'import' psQualifiedNameImport ';'
;

// Rule QualifiedNameImport
psQualifiedNameImport :
	psQualifiedName '.*'?
;

// Rule PSBlock
psBlock :
	psDeclaration |
	psStatement |
	psInstantiation
;

// Rule PSProcess
psProcess :
	'process' '{' psBlock* '}'
;

// Rule PSInstantiation
psInstantiation :
	psInterfaceInstantiation |
	psDirectGeneration
;

// Rule PSInterfaceInstantiation
psInterfaceInstantiation :
	psQualifiedName psVariable psArray? psPassedArguments? ';'
;

// Rule PSDirectGeneration
psDirectGeneration :
	isInclude='include'? psInterface psVariable psGenerator ';'
;

// Rule PSGenerator
psGenerator :
	'=' 'generate' RULE_ID psPassedArguments? RULE_GENERATOR_CONTENT?
;

// Rule PSPassedArguments
psPassedArguments :
	'(' (
		psArgument (
			',' psArgument
		)*
	)? ')'
;

// Rule PSArgument
psArgument :
	RULE_ID '=' psExpression
;

// Rule PSEqualityOp
psEqualityOp :
	'==' |
	'!='
;

psEqualityCompOp :
	'<' |
	'<=' |
	'>' |
	'>='
;

// Rule PSShiftOp
psShiftOp :
	'<<' |
	'>>' |
	'>>>'
;

// Rule PSAddOp
psAddOp :
	'+' |
	'-'
;

// Rule PSMulOp
psMulOp :
	'**' |
	'*' |
	'/' |
	'%'
;

// Rule PSCast
psCast :
	'(' psPrimitiveType psWidth? ')'
;

psExpression :
	(psCast | '!'<assoc=right> | '~'<assoc=right> | '-'<assoc=right>) psExpression |
	psExpression psMulOp psExpression |
	psExpression psAddOp psExpression |
	psExpression psShiftOp psExpression |
	psExpression psEqualityCompOp psExpression |
	psExpression psEqualityOp psExpression |
	psExpression '&' psExpression |
	psExpression '^' psExpression |
	psExpression '|' psExpression |
	psExpression '#' psExpression |
	psExpression '&&' psExpression |
	psExpression '||' psExpression |
	psExpression '?' psExpression ':' psExpression|
	psValue |
	'(' psExpression ')'
;

psValue :
	RULE_PS_LITERAL_TERMINAL |
	psVariableRef |
	RULE_STRING
;

// Rule PSBitAccess
psBitAccess :
	'{' psAccessRange ( ',' psAccessRange )* '}'
;

// Rule PSAccessRange
psAccessRange :
	psExpression ( ':' psExpression )?
;

// Rule PSVariableRef
psVariableRef :
	psRefPart ('.' psRefPart)* |
	isClk='$clk' |
	isRst='$rst'
;

// Rule PSRefPart
psRefPart :
	RULE_ID (
		psArray? psBitAccess? |
		psFuncArgs
	)
;

// Rule PSVariable
psVariable :
	RULE_ID
;

// Rule PSStatement
psStatement :
	psAssignmentOrFunc |
	psCompoundStatement |
	psProcess
;

// Rule PSFunctionDeclaration
psFunctionDeclaration :
	psNativeFunction |
	psInlineFunction |
	psSubstituteFunction
;

// Rule PSInlineFunction
psInlineFunction :
	'inline' 'function' psFunction psFuncParam '->' '(' psExpression ')'
;

// Rule PSSubstituteFunction
psSubstituteFunction :
	'substitute' 'function' psFunction psFuncParam '{' psStatement* '}'
;

// Rule PSFuncParam
psFuncParam :
	'(' ( psVariable ( ',' psVariable )* )? ')'
;

// Rule PSNativeFunction
psNativeFunction :
	isSim='simulation'? 'native' 'function' psFunction ';'
;

// Rule PSFunction
psFunction :
	RULE_ID
;

// Rule PSFuncArgs
psFuncArgs :
	'(' ( psExpression ( ',' psExpression )* )? ')'
;

// Rule PSAssignmentOrFunc
psAssignmentOrFunc :
	psVariableRef (psAssignmentOp psExpression )? ';'
;

// Rule PSAssignmentOp
psAssignmentOp :
	'=' |
	'+=' |
	'-=' |
	'*=' |
	'/=' |
	'%=' |
	'&=' |
	'^=' |
	'|=' |
	'<<=' |
	'>>=' |
	'>>>='
;

// Rule PSCompoundStatement
psCompoundStatement :
	psIfStatement |
	psForStatement |
	psSwitchStatement
;

// Rule PSIfStatement
psIfStatement :
	'if' '(' psExpression ')' ruleSimpleBlock 
	('else' ruleSimpleBlock )?
;

ruleSimpleBlock : 
	'{' psBlock* '}' |
	psBlock
;

// Rule PSForStatement
psForStatement :
	'for' '(' psVariable '=' psBitAccess ')' ruleSimpleBlock
;

// Rule PSSwitchStatement
psSwitchStatement :
	'switch' '(' psVariableRef ')' '{' psCaseStatements* '}'
;

// Rule PSCaseStatements
psCaseStatements :
	('case' psValue | 'default') ':' psBlock*
;

// Rule PSDeclaration
psDeclaration :
	psAnnotation* psDeclarationType ';'?
;

// Rule PSDeclarationType
psDeclarationType :
	psVariableDeclaration |
	psTypeDeclaration |
	psFunctionDeclaration
;

// Rule PSTypeDeclaration
psTypeDeclaration :
	psInterfaceDeclaration |
	psEnumDeclaration
;

// Rule PSEnumDeclaration
psEnumDeclaration :
	'enum' psEnum '=' '{' psVariable ( ',' psVariable )* '}'
;

// Rule PSEnum
psEnum :
	psQualifiedName
;

// Rule PSVariableDeclaration
psVariableDeclaration :
	psDirection? psPrimitive psDeclAssignment ( ',' psDeclAssignment )* ';'
;

// Rule PSDeclAssignment
psDeclAssignment :
	psAnnotation* psVariable psArray? ( '=' psArrayInit )?
;

psArrayInit :
	   (psExpression (',' psExpression )*) 	|
	'{' psArrayInit  (',' psArrayInit )* '}'
;

// Rule PSArray
psArray :
	( '[' psExpression ']' )+
;

// Rule PSDirection
psDirection :
	'in' | 'out' | 'inout' | 'param' | 'const'
;

// Rule PSAnnotation
psAnnotation :
	psAnnotationType ( '(' RULE_STRING ')' )?
;

// Rule PSAnnotationType
psAnnotationType :
	'@' RULE_ID
;

// Rule PSPrimitive
psPrimitive :
	(isRegister='register' psPassedArguments? )? (psPrimitiveType psWidth? |  'enum' psQualifiedName)
;

// Rule PSPrimitiveType
psPrimitiveType :
	'int' | 'uint' | 'bit' | 'bool' | 'string'
;

// Rule PSWidth
psWidth :
	'<' psExpression '>'
;

// Rule PSInterfaceDeclaration
psInterfaceDeclaration :
	'interface' psInterface ( 'extends' psInterfaceExtends )? psInterfaceDecl
;

// Rule PSInterface
psInterface :
	psQualifiedName
;

// Rule PSInterfaceExtends
psInterfaceExtends :
	psQualifiedName ( ',' psQualifiedName )*
;

// Rule PSInterfaceDecl
psInterfaceDecl :
	'{' psPortDeclaration* '}'
;

// Rule PSPortDeclaration
psPortDeclaration :
	psAnnotation* psVariableDeclaration
;

// Rule QualifiedName
psQualifiedName :
	RULE_ID ('.' RULE_ID)*
;

RULE_PS_LITERAL_TERMINAL :
	'0b' ( '0' | '1' | '_')+ |
	'0x' ( 'a' .. 'f' | 'A' .. 'F' | '0' .. '9' | '_')+ |
	'1' .. '9' ( '0' .. '9' | '_' )+ |
	'0' .. '9' |
	'true' |
	'false'
;

fragment
IDCHAR : 'a' .. 'z' | 'A' .. 'Z' | '_';

RULE_ID :
	IDCHAR ( IDCHAR | '0' .. '9' )*
;

fragment
ESC : 	'\\' ('b' | 't' | 'n' | 'f' | 'r' | '"' | '\'' | '\\');

RULE_STRING :
	'"'  (ESC | ~ ('\\' | '"'))* '"' |
	'\'' (ESC | ~ ('\\' |'\''))* '\''
;

RULE_ML_COMMENT :
	('/*' .*? '*/') -> channel(COMMENTS)
;

RULE_GENERATOR_CONTENT :
	'<[' .*? ']>'
;

RULE_SL_COMMENT :
	('//' ~ ('\n' | '\r' )* ('\r'? '\n')? )-> channel(COMMENTS)
;

RULE_WS :
	(' ' | '\t' | '\r' | '\n')+ -> channel(WHITESPACE)
;
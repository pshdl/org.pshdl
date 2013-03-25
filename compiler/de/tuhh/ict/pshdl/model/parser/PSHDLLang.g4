grammar PSHDLLang;

@lexer::members {
public static final int WHITESPACE = 1; 
public static final int COMMENTS = 2;
}

psModel :
	('package' psQualifiedName ';' )? (psUnit | psDeclaration )*
;

psUnit :
	psAnnotation* unitType=( 'module' | 'testbench' ) psInterface psExtends? '{' psImports* psBlock* '}'
;

psExtends:
	'extends' psQualifiedName (',' psQualifiedName)*
;

psImports :
	'import' psQualifiedNameImport ';'
;

psQualifiedNameImport :
	psQualifiedName '.*'?
;

psBlock :
	psDeclaration | psStatement | psInstantiation
;

psProcess :
	isProcess='process' '{' psBlock* '}'
;

psInstantiation :
	psInterfaceInstantiation | psDirectGeneration
;

psInterfaceInstantiation :
	psQualifiedName psVariable psArray? psPassedArguments? ';'
;

psDirectGeneration :
	isInclude='include'? psInterface psVariable '=' 'generate' RULE_ID psPassedArguments? RULE_GENERATOR_CONTENT? ';'
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
	(psCast | type='!'<assoc=right> | type='~'<assoc=right> | type='-'<assoc=right>) psExpression  #psManip
	| psExpression op=('**' | '*' | '/' | '%') psExpression							#psMul
	| psExpression op=('+' | '-') psExpression 										#psAdd
	| psExpression op=('<<' | '>>' | '>>>') psExpression 							#psShift
	| psExpression op=('<' | '<=' | '>' | '>=') psExpression						#psEqualityComp
	| psExpression op=('==' | '!=') psExpression									#psEquality
	| psExpression '&' psExpression													#psBitAnd
	| psExpression '^'<assoc=right> psExpression									#psBitXor
	| psExpression '|' psExpression													#psBitOr
	| psExpression '#' psExpression													#psConcat
	| psExpression '&&' psExpression												#psBitLogAnd
	| psExpression '||' psExpression												#psBitLogOr
	| psExpression '?' psExpression ':' psExpression								#psTernary
	| psValue 																		#psValueExp
	| '(' psExpression ')'															#psParens
;

psValue :
	RULE_PS_LITERAL_TERMINAL | psVariableRef | RULE_STRING
;

psBitAccess :
	'{' psAccessRange ( ',' psAccessRange )* '}'
;

psAccessRange :
	from=psExpression ( ':' to=psExpression )?
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

psStatement :
	psAssignmentOrFunc | psCompoundStatement | psProcess
;

psFunctionDeclaration :
	psNativeFunction | psInlineFunction | psSubstituteFunction
;

psInlineFunction :
	'inline' 'function' psFunction psFuncParam '->' '(' psExpression ')'
;

psSubstituteFunction :
	'substitute' 'function' psFunction psFuncParam '{' psStatement* '}'
;

psFuncParam :
	'(' ( psVariable ( ',' psVariable )* )? ')'
;

psNativeFunction :
	isSim='simulation'? 'native' 'function' psFunction ';'
;

psFunction :
	RULE_ID
;

psFuncArgs :
	'(' ( psExpression ( ',' psExpression )* )? ')'
;

psAssignmentOrFunc :
	psVariableRef (psAssignmentOp psExpression )? ';'
;

psAssignmentOp :
	'=' | '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '^=' | '|=' | '<<=' | '>>=' | '>>>='
;

psCompoundStatement :
	psIfStatement | psForStatement | psSwitchStatement
;

psIfStatement :
	'if' '(' psExpression ')' ifBlk=psSimpleBlock 
	('else' elseBlk=psSimpleBlock )?
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
	'enum' psEnum '=' '{' psVariable ( ',' psVariable )* '}'
;

psEnum :
	psQualifiedName
;

psVariableDeclaration :
	psDirection? psPrimitive psDeclAssignment ( ',' psDeclAssignment )* ';'
;

psDeclAssignment :
	psAnnotation* psVariable psArray? ( '=' psArrayInit )?
;

psArrayInit :
	   psExpression 	|
	'{' psArrayInitSub  (',' psArrayInitSub )* '}'
;

psArrayInitSub :
	   psExpression (',' psExpression)*	|
	'{' psArrayInitSub  (',' psArrayInitSub )* '}'
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
;

psPrimitiveType :
	'int' | 'uint' | 'bit' | 'bool' | 'string'
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
	RULE_ID ('.' RULE_ID)*
;


AND:'&';
OR:'|';
XOR:'^';
LOGI_AND:'&&';
LOGI_OR:'||';
MUL:'*';
DIV:'/';
//MINUS:'-';
PLUS:'+';
MOD:'%';
POW:'**';
SLL:'<<';
SRA:'>>';
SRL:'>>>';
EQ:'==';
NOT_EQ:'!=';
LESS:'<';
LESS_EQ:'<=';
GREATER:'>';
GREATER_EQ:'>=';
ASSGN:'=';
ADD_ASSGN:'+=';
SUB_ASSGN:'-=';
MUL_ASSGN:'*=';
DIV_ASSGN:'/=';
MOD_ASSGN:'%=';
AND_ASSGN:'&=';
XOR_ASSGN:'^=';
OR_ASSGN:'|=';
SLL_ASSGN:'<<=';
SRL_ASSGN:'>>>=';
SRA_ASSGN:'>>=';
ARITH_NEG:'-';
BIT_NEG:'~';
LOGIC_NEG:'!';

MODULE:'module';
TESTBENCH:'testbench';

RULE_PS_LITERAL_TERMINAL :
	'0b' ( '0' | '1' | '_')+ |
	'0x' ( 'a' .. 'f' | 'A' .. 'F' | '0' .. '9' | '_')+ |
	'1' .. '9' ( '0' .. '9' | '_' )+ |
	'0' .. '9' |
	'true' |
	'false'
;

fragment
    IDCHARFIRST : 'a' .. 'z' | 'A' .. 'Z';
fragment
    IDCHAR : IDCHARFIRST | '_' | '0' .. '9';

RULE_ID :
	IDCHARFIRST IDCHAR*
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
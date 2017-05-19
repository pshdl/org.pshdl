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

lexer grammar PSHDLLangLexer;

channels { COMMENTS, WHITESPACE }

AT:'@';
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

INC_RANGE:'+:';
DECC_RANGE:'-:';

IN:'in';
OUT:'out';
INOUT:'inout';
PARAM:'param';
CONST:'const';

REGISTER:'register';
RECORD:'record';

ANY_INT:'int<>';
ANY_UINT:'uint<>';
ANY_BIT:'bit<>';
ANY_IF:'interface<>';
ANY_ENUM:'enum<>';
BIT:'bit';
INT:'int';
UINT:'uint';
INT32:'int32';
UINT32:'uint32';
STRING:'string';
BOOL:'bool';
ENUM:'enum';

EXTENDS:'extends';
EXPORT:'export';
INCLUDE:'include';
IMPORT:'import';
DOT_WILDCARD:'.*';
PROCESS:'process';
GENERATE:'generate';

INLINE:'inline';
INTERFACE:'interface';
FUNCTION:'function';
SUBSTITUTE:'substitute';
SIMULATION:'simulation';
NATIVE:'native';
INLINE_FUNC_FOLLOW:'->';
FUNC_RETURN:'=>';


HASH:'#';
QUESTIONMARK:'?';
SEMICOLON:';';
COMMA:',';
COLON:':';
DOT:'.';
CURLY_OPEN:'{';
CURLY_CLOSE:'}';
PAREN_OPEN:'(';
PAREN_CLOSE:')';
BRACKET_OPEN:'[';
BRACKET_CLOSE:']';

MODULE:'module';
TESTBENCH:'testbench';
PACKAGE:'package';

IF:'if';
ELSE:'else';
FOR:'for';
SWITCH:'switch';
CASE:'case';
DEFAULT:'default';

CLK:'$clk';
RST:'$rst';

RULE_PS_LITERAL_TERMINAL :
	'0b' ( '0' | '1' | '_')+ |
	'0x' ( 'a' .. 'f' | 'A' .. 'F' | '0' .. '9' | '_')+ |
	'1' .. '9' ( '0' .. '9' | '_' )+ |
	'0' .. '9' |
	'true' |
	'false'
;

fragment
    IDCHARFIRST : '$' | 'a' .. 'z' | 'A' .. 'Z';
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
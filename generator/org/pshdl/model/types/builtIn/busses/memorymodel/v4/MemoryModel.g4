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
grammar MemoryModel;

unit	 :	(declaration)*
		(memory);
declaration
	:	(
			(row) | 
			(column) | 
			(alias)  |
			(constant)
		) ;
	
row
	:
		'row' rowID '{' 
		(
			(filling)    |
			(definition) | 
			(reference)
		)* '}';

rowID:
	'^'? ID;

constant
	:
		'const' ID? value=(NUMBER | '$date' | '$time' | '$checkSum') ';';

filling
	:
		'fill' ('<' width '>')? ';';

column 
	:	
		'column' ID '{' (reference)* '}';
alias
	:	
		'alias' ID '{' 
		(
			(definition) | 
			(reference)
		)* '}';
memory
	:
		'memory' '{' (reference)* '}';
definition
	:
		rwStatus
		(hasRegister='register')?
		type
		('<' width '>')? 
		ID 
		('[' NUMBER ']')* 
		(warnType)?
		';';
warnType
	:	(silent='silent')? 
		typeString=('mask'|
		'error'|
		'limit');
rwStatus:	('r' | 'w' | 'rw');
width	:	NUMBER;
type	
	:
		('int' | 'uint' | 'bit');
reference
	:	
		ID
		('[' NUMBER ']')* ';';

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

NUMBER :
	'0b' ( '0' | '1' | '_')+ |
	'0x' ( 'a' .. 'f' | 'A' .. 'F' | '0' .. '9' | '_')+ |
	'1' .. '9' ( '0' .. '9' | '_' )+ |
	'0' .. '9'
;

COMMENT
    :   ('//' ~('\n'|'\r')* '\r'? '\n' 
        |   '/*' .*? '*/' 
        ) -> skip;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) -> skip
    ;


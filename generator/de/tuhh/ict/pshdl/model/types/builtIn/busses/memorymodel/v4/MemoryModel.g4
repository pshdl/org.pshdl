grammar MemoryModel;

unit	 :	(declaration)*
		(memory);
declaration
	:	(
			(row) | 
			(column) | 
			(alias)
		) ;
	
row
	:
		'row' ID '{' 
		(
			(definition) | 
			(reference)
		)* '}';
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
		('[' INT ']')* 
		(warnType)?
		';';
warnType
	:	(silent='silent')? 
		typeString=('mask'|
		'error'|
		'limit');
rwStatus:	('r' | 'w' | 'rw');
width	:	INT;
type	
	:
		('int' | 'uint' | 'bit');
reference
	:	
		ID
		('[' INT ']')* ';';

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	'0'..'9'+
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


grammar MemoryModel;

tokens {
	ROMWIDTH = 'rowWidth';
	ROW = 'row';
	COLUMN = 'column';
	ALIAS = 'alias';
	MEMORY = 'memory';
	INTTYPE = 'int';
	UINTTYPE = 'uint';
	BITTYPE = 'bit';
	READ='r';
	WRITE='w';
	READWRITE='rw';
	MASK='mask';
	LIMIT='limit';
	ERROR='error';
	SILENT='silent';
}
@header {
package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;
import java.util.LinkedHashMap;
import java.util.Map;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.*;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.*;
}

@lexer::header {
package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;
}

@members {
	
}

unit	returns [Unit res]
	:	{$res=new Unit();}
		(ROMWIDTH '=' INT {$res.rowWidth=Integer.parseInt($INT.text);} ';' )?
		(declaration {$res.declarations.put($declaration.decl.getName(), $declaration.decl);})*
		(memory {$res.memory=$memory.res;});
declaration returns [NamedElement decl]
	:	(
			(row {$decl=$row.res;}) | 
			(column {$decl=$column.res;}) | 
			(alias {$decl=$alias.res;})
		) ;
	
row returns [Row res]
	:
		ROW ID {$res=new Row($ID.text);} '{' 
		(
			(definition {$res.definitions.add($definition.res);}) | 
			(reference {$res.definitions.add($reference.res);})
		)* '}';
column returns [Column res]
	:	
		COLUMN ID {$res=new Column($ID.text);} '{' (reference {$res.rows.add($reference.res);})* '}';
alias returns [Alias res]	
	:	
		ALIAS ID {$res=new Alias($ID.text);} '{' 
		(
			(definition {$res.definitions.add($definition.res);}) | 
			(reference {$res.definitions.add($reference.res);})
		)* '}';
memory returns [Memory res]
	:	{$res=new Memory();}
		MEMORY '{' (reference {$res.references.add($reference.res);})* '}';
definition returns [Definition res]
	:	{$res=new Definition();}
		rwStatus {$res.rw=RWType.valueOf($rwStatus.text);}
		('register' {$res.register=true;})?
		type {$res.type=Type.valueOf($type.text.toUpperCase());} 
		('<' width {$res.width=Integer.parseInt($width.text);} '>')? 
		ID {$res.name=$ID.text;} 
		('[' INT ']' {$res.dimensions.add(Integer.parseInt($INT.text));})* 
		(warnType{$res.warn=$warnType.warn;})?
		';';
warnType returns [WarnType warn]
	:	{boolean silent=false;} (SILENT {silent=true;})? 
		(MASK{$warn=silent?WarnType.silentMask:WarnType.mask;}|
		ERROR{$warn=silent?WarnType.silentError:WarnType.error;}|
		LIMIT{$warn=silent?WarnType.silentLimit:WarnType.limit;});
rwStatus:	(READ | WRITE | READWRITE);
width	:	INT;
type	
	:
		(INTTYPE | UINTTYPE | BITTYPE);
reference returns [Reference res]
	:	
		ID {$res=new Reference($ID.text);} 
		('[' INT ']' {$res.dimensions.add(Integer.parseInt($INT.text));})* ';';

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	'0'..'9'+
    ;

COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;


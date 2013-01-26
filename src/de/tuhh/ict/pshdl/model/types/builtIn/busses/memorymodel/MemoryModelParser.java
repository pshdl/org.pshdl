// $ANTLR 3.5 /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g 2013-01-23 11:37:02

package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;
import java.util.LinkedHashMap;
import java.util.Map;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.*;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MemoryModelParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALIAS", "BITTYPE", "COLUMN", 
		"COMMENT", "ERROR", "ID", "INT", "INTTYPE", "LIMIT", "MASK", "MEMORY", 
		"READ", "READWRITE", "ROMWIDTH", "ROW", "SILENT", "UINTTYPE", "WRITE", 
		"WS", "';'", "'<'", "'='", "'>'", "'['", "']'", "'register'", "'{'", "'}'"
	};
	public static final int EOF=-1;
	public static final int T__23=23;
	public static final int T__24=24;
	public static final int T__25=25;
	public static final int T__26=26;
	public static final int T__27=27;
	public static final int T__28=28;
	public static final int T__29=29;
	public static final int T__30=30;
	public static final int T__31=31;
	public static final int ALIAS=4;
	public static final int BITTYPE=5;
	public static final int COLUMN=6;
	public static final int COMMENT=7;
	public static final int ERROR=8;
	public static final int ID=9;
	public static final int INT=10;
	public static final int INTTYPE=11;
	public static final int LIMIT=12;
	public static final int MASK=13;
	public static final int MEMORY=14;
	public static final int READ=15;
	public static final int READWRITE=16;
	public static final int ROMWIDTH=17;
	public static final int ROW=18;
	public static final int SILENT=19;
	public static final int UINTTYPE=20;
	public static final int WRITE=21;
	public static final int WS=22;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public MemoryModelParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public MemoryModelParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return MemoryModelParser.tokenNames; }
	@Override public String getGrammarFileName() { return "/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g"; }


		



	// $ANTLR start "unit"
	// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:36:1: unit returns [Unit res] : ( ROMWIDTH '=' INT ';' )? ( declaration )* ( memory ) ;
	public final Unit unit() throws RecognitionException {
		Unit res = null;


		Token INT1=null;
		NamedElement declaration2 =null;
		Memory memory3 =null;

		try {
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:37:2: ( ( ROMWIDTH '=' INT ';' )? ( declaration )* ( memory ) )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:37:4: ( ROMWIDTH '=' INT ';' )? ( declaration )* ( memory )
			{
			res =new Unit();
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:38:3: ( ROMWIDTH '=' INT ';' )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0==ROMWIDTH) ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:38:4: ROMWIDTH '=' INT ';'
					{
					match(input,ROMWIDTH,FOLLOW_ROMWIDTH_in_unit151); 
					match(input,25,FOLLOW_25_in_unit153); 
					INT1=(Token)match(input,INT,FOLLOW_INT_in_unit155); 
					res.rowWidth=Integer.parseInt((INT1!=null?INT1.getText():null));
					match(input,23,FOLLOW_23_in_unit159); 
					}
					break;

			}

			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:39:3: ( declaration )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==ALIAS||LA2_0==COLUMN||LA2_0==ROW) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:39:4: declaration
					{
					pushFollow(FOLLOW_declaration_in_unit167);
					declaration2=declaration();
					state._fsp--;

					res.declarations.put(declaration2.getName(), declaration2);
					}
					break;

				default :
					break loop2;
				}
			}

			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:40:3: ( memory )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:40:4: memory
			{
			pushFollow(FOLLOW_memory_in_unit176);
			memory3=memory();
			state._fsp--;

			res.memory=memory3;
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return res;
	}
	// $ANTLR end "unit"



	// $ANTLR start "declaration"
	// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:41:1: declaration returns [NamedElement decl] : ( ( row ) | ( column ) | ( alias ) ) ;
	public final NamedElement declaration() throws RecognitionException {
		NamedElement decl = null;


		Row row4 =null;
		Column column5 =null;
		Alias alias6 =null;

		try {
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:42:2: ( ( ( row ) | ( column ) | ( alias ) ) )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:42:4: ( ( row ) | ( column ) | ( alias ) )
			{
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:42:4: ( ( row ) | ( column ) | ( alias ) )
			int alt3=3;
			switch ( input.LA(1) ) {
			case ROW:
				{
				alt3=1;
				}
				break;
			case COLUMN:
				{
				alt3=2;
				}
				break;
			case ALIAS:
				{
				alt3=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}
			switch (alt3) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:43:4: ( row )
					{
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:43:4: ( row )
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:43:5: row
					{
					pushFollow(FOLLOW_row_in_declaration197);
					row4=row();
					state._fsp--;

					decl =row4;
					}

					}
					break;
				case 2 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:44:4: ( column )
					{
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:44:4: ( column )
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:44:5: column
					{
					pushFollow(FOLLOW_column_in_declaration209);
					column5=column();
					state._fsp--;

					decl =column5;
					}

					}
					break;
				case 3 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:45:4: ( alias )
					{
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:45:4: ( alias )
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:45:5: alias
					{
					pushFollow(FOLLOW_alias_in_declaration221);
					alias6=alias();
					state._fsp--;

					decl =alias6;
					}

					}
					break;

			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return decl;
	}
	// $ANTLR end "declaration"



	// $ANTLR start "row"
	// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:48:1: row returns [Row res] : ROW ID '{' ( ( definition ) | ( reference ) )* '}' ;
	public final Row row() throws RecognitionException {
		Row res = null;


		Token ID7=null;
		Definition definition8 =null;
		Reference reference9 =null;

		try {
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:49:2: ( ROW ID '{' ( ( definition ) | ( reference ) )* '}' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:50:3: ROW ID '{' ( ( definition ) | ( reference ) )* '}'
			{
			match(input,ROW,FOLLOW_ROW_in_row245); 
			ID7=(Token)match(input,ID,FOLLOW_ID_in_row247); 
			res =new Row((ID7!=null?ID7.getText():null));
			match(input,30,FOLLOW_30_in_row251); 
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:51:3: ( ( definition ) | ( reference ) )*
			loop4:
			while (true) {
				int alt4=3;
				int LA4_0 = input.LA(1);
				if ( ((LA4_0 >= READ && LA4_0 <= READWRITE)||LA4_0==WRITE) ) {
					alt4=1;
				}
				else if ( (LA4_0==ID) ) {
					alt4=2;
				}

				switch (alt4) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:52:4: ( definition )
					{
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:52:4: ( definition )
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:52:5: definition
					{
					pushFollow(FOLLOW_definition_in_row262);
					definition8=definition();
					state._fsp--;

					res.definitions.add(definition8);
					}

					}
					break;
				case 2 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:53:4: ( reference )
					{
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:53:4: ( reference )
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:53:5: reference
					{
					pushFollow(FOLLOW_reference_in_row274);
					reference9=reference();
					state._fsp--;

					res.definitions.add(reference9);
					}

					}
					break;

				default :
					break loop4;
				}
			}

			match(input,31,FOLLOW_31_in_row284); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return res;
	}
	// $ANTLR end "row"



	// $ANTLR start "column"
	// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:55:1: column returns [Column res] : COLUMN ID '{' ( reference )* '}' ;
	public final Column column() throws RecognitionException {
		Column res = null;


		Token ID10=null;
		Reference reference11 =null;

		try {
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:56:2: ( COLUMN ID '{' ( reference )* '}' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:57:3: COLUMN ID '{' ( reference )* '}'
			{
			match(input,COLUMN,FOLLOW_COLUMN_in_column299); 
			ID10=(Token)match(input,ID,FOLLOW_ID_in_column301); 
			res =new Column((ID10!=null?ID10.getText():null));
			match(input,30,FOLLOW_30_in_column305); 
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:57:46: ( reference )*
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( (LA5_0==ID) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:57:47: reference
					{
					pushFollow(FOLLOW_reference_in_column308);
					reference11=reference();
					state._fsp--;

					res.rows.add(reference11);
					}
					break;

				default :
					break loop5;
				}
			}

			match(input,31,FOLLOW_31_in_column314); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return res;
	}
	// $ANTLR end "column"



	// $ANTLR start "alias"
	// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:58:1: alias returns [Alias res] : ALIAS ID '{' ( ( definition ) | ( reference ) )* '}' ;
	public final Alias alias() throws RecognitionException {
		Alias res = null;


		Token ID12=null;
		Definition definition13 =null;
		Reference reference14 =null;

		try {
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:59:2: ( ALIAS ID '{' ( ( definition ) | ( reference ) )* '}' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:60:3: ALIAS ID '{' ( ( definition ) | ( reference ) )* '}'
			{
			match(input,ALIAS,FOLLOW_ALIAS_in_alias330); 
			ID12=(Token)match(input,ID,FOLLOW_ID_in_alias332); 
			res =new Alias((ID12!=null?ID12.getText():null));
			match(input,30,FOLLOW_30_in_alias336); 
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:61:3: ( ( definition ) | ( reference ) )*
			loop6:
			while (true) {
				int alt6=3;
				int LA6_0 = input.LA(1);
				if ( ((LA6_0 >= READ && LA6_0 <= READWRITE)||LA6_0==WRITE) ) {
					alt6=1;
				}
				else if ( (LA6_0==ID) ) {
					alt6=2;
				}

				switch (alt6) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:62:4: ( definition )
					{
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:62:4: ( definition )
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:62:5: definition
					{
					pushFollow(FOLLOW_definition_in_alias347);
					definition13=definition();
					state._fsp--;

					res.definitions.add(definition13);
					}

					}
					break;
				case 2 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:63:4: ( reference )
					{
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:63:4: ( reference )
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:63:5: reference
					{
					pushFollow(FOLLOW_reference_in_alias359);
					reference14=reference();
					state._fsp--;

					res.definitions.add(reference14);
					}

					}
					break;

				default :
					break loop6;
				}
			}

			match(input,31,FOLLOW_31_in_alias369); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return res;
	}
	// $ANTLR end "alias"



	// $ANTLR start "memory"
	// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:65:1: memory returns [Memory res] : MEMORY '{' ( reference )* '}' ;
	public final Memory memory() throws RecognitionException {
		Memory res = null;


		Reference reference15 =null;

		try {
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:66:2: ( MEMORY '{' ( reference )* '}' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:66:4: MEMORY '{' ( reference )* '}'
			{
			res =new Memory();
			match(input,MEMORY,FOLLOW_MEMORY_in_memory385); 
			match(input,30,FOLLOW_30_in_memory387); 
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:67:14: ( reference )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==ID) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:67:15: reference
					{
					pushFollow(FOLLOW_reference_in_memory390);
					reference15=reference();
					state._fsp--;

					res.references.add(reference15);
					}
					break;

				default :
					break loop7;
				}
			}

			match(input,31,FOLLOW_31_in_memory396); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return res;
	}
	// $ANTLR end "memory"



	// $ANTLR start "definition"
	// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:68:1: definition returns [Definition res] : rwStatus ( 'register' )? type ( '<' width '>' )? ID ( '[' INT ']' )* ( warnType )? ';' ;
	public final Definition definition() throws RecognitionException {
		Definition res = null;


		Token ID19=null;
		Token INT20=null;
		ParserRuleReturnScope rwStatus16 =null;
		ParserRuleReturnScope type17 =null;
		ParserRuleReturnScope width18 =null;
		WarnType warnType21 =null;

		try {
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:69:2: ( rwStatus ( 'register' )? type ( '<' width '>' )? ID ( '[' INT ']' )* ( warnType )? ';' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:69:4: rwStatus ( 'register' )? type ( '<' width '>' )? ID ( '[' INT ']' )* ( warnType )? ';'
			{
			res =new Definition();
			pushFollow(FOLLOW_rwStatus_in_definition412);
			rwStatus16=rwStatus();
			state._fsp--;

			res.rw=RWType.valueOf((rwStatus16!=null?input.toString(rwStatus16.start,rwStatus16.stop):null));
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:71:3: ( 'register' )?
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0==29) ) {
				alt8=1;
			}
			switch (alt8) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:71:4: 'register'
					{
					match(input,29,FOLLOW_29_in_definition419); 
					res.register=true;
					}
					break;

			}

			pushFollow(FOLLOW_type_in_definition427);
			type17=type();
			state._fsp--;

			res.type=Type.valueOf((type17!=null?input.toString(type17.start,type17.stop):null).toUpperCase());
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:73:3: ( '<' width '>' )?
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( (LA9_0==24) ) {
				alt9=1;
			}
			switch (alt9) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:73:4: '<' width '>'
					{
					match(input,24,FOLLOW_24_in_definition435); 
					pushFollow(FOLLOW_width_in_definition437);
					width18=width();
					state._fsp--;

					res.width=Integer.parseInt((width18!=null?input.toString(width18.start,width18.stop):null));
					match(input,26,FOLLOW_26_in_definition441); 
					}
					break;

			}

			ID19=(Token)match(input,ID,FOLLOW_ID_in_definition448); 
			res.name=(ID19!=null?ID19.getText():null);
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:75:3: ( '[' INT ']' )*
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( (LA10_0==27) ) {
					alt10=1;
				}

				switch (alt10) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:75:4: '[' INT ']'
					{
					match(input,27,FOLLOW_27_in_definition456); 
					INT20=(Token)match(input,INT,FOLLOW_INT_in_definition458); 
					match(input,28,FOLLOW_28_in_definition460); 
					res.dimensions.add(Integer.parseInt((INT20!=null?INT20.getText():null)));
					}
					break;

				default :
					break loop10;
				}
			}

			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:76:3: ( warnType )?
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( (LA11_0==ERROR||(LA11_0 >= LIMIT && LA11_0 <= MASK)||LA11_0==SILENT) ) {
				alt11=1;
			}
			switch (alt11) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:76:4: warnType
					{
					pushFollow(FOLLOW_warnType_in_definition470);
					warnType21=warnType();
					state._fsp--;

					res.warn=warnType21;
					}
					break;

			}

			match(input,23,FOLLOW_23_in_definition477); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return res;
	}
	// $ANTLR end "definition"



	// $ANTLR start "warnType"
	// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:78:1: warnType returns [WarnType warn] : ( SILENT )? ( MASK | ERROR | LIMIT ) ;
	public final WarnType warnType() throws RecognitionException {
		WarnType warn = null;


		try {
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:79:2: ( ( SILENT )? ( MASK | ERROR | LIMIT ) )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:79:4: ( SILENT )? ( MASK | ERROR | LIMIT )
			{
			boolean silent=false;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:79:28: ( SILENT )?
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==SILENT) ) {
				alt12=1;
			}
			switch (alt12) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:79:29: SILENT
					{
					match(input,SILENT,FOLLOW_SILENT_in_warnType492); 
					silent=true;
					}
					break;

			}

			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:80:3: ( MASK | ERROR | LIMIT )
			int alt13=3;
			switch ( input.LA(1) ) {
			case MASK:
				{
				alt13=1;
				}
				break;
			case ERROR:
				{
				alt13=2;
				}
				break;
			case LIMIT:
				{
				alt13=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 13, 0, input);
				throw nvae;
			}
			switch (alt13) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:80:4: MASK
					{
					match(input,MASK,FOLLOW_MASK_in_warnType502); 
					warn =silent?WarnType.silentMask:WarnType.mask;
					}
					break;
				case 2 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:81:3: ERROR
					{
					match(input,ERROR,FOLLOW_ERROR_in_warnType508); 
					warn =silent?WarnType.silentError:WarnType.error;
					}
					break;
				case 3 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:82:3: LIMIT
					{
					match(input,LIMIT,FOLLOW_LIMIT_in_warnType514); 
					warn =silent?WarnType.silentLimit:WarnType.limit;
					}
					break;

			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return warn;
	}
	// $ANTLR end "warnType"


	public static class rwStatus_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "rwStatus"
	// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:83:1: rwStatus : ( READ | WRITE | READWRITE ) ;
	public final MemoryModelParser.rwStatus_return rwStatus() throws RecognitionException {
		MemoryModelParser.rwStatus_return retval = new MemoryModelParser.rwStatus_return();
		retval.start = input.LT(1);

		try {
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:83:9: ( ( READ | WRITE | READWRITE ) )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:
			{
			if ( (input.LA(1) >= READ && input.LA(1) <= READWRITE)||input.LA(1)==WRITE ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "rwStatus"


	public static class width_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "width"
	// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:84:1: width : INT ;
	public final MemoryModelParser.width_return width() throws RecognitionException {
		MemoryModelParser.width_return retval = new MemoryModelParser.width_return();
		retval.start = input.LT(1);

		try {
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:84:7: ( INT )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:84:9: INT
			{
			match(input,INT,FOLLOW_INT_in_width539); 
			}

			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "width"


	public static class type_return extends ParserRuleReturnScope {
	};


	// $ANTLR start "type"
	// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:85:1: type : ( INTTYPE | UINTTYPE | BITTYPE ) ;
	public final MemoryModelParser.type_return type() throws RecognitionException {
		MemoryModelParser.type_return retval = new MemoryModelParser.type_return();
		retval.start = input.LT(1);

		try {
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:86:2: ( ( INTTYPE | UINTTYPE | BITTYPE ) )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:
			{
			if ( input.LA(1)==BITTYPE||input.LA(1)==INTTYPE||input.LA(1)==UINTTYPE ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "type"



	// $ANTLR start "reference"
	// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:88:1: reference returns [Reference res] : ID ( '[' INT ']' )* ';' ;
	public final Reference reference() throws RecognitionException {
		Reference res = null;


		Token ID22=null;
		Token INT23=null;

		try {
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:89:2: ( ID ( '[' INT ']' )* ';' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:90:3: ID ( '[' INT ']' )* ';'
			{
			ID22=(Token)match(input,ID,FOLLOW_ID_in_reference575); 
			res =new Reference((ID22!=null?ID22.getText():null));
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:91:3: ( '[' INT ']' )*
			loop14:
			while (true) {
				int alt14=2;
				int LA14_0 = input.LA(1);
				if ( (LA14_0==27) ) {
					alt14=1;
				}

				switch (alt14) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:91:4: '[' INT ']'
					{
					match(input,27,FOLLOW_27_in_reference583); 
					INT23=(Token)match(input,INT,FOLLOW_INT_in_reference585); 
					match(input,28,FOLLOW_28_in_reference587); 
					res.dimensions.add(Integer.parseInt((INT23!=null?INT23.getText():null)));
					}
					break;

				default :
					break loop14;
				}
			}

			match(input,23,FOLLOW_23_in_reference593); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return res;
	}
	// $ANTLR end "reference"

	// Delegated rules



	public static final BitSet FOLLOW_ROMWIDTH_in_unit151 = new BitSet(new long[]{0x0000000002000000L});
	public static final BitSet FOLLOW_25_in_unit153 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_INT_in_unit155 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_23_in_unit159 = new BitSet(new long[]{0x0000000000044050L});
	public static final BitSet FOLLOW_declaration_in_unit167 = new BitSet(new long[]{0x0000000000044050L});
	public static final BitSet FOLLOW_memory_in_unit176 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_in_declaration197 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_column_in_declaration209 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alias_in_declaration221 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ROW_in_row245 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_ID_in_row247 = new BitSet(new long[]{0x0000000040000000L});
	public static final BitSet FOLLOW_30_in_row251 = new BitSet(new long[]{0x0000000080218200L});
	public static final BitSet FOLLOW_definition_in_row262 = new BitSet(new long[]{0x0000000080218200L});
	public static final BitSet FOLLOW_reference_in_row274 = new BitSet(new long[]{0x0000000080218200L});
	public static final BitSet FOLLOW_31_in_row284 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COLUMN_in_column299 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_ID_in_column301 = new BitSet(new long[]{0x0000000040000000L});
	public static final BitSet FOLLOW_30_in_column305 = new BitSet(new long[]{0x0000000080000200L});
	public static final BitSet FOLLOW_reference_in_column308 = new BitSet(new long[]{0x0000000080000200L});
	public static final BitSet FOLLOW_31_in_column314 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ALIAS_in_alias330 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_ID_in_alias332 = new BitSet(new long[]{0x0000000040000000L});
	public static final BitSet FOLLOW_30_in_alias336 = new BitSet(new long[]{0x0000000080218200L});
	public static final BitSet FOLLOW_definition_in_alias347 = new BitSet(new long[]{0x0000000080218200L});
	public static final BitSet FOLLOW_reference_in_alias359 = new BitSet(new long[]{0x0000000080218200L});
	public static final BitSet FOLLOW_31_in_alias369 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MEMORY_in_memory385 = new BitSet(new long[]{0x0000000040000000L});
	public static final BitSet FOLLOW_30_in_memory387 = new BitSet(new long[]{0x0000000080000200L});
	public static final BitSet FOLLOW_reference_in_memory390 = new BitSet(new long[]{0x0000000080000200L});
	public static final BitSet FOLLOW_31_in_memory396 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_rwStatus_in_definition412 = new BitSet(new long[]{0x0000000020100820L});
	public static final BitSet FOLLOW_29_in_definition419 = new BitSet(new long[]{0x0000000000100820L});
	public static final BitSet FOLLOW_type_in_definition427 = new BitSet(new long[]{0x0000000001000200L});
	public static final BitSet FOLLOW_24_in_definition435 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_width_in_definition437 = new BitSet(new long[]{0x0000000004000000L});
	public static final BitSet FOLLOW_26_in_definition441 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_ID_in_definition448 = new BitSet(new long[]{0x0000000008883100L});
	public static final BitSet FOLLOW_27_in_definition456 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_INT_in_definition458 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_28_in_definition460 = new BitSet(new long[]{0x0000000008883100L});
	public static final BitSet FOLLOW_warnType_in_definition470 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_23_in_definition477 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SILENT_in_warnType492 = new BitSet(new long[]{0x0000000000003100L});
	public static final BitSet FOLLOW_MASK_in_warnType502 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ERROR_in_warnType508 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LIMIT_in_warnType514 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_width539 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_reference575 = new BitSet(new long[]{0x0000000008800000L});
	public static final BitSet FOLLOW_27_in_reference583 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_INT_in_reference585 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_28_in_reference587 = new BitSet(new long[]{0x0000000008800000L});
	public static final BitSet FOLLOW_23_in_reference593 = new BitSet(new long[]{0x0000000000000002L});
}

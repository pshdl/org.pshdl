// $ANTLR 3.4 /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g 2012-10-27 22:50:26

package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;
import java.util.LinkedHashMap;
import java.util.Map;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.RWType;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.Type;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class MemoryModelParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALIAS", "BITTYPE", "COLUMN", "COMMENT", "ID", "INT", "INTTYPE", "MEMORY", "READ", "READWRITE", "ROMWIDTH", "ROW", "UINTTYPE", "WRITE", "WS", "';'", "'<'", "'='", "'>'", "'['", "']'", "'register'", "'{'", "'}'"
    };

    public static final int EOF=-1;
    public static final int T__19=19;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int T__22=22;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int ALIAS=4;
    public static final int BITTYPE=5;
    public static final int COLUMN=6;
    public static final int COMMENT=7;
    public static final int ID=8;
    public static final int INT=9;
    public static final int INTTYPE=10;
    public static final int MEMORY=11;
    public static final int READ=12;
    public static final int READWRITE=13;
    public static final int ROMWIDTH=14;
    public static final int ROW=15;
    public static final int UINTTYPE=16;
    public static final int WRITE=17;
    public static final int WS=18;

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

    public String[] getTokenNames() { return MemoryModelParser.tokenNames; }
    public String getGrammarFileName() { return "/Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g"; }


    	



    // $ANTLR start "unit"
    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:33:1: unit returns [Unit res] : ( ROMWIDTH '=' INT ';' )? ( declaration )* ( memory ) ;
    public final Unit unit() throws RecognitionException {
        Unit res = null;


        Token INT1=null;
        NamedElement declaration2 =null;

        Memory memory3 =null;


        try {
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:34:2: ( ( ROMWIDTH '=' INT ';' )? ( declaration )* ( memory ) )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:34:4: ( ROMWIDTH '=' INT ';' )? ( declaration )* ( memory )
            {
            res =new Unit();

            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:35:3: ( ROMWIDTH '=' INT ';' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==ROMWIDTH) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:35:4: ROMWIDTH '=' INT ';'
                    {
                    match(input,ROMWIDTH,FOLLOW_ROMWIDTH_in_unit127); 

                    match(input,21,FOLLOW_21_in_unit129); 

                    INT1=(Token)match(input,INT,FOLLOW_INT_in_unit131); 

                    res.rowWidth=Integer.parseInt((INT1!=null?INT1.getText():null));

                    match(input,19,FOLLOW_19_in_unit135); 

                    }
                    break;

            }


            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:36:3: ( declaration )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==ALIAS||LA2_0==COLUMN||LA2_0==ROW) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:36:4: declaration
            	    {
            	    pushFollow(FOLLOW_declaration_in_unit143);
            	    declaration2=declaration();

            	    state._fsp--;


            	    res.declarations.put(declaration2.getName(), declaration2);

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:37:3: ( memory )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:37:4: memory
            {
            pushFollow(FOLLOW_memory_in_unit152);
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
    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:38:1: declaration returns [NamedElement decl] : ( ( row ) | ( column ) | ( alias ) ) ;
    public final NamedElement declaration() throws RecognitionException {
        NamedElement decl = null;


        Row row4 =null;

        Column column5 =null;

        Alias alias6 =null;


        try {
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:39:2: ( ( ( row ) | ( column ) | ( alias ) ) )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:39:4: ( ( row ) | ( column ) | ( alias ) )
            {
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:39:4: ( ( row ) | ( column ) | ( alias ) )
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
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:40:4: ( row )
                    {
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:40:4: ( row )
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:40:5: row
                    {
                    pushFollow(FOLLOW_row_in_declaration173);
                    row4=row();

                    state._fsp--;


                    decl =row4;

                    }


                    }
                    break;
                case 2 :
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:41:4: ( column )
                    {
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:41:4: ( column )
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:41:5: column
                    {
                    pushFollow(FOLLOW_column_in_declaration185);
                    column5=column();

                    state._fsp--;


                    decl =column5;

                    }


                    }
                    break;
                case 3 :
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:42:4: ( alias )
                    {
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:42:4: ( alias )
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:42:5: alias
                    {
                    pushFollow(FOLLOW_alias_in_declaration197);
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
    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:45:1: row returns [Row res] : ROW ID '{' ( ( definition ) | ( reference ) )* '}' ;
    public final Row row() throws RecognitionException {
        Row res = null;


        Token ID7=null;
        Definition definition8 =null;

        Reference reference9 =null;


        try {
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:46:2: ( ROW ID '{' ( ( definition ) | ( reference ) )* '}' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:47:3: ROW ID '{' ( ( definition ) | ( reference ) )* '}'
            {
            match(input,ROW,FOLLOW_ROW_in_row221); 

            ID7=(Token)match(input,ID,FOLLOW_ID_in_row223); 

            res =new Row((ID7!=null?ID7.getText():null));

            match(input,26,FOLLOW_26_in_row227); 

            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:48:3: ( ( definition ) | ( reference ) )*
            loop4:
            do {
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
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:49:4: ( definition )
            	    {
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:49:4: ( definition )
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:49:5: definition
            	    {
            	    pushFollow(FOLLOW_definition_in_row238);
            	    definition8=definition();

            	    state._fsp--;


            	    res.definitions.add(definition8);

            	    }


            	    }
            	    break;
            	case 2 :
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:50:4: ( reference )
            	    {
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:50:4: ( reference )
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:50:5: reference
            	    {
            	    pushFollow(FOLLOW_reference_in_row250);
            	    reference9=reference();

            	    state._fsp--;


            	    res.definitions.add(reference9);

            	    }


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            match(input,27,FOLLOW_27_in_row260); 

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
    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:52:1: column returns [Column res] : COLUMN ID '{' ( reference )* '}' ;
    public final Column column() throws RecognitionException {
        Column res = null;


        Token ID10=null;
        Reference reference11 =null;


        try {
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:53:2: ( COLUMN ID '{' ( reference )* '}' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:54:3: COLUMN ID '{' ( reference )* '}'
            {
            match(input,COLUMN,FOLLOW_COLUMN_in_column275); 

            ID10=(Token)match(input,ID,FOLLOW_ID_in_column277); 

            res =new Column((ID10!=null?ID10.getText():null));

            match(input,26,FOLLOW_26_in_column281); 

            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:54:46: ( reference )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==ID) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:54:47: reference
            	    {
            	    pushFollow(FOLLOW_reference_in_column284);
            	    reference11=reference();

            	    state._fsp--;


            	    res.rows.add(reference11);

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            match(input,27,FOLLOW_27_in_column290); 

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
    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:55:1: alias returns [Alias res] : ALIAS ID '{' ( ( definition ) | ( reference ) )* '}' ;
    public final Alias alias() throws RecognitionException {
        Alias res = null;


        Token ID12=null;
        Definition definition13 =null;

        Reference reference14 =null;


        try {
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:56:2: ( ALIAS ID '{' ( ( definition ) | ( reference ) )* '}' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:57:3: ALIAS ID '{' ( ( definition ) | ( reference ) )* '}'
            {
            match(input,ALIAS,FOLLOW_ALIAS_in_alias306); 

            ID12=(Token)match(input,ID,FOLLOW_ID_in_alias308); 

            res =new Alias((ID12!=null?ID12.getText():null));

            match(input,26,FOLLOW_26_in_alias312); 

            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:58:3: ( ( definition ) | ( reference ) )*
            loop6:
            do {
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
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:59:4: ( definition )
            	    {
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:59:4: ( definition )
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:59:5: definition
            	    {
            	    pushFollow(FOLLOW_definition_in_alias323);
            	    definition13=definition();

            	    state._fsp--;


            	    res.definitions.add(definition13);

            	    }


            	    }
            	    break;
            	case 2 :
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:60:4: ( reference )
            	    {
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:60:4: ( reference )
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:60:5: reference
            	    {
            	    pushFollow(FOLLOW_reference_in_alias335);
            	    reference14=reference();

            	    state._fsp--;


            	    res.definitions.add(reference14);

            	    }


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            match(input,27,FOLLOW_27_in_alias345); 

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
    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:62:1: memory returns [Memory res] : MEMORY '{' ( reference )* '}' ;
    public final Memory memory() throws RecognitionException {
        Memory res = null;


        Reference reference15 =null;


        try {
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:63:2: ( MEMORY '{' ( reference )* '}' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:63:4: MEMORY '{' ( reference )* '}'
            {
            res =new Memory();

            match(input,MEMORY,FOLLOW_MEMORY_in_memory361); 

            match(input,26,FOLLOW_26_in_memory363); 

            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:64:14: ( reference )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==ID) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:64:15: reference
            	    {
            	    pushFollow(FOLLOW_reference_in_memory366);
            	    reference15=reference();

            	    state._fsp--;


            	    res.references.add(reference15);

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            match(input,27,FOLLOW_27_in_memory372); 

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
    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:65:1: definition returns [Definition res] : rwStatus ( 'register' )? type ( '<' width '>' )? ID ( '[' INT ']' )* ';' ;
    public final Definition definition() throws RecognitionException {
        Definition res = null;


        Token ID19=null;
        Token INT20=null;
        MemoryModelParser.rwStatus_return rwStatus16 =null;

        MemoryModelParser.type_return type17 =null;

        MemoryModelParser.width_return width18 =null;


        try {
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:66:2: ( rwStatus ( 'register' )? type ( '<' width '>' )? ID ( '[' INT ']' )* ';' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:66:4: rwStatus ( 'register' )? type ( '<' width '>' )? ID ( '[' INT ']' )* ';'
            {
            res =new Definition();

            pushFollow(FOLLOW_rwStatus_in_definition388);
            rwStatus16=rwStatus();

            state._fsp--;


            res.rw=RWType.valueOf((rwStatus16!=null?input.toString(rwStatus16.start,rwStatus16.stop):null));

            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:68:3: ( 'register' )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==25) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:68:4: 'register'
                    {
                    match(input,25,FOLLOW_25_in_definition395); 

                    res.register=true;

                    }
                    break;

            }


            pushFollow(FOLLOW_type_in_definition403);
            type17=type();

            state._fsp--;


            res.type=Type.valueOf((type17!=null?input.toString(type17.start,type17.stop):null).toUpperCase());

            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:70:3: ( '<' width '>' )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==20) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:70:4: '<' width '>'
                    {
                    match(input,20,FOLLOW_20_in_definition411); 

                    pushFollow(FOLLOW_width_in_definition413);
                    width18=width();

                    state._fsp--;


                    res.width=Integer.parseInt((width18!=null?input.toString(width18.start,width18.stop):null));

                    match(input,22,FOLLOW_22_in_definition417); 

                    }
                    break;

            }


            ID19=(Token)match(input,ID,FOLLOW_ID_in_definition424); 

            res.name=(ID19!=null?ID19.getText():null);

            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:72:3: ( '[' INT ']' )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==23) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:72:4: '[' INT ']'
            	    {
            	    match(input,23,FOLLOW_23_in_definition432); 

            	    INT20=(Token)match(input,INT,FOLLOW_INT_in_definition434); 

            	    match(input,24,FOLLOW_24_in_definition436); 

            	    res.dimensions.add(Integer.parseInt((INT20!=null?INT20.getText():null)));

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            match(input,19,FOLLOW_19_in_definition442); 

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


    public static class rwStatus_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "rwStatus"
    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:73:1: rwStatus : ( READ | WRITE | READWRITE ) ;
    public final MemoryModelParser.rwStatus_return rwStatus() throws RecognitionException {
        MemoryModelParser.rwStatus_return retval = new MemoryModelParser.rwStatus_return();
        retval.start = input.LT(1);


        try {
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:73:9: ( ( READ | WRITE | READWRITE ) )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:
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
    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:74:1: width : INT ;
    public final MemoryModelParser.width_return width() throws RecognitionException {
        MemoryModelParser.width_return retval = new MemoryModelParser.width_return();
        retval.start = input.LT(1);


        try {
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:74:7: ( INT )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:74:9: INT
            {
            match(input,INT,FOLLOW_INT_in_width465); 

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
    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:75:1: type : ( INTTYPE | UINTTYPE | BITTYPE ) ;
    public final MemoryModelParser.type_return type() throws RecognitionException {
        MemoryModelParser.type_return retval = new MemoryModelParser.type_return();
        retval.start = input.LT(1);


        try {
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:76:2: ( ( INTTYPE | UINTTYPE | BITTYPE ) )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:
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
    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:78:1: reference returns [Reference res] : ID ( '[' INT ']' )* ';' ;
    public final Reference reference() throws RecognitionException {
        Reference res = null;


        Token ID21=null;
        Token INT22=null;

        try {
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:79:2: ( ID ( '[' INT ']' )* ';' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:80:3: ID ( '[' INT ']' )* ';'
            {
            ID21=(Token)match(input,ID,FOLLOW_ID_in_reference501); 

            res =new Reference((ID21!=null?ID21.getText():null));

            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:81:3: ( '[' INT ']' )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==23) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:81:4: '[' INT ']'
            	    {
            	    match(input,23,FOLLOW_23_in_reference509); 

            	    INT22=(Token)match(input,INT,FOLLOW_INT_in_reference511); 

            	    match(input,24,FOLLOW_24_in_reference513); 

            	    res.dimensions.add(Integer.parseInt((INT22!=null?INT22.getText():null)));

            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            match(input,19,FOLLOW_19_in_reference519); 

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


 

    public static final BitSet FOLLOW_ROMWIDTH_in_unit127 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_unit129 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_INT_in_unit131 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_unit135 = new BitSet(new long[]{0x0000000000008850L});
    public static final BitSet FOLLOW_declaration_in_unit143 = new BitSet(new long[]{0x0000000000008850L});
    public static final BitSet FOLLOW_memory_in_unit152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_in_declaration173 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_column_in_declaration185 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_alias_in_declaration197 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ROW_in_row221 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_row223 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_row227 = new BitSet(new long[]{0x0000000008023100L});
    public static final BitSet FOLLOW_definition_in_row238 = new BitSet(new long[]{0x0000000008023100L});
    public static final BitSet FOLLOW_reference_in_row250 = new BitSet(new long[]{0x0000000008023100L});
    public static final BitSet FOLLOW_27_in_row260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COLUMN_in_column275 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_column277 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_column281 = new BitSet(new long[]{0x0000000008000100L});
    public static final BitSet FOLLOW_reference_in_column284 = new BitSet(new long[]{0x0000000008000100L});
    public static final BitSet FOLLOW_27_in_column290 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALIAS_in_alias306 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_alias308 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_alias312 = new BitSet(new long[]{0x0000000008023100L});
    public static final BitSet FOLLOW_definition_in_alias323 = new BitSet(new long[]{0x0000000008023100L});
    public static final BitSet FOLLOW_reference_in_alias335 = new BitSet(new long[]{0x0000000008023100L});
    public static final BitSet FOLLOW_27_in_alias345 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MEMORY_in_memory361 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_memory363 = new BitSet(new long[]{0x0000000008000100L});
    public static final BitSet FOLLOW_reference_in_memory366 = new BitSet(new long[]{0x0000000008000100L});
    public static final BitSet FOLLOW_27_in_memory372 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rwStatus_in_definition388 = new BitSet(new long[]{0x0000000002010420L});
    public static final BitSet FOLLOW_25_in_definition395 = new BitSet(new long[]{0x0000000000010420L});
    public static final BitSet FOLLOW_type_in_definition403 = new BitSet(new long[]{0x0000000000100100L});
    public static final BitSet FOLLOW_20_in_definition411 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_width_in_definition413 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_22_in_definition417 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_definition424 = new BitSet(new long[]{0x0000000000880000L});
    public static final BitSet FOLLOW_23_in_definition432 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_INT_in_definition434 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_definition436 = new BitSet(new long[]{0x0000000000880000L});
    public static final BitSet FOLLOW_19_in_definition442 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_width465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_reference501 = new BitSet(new long[]{0x0000000000880000L});
    public static final BitSet FOLLOW_23_in_reference509 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_INT_in_reference511 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_reference513 = new BitSet(new long[]{0x0000000000880000L});
    public static final BitSet FOLLOW_19_in_reference519 = new BitSet(new long[]{0x0000000000000002L});

}
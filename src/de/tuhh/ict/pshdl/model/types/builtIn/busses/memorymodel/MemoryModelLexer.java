// $ANTLR 3.4 /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g 2012-10-27 22:50:26

package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class MemoryModelLexer extends Lexer {
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
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public MemoryModelLexer() {} 
    public MemoryModelLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public MemoryModelLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g"; }

    // $ANTLR start "ALIAS"
    public final void mALIAS() throws RecognitionException {
        try {
            int _type = ALIAS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:6:7: ( 'alias' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:6:9: 'alias'
            {
            match("alias"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ALIAS"

    // $ANTLR start "BITTYPE"
    public final void mBITTYPE() throws RecognitionException {
        try {
            int _type = BITTYPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:7:9: ( 'bit' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:7:11: 'bit'
            {
            match("bit"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "BITTYPE"

    // $ANTLR start "COLUMN"
    public final void mCOLUMN() throws RecognitionException {
        try {
            int _type = COLUMN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:8:8: ( 'column' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:8:10: 'column'
            {
            match("column"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COLUMN"

    // $ANTLR start "INTTYPE"
    public final void mINTTYPE() throws RecognitionException {
        try {
            int _type = INTTYPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:9:9: ( 'int' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:9:11: 'int'
            {
            match("int"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INTTYPE"

    // $ANTLR start "MEMORY"
    public final void mMEMORY() throws RecognitionException {
        try {
            int _type = MEMORY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:10:8: ( 'memory' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:10:10: 'memory'
            {
            match("memory"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MEMORY"

    // $ANTLR start "READ"
    public final void mREAD() throws RecognitionException {
        try {
            int _type = READ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:11:6: ( 'r' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:11:8: 'r'
            {
            match('r'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "READ"

    // $ANTLR start "READWRITE"
    public final void mREADWRITE() throws RecognitionException {
        try {
            int _type = READWRITE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:12:11: ( 'rw' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:12:13: 'rw'
            {
            match("rw"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "READWRITE"

    // $ANTLR start "ROMWIDTH"
    public final void mROMWIDTH() throws RecognitionException {
        try {
            int _type = ROMWIDTH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:13:10: ( 'rowWidth' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:13:12: 'rowWidth'
            {
            match("rowWidth"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ROMWIDTH"

    // $ANTLR start "ROW"
    public final void mROW() throws RecognitionException {
        try {
            int _type = ROW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:14:5: ( 'row' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:14:7: 'row'
            {
            match("row"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ROW"

    // $ANTLR start "UINTTYPE"
    public final void mUINTTYPE() throws RecognitionException {
        try {
            int _type = UINTTYPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:15:10: ( 'uint' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:15:12: 'uint'
            {
            match("uint"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UINTTYPE"

    // $ANTLR start "WRITE"
    public final void mWRITE() throws RecognitionException {
        try {
            int _type = WRITE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:16:7: ( 'w' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:16:9: 'w'
            {
            match('w'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WRITE"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:17:7: ( ';' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:17:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:18:7: ( '<' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:18:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:19:7: ( '=' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:19:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:20:7: ( '>' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:20:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:21:7: ( '[' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:21:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:22:7: ( ']' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:22:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:23:7: ( 'register' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:23:9: 'register'
            {
            match("register"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:24:7: ( '{' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:24:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:25:7: ( '}' )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:25:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:83:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:83:7: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:83:31: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= '0' && LA1_0 <= '9')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:86:5: ( ( '0' .. '9' )+ )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:86:7: ( '0' .. '9' )+
            {
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:86:7: ( '0' .. '9' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:90:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '/*' ( options {greedy=false; } : . )* '*/' )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='/') ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1=='/') ) {
                    alt6=1;
                }
                else if ( (LA6_1=='*') ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:90:9: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
                    {
                    match("//"); 



                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:90:14: (~ ( '\\n' | '\\r' ) )*
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( ((LA3_0 >= '\u0000' && LA3_0 <= '\t')||(LA3_0 >= '\u000B' && LA3_0 <= '\f')||(LA3_0 >= '\u000E' && LA3_0 <= '\uFFFF')) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:
                    	    {
                    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop3;
                        }
                    } while (true);


                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:90:28: ( '\\r' )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0=='\r') ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:90:28: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }


                    match('\n'); 

                    _channel=HIDDEN;

                    }
                    break;
                case 2 :
                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:91:9: '/*' ( options {greedy=false; } : . )* '*/'
                    {
                    match("/*"); 



                    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:91:14: ( options {greedy=false; } : . )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0=='*') ) {
                            int LA5_1 = input.LA(2);

                            if ( (LA5_1=='/') ) {
                                alt5=2;
                            }
                            else if ( ((LA5_1 >= '\u0000' && LA5_1 <= '.')||(LA5_1 >= '0' && LA5_1 <= '\uFFFF')) ) {
                                alt5=1;
                            }


                        }
                        else if ( ((LA5_0 >= '\u0000' && LA5_0 <= ')')||(LA5_0 >= '+' && LA5_0 <= '\uFFFF')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:91:42: .
                    	    {
                    	    matchAny(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);


                    match("*/"); 



                    _channel=HIDDEN;

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:94:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:94:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:8: ( ALIAS | BITTYPE | COLUMN | INTTYPE | MEMORY | READ | READWRITE | ROMWIDTH | ROW | UINTTYPE | WRITE | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | ID | INT | COMMENT | WS )
        int alt7=24;
        alt7 = dfa7.predict(input);
        switch (alt7) {
            case 1 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:10: ALIAS
                {
                mALIAS(); 


                }
                break;
            case 2 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:16: BITTYPE
                {
                mBITTYPE(); 


                }
                break;
            case 3 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:24: COLUMN
                {
                mCOLUMN(); 


                }
                break;
            case 4 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:31: INTTYPE
                {
                mINTTYPE(); 


                }
                break;
            case 5 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:39: MEMORY
                {
                mMEMORY(); 


                }
                break;
            case 6 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:46: READ
                {
                mREAD(); 


                }
                break;
            case 7 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:51: READWRITE
                {
                mREADWRITE(); 


                }
                break;
            case 8 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:61: ROMWIDTH
                {
                mROMWIDTH(); 


                }
                break;
            case 9 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:70: ROW
                {
                mROW(); 


                }
                break;
            case 10 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:74: UINTTYPE
                {
                mUINTTYPE(); 


                }
                break;
            case 11 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:83: WRITE
                {
                mWRITE(); 


                }
                break;
            case 12 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:89: T__19
                {
                mT__19(); 


                }
                break;
            case 13 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:95: T__20
                {
                mT__20(); 


                }
                break;
            case 14 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:101: T__21
                {
                mT__21(); 


                }
                break;
            case 15 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:107: T__22
                {
                mT__22(); 


                }
                break;
            case 16 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:113: T__23
                {
                mT__23(); 


                }
                break;
            case 17 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:119: T__24
                {
                mT__24(); 


                }
                break;
            case 18 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:125: T__25
                {
                mT__25(); 


                }
                break;
            case 19 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:131: T__26
                {
                mT__26(); 


                }
                break;
            case 20 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:137: T__27
                {
                mT__27(); 


                }
                break;
            case 21 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:143: ID
                {
                mID(); 


                }
                break;
            case 22 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:146: INT
                {
                mINT(); 


                }
                break;
            case 23 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:150: COMMENT
                {
                mCOMMENT(); 


                }
                break;
            case 24 :
                // /Volumes/Macintosh HD/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:158: WS
                {
                mWS(); 


                }
                break;

        }

    }


    protected DFA7 dfa7 = new DFA7(this);
    static final String DFA7_eotS =
        "\1\uffff\5\21\1\35\1\21\1\37\14\uffff\5\21\1\45\2\21\1\uffff\1\21"+
        "\1\uffff\1\21\1\52\1\21\1\54\1\21\1\uffff\1\57\3\21\1\uffff\1\21"+
        "\1\uffff\2\21\1\uffff\1\21\1\67\1\70\4\21\2\uffff\1\75\1\76\2\21"+
        "\2\uffff\2\21\1\103\1\104\2\uffff";
    static final String DFA7_eofS =
        "\105\uffff";
    static final String DFA7_minS =
        "\1\11\1\154\1\151\1\157\1\156\1\145\1\60\1\151\1\60\14\uffff\1\151"+
        "\1\164\1\154\1\164\1\155\1\60\1\167\1\147\1\uffff\1\156\1\uffff"+
        "\1\141\1\60\1\165\1\60\1\157\1\uffff\1\60\1\151\1\164\1\163\1\uffff"+
        "\1\155\1\uffff\1\162\1\151\1\uffff\1\163\2\60\1\156\1\171\1\144"+
        "\1\164\2\uffff\2\60\1\164\1\145\2\uffff\1\150\1\162\2\60\2\uffff";
    static final String DFA7_maxS =
        "\1\175\1\154\1\151\1\157\1\156\1\145\1\172\1\151\1\172\14\uffff"+
        "\1\151\1\164\1\154\1\164\1\155\1\172\1\167\1\147\1\uffff\1\156\1"+
        "\uffff\1\141\1\172\1\165\1\172\1\157\1\uffff\1\172\1\151\1\164\1"+
        "\163\1\uffff\1\155\1\uffff\1\162\1\151\1\uffff\1\163\2\172\1\156"+
        "\1\171\1\144\1\164\2\uffff\2\172\1\164\1\145\2\uffff\1\150\1\162"+
        "\2\172\2\uffff";
    static final String DFA7_acceptS =
        "\11\uffff\1\14\1\15\1\16\1\17\1\20\1\21\1\23\1\24\1\25\1\26\1\27"+
        "\1\30\10\uffff\1\6\1\uffff\1\13\5\uffff\1\7\4\uffff\1\2\1\uffff"+
        "\1\4\2\uffff\1\11\7\uffff\1\12\1\1\4\uffff\1\3\1\5\4\uffff\1\10"+
        "\1\22";
    static final String DFA7_specialS =
        "\105\uffff}>";
    static final String[] DFA7_transitionS = {
            "\2\24\2\uffff\1\24\22\uffff\1\24\16\uffff\1\23\12\22\1\uffff"+
            "\1\11\1\12\1\13\1\14\2\uffff\32\21\1\15\1\uffff\1\16\1\uffff"+
            "\1\21\1\uffff\1\1\1\2\1\3\5\21\1\4\3\21\1\5\4\21\1\6\2\21\1"+
            "\7\1\21\1\10\3\21\1\17\1\uffff\1\20",
            "\1\25",
            "\1\26",
            "\1\27",
            "\1\30",
            "\1\31",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\4\21\1\34\11\21\1"+
            "\33\7\21\1\32\3\21",
            "\1\36",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\40",
            "\1\41",
            "\1\42",
            "\1\43",
            "\1\44",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\46",
            "\1\47",
            "",
            "\1\50",
            "",
            "\1\51",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\53",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\55",
            "",
            "\12\21\7\uffff\26\21\1\56\3\21\4\uffff\1\21\1\uffff\32\21",
            "\1\60",
            "\1\61",
            "\1\62",
            "",
            "\1\63",
            "",
            "\1\64",
            "\1\65",
            "",
            "\1\66",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "",
            "",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\77",
            "\1\100",
            "",
            "",
            "\1\101",
            "\1\102",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( ALIAS | BITTYPE | COLUMN | INTTYPE | MEMORY | READ | READWRITE | ROMWIDTH | ROW | UINTTYPE | WRITE | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | ID | INT | COMMENT | WS );";
        }
    }
 

}
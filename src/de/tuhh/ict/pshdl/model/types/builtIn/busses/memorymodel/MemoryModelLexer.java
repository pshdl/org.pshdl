// $ANTLR 3.5 /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g 2013-01-23 11:37:02

package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MemoryModelLexer extends Lexer {
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
	@Override public String getGrammarFileName() { return "/Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g"; }

	// $ANTLR start "ALIAS"
	public final void mALIAS() throws RecognitionException {
		try {
			int _type = ALIAS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:6:7: ( 'alias' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:6:9: 'alias'
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
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:7:9: ( 'bit' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:7:11: 'bit'
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
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:8:8: ( 'column' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:8:10: 'column'
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

	// $ANTLR start "ERROR"
	public final void mERROR() throws RecognitionException {
		try {
			int _type = ERROR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:9:7: ( 'error' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:9:9: 'error'
			{
			match("error"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ERROR"

	// $ANTLR start "INTTYPE"
	public final void mINTTYPE() throws RecognitionException {
		try {
			int _type = INTTYPE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:10:9: ( 'int' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:10:11: 'int'
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

	// $ANTLR start "LIMIT"
	public final void mLIMIT() throws RecognitionException {
		try {
			int _type = LIMIT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:11:7: ( 'limit' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:11:9: 'limit'
			{
			match("limit"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LIMIT"

	// $ANTLR start "MASK"
	public final void mMASK() throws RecognitionException {
		try {
			int _type = MASK;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:12:6: ( 'mask' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:12:8: 'mask'
			{
			match("mask"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MASK"

	// $ANTLR start "MEMORY"
	public final void mMEMORY() throws RecognitionException {
		try {
			int _type = MEMORY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:13:8: ( 'memory' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:13:10: 'memory'
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
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:14:6: ( 'r' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:14:8: 'r'
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
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:15:11: ( 'rw' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:15:13: 'rw'
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
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:16:10: ( 'rowWidth' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:16:12: 'rowWidth'
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
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:17:5: ( 'row' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:17:7: 'row'
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

	// $ANTLR start "SILENT"
	public final void mSILENT() throws RecognitionException {
		try {
			int _type = SILENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:18:8: ( 'silent' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:18:10: 'silent'
			{
			match("silent"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SILENT"

	// $ANTLR start "UINTTYPE"
	public final void mUINTTYPE() throws RecognitionException {
		try {
			int _type = UINTTYPE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:19:10: ( 'uint' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:19:12: 'uint'
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
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:20:7: ( 'w' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:20:9: 'w'
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

	// $ANTLR start "T__23"
	public final void mT__23() throws RecognitionException {
		try {
			int _type = T__23;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:21:7: ( ';' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:21:9: ';'
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
	// $ANTLR end "T__23"

	// $ANTLR start "T__24"
	public final void mT__24() throws RecognitionException {
		try {
			int _type = T__24;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:22:7: ( '<' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:22:9: '<'
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
	// $ANTLR end "T__24"

	// $ANTLR start "T__25"
	public final void mT__25() throws RecognitionException {
		try {
			int _type = T__25;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:23:7: ( '=' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:23:9: '='
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
	// $ANTLR end "T__25"

	// $ANTLR start "T__26"
	public final void mT__26() throws RecognitionException {
		try {
			int _type = T__26;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:24:7: ( '>' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:24:9: '>'
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
	// $ANTLR end "T__26"

	// $ANTLR start "T__27"
	public final void mT__27() throws RecognitionException {
		try {
			int _type = T__27;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:25:7: ( '[' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:25:9: '['
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
	// $ANTLR end "T__27"

	// $ANTLR start "T__28"
	public final void mT__28() throws RecognitionException {
		try {
			int _type = T__28;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:26:7: ( ']' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:26:9: ']'
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
	// $ANTLR end "T__28"

	// $ANTLR start "T__29"
	public final void mT__29() throws RecognitionException {
		try {
			int _type = T__29;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:27:7: ( 'register' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:27:9: 'register'
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
	// $ANTLR end "T__29"

	// $ANTLR start "T__30"
	public final void mT__30() throws RecognitionException {
		try {
			int _type = T__30;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:28:7: ( '{' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:28:9: '{'
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
	// $ANTLR end "T__30"

	// $ANTLR start "T__31"
	public final void mT__31() throws RecognitionException {
		try {
			int _type = T__31;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:29:7: ( '}' )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:29:9: '}'
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
	// $ANTLR end "T__31"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:93:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:93:7: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:93:31: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '0' && LA1_0 <= '9')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:
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
			}

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
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:96:5: ( ( '0' .. '9' )+ )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:96:7: ( '0' .. '9' )+
			{
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:96:7: ( '0' .. '9' )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:
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
					EarlyExitException eee = new EarlyExitException(2, input);
					throw eee;
				}
				cnt2++;
			}

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
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:100:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '/*' ( options {greedy=false; } : . )* '*/' )
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
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 6, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 6, 0, input);
				throw nvae;
			}

			switch (alt6) {
				case 1 :
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:100:9: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
					{
					match("//"); 

					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:100:14: (~ ( '\\n' | '\\r' ) )*
					loop3:
					while (true) {
						int alt3=2;
						int LA3_0 = input.LA(1);
						if ( ((LA3_0 >= '\u0000' && LA3_0 <= '\t')||(LA3_0 >= '\u000B' && LA3_0 <= '\f')||(LA3_0 >= '\u000E' && LA3_0 <= '\uFFFF')) ) {
							alt3=1;
						}

						switch (alt3) {
						case 1 :
							// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:
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
					}

					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:100:28: ( '\\r' )?
					int alt4=2;
					int LA4_0 = input.LA(1);
					if ( (LA4_0=='\r') ) {
						alt4=1;
					}
					switch (alt4) {
						case 1 :
							// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:100:28: '\\r'
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
					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:101:9: '/*' ( options {greedy=false; } : . )* '*/'
					{
					match("/*"); 

					// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:101:14: ( options {greedy=false; } : . )*
					loop5:
					while (true) {
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
							// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:101:42: .
							{
							matchAny(); 
							}
							break;

						default :
							break loop5;
						}
					}

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
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:104:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
			// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:104:9: ( ' ' | '\\t' | '\\r' | '\\n' )
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

	@Override
	public void mTokens() throws RecognitionException {
		// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:8: ( ALIAS | BITTYPE | COLUMN | ERROR | INTTYPE | LIMIT | MASK | MEMORY | READ | READWRITE | ROMWIDTH | ROW | SILENT | UINTTYPE | WRITE | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | ID | INT | COMMENT | WS )
		int alt7=28;
		alt7 = dfa7.predict(input);
		switch (alt7) {
			case 1 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:10: ALIAS
				{
				mALIAS(); 

				}
				break;
			case 2 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:16: BITTYPE
				{
				mBITTYPE(); 

				}
				break;
			case 3 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:24: COLUMN
				{
				mCOLUMN(); 

				}
				break;
			case 4 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:31: ERROR
				{
				mERROR(); 

				}
				break;
			case 5 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:37: INTTYPE
				{
				mINTTYPE(); 

				}
				break;
			case 6 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:45: LIMIT
				{
				mLIMIT(); 

				}
				break;
			case 7 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:51: MASK
				{
				mMASK(); 

				}
				break;
			case 8 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:56: MEMORY
				{
				mMEMORY(); 

				}
				break;
			case 9 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:63: READ
				{
				mREAD(); 

				}
				break;
			case 10 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:68: READWRITE
				{
				mREADWRITE(); 

				}
				break;
			case 11 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:78: ROMWIDTH
				{
				mROMWIDTH(); 

				}
				break;
			case 12 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:87: ROW
				{
				mROW(); 

				}
				break;
			case 13 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:91: SILENT
				{
				mSILENT(); 

				}
				break;
			case 14 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:98: UINTTYPE
				{
				mUINTTYPE(); 

				}
				break;
			case 15 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:107: WRITE
				{
				mWRITE(); 

				}
				break;
			case 16 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:113: T__23
				{
				mT__23(); 

				}
				break;
			case 17 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:119: T__24
				{
				mT__24(); 

				}
				break;
			case 18 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:125: T__25
				{
				mT__25(); 

				}
				break;
			case 19 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:131: T__26
				{
				mT__26(); 

				}
				break;
			case 20 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:137: T__27
				{
				mT__27(); 

				}
				break;
			case 21 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:143: T__28
				{
				mT__28(); 

				}
				break;
			case 22 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:149: T__29
				{
				mT__29(); 

				}
				break;
			case 23 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:155: T__30
				{
				mT__30(); 

				}
				break;
			case 24 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:161: T__31
				{
				mT__31(); 

				}
				break;
			case 25 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:167: ID
				{
				mID(); 

				}
				break;
			case 26 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:170: INT
				{
				mINT(); 

				}
				break;
			case 27 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:174: COMMENT
				{
				mCOMMENT(); 

				}
				break;
			case 28 :
				// /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/types/builtIn/busses/memorymodel/MemoryModel.g:1:182: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA7 dfa7 = new DFA7(this);
	static final String DFA7_eotS =
		"\1\uffff\7\24\1\43\2\24\1\46\14\uffff\10\24\1\57\2\24\1\uffff\2\24\1\uffff"+
		"\1\24\1\65\2\24\1\70\3\24\1\uffff\1\75\4\24\1\uffff\2\24\1\uffff\1\24"+
		"\1\105\2\24\1\uffff\2\24\1\112\1\113\1\24\1\115\1\116\1\uffff\4\24\2\uffff"+
		"\1\123\2\uffff\1\124\2\24\1\127\2\uffff\2\24\1\uffff\1\132\1\133\2\uffff";
	static final String DFA7_eofS =
		"\134\uffff";
	static final String DFA7_minS =
		"\1\11\1\154\1\151\1\157\1\162\1\156\1\151\1\141\1\60\2\151\1\60\14\uffff"+
		"\1\151\1\164\1\154\1\162\1\164\1\155\1\163\1\155\1\60\1\167\1\147\1\uffff"+
		"\1\154\1\156\1\uffff\1\141\1\60\1\165\1\157\1\60\1\151\1\153\1\157\1\uffff"+
		"\1\60\1\151\1\145\1\164\1\163\1\uffff\1\155\1\162\1\uffff\1\164\1\60\1"+
		"\162\1\151\1\uffff\1\163\1\156\2\60\1\156\2\60\1\uffff\1\171\1\144\2\164"+
		"\2\uffff\1\60\2\uffff\1\60\1\164\1\145\1\60\2\uffff\1\150\1\162\1\uffff"+
		"\2\60\2\uffff";
	static final String DFA7_maxS =
		"\1\175\1\154\1\151\1\157\1\162\1\156\1\151\1\145\1\172\2\151\1\172\14"+
		"\uffff\1\151\1\164\1\154\1\162\1\164\1\155\1\163\1\155\1\172\1\167\1\147"+
		"\1\uffff\1\154\1\156\1\uffff\1\141\1\172\1\165\1\157\1\172\1\151\1\153"+
		"\1\157\1\uffff\1\172\1\151\1\145\1\164\1\163\1\uffff\1\155\1\162\1\uffff"+
		"\1\164\1\172\1\162\1\151\1\uffff\1\163\1\156\2\172\1\156\2\172\1\uffff"+
		"\1\171\1\144\2\164\2\uffff\1\172\2\uffff\1\172\1\164\1\145\1\172\2\uffff"+
		"\1\150\1\162\1\uffff\2\172\2\uffff";
	static final String DFA7_acceptS =
		"\14\uffff\1\20\1\21\1\22\1\23\1\24\1\25\1\27\1\30\1\31\1\32\1\33\1\34"+
		"\13\uffff\1\11\2\uffff\1\17\10\uffff\1\12\5\uffff\1\2\2\uffff\1\5\4\uffff"+
		"\1\14\7\uffff\1\7\4\uffff\1\16\1\1\1\uffff\1\4\1\6\4\uffff\1\3\1\10\2"+
		"\uffff\1\15\2\uffff\1\13\1\26";
	static final String DFA7_specialS =
		"\134\uffff}>";
	static final String[] DFA7_transitionS = {
			"\2\27\2\uffff\1\27\22\uffff\1\27\16\uffff\1\26\12\25\1\uffff\1\14\1\15"+
			"\1\16\1\17\2\uffff\32\24\1\20\1\uffff\1\21\1\uffff\1\24\1\uffff\1\1\1"+
			"\2\1\3\1\24\1\4\3\24\1\5\2\24\1\6\1\7\4\24\1\10\1\11\1\24\1\12\1\24\1"+
			"\13\3\24\1\22\1\uffff\1\23",
			"\1\30",
			"\1\31",
			"\1\32",
			"\1\33",
			"\1\34",
			"\1\35",
			"\1\36\3\uffff\1\37",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\4\24\1\42\11\24\1\41\7\24"+
			"\1\40\3\24",
			"\1\44",
			"\1\45",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
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
			"\1\47",
			"\1\50",
			"\1\51",
			"\1\52",
			"\1\53",
			"\1\54",
			"\1\55",
			"\1\56",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\60",
			"\1\61",
			"",
			"\1\62",
			"\1\63",
			"",
			"\1\64",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\66",
			"\1\67",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\71",
			"\1\72",
			"\1\73",
			"",
			"\12\24\7\uffff\26\24\1\74\3\24\4\uffff\1\24\1\uffff\32\24",
			"\1\76",
			"\1\77",
			"\1\100",
			"\1\101",
			"",
			"\1\102",
			"\1\103",
			"",
			"\1\104",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\106",
			"\1\107",
			"",
			"\1\110",
			"\1\111",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\114",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"",
			"\1\117",
			"\1\120",
			"\1\121",
			"\1\122",
			"",
			"",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"",
			"",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\125",
			"\1\126",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"",
			"",
			"\1\130",
			"\1\131",
			"",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
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

	protected class DFA7 extends DFA {

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
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( ALIAS | BITTYPE | COLUMN | ERROR | INTTYPE | LIMIT | MASK | MEMORY | READ | READWRITE | ROMWIDTH | ROW | SILENT | UINTTYPE | WRITE | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | ID | INT | COMMENT | WS );";
		}
	}

}

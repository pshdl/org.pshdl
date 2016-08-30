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
// Generated from MemoryModel.g4 by ANTLR 4.5.3
package org.pshdl.model.types.builtIn.busses.memorymodel.v4;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class MemoryModelLexer extends Lexer {
	static {
		RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION);
	}

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9, T__9 = 10, T__10 = 11, T__11 = 12, T__12 = 13, T__13 = 14,
			T__14 = 15, T__15 = 16, T__16 = 17, T__17 = 18, T__18 = 19, T__19 = 20, T__20 = 21, T__21 = 22, T__22 = 23, T__23 = 24, T__24 = 25, T__25 = 26, T__26 = 27, T__27 = 28,
			ID = 29, NUMBER = 30, COMMENT = 31, WS = 32;
	public static String[] modeNames = { "DEFAULT_MODE" };

	public static final String[] ruleNames = { "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", "T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15",
			"T__16", "T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", "T__25", "T__26", "T__27", "ID", "NUMBER", "COMMENT", "WS" };

	private static final String[] _LITERAL_NAMES = { null, "'row'", "'{'", "'}'", "'^'", "'const'", "'$date'", "'$time'", "'$checkSum'", "';'", "'fill'", "'<'", "'>'", "'column'",
			"'alias'", "'memory'", "'register'", "'['", "']'", "'silent'", "'mask'", "'error'", "'limit'", "'r'", "'w'", "'rw'", "'int'", "'uint'", "'bit'" };
	private static final String[] _SYMBOLIC_NAMES = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, "ID", "NUMBER", "COMMENT", "WS" };
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	public MemoryModelLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
	}

	@Override
	public String getGrammarFileName() {
		return "MemoryModel.g4";
	}

	@Override
	public String[] getRuleNames() {
		return ruleNames;
	}

	@Override
	public String getSerializedATN() {
		return _serializedATN;
	}

	@Override
	public String[] getModeNames() {
		return modeNames;
	}

	@Override
	public ATN getATN() {
		return _ATN;
	}

	public static final String _serializedATN = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\"\u0101\b\1\4\2\t"
			+ "\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13" + "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
			+ "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" + "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"
			+ "\t!\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3" + "\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t"
			+ "\3\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\16" + "\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20"
			+ "\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21" + "\3\22\3\22\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25"
			+ "\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27" + "\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\34\3\34\3\34"
			+ "\3\34\3\34\3\35\3\35\3\35\3\35\3\36\3\36\7\36\u00c4\n\36\f\36\16\36\u00c7" + "\13\36\3\37\3\37\3\37\3\37\6\37\u00cd\n\37\r\37\16\37\u00ce\3\37\3\37"
			+ "\3\37\3\37\6\37\u00d5\n\37\r\37\16\37\u00d6\3\37\3\37\6\37\u00db\n\37" + "\r\37\16\37\u00dc\3\37\5\37\u00e0\n\37\3 \3 \3 \3 \7 \u00e6\n \f \16 "
			+ "\u00e9\13 \3 \5 \u00ec\n \3 \3 \3 \3 \3 \7 \u00f3\n \f \16 \u00f6\13 " + "\3 \3 \5 \u00fa\n \3 \3 \3!\3!\3!\3!\3\u00f4\2\"\3\3\5\4\7\5\t\6\13\7"
			+ "\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25" + ")\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"\3\2\t\5\2C\\a"
			+ "ac|\6\2\62;C\\aac|\4\2\62\63aa\6\2\62;CHaach\4\2\62;aa\4\2\f\f\17\17\5" + "\2\13\f\17\17\"\"\u010b\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2"
			+ "\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25" + "\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"
			+ "\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2" + "\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3"
			+ "\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\3C\3\2\2" + "\2\5G\3\2\2\2\7I\3\2\2\2\tK\3\2\2\2\13M\3\2\2\2\rS\3\2\2\2\17Y\3\2\2\2"
			+ "\21_\3\2\2\2\23i\3\2\2\2\25k\3\2\2\2\27p\3\2\2\2\31r\3\2\2\2\33t\3\2\2" + "\2\35{\3\2\2\2\37\u0081\3\2\2\2!\u0088\3\2\2\2#\u0091\3\2\2\2%\u0093\3"
			+ "\2\2\2\'\u0095\3\2\2\2)\u009c\3\2\2\2+\u00a1\3\2\2\2-\u00a7\3\2\2\2/\u00ad" + "\3\2\2\2\61\u00af\3\2\2\2\63\u00b1\3\2\2\2\65\u00b4\3\2\2\2\67\u00b8\3"
			+ "\2\2\29\u00bd\3\2\2\2;\u00c1\3\2\2\2=\u00df\3\2\2\2?\u00f9\3\2\2\2A\u00fd" + "\3\2\2\2CD\7t\2\2DE\7q\2\2EF\7y\2\2F\4\3\2\2\2GH\7}\2\2H\6\3\2\2\2IJ\7"
			+ "\177\2\2J\b\3\2\2\2KL\7`\2\2L\n\3\2\2\2MN\7e\2\2NO\7q\2\2OP\7p\2\2PQ\7" + "u\2\2QR\7v\2\2R\f\3\2\2\2ST\7&\2\2TU\7f\2\2UV\7c\2\2VW\7v\2\2WX\7g\2\2"
			+ "X\16\3\2\2\2YZ\7&\2\2Z[\7v\2\2[\\\7k\2\2\\]\7o\2\2]^\7g\2\2^\20\3\2\2" + "\2_`\7&\2\2`a\7e\2\2ab\7j\2\2bc\7g\2\2cd\7e\2\2de\7m\2\2ef\7U\2\2fg\7"
			+ "w\2\2gh\7o\2\2h\22\3\2\2\2ij\7=\2\2j\24\3\2\2\2kl\7h\2\2lm\7k\2\2mn\7" + "n\2\2no\7n\2\2o\26\3\2\2\2pq\7>\2\2q\30\3\2\2\2rs\7@\2\2s\32\3\2\2\2t"
			+ "u\7e\2\2uv\7q\2\2vw\7n\2\2wx\7w\2\2xy\7o\2\2yz\7p\2\2z\34\3\2\2\2{|\7" + "c\2\2|}\7n\2\2}~\7k\2\2~\177\7c\2\2\177\u0080\7u\2\2\u0080\36\3\2\2\2"
			+ "\u0081\u0082\7o\2\2\u0082\u0083\7g\2\2\u0083\u0084\7o\2\2\u0084\u0085" + "\7q\2\2\u0085\u0086\7t\2\2\u0086\u0087\7{\2\2\u0087 \3\2\2\2\u0088\u0089"
			+ "\7t\2\2\u0089\u008a\7g\2\2\u008a\u008b\7i\2\2\u008b\u008c\7k\2\2\u008c" + "\u008d\7u\2\2\u008d\u008e\7v\2\2\u008e\u008f\7g\2\2\u008f\u0090\7t\2\2"
			+ "\u0090\"\3\2\2\2\u0091\u0092\7]\2\2\u0092$\3\2\2\2\u0093\u0094\7_\2\2" + "\u0094&\3\2\2\2\u0095\u0096\7u\2\2\u0096\u0097\7k\2\2\u0097\u0098\7n\2"
			+ "\2\u0098\u0099\7g\2\2\u0099\u009a\7p\2\2\u009a\u009b\7v\2\2\u009b(\3\2" + "\2\2\u009c\u009d\7o\2\2\u009d\u009e\7c\2\2\u009e\u009f\7u\2\2\u009f\u00a0"
			+ "\7m\2\2\u00a0*\3\2\2\2\u00a1\u00a2\7g\2\2\u00a2\u00a3\7t\2\2\u00a3\u00a4" + "\7t\2\2\u00a4\u00a5\7q\2\2\u00a5\u00a6\7t\2\2\u00a6,\3\2\2\2\u00a7\u00a8"
			+ "\7n\2\2\u00a8\u00a9\7k\2\2\u00a9\u00aa\7o\2\2\u00aa\u00ab\7k\2\2\u00ab" + "\u00ac\7v\2\2\u00ac.\3\2\2\2\u00ad\u00ae\7t\2\2\u00ae\60\3\2\2\2\u00af"
			+ "\u00b0\7y\2\2\u00b0\62\3\2\2\2\u00b1\u00b2\7t\2\2\u00b2\u00b3\7y\2\2\u00b3" + "\64\3\2\2\2\u00b4\u00b5\7k\2\2\u00b5\u00b6\7p\2\2\u00b6\u00b7\7v\2\2\u00b7"
			+ "\66\3\2\2\2\u00b8\u00b9\7w\2\2\u00b9\u00ba\7k\2\2\u00ba\u00bb\7p\2\2\u00bb" + "\u00bc\7v\2\2\u00bc8\3\2\2\2\u00bd\u00be\7d\2\2\u00be\u00bf\7k\2\2\u00bf"
			+ "\u00c0\7v\2\2\u00c0:\3\2\2\2\u00c1\u00c5\t\2\2\2\u00c2\u00c4\t\3\2\2\u00c3" + "\u00c2\3\2\2\2\u00c4\u00c7\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c5\u00c6\3\2"
			+ "\2\2\u00c6<\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c8\u00c9\7\62\2\2\u00c9\u00ca" + "\7d\2\2\u00ca\u00cc\3\2\2\2\u00cb\u00cd\t\4\2\2\u00cc\u00cb\3\2\2\2\u00cd"
			+ "\u00ce\3\2\2\2\u00ce\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00e0\3\2" + "\2\2\u00d0\u00d1\7\62\2\2\u00d1\u00d2\7z\2\2\u00d2\u00d4\3\2\2\2\u00d3"
			+ "\u00d5\t\5\2\2\u00d4\u00d3\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d4\3\2" + "\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00e0\3\2\2\2\u00d8\u00da\4\63;\2\u00d9"
			+ "\u00db\t\6\2\2\u00da\u00d9\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00da\3\2" + "\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00e0\3\2\2\2\u00de\u00e0\4\62;\2\u00df"
			+ "\u00c8\3\2\2\2\u00df\u00d0\3\2\2\2\u00df\u00d8\3\2\2\2\u00df\u00de\3\2" + "\2\2\u00e0>\3\2\2\2\u00e1\u00e2\7\61\2\2\u00e2\u00e3\7\61\2\2\u00e3\u00e7"
			+ "\3\2\2\2\u00e4\u00e6\n\7\2\2\u00e5\u00e4\3\2\2\2\u00e6\u00e9\3\2\2\2\u00e7" + "\u00e5\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00eb\3\2\2\2\u00e9\u00e7\3\2"
			+ "\2\2\u00ea\u00ec\7\17\2\2\u00eb\u00ea\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec" + "\u00ed\3\2\2\2\u00ed\u00fa\7\f\2\2\u00ee\u00ef\7\61\2\2\u00ef\u00f0\7"
			+ ",\2\2\u00f0\u00f4\3\2\2\2\u00f1\u00f3\13\2\2\2\u00f2\u00f1\3\2\2\2\u00f3" + "\u00f6\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f4\u00f2\3\2\2\2\u00f5\u00f7\3\2"
			+ "\2\2\u00f6\u00f4\3\2\2\2\u00f7\u00f8\7,\2\2\u00f8\u00fa\7\61\2\2\u00f9" + "\u00e1\3\2\2\2\u00f9\u00ee\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00fc\b "
			+ "\2\2\u00fc@\3\2\2\2\u00fd\u00fe\t\b\2\2\u00fe\u00ff\3\2\2\2\u00ff\u0100" + "\b!\2\2\u0100B\3\2\2\2\f\2\u00c5\u00ce\u00d6\u00dc\u00df\u00e7\u00eb\u00f4"
			+ "\u00f9\3\b\2\2";
	public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
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
// Generated from MemoryModel.g4 by ANTLR 4.7
package org.pshdl.model.types.builtIn.busses.memorymodel.v4;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class MemoryModelLexer extends Lexer {
	static {
		RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION);
	}

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9, T__9 = 10, T__10 = 11, T__11 = 12, T__12 = 13, T__13 = 14,
			T__14 = 15, T__15 = 16, T__16 = 17, T__17 = 18, T__18 = 19, T__19 = 20, T__20 = 21, T__21 = 22, T__22 = 23, T__23 = 24, T__24 = 25, T__25 = 26, T__26 = 27, T__27 = 28,
			T__28 = 29, ID = 30, NUMBER = 31, COMMENT = 32, WS = 33;
	public static String[] channelNames = { "DEFAULT_TOKEN_CHANNEL", "HIDDEN" };

	public static String[] modeNames = { "DEFAULT_MODE" };

	public static final String[] ruleNames = { "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", "T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15",
			"T__16", "T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", "T__25", "T__26", "T__27", "T__28", "ID", "NUMBER", "COMMENT", "WS" };

	private static final String[] _LITERAL_NAMES = { null, "'row'", "'{'", "'}'", "'^'", "'const'", "'<'", "'>'", "'$date'", "'$time'", "'$checkSum'", "';'", "'fill'", "'column'",
			"'alias'", "'memory'", "'register'", "'['", "']'", "'writtenFlag'", "'silent'", "'mask'", "'error'", "'limit'", "'r'", "'w'", "'rw'", "'int'", "'uint'", "'bit'" };
	private static final String[] _SYMBOLIC_NAMES = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, "ID", "NUMBER", "COMMENT", "WS" };
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
	public String[] getChannelNames() {
		return channelNames;
	}

	@Override
	public String[] getModeNames() {
		return modeNames;
	}

	@Override
	public ATN getATN() {
		return _ATN;
	}

	public static final String _serializedATN = "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2#\u010f\b\1\4\2\t"
			+ "\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13" + "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
			+ "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" + "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"
			+ "\t!\4\"\t\"\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6\3" + "\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n"
			+ "\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\r" + "\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3"
			+ "\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3" + "\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3"
			+ "\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3" + "\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3"
			+ "\30\3\31\3\31\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3" + "\35\3\35\3\35\3\36\3\36\3\36\3\36\3\37\3\37\7\37\u00d2\n\37\f\37\16\37"
			+ "\u00d5\13\37\3 \3 \3 \3 \6 \u00db\n \r \16 \u00dc\3 \3 \3 \3 \6 \u00e3" + "\n \r \16 \u00e4\3 \3 \6 \u00e9\n \r \16 \u00ea\3 \5 \u00ee\n \3!\3!\3"
			+ "!\3!\7!\u00f4\n!\f!\16!\u00f7\13!\3!\5!\u00fa\n!\3!\3!\3!\3!\3!\7!\u0101" + "\n!\f!\16!\u0104\13!\3!\3!\5!\u0108\n!\3!\3!\3\"\3\"\3\"\3\"\3\u0102\2"
			+ "#\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20" + "\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37"
			+ "= ?!A\"C#\3\2\t\5\2C\\aac|\6\2\62;C\\aac|\4\2\62\63aa\6\2\62;CHaach\4" + "\2\62;aa\4\2\f\f\17\17\5\2\13\f\17\17\"\"\2\u0119\2\3\3\2\2\2\2\5\3\2"
			+ "\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21" + "\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"
			+ "\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3" + "\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3"
			+ "\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3" + "\2\2\2\2A\3\2\2\2\2C\3\2\2\2\3E\3\2\2\2\5I\3\2\2\2\7K\3\2\2\2\tM\3\2\2"
			+ "\2\13O\3\2\2\2\rU\3\2\2\2\17W\3\2\2\2\21Y\3\2\2\2\23_\3\2\2\2\25e\3\2" + "\2\2\27o\3\2\2\2\31q\3\2\2\2\33v\3\2\2\2\35}\3\2\2\2\37\u0083\3\2\2\2"
			+ "!\u008a\3\2\2\2#\u0093\3\2\2\2%\u0095\3\2\2\2\'\u0097\3\2\2\2)\u00a3\3" + "\2\2\2+\u00aa\3\2\2\2-\u00af\3\2\2\2/\u00b5\3\2\2\2\61\u00bb\3\2\2\2\63"
			+ "\u00bd\3\2\2\2\65\u00bf\3\2\2\2\67\u00c2\3\2\2\29\u00c6\3\2\2\2;\u00cb" + "\3\2\2\2=\u00cf\3\2\2\2?\u00ed\3\2\2\2A\u0107\3\2\2\2C\u010b\3\2\2\2E"
			+ "F\7t\2\2FG\7q\2\2GH\7y\2\2H\4\3\2\2\2IJ\7}\2\2J\6\3\2\2\2KL\7\177\2\2" + "L\b\3\2\2\2MN\7`\2\2N\n\3\2\2\2OP\7e\2\2PQ\7q\2\2QR\7p\2\2RS\7u\2\2ST"
			+ "\7v\2\2T\f\3\2\2\2UV\7>\2\2V\16\3\2\2\2WX\7@\2\2X\20\3\2\2\2YZ\7&\2\2" + "Z[\7f\2\2[\\\7c\2\2\\]\7v\2\2]^\7g\2\2^\22\3\2\2\2_`\7&\2\2`a\7v\2\2a"
			+ "b\7k\2\2bc\7o\2\2cd\7g\2\2d\24\3\2\2\2ef\7&\2\2fg\7e\2\2gh\7j\2\2hi\7" + "g\2\2ij\7e\2\2jk\7m\2\2kl\7U\2\2lm\7w\2\2mn\7o\2\2n\26\3\2\2\2op\7=\2"
			+ "\2p\30\3\2\2\2qr\7h\2\2rs\7k\2\2st\7n\2\2tu\7n\2\2u\32\3\2\2\2vw\7e\2" + "\2wx\7q\2\2xy\7n\2\2yz\7w\2\2z{\7o\2\2{|\7p\2\2|\34\3\2\2\2}~\7c\2\2~"
			+ "\177\7n\2\2\177\u0080\7k\2\2\u0080\u0081\7c\2\2\u0081\u0082\7u\2\2\u0082" + "\36\3\2\2\2\u0083\u0084\7o\2\2\u0084\u0085\7g\2\2\u0085\u0086\7o\2\2\u0086"
			+ "\u0087\7q\2\2\u0087\u0088\7t\2\2\u0088\u0089\7{\2\2\u0089 \3\2\2\2\u008a" + "\u008b\7t\2\2\u008b\u008c\7g\2\2\u008c\u008d\7i\2\2\u008d\u008e\7k\2\2"
			+ "\u008e\u008f\7u\2\2\u008f\u0090\7v\2\2\u0090\u0091\7g\2\2\u0091\u0092" + "\7t\2\2\u0092\"\3\2\2\2\u0093\u0094\7]\2\2\u0094$\3\2\2\2\u0095\u0096"
			+ "\7_\2\2\u0096&\3\2\2\2\u0097\u0098\7y\2\2\u0098\u0099\7t\2\2\u0099\u009a" + "\7k\2\2\u009a\u009b\7v\2\2\u009b\u009c\7v\2\2\u009c\u009d\7g\2\2\u009d"
			+ "\u009e\7p\2\2\u009e\u009f\7H\2\2\u009f\u00a0\7n\2\2\u00a0\u00a1\7c\2\2" + "\u00a1\u00a2\7i\2\2\u00a2(\3\2\2\2\u00a3\u00a4\7u\2\2\u00a4\u00a5\7k\2"
			+ "\2\u00a5\u00a6\7n\2\2\u00a6\u00a7\7g\2\2\u00a7\u00a8\7p\2\2\u00a8\u00a9" + "\7v\2\2\u00a9*\3\2\2\2\u00aa\u00ab\7o\2\2\u00ab\u00ac\7c\2\2\u00ac\u00ad"
			+ "\7u\2\2\u00ad\u00ae\7m\2\2\u00ae,\3\2\2\2\u00af\u00b0\7g\2\2\u00b0\u00b1" + "\7t\2\2\u00b1\u00b2\7t\2\2\u00b2\u00b3\7q\2\2\u00b3\u00b4\7t\2\2\u00b4"
			+ ".\3\2\2\2\u00b5\u00b6\7n\2\2\u00b6\u00b7\7k\2\2\u00b7\u00b8\7o\2\2\u00b8" + "\u00b9\7k\2\2\u00b9\u00ba\7v\2\2\u00ba\60\3\2\2\2\u00bb\u00bc\7t\2\2\u00bc"
			+ "\62\3\2\2\2\u00bd\u00be\7y\2\2\u00be\64\3\2\2\2\u00bf\u00c0\7t\2\2\u00c0" + "\u00c1\7y\2\2\u00c1\66\3\2\2\2\u00c2\u00c3\7k\2\2\u00c3\u00c4\7p\2\2\u00c4"
			+ "\u00c5\7v\2\2\u00c58\3\2\2\2\u00c6\u00c7\7w\2\2\u00c7\u00c8\7k\2\2\u00c8" + "\u00c9\7p\2\2\u00c9\u00ca\7v\2\2\u00ca:\3\2\2\2\u00cb\u00cc\7d\2\2\u00cc"
			+ "\u00cd\7k\2\2\u00cd\u00ce\7v\2\2\u00ce<\3\2\2\2\u00cf\u00d3\t\2\2\2\u00d0" + "\u00d2\t\3\2\2\u00d1\u00d0\3\2\2\2\u00d2\u00d5\3\2\2\2\u00d3\u00d1\3\2"
			+ "\2\2\u00d3\u00d4\3\2\2\2\u00d4>\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d6\u00d7" + "\7\62\2\2\u00d7\u00d8\7d\2\2\u00d8\u00da\3\2\2\2\u00d9\u00db\t\4\2\2\u00da"
			+ "\u00d9\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc\u00dd\3\2" + "\2\2\u00dd\u00ee\3\2\2\2\u00de\u00df\7\62\2\2\u00df\u00e0\7z\2\2\u00e0"
			+ "\u00e2\3\2\2\2\u00e1\u00e3\t\5\2\2\u00e2\u00e1\3\2\2\2\u00e3\u00e4\3\2" + "\2\2\u00e4\u00e2\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00ee\3\2\2\2\u00e6"
			+ "\u00e8\4\63;\2\u00e7\u00e9\t\6\2\2\u00e8\u00e7\3\2\2\2\u00e9\u00ea\3\2" + "\2\2\u00ea\u00e8\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb\u00ee\3\2\2\2\u00ec"
			+ "\u00ee\4\62;\2\u00ed\u00d6\3\2\2\2\u00ed\u00de\3\2\2\2\u00ed\u00e6\3\2" + "\2\2\u00ed\u00ec\3\2\2\2\u00ee@\3\2\2\2\u00ef\u00f0\7\61\2\2\u00f0\u00f1"
			+ "\7\61\2\2\u00f1\u00f5\3\2\2\2\u00f2\u00f4\n\7\2\2\u00f3\u00f2\3\2\2\2" + "\u00f4\u00f7\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6\u00f9"
			+ "\3\2\2\2\u00f7\u00f5\3\2\2\2\u00f8\u00fa\7\17\2\2\u00f9\u00f8\3\2\2\2" + "\u00f9\u00fa\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u0108\7\f\2\2\u00fc\u00fd"
			+ "\7\61\2\2\u00fd\u00fe\7,\2\2\u00fe\u0102\3\2\2\2\u00ff\u0101\13\2\2\2" + "\u0100\u00ff\3\2\2\2\u0101\u0104\3\2\2\2\u0102\u0103\3\2\2\2\u0102\u0100"
			+ "\3\2\2\2\u0103\u0105\3\2\2\2\u0104\u0102\3\2\2\2\u0105\u0106\7,\2\2\u0106" + "\u0108\7\61\2\2\u0107\u00ef\3\2\2\2\u0107\u00fc\3\2\2\2\u0108\u0109\3"
			+ "\2\2\2\u0109\u010a\b!\2\2\u010aB\3\2\2\2\u010b\u010c\t\b\2\2\u010c\u010d" + "\3\2\2\2\u010d\u010e\b\"\2\2\u010eD\3\2\2\2\f\2\u00d3\u00dc\u00e4\u00ea"
			+ "\u00ed\u00f5\u00f9\u0102\u0107\3\b\2\2";
	public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
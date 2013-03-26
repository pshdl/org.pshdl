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
// Generated from MemoryModel.g4 by ANTLR 4.0
package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.v4;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.*;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class MemoryModelLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int T__21 = 1, T__20 = 2, T__19 = 3, T__18 = 4, T__17 = 5, T__16 = 6, T__15 = 7, T__14 = 8, T__13 = 9, T__12 = 10, T__11 = 11, T__10 = 12, T__9 = 13,
			T__8 = 14, T__7 = 15, T__6 = 16, T__5 = 17, T__4 = 18, T__3 = 19, T__2 = 20, T__1 = 21, T__0 = 22, ID = 23, INT = 24, COMMENT = 25, WS = 26;
	public static String[] modeNames = { "DEFAULT_MODE" };

	public static final String[] tokenNames = { "<INVALID>", "'memory'", "'column'", "'error'", "']'", "'row'", "'limit'", "'mask'", "'register'", "'rw'", "'['", "'<'", "'int'",
			"'r'", "'w'", "';'", "'alias'", "'>'", "'{'", "'silent'", "'}'", "'uint'", "'bit'", "ID", "INT", "COMMENT", "WS" };
	public static final String[] ruleNames = { "T__21", "T__20", "T__19", "T__18", "T__17", "T__16", "T__15", "T__14", "T__13", "T__12", "T__11", "T__10", "T__9", "T__8", "T__7",
			"T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "ID", "INT", "COMMENT", "WS" };

	public MemoryModelLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
	}

	@Override
	public String getGrammarFileName() {
		return "MemoryModel.g4";
	}

	@Override
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override
	public String[] getRuleNames() {
		return ruleNames;
	}

	@Override
	public String[] getModeNames() {
		return modeNames;
	}

	@Override
	public ATN getATN() {
		return _ATN;
	}

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 24:
			COMMENT_action(_localctx, actionIndex);
			break;

		case 25:
			WS_action(_localctx, actionIndex);
			break;
		}
	}

	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			skip();
			break;
		}
	}

	private void COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			skip();
			break;
		}
	}

	public static final String _serializedATN = "\2\4\34\u00be\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b"
			+ "\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20" + "\t\20\4\21\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27"
			+ "\t\27\4\30\t\30\4\31\t\31\4\32\t\32\4\33\t\33\3\2\3\2\3\2\3\2\3\2\3\2" + "\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\6\3"
			+ "\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t" + "\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\16"
			+ "\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\23" + "\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\26"
			+ "\3\26\3\27\3\27\3\27\3\27\3\30\3\30\7\30\u0095\n\30\f\30\16\30\u0098\13" + "\30\3\31\6\31\u009b\n\31\r\31\16\31\u009c\3\32\3\32\3\32\3\32\7\32\u00a3"
			+ "\n\32\f\32\16\32\u00a6\13\32\3\32\5\32\u00a9\n\32\3\32\3\32\3\32\3\32" + "\3\32\7\32\u00b0\n\32\f\32\16\32\u00b3\13\32\3\32\3\32\5\32\u00b7\n\32"
			+ "\3\32\3\32\3\33\3\33\3\33\3\33\3\u00b1\34\3\3\1\5\4\1\7\5\1\t\6\1\13\7" + "\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37"
			+ "\21\1!\22\1#\23\1%\24\1\'\25\1)\26\1+\27\1-\30\1/\31\1\61\32\1\63\33\2" + "\65\34\3\3\2\6\5C\\aac|\6\62;C\\aac|\4\f\f\17\17\5\13\f\17\17\"\"\u00c3"
			+ "\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2" + "\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"
			+ "\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2" + "\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"
			+ "\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\3\67\3\2\2\2\5>\3\2\2\2\7E\3" + "\2\2\2\tK\3\2\2\2\13M\3\2\2\2\rQ\3\2\2\2\17W\3\2\2\2\21\\\3\2\2\2\23e"
			+ "\3\2\2\2\25h\3\2\2\2\27j\3\2\2\2\31l\3\2\2\2\33p\3\2\2\2\35r\3\2\2\2\37" + "t\3\2\2\2!v\3\2\2\2#|\3\2\2\2%~\3\2\2\2\'\u0080\3\2\2\2)\u0087\3\2\2\2"
			+ "+\u0089\3\2\2\2-\u008e\3\2\2\2/\u0092\3\2\2\2\61\u009a\3\2\2\2\63\u00b6" + "\3\2\2\2\65\u00ba\3\2\2\2\678\7o\2\289\7g\2\29:\7o\2\2:;\7q\2\2;<\7t\2"
			+ "\2<=\7{\2\2=\4\3\2\2\2>?\7e\2\2?@\7q\2\2@A\7n\2\2AB\7w\2\2BC\7o\2\2CD" + "\7p\2\2D\6\3\2\2\2EF\7g\2\2FG\7t\2\2GH\7t\2\2HI\7q\2\2IJ\7t\2\2J\b\3\2"
			+ "\2\2KL\7_\2\2L\n\3\2\2\2MN\7t\2\2NO\7q\2\2OP\7y\2\2P\f\3\2\2\2QR\7n\2" + "\2RS\7k\2\2ST\7o\2\2TU\7k\2\2UV\7v\2\2V\16\3\2\2\2WX\7o\2\2XY\7c\2\2Y"
			+ "Z\7u\2\2Z[\7m\2\2[\20\3\2\2\2\\]\7t\2\2]^\7g\2\2^_\7i\2\2_`\7k\2\2`a\7" + "u\2\2ab\7v\2\2bc\7g\2\2cd\7t\2\2d\22\3\2\2\2ef\7t\2\2fg\7y\2\2g\24\3\2"
			+ "\2\2hi\7]\2\2i\26\3\2\2\2jk\7>\2\2k\30\3\2\2\2lm\7k\2\2mn\7p\2\2no\7v" + "\2\2o\32\3\2\2\2pq\7t\2\2q\34\3\2\2\2rs\7y\2\2s\36\3\2\2\2tu\7=\2\2u "
			+ "\3\2\2\2vw\7c\2\2wx\7n\2\2xy\7k\2\2yz\7c\2\2z{\7u\2\2{\"\3\2\2\2|}\7@" + "\2\2}$\3\2\2\2~\177\7}\2\2\177&\3\2\2\2\u0080\u0081\7u\2\2\u0081\u0082"
			+ "\7k\2\2\u0082\u0083\7n\2\2\u0083\u0084\7g\2\2\u0084\u0085\7p\2\2\u0085" + "\u0086\7v\2\2\u0086(\3\2\2\2\u0087\u0088\7\177\2\2\u0088*\3\2\2\2\u0089"
			+ "\u008a\7w\2\2\u008a\u008b\7k\2\2\u008b\u008c\7p\2\2\u008c\u008d\7v\2\2" + "\u008d,\3\2\2\2\u008e\u008f\7d\2\2\u008f\u0090\7k\2\2\u0090\u0091\7v\2"
			+ "\2\u0091.\3\2\2\2\u0092\u0096\t\2\2\2\u0093\u0095\t\3\2\2\u0094\u0093" + "\3\2\2\2\u0095\u0098\3\2\2\2\u0096\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097"
			+ "\60\3\2\2\2\u0098\u0096\3\2\2\2\u0099\u009b\4\62;\2\u009a\u0099\3\2\2" + "\2\u009b\u009c\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\62"
			+ "\3\2\2\2\u009e\u009f\7\61\2\2\u009f\u00a0\7\61\2\2\u00a0\u00a4\3\2\2\2" + "\u00a1\u00a3\n\4\2\2\u00a2\u00a1\3\2\2\2\u00a3\u00a6\3\2\2\2\u00a4\u00a2"
			+ "\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a7" + "\u00a9\7\17\2\2\u00a8\u00a7\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00aa\3"
			+ "\2\2\2\u00aa\u00b7\7\f\2\2\u00ab\u00ac\7\61\2\2\u00ac\u00ad\7,\2\2\u00ad" + "\u00b1\3\2\2\2\u00ae\u00b0\13\2\2\2\u00af\u00ae\3\2\2\2\u00b0\u00b3\3"
			+ "\2\2\2\u00b1\u00b2\3\2\2\2\u00b1\u00af\3\2\2\2\u00b2\u00b4\3\2\2\2\u00b3" + "\u00b1\3\2\2\2\u00b4\u00b5\7,\2\2\u00b5\u00b7\7\61\2\2\u00b6\u009e\3\2"
			+ "\2\2\u00b6\u00ab\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8\u00b9\b\32\2\2\u00b9" + "\64\3\2\2\2\u00ba\u00bb\t\5\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00bd\b\33\3"
			+ "\2\u00bd\66\3\2\2\2\t\2\u0096\u009c\u00a4\u00a8\u00b1\u00b6";
	public static final ATN _ATN = ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}

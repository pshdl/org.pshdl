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
// Generated from PSHDLLang.g4 by ANTLR 4.1
package org.pshdl.model.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.*;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class PSHDLLangLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int T__42 = 1, T__41 = 2, T__40 = 3, T__39 = 4, T__38 = 5, T__37 = 6, T__36 = 7, T__35 = 8, T__34 = 9, T__33 = 10, T__32 = 11, T__31 = 12, T__30 = 13,
			T__29 = 14, T__28 = 15, T__27 = 16, T__26 = 17, T__25 = 18, T__24 = 19, T__23 = 20, T__22 = 21, T__21 = 22, T__20 = 23, T__19 = 24, T__18 = 25, T__17 = 26, T__16 = 27,
			T__15 = 28, T__14 = 29, T__13 = 30, T__12 = 31, T__11 = 32, T__10 = 33, T__9 = 34, T__8 = 35, T__7 = 36, T__6 = 37, T__5 = 38, T__4 = 39, T__3 = 40, T__2 = 41,
			T__1 = 42, T__0 = 43, AND = 44, OR = 45, XOR = 46, LOGI_AND = 47, LOGI_OR = 48, MUL = 49, DIV = 50, PLUS = 51, MOD = 52, POW = 53, SLL = 54, SRA = 55, SRL = 56,
			EQ = 57, NOT_EQ = 58, LESS = 59, LESS_EQ = 60, GREATER = 61, GREATER_EQ = 62, ASSGN = 63, ADD_ASSGN = 64, SUB_ASSGN = 65, MUL_ASSGN = 66, DIV_ASSGN = 67,
			MOD_ASSGN = 68, AND_ASSGN = 69, XOR_ASSGN = 70, OR_ASSGN = 71, SLL_ASSGN = 72, SRL_ASSGN = 73, SRA_ASSGN = 74, ARITH_NEG = 75, BIT_NEG = 76, LOGIC_NEG = 77,
			ANY_INT = 78, ANY_UINT = 79, ANY_BIT = 80, ANY_IF = 81, ANY_ENUM = 82, BIT = 83, INT = 84, UINT = 85, STRING = 86, BOOL = 87, ENUM = 88, INTERFACE = 89, FUNCTION = 90,
			MODULE = 91, TESTBENCH = 92, RULE_PS_LITERAL_TERMINAL = 93, RULE_ID = 94, RULE_STRING = 95, RULE_ML_COMMENT = 96, RULE_GENERATOR_CONTENT = 97, RULE_SL_COMMENT = 98,
			RULE_WS = 99;
	public static String[] modeNames = { "DEFAULT_MODE" };

	public static final String[] tokenNames = { "<INVALID>", "'record'", "'register'", "'['", "'param'", "'substitute'", "'inout'", "'}'", "'case'", "'->'", "'simulation'", "')'",
			"'generate'", "'inline'", "'-:'", "'@'", "'+:'", "'.*'", "'const'", "']'", "'default'", "'in'", "','", "':'", "'('", "'if'", "'$rst'", "'?'", "'package'", "'{'",
			"'native'", "'extends'", "'else'", "'import'", "'.'", "'=>'", "'for'", "'process'", "'$clk'", "';'", "'include'", "'switch'", "'#'", "'out'", "'&'", "'|'", "'^'",
			"'&&'", "'||'", "'*'", "'/'", "'+'", "'%'", "'**'", "'<<'", "'>>'", "'>>>'", "'=='", "'!='", "'<'", "'<='", "'>'", "'>='", "'='", "'+='", "'-='", "'*='", "'/='",
			"'%='", "'&='", "'^='", "'|='", "'<<='", "'>>>='", "'>>='", "'-'", "'~'", "'!'", "'int<>'", "'uint<>'", "'bit<>'", "'interface<>'", "'enum<>'", "'bit'", "'int'",
			"'uint'", "'string'", "'bool'", "'enum'", "'interface'", "'function'", "'module'", "'testbench'", "RULE_PS_LITERAL_TERMINAL", "RULE_ID", "RULE_STRING",
			"RULE_ML_COMMENT", "RULE_GENERATOR_CONTENT", "RULE_SL_COMMENT", "RULE_WS" };
	public static final String[] ruleNames = { "T__42", "T__41", "T__40", "T__39", "T__38", "T__37", "T__36", "T__35", "T__34", "T__33", "T__32", "T__31", "T__30", "T__29",
			"T__28", "T__27", "T__26", "T__25", "T__24", "T__23", "T__22", "T__21", "T__20", "T__19", "T__18", "T__17", "T__16", "T__15", "T__14", "T__13", "T__12", "T__11",
			"T__10", "T__9", "T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "AND", "OR", "XOR", "LOGI_AND", "LOGI_OR", "MUL", "DIV", "PLUS", "MOD", "POW",
			"SLL", "SRA", "SRL", "EQ", "NOT_EQ", "LESS", "LESS_EQ", "GREATER", "GREATER_EQ", "ASSGN", "ADD_ASSGN", "SUB_ASSGN", "MUL_ASSGN", "DIV_ASSGN", "MOD_ASSGN", "AND_ASSGN",
			"XOR_ASSGN", "OR_ASSGN", "SLL_ASSGN", "SRL_ASSGN", "SRA_ASSGN", "ARITH_NEG", "BIT_NEG", "LOGIC_NEG", "ANY_INT", "ANY_UINT", "ANY_BIT", "ANY_IF", "ANY_ENUM", "BIT",
			"INT", "UINT", "STRING", "BOOL", "ENUM", "INTERFACE", "FUNCTION", "MODULE", "TESTBENCH", "RULE_PS_LITERAL_TERMINAL", "IDCHARFIRST", "IDCHAR", "RULE_ID", "ESC",
			"RULE_STRING", "RULE_ML_COMMENT", "RULE_GENERATOR_CONTENT", "RULE_SL_COMMENT", "RULE_WS" };

	public static final int WHITESPACE = 1;
	public static final int COMMENTS = 2;

	public PSHDLLangLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
	}

	@Override
	public String getGrammarFileName() {
		return "PSHDLLang.g4";
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
		case 98:
			RULE_ML_COMMENT_action(_localctx, actionIndex);
			break;

		case 100:
			RULE_SL_COMMENT_action(_localctx, actionIndex);
			break;

		case 101:
			RULE_WS_action(_localctx, actionIndex);
			break;
		}
	}

	private void RULE_WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:
			_channel = WHITESPACE;
			break;
		}
	}

	private void RULE_SL_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			_channel = COMMENTS;
			break;
		}
	}

	private void RULE_ML_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			_channel = COMMENTS;
			break;
		}
	}

	public static final String _serializedATN = "\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2e\u02dd\b\1\4\2\t"
			+ "\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13" + "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
			+ "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" + "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"
			+ "\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4" + ",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"
			+ "\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t=" + "\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"
			+ "\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT" + "\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"
			+ "`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\3\2\3\2\3\2\3\2\3\2\3\2" + "\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3"
			+ "\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7" + "\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13"
			+ "\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r" + "\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\21\3\21"
			+ "\3\21\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\25\3\25" + "\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\30\3\30\3\31"
			+ "\3\31\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\35\3\35\3\35" + "\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37"
			+ "\3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3" + "#\3#\3$\3$\3$\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'"
			+ "\3(\3(\3)\3)\3)\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3*\3+\3+\3,\3,\3,\3," + "\3-\3-\3.\3.\3/\3/\3\60\3\60\3\60\3\61\3\61\3\61\3\62\3\62\3\63\3\63\3"
			+ "\64\3\64\3\65\3\65\3\66\3\66\3\66\3\67\3\67\3\67\38\38\38\39\39\39\39" + "\3:\3:\3:\3;\3;\3;\3<\3<\3=\3=\3=\3>\3>\3?\3?\3?\3@\3@\3A\3A\3A\3B\3B"
			+ "\3B\3C\3C\3C\3D\3D\3D\3E\3E\3E\3F\3F\3F\3G\3G\3G\3H\3H\3H\3I\3I\3I\3I" + "\3J\3J\3J\3J\3J\3K\3K\3K\3K\3L\3L\3M\3M\3N\3N\3O\3O\3O\3O\3O\3O\3P\3P"
			+ "\3P\3P\3P\3P\3P\3Q\3Q\3Q\3Q\3Q\3Q\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R\3R" + "\3S\3S\3S\3S\3S\3S\3S\3T\3T\3T\3T\3U\3U\3U\3U\3V\3V\3V\3V\3V\3W\3W\3W"
			+ "\3W\3W\3W\3W\3X\3X\3X\3X\3X\3Y\3Y\3Y\3Y\3Y\3Z\3Z\3Z\3Z\3Z\3Z\3Z\3Z\3Z" + "\3Z\3[\3[\3[\3[\3[\3[\3[\3[\3[\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3]\3]\3]\3"
			+ "]\3]\3]\3]\3]\3]\3]\3^\3^\3^\3^\6^\u026a\n^\r^\16^\u026b\3^\3^\3^\3^\6" + "^\u0272\n^\r^\16^\u0273\3^\3^\6^\u0278\n^\r^\16^\u0279\3^\3^\3^\3^\3^"
			+ "\3^\3^\3^\3^\3^\5^\u0286\n^\3_\3_\3`\3`\5`\u028c\n`\3a\3a\7a\u0290\na" + "\fa\16a\u0293\13a\3b\3b\3b\3c\3c\3c\7c\u029b\nc\fc\16c\u029e\13c\3c\3"
			+ "c\3c\3c\7c\u02a4\nc\fc\16c\u02a7\13c\3c\5c\u02aa\nc\3d\3d\3d\3d\7d\u02b0" + "\nd\fd\16d\u02b3\13d\3d\3d\3d\3d\3d\3e\3e\3e\3e\7e\u02be\ne\fe\16e\u02c1"
			+ "\13e\3e\3e\3e\3f\3f\3f\3f\7f\u02ca\nf\ff\16f\u02cd\13f\3f\5f\u02d0\nf" + "\3f\5f\u02d3\nf\3f\3f\3g\6g\u02d8\ng\rg\16g\u02d9\3g\3g\4\u02b1\u02bf"
			+ "h\3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27" + "\r\1\31\16\1\33\17\1\35\20\1\37\21\1!\22\1#\23\1%\24\1\'\25\1)\26\1+\27"
			+ "\1-\30\1/\31\1\61\32\1\63\33\1\65\34\1\67\35\19\36\1;\37\1= \1?!\1A\"" + "\1C#\1E$\1G%\1I&\1K\'\1M(\1O)\1Q*\1S+\1U,\1W-\1Y.\1[/\1]\60\1_\61\1a\62"
			+ "\1c\63\1e\64\1g\65\1i\66\1k\67\1m8\1o9\1q:\1s;\1u<\1w=\1y>\1{?\1}@\1\177" + "A\1\u0081B\1\u0083C\1\u0085D\1\u0087E\1\u0089F\1\u008bG\1\u008dH\1\u008f"
			+ "I\1\u0091J\1\u0093K\1\u0095L\1\u0097M\1\u0099N\1\u009bO\1\u009dP\1\u009f" + "Q\1\u00a1R\1\u00a3S\1\u00a5T\1\u00a7U\1\u00a9V\1\u00abW\1\u00adX\1\u00af"
			+ "Y\1\u00b1Z\1\u00b3[\1\u00b5\\\1\u00b7]\1\u00b9^\1\u00bb_\1\u00bd\2\1\u00bf" + "\2\1\u00c1`\1\u00c3\2\1\u00c5a\1\u00c7b\2\u00c9c\1\u00cbd\3\u00cde\4\3"
			+ "\2\13\4\2\62\63aa\6\2\62;CHaach\4\2\62;aa\5\2&&C\\c|\n\2$$))^^ddhhppt" + "tvv\4\2$$^^\4\2))^^\4\2\f\f\17\17\5\2\13\f\17\17\"\"\u02ee\2\3\3\2\2\2"
			+ "\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2" + "\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2"
			+ "\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2" + "\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2"
			+ "\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2" + "\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2"
			+ "\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W" + "\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2"
			+ "\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2" + "\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}"
			+ "\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2" + "\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2\2\2\u008f"
			+ "\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097\3\2\2" + "\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2\2\2\u00a1"
			+ "\3\2\2\2\2\u00a3\3\2\2\2\2\u00a5\3\2\2\2\2\u00a7\3\2\2\2\2\u00a9\3\2\2" + "\2\2\u00ab\3\2\2\2\2\u00ad\3\2\2\2\2\u00af\3\2\2\2\2\u00b1\3\2\2\2\2\u00b3"
			+ "\3\2\2\2\2\u00b5\3\2\2\2\2\u00b7\3\2\2\2\2\u00b9\3\2\2\2\2\u00bb\3\2\2" + "\2\2\u00c1\3\2\2\2\2\u00c5\3\2\2\2\2\u00c7\3\2\2\2\2\u00c9\3\2\2\2\2\u00cb"
			+ "\3\2\2\2\2\u00cd\3\2\2\2\3\u00cf\3\2\2\2\5\u00d6\3\2\2\2\7\u00df\3\2\2" + "\2\t\u00e1\3\2\2\2\13\u00e7\3\2\2\2\r\u00f2\3\2\2\2\17\u00f8\3\2\2\2\21"
			+ "\u00fa\3\2\2\2\23\u00ff\3\2\2\2\25\u0102\3\2\2\2\27\u010d\3\2\2\2\31\u010f" + "\3\2\2\2\33\u0118\3\2\2\2\35\u011f\3\2\2\2\37\u0122\3\2\2\2!\u0124\3\2"
			+ "\2\2#\u0127\3\2\2\2%\u012a\3\2\2\2\'\u0130\3\2\2\2)\u0132\3\2\2\2+\u013a" + "\3\2\2\2-\u013d\3\2\2\2/\u013f\3\2\2\2\61\u0141\3\2\2\2\63\u0143\3\2\2"
			+ "\2\65\u0146\3\2\2\2\67\u014b\3\2\2\29\u014d\3\2\2\2;\u0155\3\2\2\2=\u0157" + "\3\2\2\2?\u015e\3\2\2\2A\u0166\3\2\2\2C\u016b\3\2\2\2E\u0172\3\2\2\2G"
			+ "\u0174\3\2\2\2I\u0177\3\2\2\2K\u017b\3\2\2\2M\u0183\3\2\2\2O\u0188\3\2" + "\2\2Q\u018a\3\2\2\2S\u0192\3\2\2\2U\u0199\3\2\2\2W\u019b\3\2\2\2Y\u019f"
			+ "\3\2\2\2[\u01a1\3\2\2\2]\u01a3\3\2\2\2_\u01a5\3\2\2\2a\u01a8\3\2\2\2c" + "\u01ab\3\2\2\2e\u01ad\3\2\2\2g\u01af\3\2\2\2i\u01b1\3\2\2\2k\u01b3\3\2"
			+ "\2\2m\u01b6\3\2\2\2o\u01b9\3\2\2\2q\u01bc\3\2\2\2s\u01c0\3\2\2\2u\u01c3" + "\3\2\2\2w\u01c6\3\2\2\2y\u01c8\3\2\2\2{\u01cb\3\2\2\2}\u01cd\3\2\2\2\177"
			+ "\u01d0\3\2\2\2\u0081\u01d2\3\2\2\2\u0083\u01d5\3\2\2\2\u0085\u01d8\3\2" + "\2\2\u0087\u01db\3\2\2\2\u0089\u01de\3\2\2\2\u008b\u01e1\3\2\2\2\u008d"
			+ "\u01e4\3\2\2\2\u008f\u01e7\3\2\2\2\u0091\u01ea\3\2\2\2\u0093\u01ee\3\2" + "\2\2\u0095\u01f3\3\2\2\2\u0097\u01f7\3\2\2\2\u0099\u01f9\3\2\2\2\u009b"
			+ "\u01fb\3\2\2\2\u009d\u01fd\3\2\2\2\u009f\u0203\3\2\2\2\u00a1\u020a\3\2" + "\2\2\u00a3\u0210\3\2\2\2\u00a5\u021c\3\2\2\2\u00a7\u0223\3\2\2\2\u00a9"
			+ "\u0227\3\2\2\2\u00ab\u022b\3\2\2\2\u00ad\u0230\3\2\2\2\u00af\u0237\3\2" + "\2\2\u00b1\u023c\3\2\2\2\u00b3\u0241\3\2\2\2\u00b5\u024b\3\2\2\2\u00b7"
			+ "\u0254\3\2\2\2\u00b9\u025b\3\2\2\2\u00bb\u0285\3\2\2\2\u00bd\u0287\3\2" + "\2\2\u00bf\u028b\3\2\2\2\u00c1\u028d\3\2\2\2\u00c3\u0294\3\2\2\2\u00c5"
			+ "\u02a9\3\2\2\2\u00c7\u02ab\3\2\2\2\u00c9\u02b9\3\2\2\2\u00cb\u02c5\3\2" + "\2\2\u00cd\u02d7\3\2\2\2\u00cf\u00d0\7t\2\2\u00d0\u00d1\7g\2\2\u00d1\u00d2"
			+ "\7e\2\2\u00d2\u00d3\7q\2\2\u00d3\u00d4\7t\2\2\u00d4\u00d5\7f\2\2\u00d5" + "\4\3\2\2\2\u00d6\u00d7\7t\2\2\u00d7\u00d8\7g\2\2\u00d8\u00d9\7i\2\2\u00d9"
			+ "\u00da\7k\2\2\u00da\u00db\7u\2\2\u00db\u00dc\7v\2\2\u00dc\u00dd\7g\2\2" + "\u00dd\u00de\7t\2\2\u00de\6\3\2\2\2\u00df\u00e0\7]\2\2\u00e0\b\3\2\2\2"
			+ "\u00e1\u00e2\7r\2\2\u00e2\u00e3\7c\2\2\u00e3\u00e4\7t\2\2\u00e4\u00e5" + "\7c\2\2\u00e5\u00e6\7o\2\2\u00e6\n\3\2\2\2\u00e7\u00e8\7u\2\2\u00e8\u00e9"
			+ "\7w\2\2\u00e9\u00ea\7d\2\2\u00ea\u00eb\7u\2\2\u00eb\u00ec\7v\2\2\u00ec" + "\u00ed\7k\2\2\u00ed\u00ee\7v\2\2\u00ee\u00ef\7w\2\2\u00ef\u00f0\7v\2\2"
			+ "\u00f0\u00f1\7g\2\2\u00f1\f\3\2\2\2\u00f2\u00f3\7k\2\2\u00f3\u00f4\7p" + "\2\2\u00f4\u00f5\7q\2\2\u00f5\u00f6\7w\2\2\u00f6\u00f7\7v\2\2\u00f7\16"
			+ "\3\2\2\2\u00f8\u00f9\7\177\2\2\u00f9\20\3\2\2\2\u00fa\u00fb\7e\2\2\u00fb" + "\u00fc\7c\2\2\u00fc\u00fd\7u\2\2\u00fd\u00fe\7g\2\2\u00fe\22\3\2\2\2\u00ff"
			+ "\u0100\7/\2\2\u0100\u0101\7@\2\2\u0101\24\3\2\2\2\u0102\u0103\7u\2\2\u0103" + "\u0104\7k\2\2\u0104\u0105\7o\2\2\u0105\u0106\7w\2\2\u0106\u0107\7n\2\2"
			+ "\u0107\u0108\7c\2\2\u0108\u0109\7v\2\2\u0109\u010a\7k\2\2\u010a\u010b" + "\7q\2\2\u010b\u010c\7p\2\2\u010c\26\3\2\2\2\u010d\u010e\7+\2\2\u010e\30"
			+ "\3\2\2\2\u010f\u0110\7i\2\2\u0110\u0111\7g\2\2\u0111\u0112\7p\2\2\u0112" + "\u0113\7g\2\2\u0113\u0114\7t\2\2\u0114\u0115\7c\2\2\u0115\u0116\7v\2\2"
			+ "\u0116\u0117\7g\2\2\u0117\32\3\2\2\2\u0118\u0119\7k\2\2\u0119\u011a\7" + "p\2\2\u011a\u011b\7n\2\2\u011b\u011c\7k\2\2\u011c\u011d\7p\2\2\u011d\u011e"
			+ "\7g\2\2\u011e\34\3\2\2\2\u011f\u0120\7/\2\2\u0120\u0121\7<\2\2\u0121\36" + "\3\2\2\2\u0122\u0123\7B\2\2\u0123 \3\2\2\2\u0124\u0125\7-\2\2\u0125\u0126"
			+ "\7<\2\2\u0126\"\3\2\2\2\u0127\u0128\7\60\2\2\u0128\u0129\7,\2\2\u0129" + "$\3\2\2\2\u012a\u012b\7e\2\2\u012b\u012c\7q\2\2\u012c\u012d\7p\2\2\u012d"
			+ "\u012e\7u\2\2\u012e\u012f\7v\2\2\u012f&\3\2\2\2\u0130\u0131\7_\2\2\u0131" + "(\3\2\2\2\u0132\u0133\7f\2\2\u0133\u0134\7g\2\2\u0134\u0135\7h\2\2\u0135"
			+ "\u0136\7c\2\2\u0136\u0137\7w\2\2\u0137\u0138\7n\2\2\u0138\u0139\7v\2\2" + "\u0139*\3\2\2\2\u013a\u013b\7k\2\2\u013b\u013c\7p\2\2\u013c,\3\2\2\2\u013d"
			+ "\u013e\7.\2\2\u013e.\3\2\2\2\u013f\u0140\7<\2\2\u0140\60\3\2\2\2\u0141" + "\u0142\7*\2\2\u0142\62\3\2\2\2\u0143\u0144\7k\2\2\u0144\u0145\7h\2\2\u0145"
			+ "\64\3\2\2\2\u0146\u0147\7&\2\2\u0147\u0148\7t\2\2\u0148\u0149\7u\2\2\u0149" + "\u014a\7v\2\2\u014a\66\3\2\2\2\u014b\u014c\7A\2\2\u014c8\3\2\2\2\u014d"
			+ "\u014e\7r\2\2\u014e\u014f\7c\2\2\u014f\u0150\7e\2\2\u0150\u0151\7m\2\2" + "\u0151\u0152\7c\2\2\u0152\u0153\7i\2\2\u0153\u0154\7g\2\2\u0154:\3\2\2"
			+ "\2\u0155\u0156\7}\2\2\u0156<\3\2\2\2\u0157\u0158\7p\2\2\u0158\u0159\7" + "c\2\2\u0159\u015a\7v\2\2\u015a\u015b\7k\2\2\u015b\u015c\7x\2\2\u015c\u015d"
			+ "\7g\2\2\u015d>\3\2\2\2\u015e\u015f\7g\2\2\u015f\u0160\7z\2\2\u0160\u0161" + "\7v\2\2\u0161\u0162\7g\2\2\u0162\u0163\7p\2\2\u0163\u0164\7f\2\2\u0164"
			+ "\u0165\7u\2\2\u0165@\3\2\2\2\u0166\u0167\7g\2\2\u0167\u0168\7n\2\2\u0168" + "\u0169\7u\2\2\u0169\u016a\7g\2\2\u016aB\3\2\2\2\u016b\u016c\7k\2\2\u016c"
			+ "\u016d\7o\2\2\u016d\u016e\7r\2\2\u016e\u016f\7q\2\2\u016f\u0170\7t\2\2" + "\u0170\u0171\7v\2\2\u0171D\3\2\2\2\u0172\u0173\7\60\2\2\u0173F\3\2\2\2"
			+ "\u0174\u0175\7?\2\2\u0175\u0176\7@\2\2\u0176H\3\2\2\2\u0177\u0178\7h\2" + "\2\u0178\u0179\7q\2\2\u0179\u017a\7t\2\2\u017aJ\3\2\2\2\u017b\u017c\7"
			+ "r\2\2\u017c\u017d\7t\2\2\u017d\u017e\7q\2\2\u017e\u017f\7e\2\2\u017f\u0180" + "\7g\2\2\u0180\u0181\7u\2\2\u0181\u0182\7u\2\2\u0182L\3\2\2\2\u0183\u0184"
			+ "\7&\2\2\u0184\u0185\7e\2\2\u0185\u0186\7n\2\2\u0186\u0187\7m\2\2\u0187" + "N\3\2\2\2\u0188\u0189\7=\2\2\u0189P\3\2\2\2\u018a\u018b\7k\2\2\u018b\u018c"
			+ "\7p\2\2\u018c\u018d\7e\2\2\u018d\u018e\7n\2\2\u018e\u018f\7w\2\2\u018f" + "\u0190\7f\2\2\u0190\u0191\7g\2\2\u0191R\3\2\2\2\u0192\u0193\7u\2\2\u0193"
			+ "\u0194\7y\2\2\u0194\u0195\7k\2\2\u0195\u0196\7v\2\2\u0196\u0197\7e\2\2" + "\u0197\u0198\7j\2\2\u0198T\3\2\2\2\u0199\u019a\7%\2\2\u019aV\3\2\2\2\u019b"
			+ "\u019c\7q\2\2\u019c\u019d\7w\2\2\u019d\u019e\7v\2\2\u019eX\3\2\2\2\u019f" + "\u01a0\7(\2\2\u01a0Z\3\2\2\2\u01a1\u01a2\7~\2\2\u01a2\\\3\2\2\2\u01a3"
			+ "\u01a4\7`\2\2\u01a4^\3\2\2\2\u01a5\u01a6\7(\2\2\u01a6\u01a7\7(\2\2\u01a7" + "`\3\2\2\2\u01a8\u01a9\7~\2\2\u01a9\u01aa\7~\2\2\u01aab\3\2\2\2\u01ab\u01ac"
			+ "\7,\2\2\u01acd\3\2\2\2\u01ad\u01ae\7\61\2\2\u01aef\3\2\2\2\u01af\u01b0" + "\7-\2\2\u01b0h\3\2\2\2\u01b1\u01b2\7\'\2\2\u01b2j\3\2\2\2\u01b3\u01b4"
			+ "\7,\2\2\u01b4\u01b5\7,\2\2\u01b5l\3\2\2\2\u01b6\u01b7\7>\2\2\u01b7\u01b8" + "\7>\2\2\u01b8n\3\2\2\2\u01b9\u01ba\7@\2\2\u01ba\u01bb\7@\2\2\u01bbp\3"
			+ "\2\2\2\u01bc\u01bd\7@\2\2\u01bd\u01be\7@\2\2\u01be\u01bf\7@\2\2\u01bf" + "r\3\2\2\2\u01c0\u01c1\7?\2\2\u01c1\u01c2\7?\2\2\u01c2t\3\2\2\2\u01c3\u01c4"
			+ "\7#\2\2\u01c4\u01c5\7?\2\2\u01c5v\3\2\2\2\u01c6\u01c7\7>\2\2\u01c7x\3" + "\2\2\2\u01c8\u01c9\7>\2\2\u01c9\u01ca\7?\2\2\u01caz\3\2\2\2\u01cb\u01cc"
			+ "\7@\2\2\u01cc|\3\2\2\2\u01cd\u01ce\7@\2\2\u01ce\u01cf\7?\2\2\u01cf~\3" + "\2\2\2\u01d0\u01d1\7?\2\2\u01d1\u0080\3\2\2\2\u01d2\u01d3\7-\2\2\u01d3"
			+ "\u01d4\7?\2\2\u01d4\u0082\3\2\2\2\u01d5\u01d6\7/\2\2\u01d6\u01d7\7?\2" + "\2\u01d7\u0084\3\2\2\2\u01d8\u01d9\7,\2\2\u01d9\u01da\7?\2\2\u01da\u0086"
			+ "\3\2\2\2\u01db\u01dc\7\61\2\2\u01dc\u01dd\7?\2\2\u01dd\u0088\3\2\2\2\u01de" + "\u01df\7\'\2\2\u01df\u01e0\7?\2\2\u01e0\u008a\3\2\2\2\u01e1\u01e2\7(\2"
			+ "\2\u01e2\u01e3\7?\2\2\u01e3\u008c\3\2\2\2\u01e4\u01e5\7`\2\2\u01e5\u01e6" + "\7?\2\2\u01e6\u008e\3\2\2\2\u01e7\u01e8\7~\2\2\u01e8\u01e9\7?\2\2\u01e9"
			+ "\u0090\3\2\2\2\u01ea\u01eb\7>\2\2\u01eb\u01ec\7>\2\2\u01ec\u01ed\7?\2" + "\2\u01ed\u0092\3\2\2\2\u01ee\u01ef\7@\2\2\u01ef\u01f0\7@\2\2\u01f0\u01f1"
			+ "\7@\2\2\u01f1\u01f2\7?\2\2\u01f2\u0094\3\2\2\2\u01f3\u01f4\7@\2\2\u01f4" + "\u01f5\7@\2\2\u01f5\u01f6\7?\2\2\u01f6\u0096\3\2\2\2\u01f7\u01f8\7/\2"
			+ "\2\u01f8\u0098\3\2\2\2\u01f9\u01fa\7\u0080\2\2\u01fa\u009a\3\2\2\2\u01fb" + "\u01fc\7#\2\2\u01fc\u009c\3\2\2\2\u01fd\u01fe\7k\2\2\u01fe\u01ff\7p\2"
			+ "\2\u01ff\u0200\7v\2\2\u0200\u0201\7>\2\2\u0201\u0202\7@\2\2\u0202\u009e" + "\3\2\2\2\u0203\u0204\7w\2\2\u0204\u0205\7k\2\2\u0205\u0206\7p\2\2\u0206"
			+ "\u0207\7v\2\2\u0207\u0208\7>\2\2\u0208\u0209\7@\2\2\u0209\u00a0\3\2\2" + "\2\u020a\u020b\7d\2\2\u020b\u020c\7k\2\2\u020c\u020d\7v\2\2\u020d\u020e"
			+ "\7>\2\2\u020e\u020f\7@\2\2\u020f\u00a2\3\2\2\2\u0210\u0211\7k\2\2\u0211" + "\u0212\7p\2\2\u0212\u0213\7v\2\2\u0213\u0214\7g\2\2\u0214\u0215\7t\2\2"
			+ "\u0215\u0216\7h\2\2\u0216\u0217\7c\2\2\u0217\u0218\7e\2\2\u0218\u0219" + "\7g\2\2\u0219\u021a\7>\2\2\u021a\u021b\7@\2\2\u021b\u00a4\3\2\2\2\u021c"
			+ "\u021d\7g\2\2\u021d\u021e\7p\2\2\u021e\u021f\7w\2\2\u021f\u0220\7o\2\2" + "\u0220\u0221\7>\2\2\u0221\u0222\7@\2\2\u0222\u00a6\3\2\2\2\u0223\u0224"
			+ "\7d\2\2\u0224\u0225\7k\2\2\u0225\u0226\7v\2\2\u0226\u00a8\3\2\2\2\u0227" + "\u0228\7k\2\2\u0228\u0229\7p\2\2\u0229\u022a\7v\2\2\u022a\u00aa\3\2\2"
			+ "\2\u022b\u022c\7w\2\2\u022c\u022d\7k\2\2\u022d\u022e\7p\2\2\u022e\u022f" + "\7v\2\2\u022f\u00ac\3\2\2\2\u0230\u0231\7u\2\2\u0231\u0232\7v\2\2\u0232"
			+ "\u0233\7t\2\2\u0233\u0234\7k\2\2\u0234\u0235\7p\2\2\u0235\u0236\7i\2\2" + "\u0236\u00ae\3\2\2\2\u0237\u0238\7d\2\2\u0238\u0239\7q\2\2\u0239\u023a"
			+ "\7q\2\2\u023a\u023b\7n\2\2\u023b\u00b0\3\2\2\2\u023c\u023d\7g\2\2\u023d" + "\u023e\7p\2\2\u023e\u023f\7w\2\2\u023f\u0240\7o\2\2\u0240\u00b2\3\2\2"
			+ "\2\u0241\u0242\7k\2\2\u0242\u0243\7p\2\2\u0243\u0244\7v\2\2\u0244\u0245" + "\7g\2\2\u0245\u0246\7t\2\2\u0246\u0247\7h\2\2\u0247\u0248\7c\2\2\u0248"
			+ "\u0249\7e\2\2\u0249\u024a\7g\2\2\u024a\u00b4\3\2\2\2\u024b\u024c\7h\2" + "\2\u024c\u024d\7w\2\2\u024d\u024e\7p\2\2\u024e\u024f\7e\2\2\u024f\u0250"
			+ "\7v\2\2\u0250\u0251\7k\2\2\u0251\u0252\7q\2\2\u0252\u0253\7p\2\2\u0253" + "\u00b6\3\2\2\2\u0254\u0255\7o\2\2\u0255\u0256\7q\2\2\u0256\u0257\7f\2"
			+ "\2\u0257\u0258\7w\2\2\u0258\u0259\7n\2\2\u0259\u025a\7g\2\2\u025a\u00b8" + "\3\2\2\2\u025b\u025c\7v\2\2\u025c\u025d\7g\2\2\u025d\u025e\7u\2\2\u025e"
			+ "\u025f\7v\2\2\u025f\u0260\7d\2\2\u0260\u0261\7g\2\2\u0261\u0262\7p\2\2" + "\u0262\u0263\7e\2\2\u0263\u0264\7j\2\2\u0264\u00ba\3\2\2\2\u0265\u0266"
			+ "\7\62\2\2\u0266\u0267\7d\2\2\u0267\u0269\3\2\2\2\u0268\u026a\t\2\2\2\u0269" + "\u0268\3\2\2\2\u026a\u026b\3\2\2\2\u026b\u0269\3\2\2\2\u026b\u026c\3\2"
			+ "\2\2\u026c\u0286\3\2\2\2\u026d\u026e\7\62\2\2\u026e\u026f\7z\2\2\u026f" + "\u0271\3\2\2\2\u0270\u0272\t\3\2\2\u0271\u0270\3\2\2\2\u0272\u0273\3\2"
			+ "\2\2\u0273\u0271\3\2\2\2\u0273\u0274\3\2\2\2\u0274\u0286\3\2\2\2\u0275" + "\u0277\4\63;\2\u0276\u0278\t\4\2\2\u0277\u0276\3\2\2\2\u0278\u0279\3\2"
			+ "\2\2\u0279\u0277\3\2\2\2\u0279\u027a\3\2\2\2\u027a\u0286\3\2\2\2\u027b" + "\u0286\4\62;\2\u027c\u027d\7v\2\2\u027d\u027e\7t\2\2\u027e\u027f\7w\2"
			+ "\2\u027f\u0286\7g\2\2\u0280\u0281\7h\2\2\u0281\u0282\7c\2\2\u0282\u0283" + "\7n\2\2\u0283\u0284\7u\2\2\u0284\u0286\7g\2\2\u0285\u0265\3\2\2\2\u0285"
			+ "\u026d\3\2\2\2\u0285\u0275\3\2\2\2\u0285\u027b\3\2\2\2\u0285\u027c\3\2" + "\2\2\u0285\u0280\3\2\2\2\u0286\u00bc\3\2\2\2\u0287\u0288\t\5\2\2\u0288"
			+ "\u00be\3\2\2\2\u0289\u028c\5\u00bd_\2\u028a\u028c\t\4\2\2\u028b\u0289" + "\3\2\2\2\u028b\u028a\3\2\2\2\u028c\u00c0\3\2\2\2\u028d\u0291\5\u00bd_"
			+ "\2\u028e\u0290\5\u00bf`\2\u028f\u028e\3\2\2\2\u0290\u0293\3\2\2\2\u0291" + "\u028f\3\2\2\2\u0291\u0292\3\2\2\2\u0292\u00c2\3\2\2\2\u0293\u0291\3\2"
			+ "\2\2\u0294\u0295\7^\2\2\u0295\u0296\t\6\2\2\u0296\u00c4\3\2\2\2\u0297" + "\u029c\7$\2\2\u0298\u029b\5\u00c3b\2\u0299\u029b\n\7\2\2\u029a\u0298\3"
			+ "\2\2\2\u029a\u0299\3\2\2\2\u029b\u029e\3\2\2\2\u029c\u029a\3\2\2\2\u029c" + "\u029d\3\2\2\2\u029d\u029f\3\2\2\2\u029e\u029c\3\2\2\2\u029f\u02aa\7$"
			+ "\2\2\u02a0\u02a5\7)\2\2\u02a1\u02a4\5\u00c3b\2\u02a2\u02a4\n\b\2\2\u02a3" + "\u02a1\3\2\2\2\u02a3\u02a2\3\2\2\2\u02a4\u02a7\3\2\2\2\u02a5\u02a3\3\2"
			+ "\2\2\u02a5\u02a6\3\2\2\2\u02a6\u02a8\3\2\2\2\u02a7\u02a5\3\2\2\2\u02a8" + "\u02aa\7)\2\2\u02a9\u0297\3\2\2\2\u02a9\u02a0\3\2\2\2\u02aa\u00c6\3\2"
			+ "\2\2\u02ab\u02ac\7\61\2\2\u02ac\u02ad\7,\2\2\u02ad\u02b1\3\2\2\2\u02ae" + "\u02b0\13\2\2\2\u02af\u02ae\3\2\2\2\u02b0\u02b3\3\2\2\2\u02b1\u02b2\3"
			+ "\2\2\2\u02b1\u02af\3\2\2\2\u02b2\u02b4\3\2\2\2\u02b3\u02b1\3\2\2\2\u02b4" + "\u02b5\7,\2\2\u02b5\u02b6\7\61\2\2\u02b6\u02b7\3\2\2\2\u02b7\u02b8\bd"
			+ "\2\2\u02b8\u00c8\3\2\2\2\u02b9\u02ba\7>\2\2\u02ba\u02bb\7]\2\2\u02bb\u02bf" + "\3\2\2\2\u02bc\u02be\13\2\2\2\u02bd\u02bc\3\2\2\2\u02be\u02c1\3\2\2\2"
			+ "\u02bf\u02c0\3\2\2\2\u02bf\u02bd\3\2\2\2\u02c0\u02c2\3\2\2\2\u02c1\u02bf" + "\3\2\2\2\u02c2\u02c3\7_\2\2\u02c3\u02c4\7@\2\2\u02c4\u00ca\3\2\2\2\u02c5"
			+ "\u02c6\7\61\2\2\u02c6\u02c7\7\61\2\2\u02c7\u02cb\3\2\2\2\u02c8\u02ca\n" + "\t\2\2\u02c9\u02c8\3\2\2\2\u02ca\u02cd\3\2\2\2\u02cb\u02c9\3\2\2\2\u02cb"
			+ "\u02cc\3\2\2\2\u02cc\u02d2\3\2\2\2\u02cd\u02cb\3\2\2\2\u02ce\u02d0\7\17" + "\2\2\u02cf\u02ce\3\2\2\2\u02cf\u02d0\3\2\2\2\u02d0\u02d1\3\2\2\2\u02d1"
			+ "\u02d3\7\f\2\2\u02d2\u02cf\3\2\2\2\u02d2\u02d3\3\2\2\2\u02d3\u02d4\3\2" + "\2\2\u02d4\u02d5\bf\3\2\u02d5\u00cc\3\2\2\2\u02d6\u02d8\t\n\2\2\u02d7"
			+ "\u02d6\3\2\2\2\u02d8\u02d9\3\2\2\2\u02d9\u02d7\3\2\2\2\u02d9\u02da\3\2" + "\2\2\u02da\u02db\3\2\2\2\u02db\u02dc\bg\4\2\u02dc\u00ce\3\2\2\2\24\2\u026b"
			+ "\u0273\u0279\u0285\u028b\u0291\u029a\u029c\u02a3\u02a5\u02a9\u02b1\u02bf" + "\u02cb\u02cf\u02d2\u02d9";
	public static final ATN _ATN = ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
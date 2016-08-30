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

import java.util.List;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class MemoryModelParser extends Parser {
	static {
		RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION);
	}

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9, T__9 = 10, T__10 = 11, T__11 = 12, T__12 = 13, T__13 = 14,
			T__14 = 15, T__15 = 16, T__16 = 17, T__17 = 18, T__18 = 19, T__19 = 20, T__20 = 21, T__21 = 22, T__22 = 23, T__23 = 24, T__24 = 25, T__25 = 26, T__26 = 27, T__27 = 28,
			ID = 29, NUMBER = 30, COMMENT = 31, WS = 32;
	public static final int RULE_unit = 0, RULE_declaration = 1, RULE_row = 2, RULE_rowID = 3, RULE_constant = 4, RULE_filling = 5, RULE_column = 6, RULE_alias = 7,
			RULE_memory = 8, RULE_definition = 9, RULE_warnType = 10, RULE_rwStatus = 11, RULE_width = 12, RULE_type = 13, RULE_reference = 14;
	public static final String[] ruleNames = { "unit", "declaration", "row", "rowID", "constant", "filling", "column", "alias", "memory", "definition", "warnType", "rwStatus",
			"width", "type", "reference" };

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
	public ATN getATN() {
		return _ATN;
	}

	public MemoryModelParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
	}

	public static class UnitContext extends ParserRuleContext {
		public MemoryContext memory() {
			return getRuleContext(MemoryContext.class, 0);
		}

		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}

		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class, i);
		}

		public UnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_unit;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterUnit(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitUnit(this);
			}
		}
	}

	public final UnitContext unit() throws RecognitionException {
		final UnitContext _localctx = new UnitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_unit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(33);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << T__0) | (1L << T__4) | (1L << T__12) | (1L << T__13))) != 0))) {
					{
						{
							setState(30);
							declaration();
						}
					}
					setState(35);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				{
					setState(36);
					memory();
				}
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclarationContext extends ParserRuleContext {
		public RowContext row() {
			return getRuleContext(RowContext.class, 0);
		}

		public ColumnContext column() {
			return getRuleContext(ColumnContext.class, 0);
		}

		public AliasContext alias() {
			return getRuleContext(AliasContext.class, 0);
		}

		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class, 0);
		}

		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_declaration;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterDeclaration(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitDeclaration(this);
			}
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		final DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(42);
				switch (_input.LA(1)) {
				case T__0: {
					{
						setState(38);
						row();
					}
				}
					break;
				case T__12: {
					{
						setState(39);
						column();
					}
				}
					break;
				case T__13: {
					{
						setState(40);
						alias();
					}
				}
					break;
				case T__4: {
					{
						setState(41);
						constant();
					}
				}
					break;
				default:
					throw new NoViableAltException(this);
				}
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RowContext extends ParserRuleContext {
		public RowIDContext rowID() {
			return getRuleContext(RowIDContext.class, 0);
		}

		public List<FillingContext> filling() {
			return getRuleContexts(FillingContext.class);
		}

		public FillingContext filling(int i) {
			return getRuleContext(FillingContext.class, i);
		}

		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}

		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class, i);
		}

		public List<ReferenceContext> reference() {
			return getRuleContexts(ReferenceContext.class);
		}

		public ReferenceContext reference(int i) {
			return getRuleContext(ReferenceContext.class, i);
		}

		public RowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_row;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterRow(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitRow(this);
			}
		}
	}

	public final RowContext row() throws RecognitionException {
		final RowContext _localctx = new RowContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_row);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(44);
				match(T__0);
				setState(45);
				rowID();
				setState(46);
				match(T__1);
				setState(52);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << T__9) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << ID))) != 0))) {
					{
						setState(50);
						switch (_input.LA(1)) {
						case T__9: {
							{
								setState(47);
								filling();
							}
						}
							break;
						case T__22:
						case T__23:
						case T__24: {
							{
								setState(48);
								definition();
							}
						}
							break;
						case ID: {
							{
								setState(49);
								reference();
							}
						}
							break;
						default:
							throw new NoViableAltException(this);
						}
					}
					setState(54);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(55);
				match(T__2);
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RowIDContext extends ParserRuleContext {
		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public RowIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_rowID;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterRowID(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitRowID(this);
			}
		}
	}

	public final RowIDContext rowID() throws RecognitionException {
		final RowIDContext _localctx = new RowIDContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_rowID);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(58);
				_la = _input.LA(1);
				if (_la == T__3) {
					{
						setState(57);
						match(T__3);
					}
				}

				setState(60);
				match(ID);
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public Token value;

		public TerminalNode NUMBER() {
			return getToken(MemoryModelParser.NUMBER, 0);
		}

		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_constant;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterConstant(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitConstant(this);
			}
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		final ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_constant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(62);
				match(T__4);
				setState(64);
				_la = _input.LA(1);
				if (_la == ID) {
					{
						setState(63);
						match(ID);
					}
				}

				setState(66);
				_localctx.value = _input.LT(1);
				_la = _input.LA(1);
				if (!(((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << NUMBER))) != 0)))) {
					_localctx.value = _errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(67);
				match(T__8);
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FillingContext extends ParserRuleContext {
		public WidthContext width() {
			return getRuleContext(WidthContext.class, 0);
		}

		public FillingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_filling;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterFilling(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitFilling(this);
			}
		}
	}

	public final FillingContext filling() throws RecognitionException {
		final FillingContext _localctx = new FillingContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_filling);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(69);
				match(T__9);
				setState(74);
				_la = _input.LA(1);
				if (_la == T__10) {
					{
						setState(70);
						match(T__10);
						setState(71);
						width();
						setState(72);
						match(T__11);
					}
				}

				setState(76);
				match(T__8);
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnContext extends ParserRuleContext {
		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public List<ReferenceContext> reference() {
			return getRuleContexts(ReferenceContext.class);
		}

		public ReferenceContext reference(int i) {
			return getRuleContext(ReferenceContext.class, i);
		}

		public ColumnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_column;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterColumn(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitColumn(this);
			}
		}
	}

	public final ColumnContext column() throws RecognitionException {
		final ColumnContext _localctx = new ColumnContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_column);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(78);
				match(T__12);
				setState(79);
				match(ID);
				setState(80);
				match(T__1);
				setState(84);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == ID) {
					{
						{
							setState(81);
							reference();
						}
					}
					setState(86);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(87);
				match(T__2);
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AliasContext extends ParserRuleContext {
		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}

		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class, i);
		}

		public List<ReferenceContext> reference() {
			return getRuleContexts(ReferenceContext.class);
		}

		public ReferenceContext reference(int i) {
			return getRuleContext(ReferenceContext.class, i);
		}

		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_alias;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterAlias(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitAlias(this);
			}
		}
	}

	public final AliasContext alias() throws RecognitionException {
		final AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_alias);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(89);
				match(T__13);
				setState(90);
				match(ID);
				setState(91);
				match(T__1);
				setState(96);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << ID))) != 0))) {
					{
						setState(94);
						switch (_input.LA(1)) {
						case T__22:
						case T__23:
						case T__24: {
							{
								setState(92);
								definition();
							}
						}
							break;
						case ID: {
							{
								setState(93);
								reference();
							}
						}
							break;
						default:
							throw new NoViableAltException(this);
						}
					}
					setState(98);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(99);
				match(T__2);
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MemoryContext extends ParserRuleContext {
		public List<ReferenceContext> reference() {
			return getRuleContexts(ReferenceContext.class);
		}

		public ReferenceContext reference(int i) {
			return getRuleContext(ReferenceContext.class, i);
		}

		public MemoryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_memory;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterMemory(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitMemory(this);
			}
		}
	}

	public final MemoryContext memory() throws RecognitionException {
		final MemoryContext _localctx = new MemoryContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_memory);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(101);
				match(T__14);
				setState(102);
				match(T__1);
				setState(106);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == ID) {
					{
						{
							setState(103);
							reference();
						}
					}
					setState(108);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(109);
				match(T__2);
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefinitionContext extends ParserRuleContext {
		public Token hasRegister;

		public RwStatusContext rwStatus() {
			return getRuleContext(RwStatusContext.class, 0);
		}

		public TypeContext type() {
			return getRuleContext(TypeContext.class, 0);
		}

		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public WidthContext width() {
			return getRuleContext(WidthContext.class, 0);
		}

		public List<TerminalNode> NUMBER() {
			return getTokens(MemoryModelParser.NUMBER);
		}

		public TerminalNode NUMBER(int i) {
			return getToken(MemoryModelParser.NUMBER, i);
		}

		public WarnTypeContext warnType() {
			return getRuleContext(WarnTypeContext.class, 0);
		}

		public DefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_definition;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterDefinition(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitDefinition(this);
			}
		}
	}

	public final DefinitionContext definition() throws RecognitionException {
		final DefinitionContext _localctx = new DefinitionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(111);
				rwStatus();
				setState(113);
				_la = _input.LA(1);
				if (_la == T__15) {
					{
						setState(112);
						_localctx.hasRegister = match(T__15);
					}
				}

				setState(115);
				type();
				setState(120);
				_la = _input.LA(1);
				if (_la == T__10) {
					{
						setState(116);
						match(T__10);
						setState(117);
						width();
						setState(118);
						match(T__11);
					}
				}

				setState(122);
				match(ID);
				setState(128);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == T__16) {
					{
						{
							setState(123);
							match(T__16);
							setState(124);
							match(NUMBER);
							setState(125);
							match(T__17);
						}
					}
					setState(130);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(132);
				_la = _input.LA(1);
				if (((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21))) != 0))) {
					{
						setState(131);
						warnType();
					}
				}

				setState(134);
				match(T__8);
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WarnTypeContext extends ParserRuleContext {
		public Token silent;
		public Token typeString;

		public WarnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_warnType;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterWarnType(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitWarnType(this);
			}
		}
	}

	public final WarnTypeContext warnType() throws RecognitionException {
		final WarnTypeContext _localctx = new WarnTypeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_warnType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(137);
				_la = _input.LA(1);
				if (_la == T__18) {
					{
						setState(136);
						_localctx.silent = match(T__18);
					}
				}

				setState(139);
				_localctx.typeString = _input.LT(1);
				_la = _input.LA(1);
				if (!(((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << T__19) | (1L << T__20) | (1L << T__21))) != 0)))) {
					_localctx.typeString = _errHandler.recoverInline(this);
				} else {
					consume();
				}
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RwStatusContext extends ParserRuleContext {
		public RwStatusContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_rwStatus;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterRwStatus(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitRwStatus(this);
			}
		}
	}

	public final RwStatusContext rwStatus() throws RecognitionException {
		final RwStatusContext _localctx = new RwStatusContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_rwStatus);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(141);
				_la = _input.LA(1);
				if (!(((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << T__22) | (1L << T__23) | (1L << T__24))) != 0)))) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WidthContext extends ParserRuleContext {
		public TerminalNode NUMBER() {
			return getToken(MemoryModelParser.NUMBER, 0);
		}

		public WidthContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_width;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterWidth(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitWidth(this);
			}
		}
	}

	public final WidthContext width() throws RecognitionException {
		final WidthContext _localctx = new WidthContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_width);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(143);
				match(NUMBER);
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_type;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterType(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitType(this);
			}
		}
	}

	public final TypeContext type() throws RecognitionException {
		final TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(145);
				_la = _input.LA(1);
				if (!(((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << T__25) | (1L << T__26) | (1L << T__27))) != 0)))) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReferenceContext extends ParserRuleContext {
		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public List<TerminalNode> NUMBER() {
			return getTokens(MemoryModelParser.NUMBER);
		}

		public TerminalNode NUMBER(int i) {
			return getToken(MemoryModelParser.NUMBER, i);
		}

		public ReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_reference;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).enterReference(this);
			}
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener) {
				((MemoryModelListener) listener).exitReference(this);
			}
		}
	}

	public final ReferenceContext reference() throws RecognitionException {
		final ReferenceContext _localctx = new ReferenceContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_reference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(147);
				match(ID);
				setState(153);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == T__16) {
					{
						{
							setState(148);
							match(T__16);
							setState(149);
							match(NUMBER);
							setState(150);
							match(T__17);
						}
					}
					setState(155);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(156);
				match(T__8);
			}
		} catch (final RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\"\u00a1\4\2\t\2\4"
			+ "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" + "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\7\2\"\n\2\f\2\16"
			+ "\2%\13\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3-\n\3\3\4\3\4\3\4\3\4\3\4\3\4\7\4" + "\65\n\4\f\4\16\48\13\4\3\4\3\4\3\5\5\5=\n\5\3\5\3\5\3\6\3\6\5\6C\n\6\3"
			+ "\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\5\7M\n\7\3\7\3\7\3\b\3\b\3\b\3\b\7\bU\n" + "\b\f\b\16\bX\13\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\7\ta\n\t\f\t\16\td\13\t"
			+ "\3\t\3\t\3\n\3\n\3\n\7\nk\n\n\f\n\16\nn\13\n\3\n\3\n\3\13\3\13\5\13t\n" + "\13\3\13\3\13\3\13\3\13\3\13\5\13{\n\13\3\13\3\13\3\13\3\13\7\13\u0081"
			+ "\n\13\f\13\16\13\u0084\13\13\3\13\5\13\u0087\n\13\3\13\3\13\3\f\5\f\u008c" + "\n\f\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20\7\20\u009a"
			+ "\n\20\f\20\16\20\u009d\13\20\3\20\3\20\3\20\2\2\21\2\4\6\b\n\f\16\20\22" + "\24\26\30\32\34\36\2\6\4\2\b\n  \3\2\26\30\3\2\31\33\3\2\34\36\u00a5\2"
			+ "#\3\2\2\2\4,\3\2\2\2\6.\3\2\2\2\b<\3\2\2\2\n@\3\2\2\2\fG\3\2\2\2\16P\3" + "\2\2\2\20[\3\2\2\2\22g\3\2\2\2\24q\3\2\2\2\26\u008b\3\2\2\2\30\u008f\3"
			+ "\2\2\2\32\u0091\3\2\2\2\34\u0093\3\2\2\2\36\u0095\3\2\2\2 \"\5\4\3\2!" + " \3\2\2\2\"%\3\2\2\2#!\3\2\2\2#$\3\2\2\2$&\3\2\2\2%#\3\2\2\2&\'\5\22\n"
			+ "\2\'\3\3\2\2\2(-\5\6\4\2)-\5\16\b\2*-\5\20\t\2+-\5\n\6\2,(\3\2\2\2,)\3" + "\2\2\2,*\3\2\2\2,+\3\2\2\2-\5\3\2\2\2./\7\3\2\2/\60\5\b\5\2\60\66\7\4"
			+ "\2\2\61\65\5\f\7\2\62\65\5\24\13\2\63\65\5\36\20\2\64\61\3\2\2\2\64\62" + "\3\2\2\2\64\63\3\2\2\2\658\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2\2\679\3\2"
			+ "\2\28\66\3\2\2\29:\7\5\2\2:\7\3\2\2\2;=\7\6\2\2<;\3\2\2\2<=\3\2\2\2=>" + "\3\2\2\2>?\7\37\2\2?\t\3\2\2\2@B\7\7\2\2AC\7\37\2\2BA\3\2\2\2BC\3\2\2"
			+ "\2CD\3\2\2\2DE\t\2\2\2EF\7\13\2\2F\13\3\2\2\2GL\7\f\2\2HI\7\r\2\2IJ\5" + "\32\16\2JK\7\16\2\2KM\3\2\2\2LH\3\2\2\2LM\3\2\2\2MN\3\2\2\2NO\7\13\2\2"
			+ "O\r\3\2\2\2PQ\7\17\2\2QR\7\37\2\2RV\7\4\2\2SU\5\36\20\2TS\3\2\2\2UX\3" + "\2\2\2VT\3\2\2\2VW\3\2\2\2WY\3\2\2\2XV\3\2\2\2YZ\7\5\2\2Z\17\3\2\2\2["
			+ "\\\7\20\2\2\\]\7\37\2\2]b\7\4\2\2^a\5\24\13\2_a\5\36\20\2`^\3\2\2\2`_" + "\3\2\2\2ad\3\2\2\2b`\3\2\2\2bc\3\2\2\2ce\3\2\2\2db\3\2\2\2ef\7\5\2\2f"
			+ "\21\3\2\2\2gh\7\21\2\2hl\7\4\2\2ik\5\36\20\2ji\3\2\2\2kn\3\2\2\2lj\3\2" + "\2\2lm\3\2\2\2mo\3\2\2\2nl\3\2\2\2op\7\5\2\2p\23\3\2\2\2qs\5\30\r\2rt"
			+ "\7\22\2\2sr\3\2\2\2st\3\2\2\2tu\3\2\2\2uz\5\34\17\2vw\7\r\2\2wx\5\32\16" + "\2xy\7\16\2\2y{\3\2\2\2zv\3\2\2\2z{\3\2\2\2{|\3\2\2\2|\u0082\7\37\2\2"
			+ "}~\7\23\2\2~\177\7 \2\2\177\u0081\7\24\2\2\u0080}\3\2\2\2\u0081\u0084" + "\3\2\2\2\u0082\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0086\3\2\2\2\u0084"
			+ "\u0082\3\2\2\2\u0085\u0087\5\26\f\2\u0086\u0085\3\2\2\2\u0086\u0087\3" + "\2\2\2\u0087\u0088\3\2\2\2\u0088\u0089\7\13\2\2\u0089\25\3\2\2\2\u008a"
			+ "\u008c\7\25\2\2\u008b\u008a\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u008d\3" + "\2\2\2\u008d\u008e\t\3\2\2\u008e\27\3\2\2\2\u008f\u0090\t\4\2\2\u0090"
			+ "\31\3\2\2\2\u0091\u0092\7 \2\2\u0092\33\3\2\2\2\u0093\u0094\t\5\2\2\u0094" + "\35\3\2\2\2\u0095\u009b\7\37\2\2\u0096\u0097\7\23\2\2\u0097\u0098\7 \2"
			+ "\2\u0098\u009a\7\24\2\2\u0099\u0096\3\2\2\2\u009a\u009d\3\2\2\2\u009b" + "\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009e\3\2\2\2\u009d\u009b\3\2"
			+ "\2\2\u009e\u009f\7\13\2\2\u009f\37\3\2\2\2\23#,\64\66<BLV`blsz\u0082\u0086" + "\u008b\u009b";
	public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
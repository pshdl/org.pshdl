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

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class MemoryModelParser extends Parser {
	static {
		RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION);
	}

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9, T__9 = 10, T__10 = 11, T__11 = 12, T__12 = 13, T__13 = 14,
			T__14 = 15, T__15 = 16, T__16 = 17, T__17 = 18, T__18 = 19, T__19 = 20, T__20 = 21, T__21 = 22, T__22 = 23, T__23 = 24, T__24 = 25, T__25 = 26, T__26 = 27, T__27 = 28,
			T__28 = 29, ID = 30, NUMBER = 31, COMMENT = 32, WS = 33;
	public static final int RULE_unit = 0, RULE_declaration = 1, RULE_row = 2, RULE_rowID = 3, RULE_constant = 4, RULE_filling = 5, RULE_column = 6, RULE_alias = 7,
			RULE_memory = 8, RULE_definition = 9, RULE_warnType = 10, RULE_rwStatus = 11, RULE_width = 12, RULE_type = 13, RULE_reference = 14;
	public static final String[] ruleNames = { "unit", "declaration", "row", "rowID", "constant", "filling", "column", "alias", "memory", "definition", "warnType", "rwStatus",
			"width", "type", "reference" };

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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterUnit(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitUnit(this);
		}
	}

	public final UnitContext unit() throws RecognitionException {
		UnitContext _localctx = new UnitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_unit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(33);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__12) | (1L << T__13))) != 0)) {
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
		} catch (RecognitionException re) {
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

		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_declaration;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterDeclaration(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitDeclaration(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(41);
				_errHandler.sync(this);
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
				default:
					throw new NoViableAltException(this);
				}
			}
		} catch (RecognitionException re) {
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

		public List<ConstantContext> constant() {
			return getRuleContexts(ConstantContext.class);
		}

		public ConstantContext constant(int i) {
			return getRuleContext(ConstantContext.class, i);
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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterRow(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitRow(this);
		}
	}

	public final RowContext row() throws RecognitionException {
		RowContext _localctx = new RowContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_row);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(43);
				match(T__0);
				setState(44);
				rowID();
				setState(45);
				match(T__1);
				setState(52);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__11) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << ID))) != 0)) {
					{
						setState(50);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case T__11: {
							{
								setState(46);
								filling();
							}
						}
							break;
						case T__23:
						case T__24:
						case T__25: {
							{
								setState(47);
								definition();
							}
						}
							break;
						case ID: {
							{
								setState(48);
								reference();
							}
						}
							break;
						case T__4: {
							{
								setState(49);
								constant();
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
		} catch (RecognitionException re) {
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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterRowID(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitRowID(this);
		}
	}

	public final RowIDContext rowID() throws RecognitionException {
		RowIDContext _localctx = new RowIDContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_rowID);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(58);
				_errHandler.sync(this);
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
		} catch (RecognitionException re) {
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

		public WidthContext width() {
			return getRuleContext(WidthContext.class, 0);
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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterConstant(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitConstant(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_constant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(62);
				match(T__4);
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == T__5) {
					{
						setState(63);
						match(T__5);
						setState(64);
						width();
						setState(65);
						match(T__6);
					}
				}

				setState(70);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == ID) {
					{
						setState(69);
						match(ID);
					}
				}

				setState(72);
				((ConstantContext) _localctx).value = _input.LT(1);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << NUMBER))) != 0))) {
					((ConstantContext) _localctx).value = (Token) _errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF)
						matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(73);
				match(T__10);
			}
		} catch (RecognitionException re) {
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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterFilling(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitFilling(this);
		}
	}

	public final FillingContext filling() throws RecognitionException {
		FillingContext _localctx = new FillingContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_filling);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(75);
				match(T__11);
				setState(80);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == T__5) {
					{
						setState(76);
						match(T__5);
						setState(77);
						width();
						setState(78);
						match(T__6);
					}
				}

				setState(82);
				match(T__10);
			}
		} catch (RecognitionException re) {
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

		public List<ConstantContext> constant() {
			return getRuleContexts(ConstantContext.class);
		}

		public ConstantContext constant(int i) {
			return getRuleContext(ConstantContext.class, i);
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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterColumn(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitColumn(this);
		}
	}

	public final ColumnContext column() throws RecognitionException {
		ColumnContext _localctx = new ColumnContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_column);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(84);
				match(T__12);
				setState(85);
				match(ID);
				setState(86);
				match(T__1);
				setState(91);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == T__4 || _la == ID) {
					{
						setState(89);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case ID: {
							{
								setState(87);
								reference();
							}
						}
							break;
						case T__4: {
							{
								setState(88);
								constant();
							}
						}
							break;
						default:
							throw new NoViableAltException(this);
						}
					}
					setState(93);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(94);
				match(T__2);
			}
		} catch (RecognitionException re) {
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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterAlias(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitAlias(this);
		}
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_alias);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(96);
				match(T__13);
				setState(97);
				match(ID);
				setState(98);
				match(T__1);
				setState(103);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << ID))) != 0)) {
					{
						setState(101);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case T__23:
						case T__24:
						case T__25: {
							{
								setState(99);
								definition();
							}
						}
							break;
						case ID: {
							{
								setState(100);
								reference();
							}
						}
							break;
						default:
							throw new NoViableAltException(this);
						}
					}
					setState(105);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(106);
				match(T__2);
			}
		} catch (RecognitionException re) {
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

		public List<ConstantContext> constant() {
			return getRuleContexts(ConstantContext.class);
		}

		public ConstantContext constant(int i) {
			return getRuleContext(ConstantContext.class, i);
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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterMemory(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitMemory(this);
		}
	}

	public final MemoryContext memory() throws RecognitionException {
		MemoryContext _localctx = new MemoryContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_memory);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(108);
				match(T__14);
				setState(109);
				match(T__1);
				setState(114);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == T__4 || _la == ID) {
					{
						setState(112);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case ID: {
							{
								setState(110);
								reference();
							}
						}
							break;
						case T__4: {
							{
								setState(111);
								constant();
							}
						}
							break;
						default:
							throw new NoViableAltException(this);
						}
					}
					setState(116);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(117);
				match(T__2);
			}
		} catch (RecognitionException re) {
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
		public Token modFlag;

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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterDefinition(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitDefinition(this);
		}
	}

	public final DefinitionContext definition() throws RecognitionException {
		DefinitionContext _localctx = new DefinitionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(119);
				rwStatus();
				setState(121);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == T__15) {
					{
						setState(120);
						((DefinitionContext) _localctx).hasRegister = match(T__15);
					}
				}

				setState(123);
				type();
				setState(128);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == T__5) {
					{
						setState(124);
						match(T__5);
						setState(125);
						width();
						setState(126);
						match(T__6);
					}
				}

				setState(130);
				match(ID);
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == T__16) {
					{
						{
							setState(131);
							match(T__16);
							setState(132);
							match(NUMBER);
							setState(133);
							match(T__17);
						}
					}
					setState(138);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(140);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22))) != 0)) {
					{
						setState(139);
						warnType();
					}
				}

				setState(143);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == T__18) {
					{
						setState(142);
						((DefinitionContext) _localctx).modFlag = match(T__18);
					}
				}

				setState(145);
				match(T__10);
			}
		} catch (RecognitionException re) {
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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterWarnType(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitWarnType(this);
		}
	}

	public final WarnTypeContext warnType() throws RecognitionException {
		WarnTypeContext _localctx = new WarnTypeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_warnType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == T__19) {
					{
						setState(147);
						((WarnTypeContext) _localctx).silent = match(T__19);
					}
				}

				setState(150);
				((WarnTypeContext) _localctx).typeString = _input.LT(1);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__20) | (1L << T__21) | (1L << T__22))) != 0))) {
					((WarnTypeContext) _localctx).typeString = (Token) _errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF)
						matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		} catch (RecognitionException re) {
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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterRwStatus(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitRwStatus(this);
		}
	}

	public final RwStatusContext rwStatus() throws RecognitionException {
		RwStatusContext _localctx = new RwStatusContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_rwStatus);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(152);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__23) | (1L << T__24) | (1L << T__25))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF)
						matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		} catch (RecognitionException re) {
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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterWidth(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitWidth(this);
		}
	}

	public final WidthContext width() throws RecognitionException {
		WidthContext _localctx = new WidthContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_width);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(154);
				match(NUMBER);
			}
		} catch (RecognitionException re) {
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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterType(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitType(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(156);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__26) | (1L << T__27) | (1L << T__28))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF)
						matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		} catch (RecognitionException re) {
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
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).enterReference(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof MemoryModelListener)
				((MemoryModelListener) listener).exitReference(this);
		}
	}

	public final ReferenceContext reference() throws RecognitionException {
		ReferenceContext _localctx = new ReferenceContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_reference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(158);
				match(ID);
				setState(164);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == T__16) {
					{
						{
							setState(159);
							match(T__16);
							setState(160);
							match(NUMBER);
							setState(161);
							match(T__17);
						}
					}
					setState(166);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(167);
				match(T__10);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN = "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3#\u00ac\4\2\t\2\4"
			+ "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" + "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\7\2\"\n\2\f\2\16"
			+ "\2%\13\2\3\2\3\2\3\3\3\3\3\3\5\3,\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4" + "\65\n\4\f\4\16\48\13\4\3\4\3\4\3\5\5\5=\n\5\3\5\3\5\3\6\3\6\3\6\3\6\3"
			+ "\6\5\6F\n\6\3\6\5\6I\n\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\5\7S\n\7\3\7" + "\3\7\3\b\3\b\3\b\3\b\3\b\7\b\\\n\b\f\b\16\b_\13\b\3\b\3\b\3\t\3\t\3\t"
			+ "\3\t\3\t\7\th\n\t\f\t\16\tk\13\t\3\t\3\t\3\n\3\n\3\n\3\n\7\ns\n\n\f\n" + "\16\nv\13\n\3\n\3\n\3\13\3\13\5\13|\n\13\3\13\3\13\3\13\3\13\3\13\5\13"
			+ "\u0083\n\13\3\13\3\13\3\13\3\13\7\13\u0089\n\13\f\13\16\13\u008c\13\13" + "\3\13\5\13\u008f\n\13\3\13\5\13\u0092\n\13\3\13\3\13\3\f\5\f\u0097\n\f"
			+ "\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20\7\20\u00a5\n" + "\20\f\20\16\20\u00a8\13\20\3\20\3\20\3\20\2\2\21\2\4\6\b\n\f\16\20\22"
			+ "\24\26\30\32\34\36\2\6\4\2\n\f!!\3\2\27\31\3\2\32\34\3\2\35\37\2\u00b4" + "\2#\3\2\2\2\4+\3\2\2\2\6-\3\2\2\2\b<\3\2\2\2\n@\3\2\2\2\fM\3\2\2\2\16"
			+ "V\3\2\2\2\20b\3\2\2\2\22n\3\2\2\2\24y\3\2\2\2\26\u0096\3\2\2\2\30\u009a" + "\3\2\2\2\32\u009c\3\2\2\2\34\u009e\3\2\2\2\36\u00a0\3\2\2\2 \"\5\4\3\2"
			+ "! \3\2\2\2\"%\3\2\2\2#!\3\2\2\2#$\3\2\2\2$&\3\2\2\2%#\3\2\2\2&\'\5\22" + "\n\2\'\3\3\2\2\2(,\5\6\4\2),\5\16\b\2*,\5\20\t\2+(\3\2\2\2+)\3\2\2\2+"
			+ "*\3\2\2\2,\5\3\2\2\2-.\7\3\2\2./\5\b\5\2/\66\7\4\2\2\60\65\5\f\7\2\61" + "\65\5\24\13\2\62\65\5\36\20\2\63\65\5\n\6\2\64\60\3\2\2\2\64\61\3\2\2"
			+ "\2\64\62\3\2\2\2\64\63\3\2\2\2\658\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2\2" + "\679\3\2\2\28\66\3\2\2\29:\7\5\2\2:\7\3\2\2\2;=\7\6\2\2<;\3\2\2\2<=\3"
			+ "\2\2\2=>\3\2\2\2>?\7 \2\2?\t\3\2\2\2@E\7\7\2\2AB\7\b\2\2BC\5\32\16\2C" + "D\7\t\2\2DF\3\2\2\2EA\3\2\2\2EF\3\2\2\2FH\3\2\2\2GI\7 \2\2HG\3\2\2\2H"
			+ "I\3\2\2\2IJ\3\2\2\2JK\t\2\2\2KL\7\r\2\2L\13\3\2\2\2MR\7\16\2\2NO\7\b\2" + "\2OP\5\32\16\2PQ\7\t\2\2QS\3\2\2\2RN\3\2\2\2RS\3\2\2\2ST\3\2\2\2TU\7\r"
			+ "\2\2U\r\3\2\2\2VW\7\17\2\2WX\7 \2\2X]\7\4\2\2Y\\\5\36\20\2Z\\\5\n\6\2" + "[Y\3\2\2\2[Z\3\2\2\2\\_\3\2\2\2][\3\2\2\2]^\3\2\2\2^`\3\2\2\2_]\3\2\2"
			+ "\2`a\7\5\2\2a\17\3\2\2\2bc\7\20\2\2cd\7 \2\2di\7\4\2\2eh\5\24\13\2fh\5" + "\36\20\2ge\3\2\2\2gf\3\2\2\2hk\3\2\2\2ig\3\2\2\2ij\3\2\2\2jl\3\2\2\2k"
			+ "i\3\2\2\2lm\7\5\2\2m\21\3\2\2\2no\7\21\2\2ot\7\4\2\2ps\5\36\20\2qs\5\n" + "\6\2rp\3\2\2\2rq\3\2\2\2sv\3\2\2\2tr\3\2\2\2tu\3\2\2\2uw\3\2\2\2vt\3\2"
			+ "\2\2wx\7\5\2\2x\23\3\2\2\2y{\5\30\r\2z|\7\22\2\2{z\3\2\2\2{|\3\2\2\2|" + "}\3\2\2\2}\u0082\5\34\17\2~\177\7\b\2\2\177\u0080\5\32\16\2\u0080\u0081"
			+ "\7\t\2\2\u0081\u0083\3\2\2\2\u0082~\3\2\2\2\u0082\u0083\3\2\2\2\u0083" + "\u0084\3\2\2\2\u0084\u008a\7 \2\2\u0085\u0086\7\23\2\2\u0086\u0087\7!"
			+ "\2\2\u0087\u0089\7\24\2\2\u0088\u0085\3\2\2\2\u0089\u008c\3\2\2\2\u008a" + "\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u008e\3\2\2\2\u008c\u008a\3\2"
			+ "\2\2\u008d\u008f\5\26\f\2\u008e\u008d\3\2\2\2\u008e\u008f\3\2\2\2\u008f" + "\u0091\3\2\2\2\u0090\u0092\7\25\2\2\u0091\u0090\3\2\2\2\u0091\u0092\3"
			+ "\2\2\2\u0092\u0093\3\2\2\2\u0093\u0094\7\r\2\2\u0094\25\3\2\2\2\u0095" + "\u0097\7\26\2\2\u0096\u0095\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0098\3"
			+ "\2\2\2\u0098\u0099\t\3\2\2\u0099\27\3\2\2\2\u009a\u009b\t\4\2\2\u009b" + "\31\3\2\2\2\u009c\u009d\7!\2\2\u009d\33\3\2\2\2\u009e\u009f\t\5\2\2\u009f"
			+ "\35\3\2\2\2\u00a0\u00a6\7 \2\2\u00a1\u00a2\7\23\2\2\u00a2\u00a3\7!\2\2" + "\u00a3\u00a5\7\24\2\2\u00a4\u00a1\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4"
			+ "\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a9\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9" + "\u00aa\7\r\2\2\u00aa\37\3\2\2\2\27#+\64\66<EHR[]girt{\u0082\u008a\u008e"
			+ "\u0091\u0096\u00a6";
	public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
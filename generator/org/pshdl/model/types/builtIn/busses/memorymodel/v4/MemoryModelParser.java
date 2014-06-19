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
// Generated from MemoryModel.g4 by ANTLR 4.2.2
package org.pshdl.model.types.builtIn.busses.memorymodel.v4;

import java.util.List;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class MemoryModelParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int T__21 = 1, T__20 = 2, T__19 = 3, T__18 = 4, T__17 = 5, T__16 = 6, T__15 = 7, T__14 = 8, T__13 = 9, T__12 = 10, T__11 = 11, T__10 = 12, T__9 = 13,
			T__8 = 14, T__7 = 15, T__6 = 16, T__5 = 17, T__4 = 18, T__3 = 19, T__2 = 20, T__1 = 21, T__0 = 22, ID = 23, INT = 24, COMMENT = 25, WS = 26;
	public static final String[] tokenNames = { "<INVALID>", "'memory'", "'column'", "'error'", "']'", "'row'", "'limit'", "'mask'", "'register'", "'rw'", "'['", "'<'", "'int'",
		"'r'", "'w'", "';'", "'alias'", "'>'", "'{'", "'silent'", "'}'", "'uint'", "'bit'", "ID", "INT", "COMMENT", "WS" };
	public static final int RULE_unit = 0, RULE_declaration = 1, RULE_row = 2, RULE_column = 3, RULE_alias = 4, RULE_memory = 5, RULE_definition = 6, RULE_warnType = 7,
			RULE_rwStatus = 8, RULE_width = 9, RULE_type = 10, RULE_reference = 11;
	public static final String[] ruleNames = { "unit", "declaration", "row", "column", "alias", "memory", "definition", "warnType", "rwStatus", "width", "type", "reference" };

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
				setState(27);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 2) | (1L << 5) | (1L << 16))) != 0))) {
					{
						{
							setState(24);
							declaration();
						}
					}
					setState(29);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				{
					setState(30);
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

		public AliasContext alias() {
			return getRuleContext(AliasContext.class, 0);
		}

		public ColumnContext column() {
			return getRuleContext(ColumnContext.class, 0);
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
				setState(35);
				switch (_input.LA(1)) {
				case 5: {
					{
						setState(32);
						row();
					}
				}
				break;
				case 2: {
					{
						setState(33);
						column();
					}
				}
				break;
				case 16: {
					{
						setState(34);
						alias();
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
		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}

		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class, i);
		}

		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public ReferenceContext reference(int i) {
			return getRuleContext(ReferenceContext.class, i);
		}

		public List<ReferenceContext> reference() {
			return getRuleContexts(ReferenceContext.class);
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
				setState(37);
				match(5);
				setState(38);
				match(ID);
				setState(39);
				match(18);
				setState(44);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 9) | (1L << 13) | (1L << 14) | (1L << ID))) != 0))) {
					{
						setState(42);
						switch (_input.LA(1)) {
						case 9:
						case 13:
						case 14: {
							{
								setState(40);
								definition();
							}
						}
						break;
						case ID: {
							{
								setState(41);
								reference();
							}
						}
						break;
						default:
							throw new NoViableAltException(this);
						}
					}
					setState(46);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(47);
				match(20);
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

		public ReferenceContext reference(int i) {
			return getRuleContext(ReferenceContext.class, i);
		}

		public List<ReferenceContext> reference() {
			return getRuleContexts(ReferenceContext.class);
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
		enterRule(_localctx, 6, RULE_column);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(49);
				match(2);
				setState(50);
				match(ID);
				setState(51);
				match(18);
				setState(55);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == ID) {
					{
						{
							setState(52);
							reference();
						}
					}
					setState(57);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(58);
				match(20);
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
		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}

		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class, i);
		}

		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public ReferenceContext reference(int i) {
			return getRuleContext(ReferenceContext.class, i);
		}

		public List<ReferenceContext> reference() {
			return getRuleContexts(ReferenceContext.class);
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
		enterRule(_localctx, 8, RULE_alias);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(60);
				match(16);
				setState(61);
				match(ID);
				setState(62);
				match(18);
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 9) | (1L << 13) | (1L << 14) | (1L << ID))) != 0))) {
					{
						setState(65);
						switch (_input.LA(1)) {
						case 9:
						case 13:
						case 14: {
							{
								setState(63);
								definition();
							}
						}
						break;
						case ID: {
							{
								setState(64);
								reference();
							}
						}
						break;
						default:
							throw new NoViableAltException(this);
						}
					}
					setState(69);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(70);
				match(20);
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
		public ReferenceContext reference(int i) {
			return getRuleContext(ReferenceContext.class, i);
		}

		public List<ReferenceContext> reference() {
			return getRuleContexts(ReferenceContext.class);
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
		enterRule(_localctx, 10, RULE_memory);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(72);
				match(1);
				setState(73);
				match(18);
				setState(77);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == ID) {
					{
						{
							setState(74);
							reference();
						}
					}
					setState(79);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(80);
				match(20);
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

		public List<TerminalNode> INT() {
			return getTokens(MemoryModelParser.INT);
		}

		public RwStatusContext rwStatus() {
			return getRuleContext(RwStatusContext.class, 0);
		}

		public WarnTypeContext warnType() {
			return getRuleContext(WarnTypeContext.class, 0);
		}

		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public TypeContext type() {
			return getRuleContext(TypeContext.class, 0);
		}

		public WidthContext width() {
			return getRuleContext(WidthContext.class, 0);
		}

		public TerminalNode INT(int i) {
			return getToken(MemoryModelParser.INT, i);
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
		enterRule(_localctx, 12, RULE_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(82);
				rwStatus();
				setState(84);
				_la = _input.LA(1);
				if (_la == 8) {
					{
						setState(83);
						_localctx.hasRegister = match(8);
					}
				}

				setState(86);
				type();
				setState(91);
				_la = _input.LA(1);
				if (_la == 11) {
					{
						setState(87);
						match(11);
						setState(88);
						width();
						setState(89);
						match(17);
					}
				}

				setState(93);
				match(ID);
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == 10) {
					{
						{
							setState(94);
							match(10);
							setState(95);
							match(INT);
							setState(96);
							match(4);
						}
					}
					setState(101);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(103);
				_la = _input.LA(1);
				if (((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 3) | (1L << 6) | (1L << 7) | (1L << 19))) != 0))) {
					{
						setState(102);
						warnType();
					}
				}

				setState(105);
				match(15);
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
		enterRule(_localctx, 14, RULE_warnType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(108);
				_la = _input.LA(1);
				if (_la == 19) {
					{
						setState(107);
						_localctx.silent = match(19);
					}
				}

				setState(110);
				_localctx.typeString = _input.LT(1);
				_la = _input.LA(1);
				if (!(((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 3) | (1L << 6) | (1L << 7))) != 0)))) {
					_localctx.typeString = _errHandler.recoverInline(this);
				}
				consume();
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
		enterRule(_localctx, 16, RULE_rwStatus);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(112);
				_la = _input.LA(1);
				if (!(((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 9) | (1L << 13) | (1L << 14))) != 0)))) {
					_errHandler.recoverInline(this);
				}
				consume();
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
		public TerminalNode INT() {
			return getToken(MemoryModelParser.INT, 0);
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
		enterRule(_localctx, 18, RULE_width);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(114);
				match(INT);
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
		enterRule(_localctx, 20, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(116);
				_la = _input.LA(1);
				if (!(((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 12) | (1L << 21) | (1L << 22))) != 0)))) {
					_errHandler.recoverInline(this);
				}
				consume();
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
		public List<TerminalNode> INT() {
			return getTokens(MemoryModelParser.INT);
		}

		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public TerminalNode INT(int i) {
			return getToken(MemoryModelParser.INT, i);
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
		enterRule(_localctx, 22, RULE_reference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(118);
				match(ID);
				setState(124);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == 10) {
					{
						{
							setState(119);
							match(10);
							setState(120);
							match(INT);
							setState(121);
							match(4);
						}
					}
					setState(126);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(127);
				match(15);
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

	public static final String _serializedATN = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\34\u0084\4\2\t\2"
			+ "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13" + "\t\13\4\f\t\f\4\r\t\r\3\2\7\2\34\n\2\f\2\16\2\37\13\2\3\2\3\2\3\3\3\3"
			+ "\3\3\5\3&\n\3\3\4\3\4\3\4\3\4\3\4\7\4-\n\4\f\4\16\4\60\13\4\3\4\3\4\3" + "\5\3\5\3\5\3\5\7\58\n\5\f\5\16\5;\13\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\7\6"
			+ "D\n\6\f\6\16\6G\13\6\3\6\3\6\3\7\3\7\3\7\7\7N\n\7\f\7\16\7Q\13\7\3\7\3" + "\7\3\b\3\b\5\bW\n\b\3\b\3\b\3\b\3\b\3\b\5\b^\n\b\3\b\3\b\3\b\3\b\7\bd"
			+ "\n\b\f\b\16\bg\13\b\3\b\5\bj\n\b\3\b\3\b\3\t\5\to\n\t\3\t\3\t\3\n\3\n" + "\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\7\r}\n\r\f\r\16\r\u0080\13\r\3\r\3"
			+ "\r\3\r\2\2\16\2\4\6\b\n\f\16\20\22\24\26\30\2\5\4\2\5\5\b\t\4\2\13\13" + "\17\20\4\2\16\16\27\30\u0086\2\35\3\2\2\2\4%\3\2\2\2\6\'\3\2\2\2\b\63"
			+ "\3\2\2\2\n>\3\2\2\2\fJ\3\2\2\2\16T\3\2\2\2\20n\3\2\2\2\22r\3\2\2\2\24" + "t\3\2\2\2\26v\3\2\2\2\30x\3\2\2\2\32\34\5\4\3\2\33\32\3\2\2\2\34\37\3"
			+ "\2\2\2\35\33\3\2\2\2\35\36\3\2\2\2\36 \3\2\2\2\37\35\3\2\2\2 !\5\f\7\2" + "!\3\3\2\2\2\"&\5\6\4\2#&\5\b\5\2$&\5\n\6\2%\"\3\2\2\2%#\3\2\2\2%$\3\2"
			+ "\2\2&\5\3\2\2\2\'(\7\7\2\2()\7\31\2\2).\7\24\2\2*-\5\16\b\2+-\5\30\r\2" + ",*\3\2\2\2,+\3\2\2\2-\60\3\2\2\2.,\3\2\2\2./\3\2\2\2/\61\3\2\2\2\60.\3"
			+ "\2\2\2\61\62\7\26\2\2\62\7\3\2\2\2\63\64\7\4\2\2\64\65\7\31\2\2\659\7" + "\24\2\2\668\5\30\r\2\67\66\3\2\2\28;\3\2\2\29\67\3\2\2\29:\3\2\2\2:<\3"
			+ "\2\2\2;9\3\2\2\2<=\7\26\2\2=\t\3\2\2\2>?\7\22\2\2?@\7\31\2\2@E\7\24\2" + "\2AD\5\16\b\2BD\5\30\r\2CA\3\2\2\2CB\3\2\2\2DG\3\2\2\2EC\3\2\2\2EF\3\2"
			+ "\2\2FH\3\2\2\2GE\3\2\2\2HI\7\26\2\2I\13\3\2\2\2JK\7\3\2\2KO\7\24\2\2L" + "N\5\30\r\2ML\3\2\2\2NQ\3\2\2\2OM\3\2\2\2OP\3\2\2\2PR\3\2\2\2QO\3\2\2\2"
			+ "RS\7\26\2\2S\r\3\2\2\2TV\5\22\n\2UW\7\n\2\2VU\3\2\2\2VW\3\2\2\2WX\3\2" + "\2\2X]\5\26\f\2YZ\7\r\2\2Z[\5\24\13\2[\\\7\23\2\2\\^\3\2\2\2]Y\3\2\2\2"
			+ "]^\3\2\2\2^_\3\2\2\2_e\7\31\2\2`a\7\f\2\2ab\7\32\2\2bd\7\6\2\2c`\3\2\2" + "\2dg\3\2\2\2ec\3\2\2\2ef\3\2\2\2fi\3\2\2\2ge\3\2\2\2hj\5\20\t\2ih\3\2"
			+ "\2\2ij\3\2\2\2jk\3\2\2\2kl\7\21\2\2l\17\3\2\2\2mo\7\25\2\2nm\3\2\2\2n" + "o\3\2\2\2op\3\2\2\2pq\t\2\2\2q\21\3\2\2\2rs\t\3\2\2s\23\3\2\2\2tu\7\32"
			+ "\2\2u\25\3\2\2\2vw\t\4\2\2w\27\3\2\2\2x~\7\31\2\2yz\7\f\2\2z{\7\32\2\2" + "{}\7\6\2\2|y\3\2\2\2}\u0080\3\2\2\2~|\3\2\2\2~\177\3\2\2\2\177\u0081\3"
			+ "\2\2\2\u0080~\3\2\2\2\u0081\u0082\7\21\2\2\u0082\31\3\2\2\2\20\35%,.9" + "CEOV]ein~";
	public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
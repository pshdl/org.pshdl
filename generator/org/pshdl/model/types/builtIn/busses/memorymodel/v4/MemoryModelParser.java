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
	public static final int T__26 = 1, T__25 = 2, T__24 = 3, T__23 = 4, T__22 = 5, T__21 = 6, T__20 = 7, T__19 = 8, T__18 = 9, T__17 = 10, T__16 = 11, T__15 = 12, T__14 = 13,
			T__13 = 14, T__12 = 15, T__11 = 16, T__10 = 17, T__9 = 18, T__8 = 19, T__7 = 20, T__6 = 21, T__5 = 22, T__4 = 23, T__3 = 24, T__2 = 25, T__1 = 26, T__0 = 27, ID = 28,
			NUMBER = 29, COMMENT = 30, WS = 31;
	public static final String[] tokenNames = { "<INVALID>", "'column'", "']'", "'row'", "'mask'", "'rw'", "'register'", "'$checkSum'", "'['", "'<'", "'int'", "'w'", "'$time'",
			"'{'", "'silent'", "'}'", "'uint'", "'error'", "'memory'", "'limit'", "'fill'", "'r'", "';'", "'alias'", "'const'", "'>'", "'$date'", "'bit'", "ID", "NUMBER",
			"COMMENT", "WS" };
	public static final int RULE_unit = 0, RULE_declaration = 1, RULE_row = 2, RULE_constant = 3, RULE_filling = 4, RULE_column = 5, RULE_alias = 6, RULE_memory = 7,
			RULE_definition = 8, RULE_warnType = 9, RULE_rwStatus = 10, RULE_width = 11, RULE_type = 12, RULE_reference = 13;
	public static final String[] ruleNames = { "unit", "declaration", "row", "constant", "filling", "column", "alias", "memory", "definition", "warnType", "rwStatus", "width",
			"type", "reference" };

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
				setState(31);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 1) | (1L << 3) | (1L << 23) | (1L << 24))) != 0))) {
					{
						{
							setState(28);
							declaration();
						}
					}
					setState(33);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				{
					setState(34);
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

		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class, 0);
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
				setState(40);
				switch (_input.LA(1)) {
				case 3: {
					{
						setState(36);
						row();
					}
				}
					break;
				case 1: {
					{
						setState(37);
						column();
					}
				}
					break;
				case 23: {
					{
						setState(38);
						alias();
					}
				}
					break;
				case 24: {
					{
						setState(39);
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
		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}

		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class, i);
		}

		public FillingContext filling(int i) {
			return getRuleContext(FillingContext.class, i);
		}

		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public ReferenceContext reference(int i) {
			return getRuleContext(ReferenceContext.class, i);
		}

		public List<FillingContext> filling() {
			return getRuleContexts(FillingContext.class);
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
				setState(42);
				match(3);
				setState(43);
				match(ID);
				setState(44);
				match(13);
				setState(50);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 5) | (1L << 11) | (1L << 20) | (1L << 21) | (1L << ID))) != 0))) {
					{
						setState(48);
						switch (_input.LA(1)) {
						case 20: {
							{
								setState(45);
								filling();
							}
						}
							break;
						case 5:
						case 11:
						case 21: {
							{
								setState(46);
								definition();
							}
						}
							break;
						case ID: {
							{
								setState(47);
								reference();
							}
						}
							break;
						default:
							throw new NoViableAltException(this);
						}
					}
					setState(52);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(53);
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

	public static class ConstantContext extends ParserRuleContext {
		public Token value;

		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public TerminalNode NUMBER() {
			return getToken(MemoryModelParser.NUMBER, 0);
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
		enterRule(_localctx, 6, RULE_constant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(55);
				match(24);
				setState(57);
				_la = _input.LA(1);
				if (_la == ID) {
					{
						setState(56);
						match(ID);
					}
				}

				setState(59);
				_localctx.value = _input.LT(1);
				_la = _input.LA(1);
				if (!(((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 7) | (1L << 12) | (1L << 26) | (1L << NUMBER))) != 0)))) {
					_localctx.value = _errHandler.recoverInline(this);
				}
				consume();
				setState(60);
				match(22);
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
		enterRule(_localctx, 8, RULE_filling);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(62);
				match(20);
				setState(67);
				_la = _input.LA(1);
				if (_la == 9) {
					{
						setState(63);
						match(9);
						setState(64);
						width();
						setState(65);
						match(25);
					}
				}

				setState(69);
				match(22);
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
		enterRule(_localctx, 10, RULE_column);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(71);
				match(1);
				setState(72);
				match(ID);
				setState(73);
				match(13);
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
		enterRule(_localctx, 12, RULE_alias);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(82);
				match(23);
				setState(83);
				match(ID);
				setState(84);
				match(13);
				setState(89);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 5) | (1L << 11) | (1L << 21) | (1L << ID))) != 0))) {
					{
						setState(87);
						switch (_input.LA(1)) {
						case 5:
						case 11:
						case 21: {
							{
								setState(85);
								definition();
							}
						}
							break;
						case ID: {
							{
								setState(86);
								reference();
							}
						}
							break;
						default:
							throw new NoViableAltException(this);
						}
					}
					setState(91);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(92);
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
		enterRule(_localctx, 14, RULE_memory);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(94);
				match(18);
				setState(95);
				match(13);
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == ID) {
					{
						{
							setState(96);
							reference();
						}
					}
					setState(101);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(102);
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

	public static class DefinitionContext extends ParserRuleContext {
		public Token hasRegister;

		public RwStatusContext rwStatus() {
			return getRuleContext(RwStatusContext.class, 0);
		}

		public WarnTypeContext warnType() {
			return getRuleContext(WarnTypeContext.class, 0);
		}

		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public TerminalNode NUMBER(int i) {
			return getToken(MemoryModelParser.NUMBER, i);
		}

		public TypeContext type() {
			return getRuleContext(TypeContext.class, 0);
		}

		public List<TerminalNode> NUMBER() {
			return getTokens(MemoryModelParser.NUMBER);
		}

		public WidthContext width() {
			return getRuleContext(WidthContext.class, 0);
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
		enterRule(_localctx, 16, RULE_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(104);
				rwStatus();
				setState(106);
				_la = _input.LA(1);
				if (_la == 6) {
					{
						setState(105);
						_localctx.hasRegister = match(6);
					}
				}

				setState(108);
				type();
				setState(113);
				_la = _input.LA(1);
				if (_la == 9) {
					{
						setState(109);
						match(9);
						setState(110);
						width();
						setState(111);
						match(25);
					}
				}

				setState(115);
				match(ID);
				setState(121);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == 8) {
					{
						{
							setState(116);
							match(8);
							setState(117);
							match(NUMBER);
							setState(118);
							match(2);
						}
					}
					setState(123);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(125);
				_la = _input.LA(1);
				if (((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 4) | (1L << 14) | (1L << 17) | (1L << 19))) != 0))) {
					{
						setState(124);
						warnType();
					}
				}

				setState(127);
				match(22);
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
		enterRule(_localctx, 18, RULE_warnType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(130);
				_la = _input.LA(1);
				if (_la == 14) {
					{
						setState(129);
						_localctx.silent = match(14);
					}
				}

				setState(132);
				_localctx.typeString = _input.LT(1);
				_la = _input.LA(1);
				if (!(((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 4) | (1L << 17) | (1L << 19))) != 0)))) {
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
		enterRule(_localctx, 20, RULE_rwStatus);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(134);
				_la = _input.LA(1);
				if (!(((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 5) | (1L << 11) | (1L << 21))) != 0)))) {
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
		enterRule(_localctx, 22, RULE_width);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(136);
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
		enterRule(_localctx, 24, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(138);
				_la = _input.LA(1);
				if (!(((((_la) & ~0x3f) == 0) && (((1L << _la) & ((1L << 10) | (1L << 16) | (1L << 27))) != 0)))) {
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
		public TerminalNode ID() {
			return getToken(MemoryModelParser.ID, 0);
		}

		public TerminalNode NUMBER(int i) {
			return getToken(MemoryModelParser.NUMBER, i);
		}

		public List<TerminalNode> NUMBER() {
			return getTokens(MemoryModelParser.NUMBER);
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
		enterRule(_localctx, 26, RULE_reference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(140);
				match(ID);
				setState(146);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == 8) {
					{
						{
							setState(141);
							match(8);
							setState(142);
							match(NUMBER);
							setState(143);
							match(2);
						}
					}
					setState(148);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(149);
				match(22);
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

	public static final String _serializedATN = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3!\u009a\4\2\t\2\4"
			+ "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" + "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\7\2 \n\2\f\2\16\2#\13\2\3"
			+ "\2\3\2\3\3\3\3\3\3\3\3\5\3+\n\3\3\4\3\4\3\4\3\4\3\4\3\4\7\4\63\n\4\f\4" + "\16\4\66\13\4\3\4\3\4\3\5\3\5\5\5<\n\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6"
			+ "\5\6F\n\6\3\6\3\6\3\7\3\7\3\7\3\7\7\7N\n\7\f\7\16\7Q\13\7\3\7\3\7\3\b" + "\3\b\3\b\3\b\3\b\7\bZ\n\b\f\b\16\b]\13\b\3\b\3\b\3\t\3\t\3\t\7\td\n\t"
			+ "\f\t\16\tg\13\t\3\t\3\t\3\n\3\n\5\nm\n\n\3\n\3\n\3\n\3\n\3\n\5\nt\n\n" + "\3\n\3\n\3\n\3\n\7\nz\n\n\f\n\16\n}\13\n\3\n\5\n\u0080\n\n\3\n\3\n\3\13"
			+ "\5\13\u0085\n\13\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3" + "\17\7\17\u0093\n\17\f\17\16\17\u0096\13\17\3\17\3\17\3\17\2\2\20\2\4\6"
			+ "\b\n\f\16\20\22\24\26\30\32\34\2\6\6\2\t\t\16\16\34\34\37\37\5\2\6\6\23" + "\23\25\25\5\2\7\7\r\r\27\27\5\2\f\f\22\22\35\35\u009e\2!\3\2\2\2\4*\3"
			+ "\2\2\2\6,\3\2\2\2\b9\3\2\2\2\n@\3\2\2\2\fI\3\2\2\2\16T\3\2\2\2\20`\3\2" + "\2\2\22j\3\2\2\2\24\u0084\3\2\2\2\26\u0088\3\2\2\2\30\u008a\3\2\2\2\32"
			+ "\u008c\3\2\2\2\34\u008e\3\2\2\2\36 \5\4\3\2\37\36\3\2\2\2 #\3\2\2\2!\37" + "\3\2\2\2!\"\3\2\2\2\"$\3\2\2\2#!\3\2\2\2$%\5\20\t\2%\3\3\2\2\2&+\5\6\4"
			+ "\2\'+\5\f\7\2(+\5\16\b\2)+\5\b\5\2*&\3\2\2\2*\'\3\2\2\2*(\3\2\2\2*)\3" + "\2\2\2+\5\3\2\2\2,-\7\5\2\2-.\7\36\2\2.\64\7\17\2\2/\63\5\n\6\2\60\63"
			+ "\5\22\n\2\61\63\5\34\17\2\62/\3\2\2\2\62\60\3\2\2\2\62\61\3\2\2\2\63\66" + "\3\2\2\2\64\62\3\2\2\2\64\65\3\2\2\2\65\67\3\2\2\2\66\64\3\2\2\2\678\7"
			+ "\21\2\28\7\3\2\2\29;\7\32\2\2:<\7\36\2\2;:\3\2\2\2;<\3\2\2\2<=\3\2\2\2" + "=>\t\2\2\2>?\7\30\2\2?\t\3\2\2\2@E\7\26\2\2AB\7\13\2\2BC\5\30\r\2CD\7"
			+ "\33\2\2DF\3\2\2\2EA\3\2\2\2EF\3\2\2\2FG\3\2\2\2GH\7\30\2\2H\13\3\2\2\2" + "IJ\7\3\2\2JK\7\36\2\2KO\7\17\2\2LN\5\34\17\2ML\3\2\2\2NQ\3\2\2\2OM\3\2"
			+ "\2\2OP\3\2\2\2PR\3\2\2\2QO\3\2\2\2RS\7\21\2\2S\r\3\2\2\2TU\7\31\2\2UV" + "\7\36\2\2V[\7\17\2\2WZ\5\22\n\2XZ\5\34\17\2YW\3\2\2\2YX\3\2\2\2Z]\3\2"
			+ "\2\2[Y\3\2\2\2[\\\3\2\2\2\\^\3\2\2\2][\3\2\2\2^_\7\21\2\2_\17\3\2\2\2" + "`a\7\24\2\2ae\7\17\2\2bd\5\34\17\2cb\3\2\2\2dg\3\2\2\2ec\3\2\2\2ef\3\2"
			+ "\2\2fh\3\2\2\2ge\3\2\2\2hi\7\21\2\2i\21\3\2\2\2jl\5\26\f\2km\7\b\2\2l" + "k\3\2\2\2lm\3\2\2\2mn\3\2\2\2ns\5\32\16\2op\7\13\2\2pq\5\30\r\2qr\7\33"
			+ "\2\2rt\3\2\2\2so\3\2\2\2st\3\2\2\2tu\3\2\2\2u{\7\36\2\2vw\7\n\2\2wx\7" + "\37\2\2xz\7\4\2\2yv\3\2\2\2z}\3\2\2\2{y\3\2\2\2{|\3\2\2\2|\177\3\2\2\2"
			+ "}{\3\2\2\2~\u0080\5\24\13\2\177~\3\2\2\2\177\u0080\3\2\2\2\u0080\u0081" + "\3\2\2\2\u0081\u0082\7\30\2\2\u0082\23\3\2\2\2\u0083\u0085\7\20\2\2\u0084"
			+ "\u0083\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0087\t\3" + "\2\2\u0087\25\3\2\2\2\u0088\u0089\t\4\2\2\u0089\27\3\2\2\2\u008a\u008b"
			+ "\7\37\2\2\u008b\31\3\2\2\2\u008c\u008d\t\5\2\2\u008d\33\3\2\2\2\u008e" + "\u0094\7\36\2\2\u008f\u0090\7\n\2\2\u0090\u0091\7\37\2\2\u0091\u0093\7"
			+ "\4\2\2\u0092\u008f\3\2\2\2\u0093\u0096\3\2\2\2\u0094\u0092\3\2\2\2\u0094" + "\u0095\3\2\2\2\u0095\u0097\3\2\2\2\u0096\u0094\3\2\2\2\u0097\u0098\7\30"
			+ "\2\2\u0098\35\3\2\2\2\22!*\62\64;EOY[els{\177\u0084\u0094";
	public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
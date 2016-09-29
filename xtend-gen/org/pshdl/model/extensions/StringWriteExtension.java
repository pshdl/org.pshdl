/**
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
 */
package org.pshdl.model.extensions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLArgument;
import org.pshdl.model.HDLArrayInit;
import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLBitOp;
import org.pshdl.model.HDLBlock;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLConcat;
import org.pshdl.model.HDLDeclaration;
import org.pshdl.model.HDLDirectGeneration;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumDeclaration;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLEqualityOp;
import org.pshdl.model.HDLExport;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLInlineFunction;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLInterfaceDeclaration;
import org.pshdl.model.HDLInterfaceInstantiation;
import org.pshdl.model.HDLInterfaceRef;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLNativeFunction;
import org.pshdl.model.HDLOpExpression;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLSubstituteFunction;
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.HDLTernary;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLUnresolvedFragmentFunction;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.parser.SourceInfo;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.SyntaxHighlighter;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

@SuppressWarnings("all")
public class StringWriteExtension {
	private static StringWriteExtension INST = new StringWriteExtension();

	protected String _toString(final IHDLObject exp, final SyntaxHighlighter highlight) {
		final HDLClass _classType = exp.getClassType();
		final String _plus = ("Did not implement toString for " + _classType);
		throw new RuntimeException(_plus);
	}

	protected String _toString(final HDLExpression exp, final SyntaxHighlighter highlight) {
		final HDLClass _classType = exp.getClassType();
		final String _plus = ("Did not implement toString for " + _classType);
		throw new RuntimeException(_plus);
	}

	protected String _toString(final HDLStatement exp, final SyntaxHighlighter highlight) {
		final HDLClass _classType = exp.getClassType();
		final String _plus = ("Did not implement toString for " + _classType);
		throw new RuntimeException(_plus);
	}

	public static String asString(final IHDLObject exp, final SyntaxHighlighter highlight) {
		if ((exp == null))
			throw new IllegalArgumentException("Can not handle null argument");
		return StringWriteExtension.INST.toString(exp, highlight);
	}

	protected String _toString(final HDLArrayInit array, final SyntaxHighlighter highlight) {
		final StringConcatenation _builder = new StringConcatenation();
		final String _entering = this.entering(array, highlight);
		_builder.append(_entering, "");
		_builder.append("{");
		{
			final ArrayList<HDLExpression> _exp = array.getExp();
			boolean _hasElements = false;
			for (final HDLExpression e : _exp) {
				if (!_hasElements) {
					_hasElements = true;
				} else {
					_builder.appendImmediate(",", "");
				}
				final String _string = this.toString(e, highlight);
				_builder.append(_string, "");
			}
		}
		_builder.append("}");
		final String _leaving = this.leaving(array, highlight);
		_builder.append(_leaving, "");
		return _builder.toString();
	}

	public String leaving(final IHDLObject init, final SyntaxHighlighter highlighter) {
		return highlighter.leaving(init);
	}

	public String entering(final IHDLObject init, final SyntaxHighlighter highlighter) {
		final List<String> comments = init.<List<String>> getMeta(SourceInfo.COMMENT);
		final StringBuilder sb = new StringBuilder();
		if ((comments != null)) {
			for (final String comment : comments) {
				final String _comment = highlighter.comment(comment);
				final StringBuilder _append = sb.append(_comment);
				final String _newLine = highlighter.newLine();
				_append.append(_newLine);
			}
		}
		final String _entering = highlighter.entering(init);
		return (sb + _entering);
	}

	protected String _toString(final HDLAnnotation anno, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		final String _entering = this.entering(anno, highlight);
		sb.append(_entering);
		final String _name = anno.getName();
		final String _annotation = highlight.annotation(_name);
		sb.append(_annotation);
		final String _value = anno.getValue();
		final boolean _tripleNotEquals = (_value != null);
		if (_tripleNotEquals) {
			final StringBuilder _append = sb.append("(");
			final String _value_1 = anno.getValue();
			final String _plus = ("\"" + _value_1);
			final String _plus_1 = (_plus + "\"");
			final String _string = highlight.string(_plus_1);
			final StringBuilder _append_1 = _append.append(_string);
			_append_1.append(")");
		}
		final String _leaving = this.leaving(anno, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLTernary tern, final SyntaxHighlighter highlight) {
		final StringConcatenation _builder = new StringConcatenation();
		final String _entering = this.entering(tern, highlight);
		_builder.append(_entering, "");
		_builder.append("(");
		final HDLExpression _ifExpr = tern.getIfExpr();
		final String _string = this.toString(_ifExpr, highlight);
		_builder.append(_string, "");
		final String _operator = highlight.operator("?");
		_builder.append(_operator, "");
		final HDLExpression _thenExpr = tern.getThenExpr();
		final String _string_1 = this.toString(_thenExpr, highlight);
		_builder.append(_string_1, "");
		final String _operator_1 = highlight.operator(":");
		_builder.append(_operator_1, "");
		final HDLExpression _elseExpr = tern.getElseExpr();
		final String _string_2 = this.toString(_elseExpr, highlight);
		_builder.append(_string_2, "");
		_builder.append(")");
		final String _leaving = this.leaving(tern, highlight);
		_builder.append(_leaving, "");
		return _builder.toString();
	}

	protected String _toString(final HDLOpExpression op, final SyntaxHighlighter highlight) {
		final StringConcatenation _builder = new StringConcatenation();
		final String _entering = this.entering(op, highlight);
		_builder.append(_entering, "");
		_builder.append("(");
		final HDLExpression _left = op.getLeft();
		final String _string = this.toString(_left, highlight);
		_builder.append(_string, "");
		final Enum<?> _type = op.getType();
		final String _string_1 = _type.toString();
		final String _operator = highlight.operator(_string_1);
		_builder.append(_operator, "");
		final HDLExpression _right = op.getRight();
		final String _string_2 = this.toString(_right, highlight);
		_builder.append(_string_2, "");
		_builder.append(")");
		final String _leaving = this.leaving(op, highlight);
		_builder.append(_leaving, "");
		return _builder.toString();
	}

	protected String _toString(final HDLEqualityOp op, final SyntaxHighlighter highlight) {
		final StringConcatenation _builder = new StringConcatenation();
		final String _entering = this.entering(op, highlight);
		_builder.append(_entering, "");
		_builder.append("(");
		final HDLExpression _left = op.getLeft();
		final String _string = this.toString(_left, highlight);
		_builder.append(_string, "");
		final String _simpleSpace = highlight.simpleSpace();
		_builder.append(_simpleSpace, "");
		final HDLEqualityOp.HDLEqualityOpType _type = op.getType();
		final String _string_1 = _type.toString();
		final String _operator = highlight.operator(_string_1);
		_builder.append(_operator, "");
		final String _simpleSpace_1 = highlight.simpleSpace();
		_builder.append(_simpleSpace_1, "");
		final HDLExpression _right = op.getRight();
		final String _string_2 = this.toString(_right, highlight);
		_builder.append(_string_2, "");
		_builder.append(")");
		final String _leaving = this.leaving(op, highlight);
		_builder.append(_leaving, "");
		return _builder.toString();
	}

	protected String _toString(final HDLUnresolvedFragmentFunction frag, final SyntaxHighlighter highlight) {
		String _xifexpression = null;
		final Boolean _isStatement = frag.getIsStatement();
		if ((_isStatement).booleanValue()) {
			final StringBuilder _spacing = highlight.getSpacing();
			_xifexpression = _spacing.toString();
		} else {
			_xifexpression = "";
		}
		final String sb = _xifexpression;
		final String _entering = this.entering(frag, highlight);
		final String _plus = (sb + _entering);
		final String _stringFrag = this.toStringFrag(frag, highlight, false);
		final String _plus_1 = (_plus + _stringFrag);
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("(");
		{
			final ArrayList<HDLExpression> _params = frag.getParams();
			boolean _hasElements = false;
			for (final HDLExpression p : _params) {
				if (!_hasElements) {
					_hasElements = true;
				} else {
					_builder.appendImmediate(",", "");
				}
				final String _string = this.toString(p, highlight);
				_builder.append(_string, "");
			}
		}
		_builder.append(")");
		String res = (_plus_1 + _builder);
		final HDLUnresolvedFragment _sub = frag.getSub();
		final boolean _tripleNotEquals = (_sub != null);
		if (_tripleNotEquals) {
			final HDLUnresolvedFragment _sub_1 = frag.getSub();
			final String _string_1 = this.toString(_sub_1, highlight);
			final String _plus_2 = ((res + ".") + _string_1);
			res = _plus_2;
		}
		final Boolean _isStatement_1 = frag.getIsStatement();
		if ((_isStatement_1).booleanValue()) {
			res = (res + ";");
		}
		final String _leaving = this.leaving(frag, highlight);
		return (res + _leaving);
	}

	protected String _toString(final HDLUnresolvedFragment frag, final SyntaxHighlighter highlight) {
		final String _entering = this.entering(frag, highlight);
		final String _stringFrag = this.toStringFrag(frag, highlight, true);
		String string = (_entering + _stringFrag);
		final Boolean _isStatement = frag.getIsStatement();
		if ((_isStatement).booleanValue()) {
			string = (string + ";");
		}
		final String _leaving = this.leaving(frag, highlight);
		return (string + _leaving);
	}

	public String toStringFrag(final HDLUnresolvedFragment frag, final SyntaxHighlighter highlight, final boolean doSub) {
		final StringBuilder sb = new StringBuilder();
		final String _frag = frag.getFrag();
		sb.append(_frag);
		final StringConcatenation _builder = new StringConcatenation();
		{
			final ArrayList<HDLExpression> _array = frag.getArray();
			boolean _hasElements = false;
			for (final HDLExpression p : _array) {
				if (!_hasElements) {
					_hasElements = true;
					_builder.append("[", "");
				} else {
					_builder.appendImmediate("][", "");
				}
				final String _string = this.toString(p, highlight);
				_builder.append(_string, "");
			}
			if (_hasElements) {
				_builder.append("]", "");
			}
		}
		sb.append(_builder);
		final StringConcatenation _builder_1 = new StringConcatenation();
		{
			final ArrayList<HDLRange> _bits = frag.getBits();
			boolean _hasElements_1 = false;
			for (final HDLRange p_1 : _bits) {
				if (!_hasElements_1) {
					_hasElements_1 = true;
					_builder_1.append("{", "");
				} else {
					_builder_1.appendImmediate(",", "");
				}
				final String _string_1 = this.toString(p_1, highlight);
				_builder_1.append(_string_1, "");
			}
			if (_hasElements_1) {
				_builder_1.append("}", "");
			}
		}
		sb.append(_builder_1);
		if ((doSub && (frag.getSub() != null))) {
			final StringBuilder _append = sb.append(".");
			final HDLUnresolvedFragment _sub = frag.getSub();
			final String _string_2 = this.toString(_sub, highlight);
			_append.append(_string_2);
		}
		return sb.toString();
	}

	protected String _toString(final HDLBitOp bitOp, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		final String _entering = this.entering(bitOp, highlight);
		sb.append(_entering);
		final StringBuilder _append = sb.append("(");
		final HDLExpression _left = bitOp.getLeft();
		final String _string = this.toString(_left, highlight);
		_append.append(_string);
		final HDLBitOp.HDLBitOpType type = bitOp.getType();
		if ((Objects.equal(type, HDLBitOp.HDLBitOpType.LOGI_AND) || Objects.equal(type, HDLBitOp.HDLBitOpType.LOGI_OR))) {
			final String _simpleSpace = highlight.simpleSpace();
			final StringBuilder _append_1 = sb.append(_simpleSpace);
			final String _string_1 = type.toString();
			final String _operator = highlight.operator(_string_1);
			final StringBuilder _append_2 = _append_1.append(_operator);
			final String _simpleSpace_1 = highlight.simpleSpace();
			_append_2.append(_simpleSpace_1);
		} else {
			final String _string_2 = type.toString();
			final String _operator_1 = highlight.operator(_string_2);
			sb.append(_operator_1);
		}
		final HDLExpression _right = bitOp.getRight();
		final String _string_3 = this.toString(_right, highlight);
		final StringBuilder _append_3 = sb.append(_string_3);
		_append_3.append(")");
		final String _leaving = this.leaving(bitOp, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLConcat concat, final SyntaxHighlighter highlight) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("(");
		final String _entering = this.entering(concat, highlight);
		_builder.append(_entering, "");
		{
			final ArrayList<HDLExpression> _cats = concat.getCats();
			boolean _hasElements = false;
			for (final HDLExpression cat : _cats) {
				if (!_hasElements) {
					_hasElements = true;
				} else {
					final String _operator = highlight.operator("#");
					_builder.appendImmediate(_operator, "");
				}
				final String _string = this.toString(cat, highlight);
				final String _operator_1 = highlight.operator(_string);
				_builder.append(_operator_1, "");
			}
		}
		final String _leaving = this.leaving(concat, highlight);
		_builder.append(_leaving, "");
		_builder.append(")");
		return _builder.toString();
	}

	protected String _toString(final HDLFunctionCall func, final SyntaxHighlighter highlight) {
		boolean isStatement = false;
		final IHDLObject _container = func.getContainer();
		final IHDLObject container = _container;
		boolean _matched = false;
		if (container instanceof HDLBlock) {
			_matched = true;
			isStatement = true;
		}
		if (!_matched) {
			if (container instanceof HDLStatement) {
				_matched = true;
				isStatement = (((!(container instanceof HDLAssignment)) && (!(container instanceof HDLFunctionCall))) && (!(container instanceof HDLInlineFunction)));
			}
		}
		if (!_matched) {
			if (container instanceof HDLUnit) {
				_matched = true;
				isStatement = true;
			}
		}
		StringBuilder _xifexpression = null;
		if (isStatement) {
			_xifexpression = highlight.getSpacing();
		} else {
			_xifexpression = new StringBuilder();
		}
		final StringBuilder sb = _xifexpression;
		final String _entering = this.entering(func, highlight);
		sb.append(_entering);
		final HDLQualifiedName _functionRefName = func.getFunctionRefName();
		final String _string = _functionRefName.toString();
		final String _functionCall = highlight.functionCall(_string);
		final StringBuilder _append = sb.append(_functionCall);
		_append.append("(");
		final StringConcatenation _builder = new StringConcatenation();
		{
			final ArrayList<HDLExpression> _params = func.getParams();
			boolean _hasElements = false;
			for (final HDLExpression p : _params) {
				if (!_hasElements) {
					_hasElements = true;
				} else {
					_builder.appendImmediate(",", "");
				}
				final String _string_1 = this.toString(p, highlight);
				_builder.append(_string_1, "");
			}
		}
		sb.append(_builder);
		sb.append(")");
		if (isStatement) {
			sb.append(";");
		}
		final String _leaving = this.leaving(func, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLFunctionParameter func, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		final HDLFunctionParameter.RWType _rw = func.getRw();
		final boolean _tripleNotEquals = (_rw != HDLFunctionParameter.RWType.READ);
		if (_tripleNotEquals) {
			final HDLFunctionParameter.RWType _rw_1 = func.getRw();
			sb.append(_rw_1);
		}
		final HDLFunctionParameter.Type _type = func.getType();
		sb.append(_type);
		final HDLFunctionParameter.Type _type_1 = func.getType();
		if (_type_1 != null) {
			switch (_type_1) {
			case PARAM_ENUM:
				final StringBuilder _append = sb.append("<");
				final HDLQualifiedName _enumSpecRefName = func.getEnumSpecRefName();
				final String _string = _enumSpecRefName.toString();
				final String _enumRefType = highlight.enumRefType(_string);
				final StringBuilder _append_1 = _append.append(_enumRefType);
				_append_1.append(">");
				break;
			case PARAM_IF:
				final StringBuilder _append_2 = sb.append("<");
				final HDLQualifiedName _enumSpecRefName_1 = func.getEnumSpecRefName();
				final String _string_1 = _enumSpecRefName_1.toString();
				final String _enumRefType_1 = highlight.enumRefType(_string_1);
				final StringBuilder _append_3 = _append_2.append(_enumRefType_1);
				_append_3.append(">");
				break;
			case PARAM_FUNCTION:
				final StringConcatenation _builder = new StringConcatenation();
				_builder.append("<"); {
				final ArrayList<HDLFunctionParameter> _funcSpec = func.getFuncSpec();
				boolean _hasElements = false;
				for (final HDLFunctionParameter p : _funcSpec) {
					if (!_hasElements) {
						_hasElements = true;
					} else {
						_builder.appendImmediate(",", "");
					}
					final String _string_2 = this.toString(p, highlight);
					_builder.append(_string_2, "");
				}
			}
				sb.append(_builder);
				final HDLFunctionParameter _funcReturnSpec = func.getFuncReturnSpec();
				final boolean _tripleNotEquals_1 = (_funcReturnSpec != null);
				if (_tripleNotEquals_1) {
					final String _simpleSpace = highlight.simpleSpace();
					final StringBuilder _append_4 = sb.append(_simpleSpace);
					final StringBuilder _append_5 = _append_4.append("=>");
					final String _simpleSpace_1 = highlight.simpleSpace();
					final StringBuilder _append_6 = _append_5.append(_simpleSpace_1);
					final HDLFunctionParameter _funcReturnSpec_1 = func.getFuncReturnSpec();
					final String _string_3 = this.toString(_funcReturnSpec_1, highlight);
					_append_6.append(_string_3);
				}
				sb.append(">");
				break;
			default:
				break;
			}
		}
		final HDLVariable _name = func.getName();
		final boolean _tripleNotEquals_2 = (_name != null);
		if (_tripleNotEquals_2) {
			final String _simpleSpace_2 = highlight.simpleSpace();
			final StringBuilder _append_7 = sb.append(_simpleSpace_2);
			final HDLVariable _name_1 = func.getName();
			final String _varName = highlight.varName(_name_1);
			_append_7.append(_varName);
		}
		final ArrayList<HDLExpression> _dim = func.getDim();
		for (final HDLExpression d : _dim) {
			boolean _matched = false;
			if (d instanceof HDLLiteral) {
				final Boolean _str = ((HDLLiteral) d).getStr();
				if (_str) {
					_matched = true;
					sb.append("[]");
				}
			}
			if (!_matched) {
				final StringBuilder _append_8 = sb.append("[");
				final String _string_4 = this.toString(d, highlight);
				final StringBuilder _append_9 = _append_8.append(_string_4);
				_append_9.append("]");
			}
		}
		return sb.toString();
	}

	protected String _toString(final HDLNativeFunction func, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		final String _entering = this.entering(func, highlight);
		sb.append(_entering);
		final ArrayList<HDLAnnotation> _annotations = func.getAnnotations();
		for (final HDLAnnotation anno : _annotations) {
			final String _string = this.toString(anno, highlight);
			final StringBuilder _append = sb.append(_string);
			final String _simpleSpace = highlight.simpleSpace();
			_append.append(_simpleSpace);
		}
		final Boolean _simOnly = func.getSimOnly();
		if ((_simOnly).booleanValue()) {
			final String _keyword = highlight.keyword("simulation");
			final StringBuilder _append_1 = sb.append(_keyword);
			final String _simpleSpace_1 = highlight.simpleSpace();
			_append_1.append(_simpleSpace_1);
		}
		final String _keyword_1 = highlight.keyword("native");
		sb.append(_keyword_1);
		final String _simpleSpace_2 = highlight.simpleSpace();
		sb.append(_simpleSpace_2);
		final String _keyword_2 = highlight.keyword("function");
		sb.append(_keyword_2);
		final String _simpleSpace_3 = highlight.simpleSpace();
		sb.append(_simpleSpace_3);
		final HDLFunctionParameter _returnType = func.getReturnType();
		final boolean _tripleNotEquals = (_returnType != null);
		if (_tripleNotEquals) {
			final HDLFunctionParameter _returnType_1 = func.getReturnType();
			final String _string_1 = this.toString(_returnType_1, highlight);
			final StringBuilder _append_2 = sb.append(_string_1);
			final String _simpleSpace_4 = highlight.simpleSpace();
			_append_2.append(_simpleSpace_4);
		}
		final String _name = func.getName();
		final String _functionDecl = highlight.functionDecl(_name);
		sb.append(_functionDecl);
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("(");
		{
			final ArrayList<HDLFunctionParameter> _args = func.getArgs();
			boolean _hasElements = false;
			for (final HDLFunctionParameter arg : _args) {
				if (!_hasElements) {
					_hasElements = true;
				} else {
					_builder.appendImmediate(",", "");
				}
				final String _string_2 = this.toString(arg, highlight);
				_builder.append(_string_2, "");
			}
		}
		_builder.append(")");
		sb.append(_builder);
		final StringBuilder _append_3 = sb.append(";");
		final String _newLine = highlight.newLine();
		_append_3.append(_newLine);
		final String _leaving = this.leaving(func, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLInlineFunction func, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		final String _entering = this.entering(func, highlight);
		sb.append(_entering);
		final ArrayList<HDLAnnotation> _annotations = func.getAnnotations();
		for (final HDLAnnotation anno : _annotations) {
			final String _string = this.toString(anno, highlight);
			final StringBuilder _append = sb.append(_string);
			final String _simpleSpace = highlight.simpleSpace();
			_append.append(_simpleSpace);
		}
		final String _keyword = highlight.keyword("inline");
		final StringBuilder _append_1 = sb.append(_keyword);
		final String _simpleSpace_1 = highlight.simpleSpace();
		final StringBuilder _append_2 = _append_1.append(_simpleSpace_1);
		final String _keyword_1 = highlight.keyword("function");
		_append_2.append(_keyword_1);
		final String _simpleSpace_2 = highlight.simpleSpace();
		sb.append(_simpleSpace_2);
		final HDLFunctionParameter _returnType = func.getReturnType();
		final String _string_1 = this.toString(_returnType, highlight);
		final StringBuilder _append_3 = sb.append(_string_1);
		final String _simpleSpace_3 = highlight.simpleSpace();
		_append_3.append(_simpleSpace_3);
		final String _name = func.getName();
		final String _functionDecl = highlight.functionDecl(_name);
		sb.append(_functionDecl);
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("(");
		{
			final ArrayList<HDLFunctionParameter> _args = func.getArgs();
			boolean _hasElements = false;
			for (final HDLFunctionParameter arg : _args) {
				if (!_hasElements) {
					_hasElements = true;
				} else {
					_builder.appendImmediate(",", "");
				}
				final String _string_2 = this.toString(arg, highlight);
				_builder.append(_string_2, "");
			}
		}
		_builder.append(")");
		sb.append(_builder);
		final String _simpleSpace_4 = highlight.simpleSpace();
		final StringBuilder _append_4 = sb.append(_simpleSpace_4);
		final StringBuilder _append_5 = _append_4.append("->");
		final String _simpleSpace_5 = highlight.simpleSpace();
		final StringBuilder _append_6 = _append_5.append(_simpleSpace_5);
		final StringBuilder _append_7 = _append_6.append("(");
		final HDLExpression _expr = func.getExpr();
		final String _string_3 = this.toString(_expr, highlight);
		final StringBuilder _append_8 = _append_7.append(_string_3);
		final StringBuilder _append_9 = _append_8.append(")");
		final String _newLine = highlight.newLine();
		_append_9.append(_newLine);
		final String _leaving = this.leaving(func, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLSubstituteFunction func, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		final String _entering = this.entering(func, highlight);
		sb.append(_entering);
		final ArrayList<HDLAnnotation> _annotations = func.getAnnotations();
		for (final HDLAnnotation anno : _annotations) {
			final String _string = this.toString(anno, highlight);
			final StringBuilder _append = sb.append(_string);
			final String _simpleSpace = highlight.simpleSpace();
			_append.append(_simpleSpace);
		}
		final String _keyword = highlight.keyword("substitute");
		final StringBuilder _append_1 = sb.append(_keyword);
		final String _simpleSpace_1 = highlight.simpleSpace();
		final StringBuilder _append_2 = _append_1.append(_simpleSpace_1);
		final String _keyword_1 = highlight.keyword("function");
		final StringBuilder _append_3 = _append_2.append(_keyword_1);
		final String _simpleSpace_2 = highlight.simpleSpace();
		_append_3.append(_simpleSpace_2);
		final HDLFunctionParameter _returnType = func.getReturnType();
		final boolean _tripleNotEquals = (_returnType != null);
		if (_tripleNotEquals) {
			final HDLFunctionParameter _returnType_1 = func.getReturnType();
			final String _string_1 = this.toString(_returnType_1, highlight);
			final StringBuilder _append_4 = sb.append(_string_1);
			final String _simpleSpace_3 = highlight.simpleSpace();
			_append_4.append(_simpleSpace_3);
		}
		final String _name = func.getName();
		final String _functionDecl = highlight.functionDecl(_name);
		sb.append(_functionDecl);
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("(");
		{
			final ArrayList<HDLFunctionParameter> _args = func.getArgs();
			boolean _hasElements = false;
			for (final HDLFunctionParameter arg : _args) {
				if (!_hasElements) {
					_hasElements = true;
				} else {
					_builder.appendImmediate(",", "");
				}
				final String _string_2 = this.toString(arg, highlight);
				_builder.append(_string_2, "");
			}
		}
		_builder.append(")");
		sb.append(_builder);
		final String _simpleSpace_4 = highlight.simpleSpace();
		final StringBuilder _append_5 = sb.append(_simpleSpace_4);
		final StringBuilder _append_6 = _append_5.append("{");
		final String _newLine = highlight.newLine();
		_append_6.append(_newLine);
		highlight.incSpacing();
		final ArrayList<HDLStatement> _stmnts = func.getStmnts();
		for (final HDLStatement string : _stmnts) {
			final String _string_3 = this.toString(string, highlight);
			final StringBuilder _append_7 = sb.append(_string_3);
			final String _newLine_1 = highlight.newLine();
			_append_7.append(_newLine_1);
		}
		highlight.decSpacing();
		final StringBuilder _append_8 = sb.append("}");
		final String _newLine_2 = highlight.newLine();
		_append_8.append(_newLine_2);
		final String _leaving = this.leaving(func, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLInterfaceRef ref, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		final String _entering = this.entering(ref, highlight);
		sb.append(_entering);
		final HDLQualifiedName _hIfRefName = ref.getHIfRefName();
		final String _lastSegment = _hIfRefName.getLastSegment();
		final String _interfaceRef = highlight.interfaceRef(_lastSegment);
		sb.append(_interfaceRef);
		final ArrayList<HDLExpression> _ifArray = ref.getIfArray();
		for (final HDLExpression arr : _ifArray) {
			final StringBuilder _append = sb.append("[");
			final String _string = this.toString(arr, highlight);
			final StringBuilder _append_1 = _append.append(_string);
			_append_1.append("]");
		}
		sb.append(".");
		final StringBuilder _varRef = this.varRef(ref, highlight);
		sb.append(_varRef);
		final String _leaving = this.leaving(ref, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLVariableRef ref, final SyntaxHighlighter highlight) {
		final String _entering = this.entering(ref, highlight);
		final StringBuilder _varRef = this.varRef(ref, highlight);
		final String _string = _varRef.toString();
		final String _plus = (_entering + _string);
		final String _leaving = this.leaving(ref, highlight);
		return (_plus + _leaving);
	}

	public StringBuilder varRef(final HDLVariableRef ref, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		final String _entering = this.entering(ref, highlight);
		sb.append(_entering);
		final String _variableRefName = highlight.variableRefName(ref);
		sb.append(_variableRefName);
		final ArrayList<HDLExpression> _array = ref.getArray();
		for (final HDLExpression a : _array) {
			final StringBuilder _append = sb.append("[");
			final String _string = this.toString(a, highlight);
			final StringBuilder _append_1 = _append.append(_string);
			_append_1.append("]");
		}
		final ArrayList<HDLRange> _bits = ref.getBits();
		final int _size = _bits.size();
		final boolean _notEquals = (_size != 0);
		if (_notEquals) {
			final StringConcatenation _builder = new StringConcatenation();
			_builder.append("{");
			{
				final ArrayList<HDLRange> _bits_1 = ref.getBits();
				boolean _hasElements = false;
				for (final HDLRange bit : _bits_1) {
					if (!_hasElements) {
						_hasElements = true;
					} else {
						_builder.appendImmediate(",", "");
					}
					final String _string_1 = this.toString(bit, highlight);
					_builder.append(_string_1, "");
				}
			}
			_builder.append("}");
			sb.append(_builder);
		}
		final String _leaving = this.leaving(ref, highlight);
		sb.append(_leaving);
		return sb;
	}

	protected String _toString(final HDLLiteral lit, final SyntaxHighlighter highlight) {
		final Boolean _str = lit.getStr();
		if ((_str).booleanValue()) {
			final String _entering = this.entering(lit, highlight);
			final String _val = lit.getVal();
			final String _plus = ("\"" + _val);
			final String _plus_1 = (_plus + "\"");
			final String _literal = highlight.literal(_plus_1);
			final String _plus_2 = (_entering + _literal);
			final String _leaving = this.leaving(lit, highlight);
			return (_plus_2 + _leaving);
		}
		final String _entering_1 = this.entering(lit, highlight);
		final String _val_1 = lit.getVal();
		final String _literal_1 = highlight.literal(_val_1);
		final String _plus_3 = (_entering_1 + _literal_1);
		final String _leaving_1 = this.leaving(lit, highlight);
		return (_plus_3 + _leaving_1);
	}

	protected String _toString(final HDLManip manip, final SyntaxHighlighter highlight) {
		final HDLManip.HDLManipType manipType = manip.getType();
		if (manipType != null) {
			switch (manipType) {
			case ARITH_NEG:
				final String _entering = this.entering(manip, highlight);
				final String _operator = highlight.operator("-");
				final String _plus = (_entering + _operator);
				final HDLExpression _target = manip.getTarget();
				final String _string = this.toString(_target, highlight);
				final String _plus_1 = (_plus + _string);
				final String _leaving = this.leaving(manip, highlight);
				return (_plus_1 + _leaving);
			case CAST:
				final HDLType _castTo = manip.getCastTo();
				final HDLPrimitive type = ((HDLPrimitive) _castTo);
				final String entering = this.entering(manip, highlight);
				String _xifexpression = null;
				final HDLExpression _width = type.getWidth();
				final boolean _tripleNotEquals = (_width != null);
				if (_tripleNotEquals) {
					final HDLExpression _width_1 = type.getWidth();
					final String _string_1 = this.toString(_width_1, highlight);
					final String _plus_2 = ("<" + _string_1);
					final String _plus_3 = (_plus_2 + ">");
					_xifexpression = highlight.width(_plus_3);
				} else {
					_xifexpression = "";
				}
				final String width = _xifexpression;
				final HDLPrimitive.HDLPrimitiveType _type = type.getType();
				final String _string_2 = _type.toString();
				final String _lowerCase = _string_2.toLowerCase();
				final String _keyword = highlight.keyword(_lowerCase);
				final String _plus_4 = ((entering + "(") + _keyword);
				final String _plus_5 = (_plus_4 + width);
				final String _plus_6 = (_plus_5 + ")");
				final HDLExpression _target_1 = manip.getTarget();
				final String _string_3 = this.toString(_target_1, highlight);
				final String _plus_7 = (_plus_6 + _string_3);
				final String _leaving_1 = this.leaving(manip, highlight);
				return (_plus_7 + _leaving_1);
			case BIT_NEG:
				final String _entering_1 = this.entering(manip, highlight);
				final String _operator_1 = highlight.operator("~");
				final String _plus_8 = (_entering_1 + _operator_1);
				final HDLExpression _target_2 = manip.getTarget();
				final String _string_4 = this.toString(_target_2, highlight);
				final String _plus_9 = (_plus_8 + _string_4);
				final String _leaving_2 = this.leaving(manip, highlight);
				return (_plus_9 + _leaving_2);
			case LOGIC_NEG:
				final String _entering_2 = this.entering(manip, highlight);
				final String _operator_2 = highlight.operator("!");
				final String _plus_10 = (_entering_2 + _operator_2);
				final HDLExpression _target_3 = manip.getTarget();
				final String _string_5 = this.toString(_target_3, highlight);
				final String _plus_11 = (_plus_10 + _string_5);
				final String _leaving_3 = this.leaving(manip, highlight);
				return (_plus_11 + _leaving_3);
			default:
				break;
			}
		}
		final HDLManip.HDLManipType _type_1 = manip.getType();
		final String _plus_12 = ("Unexpected Type:" + _type_1);
		throw new IllegalArgumentException(_plus_12);
	}

	protected String _toString(final HDLBlock block, final SyntaxHighlighter highlight) {
		final StringBuilder _spacing = highlight.getSpacing();
		final StringBuilder sb = new StringBuilder(_spacing);
		final String _entering = this.entering(block, highlight);
		sb.append(_entering);
		final Boolean _process = block.getProcess();
		if ((_process).booleanValue()) {
			final StringBuilder _append = sb.append("process");
			final String _simpleSpace = highlight.simpleSpace();
			_append.append(_simpleSpace);
		}
		final StringBuilder _append_1 = sb.append("{");
		final String _newLine = highlight.newLine();
		_append_1.append(_newLine);
		highlight.incSpacing();
		final ArrayList<HDLStatement> _statements = block.getStatements();
		for (final HDLStatement string : _statements) {
			final String _string = this.toString(string, highlight);
			final StringBuilder _append_2 = sb.append(_string);
			final String _newLine_1 = highlight.newLine();
			_append_2.append(_newLine_1);
		}
		highlight.decSpacing();
		final StringBuilder _spacing_1 = highlight.getSpacing();
		final StringBuilder _append_3 = sb.append(_spacing_1);
		_append_3.append("}");
		final String _leaving = this.leaving(block, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLExport export, final SyntaxHighlighter highlight) {
		final StringBuilder builder = highlight.getSpacing();
		final String _entering = this.entering(export, highlight);
		builder.append(_entering);
		final String _keyword = highlight.keyword("export");
		builder.append(_keyword);
		final String _simpleSpace = highlight.simpleSpace();
		builder.append(_simpleSpace);
		final HDLQualifiedName _hIfRefName = export.getHIfRefName();
		final String _lastSegment = _hIfRefName.getLastSegment();
		final String _interfaceRef = highlight.interfaceRef(_lastSegment);
		builder.append(_interfaceRef);
		final HDLQualifiedName _varRefName = export.getVarRefName();
		final boolean _tripleNotEquals = (_varRefName != null);
		if (_tripleNotEquals) {
			builder.append(".");
			final HDLVariableRef _hDLVariableRef = new HDLVariableRef();
			final HDLQualifiedName _varRefName_1 = export.getVarRefName();
			final HDLVariableRef _setVar = _hDLVariableRef.setVar(_varRefName_1);
			final StringBuilder _varRef = this.varRef(_setVar, highlight);
			builder.append(_varRef);
		}
		final String _match = export.getMatch();
		final boolean _tripleNotEquals_1 = (_match != null);
		if (_tripleNotEquals_1) {
			builder.append(".");
			final String _match_1 = export.getMatch();
			final String _exportMatch = highlight.exportMatch(_match_1);
			builder.append(_exportMatch);
		}
		builder.append(";");
		final String _leaving = this.leaving(export, highlight);
		builder.append(_leaving);
		return builder.toString();
	}

	protected String _toString(final HDLAssignment ass, final SyntaxHighlighter highlight) {
		final StringBuilder builder = highlight.getSpacing();
		final String _entering = this.entering(ass, highlight);
		builder.append(_entering);
		final HDLReference _left = ass.getLeft();
		final String _string = this.toString(_left, highlight);
		builder.append(_string);
		final HDLAssignment.HDLAssignmentType _type = ass.getType();
		final String _string_1 = _type.toString();
		final String _operator = highlight.operator(_string_1);
		builder.append(_operator);
		final HDLExpression _right = ass.getRight();
		final String _string_2 = this.toString(_right, highlight);
		final StringBuilder _append = builder.append(_string_2);
		_append.append(";");
		final String _leaving = this.leaving(ass, highlight);
		builder.append(_leaving);
		return builder.toString();
	}

	protected String _toString(final HDLPrimitive prim, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		final String _entering = this.entering(prim, highlight);
		sb.append(_entering);
		final HDLPrimitive.HDLPrimitiveType _type = prim.getType();
		final String _string = _type.toString();
		final String _lowerCase = _string.toLowerCase();
		final String _primitiveType = highlight.primitiveType(_lowerCase);
		sb.append(_primitiveType);
		final HDLExpression _width = prim.getWidth();
		final boolean _tripleNotEquals = (_width != null);
		if (_tripleNotEquals) {
			final HDLExpression _width_1 = prim.getWidth();
			final String _string_1 = this.toString(_width_1, highlight);
			final String _plus = ("<" + _string_1);
			final String _plus_1 = (_plus + ">");
			final String _width_2 = highlight.width(_plus_1);
			sb.append(_width_2);
		}
		final String _leaving = this.leaving(prim, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLForLoop loop, final SyntaxHighlighter highlight) {
		final StringBuilder space = highlight.getSpacing();
		final StringBuilder sb = new StringBuilder();
		final String _entering = this.entering(loop, highlight);
		sb.append(_entering);
		final StringBuilder _append = sb.append(space);
		final String _keyword = highlight.keyword("for");
		final StringBuilder _append_1 = _append.append(_keyword);
		final String _simpleSpace = highlight.simpleSpace();
		final StringBuilder _append_2 = _append_1.append(_simpleSpace);
		final StringBuilder _append_3 = _append_2.append("(");
		final HDLVariable _param = loop.getParam();
		final String _name = _param.getName();
		final StringBuilder _append_4 = _append_3.append(_name);
		final String _simpleSpace_1 = highlight.simpleSpace();
		final StringBuilder _append_5 = _append_4.append(_simpleSpace_1);
		final StringBuilder _append_6 = _append_5.append("=");
		final String _simpleSpace_2 = highlight.simpleSpace();
		_append_6.append(_simpleSpace_2);
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("{");
		{
			final ArrayList<HDLRange> _range = loop.getRange();
			boolean _hasElements = false;
			for (final HDLRange range : _range) {
				if (!_hasElements) {
					_hasElements = true;
				} else {
					_builder.appendImmediate(",", "");
				}
				final String _string = this.toString(range, highlight);
				_builder.append(_string, "");
			}
		}
		_builder.append("}");
		sb.append(_builder);
		final StringBuilder _append_7 = sb.append(")");
		final String _simpleSpace_3 = highlight.simpleSpace();
		final StringBuilder _append_8 = _append_7.append(_simpleSpace_3);
		final StringBuilder _append_9 = _append_8.append("{");
		final String _newLine = highlight.newLine();
		_append_9.append(_newLine);
		highlight.incSpacing();
		final ArrayList<HDLStatement> _dos = loop.getDos();
		for (final HDLStatement string : _dos) {
			final String _string_1 = this.toString(string, highlight);
			final StringBuilder _append_10 = sb.append(_string_1);
			final String _newLine_1 = highlight.newLine();
			_append_10.append(_newLine_1);
		}
		highlight.decSpacing();
		final StringBuilder _append_11 = sb.append(space);
		_append_11.append("}");
		final String _leaving = this.leaving(loop, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLIfStatement ifStmnt, final SyntaxHighlighter highlight) {
		final StringBuilder sb = highlight.getSpacing();
		final String _entering = this.entering(ifStmnt, highlight);
		sb.append(_entering);
		final String origSpacing = sb.toString();
		final String _keyword = highlight.keyword("if");
		final StringBuilder _append = sb.append(_keyword);
		final String _simpleSpace = highlight.simpleSpace();
		final StringBuilder _append_1 = _append.append(_simpleSpace);
		final StringBuilder _append_2 = _append_1.append("(");
		final HDLExpression _ifExp = ifStmnt.getIfExp();
		final String _string = this.toString(_ifExp, highlight);
		final StringBuilder _append_3 = _append_2.append(_string);
		final StringBuilder _append_4 = _append_3.append(")");
		final String _simpleSpace_1 = highlight.simpleSpace();
		final StringBuilder _append_5 = _append_4.append(_simpleSpace_1);
		final StringBuilder _append_6 = _append_5.append("{");
		final String _newLine = highlight.newLine();
		_append_6.append(_newLine);
		highlight.incSpacing();
		final ArrayList<HDLStatement> _thenDo = ifStmnt.getThenDo();
		for (final HDLStatement stmt : _thenDo) {
			final String _string_1 = this.toString(stmt, highlight);
			final StringBuilder _append_7 = sb.append(_string_1);
			final String _newLine_1 = highlight.newLine();
			_append_7.append(_newLine_1);
		}
		final ArrayList<HDLStatement> _elseDo = ifStmnt.getElseDo();
		final int _size = _elseDo.size();
		final boolean _notEquals = (_size != 0);
		if (_notEquals) {
			final StringBuilder _append_8 = sb.append(origSpacing);
			final StringBuilder _append_9 = _append_8.append("}");
			final String _simpleSpace_2 = highlight.simpleSpace();
			final StringBuilder _append_10 = _append_9.append(_simpleSpace_2);
			final String _keyword_1 = highlight.keyword("else");
			final StringBuilder _append_11 = _append_10.append(_keyword_1);
			final String _simpleSpace_3 = highlight.simpleSpace();
			final StringBuilder _append_12 = _append_11.append(_simpleSpace_3);
			final StringBuilder _append_13 = _append_12.append("{");
			final String _newLine_2 = highlight.newLine();
			_append_13.append(_newLine_2);
			final ArrayList<HDLStatement> _elseDo_1 = ifStmnt.getElseDo();
			for (final HDLStatement stmt_1 : _elseDo_1) {
				final String _string_2 = this.toString(stmt_1, highlight);
				final StringBuilder _append_14 = sb.append(_string_2);
				final String _newLine_3 = highlight.newLine();
				_append_14.append(_newLine_3);
			}
		}
		highlight.decSpacing();
		final StringBuilder _append_15 = sb.append(origSpacing);
		_append_15.append("}");
		final String _leaving = this.leaving(ifStmnt, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLSwitchCaseStatement caseStmnt, final SyntaxHighlighter highlight) {
		final StringBuilder sb = highlight.getSpacing();
		final String _entering = this.entering(caseStmnt, highlight);
		sb.append(_entering);
		final HDLExpression _label = caseStmnt.getLabel();
		final boolean _tripleEquals = (_label == null);
		if (_tripleEquals) {
			final String _keyword = highlight.keyword("default");
			final StringBuilder _append = sb.append(_keyword);
			final StringBuilder _append_1 = _append.append(":");
			final String _simpleSpace = highlight.simpleSpace();
			final StringBuilder _append_2 = _append_1.append(_simpleSpace);
			final String _newLine = highlight.newLine();
			_append_2.append(_newLine);
		} else {
			final String _keyword_1 = highlight.keyword("case");
			final StringBuilder _append_3 = sb.append(_keyword_1);
			final String _simpleSpace_1 = highlight.simpleSpace();
			final StringBuilder _append_4 = _append_3.append(_simpleSpace_1);
			final HDLExpression _label_1 = caseStmnt.getLabel();
			final String _string = this.toString(_label_1, highlight);
			final StringBuilder _append_5 = _append_4.append(_string);
			final StringBuilder _append_6 = _append_5.append(":");
			final String _simpleSpace_2 = highlight.simpleSpace();
			final StringBuilder _append_7 = _append_6.append(_simpleSpace_2);
			final String _newLine_1 = highlight.newLine();
			_append_7.append(_newLine_1);
		}
		highlight.incSpacing();
		final StringConcatenation _builder = new StringConcatenation();
		{
			final ArrayList<HDLStatement> _dos = caseStmnt.getDos();
			boolean _hasElements = false;
			for (final HDLStatement stmnt : _dos) {
				if (!_hasElements) {
					_hasElements = true;
				} else {
					final String _newLine_2 = highlight.newLine();
					_builder.appendImmediate(_newLine_2, "");
				}
				final String _string_1 = this.toString(stmnt, highlight);
				_builder.append(_string_1, "");
			}
		}
		sb.append(_builder);
		highlight.decSpacing();
		final String _leaving = this.leaving(caseStmnt, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLSwitchStatement switchStmnt, final SyntaxHighlighter highlight) {
		final StringBuilder sb = highlight.getSpacing();
		final String _entering = this.entering(switchStmnt, highlight);
		sb.append(_entering);
		final String _keyword = highlight.keyword("switch");
		final StringBuilder _append = sb.append(_keyword);
		final StringBuilder _append_1 = _append.append("(");
		final HDLExpression _caseExp = switchStmnt.getCaseExp();
		final String _string = this.toString(_caseExp, highlight);
		final StringBuilder _append_2 = _append_1.append(_string);
		final StringBuilder _append_3 = _append_2.append(")");
		final String _simpleSpace = highlight.simpleSpace();
		final StringBuilder _append_4 = _append_3.append(_simpleSpace);
		final StringBuilder _append_5 = _append_4.append("{");
		final String _newLine = highlight.newLine();
		_append_5.append(_newLine);
		highlight.incSpacing();
		final ArrayList<HDLSwitchCaseStatement> _cases = switchStmnt.getCases();
		for (final HDLStatement stmnt : _cases) {
			final String _string_1 = this.toString(stmnt, highlight);
			final StringBuilder _append_6 = sb.append(_string_1);
			final String _newLine_1 = highlight.newLine();
			_append_6.append(_newLine_1);
		}
		highlight.decSpacing();
		final StringBuilder _spacing = highlight.getSpacing();
		final StringBuilder _append_7 = sb.append(_spacing);
		_append_7.append("}");
		final String _leaving = this.leaving(switchStmnt, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLVariableDeclaration hvd, final SyntaxHighlighter highlight) {
		final StringBuilder sb = highlight.getSpacing();
		final String _entering = this.entering(hvd, highlight);
		sb.append(_entering);
		final Optional<? extends HDLType> resolveType = hvd.resolveType();
		final ArrayList<HDLAnnotation> _annotations = hvd.getAnnotations();
		final boolean _tripleNotEquals = (_annotations != null);
		if (_tripleNotEquals) {
			final ArrayList<HDLAnnotation> _annotations_1 = hvd.getAnnotations();
			for (final HDLAnnotation hdla : _annotations_1) {
				final String _string = this.toString(hdla, highlight);
				final StringBuilder _append = sb.append(_string);
				final String _simpleSpace = highlight.simpleSpace();
				_append.append(_simpleSpace);
			}
		}
		final HDLVariableDeclaration.HDLDirection _direction = hvd.getDirection();
		final String dirString = _direction.toString();
		final int _length = dirString.length();
		final boolean _greaterThan = (_length > 0);
		if (_greaterThan) {
			final String _direction_1 = highlight.direction(dirString);
			final StringBuilder _append_1 = sb.append(_direction_1);
			final String _simpleSpace_1 = highlight.simpleSpace();
			_append_1.append(_simpleSpace_1);
		}
		final HDLRegisterConfig _register = hvd.getRegister();
		final boolean _tripleNotEquals_1 = (_register != null);
		if (_tripleNotEquals_1) {
			final HDLRegisterConfig _register_1 = hvd.getRegister();
			final String _string_1 = this.toString(_register_1, highlight);
			sb.append(_string_1);
		}
		final boolean _isPresent = resolveType.isPresent();
		final boolean _not = (!_isPresent);
		if (_not) {
			sb.append("#UNRESOLVED_TYPE#");
		} else {
			final HDLType _get = resolveType.get();
			if ((_get instanceof HDLEnum)) {
				final String _keyword = highlight.keyword("enum");
				final StringBuilder _append_2 = sb.append(_keyword);
				final String _simpleSpace_2 = highlight.simpleSpace();
				final StringBuilder _append_3 = _append_2.append(_simpleSpace_2);
				final HDLType _get_1 = resolveType.get();
				final String _string_2 = this.toString(_get_1, highlight);
				_append_3.append(_string_2);
			} else {
				final HDLType _get_2 = resolveType.get();
				final String _string_3 = this.toString(_get_2, highlight);
				sb.append(_string_3);
			}
		}
		final StringConcatenation _builder = new StringConcatenation();
		{
			final ArrayList<HDLVariable> _variables = hvd.getVariables();
			boolean _hasElements = false;
			for (final HDLVariable hvar : _variables) {
				if (!_hasElements) {
					_hasElements = true;
					final String _simpleSpace_3 = highlight.simpleSpace();
					_builder.append(_simpleSpace_3, "");
				} else {
					_builder.appendImmediate(",", "");
				}
				final String _string_4 = this.toString(hvar, highlight);
				_builder.append(_string_4, "");
			}
		}
		_builder.append(";");
		sb.append(_builder);
		final SyntaxHighlighter.Context _context = highlight.getContext();
		final boolean _equals = Objects.equal(_context, SyntaxHighlighter.Context.HDLPackage);
		if (_equals) {
			final String _newLine = highlight.newLine();
			sb.append(_newLine);
		}
		final String _leaving = this.leaving(hvd, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLInterfaceDeclaration hid, final SyntaxHighlighter highlight) {
		highlight.pushContext(SyntaxHighlighter.Context.HDLInterface);
		final StringBuilder annos = highlight.getSpacing();
		final String _entering = this.entering(hid, highlight);
		annos.append(_entering);
		final ArrayList<HDLAnnotation> _annotations = hid.getAnnotations();
		for (final HDLAnnotation anno : _annotations) {
			final String _string = this.toString(anno, highlight);
			final StringBuilder _append = annos.append(_string);
			final String _simpleSpace = highlight.simpleSpace();
			_append.append(_simpleSpace);
		}
		final HDLInterface _hIf = hid.getHIf();
		final String _string_1 = this.toString(_hIf, highlight);
		annos.append(_string_1);
		final String _leaving = this.leaving(hid, highlight);
		annos.append(_leaving);
		highlight.popContext();
		return annos.toString();
	}

	protected String _toString(final HDLInterface hif, final SyntaxHighlighter highlight) {
		final StringBuilder sb = highlight.getSpacing();
		final String _entering = this.entering(hif, highlight);
		sb.append(_entering);
		final String _keyword = highlight.keyword("interface");
		final StringBuilder _append = sb.append(_keyword);
		final String _simpleSpace = highlight.simpleSpace();
		_append.append(_simpleSpace);
		final String _name = hif.getName();
		final String _interfaceName = highlight.interfaceName(_name);
		sb.append(_interfaceName);
		final StringBuilder _append_1 = sb.append("{");
		final String _newLine = highlight.newLine();
		_append_1.append(_newLine);
		highlight.incSpacing();
		final ArrayList<HDLVariableDeclaration> _ports = hif.getPorts();
		for (final HDLVariableDeclaration hvar : _ports) {
			final String _string = this.toString(hvar, highlight);
			final StringBuilder _append_2 = sb.append(_string);
			final String _newLine_1 = highlight.newLine();
			_append_2.append(_newLine_1);
		}
		highlight.decSpacing();
		final StringBuilder _append_3 = sb.append("}");
		final String _newLine_2 = highlight.newLine();
		_append_3.append(_newLine_2);
		final String _leaving = this.leaving(hif, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLEnumRef ref, final SyntaxHighlighter highlight) {
		final String _entering = this.entering(ref, highlight);
		final HDLQualifiedName _hEnumRefName = ref.getHEnumRefName();
		final String _string = _hEnumRefName.toString();
		final String _enumRefType = highlight.enumRefType(_string);
		final String _plus = (_entering + _enumRefType);
		final String _plus_1 = (_plus + ".");
		final HDLQualifiedName _varRefName = ref.getVarRefName();
		final String _lastSegment = _varRefName.getLastSegment();
		final String _enumRefVar = highlight.enumRefVar(_lastSegment);
		final String _plus_2 = (_plus_1 + _enumRefVar);
		final String _leaving = this.leaving(ref, highlight);
		return (_plus_2 + _leaving);
	}

	protected String _toString(final HDLEnum e, final SyntaxHighlighter highlight) {
		final String _entering = this.entering(e, highlight);
		final String _name = e.getName();
		final String _enumName = highlight.enumName(_name);
		final String _plus = (_entering + _enumName);
		final String _leaving = this.leaving(e, highlight);
		return (_plus + _leaving);
	}

	protected String _toString(final HDLEnumDeclaration decl, final SyntaxHighlighter highlight) {
		final StringBuilder sb = highlight.getSpacing();
		final String _entering = this.entering(decl, highlight);
		sb.append(_entering);
		final ArrayList<HDLAnnotation> _annotations = decl.getAnnotations();
		for (final HDLAnnotation anno : _annotations) {
			final String _string = this.toString(anno, highlight);
			final StringBuilder _append = sb.append(_string);
			final String _simpleSpace = highlight.simpleSpace();
			_append.append(_simpleSpace);
		}
		final String _keyword = highlight.keyword("enum");
		final StringBuilder _append_1 = sb.append(_keyword);
		final String _simpleSpace_1 = highlight.simpleSpace();
		_append_1.append(_simpleSpace_1);
		final HDLEnum _hEnum = decl.getHEnum();
		final String _name = _hEnum.getName();
		final String _enumName = highlight.enumName(_name);
		sb.append(_enumName);
		final String _simpleSpace_2 = highlight.simpleSpace();
		sb.append(_simpleSpace_2);
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("{");
		{
			final HDLEnum _hEnum_1 = decl.getHEnum();
			final ArrayList<HDLVariable> _enums = _hEnum_1.getEnums();
			boolean _hasElements = false;
			for (final HDLVariable henum : _enums) {
				if (!_hasElements) {
					_hasElements = true;
				} else {
					final String _simpleSpace_3 = highlight.simpleSpace();
					final String _plus = ("," + _simpleSpace_3);
					_builder.appendImmediate(_plus, "");
				}
				final String _string_1 = this.toString(henum, highlight);
				_builder.append(_string_1, "");
			}
		}
		_builder.append("}");
		sb.append(_builder);
		final String _newLine = highlight.newLine();
		sb.append(_newLine);
		final String _leaving = this.leaving(decl, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLRegisterConfig reg, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		final String _entering = this.entering(reg, highlight);
		sb.append(_entering);
		final String _keyword = highlight.keyword("register");
		sb.append(_keyword);
		final HDLRegisterConfig defaultReg = HDLRegisterConfig.defaultConfig();
		final StringBuilder params = new StringBuilder();
		params.append("(");
		boolean first = true;
		final HDLExpression _clk = reg.getClk();
		final HDLExpression _clk_1 = defaultReg.getClk();
		final boolean _notEquals = (!Objects.equal(_clk, _clk_1));
		if (_notEquals) {
			final String _param = highlight.param(HDLRegisterConfig.CLOCK_PARAM);
			final StringBuilder _append = params.append(_param);
			final StringBuilder _append_1 = _append.append("=");
			final HDLExpression _clk_2 = reg.getClk();
			final String _string = this.toString(_clk_2, highlight);
			_append_1.append(_string);
			first = false;
		}
		final HDLExpression _rst = reg.getRst();
		final HDLExpression _rst_1 = defaultReg.getRst();
		final boolean _notEquals_1 = (!Objects.equal(_rst, _rst_1));
		if (_notEquals_1) {
			if ((!first)) {
				params.append(", ");
			}
			final String _param_1 = highlight.param(HDLRegisterConfig.RESET_PARAM);
			final StringBuilder _append_2 = params.append(_param_1);
			final StringBuilder _append_3 = _append_2.append("=");
			final HDLExpression _rst_2 = reg.getRst();
			final String _string_1 = this.toString(_rst_2, highlight);
			_append_3.append(_string_1);
			first = false;
		}
		final HDLExpression _unresolvedClockType = reg.getUnresolvedClockType();
		final boolean _tripleNotEquals = (_unresolvedClockType != null);
		if (_tripleNotEquals) {
			if ((!first)) {
				params.append(", ");
			}
			final String _param_2 = highlight.param(HDLRegisterConfig.EDGE_PARAM);
			final StringBuilder _append_4 = params.append(_param_2);
			final StringBuilder _append_5 = _append_4.append("=");
			final HDLExpression _unresolvedClockType_1 = reg.getUnresolvedClockType();
			final String _string_2 = this.toString(_unresolvedClockType_1, highlight);
			_append_5.append(_string_2);
			first = false;
		} else {
			if (((reg.getClockType() != null) && (reg.getClockType() != defaultReg.getClockType()))) {
				if ((!first)) {
					params.append(", ");
				}
				final String _param_3 = highlight.param(HDLRegisterConfig.EDGE_PARAM);
				final StringBuilder _append_6 = params.append(_param_3);
				final StringBuilder _append_7 = _append_6.append("=");
				final String _enumRefType = highlight.enumRefType("Edge");
				final StringBuilder _append_8 = _append_7.append(_enumRefType);
				final StringBuilder _append_9 = _append_8.append(".");
				final HDLRegisterConfig.HDLRegClockType _clockType = reg.getClockType();
				final String _string_3 = _clockType.toString();
				final String _enumRefVar = highlight.enumRefVar(_string_3);
				_append_9.append(_enumRefVar);
				first = false;
			}
		}
		final HDLExpression _unresolvedSyncType = reg.getUnresolvedSyncType();
		final boolean _tripleNotEquals_1 = (_unresolvedSyncType != null);
		if (_tripleNotEquals_1) {
			if ((!first)) {
				params.append(", ");
			}
			final String _param_4 = highlight.param(HDLRegisterConfig.EDGE_PARAM);
			final StringBuilder _append_10 = params.append(_param_4);
			final StringBuilder _append_11 = _append_10.append("=");
			final HDLExpression _unresolvedSyncType_1 = reg.getUnresolvedSyncType();
			final String _string_4 = this.toString(_unresolvedSyncType_1, highlight);
			_append_11.append(_string_4);
			first = false;
		} else {
			if (((reg.getSyncType() != null) && (reg.getSyncType() != defaultReg.getSyncType()))) {
				if ((!first)) {
					params.append(", ");
				}
				final String _param_5 = highlight.param(HDLRegisterConfig.RESET_SYNC_PARAM);
				final StringBuilder _append_12 = params.append(_param_5);
				final StringBuilder _append_13 = _append_12.append("=");
				final String _enumRefType_1 = highlight.enumRefType("Sync");
				final StringBuilder _append_14 = _append_13.append(_enumRefType_1);
				final StringBuilder _append_15 = _append_14.append(".");
				final HDLRegisterConfig.HDLRegSyncType _syncType = reg.getSyncType();
				final String _string_5 = _syncType.toString();
				final String _enumRefVar_1 = highlight.enumRefVar(_string_5);
				_append_15.append(_enumRefVar_1);
				first = false;
			}
		}
		final HDLExpression _unresolvedResetType = reg.getUnresolvedResetType();
		final boolean _tripleNotEquals_2 = (_unresolvedResetType != null);
		if (_tripleNotEquals_2) {
			if ((!first)) {
				params.append(", ");
			}
			final String _param_6 = highlight.param(HDLRegisterConfig.EDGE_PARAM);
			final StringBuilder _append_16 = params.append(_param_6);
			final StringBuilder _append_17 = _append_16.append("=");
			final HDLExpression _unresolvedResetType_1 = reg.getUnresolvedResetType();
			final String _string_6 = this.toString(_unresolvedResetType_1, highlight);
			_append_17.append(_string_6);
			first = false;
		} else {
			if (((reg.getResetType() != null) && (reg.getResetType() != defaultReg.getResetType()))) {
				if ((!first)) {
					params.append(", ");
				}
				final String _param_7 = highlight.param(HDLRegisterConfig.RESET_TYPE_PARAM);
				final StringBuilder _append_18 = params.append(_param_7);
				final StringBuilder _append_19 = _append_18.append("=");
				final String _enumRefType_2 = highlight.enumRefType("Active");
				final StringBuilder _append_20 = _append_19.append(_enumRefType_2);
				final StringBuilder _append_21 = _append_20.append(".");
				final HDLRegisterConfig.HDLRegResetActiveType _resetType = reg.getResetType();
				final String _string_7 = _resetType.toString();
				final String _enumRefVar_2 = highlight.enumRefVar(_string_7);
				_append_21.append(_enumRefVar_2);
				first = false;
			}
		}
		final HDLExpression _resetValue = reg.getResetValue();
		final HDLExpression _resetValue_1 = defaultReg.getResetValue();
		final boolean _notEquals_2 = (!Objects.equal(_resetValue, _resetValue_1));
		if (_notEquals_2) {
			if ((!first)) {
				params.append(", ");
			}
			final String _param_8 = highlight.param(HDLRegisterConfig.RESET_VALUE_PARAM);
			final StringBuilder _append_22 = params.append(_param_8);
			final StringBuilder _append_23 = _append_22.append("=");
			final HDLExpression _resetValue_2 = reg.getResetValue();
			final String _string_8 = this.toString(_resetValue_2, highlight);
			_append_23.append(_string_8);
			first = false;
		}
		final HDLExpression _delay = reg.getDelay();
		final HDLExpression _delay_1 = defaultReg.getDelay();
		final boolean _notEquals_3 = (!Objects.equal(_delay, _delay_1));
		if (_notEquals_3) {
			if ((!first)) {
				params.append(", ");
			}
			final String _param_9 = highlight.param(HDLRegisterConfig.DELAY_PARAM);
			final StringBuilder _append_24 = params.append(_param_9);
			final StringBuilder _append_25 = _append_24.append("=");
			final HDLExpression _delay_2 = reg.getDelay();
			final String _string_9 = this.toString(_delay_2, highlight);
			_append_25.append(_string_9);
			first = false;
		}
		params.append(")");
		if ((!first)) {
			sb.append(params);
		}
		final String _simpleSpace = highlight.simpleSpace();
		sb.append(_simpleSpace);
		final String _leaving = this.leaving(reg, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLPackage pkg, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		highlight.pushContext(SyntaxHighlighter.Context.HDLPackage);
		final String _entering = this.entering(pkg, highlight);
		sb.append(_entering);
		final String _pkg = pkg.getPkg();
		final boolean _tripleNotEquals = (_pkg != null);
		if (_tripleNotEquals) {
			final String _keyword = highlight.keyword("package");
			final StringBuilder _append = sb.append(_keyword);
			final String _simpleSpace = highlight.simpleSpace();
			final StringBuilder _append_1 = _append.append(_simpleSpace);
			final String _pkg_1 = pkg.getPkg();
			final String _packageName = highlight.packageName(_pkg_1);
			final StringBuilder _append_2 = _append_1.append(_packageName);
			final StringBuilder _append_3 = _append_2.append(";");
			final String _newLine = highlight.newLine();
			_append_3.append(_newLine);
		}
		final ArrayList<HDLDeclaration> _declarations = pkg.getDeclarations();
		for (final HDLDeclaration decl : _declarations) {
			final String _string = this.toString(decl, highlight);
			sb.append(_string);
		}
		final ArrayList<HDLUnit> _units = pkg.getUnits();
		for (final HDLUnit unit : _units) {
			final String _string_1 = this.toString(unit, highlight);
			sb.append(_string_1);
		}
		final String _leaving = this.leaving(pkg, highlight);
		sb.append(_leaving);
		highlight.popContext();
		return sb.toString();
	}

	protected String _toString(final HDLUnit unit, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		highlight.pushContext(SyntaxHighlighter.Context.HDLUnit);
		final String _entering = this.entering(unit, highlight);
		sb.append(_entering);
		final ArrayList<HDLAnnotation> _annotations = unit.getAnnotations();
		for (final HDLAnnotation anno : _annotations) {
			final String _string = this.toString(anno, highlight);
			final StringBuilder _append = sb.append(_string);
			final String _newLine = highlight.newLine();
			_append.append(_newLine);
		}
		final Boolean _simulation = unit.getSimulation();
		final boolean _not = (!(_simulation).booleanValue());
		if (_not) {
			final String _keyword = highlight.keyword("module");
			final StringBuilder _append_1 = sb.append(_keyword);
			final String _simpleSpace = highlight.simpleSpace();
			_append_1.append(_simpleSpace);
		} else {
			final String _keyword_1 = highlight.keyword("testbench");
			final StringBuilder _append_2 = sb.append(_keyword_1);
			final String _simpleSpace_1 = highlight.simpleSpace();
			_append_2.append(_simpleSpace_1);
		}
		final String _name = unit.getName();
		final String _unitName = highlight.unitName(_name);
		final StringBuilder _append_3 = sb.append(_unitName);
		final StringBuilder _append_4 = _append_3.append("{");
		final String _newLine_1 = highlight.newLine();
		_append_4.append(_newLine_1);
		highlight.incSpacing();
		final ArrayList<String> _imports = unit.getImports();
		for (final String imports : _imports) {
			final StringBuilder _spacing = highlight.getSpacing();
			final StringBuilder _append_5 = sb.append(_spacing);
			final String _keyword_2 = highlight.keyword("import");
			final StringBuilder _append_6 = _append_5.append(_keyword_2);
			final String _simpleSpace_2 = highlight.simpleSpace();
			final StringBuilder _append_7 = _append_6.append(_simpleSpace_2);
			final String _importName = highlight.importName(imports);
			final StringBuilder _append_8 = _append_7.append(_importName);
			final StringBuilder _append_9 = _append_8.append(";");
			final String _newLine_2 = highlight.newLine();
			_append_9.append(_newLine_2);
		}
		final ArrayList<HDLStatement> _inits = unit.getInits();
		for (final HDLStatement stmnt : _inits) {
			final String _string_1 = this.toString(stmnt, highlight);
			final StringBuilder _append_10 = sb.append(_string_1);
			final String _newLine_3 = highlight.newLine();
			_append_10.append(_newLine_3);
		}
		final ArrayList<HDLStatement> _statements = unit.getStatements();
		for (final HDLStatement stmnt_1 : _statements) {
			final String _string_2 = this.toString(stmnt_1, highlight);
			final StringBuilder _append_11 = sb.append(_string_2);
			final String _newLine_4 = highlight.newLine();
			_append_11.append(_newLine_4);
		}
		highlight.decSpacing();
		final StringBuilder _append_12 = sb.append("}");
		final String _newLine_5 = highlight.newLine();
		_append_12.append(_newLine_5);
		final String _leaving = this.leaving(unit, highlight);
		sb.append(_leaving);
		highlight.popContext();
		return sb.toString();
	}

	protected String _toString(final HDLInterfaceInstantiation hii, final SyntaxHighlighter highlight) {
		final StringBuilder sb = highlight.getSpacing();
		final String _entering = this.entering(hii, highlight);
		sb.append(_entering);
		final ArrayList<HDLAnnotation> _annotations = hii.getAnnotations();
		for (final HDLAnnotation anno : _annotations) {
			final String _string = this.toString(anno, highlight);
			final StringBuilder _append = sb.append(_string);
			final String _simpleSpace = highlight.simpleSpace();
			_append.append(_simpleSpace);
		}
		final HDLQualifiedName _hIfRefName = hii.getHIfRefName();
		final String _string_1 = _hIfRefName.toString();
		final String _interfaceName = highlight.interfaceName(_string_1);
		final StringBuilder _append_1 = sb.append(_interfaceName);
		final String _simpleSpace_1 = highlight.simpleSpace();
		final StringBuilder _append_2 = _append_1.append(_simpleSpace_1);
		final HDLVariable _var = hii.getVar();
		final String _string_2 = this.toString(_var, highlight);
		_append_2.append(_string_2);
		final StringConcatenation _builder = new StringConcatenation();
		{
			final ArrayList<HDLArgument> _arguments = hii.getArguments();
			boolean _hasElements = false;
			for (final HDLArgument arg : _arguments) {
				if (!_hasElements) {
					_hasElements = true;
					_builder.append("(", "");
				} else {
					_builder.appendImmediate(",", "");
				}
				final String _string_3 = this.toString(arg, highlight);
				_builder.append(_string_3, "");
			}
			if (_hasElements) {
				_builder.append(")", "");
			}
		}
		sb.append(_builder);
		sb.append(";");
		final String _leaving = this.leaving(hii, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLArgument arg, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		final String _entering = this.entering(arg, highlight);
		sb.append(_entering);
		final String _name = arg.getName();
		final String _param = highlight.param(_name);
		final StringBuilder _append = sb.append(_param);
		final StringBuilder _append_1 = _append.append("=");
		final HDLExpression _expression = arg.getExpression();
		final String _string = this.toString(_expression, highlight);
		_append_1.append(_string);
		final String _leaving = this.leaving(arg, highlight);
		sb.append(_leaving);
		return sb.toString();
	}

	protected String _toString(final HDLRange range, final SyntaxHighlighter highlight) {
		final HDLExpression _from = range.getFrom();
		final boolean _tripleNotEquals = (_from != null);
		if (_tripleNotEquals) {
			final String _entering = this.entering(range, highlight);
			final HDLExpression _from_1 = range.getFrom();
			final String _string = this.toString(_from_1, highlight);
			final String _plus = (_entering + _string);
			final String _plus_1 = (_plus + ":");
			final HDLExpression _to = range.getTo();
			final String _string_1 = this.toString(_to, highlight);
			final String _plus_2 = (_plus_1 + _string_1);
			final String _leaving = this.leaving(range, highlight);
			return (_plus_2 + _leaving);
		}
		final HDLExpression _inc = range.getInc();
		final boolean _tripleNotEquals_1 = (_inc != null);
		if (_tripleNotEquals_1) {
			final String _entering_1 = this.entering(range, highlight);
			final HDLExpression _to_1 = range.getTo();
			final String _string_2 = this.toString(_to_1, highlight);
			final String _plus_3 = (_entering_1 + _string_2);
			final String _plus_4 = (_plus_3 + " +: ");
			final HDLExpression _inc_1 = range.getInc();
			final String _string_3 = this.toString(_inc_1, highlight);
			final String _plus_5 = (_plus_4 + _string_3);
			final String _leaving_1 = this.leaving(range, highlight);
			return (_plus_5 + _leaving_1);
		}
		final HDLExpression _dec = range.getDec();
		final boolean _tripleNotEquals_2 = (_dec != null);
		if (_tripleNotEquals_2) {
			final String _entering_2 = this.entering(range, highlight);
			final HDLExpression _to_2 = range.getTo();
			final String _string_4 = this.toString(_to_2, highlight);
			final String _plus_6 = (_entering_2 + _string_4);
			final String _plus_7 = (_plus_6 + " -: ");
			final HDLExpression _dec_1 = range.getDec();
			final String _string_5 = this.toString(_dec_1, highlight);
			final String _plus_8 = (_plus_7 + _string_5);
			final String _leaving_2 = this.leaving(range, highlight);
			return (_plus_8 + _leaving_2);
		}
		final String _entering_3 = this.entering(range, highlight);
		final HDLExpression _to_3 = range.getTo();
		final String _string_6 = this.toString(_to_3, highlight);
		final String _plus_9 = (_entering_3 + _string_6);
		final String _leaving_3 = this.leaving(range, highlight);
		return (_plus_9 + _leaving_3);
	}

	protected String _toString(final HDLVariable hVar, final SyntaxHighlighter highlight) {
		final StringBuilder sb = new StringBuilder();
		this.entering(hVar, highlight);
		final ArrayList<HDLAnnotation> _annotations = hVar.getAnnotations();
		for (final HDLAnnotation anno : _annotations) {
			final String _string = this.toString(anno, highlight);
			final StringBuilder _append = sb.append(_string);
			final String _simpleSpace = highlight.simpleSpace();
			_append.append(_simpleSpace);
		}
		final String _varName = highlight.varName(hVar);
		sb.append(_varName);
		final ArrayList<HDLExpression> _dimensions = hVar.getDimensions();
		for (final HDLExpression arr : _dimensions) {
			final StringBuilder _append_1 = sb.append("[");
			final String _string_1 = this.toString(arr, highlight);
			final StringBuilder _append_2 = _append_1.append(_string_1);
			_append_2.append("]");
		}
		final HDLExpression _defaultValue = hVar.getDefaultValue();
		final boolean _tripleNotEquals = (_defaultValue != null);
		if (_tripleNotEquals) {
			final StringBuilder _append_3 = sb.append("=");
			final HDLExpression _defaultValue_1 = hVar.getDefaultValue();
			final String _string_2 = this.toString(_defaultValue_1, highlight);
			_append_3.append(_string_2);
		}
		this.leaving(hVar, highlight);
		return sb.toString();
	}

	protected String _toString(final HDLDirectGeneration hdg, final SyntaxHighlighter highlight) {
		final StringBuilder sb = highlight.getSpacing();
		this.entering(hdg, highlight);
		final ArrayList<HDLAnnotation> _annotations = hdg.getAnnotations();
		for (final HDLAnnotation anno : _annotations) {
			final String _string = this.toString(anno, highlight);
			final StringBuilder _append = sb.append(_string);
			final String _simpleSpace = highlight.simpleSpace();
			_append.append(_simpleSpace);
		}
		final Boolean _include = hdg.getInclude();
		if ((_include).booleanValue()) {
			final StringBuilder _append_1 = sb.append("include");
			final String _simpleSpace_1 = highlight.simpleSpace();
			_append_1.append(_simpleSpace_1);
		}
		final String _ifName = hdg.getIfName();
		final String _interfaceName = highlight.interfaceName(_ifName);
		final StringBuilder _append_2 = sb.append(_interfaceName);
		final String _simpleSpace_2 = highlight.simpleSpace();
		final StringBuilder _append_3 = _append_2.append(_simpleSpace_2);
		final HDLVariable _var = hdg.getVar();
		final String _varName = highlight.varName(_var);
		final StringBuilder _append_4 = _append_3.append(_varName);
		_append_4.append("=");
		final String _simpleSpace_3 = highlight.simpleSpace();
		final StringBuilder _append_5 = sb.append(_simpleSpace_3);
		final String _keyword = highlight.keyword("generate");
		final StringBuilder _append_6 = _append_5.append(_keyword);
		final String _simpleSpace_4 = highlight.simpleSpace();
		final StringBuilder _append_7 = _append_6.append(_simpleSpace_4);
		final String _generatorID = hdg.getGeneratorID();
		final String _generatorID_1 = highlight.generatorID(_generatorID);
		_append_7.append(_generatorID_1);
		sb.append("(");
		final ArrayList<HDLArgument> _arguments = hdg.getArguments();
		for (final HDLArgument args : _arguments) {
			final String _string_1 = this.toString(args, highlight);
			sb.append(_string_1);
		}
		sb.append(")");
		final String _generatorContent = hdg.getGeneratorContent();
		final boolean _tripleNotEquals = (_generatorContent != null);
		if (_tripleNotEquals) {
			final String _generatorID_2 = hdg.getGeneratorID();
			final String _generatorContent_1 = hdg.getGeneratorContent();
			final String _generatorContent_2 = highlight.generatorContent(_generatorID_2, _generatorContent_1);
			sb.append(_generatorContent_2);
		}
		sb.append(";");
		this.leaving(hdg, highlight);
		return sb.toString();
	}

	public String toString(final IHDLObject ref, final SyntaxHighlighter highlight) {
		if (ref instanceof HDLInterfaceRef)
			return _toString((HDLInterfaceRef) ref, highlight);
		else if (ref instanceof HDLEnum)
			return _toString((HDLEnum) ref, highlight);
		else if (ref instanceof HDLEnumRef)
			return _toString((HDLEnumRef) ref, highlight);
		else if (ref instanceof HDLInlineFunction)
			return _toString((HDLInlineFunction) ref, highlight);
		else if (ref instanceof HDLNativeFunction)
			return _toString((HDLNativeFunction) ref, highlight);
		else if (ref instanceof HDLPrimitive)
			return _toString((HDLPrimitive) ref, highlight);
		else if (ref instanceof HDLSubstituteFunction)
			return _toString((HDLSubstituteFunction) ref, highlight);
		else if (ref instanceof HDLUnresolvedFragmentFunction)
			return _toString((HDLUnresolvedFragmentFunction) ref, highlight);
		else if (ref instanceof HDLVariableRef)
			return _toString((HDLVariableRef) ref, highlight);
		else if (ref instanceof HDLBitOp)
			return _toString((HDLBitOp) ref, highlight);
		else if (ref instanceof HDLBlock)
			return _toString((HDLBlock) ref, highlight);
		else if (ref instanceof HDLDirectGeneration)
			return _toString((HDLDirectGeneration) ref, highlight);
		else if (ref instanceof HDLEnumDeclaration)
			return _toString((HDLEnumDeclaration) ref, highlight);
		else if (ref instanceof HDLEqualityOp)
			return _toString((HDLEqualityOp) ref, highlight);
		else if (ref instanceof HDLForLoop)
			return _toString((HDLForLoop) ref, highlight);
		else if (ref instanceof HDLIfStatement)
			return _toString((HDLIfStatement) ref, highlight);
		else if (ref instanceof HDLInterface)
			return _toString((HDLInterface) ref, highlight);
		else if (ref instanceof HDLInterfaceDeclaration)
			return _toString((HDLInterfaceDeclaration) ref, highlight);
		else if (ref instanceof HDLInterfaceInstantiation)
			return _toString((HDLInterfaceInstantiation) ref, highlight);
		else if (ref instanceof HDLSwitchCaseStatement)
			return _toString((HDLSwitchCaseStatement) ref, highlight);
		else if (ref instanceof HDLSwitchStatement)
			return _toString((HDLSwitchStatement) ref, highlight);
		else if (ref instanceof HDLUnresolvedFragment)
			return _toString((HDLUnresolvedFragment) ref, highlight);
		else if (ref instanceof HDLVariableDeclaration)
			return _toString((HDLVariableDeclaration) ref, highlight);
		else if (ref instanceof HDLAnnotation)
			return _toString((HDLAnnotation) ref, highlight);
		else if (ref instanceof HDLArgument)
			return _toString((HDLArgument) ref, highlight);
		else if (ref instanceof HDLArrayInit)
			return _toString((HDLArrayInit) ref, highlight);
		else if (ref instanceof HDLAssignment)
			return _toString((HDLAssignment) ref, highlight);
		else if (ref instanceof HDLConcat)
			return _toString((HDLConcat) ref, highlight);
		else if (ref instanceof HDLExport)
			return _toString((HDLExport) ref, highlight);
		else if (ref instanceof HDLFunctionCall)
			return _toString((HDLFunctionCall) ref, highlight);
		else if (ref instanceof HDLFunctionParameter)
			return _toString((HDLFunctionParameter) ref, highlight);
		else if (ref instanceof HDLLiteral)
			return _toString((HDLLiteral) ref, highlight);
		else if (ref instanceof HDLManip)
			return _toString((HDLManip) ref, highlight);
		else if (ref instanceof HDLOpExpression)
			return _toString((HDLOpExpression) ref, highlight);
		else if (ref instanceof HDLPackage)
			return _toString((HDLPackage) ref, highlight);
		else if (ref instanceof HDLRange)
			return _toString((HDLRange) ref, highlight);
		else if (ref instanceof HDLRegisterConfig)
			return _toString((HDLRegisterConfig) ref, highlight);
		else if (ref instanceof HDLTernary)
			return _toString((HDLTernary) ref, highlight);
		else if (ref instanceof HDLUnit)
			return _toString((HDLUnit) ref, highlight);
		else if (ref instanceof HDLVariable)
			return _toString((HDLVariable) ref, highlight);
		else if (ref instanceof HDLExpression)
			return _toString((HDLExpression) ref, highlight);
		else if (ref instanceof HDLStatement)
			return _toString((HDLStatement) ref, highlight);
		else if (ref != null)
			return _toString(ref, highlight);
		else
			throw new IllegalArgumentException("Unhandled parameter types: " + Arrays.<Object> asList(ref, highlight).toString());
	}
}

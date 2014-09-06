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
package org.pshdl.model.evaluation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pshdl.interpreter.frames.BigIntegerFrame;
import org.pshdl.model.HDLArithOp;
import org.pshdl.model.HDLArrayInit;
import org.pshdl.model.HDLBitOp;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLConcat;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLEqualityOp;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLShiftOp;
import org.pshdl.model.HDLTernary;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.extensions.FullNameExtension;
import org.pshdl.model.extensions.ProblemDescription;
import org.pshdl.model.extensions.TypeExtension;
import org.pshdl.model.types.builtIn.HDLFunctions;
import org.pshdl.model.types.builtIn.HDLPrimitives;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.Insulin;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;

/**
 * This class allows to attempt to resolve a {@link java.math.BigInteger} value
 * for any {@link org.pshdl.model.IHDLObject}. Of course this only works when
 * the given IHDLObject is truly constant. Parameters are not considered
 * constant, unless they can be found in the given
 * {@link org.pshdl.model.evaluation.HDLEvaluationContext}.
 * 
 * @author Karsten Becker
 */
@SuppressWarnings("all")
public class ConstantEvaluate {
	private static ConstantEvaluate INST = new ConstantEvaluate();

	/**
	 * Attempts to determine a constant that the given Expression can be
	 * replaced with. This method does not use parameters as their value depends
	 * on the context.
	 * 
	 * @return an absent {@link Optional} if not successful check the SOURCE and
	 *         {@link ProblemDescription#DESCRIPTION} Meta annotations
	 */
	public static Optional<BigInteger> valueOf(final HDLExpression exp) {
		final HashSet<HDLQualifiedName> _newHashSet = Sets.<HDLQualifiedName> newHashSet();
		return ConstantEvaluate.INST.constantEvaluate(exp, null, _newHashSet);
	}

	/**
	 * Attempts to determine a constant that the given Expression can be
	 * replaced with. If parameter are encountered, the provided context is used
	 * to retrieve a value for them.
	 * 
	 * @return an absent {@link Optional} if not successful check the SOURCE and
	 *         {@link ProblemDescription.DESCRIPTION} Meta annotations
	 */
	public static Optional<BigInteger> valueOf(final HDLExpression exp, final HDLEvaluationContext context) {
		final boolean _tripleEquals = (exp == null);
		if (_tripleEquals)
			return Optional.<BigInteger> absent();
		final HashSet<HDLQualifiedName> _newHashSet = Sets.<HDLQualifiedName> newHashSet();
		return ConstantEvaluate.INST.constantEvaluate(exp, context, _newHashSet);
	}

	protected Optional<BigInteger> _constantEvaluate(final HDLUnresolvedFragment obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		final Optional<Insulin.ResolvedPart> type = Insulin.resolveFragment(obj);
		final boolean _isPresent = type.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return Optional.<BigInteger> absent();
		final Insulin.ResolvedPart _get = type.get();
		final IHDLObject _container = obj.getContainer();
		final IHDLObject _copyDeepFrozen = _get.obj.copyDeepFrozen(_container);
		return this.constantEvaluate(_copyDeepFrozen, context, evaled);
	}

	protected Optional<BigInteger> _constantEvaluate(final HDLArrayInit obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		return Optional.<BigInteger> absent();
	}

	protected Optional<BigInteger> _constantEvaluate(final IHDLObject obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		final HDLClass _classType = obj.getClassType();
		final String _plus = ("Did not implement constantEvaulate for type:" + _classType);
		throw new IllegalArgumentException(_plus);
	}

	protected Optional<BigInteger> _constantEvaluate(final HDLTernary obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		final HDLExpression _ifExpr = obj.getIfExpr();
		final Optional<BigInteger> res = this.constantEvaluate(_ifExpr, context, evaled);
		final boolean _isPresent = res.isPresent();
		if (_isPresent) {
			final BigInteger _get = res.get();
			final boolean _equals = BigInteger.ZERO.equals(_get);
			if (_equals) {
				final HDLExpression _elseExpr = obj.getElseExpr();
				return this.constantEvaluate(_elseExpr, context, evaled);
			}
			final HDLExpression _thenExpr = obj.getThenExpr();
			return this.constantEvaluate(_thenExpr, context, evaled);
		}
		final HDLExpression _ifExpr_1 = obj.getIfExpr();
		obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, _ifExpr_1);
		obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE);
		return Optional.<BigInteger> absent();
	}

	protected Optional<BigInteger> _constantEvaluate(final HDLLiteral obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		final HDLLiteral.HDLLiteralPresentation _presentation = obj.getPresentation();
		if (_presentation != null) {
			switch (_presentation) {
			case STR:
				return Optional.<BigInteger> absent();
			case BOOL:
				boolean _and = false;
				final boolean _tripleNotEquals = (context != null);
				if (!_tripleNotEquals) {
					_and = false;
				} else {
					_and = context.boolAsInt;
				}
				if (_and) {
					final HDLLiteral _false = HDLLiteral.getFalse();
					final boolean _equals = obj.equals(_false);
					final boolean _not = (!_equals);
					return ConstantEvaluate.boolInt(_not);
				}
				return Optional.<BigInteger> absent();
			default:
				break;
			}
		}
		final BigInteger _valueAsBigInt = obj.getValueAsBigInt();
		return Optional.<BigInteger> of(_valueAsBigInt);
	}

	protected Optional<BigInteger> _constantEvaluate(final HDLManip obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		final HDLExpression _target = obj.getTarget();
		final Optional<BigInteger> eval = this.subEvaluate(obj, _target, context, evaled);
		final boolean _isPresent = eval.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return Optional.<BigInteger> absent();
		final HDLManip.HDLManipType _type = obj.getType();
		if (_type != null) {
			switch (_type) {
			case ARITH_NEG:
				final BigInteger _get = eval.get();
				final BigInteger _negate = _get.negate();
				return Optional.<BigInteger> of(_negate);
			case BIT_NEG:
				final BigInteger _get_1 = eval.get();
				final BigInteger _not_1 = _get_1.not();
				return Optional.<BigInteger> of(_not_1);
			case LOGIC_NEG:
				final HDLExpression _target_1 = obj.getTarget();
				final Optional<BigInteger> const_ = this.constantEvaluate(_target_1, context, evaled);
				final boolean _isPresent_1 = const_.isPresent();
				if (_isPresent_1) {
					final BigInteger _get_2 = const_.get();
					final boolean _equals = _get_2.equals(BigInteger.ZERO);
					return ConstantEvaluate.boolInt(_equals);
				}
				return Optional.<BigInteger> absent();
			case CAST:
				final HDLType type = obj.getCastTo();
				if ((type instanceof HDLPrimitive)) {
					final HDLPrimitive prim = ((HDLPrimitive) type);
					final HDLExpression _width = prim.getWidth();
					final boolean _tripleNotEquals = (_width != null);
					if (_tripleNotEquals) {
						final HDLExpression _width_1 = prim.getWidth();
						final Optional<BigInteger> width = this.constantEvaluate(_width_1, context, evaled);
						final boolean _isPresent_2 = width.isPresent();
						if (_isPresent_2) {
							final BigInteger _get_3 = eval.get();
							final BigInteger _get_4 = width.get();
							final int _intValue = _get_4.intValue();
							final BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_intValue);
							final BigInteger _mod = _get_3.mod(_shiftLeft);
							return Optional.<BigInteger> of(_mod);
						}
						return Optional.<BigInteger> absent();
					}
					return eval;
				}
				final HDLExpression _target_2 = obj.getTarget();
				obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, _target_2);
				obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITVE_TYPE_NOT_EVALUATED);
				return Optional.<BigInteger> absent();
			default:
				break;
			}
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!");
	}

	protected Optional<BigInteger> _constantEvaluate(final HDLConcat obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		BigInteger sum = BigInteger.ZERO;
		final ArrayList<HDLExpression> _cats = obj.getCats();
		for (final HDLExpression cat : _cats) {
			{
				final Optional<BigInteger> im = this.subEvaluate(obj, cat, context, evaled);
				final boolean _isPresent = im.isPresent();
				final boolean _not = (!_isPresent);
				if (_not)
					return Optional.<BigInteger> absent();
				final Optional<? extends HDLType> type = TypeExtension.typeOf(cat);
				final boolean _isPresent_1 = type.isPresent();
				final boolean _not_1 = (!_isPresent_1);
				if (_not_1) {
					obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, cat);
					obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE);
					return Optional.<BigInteger> absent();
				}
				final HDLType _get = type.get();
				final HDLExpression _width = _get.getWidth();
				final Optional<BigInteger> width = this.constantEvaluate(_width, context, evaled);
				final boolean _isPresent_2 = width.isPresent();
				final boolean _not_2 = (!_isPresent_2);
				if (_not_2) {
					final HDLType _get_1 = type.get();
					final HDLExpression _width_1 = _get_1.getWidth();
					obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, _width_1);
					obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE);
					return Optional.<BigInteger> absent();
				}
				final BigInteger _get_2 = width.get();
				final int _intValue = _get_2.intValue();
				final BigInteger _shiftLeft = sum.shiftLeft(_intValue);
				final BigInteger _get_3 = im.get();
				final BigInteger _or = _shiftLeft.or(_get_3);
				sum = _or;
			}
		}
		return Optional.<BigInteger> of(sum);
	}

	public Optional<BigInteger> subEvaluate(final HDLExpression container, final HDLExpression left, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		final boolean _tripleEquals = (left == null);
		if (_tripleEquals)
			throw new IllegalArgumentException((("Container:" + container) + " has null left expression"));
		final Optional<BigInteger> leftVal = this.constantEvaluate(left, context, evaled);
		final boolean _isPresent = leftVal.isPresent();
		final boolean _not = (!_isPresent);
		if (_not) {
			container.<IHDLObject> addMeta(ProblemDescription.SOURCE, left);
			container.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE);
			return Optional.<BigInteger> absent();
		}
		return leftVal;
	}

	protected Optional<BigInteger> _constantEvaluate(final HDLArithOp obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		final HDLExpression _left = obj.getLeft();
		final Optional<BigInteger> leftVal = this.subEvaluate(obj, _left, context, evaled);
		final boolean _isPresent = leftVal.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return Optional.<BigInteger> absent();
		final HDLExpression _right = obj.getRight();
		final Optional<BigInteger> rightVal = this.subEvaluate(obj, _right, context, evaled);
		final boolean _isPresent_1 = rightVal.isPresent();
		final boolean _not_1 = (!_isPresent_1);
		if (_not_1)
			return Optional.<BigInteger> absent();
		final HDLArithOp.HDLArithOpType _type = obj.getType();
		if (_type != null) {
			switch (_type) {
			case DIV:
				final BigInteger _get = leftVal.get();
				final BigInteger _get_1 = rightVal.get();
				final BigInteger _divide = _get.divide(_get_1);
				return Optional.<BigInteger> of(_divide);
			case MUL:
				final BigInteger _get_2 = leftVal.get();
				final BigInteger _get_3 = rightVal.get();
				final BigInteger _multiply = _get_2.multiply(_get_3);
				return Optional.<BigInteger> of(_multiply);
			case MINUS:
				final BigInteger _get_4 = leftVal.get();
				final BigInteger _get_5 = rightVal.get();
				final BigInteger _subtract = _get_4.subtract(_get_5);
				return Optional.<BigInteger> of(_subtract);
			case PLUS:
				final BigInteger _get_6 = leftVal.get();
				final BigInteger _get_7 = rightVal.get();
				final BigInteger _add = _get_6.add(_get_7);
				return Optional.<BigInteger> of(_add);
			case MOD:
				final BigInteger _get_8 = leftVal.get();
				final BigInteger _get_9 = rightVal.get();
				final BigInteger _remainder = _get_8.remainder(_get_9);
				return Optional.<BigInteger> of(_remainder);
			case POW:
				final BigInteger _get_10 = leftVal.get();
				final BigInteger _get_11 = rightVal.get();
				final int _intValue = _get_11.intValue();
				final BigInteger _pow = _get_10.pow(_intValue);
				return Optional.<BigInteger> of(_pow);
			default:
				break;
			}
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!");
	}

	protected Optional<BigInteger> _constantEvaluate(final HDLBitOp obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		final HDLExpression _left = obj.getLeft();
		final Optional<BigInteger> leftVal = this.subEvaluate(obj, _left, context, evaled);
		final boolean _isPresent = leftVal.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return Optional.<BigInteger> absent();
		final HDLExpression _right = obj.getRight();
		final Optional<BigInteger> rightVal = this.subEvaluate(obj, _right, context, evaled);
		final boolean _isPresent_1 = rightVal.isPresent();
		final boolean _not_1 = (!_isPresent_1);
		if (_not_1)
			return Optional.<BigInteger> absent();
		final HDLBitOp.HDLBitOpType _type = obj.getType();
		if (_type != null) {
			switch (_type) {
			case AND:
				final BigInteger _get = leftVal.get();
				final BigInteger _get_1 = rightVal.get();
				final BigInteger _and = _get.and(_get_1);
				return Optional.<BigInteger> of(_and);
			case OR:
				final BigInteger _get_2 = leftVal.get();
				final BigInteger _get_3 = rightVal.get();
				final BigInteger _or = _get_2.or(_get_3);
				return Optional.<BigInteger> of(_or);
			case XOR:
				final BigInteger _get_4 = leftVal.get();
				final BigInteger _get_5 = rightVal.get();
				final BigInteger _xor = _get_4.xor(_get_5);
				return Optional.<BigInteger> of(_xor);
			case LOGI_AND:
				final BigInteger _get_6 = leftVal.get();
				final boolean _equals = BigInteger.ZERO.equals(_get_6);
				final boolean l = (!_equals);
				final BigInteger _get_7 = rightVal.get();
				final boolean _equals_1 = BigInteger.ZERO.equals(_get_7);
				final boolean r = (!_equals_1);
				return ConstantEvaluate.boolInt((l && r));
			case LOGI_OR:
				final BigInteger _get_8 = leftVal.get();
				final boolean _equals_2 = BigInteger.ZERO.equals(_get_8);
				final boolean l_1 = (!_equals_2);
				final BigInteger _get_9 = rightVal.get();
				final boolean _equals_3 = BigInteger.ZERO.equals(_get_9);
				final boolean r_1 = (!_equals_3);
				return ConstantEvaluate.boolInt((l_1 || r_1));
			default:
				break;
			}
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!");
	}

	protected Optional<BigInteger> _constantEvaluate(final HDLEqualityOp obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		final HDLExpression _left = obj.getLeft();
		final Optional<BigInteger> leftVal = this.subEvaluate(obj, _left, context, evaled);
		final boolean _isPresent = leftVal.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return Optional.<BigInteger> absent();
		final HDLExpression _right = obj.getRight();
		final Optional<BigInteger> rightVal = this.subEvaluate(obj, _right, context, evaled);
		final boolean _isPresent_1 = rightVal.isPresent();
		final boolean _not_1 = (!_isPresent_1);
		if (_not_1)
			return Optional.<BigInteger> absent();
		final HDLEqualityOp.HDLEqualityOpType _type = obj.getType();
		if (_type != null) {
			switch (_type) {
			case EQ:
				final BigInteger _get = leftVal.get();
				final BigInteger _get_1 = rightVal.get();
				final boolean _equals = _get.equals(_get_1);
				return ConstantEvaluate.boolInt(_equals);
			case NOT_EQ:
				final BigInteger _get_2 = leftVal.get();
				final BigInteger _get_3 = rightVal.get();
				final boolean _equals_1 = _get_2.equals(_get_3);
				final boolean _not_2 = (!_equals_1);
				return ConstantEvaluate.boolInt(_not_2);
			case GREATER:
				final BigInteger _get_4 = leftVal.get();
				final BigInteger _get_5 = rightVal.get();
				final int _compareTo = _get_4.compareTo(_get_5);
				final boolean _greaterThan = (_compareTo > 0);
				return ConstantEvaluate.boolInt(_greaterThan);
			case GREATER_EQ:
				final BigInteger _get_6 = leftVal.get();
				final BigInteger _get_7 = rightVal.get();
				final int _compareTo_1 = _get_6.compareTo(_get_7);
				final boolean _greaterEqualsThan = (_compareTo_1 >= 0);
				return ConstantEvaluate.boolInt(_greaterEqualsThan);
			case LESS:
				final BigInteger _get_8 = leftVal.get();
				final BigInteger _get_9 = rightVal.get();
				final int _compareTo_2 = _get_8.compareTo(_get_9);
				final boolean _lessThan = (_compareTo_2 < 0);
				return ConstantEvaluate.boolInt(_lessThan);
			case LESS_EQ:
				final BigInteger _get_10 = leftVal.get();
				final BigInteger _get_11 = rightVal.get();
				final int _compareTo_3 = _get_10.compareTo(_get_11);
				final boolean _lessEqualsThan = (_compareTo_3 <= 0);
				return ConstantEvaluate.boolInt(_lessEqualsThan);
			default:
				break;
			}
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!");
	}

	protected Optional<BigInteger> _constantEvaluate(final HDLShiftOp obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		final HDLExpression _left = obj.getLeft();
		final Optional<BigInteger> leftVal = this.subEvaluate(obj, _left, context, evaled);
		final boolean _isPresent = leftVal.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return Optional.<BigInteger> absent();
		final HDLExpression _right = obj.getRight();
		final Optional<BigInteger> rightVal = this.subEvaluate(obj, _right, context, evaled);
		final boolean _isPresent_1 = rightVal.isPresent();
		final boolean _not_1 = (!_isPresent_1);
		if (_not_1)
			return Optional.<BigInteger> absent();
		final HDLShiftOp.HDLShiftOpType _type = obj.getType();
		if (_type != null) {
			switch (_type) {
			case SLL:
				final BigInteger _get = leftVal.get();
				final BigInteger _get_1 = rightVal.get();
				final int _intValue = _get_1.intValue();
				final BigInteger _shiftLeft = _get.shiftLeft(_intValue);
				return Optional.<BigInteger> of(_shiftLeft);
			case SRA:
				final BigInteger _get_2 = leftVal.get();
				final BigInteger _get_3 = rightVal.get();
				final int _intValue_1 = _get_3.intValue();
				final BigInteger _shiftRight = _get_2.shiftRight(_intValue_1);
				return Optional.<BigInteger> of(_shiftRight);
			case SRL:
				final BigInteger l = leftVal.get();
				final int _signum = l.signum();
				final boolean _lessThan = (_signum < 0);
				if (_lessThan) {
					final HDLExpression _left_1 = obj.getLeft();
					final Optional<? extends HDLType> t = TypeExtension.typeOf(_left_1);
					final boolean _isPresent_2 = t.isPresent();
					if (_isPresent_2) {
						final HDLType _get_4 = t.get();
						final Integer width = HDLPrimitives.getWidth(_get_4, context);
						final boolean _tripleNotEquals = (width != null);
						if (_tripleNotEquals) {
							final BigInteger _get_5 = rightVal.get();
							final int shiftWidth = _get_5.intValue();
							final BigInteger res = BigIntegerFrame.srl(l, (width).intValue(), shiftWidth);
							return Optional.<BigInteger> of(res);
						}
					}
					return Optional.<BigInteger> absent();
				}
				final BigInteger _get_6 = rightVal.get();
				final int _intValue_2 = _get_6.intValue();
				final BigInteger _shiftRight_1 = l.shiftRight(_intValue_2);
				return Optional.<BigInteger> of(_shiftRight_1);
			default:
				break;
			}
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!");
	}

	protected Optional<BigInteger> _constantEvaluate(final HDLFunctionCall obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		return HDLFunctions.constantEvaluate(obj, context);
	}

	protected Optional<BigInteger> _constantEvaluate(final HDLVariableRef obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		final ArrayList<HDLExpression> _array = obj.getArray();
		final int _size = _array.size();
		final boolean _notEquals = (_size != 0);
		if (_notEquals) {
			final Optional<HDLVariable> hVarOpt = obj.resolveVar();
			final boolean _isPresent = hVarOpt.isPresent();
			if (_isPresent) {
				final HDLVariable hVar = hVarOpt.get();
				final HDLVariableDeclaration.HDLDirection _direction = hVar.getDirection();
				final boolean _equals = Objects.equal(_direction, HDLVariableDeclaration.HDLDirection.CONSTANT);
				if (_equals) {
					final HDLExpression defVal = hVar.getDefaultValue();
					final ArrayList<HDLExpression> _array_1 = obj.getArray();
					return this.arrayDefValue(defVal, _array_1, context, evaled);
				}
			}
			obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
			obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ARRAY_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
			return Optional.<BigInteger> absent();
		}
		final ArrayList<HDLRange> _bits = obj.getBits();
		final int _size_1 = _bits.size();
		final boolean _notEquals_1 = (_size_1 != 0);
		if (_notEquals_1) {
			obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
			obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
			return Optional.<BigInteger> absent();
		}
		final Optional<? extends HDLType> type = TypeExtension.typeOf(obj);
		boolean _or = false;
		final boolean _isPresent_1 = type.isPresent();
		final boolean _not = (!_isPresent_1);
		if (_not) {
			_or = true;
		} else {
			_or = (!(type.get() instanceof HDLPrimitive));
		}
		if (_or) {
			obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
			obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.TYPE_NOT_SUPPORTED_FOR_CONSTANTS);
			return Optional.<BigInteger> absent();
		}
		final Optional<HDLVariable> hVar_1 = obj.resolveVar();
		final boolean _isPresent_2 = hVar_1.isPresent();
		final boolean _not_1 = (!_isPresent_2);
		if (_not_1) {
			obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
			obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.VARIABLE_NOT_RESOLVED);
			return Optional.<BigInteger> absent();
		}
		final HDLVariable _get = hVar_1.get();
		final HDLQualifiedName fqn = FullNameExtension.fullNameOf(_get);
		final boolean _contains = evaled.contains(fqn);
		if (_contains) {
			final HDLVariable _get_1 = hVar_1.get();
			obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, _get_1);
			obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.CONSTANT_EVAL_LOOP);
			return Optional.<BigInteger> absent();
		}
		evaled.add(fqn);
		final HDLVariable _get_2 = hVar_1.get();
		final HDLVariableDeclaration.HDLDirection dir = _get_2.getDirection();
		final boolean _equals_1 = Objects.equal(dir, HDLVariableDeclaration.HDLDirection.CONSTANT);
		if (_equals_1) {
			final HDLVariable _get_3 = hVar_1.get();
			final HDLExpression _defaultValue = _get_3.getDefaultValue();
			final Optional<BigInteger> subEval = this.subEvaluate(obj, _defaultValue, context, evaled);
			final boolean _isPresent_3 = subEval.isPresent();
			if (_isPresent_3) {
				evaled.remove(fqn);
			}
			return subEval;
		}
		final boolean _equals_2 = Objects.equal(dir, HDLVariableDeclaration.HDLDirection.PARAMETER);
		if (_equals_2) {
			final boolean _tripleEquals = (context == null);
			if (_tripleEquals) {
				obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
				obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.CAN_NOT_USE_PARAMETER);
				return Optional.<BigInteger> absent();
			}
			final HDLVariable _get_4 = hVar_1.get();
			final HDLExpression cRef = context.get(_get_4);
			final boolean _tripleEquals_1 = (cRef == null);
			if (_tripleEquals_1) {
				final HDLVariable _get_5 = hVar_1.get();
				obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, _get_5);
				obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT);
				return Optional.<BigInteger> absent();
			}
			final Optional<BigInteger> cRefEval = this.constantEvaluate(cRef, context, evaled);
			final boolean _isPresent_4 = cRefEval.isPresent();
			final boolean _not_2 = (!_isPresent_4);
			if (_not_2) {
				obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, cRef);
				obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT);
				return Optional.<BigInteger> absent();
			}
			final boolean _isPresent_5 = cRefEval.isPresent();
			if (_isPresent_5) {
				evaled.remove(fqn);
			}
			return cRefEval;
		}
		obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
		obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
		return Optional.<BigInteger> absent();
	}

	public Optional<BigInteger> arrayDefValue(final HDLExpression expression, final List<HDLExpression> expressions, final HDLEvaluationContext context,
			final Set<HDLQualifiedName> evaled) {
		if ((expression instanceof HDLArrayInit)) {
			final boolean _isEmpty = expressions.isEmpty();
			if (_isEmpty)
				return Optional.<BigInteger> absent();
			final HDLExpression _get = expressions.get(0);
			final Optional<BigInteger> idx = ConstantEvaluate.valueOf(_get, context);
			final boolean _isPresent = idx.isPresent();
			final boolean _not = (!_isPresent);
			if (_not)
				return Optional.<BigInteger> absent();
			final HDLArrayInit arr = ((HDLArrayInit) expression);
			final BigInteger _get_1 = idx.get();
			final int idxValue = _get_1.intValue();
			final ArrayList<HDLExpression> _exp = arr.getExp();
			final int _size = _exp.size();
			final boolean _lessThan = (_size < idxValue);
			if (_lessThan)
				return Optional.<BigInteger> of(BigInteger.ZERO);
			final ArrayList<HDLExpression> _exp_1 = arr.getExp();
			final HDLExpression _get_2 = _exp_1.get(idxValue);
			final int _size_1 = expressions.size();
			final List<HDLExpression> _subList = expressions.subList(1, _size_1);
			return this.arrayDefValue(_get_2, _subList, context, evaled);
		}
		return this.constantEvaluate(expression, context, evaled);
	}

	protected Optional<BigInteger> _constantEvaluate(final HDLEnumRef obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		boolean _and = false;
		final boolean _tripleNotEquals = (context != null);
		if (!_tripleNotEquals) {
			_and = false;
		} else {
			_and = context.enumAsInt;
		}
		if (_and) {
			final Optional<HDLEnum> _resolveHEnum = obj.resolveHEnum();
			final HDLEnum hEnum = _resolveHEnum.get();
			final Optional<HDLVariable> _resolveVar = obj.resolveVar();
			final HDLVariable hVar = _resolveVar.get();
			final ArrayList<HDLVariable> _enums = hEnum.getEnums();
			final int _indexOf = _enums.indexOf(hVar);
			final BigInteger _valueOf = BigInteger.valueOf(_indexOf);
			return Optional.<BigInteger> of(_valueOf);
		}
		obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
		obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ENUMS_NOT_SUPPORTED_FOR_CONSTANTS);
		return Optional.<BigInteger> absent();
	}

	public static Optional<BigInteger> boolInt(final boolean b) {
		Optional<BigInteger> _xifexpression = null;
		if (b) {
			_xifexpression = Optional.<BigInteger> of(BigInteger.ONE);
		} else {
			_xifexpression = Optional.<BigInteger> of(BigInteger.ZERO);
		}
		return _xifexpression;
	}

	public Optional<BigInteger> constantEvaluate(final IHDLObject obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
		if (obj instanceof HDLEnumRef)
			return _constantEvaluate((HDLEnumRef) obj, context, evaled);
		else if (obj instanceof HDLVariableRef)
			return _constantEvaluate((HDLVariableRef) obj, context, evaled);
		else if (obj instanceof HDLArithOp)
			return _constantEvaluate((HDLArithOp) obj, context, evaled);
		else if (obj instanceof HDLBitOp)
			return _constantEvaluate((HDLBitOp) obj, context, evaled);
		else if (obj instanceof HDLEqualityOp)
			return _constantEvaluate((HDLEqualityOp) obj, context, evaled);
		else if (obj instanceof HDLShiftOp)
			return _constantEvaluate((HDLShiftOp) obj, context, evaled);
		else if (obj instanceof HDLUnresolvedFragment)
			return _constantEvaluate((HDLUnresolvedFragment) obj, context, evaled);
		else if (obj instanceof HDLArrayInit)
			return _constantEvaluate((HDLArrayInit) obj, context, evaled);
		else if (obj instanceof HDLConcat)
			return _constantEvaluate((HDLConcat) obj, context, evaled);
		else if (obj instanceof HDLFunctionCall)
			return _constantEvaluate((HDLFunctionCall) obj, context, evaled);
		else if (obj instanceof HDLLiteral)
			return _constantEvaluate((HDLLiteral) obj, context, evaled);
		else if (obj instanceof HDLManip)
			return _constantEvaluate((HDLManip) obj, context, evaled);
		else if (obj instanceof HDLTernary)
			return _constantEvaluate((HDLTernary) obj, context, evaled);
		else if (obj != null)
			return _constantEvaluate(obj, context, evaled);
		else
			throw new IllegalArgumentException("Unhandled parameter types: " + Arrays.<Object> asList(obj, context, evaled).toString());
	}
}

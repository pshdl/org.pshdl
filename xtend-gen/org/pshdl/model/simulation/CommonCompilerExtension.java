package org.pshdl.model.simulation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;

import com.google.common.base.Objects;

@SuppressWarnings("all")
public class CommonCompilerExtension {
	public ExecutableModel em;

	public Map<String, Integer> varIdx = new HashMap<String, Integer>();

	public Map<String, Integer> intIdx = new HashMap<String, Integer>();

	public Map<String, Boolean> prevMap = new HashMap<String, Boolean>();

	public boolean hasClock;

	public int bitWidth;

	public CommonCompilerExtension(final ExecutableModel em, final int bitWidth) {
		this.em = em;
		this.bitWidth = bitWidth;
		final int _length = em.variables.length;
		final ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
		for (final Integer i : _doubleDotLessThan) {
			final VariableInformation _get = em.variables[(i).intValue()];
			this.varIdx.put(_get.name, i);
		}
		final int _length_1 = em.internals.length;
		final ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, _length_1, true);
		for (final Integer i_1 : _doubleDotLessThan_1) {
			final InternalInformation _get_1 = em.internals[(i_1).intValue()];
			this.intIdx.put(_get_1.fullName, i_1);
		}
		for (final Frame f : em.frames) {
			{
				if ((f.edgeNegDepRes != (-1))) {
					final InternalInformation _asInternal = this.asInternal(f.edgeNegDepRes);
					this.prevMap.put(_asInternal.info.name, Boolean.valueOf(true));
				}
				if ((f.edgePosDepRes != (-1))) {
					final InternalInformation _asInternal_1 = this.asInternal(f.edgePosDepRes);
					this.prevMap.put(_asInternal_1.info.name, Boolean.valueOf(true));
				}
			}
		}
		final boolean _isEmpty = this.prevMap.isEmpty();
		final boolean _not = (!_isEmpty);
		this.hasClock = _not;
	}

	public InternalInformation asInternal(final int id) {
		return this.em.internals[id];
	}

	public CharSequence asMask(final int width) {
		final BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(width);
		final BigInteger mask = _shiftLeft.subtract(BigInteger.ONE);
		return this.toHexString(mask);
	}

	public CharSequence asMaskL(final int width) {
		final long _doubleLessThan = (1l << width);
		final long mask = (_doubleLessThan - 1);
		if ((width == 64))
			return "0xFFFFFFFFFFFFFFFFl";
		return this.toHexStringL(mask);
	}

	public Iterable<VariableInformation> excludeNull(final VariableInformation[] vars) {
		final Function1<VariableInformation, Boolean> _function = new Function1<VariableInformation, Boolean>() {
			@Override
			public Boolean apply(final VariableInformation it) {
				return Boolean.valueOf(CommonCompilerExtension.this.isNotNull(it));
			}
		};
		return IterableExtensions.<VariableInformation> filter(((Iterable<VariableInformation>) Conversions.doWrapArray(vars)), _function);
	}

	public boolean isNotNull(final VariableInformation it) {
		return (!Objects.equal(it.name, "#null"));
	}

	public boolean isNull(final VariableInformation it) {
		final boolean _isNotNull = this.isNotNull(it);
		return (!_isNotNull);
	}

	public Iterable<InternalInformation> excludeNull(final InternalInformation[] vars) {
		final Function1<InternalInformation, Boolean> _function = new Function1<InternalInformation, Boolean>() {
			@Override
			public Boolean apply(final InternalInformation it) {
				return Boolean.valueOf(CommonCompilerExtension.this.isNotNull(it.info));
			}
		};
		return IterableExtensions.<InternalInformation> filter(((Iterable<InternalInformation>) Conversions.doWrapArray(vars)), _function);
	}

	public long dimMask(final InternalInformation info) {
		final int size = this.totalSize(info.info);
		final long res = Long.highestOneBit(size);
		if ((res == size))
			return (res - 1);
		final long _doubleLessThan = (res << 1);
		return (_doubleLessThan - 1);
	}

	public int arrayFixedOffset(final InternalInformation v) {
		final ArrayList<Integer> dims = this.dimsLastOne(v.info);
		int off = 0;
		final int _length = v.arrayIdx.length;
		final ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
		for (final Integer i : _doubleDotLessThan) {
			{
				final int arr = v.arrayIdx[(i).intValue()];
				final Integer dim = dims.get((i).intValue());
				off = (off + (arr * (dim).intValue()));
			}
		}
		return off;
	}

	public ArrayList<Integer> dimsLastOne(final VariableInformation v) {
		ArrayList<Integer> _xblockexpression = null;
		{
			final ArrayList<Integer> dims = new ArrayList<Integer>((Collection<? extends Integer>) Conversions.doWrapArray(v.dimensions));
			final int _size = dims.size();
			final boolean _greaterThan = (_size > 0);
			if (_greaterThan) {
				final int _size_1 = dims.size();
				final int _minus = (_size_1 - 1);
				dims.set(_minus, Integer.valueOf(1));
			}
			_xblockexpression = dims;
		}
		return _xblockexpression;
	}

	public boolean isArray(final VariableInformation information) {
		final int _length = information.dimensions.length;
		return (_length != 0);
	}

	public StringBuilder arrayAccess(final VariableInformation v) {
		return this.arrayAccess(v, null, "a");
	}

	public StringBuilder arrayAccess(final VariableInformation v, final List<Integer> arr) {
		return this.arrayAccess(v, arr, "a");
	}

	public String arrayAccessBracket(final VariableInformation v, final List<Integer> arr) {
		final boolean _isArray = this.isArray(v);
		final boolean _not = (!_isArray);
		if (_not)
			return "";
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("[");
		final StringBuilder _arrayAccess = this.arrayAccess(v, arr, "a");
		_builder.append(_arrayAccess, "");
		_builder.append("]");
		return _builder.toString();
	}

	public StringBuilder arrayAccessArrIdx(final VariableInformation v) {
		final StringBuilder varAccess = new StringBuilder();
		final ArrayList<Integer> dims = this.dimsLastOne(v);
		final int _length = v.dimensions.length;
		final ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
		for (final Integer i : _doubleDotLessThan) {
			{
				final Integer dim = dims.get((i).intValue());
				if (((i).intValue() != 0)) {
					varAccess.append("+");
				}
				final Integer idx = i;
				if (((dim).intValue() != 1)) {
					final StringConcatenation _builder = new StringConcatenation();
					_builder.append("arrayIdx[");
					_builder.append(idx, "");
					_builder.append("]*");
					_builder.append(dim, "");
					varAccess.append(_builder);
				} else {
					final StringConcatenation _builder_1 = new StringConcatenation();
					_builder_1.append("arrayIdx[");
					_builder_1.append(idx, "");
					_builder_1.append("]");
					varAccess.append(_builder_1);
				}
			}
		}
		return varAccess;
	}

	public StringBuilder arrayAccess(final VariableInformation v, final List<Integer> arr, final String varName) {
		final StringBuilder varAccess = new StringBuilder();
		final ArrayList<Integer> dims = this.dimsLastOne(v);
		final int _length = v.dimensions.length;
		final ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
		for (final Integer i : _doubleDotLessThan) {
			{
				final Integer dim = dims.get((i).intValue());
				if (((i).intValue() != 0)) {
					varAccess.append("+");
				}
				Integer _xifexpression = null;
				final boolean _tripleEquals = (arr == null);
				if (_tripleEquals) {
					_xifexpression = i;
				} else {
					_xifexpression = arr.get((i).intValue());
				}
				final Integer idx = _xifexpression;
				if (((dim).intValue() != 1)) {
					final StringConcatenation _builder = new StringConcatenation();
					_builder.append(varName, "");
					_builder.append(idx, "");
					_builder.append("*");
					_builder.append(dim, "");
					varAccess.append(_builder);
				} else {
					final StringConcatenation _builder_1 = new StringConcatenation();
					_builder_1.append(varName, "");
					_builder_1.append(idx, "");
					varAccess.append(_builder_1);
				}
			}
		}
		return varAccess;
	}

	public CharSequence toHexString(final BigInteger value) {
		final StringConcatenation _builder = new StringConcatenation();
		{
			final int _signum = value.signum();
			final boolean _lessThan = (_signum < 0);
			if (_lessThan) {
				_builder.append("-");
			}
		}
		_builder.append("0x");
		final BigInteger _abs = value.abs();
		final String _string = _abs.toString(16);
		_builder.append(_string, "");
		return _builder;
	}

	public CharSequence toHexStringL(final long value) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("0x");
		final String _hexString = Long.toHexString(value);
		_builder.append(_hexString, "");
		_builder.append("l");
		return _builder;
	}

	public CharSequence toHexStringI(final Integer value) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("0x");
		final String _hexString = Integer.toHexString((value).intValue());
		_builder.append(_hexString, "");
		return _builder;
	}

	public CharSequence getFrameName(final Frame f) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("s");
		final int _max = Math.max(f.scheduleStage, 0);
		final String _format = String.format("%03d", Integer.valueOf(_max));
		_builder.append(_format, "");
		_builder.append("frame");
		final String _format_1 = String.format("%04X", Integer.valueOf(f.uniqueID));
		_builder.append(_format_1, "");
		return _builder;
	}

	public CharSequence constantL(final int id, final Frame f) {
		final StringConcatenation _builder = new StringConcatenation();
		final BigInteger _get = f.constants[id];
		final long _longValue = _get.longValue();
		final CharSequence _hexStringL = this.toHexStringL(_longValue);
		_builder.append(_hexStringL, "");
		return _builder;
	}

	public CharSequence constantI(final int id, final Frame f) {
		final StringConcatenation _builder = new StringConcatenation();
		final BigInteger _get = f.constants[id];
		final int _intValue = _get.intValue();
		final CharSequence _hexStringI = this.toHexStringI(Integer.valueOf(_intValue));
		_builder.append(_hexStringI, "");
		return _builder;
	}

	public int totalSize(final VariableInformation info) {
		int size = 1;
		for (final int d : info.dimensions) {
			size = (size * d);
		}
		return size;
	}

	public boolean isPredicate(final VariableInformation info) {
		return info.name.startsWith(InternalInformation.PRED_PREFIX);
	}

	public String idName(final VariableInformation information, final boolean prev, final boolean field) {
		return this.idName(information.name, prev, field);
	}

	public String idName(final InternalInformation ii, final boolean prev, final boolean field) {
		if (ii.fixedArray)
			return this.idName(ii.fullName, prev, field);
		return this.idName(ii.info, prev, field);
	}

	public String idName(final String name, final boolean prev, final boolean field) {
		String res = name;
		final boolean isReg = name.endsWith("$reg");
		if (isReg) {
			final int _length = name.length();
			final int _minus = (_length - 4);
			final String _substring = name.substring(0, _minus);
			res = _substring;
		}
		final String _replaceAll = res.replaceAll("[\\.\\$\\@]+", "_");
		final String _replaceAll_1 = _replaceAll.replaceAll("\\{", "Bit");
		final String _replaceAll_2 = _replaceAll_1.replaceAll("\\}", "");
		final String _replaceAll_3 = _replaceAll_2.replaceAll(":", "to");
		final String _replaceAll_4 = _replaceAll_3.replaceAll("\\[", "arr");
		final String _replaceAll_5 = _replaceAll_4.replaceAll("\\]", "");
		res = _replaceAll_5;
		final boolean _startsWith = res.startsWith("#");
		if (_startsWith) {
			final String _substring_1 = res.substring(1);
			res = _substring_1;
		}
		if (field) {
			res = ("_" + res);
		}
		if (isReg) {
			res = (res + "$reg");
		}
		if (prev)
			return (res + "_prev");
		return res;
	}

	public int maxRegUpdates(final ExecutableModel em) {
		int maxUpdates = 0;
		for (final Frame f : em.frames) {
			{
				final InternalInformation oi = this.asInternal(f.outputId);
				final boolean _isNotNull = this.isNotNull(oi.info);
				final boolean _not = (!_isNotNull);
				if (_not) {
					for (final Frame.FastInstruction inst : f.instructions) {
						final boolean _tripleEquals = (inst.inst == Instruction.writeInternal);
						if (_tripleEquals) {
							final InternalInformation _asInternal = this.asInternal(inst.arg1);
							if (_asInternal.isShadowReg) {
								maxUpdates = (maxUpdates + 1);
							}
						}
					}
				} else {
					if (oi.isShadowReg) {
						maxUpdates = (maxUpdates + 1);
					}
				}
			}
		}
		return maxUpdates;
	}

	public CharSequence twoOpValue(final String op, final String cast, final int a, final int b, final int targetSizeWithType) {
		final int targetSize = (targetSizeWithType >> 1);
		final int shift = (this.bitWidth - targetSize);
		final int _bitwiseAnd = (targetSizeWithType & 1);
		final boolean _equals = (_bitwiseAnd == 1);
		if (_equals) {
			final StringConcatenation _builder = new StringConcatenation();
			_builder.append("t");
			_builder.append(b, "");
			_builder.append(" ");
			_builder.append(op, "");
			_builder.append(" t");
			_builder.append(a, "");
			return this.signExtend(_builder, cast, shift);
		}
		final StringConcatenation _builder_1 = new StringConcatenation();
		_builder_1.append("(t");
		_builder_1.append(b, "");
		_builder_1.append(" ");
		_builder_1.append(op, "");
		_builder_1.append(" t");
		_builder_1.append(a, "");
		_builder_1.append(") & ");
		final CharSequence _asMaskL = this.asMaskL(targetSize);
		_builder_1.append(_asMaskL, "");
		return _builder_1.toString();
	}

	public CharSequence singleOpValue(final String op, final String cast, final int a, final int targetSizeWithType) {
		final int targetSize = (targetSizeWithType >> 1);
		final int shift = (this.bitWidth - targetSize);
		final int _bitwiseAnd = (targetSizeWithType & 1);
		final boolean _equals = (_bitwiseAnd == 1);
		if (_equals) {
			final StringConcatenation _builder = new StringConcatenation();
			_builder.append(op, "");
			_builder.append(" t");
			_builder.append(a, "");
			return this.signExtend(_builder, cast, shift);
		}
		final StringConcatenation _builder_1 = new StringConcatenation();
		_builder_1.append("(");
		_builder_1.append(op, "");
		_builder_1.append(" t");
		_builder_1.append(a, "");
		_builder_1.append(") & ");
		final CharSequence _asMaskL = this.asMaskL(targetSize);
		_builder_1.append(_asMaskL, "");
		return _builder_1.toString();
	}

	public CharSequence signExtend(final CharSequence op, final CharSequence cast, final int shift) {
		CharSequence _xblockexpression = null;
		{
			if ((shift == 0))
				return op;
			boolean _and = false;
			final boolean _tripleNotEquals = (cast != null);
			if (!_tripleNotEquals) {
				_and = false;
			} else {
				final boolean _notEquals = (!Objects.equal(cast, ""));
				_and = _notEquals;
			}
			if (_and) {
				final StringConcatenation _builder = new StringConcatenation();
				_builder.append("((");
				_builder.append(cast, "");
				_builder.append("(");
				_builder.append(op, "");
				_builder.append(")) << ");
				_builder.append(shift, "");
				_builder.append(") >> ");
				_builder.append(shift, "");
				return _builder.toString();
			}
			final StringConcatenation _builder_1 = new StringConcatenation();
			_builder_1.append("((");
			_builder_1.append(op, "");
			_builder_1.append(") << ");
			_builder_1.append(shift, "");
			_builder_1.append(") >> ");
			_builder_1.append(shift, "");
			_xblockexpression = _builder_1;
		}
		return _xblockexpression;
	}
}

package org.pshdl.model.simulation;

import com.google.common.base.Objects;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.Frame.FastInstruction;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;

@SuppressWarnings("all")
public class CommonCompilerExtension {
  public ExecutableModel em;
  
  public Map<String,Integer> varIdx = new Function0<Map<String,Integer>>() {
    public Map<String,Integer> apply() {
      HashMap<String,Integer> _hashMap = new HashMap<String,Integer>();
      return _hashMap;
    }
  }.apply();
  
  public Map<String,Integer> intIdx = new Function0<Map<String,Integer>>() {
    public Map<String,Integer> apply() {
      HashMap<String,Integer> _hashMap = new HashMap<String,Integer>();
      return _hashMap;
    }
  }.apply();
  
  public Map<String,Boolean> prevMap = new Function0<Map<String,Boolean>>() {
    public Map<String,Boolean> apply() {
      HashMap<String,Boolean> _hashMap = new HashMap<String,Boolean>();
      return _hashMap;
    }
  }.apply();
  
  public boolean hasClock;
  
  public CommonCompilerExtension(final ExecutableModel em) {
    this.em = em;
    int _length = em.variables.length;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer i : _doubleDotLessThan) {
      VariableInformation _get = em.variables[(i).intValue()];
      this.varIdx.put(_get.name, i);
    }
    int _length_1 = em.internals.length;
    ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, _length_1, true);
    for (final Integer i_1 : _doubleDotLessThan_1) {
      InternalInformation _get_1 = em.internals[(i_1).intValue()];
      this.intIdx.put(_get_1.fullName, i_1);
    }
    for (final Frame f : em.frames) {
      {
        int _minus = (-1);
        boolean _notEquals = (f.edgeNegDepRes != _minus);
        if (_notEquals) {
          InternalInformation _asInternal = this.asInternal(f.edgeNegDepRes);
          this.prevMap.put(_asInternal.info.name, Boolean.valueOf(true));
        }
        int _minus_1 = (-1);
        boolean _notEquals_1 = (f.edgePosDepRes != _minus_1);
        if (_notEquals_1) {
          InternalInformation _asInternal_1 = this.asInternal(f.edgePosDepRes);
          this.prevMap.put(_asInternal_1.info.name, Boolean.valueOf(true));
        }
      }
    }
    boolean _isEmpty = this.prevMap.isEmpty();
    boolean _not = (!_isEmpty);
    this.hasClock = _not;
  }
  
  public InternalInformation asInternal(final int id) {
    return this.em.internals[id];
  }
  
  public CharSequence asMask(final int width) {
    BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(width);
    final BigInteger mask = _shiftLeft.subtract(BigInteger.ONE);
    return this.toHexString(mask);
  }
  
  public CharSequence asMaskL(final int width) {
    long _doubleLessThan = (1l << width);
    final long mask = (_doubleLessThan - 1);
    boolean _equals = (width == 64);
    if (_equals) {
      return "0xFFFFFFFFFFFFFFFFl";
    }
    return this.toHexStringL(mask);
  }
  
  public Iterable<VariableInformation> excludeNull(final VariableInformation[] vars) {
    final Function1<VariableInformation,Boolean> _function = new Function1<VariableInformation,Boolean>() {
        public Boolean apply(final VariableInformation it) {
          boolean _isNotNull = CommonCompilerExtension.this.isNotNull(it);
          return Boolean.valueOf(_isNotNull);
        }
      };
    Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(vars)), _function);
    return _filter;
  }
  
  public boolean isNotNull(final VariableInformation it) {
    boolean _notEquals = (!Objects.equal(it.name, "#null"));
    return _notEquals;
  }
  
  public boolean isNull(final VariableInformation it) {
    boolean _isNotNull = this.isNotNull(it);
    boolean _not = (!_isNotNull);
    return _not;
  }
  
  public Iterable<InternalInformation> excludeNull(final InternalInformation[] vars) {
    final Function1<InternalInformation,Boolean> _function = new Function1<InternalInformation,Boolean>() {
        public Boolean apply(final InternalInformation it) {
          boolean _isNotNull = CommonCompilerExtension.this.isNotNull(it.info);
          return Boolean.valueOf(_isNotNull);
        }
      };
    Iterable<InternalInformation> _filter = IterableExtensions.<InternalInformation>filter(((Iterable<InternalInformation>)Conversions.doWrapArray(vars)), _function);
    return _filter;
  }
  
  public long dimMask(final InternalInformation info) {
    final int size = this.totalSize(info.info);
    final long res = Long.highestOneBit(size);
    boolean _equals = (res == size);
    if (_equals) {
      return (res - 1);
    }
    long _doubleLessThan = (res << 1);
    return (_doubleLessThan - 1);
  }
  
  public int arrayFixedOffset(final InternalInformation v) {
    final ArrayList<Integer> dims = this.dimsLastOne(v.info);
    int off = 0;
    int _length = v.arrayIdx.length;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final int arr = v.arrayIdx[(i).intValue()];
        final Integer dim = dims.get((i).intValue());
        int _multiply = (arr * (dim).intValue());
        int _plus = (off + _multiply);
        off = _plus;
      }
    }
    return off;
  }
  
  public ArrayList<Integer> dimsLastOne(final VariableInformation v) {
    ArrayList<Integer> _xblockexpression = null;
    {
      ArrayList<Integer> _arrayList = new ArrayList<Integer>(((Collection<? extends Integer>)Conversions.doWrapArray(v.dimensions)));
      final ArrayList<Integer> dims = _arrayList;
      int _size = dims.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        int _size_1 = dims.size();
        int _minus = (_size_1 - 1);
        dims.set(_minus, Integer.valueOf(1));
      }
      _xblockexpression = (dims);
    }
    return _xblockexpression;
  }
  
  public boolean isArray(final VariableInformation information) {
    int _length = information.dimensions.length;
    boolean _tripleNotEquals = (Integer.valueOf(_length) != Integer.valueOf(0));
    return _tripleNotEquals;
  }
  
  public StringBuilder arrayAccess(final VariableInformation v) {
    StringBuilder _arrayAccess = this.arrayAccess(v, null, "a");
    return _arrayAccess;
  }
  
  public StringBuilder arrayAccess(final VariableInformation v, final List<Integer> arr) {
    StringBuilder _arrayAccess = this.arrayAccess(v, arr, "a");
    return _arrayAccess;
  }
  
  public String arrayAccessBracket(final VariableInformation v, final List<Integer> arr) {
    boolean _isArray = this.isArray(v);
    boolean _not = (!_isArray);
    if (_not) {
      return "";
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[");
    StringBuilder _arrayAccess = this.arrayAccess(v, arr, "a");
    _builder.append(_arrayAccess, "");
    _builder.append("]");
    return _builder.toString();
  }
  
  public StringBuilder arrayAccessArrIdx(final VariableInformation v) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder varAccess = _stringBuilder;
    final ArrayList<Integer> dims = this.dimsLastOne(v);
    int _length = v.dimensions.length;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final Integer dim = dims.get((i).intValue());
        boolean _notEquals = ((i).intValue() != 0);
        if (_notEquals) {
          varAccess.append("+");
        }
        final Integer idx = i;
        boolean _notEquals_1 = ((dim).intValue() != 1);
        if (_notEquals_1) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("arrayIdx[");
          _builder.append(idx, "");
          _builder.append("]*");
          _builder.append(dim, "");
          varAccess.append(_builder);
        } else {
          StringConcatenation _builder_1 = new StringConcatenation();
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
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder varAccess = _stringBuilder;
    final ArrayList<Integer> dims = this.dimsLastOne(v);
    int _length = v.dimensions.length;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final Integer dim = dims.get((i).intValue());
        boolean _notEquals = ((i).intValue() != 0);
        if (_notEquals) {
          varAccess.append("+");
        }
        Integer _xifexpression = null;
        boolean _equals = Objects.equal(arr, null);
        if (_equals) {
          _xifexpression = i;
        } else {
          Integer _get = arr.get((i).intValue());
          _xifexpression = _get;
        }
        final Integer idx = _xifexpression;
        boolean _notEquals_1 = ((dim).intValue() != 1);
        if (_notEquals_1) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append(varName, "");
          _builder.append(idx, "");
          _builder.append("*");
          _builder.append(dim, "");
          varAccess.append(_builder);
        } else {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append(varName, "");
          _builder_1.append(idx, "");
          varAccess.append(_builder_1);
        }
      }
    }
    return varAccess;
  }
  
  public CharSequence toHexString(final BigInteger value) {
    StringConcatenation _builder = new StringConcatenation();
    {
      int _signum = value.signum();
      boolean _lessThan = (_signum < 0);
      if (_lessThan) {
        _builder.append("-");
      }
    }
    _builder.append("0x");
    BigInteger _abs = value.abs();
    String _string = _abs.toString(16);
    _builder.append(_string, "");
    return _builder;
  }
  
  public CharSequence toHexStringL(final long value) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("0x");
    String _hexString = Long.toHexString(value);
    _builder.append(_hexString, "");
    _builder.append("l");
    return _builder;
  }
  
  public CharSequence toHexStringI(final Integer value) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("0x");
    String _hexString = Integer.toHexString((value).intValue());
    _builder.append(_hexString, "");
    return _builder;
  }
  
  public CharSequence getFrameName(final Frame f) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s");
    int _max = Math.max(f.scheduleStage, 0);
    String _format = String.format("%03d", Integer.valueOf(_max));
    _builder.append(_format, "");
    _builder.append("frame");
    String _format_1 = String.format("%04X", Integer.valueOf(f.uniqueID));
    _builder.append(_format_1, "");
    return _builder;
  }
  
  public CharSequence constantL(final int id, final Frame f) {
    StringConcatenation _builder = new StringConcatenation();
    BigInteger _get = f.constants[id];
    long _longValue = _get.longValue();
    CharSequence _hexStringL = this.toHexStringL(_longValue);
    _builder.append(_hexStringL, "");
    return _builder;
  }
  
  public CharSequence constantI(final int id, final Frame f) {
    StringConcatenation _builder = new StringConcatenation();
    BigInteger _get = f.constants[id];
    int _intValue = _get.intValue();
    CharSequence _hexStringI = this.toHexStringI(Integer.valueOf(_intValue));
    _builder.append(_hexStringI, "");
    return _builder;
  }
  
  public int totalSize(final VariableInformation info) {
    int size = 1;
    for (final int d : info.dimensions) {
      int _multiply = (size * d);
      size = _multiply;
    }
    return size;
  }
  
  public boolean isPredicate(final VariableInformation info) {
    boolean _startsWith = info.name.startsWith(InternalInformation.PRED_PREFIX);
    return _startsWith;
  }
  
  public String idName(final VariableInformation information, final boolean prev, final boolean field) {
    return this.idName(information.name, prev, field);
  }
  
  public String idName(final InternalInformation ii, final boolean prev, final boolean field) {
    if (ii.fixedArray) {
      return this.idName(ii.fullName, prev, field);
    }
    return this.idName(ii.info, prev, field);
  }
  
  public String idName(final String name, final boolean prev, final boolean field) {
    String _replaceAll = name.replaceAll("\\.", "_");
    String _replaceAll_1 = _replaceAll.replaceAll("\\{", "Bit");
    String _replaceAll_2 = _replaceAll_1.replaceAll("\\}", "");
    String _replaceAll_3 = _replaceAll_2.replaceAll(":", "to");
    String _replaceAll_4 = _replaceAll_3.replaceAll("\\[", "arr");
    String res = _replaceAll_4.replaceAll("\\]", "");
    boolean _startsWith = res.startsWith("$");
    if (_startsWith) {
      String _substring = res.substring(1);
      res = _substring;
    }
    boolean _startsWith_1 = res.startsWith("#");
    if (_startsWith_1) {
      String _substring_1 = res.substring(1);
      res = _substring_1;
    }
    if (field) {
      String _plus = ("_" + res);
      res = _plus;
    }
    if (prev) {
      return (res + "_prev");
    }
    return res;
  }
  
  public int maxRegUpdates(final ExecutableModel em) {
    int maxUpdates = 0;
    for (final Frame f : em.frames) {
      {
        final InternalInformation oi = this.asInternal(f.outputId);
        boolean _isNotNull = this.isNotNull(oi.info);
        boolean _not = (!_isNotNull);
        if (_not) {
          for (final FastInstruction inst : f.instructions) {
            boolean _tripleEquals = (inst.inst == Instruction.writeInternal);
            if (_tripleEquals) {
              InternalInformation _asInternal = this.asInternal(inst.arg1);
              if (_asInternal.isShadowReg) {
                int _plus = (maxUpdates + 1);
                maxUpdates = _plus;
              }
            }
          }
        } else {
          if (oi.isShadowReg) {
            int _plus_1 = (maxUpdates + 1);
            maxUpdates = _plus_1;
          }
        }
      }
    }
    return maxUpdates;
  }
  
  public CharSequence twoOpValue(final String op, final String cast, final int a, final int b, final int targetSizeWithType) {
    final int targetSize = (targetSizeWithType >> 1);
    final int shift = (64 - targetSize);
    int _bitwiseAnd = (targetSizeWithType & 1);
    boolean _tripleEquals = (Integer.valueOf(_bitwiseAnd) == Integer.valueOf(1));
    if (_tripleEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("t");
      _builder.append(b, "");
      _builder.append(" ");
      _builder.append(op, "");
      _builder.append(" t");
      _builder.append(a, "");
      return this.signExtend(_builder, cast, shift);
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("(t");
    _builder_1.append(b, "");
    _builder_1.append(" ");
    _builder_1.append(op, "");
    _builder_1.append(" t");
    _builder_1.append(a, "");
    _builder_1.append(") & ");
    CharSequence _asMaskL = this.asMaskL(targetSize);
    _builder_1.append(_asMaskL, "");
    return _builder_1.toString();
  }
  
  public CharSequence singleOpValue(final String op, final String cast, final int a, final int targetSizeWithType) {
    final int targetSize = (targetSizeWithType >> 1);
    final int shift = (64 - targetSize);
    int _bitwiseAnd = (targetSizeWithType & 1);
    boolean _tripleEquals = (Integer.valueOf(_bitwiseAnd) == Integer.valueOf(1));
    if (_tripleEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(op, "");
      _builder.append(" t");
      _builder.append(a, "");
      return this.signExtend(_builder, cast, shift);
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("(");
    _builder_1.append(op, "");
    _builder_1.append(" t");
    _builder_1.append(a, "");
    _builder_1.append(") & ");
    CharSequence _asMaskL = this.asMaskL(targetSize);
    _builder_1.append(_asMaskL, "");
    return _builder_1.toString();
  }
  
  public CharSequence signExtend(final CharSequence op, final CharSequence cast, final int shift) {
    CharSequence _xblockexpression = null;
    {
      boolean _equals = (shift == 0);
      if (_equals) {
        return op;
      }
      boolean _and = false;
      boolean _tripleNotEquals = (cast != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        boolean _notEquals = (!Objects.equal(cast, ""));
        _and = (_tripleNotEquals && _notEquals);
      }
      if (_and) {
        StringConcatenation _builder = new StringConcatenation();
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
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("((");
      _builder_1.append(op, "");
      _builder_1.append(") << ");
      _builder_1.append(shift, "");
      _builder_1.append(") >> ");
      _builder_1.append(shift, "");
      _xblockexpression = (_builder_1);
    }
    return _xblockexpression;
  }
}

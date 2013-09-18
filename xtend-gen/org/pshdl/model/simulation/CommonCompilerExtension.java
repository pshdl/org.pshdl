package org.pshdl.model.simulation;

import com.google.common.base.Objects;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class CommonCompilerExtension {
  public /* ExecutableModel */Object em;
  
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
  
  public CommonCompilerExtension(final /* ExecutableModel */Object em) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method edgeNegDepRes is undefined for the type CommonCompilerExtension"
      + "\nThe method edgeNegDepRes is undefined for the type CommonCompilerExtension"
      + "\nThe method edgePosDepRes is undefined for the type CommonCompilerExtension"
      + "\nThe method edgePosDepRes is undefined for the type CommonCompilerExtension"
      + "\nvariables cannot be resolved"
      + "\nlength cannot be resolved"
      + "\nvariables cannot be resolved"
      + "\nget cannot be resolved"
      + "\nname cannot be resolved"
      + "\ninternals cannot be resolved"
      + "\nlength cannot be resolved"
      + "\ninternals cannot be resolved"
      + "\nget cannot be resolved"
      + "\nfullName cannot be resolved"
      + "\nframes cannot be resolved"
      + "\n!= cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nname cannot be resolved"
      + "\n!= cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nname cannot be resolved");
  }
  
  public /* InternalInformation */Object asInternal(final int id) {
    throw new Error("Unresolved compilation problems:"
      + "\ninternals cannot be resolved"
      + "\nget cannot be resolved");
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
  
  public Object excludeNull(final /* type is 'null' */ vars) {
    throw new Error("Unresolved compilation problems:"
      + "\nfilter cannot be resolved");
  }
  
  public Object isNotNull(final /* VariableInformation */Object it) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field name is undefined for the type CommonCompilerExtension"
      + "\n!= cannot be resolved");
  }
  
  public Object isNull(final /* VariableInformation */Object it) {
    throw new Error("Unresolved compilation problems:"
      + "\nInvalid number of arguments. The method isNotNull(VariableInformation) is not applicable for the arguments "
      + "\n! cannot be resolved");
  }
  
  public Object excludeNull(final /* type is 'null' */ vars) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field info is undefined for the type CommonCompilerExtension"
      + "\nfilter cannot be resolved"
      + "\nisNotNull cannot be resolved");
  }
  
  public long dimMask(final /* InternalInformation */Object info) {
    throw new Error("Unresolved compilation problems:"
      + "\ninfo cannot be resolved"
      + "\ntotalSize cannot be resolved");
  }
  
  public int arrayFixedOffset(final /* InternalInformation */Object v) {
    throw new Error("Unresolved compilation problems:"
      + "\ninfo cannot be resolved"
      + "\narrayIdx cannot be resolved"
      + "\nlength cannot be resolved"
      + "\narrayIdx cannot be resolved"
      + "\nget cannot be resolved"
      + "\n* cannot be resolved");
  }
  
  public ArrayList<Integer> dimsLastOne(final /* VariableInformation */Object v) {
    throw new Error("Unresolved compilation problems:"
      + "\ndimensions cannot be resolved");
  }
  
  public Object isArray(final /* VariableInformation */Object information) {
    throw new Error("Unresolved compilation problems:"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n!== cannot be resolved");
  }
  
  public StringBuilder arrayAccess(final /* VariableInformation */Object v) {
    StringBuilder _arrayAccess = this.arrayAccess(v, null, "a");
    return _arrayAccess;
  }
  
  public StringBuilder arrayAccess(final /* VariableInformation */Object v, final List<Integer> arr) {
    StringBuilder _arrayAccess = this.arrayAccess(v, arr, "a");
    return _arrayAccess;
  }
  
  public String arrayAccessBracket(final /* VariableInformation */Object v, final List<Integer> arr) {
    throw new Error("Unresolved compilation problems:"
      + "\narray cannot be resolved"
      + "\n! cannot be resolved");
  }
  
  public StringBuilder arrayAccessArrIdx(final /* VariableInformation */Object v) {
    throw new Error("Unresolved compilation problems:"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved");
  }
  
  public StringBuilder arrayAccess(final /* VariableInformation */Object v, final List<Integer> arr, final String varName) {
    throw new Error("Unresolved compilation problems:"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved");
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
  
  public CharSequence getFrameName(final /* Frame */Object f) {
    throw new Error("Unresolved compilation problems:"
      + "\nscheduleStage cannot be resolved"
      + "\nuniqueID cannot be resolved");
  }
  
  public CharSequence constantL(final int id, final /* Frame */Object f) {
    throw new Error("Unresolved compilation problems:"
      + "\nconstants cannot be resolved"
      + "\nget cannot be resolved"
      + "\nlongValue cannot be resolved"
      + "\ntoHexStringL cannot be resolved");
  }
  
  public CharSequence constantI(final int id, final /* Frame */Object f) {
    throw new Error("Unresolved compilation problems:"
      + "\nconstants cannot be resolved"
      + "\nget cannot be resolved"
      + "\nintValue cannot be resolved"
      + "\ntoHexStringI cannot be resolved");
  }
  
  public int totalSize(final /* VariableInformation */Object info) {
    throw new Error("Unresolved compilation problems:"
      + "\ndimensions cannot be resolved");
  }
  
  public Object isPredicate(final /* VariableInformation */Object info) {
    throw new Error("Unresolved compilation problems:"
      + "\nInternalInformation cannot be resolved to a type."
      + "\nname cannot be resolved"
      + "\nstartsWith cannot be resolved"
      + "\nPRED_PREFIX cannot be resolved");
  }
  
  public Object idName(final /* VariableInformation */Object information, final boolean prev, final boolean field) {
    throw new Error("Unresolved compilation problems:"
      + "\nname cannot be resolved"
      + "\nidName cannot be resolved");
  }
  
  public Object idName(final /* InternalInformation */Object ii, final boolean prev, final boolean field) {
    throw new Error("Unresolved compilation problems:"
      + "\nfixedArray cannot be resolved"
      + "\nfullName cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved");
  }
  
  public String idName(final String name, final boolean prev, final boolean field) {
    String res = name;
    final boolean isReg = name.endsWith("$reg");
    if (isReg) {
      int _length = name.length();
      int _minus = (_length - 4);
      String _substring = name.substring(0, _minus);
      res = _substring;
    }
    String _replaceAll = res.replaceAll("[\\.\\$\\@]+", "_");
    String _replaceAll_1 = _replaceAll.replaceAll("\\{", "Bit");
    String _replaceAll_2 = _replaceAll_1.replaceAll("\\}", "");
    String _replaceAll_3 = _replaceAll_2.replaceAll(":", "to");
    String _replaceAll_4 = _replaceAll_3.replaceAll("\\[", "arr");
    String _replaceAll_5 = _replaceAll_4.replaceAll("\\]", "");
    res = _replaceAll_5;
    boolean _startsWith = res.startsWith("#");
    if (_startsWith) {
      String _substring_1 = res.substring(1);
      res = _substring_1;
    }
    if (field) {
      String _plus = ("_" + res);
      res = _plus;
    }
    if (isReg) {
      String _plus_1 = (res + "$reg");
      res = _plus_1;
    }
    if (prev) {
      return (res + "_prev");
    }
    return res;
  }
  
  public int maxRegUpdates(final /* ExecutableModel */Object em) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method outputId is undefined for the type CommonCompilerExtension"
      + "\nThe method instructions is undefined for the type CommonCompilerExtension"
      + "\nThe method inst is undefined for the type CommonCompilerExtension"
      + "\nInstruction cannot be resolved to a type."
      + "\nThe method arg1 is undefined for the type CommonCompilerExtension"
      + "\nframes cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nnotNull cannot be resolved"
      + "\n! cannot be resolved"
      + "\n=== cannot be resolved"
      + "\nwriteInternal cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\nisShadowReg cannot be resolved"
      + "\nisShadowReg cannot be resolved");
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

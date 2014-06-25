/**
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 * 
 *     Copyright (C) 2014 Karsten Becker (feedback (at) pshdl (dot) org)
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
package org.pshdl.model.simulation;

import com.google.common.base.Objects;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        if ((f.edgeNegDepRes != (-1))) {
          InternalInformation _asInternal = this.asInternal(f.edgeNegDepRes);
          this.prevMap.put(_asInternal.info.name, Boolean.valueOf(true));
        }
        if ((f.edgePosDepRes != (-1))) {
          InternalInformation _asInternal_1 = this.asInternal(f.edgePosDepRes);
          this.prevMap.put(_asInternal_1.info.name, Boolean.valueOf(true));
        }
      }
    }
    boolean _isEmpty = this.prevMap.isEmpty();
    boolean _not = (!_isEmpty);
    this.hasClock = _not;
  }
  
  public String getJSONDescription() {
    final ArrayList<String> intVar = new ArrayList<String>();
    final ArrayList<String> inVar = new ArrayList<String>();
    final ArrayList<String> inOutVar = new ArrayList<String>();
    final ArrayList<String> outVar = new ArrayList<String>();
    for (final VariableInformation vi : this.em.variables) {
      final VariableInformation.Direction _switchValue = vi.dir;
      if (_switchValue != null) {
        switch (_switchValue) {
          case IN:
            String _port = this.toPort(vi);
            inVar.add(_port);
            break;
          case INOUT:
            String _port_1 = this.toPort(vi);
            inOutVar.add(_port_1);
            break;
          case OUT:
            String _port_2 = this.toPort(vi);
            outVar.add(_port_2);
            break;
          case INTERNAL:
            String _port_3 = this.toPort(vi);
            intVar.add(_port_3);
            break;
          default:
            break;
        }
      }
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{\\\"moduleName\\\":\\\"");
    _builder.append(this.em.moduleName, "");
    _builder.append("\\\",\\\"inPorts\\\":[");
    {
      boolean _hasElements = false;
      for(final String port : inVar) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        _builder.append(port, "");
      }
    }
    _builder.append("],\\\"inOutPorts\\\":[");
    {
      boolean _hasElements_1 = false;
      for(final String port_1 : inOutVar) {
        if (!_hasElements_1) {
          _hasElements_1 = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        _builder.append(port_1, "");
      }
    }
    _builder.append("],\\\"outPorts\\\":[");
    {
      boolean _hasElements_2 = false;
      for(final String port_2 : outVar) {
        if (!_hasElements_2) {
          _hasElements_2 = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        _builder.append(port_2, "");
      }
    }
    _builder.append("],\\\"internalPorts\\\":[");
    {
      boolean _hasElements_3 = false;
      for(final String port_3 : intVar) {
        if (!_hasElements_3) {
          _hasElements_3 = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        _builder.append(port_3, "");
      }
    }
    _builder.append("],\\\"nameIdx\\\":{");
    {
      Set<Map.Entry<String, Integer>> _entrySet = this.varIdx.entrySet();
      boolean _hasElements_4 = false;
      for(final Map.Entry<String, Integer> entry : _entrySet) {
        if (!_hasElements_4) {
          _hasElements_4 = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        _builder.append("\\\"");
        String _key = entry.getKey();
        _builder.append(_key, "");
        _builder.append("\\\":");
        Integer _value = entry.getValue();
        _builder.append(_value, "");
      }
    }
    _builder.append("}}");
    return _builder.toString();
  }
  
  public String toPort(final VariableInformation vi) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{\\\"idx\\\":");
    Integer _get = this.varIdx.get(vi.name);
    _builder.append(_get, "");
    _builder.append(",\\\"name\\\":\\\"");
    _builder.append(vi.name, "");
    _builder.append("\\\",\\\"width\\\":");
    _builder.append(vi.width, "");
    _builder.append(",\\\"clock\\\": ");
    _builder.append(vi.isClock, "");
    _builder.append(",\\\"reset\\\":");
    _builder.append(vi.isReset, "");
    _builder.append(",\\\"type\\\":");
    int _bitJsonType = this.bitJsonType(vi);
    _builder.append(_bitJsonType, "");
    _builder.append("}");
    return _builder.toString();
  }
  
  public int bitJsonType(final VariableInformation vi) {
    final VariableInformation.Type _switchValue = vi.type;
    if (_switchValue != null) {
      switch (_switchValue) {
        case BIT:
          return 0;
        case INT:
          return 1;
        case UINT:
          return 2;
        default:
          break;
      }
    }
    return 0;
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
    if ((width == 64)) {
      return "0xFFFFFFFFFFFFFFFFl";
    }
    return this.toHexStringL(mask);
  }
  
  public Iterable<VariableInformation> excludeNull(final VariableInformation[] vars) {
    final Function1<VariableInformation, Boolean> _function = new Function1<VariableInformation, Boolean>() {
      public Boolean apply(final VariableInformation it) {
        return Boolean.valueOf(CommonCompilerExtension.this.isNotNull(it));
      }
    };
    return IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(vars)), _function);
  }
  
  public boolean isNotNull(final VariableInformation it) {
    return (!Objects.equal(it.name, "#null"));
  }
  
  public boolean isNull(final VariableInformation it) {
    boolean _isNotNull = this.isNotNull(it);
    return (!_isNotNull);
  }
  
  public Iterable<InternalInformation> excludeNull(final InternalInformation[] vars) {
    final Function1<InternalInformation, Boolean> _function = new Function1<InternalInformation, Boolean>() {
      public Boolean apply(final InternalInformation it) {
        return Boolean.valueOf(CommonCompilerExtension.this.isNotNull(it.info));
      }
    };
    return IterableExtensions.<InternalInformation>filter(((Iterable<InternalInformation>)Conversions.doWrapArray(vars)), _function);
  }
  
  public long dimMask(final InternalInformation info) {
    final int size = this.totalSize(info.info);
    final long res = Long.highestOneBit(size);
    if ((res == size)) {
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
        off = (off + (arr * (dim).intValue()));
      }
    }
    return off;
  }
  
  public ArrayList<Integer> dimsLastOne(final VariableInformation v) {
    ArrayList<Integer> _xblockexpression = null;
    {
      final ArrayList<Integer> dims = new ArrayList<Integer>((Collection<? extends Integer>)Conversions.doWrapArray(v.dimensions));
      int _size = dims.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        int _size_1 = dims.size();
        int _minus = (_size_1 - 1);
        dims.set(_minus, Integer.valueOf(1));
      }
      _xblockexpression = dims;
    }
    return _xblockexpression;
  }
  
  public boolean isArray(final VariableInformation information) {
    int _length = information.dimensions.length;
    return (_length != 0);
  }
  
  public StringBuilder arrayAccess(final VariableInformation v) {
    return this.arrayAccess(v, null, "a");
  }
  
  public StringBuilder arrayAccess(final VariableInformation v, final List<Integer> arr) {
    return this.arrayAccess(v, arr, "a");
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
    final StringBuilder varAccess = new StringBuilder();
    final ArrayList<Integer> dims = this.dimsLastOne(v);
    int _length = v.dimensions.length;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final Integer dim = dims.get((i).intValue());
        if (((i).intValue() != 0)) {
          varAccess.append("+");
        }
        final Integer idx = i;
        if (((dim).intValue() != 1)) {
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
    final StringBuilder varAccess = new StringBuilder();
    final ArrayList<Integer> dims = this.dimsLastOne(v);
    int _length = v.dimensions.length;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final Integer dim = dims.get((i).intValue());
        if (((i).intValue() != 0)) {
          varAccess.append("+");
        }
        Integer _xifexpression = null;
        boolean _tripleEquals = (arr == null);
        if (_tripleEquals) {
          _xifexpression = i;
        } else {
          _xifexpression = arr.get((i).intValue());
        }
        final Integer idx = _xifexpression;
        if (((dim).intValue() != 1)) {
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
    {
      boolean _notEquals = (!Objects.equal(f.process, null));
      if (_notEquals) {
        _builder.append(f.process, "");
      }
    }
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
    if (ii.fixedArray) {
      return this.idName(ii.fullName, prev, field);
    }
    return this.idName(ii.info, prev, field);
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
    String _replaceAll = res.replaceAll("[\\.\\$\\@\\/]+", "_");
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
      res = ("_" + res);
    }
    if (isReg) {
      res = (res + "$reg");
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
          for (final Frame.FastInstruction inst : f.instructions) {
            boolean _tripleEquals = (inst.inst == Instruction.writeInternal);
            if (_tripleEquals) {
              InternalInformation _asInternal = this.asInternal(inst.arg1);
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
    int _bitwiseAnd = (targetSizeWithType & 1);
    boolean _equals = (_bitwiseAnd == 1);
    if (_equals) {
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
    final int shift = (this.bitWidth - targetSize);
    int _bitwiseAnd = (targetSizeWithType & 1);
    boolean _equals = (_bitwiseAnd == 1);
    if (_equals) {
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
      if ((shift == 0)) {
        return op;
      }
      boolean _and = false;
      boolean _tripleNotEquals = (cast != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        boolean _notEquals = (!Objects.equal(cast, ""));
        _and = _notEquals;
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
      _xblockexpression = _builder_1;
    }
    return _xblockexpression;
  }
  
  public String processState(final ExecutableModel model, final CharSequence processName) {
    return this.varByName(model, ("$process_state_@" + processName));
  }
  
  public String processTime(final ExecutableModel model, final CharSequence processName) {
    return this.varByName(model, ("$process_time_next_@" + processName));
  }
  
  public String varByName(final ExecutableModel model, final String varName) {
    final String fullName = ((model.moduleName + ".") + varName);
    final Function1<VariableInformation, Boolean> _function = new Function1<VariableInformation, Boolean>() {
      public Boolean apply(final VariableInformation it) {
        boolean _or = false;
        boolean _equals = Objects.equal(it.name, varName);
        if (_equals) {
          _or = true;
        } else {
          boolean _equals_1 = Objects.equal(it.name, fullName);
          _or = _equals_1;
        }
        return Boolean.valueOf(_or);
      }
    };
    VariableInformation _findFirst = IterableExtensions.<VariableInformation>findFirst(((Iterable<VariableInformation>)Conversions.doWrapArray(model.variables)), _function);
    return this.idName(_findFirst, false, false);
  }
  
  public Iterable<Frame> getNonProcessframes(final ExecutableModel model) {
    final Function1<Frame, Boolean> _function = new Function1<Frame, Boolean>() {
      public Boolean apply(final Frame it) {
        return Boolean.valueOf(Objects.equal(it.process, null));
      }
    };
    return IterableExtensions.<Frame>filter(((Iterable<Frame>)Conversions.doWrapArray(model.frames)), _function);
  }
  
  public Iterable<Frame> getProcessframes(final ExecutableModel model) {
    final Function1<Frame, Boolean> _function = new Function1<Frame, Boolean>() {
      public Boolean apply(final Frame it) {
        return Boolean.valueOf((!Objects.equal(it.process, null)));
      }
    };
    return IterableExtensions.<Frame>filter(((Iterable<Frame>)Conversions.doWrapArray(model.frames)), _function);
  }
  
  public String predicateConditions(final Frame f) {
    final StringBuilder sb = new StringBuilder();
    boolean first = true;
    if ((f.edgeNegDepRes != (-1))) {
      StringConcatenation _builder = new StringConcatenation();
      InternalInformation _asInternal = this.asInternal(f.edgeNegDepRes);
      String _idName = this.idName(_asInternal, false, false);
      _builder.append(_idName, "");
      _builder.append("_isFalling && !");
      InternalInformation _asInternal_1 = this.asInternal(f.edgeNegDepRes);
      String _idName_1 = this.idName(_asInternal_1, false, false);
      _builder.append(_idName_1, "");
      _builder.append("_fallingIsHandled");
      sb.append(_builder);
      first = false;
    }
    if ((f.edgePosDepRes != (-1))) {
      if ((!first)) {
        sb.append(" && ");
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      InternalInformation _asInternal_2 = this.asInternal(f.edgePosDepRes);
      String _idName_2 = this.idName(_asInternal_2, false, false);
      _builder_1.append(_idName_2, "");
      _builder_1.append("_isRising&& !");
      InternalInformation _asInternal_3 = this.asInternal(f.edgePosDepRes);
      String _idName_3 = this.idName(_asInternal_3, false, false);
      _builder_1.append(_idName_3, "");
      _builder_1.append("_risingIsHandled");
      sb.append(_builder_1);
      first = false;
    }
    for (final int p : f.predNegDepRes) {
      {
        if ((!first)) {
          sb.append(" && ");
        }
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("!p");
        _builder_2.append(p, "");
        _builder_2.append(" && p");
        _builder_2.append(p, "");
        _builder_2.append("_fresh");
        sb.append(_builder_2);
        first = false;
      }
    }
    for (final int p_1 : f.predPosDepRes) {
      {
        if ((!first)) {
          sb.append(" && ");
        }
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("p");
        _builder_2.append(p_1, "");
        _builder_2.append(" && p");
        _builder_2.append(p_1, "");
        _builder_2.append("_fresh");
        sb.append(_builder_2);
        first = false;
      }
    }
    return sb.toString();
  }
}

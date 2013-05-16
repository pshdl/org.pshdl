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

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import java.util.Arrays;
import java.util.Iterator;
import javax.annotation.Nonnull;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.pshdl.model.HDLBlock;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLIfStatement.TreeSide;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLObject.GenericMeta;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.HDLQualifiedName;

/**
 * The FullNameExtension provides a {@link HDLQualifiedName} for every IHDLObject.
 * See {@link FullNameExtension#fullNameOf(IHDLObject)}
 * 
 * @author Karsten Becker
 */
@SuppressWarnings("all")
public class FullNameExtension {
  /**
   * This annotation is used to store {@ink HDLQualifiedName} for the case that the resolution diverges from the actual tree
   */
  public static GenericMeta<HDLQualifiedName> FULLNAME = new Function0<GenericMeta<HDLQualifiedName>>() {
    public GenericMeta<HDLQualifiedName> apply() {
      GenericMeta<HDLQualifiedName> _genericMeta = new GenericMeta<HDLQualifiedName>("FULLNAME", true);
      return _genericMeta;
    }
  }.apply();
  
  private static FullNameExtension INST = new Function0<FullNameExtension>() {
    public FullNameExtension apply() {
      FullNameExtension _fullNameExtension = new FullNameExtension();
      return _fullNameExtension;
    }
  }.apply();
  
  /**
   * Returns the {@link HDLQualifiedName} for the given obj.
   */
  @Nonnull
  public static HDLQualifiedName fullNameOf(final IHDLObject obj) {
    HDLQualifiedName _xblockexpression = null;
    {
      boolean _tripleEquals = (obj == null);
      if (_tripleEquals) {
        NullPointerException _nullPointerException = new NullPointerException("Can not get a name for null");
        throw _nullPointerException;
      }
      HDLQualifiedName _fullName = FullNameExtension.INST.getFullName(obj);
      _xblockexpression = (_fullName);
    }
    return _xblockexpression;
  }
  
  private static int countInstance(final HDLObject obj) {
    int count = 0;
    IHDLObject _container = obj.getContainer();
    boolean _tripleNotEquals = (_container != null);
    if (_tripleNotEquals) {
      IHDLObject _container_1 = obj.getContainer();
      final Iterator<IHDLObject> iterator = _container_1.iterator();
      boolean _hasNext = iterator.hasNext();
      boolean _while = _hasNext;
      while (_while) {
        {
          final IHDLObject hdlObject = iterator.next();
          boolean _equals = Objects.equal(hdlObject, obj);
          if (_equals) {
            return count;
          }
          HDLClass _classType = hdlObject.getClassType();
          HDLClass _classType_1 = obj.getClassType();
          boolean _equals_1 = Objects.equal(_classType, _classType_1);
          if (_equals_1) {
            int _plus = (count + 1);
            count = _plus;
          }
        }
        boolean _hasNext_1 = iterator.hasNext();
        _while = _hasNext_1;
      }
    }
    return count;
  }
  
  protected HDLQualifiedName _getFullName(final HDLForLoop loop) {
    HDLQualifiedName _meta = loop.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return loop.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    final HDLQualifiedName fullName = this.getSuperFullName(loop);
    final int count = FullNameExtension.countInstance(loop);
    String _plus = ("$for" + Integer.valueOf(count));
    return fullName.append(_plus);
  }
  
  protected HDLQualifiedName _getFullName(final HDLBlock block) {
    HDLQualifiedName _meta = block.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return block.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    final HDLQualifiedName fullName = this.getSuperFullName(block);
    final int count = FullNameExtension.countInstance(block);
    String _plus = ("$block" + Integer.valueOf(count));
    return fullName.append(_plus);
  }
  
  protected HDLQualifiedName _getFullName(final HDLIfStatement ifStamnt) {
    HDLQualifiedName _meta = ifStamnt.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return ifStamnt.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    final HDLQualifiedName fullName = this.getSuperFullName(ifStamnt);
    final int count = FullNameExtension.countInstance(ifStamnt);
    String _plus = ("$if" + Integer.valueOf(count));
    return fullName.append(_plus);
  }
  
  protected HDLQualifiedName _getFullName(final HDLSwitchStatement stmnt) {
    HDLQualifiedName _meta = stmnt.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return stmnt.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    final HDLQualifiedName fullName = this.getSuperFullName(stmnt);
    final int count = FullNameExtension.countInstance(stmnt);
    String _plus = ("$switch" + Integer.valueOf(count));
    return fullName.append(_plus);
  }
  
  protected HDLQualifiedName _getFullName(final HDLSwitchCaseStatement stmnt) {
    HDLQualifiedName _meta = stmnt.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return stmnt.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    final HDLQualifiedName fullName = this.getSuperFullName(stmnt);
    final int count = FullNameExtension.countInstance(stmnt);
    String _plus = ("$case" + Integer.valueOf(count));
    return fullName.append(_plus);
  }
  
  protected HDLQualifiedName _getFullName(final HDLUnit unit) {
    HDLQualifiedName _meta = unit.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return unit.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    final HDLQualifiedName fullName = this.getSuperFullName(unit);
    String _name = unit.getName();
    HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_name);
    return fullName.append(_hDLQualifiedName);
  }
  
  protected HDLQualifiedName _getFullName(final HDLInterface unit) {
    HDLQualifiedName _meta = unit.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return unit.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    final HDLQualifiedName fullName = this.getSuperFullName(unit);
    String _name = unit.getName();
    HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_name);
    return fullName.append(_hDLQualifiedName);
  }
  
  protected HDLQualifiedName _getFullName(final HDLEnum unit) {
    HDLQualifiedName _meta = unit.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return unit.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    final HDLQualifiedName fullName = this.getSuperFullName(unit);
    String _name = unit.getName();
    HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_name);
    return fullName.append(_hDLQualifiedName);
  }
  
  protected HDLQualifiedName _getFullName(final HDLFunction unit) {
    HDLQualifiedName _meta = unit.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return unit.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    final HDLQualifiedName fullName = this.getSuperFullName(unit);
    String _name = unit.getName();
    HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_name);
    return fullName.append(_hDLQualifiedName);
  }
  
  protected HDLQualifiedName _getFullName(final HDLPackage pkg) {
    HDLQualifiedName _meta = pkg.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return pkg.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    final HDLQualifiedName fullName = this.getSuperFullName(pkg);
    String _pkg = pkg.getPkg();
    boolean _tripleNotEquals_1 = (_pkg != null);
    if (_tripleNotEquals_1) {
      String _pkg_1 = pkg.getPkg();
      HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_pkg_1);
      return fullName.append(_hDLQualifiedName);
    }
    return fullName;
  }
  
  protected HDLQualifiedName _getFullName(final HDLEnumRef ref) {
    HDLQualifiedName _meta = ref.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return ref.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    Optional<HDLVariable> _resolveVar = ref.resolveVar();
    HDLVariable _get = _resolveVar.get();
    return this.getFullName(_get);
  }
  
  protected HDLQualifiedName _getFullName(final HDLVariableRef ref) {
    HDLQualifiedName _meta = ref.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return ref.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    Optional<HDLVariable> _resolveVar = ref.resolveVar();
    HDLVariable _get = _resolveVar.get();
    return this.getFullName(_get);
  }
  
  protected HDLQualifiedName _getFullName(final HDLVariable unit) {
    HDLQualifiedName _meta = unit.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return unit.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    final HDLQualifiedName fullName = this.getSuperFullName(unit);
    String _name = unit.getName();
    HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_name);
    return fullName.append(_hDLQualifiedName);
  }
  
  protected HDLQualifiedName _getFullName(final IHDLObject obj) {
    HDLQualifiedName _meta = obj.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return obj.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    IHDLObject _container = obj.getContainer();
    boolean _tripleNotEquals_1 = (_container != null);
    if (_tripleNotEquals_1) {
      IHDLObject _container_1 = obj.getContainer();
      return this.getFullName(_container_1);
    }
    return HDLQualifiedName.EMPTY;
  }
  
  protected HDLQualifiedName _getFullName(final HDLObject obj) {
    HDLQualifiedName _meta = obj.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return obj.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    IHDLObject _container = obj.getContainer();
    boolean _tripleNotEquals_1 = (_container != null);
    if (_tripleNotEquals_1) {
      IHDLObject _container_1 = obj.getContainer();
      return this.getFullName(_container_1);
    }
    return HDLQualifiedName.EMPTY;
  }
  
  public HDLQualifiedName getSuperFullName(final HDLObject obj) {
    HDLQualifiedName _meta = obj.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    boolean _tripleNotEquals = (_meta != null);
    if (_tripleNotEquals) {
      return obj.<HDLQualifiedName>getMeta(FullNameExtension.FULLNAME);
    }
    IHDLObject _container = obj.getContainer();
    boolean _tripleNotEquals_1 = (_container != null);
    if (_tripleNotEquals_1) {
      IHDLObject _container_1 = obj.getContainer();
      final HDLQualifiedName fn = this.getFullName(_container_1);
      IHDLObject _container_2 = obj.getContainer();
      if ((_container_2 instanceof HDLIfStatement)) {
        IHDLObject _container_3 = obj.getContainer();
        final HDLIfStatement ifStmnt = ((HDLIfStatement) _container_3);
        final TreeSide side = ifStmnt.treeSide(obj);
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(side,TreeSide.thenTree)) {
            _matched=true;
            String _string = fn.toString();
            String _plus = (_string + "p");
            HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_plus);
            return _hDLQualifiedName;
          }
        }
        if (!_matched) {
          if (Objects.equal(side,TreeSide.elseTree)) {
            _matched=true;
            String _string_1 = fn.toString();
            String _plus_1 = (_string_1 + "n");
            HDLQualifiedName _hDLQualifiedName_1 = new HDLQualifiedName(_plus_1);
            return _hDLQualifiedName_1;
          }
        }
      }
      return fn;
    }
    return HDLQualifiedName.EMPTY;
  }
  
  public HDLQualifiedName getFullName(final IHDLObject unit) {
    if (unit instanceof HDLEnum) {
      return _getFullName((HDLEnum)unit);
    } else if (unit instanceof HDLEnumRef) {
      return _getFullName((HDLEnumRef)unit);
    } else if (unit instanceof HDLVariableRef) {
      return _getFullName((HDLVariableRef)unit);
    } else if (unit instanceof HDLForLoop) {
      return _getFullName((HDLForLoop)unit);
    } else if (unit instanceof HDLFunction) {
      return _getFullName((HDLFunction)unit);
    } else if (unit instanceof HDLIfStatement) {
      return _getFullName((HDLIfStatement)unit);
    } else if (unit instanceof HDLInterface) {
      return _getFullName((HDLInterface)unit);
    } else if (unit instanceof HDLSwitchCaseStatement) {
      return _getFullName((HDLSwitchCaseStatement)unit);
    } else if (unit instanceof HDLSwitchStatement) {
      return _getFullName((HDLSwitchStatement)unit);
    } else if (unit instanceof HDLBlock) {
      return _getFullName((HDLBlock)unit);
    } else if (unit instanceof HDLPackage) {
      return _getFullName((HDLPackage)unit);
    } else if (unit instanceof HDLUnit) {
      return _getFullName((HDLUnit)unit);
    } else if (unit instanceof HDLVariable) {
      return _getFullName((HDLVariable)unit);
    } else if (unit instanceof HDLObject) {
      return _getFullName((HDLObject)unit);
    } else if (unit != null) {
      return _getFullName(unit);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(unit).toString());
    }
  }
}

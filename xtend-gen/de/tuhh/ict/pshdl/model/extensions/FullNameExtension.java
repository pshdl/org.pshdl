package de.tuhh.ict.pshdl.model.extensions;

import com.google.common.base.Objects;
import de.tuhh.ict.pshdl.model.HDLBlock;
import de.tuhh.ict.pshdl.model.HDLClass;
import de.tuhh.ict.pshdl.model.HDLEnum;
import de.tuhh.ict.pshdl.model.HDLForLoop;
import de.tuhh.ict.pshdl.model.HDLFunction;
import de.tuhh.ict.pshdl.model.HDLIfStatement;
import de.tuhh.ict.pshdl.model.HDLInterface;
import de.tuhh.ict.pshdl.model.HDLObject;
import de.tuhh.ict.pshdl.model.HDLObject.GenericMeta;
import de.tuhh.ict.pshdl.model.HDLPackage;
import de.tuhh.ict.pshdl.model.HDLSwitchCaseStatement;
import de.tuhh.ict.pshdl.model.HDLSwitchStatement;
import de.tuhh.ict.pshdl.model.HDLUnit;
import de.tuhh.ict.pshdl.model.HDLVariable;
import de.tuhh.ict.pshdl.model.IHDLObject;
import de.tuhh.ict.pshdl.model.utils.HDLQualifiedName;
import java.util.Arrays;
import java.util.Iterator;
import javax.annotation.Nonnull;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class FullNameExtension {
  public static GenericMeta<HDLQualifiedName> FULLNAME = new Function0<GenericMeta<HDLQualifiedName>>() {
    public GenericMeta<HDLQualifiedName> apply() {
      GenericMeta<HDLQualifiedName> _genericMeta = new GenericMeta<HDLQualifiedName>("FULLNAME", true);
      return _genericMeta;
    }
  }.apply();
  
  public static FullNameExtension INST = new Function0<FullNameExtension>() {
    public FullNameExtension apply() {
      FullNameExtension _fullNameExtension = new FullNameExtension();
      return _fullNameExtension;
    }
  }.apply();
  
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
      return this.getFullName(_container_1);
    }
    return HDLQualifiedName.EMPTY;
  }
  
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
  
  public static int countInstance(final HDLObject obj) {
    int count = 0;
    IHDLObject _container = obj.getContainer();
    boolean _tripleNotEquals = (_container != null);
    if (_tripleNotEquals) {
      IHDLObject _container_1 = obj.getContainer();
      final Iterator<IHDLObject> iterator = _container_1.iterator(false);
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
  
  public HDLQualifiedName getFullName(final IHDLObject unit) {
    if (unit instanceof HDLEnum) {
      return _getFullName((HDLEnum)unit);
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

package de.tuhh.ict.pshdl.model.aspects;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

public aspect FullNameAspect {
	
	public static String FULLNAME="FULLNAME";
	public HDLQualifiedName HDLForLoop.getFullName() {
		if (getMeta(FULLNAME)!=null)
			return (HDLQualifiedName)getMeta(FULLNAME);
		HDLQualifiedName fullName = super.getFullName();
		int count = countInstance(this);
		return fullName.append("$for" + count);
	}
	
	public HDLQualifiedName HDLBlock.getFullName() {
		if (getMeta(FULLNAME)!=null)
			return (HDLQualifiedName)getMeta(FULLNAME);
		HDLQualifiedName fullName = super.getFullName();
		int count = countInstance(this);
		return fullName.append("$block" + count);
	}

	public HDLQualifiedName HDLIfStatement.getFullName() {
		if (getMeta(FULLNAME)!=null)
			return (HDLQualifiedName)getMeta(FULLNAME);
		HDLQualifiedName fullName = super.getFullName();
		int count = countInstance(this);
		return fullName.append("$if" + count);
	}

	public HDLQualifiedName HDLSwitchStatement.getFullName() {
		if (getMeta(FULLNAME)!=null)
			return (HDLQualifiedName)getMeta(FULLNAME);
		HDLQualifiedName fullName = super.getFullName();
		int count = countInstance(this);
		return fullName.append("$switch" + count);
	}

	public HDLQualifiedName HDLSwitchCaseStatement.getFullName() {
		if (getMeta(FULLNAME)!=null)
			return (HDLQualifiedName)getMeta(FULLNAME);
		HDLQualifiedName fullName = super.getFullName();
		int count = countInstance(this);
		return fullName.append("$case" + count);
	}

	public HDLQualifiedName HDLUnit.getFullName() {
		if (getMeta(FULLNAME)!=null)
			return (HDLQualifiedName)getMeta(FULLNAME);
		HDLQualifiedName fullName = super.getFullName();
		return fullName.append(new HDLQualifiedName(getName()));
	}
	public HDLQualifiedName HDLInterface.getFullName() {
		if (getMeta(FULLNAME)!=null)
			return (HDLQualifiedName)getMeta(FULLNAME);
		HDLQualifiedName fullName = super.getFullName();
		return fullName.append(new HDLQualifiedName(getName()));
	}
	public HDLQualifiedName HDLEnum.getFullName() {
		if (getMeta(FULLNAME)!=null)
			return (HDLQualifiedName)getMeta(FULLNAME);
		HDLQualifiedName fullName = super.getFullName();
		return fullName.append(new HDLQualifiedName(getName()));
	}
	public HDLQualifiedName HDLFunction.getFullName() {
		if (getMeta(FULLNAME)!=null)
			return (HDLQualifiedName)getMeta(FULLNAME);
		HDLQualifiedName fullName = super.getFullName();
		return fullName.append(new HDLQualifiedName(getName()));
	}

	public HDLQualifiedName HDLPackage.getFullName() {
		if (getMeta(FULLNAME)!=null)
			return (HDLQualifiedName)getMeta(FULLNAME);
		HDLQualifiedName fullName = super.getFullName();
		if (getPkg() != null)
			return fullName.append(new HDLQualifiedName(getPkg()));
		return fullName;
	}

	public HDLQualifiedName HDLVariable.getFullName() {
		if (getMeta(FULLNAME)!=null)
			return (HDLQualifiedName)getMeta(FULLNAME);
		return super.getFullName().append(getName());
	}

	
	public HDLQualifiedName HDLObject.getFullName() {
		if (getMeta(FULLNAME)!=null)
			return (HDLQualifiedName)getMeta(FULLNAME);
		if (getContainer() != null)
			return getContainer().getFullName();
		return HDLQualifiedName.EMPTY;
	}

	private static int countInstance(HDLObject obj) {
		int count = 0;
		if (obj.getContainer() != null) {
			Iterator<IHDLObject> iterator = obj.getContainer().iterator(false);
			while (iterator.hasNext()) {
				IHDLObject hdlObject = iterator.next();
				if (hdlObject == obj)
					break;
				if (hdlObject.getClassType() == obj.getClassType())
					count++;
			}
		}
		return count;
	}

}

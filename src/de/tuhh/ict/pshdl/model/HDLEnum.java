package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLEnum contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLVariable> enums. Can <b>not</b> be <code>null</code>,
 * additionally the collection must contain at least one element.</li>
 * </ul>
 */
public class HDLEnum extends AbstractHDLEnum {
	/**
	 * Constructs a new instance of {@link HDLEnum}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param enums
	 *            the value for enums. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLEnum(int containerID, @Nullable HDLObject container, @NonNull String name, @NonNull ArrayList<HDLVariable> enums, boolean validate) {
		super(containerID, container, name, enums, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLEnum}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param enums
	 *            the value for enums. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 */
	public HDLEnum(int containerID, @Nullable HDLObject container, @NonNull String name, @NonNull ArrayList<HDLVariable> enums) {
		this(containerID, container, name, enums, true);
	}

	public HDLEnum() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLEnum;
	}

	/**
	 * The accessor for the field enums which is of type ArrayList<HDLVariable>.
	 */
	public static HDLFieldAccess<HDLEnum, ArrayList<HDLVariable>> fEnums = new HDLFieldAccess<HDLEnum, ArrayList<HDLVariable>>() {
		@Override
		public ArrayList<HDLVariable> getValue(HDLEnum obj) {
			if (obj == null)
				return null;
			return obj.getEnums();
		}
	};

	// $CONTENT-BEGIN$

	@Override
	public HDLVariable resolveVariable(HDLQualifiedName var) {
		if (var.length == 1) {
			return getVariable(var.getLastSegment());
		}
		if (getFullName().equals(var.skipLast(1)))
			return getVariable(var.getLastSegment());
		return super.resolveVariable(var);
	}

	public HDLVariable getVariable(String lastSegment) {
		for (HDLVariable var : getEnums()) {
			if (var.getName().equals(lastSegment))
				return var;
		}
		return null;
	}

	// $CONTENT-END$

}

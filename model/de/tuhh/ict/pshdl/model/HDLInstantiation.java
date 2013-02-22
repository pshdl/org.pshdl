package de.tuhh.ict.pshdl.model;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLInstantiation contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLVariable var. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLArgument> arguments. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLInstantiation extends AbstractHDLInstantiation {
	/**
	 * Constructs a new instance of {@link HDLInstantiation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLInstantiation(@Nullable IHDLObject container, @Nonnull HDLVariable var, @Nullable ArrayList<HDLArgument> arguments, boolean validate) {
		super(container, var, arguments, validate);
	}

	public HDLInstantiation() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLInstantiation;
	}

	/**
	 * The accessor for the field var which is of type HDLVariable.
	 */
	public static HDLFieldAccess<HDLInstantiation, HDLVariable> fVar = new HDLFieldAccess<HDLInstantiation, HDLVariable>("var") {
		@Override
		public HDLVariable getValue(HDLInstantiation obj) {
			if (obj == null)
				return null;
			return obj.getVar();
		}
	};
	/**
	 * The accessor for the field arguments which is of type
	 * ArrayList<HDLArgument>.
	 */
	public static HDLFieldAccess<HDLInstantiation, ArrayList<HDLArgument>> fArguments = new HDLFieldAccess<HDLInstantiation, ArrayList<HDLArgument>>("arguments") {
		@Override
		public ArrayList<HDLArgument> getValue(HDLInstantiation obj) {
			if (obj == null)
				return null;
			return obj.getArguments();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

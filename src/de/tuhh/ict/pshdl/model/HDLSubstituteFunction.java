package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLSubstituteFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>String args. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> stmnts. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLSubstituteFunction extends AbstractHDLSubstituteFunction {
	/**
	 * Constructs a new instance of {@link HDLSubstituteFunction}
	 * 
	 * @param objectID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param args
	 *            the value for args. Can <b>not</b> be <code>null</code>.
	 * @param stmnts
	 *            the value for stmnts. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLSubstituteFunction(int objectID, @Nullable IHDLObject container, @NonNull String name, @NonNull String args, @Nullable ArrayList<HDLStatement> stmnts,
			boolean validate, boolean updateContainer) {
		super(objectID, container, name, args, stmnts, validate, updateContainer);
	}

	/**
	 * Constructs a new instance of {@link HDLSubstituteFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param args
	 *            the value for args. Can <b>not</b> be <code>null</code>.
	 * @param stmnts
	 *            the value for stmnts. Can be <code>null</code>.
	 */
	public HDLSubstituteFunction(int objectID, @Nullable IHDLObject container, @NonNull String name, @NonNull String args, @Nullable ArrayList<HDLStatement> stmnts) {
		this(objectID, container, name, args, stmnts, true, true);
	}

	public HDLSubstituteFunction() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLSubstituteFunction;
	}

	/**
	 * The accessor for the field args which is of type String.
	 */
	public static HDLFieldAccess<HDLSubstituteFunction, String> fArgs = new HDLFieldAccess<HDLSubstituteFunction, String>() {
		@Override
		public String getValue(HDLSubstituteFunction obj) {
			if (obj == null)
				return null;
			return obj.getArgs();
		}
	};
	/**
	 * The accessor for the field stmnts which is of type
	 * ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLSubstituteFunction, ArrayList<HDLStatement>> fStmnts = new HDLFieldAccess<HDLSubstituteFunction, ArrayList<HDLStatement>>() {
		@Override
		public ArrayList<HDLStatement> getValue(HDLSubstituteFunction obj) {
			if (obj == null)
				return null;
			return obj.getStmnts();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

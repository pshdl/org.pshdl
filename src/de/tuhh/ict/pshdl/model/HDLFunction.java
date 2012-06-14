package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLFunction contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> params. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLFunction extends AbstractHDLFunction {
	/**
	 * Constructs a new instance of {@link HDLFunction}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLFunction(int containerID, HDLObject container, String name, ArrayList<HDLExpression> params, boolean validate) {
		super(containerID, container, name, params, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 */
	public HDLFunction(int containerID, HDLObject container, String name, ArrayList<HDLExpression> params) {
		this(containerID, container, name, params, true);
	}

	public HDLFunction() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLFunction;
	}

	/**
	 * The accessor for the field name which is of type String
	 */
	public static HDLFieldAccess<HDLFunction, String> fName = new HDLFieldAccess<HDLFunction, String>() {
		@Override
		public String getValue(HDLFunction obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};
	/**
	 * The accessor for the field params which is of type
	 * ArrayList<HDLExpression>
	 */
	public static HDLFieldAccess<HDLFunction, ArrayList<HDLExpression>> fParams = new HDLFieldAccess<HDLFunction, ArrayList<HDLExpression>>() {
		@Override
		public ArrayList<HDLExpression> getValue(HDLFunction obj) {
			if (obj == null)
				return null;
			return obj.getParams();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

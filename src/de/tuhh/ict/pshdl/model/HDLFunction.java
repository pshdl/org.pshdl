package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLFunction extends AbstractHDLFunction {
	/**
	 * Constructs a new instance of {@link HDLFunction}
	 * 
	 * @param objectID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLFunction(int objectID, @Nullable IHDLObject container, @NonNull String name, boolean validate, boolean updateContainer) {
		super(objectID, container, name, validate, updateContainer);
	}

	/**
	 * Constructs a new instance of {@link HDLFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 */
	public HDLFunction(int objectID, @Nullable IHDLObject container, @NonNull String name) {
		this(objectID, container, name, true, true);
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
	 * The accessor for the field name which is of type String.
	 */
	public static HDLFieldAccess<HDLFunction, String> fName = new HDLFieldAccess<HDLFunction, String>() {
		@Override
		public String getValue(HDLFunction obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

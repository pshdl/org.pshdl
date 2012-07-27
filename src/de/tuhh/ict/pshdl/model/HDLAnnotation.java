package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLAnnotation contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>String value. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLAnnotation extends AbstractHDLAnnotation {
	/**
	 * Constructs a new instance of {@link HDLAnnotation}
	 * 
	 * @param objectID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param value
	 *            the value for value. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLAnnotation(int objectID, @Nullable IHDLObject container, @NonNull String name, @Nullable String value, boolean validate, boolean updateContainer) {
		super(objectID, container, name, value, validate, updateContainer);
	}

	/**
	 * Constructs a new instance of {@link HDLAnnotation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param value
	 *            the value for value. Can be <code>null</code>.
	 */
	public HDLAnnotation(int objectID, @Nullable IHDLObject container, @NonNull String name, @Nullable String value) {
		this(objectID, container, name, value, true, true);
	}

	public HDLAnnotation() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLAnnotation;
	}

	/**
	 * The accessor for the field name which is of type String.
	 */
	public static HDLFieldAccess<HDLAnnotation, String> fName = new HDLFieldAccess<HDLAnnotation, String>() {
		@Override
		public String getValue(HDLAnnotation obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};
	/**
	 * The accessor for the field value which is of type String.
	 */
	public static HDLFieldAccess<HDLAnnotation, String> fValue = new HDLFieldAccess<HDLAnnotation, String>() {
		@Override
		public String getValue(HDLAnnotation obj) {
			if (obj == null)
				return null;
			return obj.getValue();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

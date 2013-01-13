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
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param value
	 *            the value for value. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLAnnotation(@Nullable IHDLObject container, @NonNull String name, @Nullable String value, boolean validate) {
		super(container, name, value, validate);
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
	public static HDLFieldAccess<HDLAnnotation, String> fName = new HDLFieldAccess<HDLAnnotation, String>("name") {
		@Override
		public String getValue(HDLAnnotation obj) {
			if (obj == null) {
				return null;
			}
			return obj.getName();
		}
	};
	/**
	 * The accessor for the field value which is of type String.
	 */
	public static HDLFieldAccess<HDLAnnotation, String> fValue = new HDLFieldAccess<HDLAnnotation, String>("value") {
		@Override
		public String getValue(HDLAnnotation obj) {
			if (obj == null) {
				return null;
			}
			return obj.getValue();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

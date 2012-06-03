package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLAnnotation contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
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
	public HDLAnnotation(HDLObject container, String name, String value, boolean validate) {
		super(container, name, value, validate);
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
	public HDLAnnotation(HDLObject container, String name, String value) {
		this(container, name, value, true);
	}

	public HDLAnnotation() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLAnnotation;
	}

	public static HDLFieldAccess<HDLAnnotation, String> fName = new HDLFieldAccess<HDLAnnotation, String>() {
		@Override
		public String getValue(HDLAnnotation obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};
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

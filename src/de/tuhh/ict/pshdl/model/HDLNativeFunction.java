package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLNativeFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>Boolean simOnly. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLNativeFunction extends AbstractHDLNativeFunction {
	/**
	 * Constructs a new instance of {@link HDLNativeFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param simOnly
	 *            the value for simOnly. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLNativeFunction(@Nullable IHDLObject container, @Nullable ArrayList<HDLAnnotation> annotations, @NonNull String name, @NonNull Boolean simOnly, boolean validate) {
		super(container, annotations, name, simOnly, validate);
	}

	public HDLNativeFunction() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLNativeFunction;
	}

	/**
	 * The accessor for the field simOnly which is of type Boolean.
	 */
	public static HDLFieldAccess<HDLNativeFunction, Boolean> fSimOnly = new HDLFieldAccess<HDLNativeFunction, Boolean>("simOnly") {
		@Override
		public Boolean getValue(HDLNativeFunction obj) {
			if (obj == null)
				return null;
			return obj.getSimOnly();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

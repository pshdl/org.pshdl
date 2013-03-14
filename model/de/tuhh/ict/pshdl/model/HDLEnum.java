package de.tuhh.ict.pshdl.model;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLEnum contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> dim. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLVariable> enums. Can <b>not</b> be <code>null</code>,
 * additionally the collection must contain at least one element.</li>
 * </ul>
 */
public class HDLEnum extends AbstractHDLEnum {
	/**
	 * Constructs a new instance of {@link HDLEnum}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dim
	 *            the value for dim. Can be <code>null</code>.
	 * @param enums
	 *            the value for enums. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLEnum(@Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dim, @Nonnull Iterable<HDLVariable> enums, boolean validate) {
		super(container, name, dim, enums, validate);
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
	public static HDLFieldAccess<HDLEnum, ArrayList<HDLVariable>> fEnums = new HDLFieldAccess<HDLEnum, ArrayList<HDLVariable>>("enums") {
		@Override
		public ArrayList<HDLVariable> getValue(HDLEnum obj) {
			if (obj == null)
				return null;
			return obj.getEnums();
		}
	};
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}

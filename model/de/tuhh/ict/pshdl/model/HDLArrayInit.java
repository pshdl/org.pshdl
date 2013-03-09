package de.tuhh.ict.pshdl.model;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLArrayInit contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> exp. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLArrayInit extends AbstractHDLArrayInit {
	/**
	 * Constructs a new instance of {@link HDLArrayInit}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param exp
	 *            the value for exp. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLArrayInit(@Nullable IHDLObject container, @Nullable Iterable<HDLExpression> exp, boolean validate) {
		super(container, exp, validate);
	}

	public HDLArrayInit() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLArrayInit;
	}

	/**
	 * The accessor for the field exp which is of type ArrayList<HDLExpression>.
	 */
	public static HDLFieldAccess<HDLArrayInit, ArrayList<HDLExpression>> fExp = new HDLFieldAccess<HDLArrayInit, ArrayList<HDLExpression>>("exp") {
		@Override
		public ArrayList<HDLExpression> getValue(HDLArrayInit obj) {
			if (obj == null)
				return null;
			return obj.getExp();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

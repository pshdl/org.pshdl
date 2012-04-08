package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

public class HDLManip extends AbstractHDLManip {
	/**
	 * Constructs a new instance of {@link HDLManip}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param target
	 *            the value for target. Can <b>not</b> be <code>null</code>.
	 * @param castTo
	 *            the value for castTo. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLManip(HDLObject container, HDLManipType type, HDLExpression target, HDLType castTo, boolean validate) {
		super(container, type, target, castTo, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLManip}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param target
	 *            the value for target. Can <b>not</b> be <code>null</code>.
	 * @param castTo
	 *            the value for castTo. Can be <code>null</code>.
	 */
	public HDLManip(HDLObject container, HDLManipType type, HDLExpression target, HDLType castTo) {
		this(container, type, target, castTo, true);
	}

	public HDLManip() {
		super();
	}

	public HDLClass getClassType() {
		return HDLClass.HDLManip;
	}

	public static enum HDLManipType {
		CAST, ARITH_NEG, BIT_NEG, LOGIC_NEG;
	}

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

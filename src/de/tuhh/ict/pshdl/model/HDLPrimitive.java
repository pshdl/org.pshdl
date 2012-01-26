package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.HDLValueType.*;
import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLPrimitive extends AbstractHDLPrimitive {
	/**
	 * Constructs a new instance of {@link HDLPrimitive}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>,
	 *            {@link HDLDirection#INTERNAL} is used as default.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param width
	 *            the value for width. Can be <code>null</code>.
	 */
	public HDLPrimitive(HDLObject container, HDLRegisterConfig register, HDLDirection direction, HDLPrimitiveType type, HDLExpression width) {
		super(container, register, direction, type, width);
	}

	public HDLPrimitive() {
		super();
	}

	public static enum HDLPrimitiveType {
		INT, UINT, INTEGER, NATURAL, BIT, BITVECTOR, BOOL,
	}
}

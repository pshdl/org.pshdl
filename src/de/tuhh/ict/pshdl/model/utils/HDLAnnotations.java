package de.tuhh.ict.pshdl.model.utils;

import de.tuhh.ict.pshdl.model.*;

public enum HDLAnnotations {
	/**
	 * Designates a bit signal as clock
	 */
	clock,
	/**
	 * Designates a bit signal as reset
	 */
	reset,
	/**
	 * Indicates a range of values that are allowed for this variable. The value
	 * are the lower bound separated by a semicolon and the upper bound. For
	 * example: -1;6 indicates that the variable can have a value of either
	 * -1,0,1,2,3,4,5,6
	 */
	range,
	/**
	 * The name of the type that should be used instead of an automatically
	 * created type during VHDL code generation. The name should start with
	 * VHDL.
	 */
	VHDLType,
	/**
	 * This annotation causes the default initialization to 0 to be omitted.
	 * This MAY cause a latch to be created.
	 */
	VHDLLatchable;
	@Override
	public String toString() {
		return "@" + name();
	}

	public boolean is(HDLAnnotation anno) {
		return anno.getName().substring(1).equals(name());
	}

	public HDLAnnotation create(String value) {
		return new HDLAnnotation().setName(toString()).setValue(value);
	}
}

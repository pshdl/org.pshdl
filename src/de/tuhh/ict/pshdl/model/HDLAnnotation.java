package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLAnnotation extends AbstractHDLAnnotation {
	/**
	 * @see AbstractHDLAnnotation#AbstractHDLAnnotation(HDLObject, String,
	 *      String) AbstractHDLAnnotation
	 */
	public HDLAnnotation(HDLObject container, String name, String value) {
		super(container, name, value);
	}

	public HDLAnnotation() {
		super();
	}
}

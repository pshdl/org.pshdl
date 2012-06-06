package de.tuhh.ict.pshdl.model.utils;

import de.tuhh.ict.pshdl.model.*;

public enum HDLAnnotations {
	clock, reset, VHDLType;
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

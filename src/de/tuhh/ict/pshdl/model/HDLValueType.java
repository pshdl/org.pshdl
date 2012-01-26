package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public abstract class HDLValueType extends AbstractHDLValueType {
	public HDLValueType(HDLObject container, HDLRegisterConfig register, HDLDirection direction) {
		super(container, register, direction);
	}
	public HDLValueType() {
		super();
	}
	 	public static enum HDLDirection {
			IN,OUT,INOUT,PARAMETER,CONST,INTERNAL,HIDDEN,
		}
}	

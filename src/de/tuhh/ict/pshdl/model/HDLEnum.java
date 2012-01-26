package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLEnum extends AbstractHDLEnum {
	public HDLEnum(HDLObject container, HDLRegisterConfig register, HDLDirection direction, String name, ArrayList<String> enums) {
		super(container, register, direction, name, enums);
	}
	public HDLEnum() {
		super();
	}
}	

package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
public class HDLUnit extends AbstractHDLUnit {
	public HDLUnit(HDLObject container, String name, ArrayList<String> imports, ArrayList<HDLStatement> statements) {
		super(container, name, imports, statements);
	}

	public HDLUnit() {
		super();
	}
}

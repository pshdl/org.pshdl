package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import org.antlr.v4.runtime.*;

public interface NamedElement {
	public String getName();

	public void setLocation(Token start);

}

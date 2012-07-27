package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;

public interface HDLStatement extends de.tuhh.ict.pshdl.model.utils.IStatementContainer, de.tuhh.ict.pshdl.model.IHDLObject {
	@Override
	public HDLStatement copy();

	@Override
	public HDLStatement copyFiltered(CopyFilter filter);

}

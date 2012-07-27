package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;

public interface HDLExpression extends de.tuhh.ict.pshdl.model.IHDLObject {
	@Override
	public HDLExpression copy();

	@Override
	public HDLExpression copyFiltered(CopyFilter filter);
}

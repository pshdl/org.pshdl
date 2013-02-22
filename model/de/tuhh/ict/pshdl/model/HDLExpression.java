package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;

public interface HDLExpression extends de.tuhh.ict.pshdl.model.IHDLObject {

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	public abstract HDLExpression copy();

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	public abstract HDLExpression copyFiltered(CopyFilter filter);

	/**
	 * Creates a deep copy of this class with the same fields and frozen
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	public abstract HDLExpression copyDeepFrozen(IHDLObject container);

}
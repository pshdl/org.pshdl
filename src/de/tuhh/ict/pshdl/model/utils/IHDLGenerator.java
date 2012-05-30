package de.tuhh.ict.pshdl.model.utils;

import de.tuhh.ict.pshdl.model.*;

public interface IHDLGenerator {

	public static class HDLGenerationInfo {
		public final boolean include;
		public final HDLUnit unit;

		public HDLGenerationInfo(boolean include, HDLUnit unit) {
			super();
			this.include = include;
			this.unit = unit;
		}
	}

	public HDLInterface getInterface(HDLDirectGeneration hdl);

	public HDLGenerationInfo getImplementation(HDLDirectGeneration hdl);

}

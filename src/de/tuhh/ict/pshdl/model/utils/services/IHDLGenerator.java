package de.tuhh.ict.pshdl.model.utils.services;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public interface IHDLGenerator {
	public static class SideFile {
		public static final byte[] THIS = new byte[] { 'T', 'H', 'I', 'S' };
		public final String relPath;
		public final byte[] contents;

		public SideFile(String relPath, byte[] contents) {
			super();
			this.relPath = relPath;
			this.contents = contents;
		}

	}

	public static class HDLGenerationInfo {
		public List<SideFile> files = new LinkedList<SideFile>();
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

	public String[] getNames();

}

package de.tuhh.ict.pshdl.model.utils.services;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.GeneratorInformation;
import de.tuhh.ict.pshdl.model.validation.*;

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
		public final HDLUnit unit;

		public HDLGenerationInfo(HDLUnit unit) {
			super();
			this.unit = unit;
		}
	}

	public HDLInterface getInterface(HDLDirectGeneration hdl);

	public HDLGenerationInfo getImplementation(HDLDirectGeneration hdl);

	public String[] getNames();

	public void validate(HDLDirectGeneration hdg, Set<Problem> problems, HDLEvaluationContext context);

	public List<HDLVariableDeclaration> getPortAdditions(HDLDirectGeneration hdl);

	public GeneratorInformation getGeneratorInfo(String name);

}

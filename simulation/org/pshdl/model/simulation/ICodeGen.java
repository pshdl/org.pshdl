package org.pshdl.model.simulation;

public interface ICodeGen {
	public CharSequence compile(String packageName, String unitName);

	public CharSequence createChangeAdapter(String packageName, String unitName);
}

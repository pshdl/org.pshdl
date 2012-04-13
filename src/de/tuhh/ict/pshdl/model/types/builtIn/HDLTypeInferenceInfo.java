package de.tuhh.ict.pshdl.model.types.builtIn;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLTypeInferenceInfo {
	public HDLPrimitive result;
	public String error;
	public HDLPrimitive[] args;

	public HDLTypeInferenceInfo(HDLPrimitive result, HDLPrimitive... args) {
		super();
		this.args = args;
		this.result = result;
	}

	@Override
	public String toString() {
		return "HDLTypeInferenceInfo [result=" + result + ", error=" + error + " args=" + Arrays.toString(args) + "]";
	}

}
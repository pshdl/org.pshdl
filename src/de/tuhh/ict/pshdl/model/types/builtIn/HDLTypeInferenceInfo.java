package de.tuhh.ict.pshdl.model.types.builtIn;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLTypeInferenceInfo {
	public HDLType result;
	public String error;
	public HDLType[] args;

	public HDLTypeInferenceInfo(HDLPrimitive result, HDLType... args) {
		super();
		this.args = args;
		this.result = result;
	}

	@Override
	public String toString() {
		return "HDLTypeInferenceInfo [result=" + result + ", error=" + error + " args=" + Arrays.toString(args) + "]";
	}

}
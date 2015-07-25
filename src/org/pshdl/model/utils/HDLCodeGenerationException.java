package org.pshdl.model.utils;

import org.pshdl.model.IHDLObject;

public class HDLCodeGenerationException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1233367291601210999L;
	public final IHDLObject causedBy;
	public final String reason;
	public final String stage;

	public HDLCodeGenerationException(IHDLObject causedBy, String reason, String stage) {
		super("The object:" + causedBy + " caused a " + reason + " at stage:" + stage);
		this.causedBy = causedBy;
		this.reason = reason;
		this.stage = stage;
	}

	@Override
	public String toString() {
		return "The object:" + causedBy + " " + reason + " at stage:" + stage;
	}

}

package de.tuhh.ict.pshdl.model.evaluation;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLEvaluationContext {

	private Map<String, HDLExpression> context;

	public HDLEvaluationContext(Map<String, HDLExpression> context) {
		this.context = context;
	}

	public HDLExpression get(HDLVariable ref) {
		return context.get(ref.getName());
	}

}

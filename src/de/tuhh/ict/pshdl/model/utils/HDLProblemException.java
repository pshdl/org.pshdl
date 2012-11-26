package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.validation.*;

public class HDLProblemException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1516706647395219835L;
	public final Problem[] problems;

	public HDLProblemException(String message, Problem... problems) {
		super(message);
		this.problems = problems;
	}

	public HDLProblemException(Problem... problems) {
		super("The following problems where encountered:" + Arrays.toString(problems));
		this.problems = problems;
	}

	public Problem[] getProblems() {
		return problems;
	}
}

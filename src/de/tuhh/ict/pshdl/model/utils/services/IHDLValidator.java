package de.tuhh.ict.pshdl.model.utils.services;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.validation.HDLValidator.HDLAdvise;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.Problem.ProblemSeverity;

public interface IHDLValidator {
	public interface IErrorCode {
		public String name();

		public ProblemSeverity getSeverity();
	}

	public boolean check(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> context);

	public Class<?> getErrorClass();

	public HDLAdvise advise(Problem problem);

	public String getName();
}

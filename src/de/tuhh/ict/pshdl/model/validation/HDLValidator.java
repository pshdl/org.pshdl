package de.tuhh.ict.pshdl.model.validation;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;

public class HDLValidator {

	public static class HDLAdvise {
		public final Problem problem;
		public final String explanation;
		public final String message;
		public final String[] solutions;

		public HDLAdvise(Problem problem, String message, String explanation, String... solutions) {
			super();
			this.problem = problem;
			this.explanation = explanation;
			this.message = message;
			this.solutions = solutions;
		}
	}

	private static Map<Class<?>, IHDLValidator> validators;

	public static void init(CompilerInformation info, IServiceProvider sp) {
		validators = new HashMap<Class<?>, IHDLValidator>();
		for (IHDLValidator gen : sp.getAllValidators()) {
			String name = gen.getName();
			validators.put(gen.getErrorClass(), gen);
			info.registeredValidators.put(name, gen);
		}
	}

	public static Set<Problem> validate(HDLPackage pkg, Map<HDLQualifiedName, HDLEvaluationContext> context) {
		Set<Problem> res = new LinkedHashSet<Problem>();
		if (context == null)
			context = HDLEvaluationContext.createDefault(pkg);
		for (IHDLValidator validator : validators.values()) {
			validator.check(pkg, res, context);
		}
		return res;
	}

	public static HDLAdvise advise(Problem problem) {
		IHDLValidator validator = validators.get(problem.code.getClass());
		return validator.advise(problem);
	}

}

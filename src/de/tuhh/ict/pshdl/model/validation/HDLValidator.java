package de.tuhh.ict.pshdl.model.validation;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLObject.MetaAccess;
import de.tuhh.ict.pshdl.model.utils.*;

public class HDLValidator {

	public static enum IntegerMeta implements MetaAccess<Integer> {
		READ_COUNT, WRITE_COUNT, ACCESS
	}

	public static enum ProblemSeverity {
		INFO, WARNING, ERROR;
	}

	public static enum ErrorCode {
		PARAM_NOT_CONSTANT,
		// RWValidation
		WRITE_ACCESS_TO_IN_PORT, IN_PORT_NEVER_READ, OUT_PORT_NEVER_WRITTEN, OUT_PORT_ONLY_READ, UNUSED_VARIABLE, INTERNAL_SIGNAL_WRITTEN_BUT_NEVER_READ, WRITE_ACCESS_TO_PARAMETER_OR_CONSTANT_PORT, PARAMETER_OR_CONSTANT_NEVER_READ, WRITE_ACCESS_TO_OUT_PORT_OF_INTERFACE, INTERNAL_SIGNAL_READ_BUT_NEVER_WRITTEN,
		// RWValidation Interface
		INTERFACE_OUT_PORT_NEVER_READ, INTERFACE_OUT_WRITTEN, INTERFACE_IN_PORT_NEVER_WRITTEN, INTERFACE_UNUSED_PORT,
		// TypeChecker
		BIT_ASSIGNMENT_WIDTH_MISMATCH, CONSTANT_WIDTH_MISMATCH,
		// Annotation check
		ONLY_ONE_CLOCK_ANNOTATION_ALLOWED, ONLY_ONE_RESET_ANNOTATION_ALLOWED,
	}

	public static class Problem {
		public enum ProblemAccess implements MetaAccess<Problem> {
			PROBLEM;
		}

		public final ProblemSeverity severity;
		public final ErrorCode code;
		public final HDLObject node;
		public final HDLObject context;

		public Problem(ProblemSeverity severity, ErrorCode code, HDLObject node) {
			this(severity, code, node, null);
		}

		public Problem(ProblemSeverity severity, ErrorCode code, HDLObject node, HDLObject context) {
			if (node == null)
				throw new IllegalArgumentException("Node can not be null!");
			this.context = context;
			this.severity = severity;
			this.code = code;
			this.node = node;
			node.addMeta(ProblemAccess.PROBLEM, this);
			if (context != null)
				context.addMeta(ProblemAccess.PROBLEM, this);
		}

		@Override
		public String toString() {
			String string = severity + ":" + code.name().toLowerCase() + " for: " + node;
			if (context != null)
				string += " @ " + context;
			return string;
		}

		public String toStringWithoutSeverity() {
			String string = code.name().toLowerCase() + " for: " + node;
			if (context != null)
				string += " @ " + context;
			return string;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + ((code == null) ? 0 : code.hashCode());
			result = (prime * result) + ((severity == null) ? 0 : severity.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Problem other = (Problem) obj;
			if (code != other.code)
				return false;
			if (context != other.context)
				return false;
			if (node != other.node)
				return false;
			if (severity != other.severity)
				return false;
			return true;
		}

	}

	public static Set<Problem> validate(HDLPackage unit, Map<String, HDLExpression> context) {
		Set<Problem> problems = new HashSet<Problem>();
		try {
			RWValidation.checkVariableUsage(unit, problems);
			checkClockAndResetAnnotation(unit, problems);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return problems;
	}

	private static void checkClockAndResetAnnotation(HDLPackage unit, Set<Problem> problems) {
		List<HDLAnnotation> clocks = HDLQuery.select(HDLAnnotation.class).from(unit).where(HDLAnnotation.fName).isEqualTo(HDLAnnotations.clock.toString()).getAll();
		if (clocks.size() > 1)
			for (HDLAnnotation anno : clocks) {
				problems.add(new Problem(ProblemSeverity.ERROR, ErrorCode.ONLY_ONE_CLOCK_ANNOTATION_ALLOWED, anno));
			}
		List<HDLAnnotation> resets = HDLQuery.select(HDLAnnotation.class).from(unit).where(HDLAnnotation.fName).isEqualTo(HDLAnnotations.reset.toString()).getAll();
		if (resets.size() > 1)
			for (HDLAnnotation anno : resets) {
				problems.add(new Problem(ProblemSeverity.ERROR, ErrorCode.ONLY_ONE_RESET_ANNOTATION_ALLOWED, anno));
			}
	}

}

package de.tuhh.ict.pshdl.model.validation;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.LevenshteinDistance.Score;
import de.tuhh.ict.pshdl.model.utils.services.*;

public class HDLAdvisor {
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

	public static HDLAdvise getAdvise(Problem problem) {
		switch (problem.code) {
		case ANNOTATION_INVALID:
			String annoName = ((HDLAnnotation) problem.node).getName();
			return new HDLAdvise(
					problem,
					"The String you provided is incorrect for this annotation: " + problem.info,
					"Some annotations can have additional information. Those are stated as a String in double quotes. The interpretation of this String is depending on the Annotation. The String you provided however was not correct.",
					"Check the message for information what might be incorrect", "Check the documentation for the " + annoName + " Annotation");
		case ANNOTATION_UNKNOWN:
			String lastName = ((HDLAnnotation) problem.node).getName();
			Score[] topMatches = LevenshteinDistance.getTopMatches(lastName, true, HDLAnnotations.knownAnnotations());
			StringBuilder matchSolution = new StringBuilder();
			matchSolution.append("Annotations that you might have meant: ");
			for (int i = 0; i < Math.min(3, topMatches.length); i++) {
				Score score = topMatches[i];
				if (i != 0)
					matchSolution.append(", ");
				matchSolution.append('@').append(score.string);
			}
			return new HDLAdvise(problem, "The annotation: " + lastName + " is not known",
					"The annotation you have used is not known to the compiler. Maybe it was misspelled or it is not installed.", matchSolution.toString(),
					"Check the installed libraries of the compiler");
		case ARRAY_INDEX_NEGATIVE:
			return new HDLAdvise(problem, "The index of the array can only be negative",
					"When an array is accessed, the index has to be positive as negative indexes don't make sense", "Cast the index to uint");
		case ARRAY_INDEX_NOT_PRIMITIVE:
			return new HDLAdvise(problem, "Only primitives like uint and int are allowed as index index",
					"Enums or interface references do not have a numerical value, they thus can't be used as array index", "Use another variable or constant as index");
		case ARRAY_INDEX_OUT_OF_BOUNDS: {
			ValueRange accessRange = (ValueRange) problem.meta.get("accessRange");
			ValueRange arrayRange = (ValueRange) problem.meta.get("arrayRange");
			return new HDLAdvise(problem, "The array index exceeds its capacity", "Valid access index for the array are: " + arrayRange + " while the index has a range of: "
					+ accessRange + ". These don't overlap which means that the index will never be valid");
		}
		case ARRAY_INDEX_POSSIBLY_NEGATIVE: {
			ValueRange accessRange = (ValueRange) problem.meta.get("accessRange");
			ValueRange arrayRange = (ValueRange) problem.meta.get("arrayRange");
			ValueRange commonRange = arrayRange.and(accessRange);
			String[] solutions;
			if (commonRange == null)
				solutions = new String[] { "Cast the index to uint with:(uint)" + problem.node };
			else
				solutions = new String[] { "Cast the index to uint with:(uint)" + problem.node,
						"Manually declare a range for the index with the @range(\"" + arrayRange.from + ";" + arrayRange.to + "\") annotation to define a range" };
			return new HDLAdvise(problem, "The array index could possibly become negative", "The given array index has a possible negative value (" + accessRange.from
					+ "), even tough it does not need to become negative by design, it would be possible. This moght indicate a programming error", solutions);
		}
		case ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS: {
			ValueRange accessRange = (ValueRange) problem.meta.get("accessRange");
			ValueRange arrayRange = (ValueRange) problem.meta.get("arrayRange");
			ValueRange commonRange = arrayRange.and(accessRange);
			return new HDLAdvise(problem, "The array index can exceed its capacity", "The given array index has a possible range of:" + accessRange
					+ " while the highest index of the array is " + arrayRange.to, "Limit the possible range by masking with &", "Downcast the index to a suitable size",
					"Use the @range(\"" + commonRange.from + ";" + commonRange.to + "\") Annotation to indicate the expected range");
		}
		case ARRAY_REFERENCE_NOT_SAME_DIMENSIONS:
			break;
		case COMBINED_ASSIGNMENT_NOT_ALLOWED:
			break;
		case CONSTANT_DEFAULT_VALUE_NOT_CONSTANT:
			break;
		case CONSTANT_NEED_DEFAULTVALUE:
			break;
		case EQUALITY_ALWAYS_FALSE:
			break;
		case EQUALITY_ALWAYS_TRUE:
			break;
		case FOR_LOOP_RANGE_NOT_CONSTANT:
			break;
		case GENERATOR_ERROR:
			break;
		case GENERATOR_INFO:
			break;
		case GENERATOR_NOT_KNOWN:
			break;
		case GENERATOR_WARNING:
			break;
		case INTERFACE_IN_PORT_NEVER_WRITTEN:
			break;
		case INTERFACE_OUT_PORT_NEVER_READ:
			break;
		case INTERFACE_OUT_WRITTEN:
			break;
		case INTERFACE_UNUSED_PORT:
			break;
		case INTERNAL_SIGNAL_READ_BUT_NEVER_WRITTEN:
			break;
		case INTERNAL_SIGNAL_WRITTEN_BUT_NEVER_READ:
			break;
		case IN_PORT_CANT_REGISTER:
			break;
		case IN_PORT_NEVER_READ:
			break;
		case MULTI_PROCESS_WRITE:
			break;
		case NO_SUCH_FUNCTION:
			break;
		case ONLY_ONE_CLOCK_ANNOTATION_ALLOWED:
			break;
		case ONLY_ONE_RESET_ANNOTATION_ALLOWED:
			break;
		case OUT_PORT_NEVER_WRITTEN:
			break;
		case PARAMETER_OR_CONSTANT_NEVER_READ:
			break;
		case UNRESOLVED_ENUM:
			break;
		case UNRESOLVED_INTERFACE:
			break;
		case UNRESOLVED_REFERENCE:
			break;
		case UNRESOLVED_TYPE:
			break;
		case UNRESOLVED_VARIABLE:
			break;
		case UNSUPPORTED_TYPE_FOR_OP:
			switch (problem.node.getClassType()) {
			case HDLArithOp:
				return new HDLAdvise(problem, problem.info, "Arithmetic operations require an interpretable value, they can not work on bit types",
						"Cast either side to uint<?>/int<?>");
			case HDLShiftOp:
				return new HDLAdvise(problem, problem.info, "Shift operations require an interpretable value on the right hand side, they can not work on bit types",
						"Cast the right hand-side to uint<?>/int<?>");
			case HDLEqualityOp:
				return new HDLAdvise(problem, problem.info,
						"Equality operations require an interpretable value when a greater/less than compare is used, they can not work on bit types",
						"Cast the either side to uint<?>/int<?>");
			case HDLBitOp:
				return new HDLAdvise(problem, problem.info, "Bit operations generally work on any primitive type, see the message for more information");
			default:

			}
			break;
		case UNUSED_VARIABLE:
			return new HDLAdvise(problem, "The variable: " + problem.node + " is not used", "This variable is never read or written. You can safely remove it", "Remove it",
					"Use it");
		case WRITE_ACCESS_TO_IN_PORT:
			return new HDLAdvise(problem, "The variable: " + problem.node + " is defined as in port and written",
					"A variable that is declared as in can only be read. Writing to it is not supported", "Remove the in keyword in the declaration",
					"Assign it to a new internal variable", "Change the direction to inout");
		}
		return null;
	}
}

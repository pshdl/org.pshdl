package de.tuhh.ict.pshdl.model.validation.builtin;

import java.math.*;
import java.util.*;

import com.google.common.collect.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.LevenshteinDistance.Score;
import de.tuhh.ict.pshdl.model.validation.HDLValidator.HDLAdvise;
import de.tuhh.ict.pshdl.model.validation.*;

public class BuiltInAdvisor {

	public static HDLAdvise advise(Problem problem) {
		switch ((ErrorCode) problem.code) {
		case ANNOTATION_INVALID:
			String annoName = ((HDLAnnotation) problem.node).getName();
			return new HDLAdvise(
					problem,
					"The String you provided is incorrect for this annotation: " + problem.info,
					"Some annotations can have additional information. Those are stated as a String in double quotes. The interpretation of this String is depending on the Annotation. The String you provided however was not correct.",
					"Check the message for information what might be incorrect", "Check the documentation for the " + annoName + " Annotation");
		case ANNOTATION_UNKNOWN: {
			String lastName = ((HDLAnnotation) problem.node).getName();
			Set<String> knownAnnotations = HDLCore.getCompilerInformation().registeredAnnotations.keySet();
			String annoProposal = getMatchProposal(knownAnnotations, "Generators", lastName);
			return new HDLAdvise(problem, "The annotation: " + lastName + " is not known",
					"The annotation you have used is not known to the compiler. Maybe it was misspelled or it is not installed.", annoProposal,
					"Check the installed libraries of the compiler");
		}
		case ARRAY_INDEX_NEGATIVE:
			return new HDLAdvise(problem, "The index of the array can only be negative",
					"When an array is accessed, the index has to be positive as negative indexes don't make sense", "Cast the index to uint");
		case ARRAY_INDEX_OUT_OF_BOUNDS: {
			Range<BigInteger> accessRange = problem.getMeta(BuiltInValidator.ACCESS_RANGE);
			Range<BigInteger> arrayRange = problem.getMeta(BuiltInValidator.ARRAY_RANGE);
			return new HDLAdvise(problem, "The array index exceeds its capacity", "Valid access index for the array are: " + arrayRange + " while the index has a range of: "
					+ accessRange + ". These don't overlap which means that the index will never be valid");
		}
		case ARRAY_INDEX_POSSIBLY_NEGATIVE: {
			Range<BigInteger> accessRange = problem.getMeta(BuiltInValidator.ACCESS_RANGE);
			Range<BigInteger> arrayRange = problem.getMeta(BuiltInValidator.ARRAY_RANGE);
			String[] solutions;
			if (!arrayRange.isConnected(accessRange))
				solutions = new String[] { "Cast the index to uint with:(uint)" + problem.node };
			else
				solutions = new String[] {
						"Cast the index to uint with:(uint)" + problem.node,
						"Manually declare a range for the index with the @range(\"" + arrayRange.lowerEndpoint() + ";" + arrayRange.upperEndpoint()
								+ "\") annotation to define a range" };
			return new HDLAdvise(problem, "The array index could possibly become negative", "The given array index has a possible negative value (" + accessRange.lowerEndpoint()
					+ "), even tough it does not need to become negative by design, it would be possible. This moght indicate a programming error", solutions);
		}
		case ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS: {
			Range<BigInteger> accessRange = problem.getMeta(BuiltInValidator.ACCESS_RANGE);
			Range<BigInteger> arrayRange = problem.getMeta(BuiltInValidator.ARRAY_RANGE);
			Range<BigInteger> commonRange = arrayRange.intersection(accessRange);
			return new HDLAdvise(problem, "The array index can exceed its capacity", "The given array index has a possible range of:" + accessRange
					+ " while the highest index of the array is " + arrayRange.upperEndpoint(), "Limit the possible range by masking with &",
					"Downcast the index to a suitable size", "Use the @range(\"" + commonRange.lowerEndpoint() + ";" + commonRange.upperEndpoint()
							+ "\") Annotation to indicate the expected range");
		}
		case ARRAY_REFERENCE_NOT_SAME_DIMENSIONS:
			break;
		case BIT_ACCESS_NOT_POSSIBLE:
			return new HDLAdvise(problem, "Can not access bits of this type",
					"Only bits from primitive types with a known with can be access, that is either int<?>,uint<?>,bit<?>.", "cast type to a known width");
		case COMBINED_ASSIGNMENT_NOT_ALLOWED:
			return new HDLAdvise(problem, "A combined assignment can only be used on registers",
					"Using a combined assignment on a non register variable will cause a combinaatorical loop. This is not what you want on a FPGA",
					"Declare the variable as register");
		case CONSTANT_DEFAULT_VALUE_NOT_CONSTANT:
			return new HDLAdvise(problem, "The value of a constant needs to be constant", "You can not use parameters or other variables for computing the value of a constant",
					"Ensure that only constant variables are used as value");
		case CONSTANT_NEED_DEFAULTVALUE:
			return new HDLAdvise(problem, "Constants need to have a value", "A constant without value is pretty pointless", "Assign a default value to the constant");
		case EQUALITY_ALWAYS_FALSE:
			break;
		case EQUALITY_ALWAYS_TRUE:
			break;
		case FOR_LOOP_RANGE_NOT_CONSTANT:
			break;
		case GENERATOR_ERROR:
			return new HDLAdvise(problem, "The generator contains an error", problem.info, "Read the documentation for the specific generator");
		case GENERATOR_WARNING:
			return new HDLAdvise(problem, "The generator produced a warning", problem.info, "Read the documentation for the specific generator");
		case GENERATOR_INFO:
			return new HDLAdvise(problem, "The generator contains an information", problem.info);
		case GENERATOR_NOT_KNOWN: {
			String genName = ((HDLDirectGeneration) problem.node).getGeneratorID();
			Set<String> genIDs = HDLCore.getCompilerInformation().registeredGenerators.keySet();
			String generatorProposal = getMatchProposal(genIDs, "Generators", genName);
			return new HDLAdvise(problem, "The generator with the id: " + genName + " is not known",
					"The generator you have used is not known to the compiler. Maybe it was misspelled or it is not installed.", generatorProposal,
					"Check the installed generators of the compiler");
		}
		case INTERFACE_IN_PORT_NEVER_WRITTEN: {
			HDLVariable var = (HDLVariable) problem.context;
			HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) problem.node;
			return new HDLAdvise(problem, "No write access to the in port: " + var.getName() + " of the instance: " + hii.getVar().getName() + " detected",
					"It appears that the port " + var.getName()
							+ " is never written, altough it is marked as in port. If you don't write to it, it will have the default value of 0",
					"Write a meaningful value to the port", "Write a zero to it: " + hii.getVar().getName() + "." + var.getName() + " = 0;");
		}
		case INTERFACE_OUT_PORT_NEVER_READ: {
			HDLVariable var = (HDLVariable) problem.context;
			HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) problem.node;
			return new HDLAdvise(problem, "No read access to the out port: " + var.getName() + " of the instance: " + hii.getVar().getName() + " detected",
					"It appears that the port " + var.getName() + " is never read, altough it is marked as out port", "Do something with the port");
		}
		case INTERFACE_OUT_WRITTEN: {
			HDLVariable var = (HDLVariable) problem.context;
			HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) problem.node;
			return new HDLAdvise(problem, "The out port: " + var.getName() + " of the instance: " + hii.getVar().getName() + " is written", "It appears that the port "
					+ var.getName() + " is written to, altough it is marked as out port", "Remove the write access");
		}
		case INTERFACE_UNUSED_PORT: {
			HDLVariable var = (HDLVariable) problem.context;
			HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) problem.node;
			return new HDLAdvise(problem, "The port: " + var.getName() + " of the instance: " + hii.getVar().getName() + " is never read or written", "It appears that the port "
					+ var.getName() + " is neither read, nor written to. You might want to check wether this is intentional", "Remove the port if it is not necessary",
					"Do something useful with it");
		}
		case INTERNAL_SIGNAL_READ_BUT_NEVER_WRITTEN: {
			HDLVariable var = (HDLVariable) problem.node;
			return new HDLAdvise(problem, "The variable:" + var.getName() + " is read, but never written",
					"Only reading an internal variable does not make much sense as it needs to be written as well", "Write to it", "Remove it");
		}
		case INTERNAL_SIGNAL_WRITTEN_BUT_NEVER_READ: {
			HDLVariable var = (HDLVariable) problem.node;
			return new HDLAdvise(problem, "The variable:" + var.getName() + " is written, but never read",
					"Only writing an internal variable does not make much sense as it needs to be read as well", "Read from it", "Remove it");
		}
		case IN_PORT_CANT_REGISTER:
			return new HDLAdvise(problem, "A variable of direction in can not be declared a register",
					"It does not make much sense to declare a incoming variable as register as the data is directly coming from the outside", "Revmoed the register keyword");
		case IN_PORT_NEVER_READ: {
			HDLVariable var = (HDLVariable) problem.node;
			return new HDLAdvise(problem, "The variable:" + var.getName() + " is declared with direction in, but never read",
					"A variable with direction in should be used otherwise it is rather useless", "Remove it");
		}
		case MULTI_PROCESS_WRITE: {
			return new HDLAdvise(problem, "Only a single process can write to a variable",
					"Multiple writes to a variable will lead to a X in the VHDL simulation, it is thus not allowed. Reads from multiples process on the other hand are allowed",
					"Merge all write access to one process");
		}
		case NO_SUCH_FUNCTION: {
			HDLQualifiedName genName = ((HDLFunctionCall) problem.node).getNameRefName();
			return new HDLAdvise(problem, "The function:" + genName + " can not be found", "The function could not be found, maybe you misspelled it.", "Double check the name",
					"Check your imports");
		}
		case ONLY_ONE_CLOCK_ANNOTATION_ALLOWED:
			return new HDLAdvise(problem, "Only one variable is allowed to be annotated with the @clock annotation",
					"Declaring multiple variables as @clock does not make much sense as it is unclear which one should be used as subsitute for $clk",
					"Remove one @clock annotation");
		case ONLY_ONE_RESET_ANNOTATION_ALLOWED:
			return new HDLAdvise(problem, "Only one variable is allowed to be annotated with the @reset annotation",
					"Declaring multiple variables as @reset does not make much sense as it is unclear which one should be used as subsitute for $rst",
					"Remove one @reset annotation");
		case OUT_PORT_NEVER_WRITTEN:
			HDLVariable var = (HDLVariable) problem.node;
			return new HDLAdvise(problem, "The variable:" + var.getName() + " is read, but never written",
					"Only reading an internal variable does not make much sense as it needs to be written as well", "Write to it", "Remove it");

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
		case UNRESOLVED_FRAGMENT: {
			HDLUnresolvedFragment uf = (HDLUnresolvedFragment) problem.node;
			Set<String> funcIDs = HDLCore.getCompilerInformation().registeredFunctions.keySet();
			String functionProposal = getMatchProposal(funcIDs, "Functions", uf.getFrag());
			return new HDLAdvise(
					problem,
					"The fragment:" + problem.node + " can not be resolved",
					"Fragments are unrecognized references to something. It could be a function, an enum or a variable. The compiler was however unable to properly determine what it is.",
					"If you want to reference a variable check that the spelling is correct and that the variable is visible in this scope", functionProposal);
		}
		case UNRESOLVED_FUNCTION:
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
		case ARRAY_REFERENCE_TOO_MANY_DIMENSIONS:
			break;
		case ASSIGNEMENT_NOT_SUPPORTED:
			break;
		case ASSIGNMENT_CLIPPING_WILL_OCCUR:
			break;
		case ASSIGNMENT_NOT_ENUM:
			break;
		case ASSIGNMENT_NOT_PRIMITIVE:
			break;
		case ASSIGNMENT_TARGET_TOO_SMALL:
			break;
		case CONCAT_TYPE_NOT_ALLOWED:
			break;
		case SWITCH_CASE_NEEDS_WIDTH:
			break;
		case SWITCH_LABEL_DUPLICATE:
			break;
		case SWITCH_LABEL_NOT_CONSTANT:
			break;
		case SWITCH_MULTIPLE_DEFAULT:
			break;
		case SWITCH_NO_DEFAULT:
			break;
		case VARIABLE_KEYWORD_NAME:
			break;
		case VARIABLE_SAME_NAME:
			break;
		case VARIABLE_SAME_NAME_DIFFERENT_CASE:
			break;
		case VARIABLE_SCOPE_SAME_NAME:
			break;
		case VARIABLE_SCOPE_SAME_NAME_DIFFERENT_CASE:
			break;
		case INLINE_FUNCTION_NO_TYPE:
			break;
		case ARRAY_INDEX_NO_RANGE:
			break;
		case ARRAY_REFERENCE_TOO_FEW_DIMENSIONS_IN_EXPRESSION:
			break;
		case ARRAY_WRITE_MULTI_DIMENSION:
			break;
		case ASSIGNMENT_ENUM_NOT_WRITABLE:
			break;
		case ARRAY_ASSIGNMENT_NOT_SAME_DIMENSIONS:
			break;
		case ARRAY_DIMENSIONS_NOT_CONSTANT:
			break;
		case SWITCH_LABEL_WRONG_ENUM:
			break;
		case BIT_ACCESS_NOT_POSSIBLE_ON_TYPE:
			break;
		case CONSTANT_PORT_CANT_REGISTER:
			break;
		case GLOBAL_CANT_REGISTER:
			break;
		case RANGE_NOT_DOWN:
			break;
		case RANGE_NOT_UP:
			break;
		case RANGE_OVERLAP:
			break;
		case UNKNOWN_RANGE:
			break;
		}
		return null;
	}

	private static String getMatchProposal(Set<String> funcIDs, String type, String name) {
		Score[] topMatches = LevenshteinDistance.getTopMatches(name, true, funcIDs.toArray(new String[funcIDs.size()]));
		StringBuilder matchSolution = new StringBuilder();
		matchSolution.append(type + " that you might have meant: ");
		for (int i = 0; i < Math.min(3, topMatches.length); i++) {
			Score score = topMatches[i];
			if (i != 0)
				matchSolution.append(", ");
			matchSolution.append('@').append(score.string);
		}
		String functionProposal = matchSolution.toString();
		return functionProposal;
	}

}

/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for
 *     reasonable and customary use in describing the origin of the Work.
 *
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package org.pshdl.model.validation.builtin;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLArgument;
import org.pshdl.model.HDLDirectGeneration;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLInterfaceInstantiation;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLManip.HDLManipType;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLUnresolvedFragmentFunction;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.extensions.ScopingExtension;
import org.pshdl.model.utils.HDLCore;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.HDLQuery;
import org.pshdl.model.utils.internal.LevenshteinDistance;
import org.pshdl.model.utils.internal.LevenshteinDistance.Score;
import org.pshdl.model.validation.HDLValidator.HDLAdvise;
import org.pshdl.model.validation.Problem;

import com.google.common.collect.Range;
import com.google.common.collect.Sets;

public class BuiltInAdvisor {

	public static HDLAdvise advise(Problem problem) {
		switch ((ErrorCode) problem.code) {
		case ANNOTATION_INVALID:
			final String annoName = ((HDLAnnotation) problem.node).getName();
			return new HDLAdvise(
					problem,
					"The String you provided is incorrect for this annotation: " + problem.info,
					"Some annotations can have additional information. Those are stated as a String in double quotes. The interpretation of this String is depending on the Annotation. The String you provided however was not correct.",
					"Check the message for information what might be incorrect", "Check the documentation for the " + annoName + " Annotation");
		case ANNOTATION_UNKNOWN: {
			final String lastName = ((HDLAnnotation) problem.node).getName();
			final Set<String> knownAnnotations = HDLCore.getCompilerInformation().registeredAnnotations.keySet();
			final String annoProposal = getMatchProposal(knownAnnotations, "Annotations", lastName);
			return new HDLAdvise(problem, "The annotation: " + lastName + " is not known",
					"The annotation you have used is not known to the compiler. Maybe it was misspelled or it is not installed.", annoProposal,
					"Check the installed libraries of the compiler");
		}
		case BIT_ACCESS_NEGATIVE:
			return new HDLAdvise(problem, "The index of the bit access can only be negative",
					"When bits are accessed, the index has to be positive as negative indexes don't make sense", "Cast the index to uint");
		case ARRAY_INDEX_NEGATIVE:
			return new HDLAdvise(problem, "The index of the array can only be negative",
					"When an array is accessed, the index has to be positive as negative indexes don't make sense", "Cast the index to uint");
		case BIT_ACCESS_OUT_OF_BOUNDS: {
			final Range<BigInteger> accessRange = problem.getMeta(BuiltInValidator.ACCESS_RANGE);
			final Range<BigInteger> arrayRange = problem.getMeta(BuiltInValidator.ARRAY_RANGE);
			return new HDLAdvise(problem, "The bit access exceeds the variables' capacity", "Valid access index for the variable are: " + arrayRange
					+ " while the index has a range of: " + accessRange + ". These don't overlap which means that the index will never be valid");
		}
		case ARRAY_INDEX_OUT_OF_BOUNDS: {
			final Range<BigInteger> accessRange = problem.getMeta(BuiltInValidator.ACCESS_RANGE);
			final Range<BigInteger> arrayRange = problem.getMeta(BuiltInValidator.ARRAY_RANGE);
			return new HDLAdvise(problem, "The array index exceeds its capacity", "Valid access index for the array are: " + arrayRange + " while the index has a range of: "
					+ accessRange + ". These don't overlap which means that the index will never be valid");
		}
		case BIT_ACCESS_POSSIBLY_NEGATIVE: {
			final Range<BigInteger> accessRange = problem.getMeta(BuiltInValidator.ACCESS_RANGE);
			final Range<BigInteger> arrayRange = problem.getMeta(BuiltInValidator.ARRAY_RANGE);
			String[] solutions;
			if (!arrayRange.isConnected(accessRange)) {
				solutions = new String[] { "Cast the index to uint with:(uint<>)" + problem.node };
			} else {
				solutions = new String[] {
						"Cast the index to uint with:(uint<>)" + problem.node,
						"Manually declare a range for the index with the @range(\"" + arrayRange.lowerEndpoint() + ";" + arrayRange.upperEndpoint()
								+ "\") annotation to define a range" };
			}
			return new HDLAdvise(problem, "The bit access could possibly become negative", "The given bit access has a possible negative value (" + accessRange.lowerEndpoint()
					+ "), even tough it does not need to become negative by design, it would be possible. This might indicate a programming error", solutions);
		}
		case ARRAY_INDEX_POSSIBLY_NEGATIVE: {
			final Range<BigInteger> accessRange = problem.getMeta(BuiltInValidator.ACCESS_RANGE);
			final Range<BigInteger> arrayRange = problem.getMeta(BuiltInValidator.ARRAY_RANGE);
			String[] solutions;
			if (!arrayRange.isConnected(accessRange)) {
				solutions = new String[] { "Cast the index to uint with:(uint)" + problem.node };
			} else {
				solutions = new String[] {
						"Cast the index to uint with:(uint)" + problem.node,
						"Manually declare a range for the index with the @range(\"" + arrayRange.lowerEndpoint() + ";" + arrayRange.upperEndpoint()
								+ "\") annotation to define a range" };
			}
			return new HDLAdvise(problem, "The array index could possibly become negative", "The given array index has a possible negative value (" + accessRange.lowerEndpoint()
					+ "), even tough it does not need to become negative by design, it would be possible. This might indicate a programming error", solutions);
		}
		case BIT_ACCESS_POSSIBLY_OUT_OF_BOUNDS: {
			final Range<BigInteger> accessRange = problem.getMeta(BuiltInValidator.ACCESS_RANGE);
			final Range<BigInteger> arrayRange = problem.getMeta(BuiltInValidator.ARRAY_RANGE);
			final Range<BigInteger> commonRange = arrayRange.intersection(accessRange);
			return new HDLAdvise(problem, "The bit access can exceed the variables' capacity", "The given bit access has a possible range of:" + accessRange
					+ " while the highest index of the variable is " + arrayRange.upperEndpoint(), "Limit the possible range by masking with &",
					"Downcast the index to a suitable size", "Use the @range(\"" + commonRange.lowerEndpoint() + ";" + commonRange.upperEndpoint()
							+ "\") Annotation to indicate the expected range");
		}
		case ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS: {
			final Range<BigInteger> accessRange = problem.getMeta(BuiltInValidator.ACCESS_RANGE);
			final Range<BigInteger> arrayRange = problem.getMeta(BuiltInValidator.ARRAY_RANGE);
			final Range<BigInteger> commonRange = arrayRange.intersection(accessRange);
			return new HDLAdvise(problem, "The array index can exceed its capacity", "The given array index has a possible range of:" + accessRange
					+ " while the highest index of the array is " + arrayRange.upperEndpoint(), "Limit the possible range by masking with &",
					"Downcast the index to a suitable size", "Use the @range(\"" + commonRange.lowerEndpoint() + ";" + commonRange.upperEndpoint()
							+ "\") Annotation to indicate the expected range");
		}
		case ARRAY_REFERENCE_NOT_SAME_DIMENSIONS:
			return new HDLAdvise(problem, "The dimensions of assignment do not match", "When an array is assigned to another array, the size of the dimension need to match.",
					"Check that the right handside of the array has the same dimension(s) as the left handside");
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
			return new HDLAdvise(problem, "This equality is always false.", "This behaviour might be desired, but it might also indicate a programming error.");
		case EQUALITY_ALWAYS_TRUE:
			return new HDLAdvise(problem, "This equality is always true.", "This behaviour might be desired, but it might also indicate a programming error.");
		case FOR_LOOP_RANGE_NOT_CONSTANT:
			return new HDLAdvise(
					problem,
					"The range of this for loop can not be statically determined",
					"A for loop need to have known boundaries. Those boundaries can be derived from parameters or constants. Variables however are not allowed as their value is not known statically.",
					"Instead of using variables, use parameter and constants");
		case GENERATOR_ERROR:
			return new HDLAdvise(problem, "The generator contains an error", problem.info, "Read the documentation for the specific generator");
		case GENERATOR_WARNING:
			return new HDLAdvise(problem, "The generator produced a warning", problem.info, "Read the documentation for the specific generator");
		case GENERATOR_INFO:
			return new HDLAdvise(problem, "The generator contains an information", problem.info);
		case GENERATOR_NOT_KNOWN: {
			final String genName = ((HDLDirectGeneration) problem.node).getGeneratorID();
			final Set<String> genIDs = HDLCore.getCompilerInformation().registeredGenerators.keySet();
			final String generatorProposal = getMatchProposal(genIDs, "Generators", genName);
			return new HDLAdvise(problem, "The generator with the id: " + genName + " is not known",
					"The generator you have used is not known to the compiler. Maybe it was misspelled or it is not installed.", generatorProposal,
					"Check the installed generators of the compiler");
		}
		case INTERFACE_IN_PORT_NEVER_WRITTEN: {
			final HDLVariable var = (HDLVariable) problem.context;
			final HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) problem.node;
			return new HDLAdvise(problem, "No write access to the in port: " + var.getName() + " of the instance: " + hii.getVar().getName() + " detected",
					"It appears that the port " + var.getName()
							+ " is never written, altough it is marked as in port. If you don't write to it, it will have the default value of 0",
					"Write a meaningful value to the port", "Write a zero to it: " + hii.getVar().getName() + "." + var.getName() + " = 0;");
		}
		case INTERFACE_OUT_PORT_NEVER_READ: {
			final HDLVariable var = (HDLVariable) problem.context;
			final HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) problem.node;
			return new HDLAdvise(problem, "No read access to the out port: " + var.getName() + " of the instance: " + hii.getVar().getName() + " detected",
					"It appears that the port " + var.getName() + " is never read, altough it is marked as out port", "Do something with the port");
		}
		case INTERFACE_OUT_WRITTEN: {
			final HDLVariable var = (HDLVariable) problem.context;
			final HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) problem.node;
			return new HDLAdvise(problem, "The out port: " + var.getName() + " of the instance: " + hii.getVar().getName() + " is written", "It appears that the port "
					+ var.getName() + " is written to, altough it is marked as out port", "Remove the write access");
		}
		case INTERFACE_UNUSED_PORT: {
			final HDLVariable var = (HDLVariable) problem.context;
			final HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) problem.node;
			return new HDLAdvise(problem, "The port: " + var.getName() + " of the instance: " + hii.getVar().getName() + " is never read or written", "It appears that the port "
					+ var.getName() + " is neither read, nor written to. You might want to check wether this is intentional", "Remove the port if it is not necessary",
					"Do something useful with it");
		}
		case INTERNAL_SIGNAL_READ_BUT_NEVER_WRITTEN: {
			final HDLVariable var = (HDLVariable) problem.node;
			return new HDLAdvise(problem, "The variable:" + var.getName() + " is read, but never written",
					"Only reading an internal variable does not make much sense as it needs to be written as well", "Write to it", "Remove it");
		}
		case INTERNAL_SIGNAL_WRITTEN_BUT_NEVER_READ: {
			final HDLVariable var = (HDLVariable) problem.node;
			return new HDLAdvise(problem, "The variable:" + var.getName() + " is written, but never read",
					"Only writing an internal variable does not make much sense as it needs to be read as well", "Read from it", "Remove it");
		}
		case IN_PORT_CANT_REGISTER:
			return new HDLAdvise(problem, "A variable of direction in can not be declared a register",
					"It does not make much sense to declare a incoming variable as register as the data is directly coming from the outside", "Revmoed the register keyword");
		case IN_PORT_NEVER_READ: {
			final HDLVariable var = (HDLVariable) problem.node;
			return new HDLAdvise(problem, "The variable:" + var.getName() + " is declared with direction in, but never read",
					"A variable with direction in should be used otherwise it is rather useless", "Remove it");
		}
		case MULTI_PROCESS_WRITE: {
			return new HDLAdvise(problem, "Only a single process can write to a variable",
					"Multiple writes to a variable will lead to a X in the VHDL simulation, it is thus not allowed. Reads from multiples process on the other hand are allowed",
					"Merge all write access to one process");
		}
		case NO_SUCH_FUNCTION: {
			final HDLQualifiedName genName = ((HDLFunctionCall) problem.node).getNameRefName();
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
		case OUT_PORT_NEVER_WRITTEN: {
			final HDLVariable var = (HDLVariable) problem.node;
			return new HDLAdvise(problem, "The variable:" + var.getName() + " is read, but never written",
					"Only reading an internal variable does not make much sense as it needs to be written as well", "Write to it", "Remove it");
		}
		case PARAMETER_OR_CONSTANT_NEVER_READ: {
			final HDLVariable var = (HDLVariable) problem.node;
			return new HDLAdvise(problem, "The constant:" + var.getName() + " is never read", "Declaring constants and not reading them is not very useful.", "Use it", "Remove it");
		}
		case UNRESOLVED_ENUM: {
			return new HDLAdvise(problem, "The enum:" + problem.node + " can not be resolved",
					"Either the referenced enum does not declare an enum with that name, or the referenced enum could not be found.", "Check that the name is correct",
					"Check that the type is imported or fully referenced");
		}
		case UNRESOLVED_INTERFACE:
			return new HDLAdvise(problem, "The interface:" + problem.node + " can not be resolved", "An interface with that name can not be found",
					"Check that the name is correct", "Check that the type is imported or fully referenced");
		case UNRESOLVED_REFERENCE:
			return new HDLAdvise(problem, "The reference:" + problem.node + " can not be resolved", "A type/function/variable with that name can not be found",
					"Check that the name is correct", "Check that the type is imported or fully referenced");
		case UNRESOLVED_TYPE:
			return new HDLAdvise(problem, "The type:" + problem.node + " can not be resolved", "A type with that name can not be found", "Check that the name is correct",
					"Check that the type is imported or fully referenced");
		case UNRESOLVED_VARIABLE:
			return new HDLAdvise(problem, "The variable:" + problem.node + " can not be resolved", "A variable with that name can not be found", "Check that the name is correct",
					"If it is a global constant, check that it is either imported, or fully qualified");
		case UNRESOLVED_FRAGMENT: {
			final HDLUnresolvedFragment uf = (HDLUnresolvedFragment) problem.node;
			String similiarProposal;
			if (uf instanceof HDLUnresolvedFragmentFunction) {
				final Set<String> funcIDs = HDLCore.getCompilerInformation().registeredFunctions.keySet();
				similiarProposal = getMatchProposal(funcIDs, "Functions", uf.getFrag());
			} else {
				final Set<String> vars = collectVariables(uf);
				similiarProposal = getMatchProposal(vars, "Variables", uf.getFrag());
			}
			return new HDLAdvise(
					problem,
					"The fragment:" + problem.node + " can not be resolved",
					"Fragments are unrecognized references to something. It could be a function, an enum or a variable. The compiler was however unable to properly determine what it is.",
					"If you want to reference a variable check that the spelling is correct and that the variable is visible in this scope", similiarProposal);
		}
		case UNRESOLVED_FUNCTION:
			final HDLFunctionCall fc = (HDLFunctionCall) problem.node;
			final Set<String> funcIDs = HDLCore.getCompilerInformation().registeredFunctions.keySet();
			final String functionProposal = getMatchProposal(funcIDs, "Functions", fc.getNameRefName().toString());
			return new HDLAdvise(problem, "The function:" + problem.node + " can not be resolved", "A function with that name can not be found", functionProposal,
					"Check that the function is imported or fully referenced");
		case UNSUPPORTED_TYPE_FOR_OP:
			switch (problem.node.getClassType()) {
			case HDLManip:
				final HDLManip manip = (HDLManip) problem.node;
				final HDLManipType type = manip.getType();
				switch (type) {
				case ARITH_NEG:
					return new HDLAdvise(problem, problem.info, "The arithmetic negation is not available for non numeric types. It is only support for int/uint",
							"Cast either side to uint<?>/int<?>");
				case BIT_NEG:
					return new HDLAdvise(problem, problem.info, "The bit negation is not available for non bit types. It is only support for int/uint/bit",
							"Cast either side to uint<?>/int<?>/bit<?>");
				case LOGIC_NEG:
					return new HDLAdvise(problem, problem.info, "The logic negation is only allowed for bit and bool types.");
				case CAST:
					break;
				}
				return new HDLAdvise(problem, problem.info, "The operation " + type + " is not supported for the expresion.");
			case HDLArithOp:
				return new HDLAdvise(problem, problem.info, "Arithmetic operations require an interpretable value, they can not work on bit types",
						"Cast either side to uint<?>/int<?>");
			case HDLShiftOp:
				return new HDLAdvise(problem, problem.info, "Shift operations require an interpretable value on the right hand side, they can not work on bit types",
						"Cast the right hand-side to uint");
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
			return new HDLAdvise(problem, "The reference contains more array dimensions than the declared type",
					"A variable that is declared with just one dimension, can not be access with two or more",
					"Check that the declared type has the expected number of dimensione", "Remove one of more array acceses");
		case ASSIGNMENT_NOT_SUPPORTED:
			return new HDLAdvise(
					problem,
					problem.info,
					"Some types can not be automatically converted, you thus need to cast the right hand side explicitly.\nUnintended conversions can happen for example if you access the bits of an int/uint the result will be bit, or the result of concatenation is also bit.",
					"Convert the type explicitly by casting it", "Check that the assignment target has the expected type");
		case ASSIGNMENT_CLIPPING_WILL_OCCUR:
			return new HDLAdvise(problem, "The assigned target has fewer bits than the assigned value", "The assigned value will be truncated", "Widen the assignment target",
					"Explicitly down cast the assigned value");
		case ASSIGNMENT_NOT_ENUM:
			return new HDLAdvise(problem, "Assigned value is not an enum", "Enums can only accept the same type of enum.", "Assign a proper enum");
		case ASSIGNMENT_NOT_PRIMITIVE:
			return new HDLAdvise(problem, "Assigned value is not a primitive", "Primitives like uint, int and bit can only be assigned other primitives", "");
		case CONCAT_TYPE_NOT_ALLOWED:
			return new HDLAdvise(problem, "Only variables with known width are allowed in concatenations",
					"Literals and primitives without width don't have a known width, they can thus not be used to be concatenated as the resulting size would be unclear",
					"Explicitly cast the expresion to give it a known width example (bit<18>)" + problem.node);
		case SWITCH_CASE_NEEDS_WIDTH:
			return new HDLAdvise(
					problem,
					"The expression used for a switch needs to have a fixed width",
					"Switch expression can not work on epxressions which do not have a known width with the exception of enums. This width needs to be constant, parameterized width are also not allowed.",
					"Cast the expression to a constant width");
		case SWITCH_LABEL_DUPLICATE:
			return new HDLAdvise(problem, "Duplicate switch case value", "Each case within a switch must have a unique value, duplicates are not allowed",
					"Remove or merge the offending case");
		case SWITCH_LABEL_NOT_CONSTANT:
			return new HDLAdvise(problem, "Switch cases need to have a constant value", "", "Ensure that the used case value is constant");
		case SWITCH_MULTIPLE_DEFAULT:
			return new HDLAdvise(problem, "Switch case does not contain more than one default case", "All switch statements require exactly one default case.",
					"Remove one default case");
		case SWITCH_NO_DEFAULT:
			return new HDLAdvise(problem, "Switch case does not contain a default case",
					"All switch statements require a default case. This is to avoid missed cases which lead to an incomplete description", "Add a default case");
		case VARIABLE_KEYWORD_NAME:
			return new HDLAdvise(problem, "The variable name is a keyword in PSHDL", "Keywords can not be used as variable names as the parser would fail", "Rename the variable");
		case VARIABLE_SAME_NAME:
			return new HDLAdvise(problem, "A variable with this name already exists", "The compiler can not tell those variables apart as they have the exact same name",
					"Rename either variable");
		case VARIABLE_SAME_NAME_DIFFERENT_CASE: {
			final HDLVariable other = (HDLVariable) problem.context;
			final HDLVariable var = (HDLVariable) problem.node;
			return new HDLAdvise(problem, "The variable " + var.getName() + " already exists in a slightly different case:" + other.getName(),
					"When compiling to VHDL, a case insensitive language, a name collision will occour", "Rename either variable");
		}
		case VARIABLE_SCOPE_SAME_NAME:
			return new HDLAdvise(problem, "A variable with this name already exists", "In a scope above this scope a similar named variable is already declared",
					"Rename either variable");
		case VARIABLE_SCOPE_SAME_NAME_DIFFERENT_CASE: {
			final HDLVariable other = (HDLVariable) problem.context;
			final HDLVariable var = (HDLVariable) problem.node;
			return new HDLAdvise(problem, "The variable " + var.getName() + " already exists in a slightly different case: " + other.getName(),
					"When compiling to VHDL, a case insensitive language, a name collision will occour", "Rename either variable");
		}
		case INLINE_FUNCTION_NO_TYPE:
			return new HDLAdvise(problem, "Can not determine (return) type of: " + problem.node,
					"This message should not be displayed to a user, so if you see this, please write a bug report about it", "File a bug report");
		case ARRAY_INDEX_NO_RANGE:
			return new HDLAdvise(problem, "The possible value range used for accessing the array can not be determined",
					"In order to determine whether an array index is exceeding the declared array boundaries, a possible range for the expression needs to be computed. This is not possible for: "
							+ problem.node);
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
			return new HDLAdvise(problem, "This type does not support bit acess", "Only primitive types like int.uint and bit of any width allow access to their bits",
					"Check that you are accesssing the correct variable");
		case CONSTANT_PORT_CANT_REGISTER:
			return new HDLAdvise(problem, "Constants can not be a register", "A constant can not be of type register as this implies that it could be modified",
					"Remove the register keyword");
		case GLOBAL_CANT_REGISTER:
			return new HDLAdvise(problem, "A global variable can not be a register", "Global variables can only be constant",
					"Declare the variable as constant and remove the register keyword");
		case RANGE_NOT_DOWN:
			return new HDLAdvise(
					problem,
					"Range not of direction down",
					"All variables in PSHDL have their LSB at index 0, thus ranges that access bits need to have a 'down' direction. This means that the first index is bigger or equal to the second index",
					"Ensure that for a range {a:b} a >= b", "Annotate either index with a '@range' annotation to help the compiler");
		case RANGE_NOT_UP:
			return new HDLAdvise(problem, "Range not of direction up",
					"Loops do have a range that counts upwards. This means that the first index must be smaller or equal to the second index",
					"Ensure that for a range {a:b} a <= b");
		case RANGE_OVERLAP:
			return new HDLAdvise(
					problem,
					"The boundaries of the range expression can possibly overlap",
					"The compiler can not guarantee that for all combinations of inputs for the range results in an appropriate direction. As both sides are seen independently, ranges of type {A+x:A} can not be determined when A or x is a parameter.",
					"Convert ranges of type {A+x:A} to {A+:x}", "Convert ranges of type {A:A-x} to {A-:x}", "Annotate the variable with @range to guarantee a certain input range");
		case UNKNOWN_RANGE:
			return new HDLAdvise(problem, "Can not determine direction of range",
					"In order to ensure the correct direction of ranges, the compiler needs to know the bounds of each index. This was not possible",
					"Annotate the index variables with '@range' annotations");
		case SUBSTITUTE_FUNCTION_NO_TYPE:
			break;
		case GLOBAL_NOT_CONSTANT:
			return new HDLAdvise(problem, "Constants have to be constant", "The default value of this global constant is not constant");
		case FUNCTION_SAME_NAME:
			return new HDLAdvise(problem, "A function with this name already exists", "Function names have to be unique", "Rename this function or the other");
		case GLOBAL_VAR_SAME_NAME:
			return new HDLAdvise(problem, "A global variable with this name already exists", "Global variables have to have a unique name", "Rename this variable or the other");
		case TYPE_SAME_NAME:
			return new HDLAdvise(problem, "A type with this name already exists", "Type names have to be unique", "Rename this type or the other");
		case CONSTANT_WIDTH_MISMATCH:
			return new HDLAdvise(
					problem,
					"Constant expression is losing bits",
					"In most operations the type is determined from the left operand. The expression is cast to this type, which is smaller than the constant given. The result is a loss of information",
					"Cast the left-handside to a sufficently large type");
		case VARIABLE_NAME_NOT_RECOMMENDED:
			return new HDLAdvise(problem, "Variables starting with $ are not recommended",
					"Variables starting with $ are used for internal/automatically generated signals. Using such a variable may cause a name collision",
					"Rename your variable to something without $ in the beginning");
		case BOOL_NEGATE_NUMERIC_NOT_SUPPORTED:
			final HDLManip node = (HDLManip) problem.node;
			return new HDLAdvise(problem, "Logic negate does not support numbers",
					"The logical negate is intended for negating booleans, while it can also use a single bit, you probably want to use the binary invert ~.", "Use ~"
							+ node.getTarget());
		case CLOCK_NOT_BIT:
			return new HDLAdvise(problem, "The clock needs to be of type bit",
					"A clock can only be a single bit variable. It is not advised to use the output of a combinatorical logic as clock as this may impact timing",
					"Assign the value to a single bit variable");
		case CLOCK_UNKNOWN_WIDTH:
			return new HDLAdvise(problem, "The clock needs to be of type bit, but the width could not be determined",
					"A clock can only be a single bit variable. It is not advised to use the output of a combinatorical logic as clock as this may impact the timing.",
					"Assign the value to a single bit variable");
		case RESET_NOT_BIT:
			return new HDLAdvise(problem, "The reset needs to be of type bit",
					"A reset can only be a single bit variable. It is not advised to use the output of a combinatorical logic as reset as this may impact the timing",
					"Assign the value to a single bit variable");
		case RESET_UNKNOWN_WIDTH:
			return new HDLAdvise(problem, "The reset needs to be of type bit, but the width could not be determined",
					"A reset can only be a single bit variable. It is not advised to use the output of a combinatorical logic as clock as this may impact the timing.",
					"Assign the value to a single bit variable");
		case SWITCH_CASE_NEEDS_CONSTANT_WIDTH:
			return new HDLAdvise(
					problem,
					"The switch expression needs to have a known width",
					"The width of the switch expression needs to be constant. That means that it can not work on parameterized values as these are only known when the module is instanciated.",
					"Create a new signal with a fixed width and use that as the switch expression");
		case PARAMETER_NOT_FOUND: {
			final HDLArgument arg = (HDLArgument) problem.node;
			final HDLInterface hif = (HDLInterface) problem.context;
			final TreeSet<String> validNames = Sets.newTreeSet();
			final Collection<HDLVariableDeclaration> params = HDLQuery.select(HDLVariableDeclaration.class).from(hif).where(HDLVariableDeclaration.fDirection)
					.isEqualTo(HDLDirection.PARAMETER).getAll();
			for (final HDLVariableDeclaration port : params) {
				for (final HDLVariable var : port.getVariables()) {
					final String origName = var.getMeta(HDLInterfaceInstantiation.ORIG_NAME);
					if (origName == null) {
						validNames.add(var.getName());
					} else {
						validNames.add(origName);
					}

				}
			}
			return new HDLAdvise(problem, "The parameter '" + arg.getName() + "' does not exist",
					"In the interface that you instantiate, a variable declaration of type parameter with that name could not be found.", getMatchProposal(validNames,
							"Parameters", arg.getName()));
		}
		case REGISTER_UNKNOWN_ARGUMENT: {
			final HDLArgument arg = (HDLArgument) problem.node;
			return new HDLAdvise(problem, "The parameter '" + arg.getName() + "' does not exist", "", getMatchProposal(HDLRegisterConfig.VALID_PARAMS, "Parameters", arg.getName()));
		}
		case DIRECTION_NOT_ALLOWED_IN_SCOPE:
			return new HDLAdvise(problem, "Direction not allowed in sub scope",
					"A variable of direction 'in', 'out', 'inout' and 'param' is only allowed outside of scopes like for-loops, if-statements, etc..");
		case REGISTER_UNKNOWN_ARGUMENT_VALUE:
			return new HDLAdvise(problem, "Invalid value, allowed values are:" + problem.info, "Only the " + problem.info
					+ " are valid for the description of the register config.", "Change the expression to either of:" + problem.info);
		}
		return null;
	}

	private static String getMatchProposal(Set<String> possibleNames, String type, String name) {
		final Score[] topMatches = LevenshteinDistance.getTopMatches(name, true, possibleNames.toArray(new String[possibleNames.size()]));
		final StringBuilder matchSolution = new StringBuilder();
		matchSolution.append(type + " that you might have meant: ");
		for (int i = 0; i < Math.min(3, topMatches.length); i++) {
			final Score score = topMatches[i];
			if (i != 0) {
				matchSolution.append(", ");
			}
			matchSolution.append(score.string);
		}
		final String functionProposal = matchSolution.toString();
		return functionProposal;
	}

	private static Set<String> collectVariables(IHDLObject obj) {
		final HDLUnit unit = obj.getContainer(HDLUnit.class);
		if (unit != null) {
			final Set<String> res = Sets.newHashSet();
			final List<HDLVariable> vars = ScopingExtension.INST.doGetVariables(unit);
			for (final HDLVariable hdlVariable : vars) {
				res.add(hdlVariable.getName());
			}
			return res;
		}
		return Sets.newHashSet();
	}
}

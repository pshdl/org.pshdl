package de.tuhh.ict.pshdl.model.validation.builtin;

import java.math.*;
import java.util.*;
import java.util.Map.Entry;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.LevenshteinDistance.Score;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.validation.HDLValidator.HDLAdvise;
import de.tuhh.ict.pshdl.model.validation.*;

public class BuiltInValidator implements IHDLValidator {

	public static String[] PSHDL_KEYWORDS = new String[] { "bit", "out", "string", "switch", "include", "process", "for", "function", "import", "else", "extends", "native",
			"package", "testbench", "int", "if", "in", "default", "enum", "const", "module", "inline", "generate", "bool", "simulation", "uint", "case", "inout", "substitute",
			"param", "register", "interface" };
	public final static Set<String> keywordSet;
	static {
		keywordSet = new HashSet<String>();
		for (String keyword : PSHDL_KEYWORDS) {
			keywordSet.add(keyword);
		}
	}

	public static enum IntegerMeta implements MetaAccess<Integer> {
		READ_COUNT, WRITE_COUNT, ACCESS;

		@Override
		public boolean inherit() {
			return true;
		}
	}

	@Override
	public void check(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		// TODO find a way to distinguish between context dependent problems and
		// others
		try {
			RWValidation.checkVariableUsage(pkg, problems);
			checkFunctionCalls(pkg, problems, hContext);
			pkg = Insulin.inlineFunctions(pkg);
			pkg = Insulin.setParameterOnInstance(pkg);
			checkVariableNaming(pkg, problems);
			checkClockAndResetAnnotation(pkg, problems);
			checkConstantBoundaries(pkg, problems, hContext);
			checkArrayBoundaries(pkg, problems, hContext);
			checkConstantEquals(pkg, problems, hContext);
			// TODO Validate value ranges, check for 0 divide
			// TODO Check for POW only power of 2
			checkCombinedAssignment(pkg, problems, hContext);
			checkAnnotations(pkg, problems, hContext);
			checkType(pkg, problems, hContext);
			checkProessWrite(pkg, problems, hContext);
			checkGenerators(pkg, problems, hContext);
			checkInputReg(pkg, problems, hContext);
			// TODO Validate bitWidth mismatch
			// TODO Check bit access direction
			// TODO Multi-bit Write only for Constants
			// TODO check for signals named clk or rst and warn about the
			// collision
			// TODO check for proper variable naming
			// TODO check for valid parameter
			// TODO Check for unconstrained arrays in switch case
			// TODO Check for covered cases in switch clause
			// TODO Check for only one default clause
			// TODO Check for Range direction
			// TODO Type checking!
			// TODO HDLConcat need known width
			// TODO Check for combinatorical loop.
			// TODO Check for multiple assignment in same Scope
			// TODO No processes in Module
			// TODO no I/O variables in block
			// TODO Switch expression needs to have width (no natural etc..)
			// TODO no Registers in Testbench
			// TODO warn for name collision in generators
			// TODO check for same name with different case
			// TODO Out port array need to be constant
			// TODO Check for bit access on left assignment side when right is
			// matching

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkVariableNaming(HDLPackage pkg, Set<Problem> problems) {
		Set<HDLVariable> vars = pkg.getAllObjectsOf(HDLVariable.class, true);
		Map<String, HDLVariable> nameMap = new HashMap<String, HDLVariable>();
		for (HDLVariable hdlVariable : vars) {
			HDLQualifiedName fullName = hdlVariable.getFullName();
			if (keywordSet.contains(fullName.getLastSegment())) {
				problems.add(new Problem(ErrorCode.VARIABLE_KEYWORD_NAME, hdlVariable));
			}
			HDLVariable put = nameMap.put(fullName.toString().toLowerCase(), hdlVariable);
			if (put != null) {
				HDLQualifiedName otherName = put.getFullName();
				if (otherName.equals(fullName))
					problems.add(new Problem(ErrorCode.VARIABLE_SAME_NAME, hdlVariable, put));
				else
					problems.add(new Problem(ErrorCode.VARIABLE_SAME_NAME_DIFFERENT_CASE, hdlVariable, put));
			}
		}
		for (Entry<String, HDLVariable> entry : nameMap.entrySet()) {
			HDLVariable var = entry.getValue();
			HDLQualifiedName fullName = var.getFullName();
			String lastSegment = fullName.getLastSegment();
			HDLQualifiedName type = fullName.getTypePart();
			HDLQualifiedName localPart = fullName.getLocalPart().skipLast(1);
			for (int i = 0; i < localPart.length; i++) {
				HDLQualifiedName scoped = localPart.skipLast(i + 1).append(lastSegment);
				HDLQualifiedName newName = type.append(scoped);
				HDLVariable otherVar = nameMap.get(newName.toString().toLowerCase());
				if (otherVar != null) {
					HDLQualifiedName otherName = otherVar.getFullName();
					if (otherName.equals(newName))
						problems.add(new Problem(ErrorCode.VARIABLE_SCOPE_SAME_NAME, var, otherVar));
					else
						problems.add(new Problem(ErrorCode.VARIABLE_SCOPE_SAME_NAME_DIFFERENT_CASE, var, otherVar));
				}
			}
		}
	}

	private static void checkInputReg(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Collection<HDLVariableDeclaration> allIns = HDLQuery.select(HDLVariableDeclaration.class).from(pkg).where(HDLVariableDeclaration.fDirection).isEqualTo(HDLDirection.IN)
				.getAll();
		for (HDLVariableDeclaration inPort : allIns) {
			if (inPort.getRegister() != null)
				problems.add(new Problem(ErrorCode.IN_PORT_CANT_REGISTER, inPort));
		}

	}

	private static void checkGenerators(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Set<HDLDirectGeneration> generators = unit.getAllObjectsOf(HDLDirectGeneration.class, true);
		for (HDLDirectGeneration hdg : generators) {
			HDLGenerators.validate(hdg, problems, getContext(hContext, hdg));
		}
	}

	private static void checkFunctionCalls(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Set<HDLFunctionCall> functions = unit.getAllObjectsOf(HDLFunctionCall.class, true);
		for (HDLFunctionCall function : functions) {
			HDLTypeInferenceInfo info = HDLFunctions.getInferenceInfo(function);
			if (info == null) {
				try {
					HDLFunction f = function.resolveName();
					if (f == null)
						problems.add(new Problem(ErrorCode.NO_SUCH_FUNCTION, function));
				} catch (Exception e) {
					problems.add(new Problem(ErrorCode.NO_SUCH_FUNCTION, function));
				}
			}
		}
	}

	private static void checkProessWrite(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Set<HDLVariable> vars = unit.getAllObjectsOf(HDLVariable.class, true);
		for (HDLVariable var : vars) {
			if (var.hasMeta(RWValidation.BlockMetaClash.clash)) {
				problems.add(new Problem(ErrorCode.MULTI_PROCESS_WRITE, var));
			}
		}
	}

	private static void checkType(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Set<HDLOpExpression> ops = unit.getAllObjectsOf(HDLOpExpression.class, true);
		for (HDLOpExpression ope : ops) {
			if (skipExp(ope))
				continue;
			HDLTypeInferenceInfo info = null;
			switch (ope.getClassType()) {
			case HDLArithOp:
				info = HDLPrimitives.getInstance().getArithOpType((HDLArithOp) ope);
				break;
			case HDLBitOp:
				info = HDLPrimitives.getInstance().getBitOpType((HDLBitOp) ope);
				break;
			case HDLShiftOp:
				info = HDLPrimitives.getInstance().getShiftOpType((HDLShiftOp) ope);
				break;
			case HDLEqualityOp:
				info = HDLPrimitives.getInstance().getEqualityOpType((HDLEqualityOp) ope);
				break;
			default:
				throw new IllegalArgumentException("Did not expect class:" + ope.getClassType());
			}
			if (info == null)
				throw new IllegalArgumentException("Info should not be null");
			if (info.error != null)
				problems.add(new Problem(ErrorCode.UNSUPPORTED_TYPE_FOR_OP, ope, info.error));
		}
	}

	private static void checkAnnotations(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Set<HDLAnnotation> annos = unit.getAllObjectsOf(HDLAnnotation.class, true);
		for (HDLAnnotation hdlAnnotation : annos) {
			Problem[] p = HDLAnnotations.validate(hdlAnnotation);
			for (Problem problem : p) {
				problems.add(problem);
			}
		}
	}

	private static void checkCombinedAssignment(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Collection<HDLAssignment> all = HDLQuery.select(HDLAssignment.class).from(unit).where(HDLAssignment.fType).isNotEqualTo(HDLAssignment.HDLAssignmentType.ASSGN).getAll();
		for (HDLAssignment ass : all) {
			if (ass.getLeft().resolveVar().getRegisterConfig() == null) {
				problems.add(new Problem(ErrorCode.COMBINED_ASSIGNMENT_NOT_ALLOWED, ass));
			}
		}
	}

	private static void checkConstantEquals(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Collection<HDLEqualityOp> equalities = unit.getAllObjectsOf(HDLEqualityOp.class, true);
		for (HDLEqualityOp op : equalities) {
			if (skipExp(op))
				continue;
			BigInteger res = op.constantEvaluate(getContext(hContext, op));
			if (res != null) {
				if (res.equals(BigInteger.ONE))
					problems.add(new Problem(ErrorCode.EQUALITY_ALWAYS_TRUE, op));
				else
					problems.add(new Problem(ErrorCode.EQUALITY_ALWAYS_FALSE, op));
			}
		}
	}

	public static boolean skipExp(IHDLObject op) {
		return (op.getContainer(HDLInlineFunction.class) != null) || (op.getContainer(HDLSubstituteFunction.class) != null);
	}

	private static void checkConstantBoundaries(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Collection<HDLVariableDeclaration> constants = HDLQuery.select(HDLVariableDeclaration.class).from(unit).where(HDLVariableDeclaration.fDirection)
				.isEqualTo(HDLDirection.CONSTANT).or(HDLDirection.PARAMETER);
		for (HDLVariableDeclaration hvd : constants) {
			for (HDLVariable var : hvd.getVariables()) {
				if (var.getDefaultValue() == null) {
					problems.add(new Problem(ErrorCode.CONSTANT_NEED_DEFAULTVALUE, var));
				} else {
					HDLExpression def = var.getDefaultValue();
					BigInteger constant = def.constantEvaluate(getContext(hContext, var));
					if (constant == null) {
						if (!(def instanceof HDLLiteral))
							problems.add(new Problem(ErrorCode.CONSTANT_DEFAULT_VALUE_NOT_CONSTANT, def, var, null));

					}
				}
			}
		}
		Collection<HDLForLoop> forLoops = unit.getAllObjectsOf(HDLForLoop.class, true);
		for (HDLForLoop hdlForLoop : forLoops) {
			for (HDLRange r : hdlForLoop.getRange()) {
				BigInteger evalTo = r.getTo().constantEvaluate(getContext(hContext, r));
				if (evalTo == null) {
					problems.add(new Problem(ErrorCode.FOR_LOOP_RANGE_NOT_CONSTANT, r.getTo(), r, null));
				}
				if (r.getFrom() != null) {
					BigInteger evalFrom = r.getFrom().constantEvaluate(getContext(hContext, r));
					if (evalFrom == null) {
						problems.add(new Problem(ErrorCode.FOR_LOOP_RANGE_NOT_CONSTANT, r.getFrom(), r, null));
					}
				}
			}
		}
	}

	private static void checkArrayBoundaries(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Collection<HDLVariableRef> asss = unit.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : asss) {
			if (skipExp(ref))
				continue;
			compareBoundaries(problems, hContext, ref, ref.resolveVar().getDimensions(), ref.getArray());
			if (ref instanceof HDLInterfaceRef) {
				HDLInterfaceRef hir = (HDLInterfaceRef) ref;
				HDLVariable var = hir.resolveHIf();
				compareBoundaries(problems, hContext, ref, var.getDimensions(), hir.getIfArray());
			}
		}
	}

	private static void compareBoundaries(Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext, HDLVariableRef ref, ArrayList<HDLExpression> dimensions,
			ArrayList<HDLExpression> array) {
		if (dimensions.size() < array.size()) {
			problems.add(new Problem(ErrorCode.ARRAY_REFERENCE_TOO_MANY_DIMENSIONS, ref));
		} else {
			int dim = 0;
			for (HDLExpression arr : array) {
				HDLEvaluationContext context = getContext(hContext, arr);
				ValueRange accessRange = arr.determineRange(context);
				ValueRange arrayRange = dimensions.get(dim).determineRange(context);
				arrayRange = new ValueRange(BigInteger.ZERO, arrayRange.to.subtract(BigInteger.ONE));
				String info = "Expected value range:" + accessRange;
				if (accessRange.to.signum() < 0)
					problems.add(new Problem(ErrorCode.ARRAY_INDEX_NEGATIVE, arr, ref, info).addMeta("accessRange", accessRange).addMeta("arrayRange", arrayRange));
				else if (accessRange.from.signum() < 0)
					problems.add(new Problem(ErrorCode.ARRAY_INDEX_POSSIBLY_NEGATIVE, arr, ref, info).addMeta("accessRange", accessRange).addMeta("arrayRange", arrayRange));
				ValueRange commonRange = arrayRange.and(accessRange);
				if (commonRange == null)
					problems.add(new Problem(ErrorCode.ARRAY_INDEX_OUT_OF_BOUNDS, arr, ref, info).addMeta("accessRange", accessRange).addMeta("arrayRange", arrayRange));
				else if (accessRange.to.compareTo(arrayRange.to) > 0)
					problems.add(new Problem(ErrorCode.ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS, arr, ref, info).addMeta("accessRange", accessRange).addMeta("arrayRange", arrayRange));

				dim++;
			}
		}
	}

	private static HDLEvaluationContext getContext(Map<HDLQualifiedName, HDLEvaluationContext> hContext, IHDLObject var) {
		HDLUnit container = var.getContainer(HDLUnit.class);
		if (container == null)
			return null;
		HDLEvaluationContext hdlEvaluationContext = hContext.get(container.getFullName());
		return hdlEvaluationContext;
	}

	private static void checkClockAndResetAnnotation(HDLPackage unit, Set<Problem> problems) {
		Collection<HDLAnnotation> clocks = HDLQuery.select(HDLAnnotation.class).from(unit).where(HDLAnnotation.fName).isEqualTo(HDLBuiltInAnnotations.clock.toString()).getAll();
		if (clocks.size() > 1)
			for (HDLAnnotation anno : clocks) {
				problems.add(new Problem(ErrorCode.ONLY_ONE_CLOCK_ANNOTATION_ALLOWED, anno));
			}
		Collection<HDLAnnotation> resets = HDLQuery.select(HDLAnnotation.class).from(unit).where(HDLAnnotation.fName).isEqualTo(HDLBuiltInAnnotations.reset.toString()).getAll();
		if (resets.size() > 1)
			for (HDLAnnotation anno : resets) {
				problems.add(new Problem(ErrorCode.ONLY_ONE_RESET_ANNOTATION_ALLOWED, anno));
			}
	}

	@Override
	public Class<?> getErrorClass() {
		return ErrorCode.class;
	}

	@Override
	public HDLAdvise advise(Problem problem) {
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
		}
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
			return new HDLAdvise(problem, "The generator contains an error", problem.info, "Read the documentation for the specific generator");
		case GENERATOR_WARNING:
			return new HDLAdvise(problem, "The generator produced a warning", problem.info, "Read the documentation for the specific generator");
		case GENERATOR_INFO:
			return new HDLAdvise(problem, "The generator contains an information", problem.info);
		case GENERATOR_NOT_KNOWN: {
			String genName = ((HDLDirectGeneration) problem.node).getGeneratorID();
			Set<String> genIDs = HDLGenerators.getAllGeneratorIDs();
			Score[] topMatches = LevenshteinDistance.getTopMatches(genName, true, genIDs.toArray(new String[genIDs.size()]));
			StringBuilder matchSolution = new StringBuilder();
			matchSolution.append("Generators that you might have meant: ");
			for (int i = 0; i < Math.min(3, topMatches.length); i++) {
				Score score = topMatches[i];
				if (i != 0)
					matchSolution.append(", ");
				matchSolution.append('@').append(score.string);
			}
			return new HDLAdvise(problem, "The generator with the id: " + genName + " is not known",
					"The generator you have used is not known to the compiler. Maybe it was misspelled or it is not installed.", matchSolution.toString(),
					"Check the installed generators of the compiler");
		}
		case INTERFACE_IN_PORT_NEVER_WRITTEN: {
			HDLVariable var = (HDLVariable) problem.node;
			HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) problem.context;
			return new HDLAdvise(problem, "No write access to the in port: " + var.getName() + " of the instance: " + hii.getVar().getName() + " detected",
					"It appears that the port " + var.getName()
							+ " is never written, altough it is marked as in port. If you don't write to it, it will have the default value of 0",
					"Write a meaningful value to the port", "Write a zero to it: " + hii.getVar().getName() + "." + var.getName() + " = 0;");
		}
		case INTERFACE_OUT_PORT_NEVER_READ: {
			HDLVariable var = (HDLVariable) problem.node;
			HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) problem.context;
			return new HDLAdvise(problem, "No read access to the out port: " + var.getName() + " of the instance: " + hii.getVar().getName() + " detected",
					"It appears that the port " + var.getName() + " is never read, altough it is marked as out port", "Do something with the port");
		}
		case INTERFACE_OUT_WRITTEN: {
			HDLVariable var = (HDLVariable) problem.node;
			HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) problem.context;
			return new HDLAdvise(problem, "The out port: " + var.getName() + " of the instance: " + hii.getVar().getName() + " is written", "It appears that the port "
					+ var.getName() + " is written to, altough it is marked as out port", "Remove the write access");
		}
		case INTERFACE_UNUSED_PORT: {
			HDLVariable var = (HDLVariable) problem.node;
			HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) problem.context;
			return new HDLAdvise(problem, "The port: " + var.getName() + " of the instance: " + hii.getVar().getName() + " is never read or written", "It appears that the port "
					+ var.getName() + " is neither read, nor written to. You might want to check wether this is intentional", "Remove the port if it is not necessary",
					"Do something useful with it");
		}
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
		case ARRAY_REFERENCE_TOO_MANY_DIMENSIONS:
			break;
		case INLINE_FUNCTION_NO_TYPE:
			break;
		case UNRESOLVED_FUNCTION:
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	public String getName() {
		return "PSHDL Validator";
	}

}

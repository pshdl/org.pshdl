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

import static org.pshdl.model.extensions.FullNameExtension.fullNameOf;
import static org.pshdl.model.utils.HDLQuery.isEqualTo;
import static org.pshdl.model.validation.builtin.ErrorCode.ARRAY_ASSIGNMENT_NOT_SAME_DIMENSIONS;
import static org.pshdl.model.validation.builtin.ErrorCode.ARRAY_DIMENSIONS_NOT_CONSTANT;
import static org.pshdl.model.validation.builtin.ErrorCode.ARRAY_INDEX_NEGATIVE;
import static org.pshdl.model.validation.builtin.ErrorCode.ARRAY_INDEX_NO_RANGE;
import static org.pshdl.model.validation.builtin.ErrorCode.ARRAY_INDEX_OUT_OF_BOUNDS;
import static org.pshdl.model.validation.builtin.ErrorCode.ARRAY_INDEX_POSSIBLY_NEGATIVE;
import static org.pshdl.model.validation.builtin.ErrorCode.ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS;
import static org.pshdl.model.validation.builtin.ErrorCode.ARRAY_REFERENCE_NOT_SAME_DIMENSIONS;
import static org.pshdl.model.validation.builtin.ErrorCode.ARRAY_REFERENCE_TOO_FEW_DIMENSIONS_IN_EXPRESSION;
import static org.pshdl.model.validation.builtin.ErrorCode.ARRAY_REFERENCE_TOO_MANY_DIMENSIONS;
import static org.pshdl.model.validation.builtin.ErrorCode.ARRAY_WRITE_MULTI_DIMENSION;
import static org.pshdl.model.validation.builtin.ErrorCode.ASSIGNMENT_CLIPPING_WILL_OCCUR;
import static org.pshdl.model.validation.builtin.ErrorCode.ASSIGNMENT_ENUM_NOT_WRITABLE;
import static org.pshdl.model.validation.builtin.ErrorCode.ASSIGNMENT_NOT_ENUM;
import static org.pshdl.model.validation.builtin.ErrorCode.ASSIGNMENT_NOT_PRIMITIVE;
import static org.pshdl.model.validation.builtin.ErrorCode.ASSIGNMENT_NOT_SUPPORTED;
import static org.pshdl.model.validation.builtin.ErrorCode.BIT_ACCESS_NEGATIVE;
import static org.pshdl.model.validation.builtin.ErrorCode.BIT_ACCESS_NOT_POSSIBLE;
import static org.pshdl.model.validation.builtin.ErrorCode.BIT_ACCESS_NOT_POSSIBLE_ON_TYPE;
import static org.pshdl.model.validation.builtin.ErrorCode.BIT_ACCESS_OUT_OF_BOUNDS;
import static org.pshdl.model.validation.builtin.ErrorCode.BIT_ACCESS_POSSIBLY_NEGATIVE;
import static org.pshdl.model.validation.builtin.ErrorCode.BIT_ACCESS_POSSIBLY_OUT_OF_BOUNDS;
import static org.pshdl.model.validation.builtin.ErrorCode.BOOL_NEGATE_NUMERIC_NOT_SUPPORTED;
import static org.pshdl.model.validation.builtin.ErrorCode.CLOCK_NOT_BIT;
import static org.pshdl.model.validation.builtin.ErrorCode.CLOCK_UNKNOWN_WIDTH;
import static org.pshdl.model.validation.builtin.ErrorCode.COMBINED_ASSIGNMENT_NOT_ALLOWED;
import static org.pshdl.model.validation.builtin.ErrorCode.CONCAT_TYPE_NOT_ALLOWED;
import static org.pshdl.model.validation.builtin.ErrorCode.CONSTANT_DEFAULT_VALUE_NOT_CONSTANT;
import static org.pshdl.model.validation.builtin.ErrorCode.CONSTANT_NEED_DEFAULTVALUE;
import static org.pshdl.model.validation.builtin.ErrorCode.CONSTANT_PORT_CANT_REGISTER;
import static org.pshdl.model.validation.builtin.ErrorCode.CONSTANT_WIDTH_MISMATCH;
import static org.pshdl.model.validation.builtin.ErrorCode.DIRECTION_NOT_ALLOWED_IN_SCOPE;
import static org.pshdl.model.validation.builtin.ErrorCode.EQUALITY_ALWAYS_FALSE;
import static org.pshdl.model.validation.builtin.ErrorCode.EQUALITY_ALWAYS_TRUE;
import static org.pshdl.model.validation.builtin.ErrorCode.FOR_LOOP_RANGE_NOT_CONSTANT;
import static org.pshdl.model.validation.builtin.ErrorCode.GLOBAL_CANT_REGISTER;
import static org.pshdl.model.validation.builtin.ErrorCode.GLOBAL_NOT_CONSTANT;
import static org.pshdl.model.validation.builtin.ErrorCode.GLOBAL_VAR_SAME_NAME;
import static org.pshdl.model.validation.builtin.ErrorCode.IN_PORT_CANT_REGISTER;
import static org.pshdl.model.validation.builtin.ErrorCode.MULTI_PROCESS_WRITE;
import static org.pshdl.model.validation.builtin.ErrorCode.NO_SUCH_FUNCTION;
import static org.pshdl.model.validation.builtin.ErrorCode.ONLY_ONE_CLOCK_ANNOTATION_ALLOWED;
import static org.pshdl.model.validation.builtin.ErrorCode.ONLY_ONE_RESET_ANNOTATION_ALLOWED;
import static org.pshdl.model.validation.builtin.ErrorCode.PARAMETER_NOT_FOUND;
import static org.pshdl.model.validation.builtin.ErrorCode.RANGE_NOT_CONSTANT;
import static org.pshdl.model.validation.builtin.ErrorCode.RANGE_NOT_DOWN;
import static org.pshdl.model.validation.builtin.ErrorCode.RANGE_NOT_UP;
import static org.pshdl.model.validation.builtin.ErrorCode.RANGE_OVERLAP;
import static org.pshdl.model.validation.builtin.ErrorCode.RANGE_POSSIBLY_ZERO_OR_NEGATIVE;
import static org.pshdl.model.validation.builtin.ErrorCode.RANGE_ZERO_OR_NEGATIVE;
import static org.pshdl.model.validation.builtin.ErrorCode.REGISTER_UNKNOWN_ARGUMENT;
import static org.pshdl.model.validation.builtin.ErrorCode.REGISTER_UNKNOWN_ARGUMENT_VALUE;
import static org.pshdl.model.validation.builtin.ErrorCode.RESET_NOT_BIT;
import static org.pshdl.model.validation.builtin.ErrorCode.RESET_UNKNOWN_WIDTH;
import static org.pshdl.model.validation.builtin.ErrorCode.SWITCH_CASE_NEEDS_CONSTANT_WIDTH;
import static org.pshdl.model.validation.builtin.ErrorCode.SWITCH_CASE_NEEDS_WIDTH;
import static org.pshdl.model.validation.builtin.ErrorCode.SWITCH_LABEL_DUPLICATE;
import static org.pshdl.model.validation.builtin.ErrorCode.SWITCH_LABEL_NOT_CONSTANT;
import static org.pshdl.model.validation.builtin.ErrorCode.SWITCH_LABEL_WRONG_ENUM;
import static org.pshdl.model.validation.builtin.ErrorCode.SWITCH_MULTIPLE_DEFAULT;
import static org.pshdl.model.validation.builtin.ErrorCode.SWITCH_NO_DEFAULT;
import static org.pshdl.model.validation.builtin.ErrorCode.TYPE_INVALID_PRIMITIVE;
import static org.pshdl.model.validation.builtin.ErrorCode.TYPE_SAME_NAME;
import static org.pshdl.model.validation.builtin.ErrorCode.UNKNOWN_RANGE;
import static org.pshdl.model.validation.builtin.ErrorCode.UNRESOLVED_ENUM;
import static org.pshdl.model.validation.builtin.ErrorCode.UNRESOLVED_FRAGMENT;
import static org.pshdl.model.validation.builtin.ErrorCode.UNRESOLVED_FUNCTION;
import static org.pshdl.model.validation.builtin.ErrorCode.UNRESOLVED_INTERFACE;
import static org.pshdl.model.validation.builtin.ErrorCode.UNRESOLVED_TYPE;
import static org.pshdl.model.validation.builtin.ErrorCode.UNRESOLVED_VARIABLE;
import static org.pshdl.model.validation.builtin.ErrorCode.UNSUPPORTED_TYPE_FOR_OP;
import static org.pshdl.model.validation.builtin.ErrorCode.VARIABLE_KEYWORD_NAME;
import static org.pshdl.model.validation.builtin.ErrorCode.VARIABLE_NAME_NOT_RECOMMENDED;
import static org.pshdl.model.validation.builtin.ErrorCode.VARIABLE_SAME_NAME;
import static org.pshdl.model.validation.builtin.ErrorCode.VARIABLE_SAME_NAME_DIFFERENT_CASE;
import static org.pshdl.model.validation.builtin.ErrorCode.VARIABLE_SCOPE_SAME_NAME;
import static org.pshdl.model.validation.builtin.ErrorCode.VARIABLE_SCOPE_SAME_NAME_DIFFERENT_CASE;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLArgument;
import org.pshdl.model.HDLArithOp;
import org.pshdl.model.HDLArrayInit;
import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLBitOp;
import org.pshdl.model.HDLBlock;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLConcat;
import org.pshdl.model.HDLDirectGeneration;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLEqualityOp;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLInlineFunction;
import org.pshdl.model.HDLInstantiation;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLInterfaceInstantiation;
import org.pshdl.model.HDLInterfaceRef;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLObject.GenericMeta;
import org.pshdl.model.HDLOpExpression;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLResolvedRef;
import org.pshdl.model.HDLShiftOp;
import org.pshdl.model.HDLSubstituteFunction;
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.evaluation.ConstantEvaluate;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.extensions.RangeExtension;
import org.pshdl.model.extensions.TypeExtension;
import org.pshdl.model.parser.ParserToModelExtension;
import org.pshdl.model.simulation.RangeTool;
import org.pshdl.model.types.builtIn.HDLAnnotations;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import org.pshdl.model.types.builtIn.HDLGenerators;
import org.pshdl.model.types.builtIn.HDLPrimitives;
import org.pshdl.model.utils.HDLLibrary;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.HDLQuery;
import org.pshdl.model.utils.Insulin;
import org.pshdl.model.utils.MetaAccess;
import org.pshdl.model.utils.services.HDLTypeInferenceInfo;
import org.pshdl.model.utils.services.IHDLValidator;
import org.pshdl.model.validation.HDLValidator.HDLAdvise;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.RWValidation;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

public class BuiltInValidator implements IHDLValidator {

	public static final GenericMeta<Range<BigInteger>> ARRAY_RANGE = new GenericMeta<>("arrayRange", true);
	public static final GenericMeta<Range<BigInteger>> ACCESS_RANGE = new GenericMeta<>("accessRange", true);
	public static String[] PSHDL_KEYWORDS = new String[] { "bit", "out", "string", "switch", "include", "process", "for", "function", "import", "else", "extends", "native",
			"package", "testbench", "int", "if", "in", "default", "enum", "const", "module", "inline", "generate", "bool", "simulation", "uint", "case", "inout", "substitute",
			"param", "register", "interface" };
	public final static Set<String> keywordSet;

	static {
		keywordSet = Sets.newLinkedHashSet();
		for (final String keyword : PSHDL_KEYWORDS) {
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
	public boolean check(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		// TODO find a way to distinguish between context dependent problems and
		// others
		try {
			checkUnresolved(pkg, problems);
			checkFunctionCalls(pkg, problems, hContext);
			pkg = Insulin.inlineFunctions(pkg);
			RWValidation.checkVariableUsage(pkg, problems);
			pkg = Insulin.setParameterOnInstance(pkg);
			checkVariableNaming(pkg, problems);
			checkClockAndResetAnnotation(pkg, problems);
			checkConstantBoundaries(pkg, problems, hContext);
			checkParameterInstance(pkg, problems, hContext);
			checkArrayBoundaries(pkg, problems, hContext);
			checkConstantEquals(pkg, problems, hContext);
			checkBitAccess(pkg, problems, hContext);
			// VALIDATE Validate value ranges, check for 0 divide
			checkRanges(pkg, problems, hContext);
			// VALIDATE Check for POW only power of 2
			checkCombinedAssignment(pkg, problems, hContext);
			checkAnnotations(pkg, problems, hContext);
			checkType(pkg, problems, hContext);
			checkProcessWrite(pkg, problems, hContext);
			checkGenerators(pkg, problems, hContext);
			checkConstantPackageDeclarations(pkg, problems, hContext);
			checkLiteralConcat(pkg, problems);
			checkDuplicateType(pkg, problems);
			// VALIDATE Validate bitWidth mismatch
			checkBitWidthMismatch(pkg, problems, hContext);
			checkAssignments(pkg, problems, hContext);
			checkDirectionSubScopes(pkg, problems, hContext);
			// VALIDATE Multi-bit Write only for Constants
			// VALIDATE check for signals named clk or rst and warn about the
			// collision
			// VALIDATE check for valid parameter
			checkSwitchStatements(pkg, problems, hContext);
			// VALIDATE Type checking!
			// VALIDATE Check for combinatorial loop.
			// VALIDATE Check for multiple assignment in same Scope
			// VALIDATE No processes in Module
			// VALIDATE no I/O variables in block
			// VALIDATE warn for name collision in generators
			// VALIDATE Out port array size need to be constant
			// VALIDATE Check for bit access on left assignment side when right is
			// matching
			// VALIDATE no Registers in Testbench/Global gen
			// VALIDATE: Don't allow boolean assignments to non-boolean variable
			// VALIDATE warn for big delay numbers
			// VALIDATE Param should be uint32
			checkRegisters(pkg, problems, hContext);

		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void checkDirectionSubScopes(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLUnit[] units = pkg.getAllObjectsOf(HDLUnit.class, true);
		for (final HDLUnit hdlUnit : units) {
			final Collection<HDLVariableDeclaration> allHVDs = HDLQuery.select(HDLVariableDeclaration.class).from(hdlUnit).where(HDLObject.fContainer).isNotEqualTo(hdlUnit)
					.getAll();
			for (final HDLVariableDeclaration hvd : allHVDs) {
				switch (hvd.getDirection()) {
				case CONSTANT:
				case HIDDEN:
				case INTERNAL:
					break;
				case IN:
				case INOUT:
				case OUT:
				case PARAMETER:
					problems.add(new Problem(DIRECTION_NOT_ALLOWED_IN_SCOPE, hvd));
					break;
				}
			}
		}
	}

	private void checkParameterInstance(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLInterfaceInstantiation[] instances = pkg.getAllObjectsOf(HDLInterfaceInstantiation.class, true);
		for (final HDLInterfaceInstantiation hii : instances) {
			final Optional<HDLInterface> hIf = hii.resolveHIf();
			if (!hIf.isPresent()) {
				continue;
			}
			final HDLInterface hif = hIf.get();
			final Collection<HDLVariableDeclaration> params = HDLQuery.select(HDLVariableDeclaration.class).from(hif).where(HDLVariableDeclaration.fDirection)
					.isEqualTo(HDLDirection.PARAMETER).getAll();
			final Map<String, HDLVariable> paramNames = Maps.newLinkedHashMap();
			for (final HDLVariableDeclaration hvd : params) {
				for (final HDLVariable var : hvd.getVariables()) {
					paramNames.put(var.getMeta(HDLInterfaceInstantiation.ORIG_NAME), var);
				}
			}
			final ArrayList<HDLArgument> arguments = hii.getArguments();
			for (final HDLArgument hdlArgument : arguments) {
				if (!paramNames.containsKey(hdlArgument.getName())) {
					problems.add(new Problem(PARAMETER_NOT_FOUND, hdlArgument, hif));
				} else {
					final HDLVariable var = paramNames.get(hdlArgument.getName());
					checkAss(hdlArgument, var, hdlArgument.getExpression(), problems, getContext(hContext, hdlArgument));
				}
			}
		}
	}

	private static void checkBitWidthMismatch(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		for (final HDLOpExpression ope : pkg.getAllObjectsOf(HDLOpExpression.class, true)) {
			final HDLExpression left = ope.getLeft();
			final Optional<? extends HDLType> leftType = TypeExtension.typeOf(left);
			if (!leftType.isPresent()) {
				continue;
			}
			final HDLExpression right = ope.getRight();
			final Optional<? extends HDLType> rightType = TypeExtension.typeOf(right);
			if (!rightType.isPresent()) {
				continue;
			}
			final HDLType rType = rightType.get();
			if (rType.getClassType() != HDLClass.HDLPrimitive) {
				continue;
			}
			final HDLPrimitive rpType = (HDLPrimitive) rType;
			if ((rpType.getType() == HDLPrimitiveType.STRING) || (rpType.getType() == HDLPrimitiveType.BOOL)) {
				continue;
			}
			final HDLType lType = leftType.get();
			if (lType.getClassType() != HDLClass.HDLPrimitive) {
				continue;
			}
			final HDLPrimitive lpType = (HDLPrimitive) lType;
			if ((lpType.getType() == HDLPrimitiveType.STRING) || (lpType.getType() == HDLPrimitiveType.BOOL)) {
				continue;
			}
			final HDLEvaluationContext lcontext = getContext(hContext, left);
			final Integer lw = HDLPrimitives.getWidth(lType, lcontext);
			if (lw == null) {
				continue;
			}
			final HDLEvaluationContext rcontext = getContext(hContext, right);
			final Integer rw = HDLPrimitives.getWidth(lType, rcontext);
			if (rw == null) {
				continue;
			}
			final Optional<BigInteger> lValue = ConstantEvaluate.valueOf(left, lcontext);
			if (lValue.isPresent()) {
				final BigInteger val = lValue.get();
				if (rw < val.bitLength()) {
					problems.add(new Problem(CONSTANT_WIDTH_MISMATCH, left));
				}
			}
			final Optional<BigInteger> rValue = ConstantEvaluate.valueOf(right, rcontext);
			if (rValue.isPresent()) {
				final BigInteger val = rValue.get();
				if (lw < val.bitLength()) {
					problems.add(new Problem(CONSTANT_WIDTH_MISMATCH, right));
				}
			}
		}
	}

	private static void checkDuplicateType(HDLPackage pkg, Set<Problem> problems) {
		checkType(problems, pkg.getAllObjectsOf(HDLUnit.class, true));
		checkType(problems, pkg.getAllObjectsOf(HDLInterface.class, true));
		checkType(problems, pkg.getAllObjectsOf(HDLEnum.class, true));

		final HDLVariableDeclaration[] hvds = pkg.getAllObjectsOf(HDLVariableDeclaration.class, false);
		for (final HDLVariableDeclaration hvd : hvds) {
			for (final HDLVariable hif : hvd.getVariables()) {

				final HDLQualifiedName fqn = fullNameOf(hif);
				final HDLLibrary library = hif.getLibrary();
				final Optional<HDLVariable> resolve = library.resolveVariable(Collections.<String> emptyList(), fqn);
				if (resolve.isPresent()) {
					final HDLVariable type = resolve.get();
					if (type.getID() != hif.getID()) {
						problems.add(new Problem(GLOBAL_VAR_SAME_NAME, hif, hif));
					}
				}
			}
		}
	}

	public static void checkType(Set<Problem> problems, HDLObject[] ifs) {
		for (final HDLObject hif : ifs) {
			final HDLQualifiedName fqn = fullNameOf(hif);
			final HDLLibrary library = hif.getLibrary();
			if (library != null) {
				final Optional<? extends HDLType> resolve = library.resolve(Collections.<String> emptyList(), fqn);
				if (resolve.isPresent()) {
					final HDLType type = resolve.get();
					if (type.getID() != hif.getID()) {
						problems.add(new Problem(TYPE_SAME_NAME, hif, type));
					}
				}
			}
		}
	}

	/**
	 * Check that constants declared on the global scope are indeed constant and not registers
	 */
	private static void checkConstantPackageDeclarations(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLVariableDeclaration[] hvds = pkg.getAllObjectsOf(HDLVariableDeclaration.class, false);
		for (final HDLVariableDeclaration hvd : hvds) {
			if (hvd.getRegister() != null) {
				problems.add(new Problem(GLOBAL_CANT_REGISTER, hvd));
			}
			if (hvd.getDirection() != HDLDirection.CONSTANT) {
				problems.add(new Problem(GLOBAL_NOT_CONSTANT, hvd));
			}
			for (final HDLVariable var : hvd.getVariables()) {
				if (var.getDefaultValue() == null) {
					problems.add(new Problem(GLOBAL_NOT_CONSTANT, var));
				} else {
					final Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(var.getDefaultValue());
					if (!valueOf.isPresent()) {
						problems.add(new Problem(GLOBAL_NOT_CONSTANT, var));
					}
				}
			}
		}
	}

	/**
	 * Check that registers are neither constants nor in ports
	 */
	private static void checkRegisters(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLVariableDeclaration[] hvds = pkg.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (final HDLVariableDeclaration hvd : hvds) {
			final HDLRegisterConfig reg = hvd.getRegister();
			if (reg != null) {
				final Iterable<HDLArgument> meta = reg.getMeta(HDLRegisterConfig.ORIGINAL_ARGS);
				for (final HDLArgument hdlArgument : meta) {
					if (!HDLRegisterConfig.VALID_PARAMS.contains(hdlArgument.getName())) {
						problems.add(new Problem(REGISTER_UNKNOWN_ARGUMENT, hdlArgument));
					}
				}
				if ((reg.getUnresolvedClockType() != null) && !(reg.getUnresolvedClockType() instanceof HDLEnumRef)) {
					problems.add(new Problem(REGISTER_UNKNOWN_ARGUMENT_VALUE, reg.getUnresolvedClockType(), "Edge.RISING and Edge.FALLING"));
				}
				if ((reg.getUnresolvedSyncType() != null) && !(reg.getUnresolvedSyncType() instanceof HDLEnumRef)) {
					problems.add(new Problem(REGISTER_UNKNOWN_ARGUMENT_VALUE, reg.getUnresolvedSyncType(), "Sync.SYNC and Sync.ASYNC"));
				}
				if ((reg.getUnresolvedResetType() != null) && !(reg.getUnresolvedResetType() instanceof HDLEnumRef)) {
					problems.add(new Problem(REGISTER_UNKNOWN_ARGUMENT_VALUE, reg.getUnresolvedResetType(), "Active.HIGH and Active.LOW"));
				}
				final HDLExpression clk = reg.getClk();
				if (clk != null) {
					final HDLExpression width = TypeExtension.getWidth(clk);
					if (width == null) {
						problems.add(new Problem(CLOCK_UNKNOWN_WIDTH, clk));
					} else {
						final Optional<BigInteger> clkWidth = ConstantEvaluate.valueOf(width);
						if (!clkWidth.isPresent()) {
							problems.add(new Problem(CLOCK_UNKNOWN_WIDTH, clk));
						} else {
							if (!clkWidth.get().equals(BigInteger.ONE)) {
								problems.add(new Problem(CLOCK_NOT_BIT, clk));
							}
						}
					}
				}
				final HDLExpression rst = reg.getRst();
				if (rst != null) {
					final HDLExpression width = TypeExtension.getWidth(rst);
					if (width == null) {
						problems.add(new Problem(RESET_UNKNOWN_WIDTH, rst));
					} else {
						final Optional<BigInteger> rstWidth = ConstantEvaluate.valueOf(width);
						if (!rstWidth.isPresent()) {
							problems.add(new Problem(RESET_UNKNOWN_WIDTH, rst));
						} else {
							if (!rstWidth.get().equals(BigInteger.ONE)) {
								problems.add(new Problem(RESET_NOT_BIT, rst));
							}
						}
					}
				}
				switch (hvd.getDirection()) {
				case CONSTANT:
				case PARAMETER:
					problems.add(new Problem(CONSTANT_PORT_CANT_REGISTER, hvd));
					break;
				case IN:
					problems.add(new Problem(IN_PORT_CANT_REGISTER, hvd));
					break;
				default:
				}
			}
		}
	}

	/**
	 * Check that the from range is of the right correction. That is from>to for variables and to>from for loops
	 */
	private static void checkRanges(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLRange[] ranges = pkg.getAllObjectsOf(HDLRange.class, true);
		for (final HDLRange hdlRange : ranges) {
			final HDLExpression from = hdlRange.getFrom();
			if (skipExp(hdlRange)) {
				continue;
			}
			final HDLEvaluationContext context = getContext(hContext, hdlRange);
			if (from == null) {
				if (hdlRange.getInc() != null) {
					if (checkRangeAddendum(hdlRange, problems, hdlRange.getInc(), context)) {
						continue;
					}
				}
				if (hdlRange.getDec() != null) {
					if (checkRangeAddendum(hdlRange, problems, hdlRange.getDec(), context)) {
						continue;
					}
				}
				continue;
			}
			// For loop ranges are up to
			final Optional<Range<BigInteger>> fromRangeOf = RangeExtension.rangeOf(from, context);
			if (!fromRangeOf.isPresent()) {
				problems.add(new Problem(UNKNOWN_RANGE, from));
				continue;
			}
			final HDLExpression to = hdlRange.getTo();
			final Optional<Range<BigInteger>> toRangeOf = RangeExtension.rangeOf(to, context);
			if (!toRangeOf.isPresent()) {
				problems.add(new Problem(UNKNOWN_RANGE, to));
				continue;
			}
			if (fromRangeOf.get().isConnected(toRangeOf.get()) && !fromRangeOf.get().equals(toRangeOf.get())) {
				problems.add(new Problem(RANGE_OVERLAP, hdlRange));
				continue;
			}
			if (hdlRange.getContainer() instanceof HDLForLoop) {
				if (fromRangeOf.get().upperEndpoint().compareTo(toRangeOf.get().lowerEndpoint()) > 0) {
					problems.add(new Problem(RANGE_NOT_UP, hdlRange));
				}
			} else if (toRangeOf.get().lowerEndpoint().compareTo(fromRangeOf.get().upperEndpoint()) > 0) {
				problems.add(new Problem(RANGE_NOT_DOWN, hdlRange));
			}
		}
	}

	private static boolean checkRangeAddendum(HDLRange hdlRange, Set<Problem> problems, HDLExpression inc, HDLEvaluationContext context) {
		final Optional<BigInteger> incValOpt = ConstantEvaluate.valueOf(inc, context);
		if (incValOpt.isPresent()) {
			if (incValOpt.get().compareTo(BigInteger.ONE) < 0) {
				problems.add(new Problem(RANGE_ZERO_OR_NEGATIVE, inc));
				return true;
			}
		} else {
			problems.add(new Problem(RANGE_NOT_CONSTANT, inc));
			return true;
		}
		final Optional<Range<BigInteger>> incRange = RangeExtension.rangeOf(inc);
		if (incRange.isPresent()) {
			final Range<BigInteger> range = incRange.get();
			if (range.lowerEndpoint().compareTo(BigInteger.ONE) < 0) {
				problems.add(new Problem(RANGE_POSSIBLY_ZERO_OR_NEGATIVE, inc));
				return true;
			}
		}
		return false;
	}

	private static void checkBitAccess(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLVariableRef[] refs = pkg.getAllObjectsOf(HDLVariableRef.class, true);
		for (final HDLVariableRef ref : refs) {
			if (skipExp(ref)) {
				continue;
			}
			if (ref.getBits().size() != 0) {
				final Optional<HDLVariable> resolveVar = ref.resolveVar();
				if (resolveVar.isPresent()) {
					final Optional<? extends HDLType> varType = TypeExtension.typeOf(resolveVar.get());
					if (!varType.isPresent()) {
						// Don't know type, do nothing then..
					} else if (!(varType.get() instanceof HDLPrimitive)) {
						problems.add(new Problem(BIT_ACCESS_NOT_POSSIBLE_ON_TYPE, ref, varType.get()));
					} else {
						final HDLPrimitive primitive = (HDLPrimitive) varType.get();
						switch (primitive.getType()) {
						case BITVECTOR:
						case INT:
						case UINT:
							break;
						default:
							problems.add(new Problem(BIT_ACCESS_NOT_POSSIBLE, ref, varType.get()));
							continue;
						}
						final Optional<Range<BigInteger>> bitSizeRangeOpt = RangeExtension.rangeOf(primitive.getWidth(), null);
						if (!bitSizeRangeOpt.isPresent()) {
							continue;
						}
						final Range<BigInteger> availableRange = bitSizeRangeOpt.get();
						for (final HDLRange bit : ref.getBits()) {
							final Optional<Range<BigInteger>> accessRangeFrom = RangeExtension.rangeOf(bit);
							if (!accessRangeFrom.isPresent()) {
								continue;
							}
							checkAccessBoundaries(accessRangeFrom.get(), availableRange, problems, bit, ref, true);
						}
					}
				}
			}
		}
	}

	private static void checkUnresolved(HDLPackage pkg, Set<Problem> problems) {
		final HDLUnresolvedFragment[] fragments = pkg.getAllObjectsOf(HDLUnresolvedFragment.class, true);
		for (final HDLUnresolvedFragment fragment : fragments) {
			final IHDLObject container = fragment.getContainer();
			if ((container instanceof HDLUnresolvedFragment)) {
				final HDLUnresolvedFragment contFrag = (HDLUnresolvedFragment) container;
				if (contFrag.getSub() == fragment) {
					continue;
				}
			}
			problems.add(new Problem(UNRESOLVED_FRAGMENT, fragment));
		}
		final HDLResolvedRef[] refs = pkg.getAllObjectsOf(HDLResolvedRef.class, true);
		for (final HDLResolvedRef ref : refs) {
			final Optional<HDLVariable> resolveVar = ref.resolveVar();
			if (!resolveVar.isPresent()) {
				problems.add(new Problem(UNRESOLVED_VARIABLE, ref));
			}
			if (ref instanceof HDLEnumRef) {
				final HDLEnumRef enumRef = (HDLEnumRef) ref;
				final Optional<HDLEnum> resolveHEnum = enumRef.resolveHEnum();
				if (!resolveHEnum.isPresent()) {
					problems.add(new Problem(UNRESOLVED_ENUM, ref));
				}
			}
			if (ref instanceof HDLInterfaceRef) {
				final HDLInterfaceRef hir = (HDLInterfaceRef) ref;
				final Optional<HDLVariable> hIf = hir.resolveHIf();
				if (!hIf.isPresent()) {
					problems.add(new Problem(UNRESOLVED_VARIABLE, ref));
				}
			}
		}
		final HDLFunctionCall[] functionCalls = pkg.getAllObjectsOf(HDLFunctionCall.class, true);
		for (final HDLFunctionCall call : functionCalls) {
			final Optional<HDLFunction> function = call.resolveFunction();
			if (!function.isPresent()) {
				problems.add(new Problem(UNRESOLVED_FUNCTION, call));
			}
		}
		final HDLVariableDeclaration[] hvds = pkg.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (final HDLVariableDeclaration hvd : hvds) {
			final Optional<? extends HDLType> type = hvd.resolveType();
			if (!type.isPresent()) {
				problems.add(new Problem(UNRESOLVED_TYPE, hvd));
			}
		}
		final HDLInterfaceInstantiation[] hiis = pkg.getAllObjectsOf(HDLInterfaceInstantiation.class, true);
		for (final HDLInterfaceInstantiation hii : hiis) {
			final Optional<HDLInterface> type = hii.resolveHIf();
			if (!type.isPresent()) {
				problems.add(new Problem(UNRESOLVED_INTERFACE, hii));
			}
		}
	}

	private static void checkAssignments(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLAssignment[] asss = pkg.getAllObjectsOf(HDLAssignment.class, true);
		for (final HDLAssignment ass : asss) {
			final HDLEvaluationContext context = getContext(hContext, ass);
			checkAss(ass, ass.getLeft(), ass.getRight(), problems, context);
		}

		final HDLVariable[] vars = pkg.getAllObjectsOf(HDLVariable.class, true);
		for (final HDLVariable hdlVariable : vars) {
			if (hdlVariable.getDefaultValue() != null) {
				final HDLEvaluationContext context = getContext(hContext, hdlVariable);
				checkAss(hdlVariable, hdlVariable, hdlVariable.getDefaultValue(), problems, context);
			}
		}
	}

	private static void checkAss(IHDLObject obj, IHDLObject leftRef, HDLExpression rightExp, Set<Problem> problems, HDLEvaluationContext context) {
		final Optional<? extends HDLType> lType = TypeExtension.typeOf(leftRef);
		final Optional<? extends HDLType> rType = TypeExtension.typeOf(rightExp);
		if ((!lType.isPresent()) || (!rType.isPresent())) {
			return;
		}
		switch (lType.get().getClassType()) {
		case HDLEnum:
			if (rType.get().getClassType() != HDLClass.HDLEnum) {
				problems.add(new Problem(ASSIGNMENT_NOT_ENUM, obj));
			}
			break;
		case HDLPrimitive:
			if (rType.get().getClassType() != HDLClass.HDLPrimitive) {
				problems.add(new Problem(ASSIGNMENT_NOT_PRIMITIVE, obj));
			} else {
				final HDLPrimitive left = (HDLPrimitive) lType.get();
				final HDLPrimitive right = (HDLPrimitive) rType.get();
				if ((right.getType() == HDLPrimitiveType.STRING) && (left.getType() != HDLPrimitiveType.STRING)) {
					problems.add(new Problem(ASSIGNMENT_NOT_SUPPORTED, obj, "Strings can only be assigned to other strings"));
				} else {
					switch (left.getType()) {
					case BIT:
						if (right.getType() != HDLPrimitiveType.BIT) {
							if (right.getWidth() != null) {
								final Optional<BigInteger> w = ConstantEvaluate.valueOf(right.getWidth(), context);
								if (w.isPresent() && !w.get().equals(BigInteger.ONE)) {
									problems.add(new Problem(ASSIGNMENT_CLIPPING_WILL_OCCUR, rightExp, obj));
								}
							}
						}
						break;
					case BITVECTOR:
						break;
					case BOOL:
						// String is also invalid, but handled above
						break;
					case INT:
					case INTEGER:
					case NATURAL:
					case UINT:
						if (!right.isNumber()) {
							problems.add(new Problem(ASSIGNMENT_NOT_SUPPORTED, obj, "The assigned value needs to be numeric (uint<?>/int<?>)"));
						}
						break;
					case STRING:
						if (right.getType() != HDLPrimitiveType.STRING) {
							problems.add(new Problem(ASSIGNMENT_NOT_SUPPORTED, obj, "Strings can only be assigned to other strings"));
						}
						break;
					}
				}
			}
			break;
		case HDLInterface:
		default:
			problems.add(new Problem(ASSIGNMENT_NOT_SUPPORTED, obj));
		}
	}

	private static void checkLiteralConcat(HDLPackage pkg, Set<Problem> problems) {
		final HDLConcat[] concats = pkg.getAllObjectsOf(HDLConcat.class, true);
		for (final HDLConcat hdlConcat : concats) {
			final ArrayList<HDLExpression> cats = hdlConcat.getCats();
			for (final HDLExpression exp : cats) {
				final Optional<? extends HDLType> type = TypeExtension.typeOf(exp);
				if (type.isPresent()) {
					if (type.get() instanceof HDLPrimitive) {
						final HDLPrimitive prim = (HDLPrimitive) type.get();
						switch (prim.getType()) {
						case BIT:
						case BITVECTOR:
						case INT:
						case UINT:
							break;
						case BOOL:
						case INTEGER:
						case NATURAL:
						case STRING:
							problems.add(new Problem(CONCAT_TYPE_NOT_ALLOWED, exp, prim));
							break;
						}
					} else {
						problems.add(new Problem(CONCAT_TYPE_NOT_ALLOWED, exp, type.get()));
					}
				}
			}
		}
	}

	private static void checkSwitchStatements(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLSwitchStatement[] switches = pkg.getAllObjectsOf(HDLSwitchStatement.class, true);
		for (final HDLSwitchStatement switchStatement : switches) {
			boolean defaultFound = false;
			final ArrayList<HDLSwitchCaseStatement> cases = switchStatement.getCases();
			final Set<BigInteger> values = Sets.newLinkedHashSet();
			final Set<HDLQualifiedName> enums = Sets.newLinkedHashSet();
			final Optional<? extends HDLType> type = TypeExtension.typeOf(switchStatement.getCaseExp());
			if (!type.isPresent()) {
				continue;
			}
			if (type.get() instanceof HDLPrimitive) {
				final HDLPrimitive primitive = (HDLPrimitive) type.get();
				if (primitive.getWidth() == null) {
					problems.add(new Problem(SWITCH_CASE_NEEDS_WIDTH, switchStatement.getCaseExp()));
				}
				final Optional<BigInteger> width = ConstantEvaluate.valueOf(primitive.getWidth(), null);
				if (!width.isPresent() && ((primitive.getType() == HDLPrimitiveType.INT) || (primitive.getType() == HDLPrimitiveType.UINT)
						|| (primitive.getType() == HDLPrimitiveType.BITVECTOR))) {
					problems.add(new Problem(SWITCH_CASE_NEEDS_CONSTANT_WIDTH, switchStatement.getCaseExp()));
				}
			}
			final boolean isEnum = type.get() instanceof HDLEnum;
			for (final HDLSwitchCaseStatement caseStatement : cases) {
				final HDLExpression label = caseStatement.getLabel();
				if (label == null) {
					if (defaultFound) {
						problems.add(new Problem(SWITCH_MULTIPLE_DEFAULT, caseStatement));
					}
					defaultFound = true;
				} else if (!isEnum) {
					final Optional<BigInteger> constant = ConstantEvaluate.valueOf(label, null);
					if (!constant.isPresent()) {
						problems.add(new Problem(SWITCH_LABEL_NOT_CONSTANT, caseStatement));
					} else if (!values.add(constant.get())) {
						problems.add(new Problem(SWITCH_LABEL_DUPLICATE, caseStatement));
					}
				} else {
					final Optional<? extends HDLType> labelType = TypeExtension.typeOf(label);
					if (labelType.isPresent() && !type.get().equals(labelType.get())) {
						problems.add(new Problem(SWITCH_LABEL_WRONG_ENUM, caseStatement));
					}
					if ((label instanceof HDLEnumRef) && !enums.add(((HDLEnumRef) label).getVarRefName())) {
						problems.add(new Problem(SWITCH_LABEL_DUPLICATE, caseStatement));
					}
				}
			}
			if (!defaultFound) {
				problems.add(new Problem(SWITCH_NO_DEFAULT, switchStatement));
			}
		}
	}

	private static void checkVariableNaming(HDLPackage pkg, Set<Problem> problems) {
		final HDLVariable[] vars = pkg.getAllObjectsOf(HDLVariable.class, true);
		final Map<String, HDLVariable> nameMap = Maps.newLinkedHashMap();
		for (final HDLVariable hdlVariable : vars) {
			final HDLQualifiedName fullName = fullNameOf(hdlVariable);
			final String lastSegment = fullName.getLastSegment();
			if (keywordSet.contains(lastSegment)) {
				problems.add(new Problem(VARIABLE_KEYWORD_NAME, hdlVariable));
			}
			if (lastSegment.charAt(0) == '$') {
				problems.add(new Problem(VARIABLE_NAME_NOT_RECOMMENDED, hdlVariable));
			}
			final HDLVariable put = nameMap.put(fullName.toString().toLowerCase(), hdlVariable);
			if (put != null) {
				final HDLQualifiedName otherName = fullNameOf(put);
				if (otherName.equals(fullName)) {
					problems.add(new Problem(VARIABLE_SAME_NAME, hdlVariable, put));
				} else {
					problems.add(new Problem(VARIABLE_SAME_NAME_DIFFERENT_CASE, hdlVariable, put));
				}
			}
		}
		for (final Entry<String, HDLVariable> entry : nameMap.entrySet()) {
			final HDLVariable var = entry.getValue();
			final HDLQualifiedName fullName = fullNameOf(var);
			final String lastSegment = fullName.getLastSegment();
			final HDLQualifiedName type = fullName.getTypePart();
			final HDLQualifiedName localPart = fullName.getLocalPart().skipLast(1);
			for (int i = 0; i < localPart.length; i++) {
				final HDLQualifiedName scoped = localPart.skipLast(i + 1).append(lastSegment);
				final HDLQualifiedName newName = type.append(scoped);
				final HDLVariable otherVar = nameMap.get(newName.toString().toLowerCase());
				if (otherVar != null) {
					final HDLQualifiedName otherName = fullNameOf(otherVar);
					if (otherName.equals(newName)) {
						problems.add(new Problem(VARIABLE_SCOPE_SAME_NAME, var, otherVar));
					} else {
						problems.add(new Problem(VARIABLE_SCOPE_SAME_NAME_DIFFERENT_CASE, var, otherVar));
					}
				}
			}
		}
	}

	private static void checkGenerators(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLDirectGeneration[] generators = unit.getAllObjectsOf(HDLDirectGeneration.class, true);
		for (final HDLDirectGeneration hdg : generators) {
			if (HDLGenerators.validate(hdg, problems, getContext(hContext, hdg))) {
				if (hdg.getHIf() == null) {
					problems.add(new Problem(ErrorCode.GENERATOR_ERROR, hdg));
				}
			}
		}
	}

	/**
	 * Checks whether called functions exists, whether they have the correct number of arguments etc..
	 */
	private static void checkFunctionCalls(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLFunctionCall[] calls = unit.getAllObjectsOf(HDLFunctionCall.class, true);
		for (final HDLFunctionCall call : calls) {
			final Optional<HDLFunction> function = call.resolveFunction();
			if (!function.isPresent()) {
				problems.add(new Problem(NO_SUCH_FUNCTION, call));
			}
		}
	}

	private static void checkProcessWrite(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLVariable[] vars = unit.getAllObjectsOf(HDLVariable.class, true);
		for (final HDLVariable var : vars) {
			if (var.hasMeta(RWValidation.BLOCK_META_CLASH)) {
				problems.add(new Problem(MULTI_PROCESS_WRITE, var));
			}
		}
	}

	private static void checkType(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLVariableDeclaration[] hvds = unit.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (final HDLVariableDeclaration hvd : hvds) {
			final Optional<? extends HDLType> type = hvd.resolveType();
			if (type.isPresent()) {
				final HDLType hdlType = type.get();
				if (hdlType instanceof HDLPrimitive) {
					final HDLPrimitive primType = (HDLPrimitive) hdlType;
					if (primType.hasMeta(ParserToModelExtension.isDeprecatedDeclaration)) {
						problems.add(new Problem(ErrorCode.DEPRECATED_TYPE, primType));
					}
					switch (primType.getType()) {
					case BIT:
					case INTEGER:
					case NATURAL:
						break;
					case STRING:
						if (primType.getWidth() != null) {
							problems.add(new Problem(TYPE_INVALID_PRIMITIVE, hvd, "Strings can not have a width"));
						}
						break;
					case BOOL:
						if (primType.getWidth() != null) {
							problems.add(new Problem(TYPE_INVALID_PRIMITIVE, hvd, "Booleans can not have a width"));
						}
						break;
					case BITVECTOR:
					case INT:
					case UINT:
						final Optional<Range<BigInteger>> rangeOpt = RangeExtension.rangeOf(primType.getWidth());
						if (rangeOpt.isPresent()) {
							final Range<BigInteger> range = rangeOpt.get();
							if (!range.hasLowerBound()) {
								problems.add(new Problem(ErrorCode.TYPE_NEGATIVE_WIDTH, hvd));
							} else {
								final BigInteger le = range.lowerEndpoint();
								if (le.compareTo(BigInteger.ZERO) < 0) {
									if (range.hasUpperBound() && (range.upperEndpoint().compareTo(BigInteger.ZERO) < 0)) {
										problems.add(new Problem(ErrorCode.TYPE_NEGATIVE_WIDTH, hvd));
									} else {
										problems.add(new Problem(ErrorCode.TYPE_POSSIBLY_NEGATIVE_WIDTH, hvd));
									}
								} else if (le.equals(BigInteger.ZERO) && range.hasUpperBound() && range.upperEndpoint().equals(BigInteger.ZERO)) {
									problems.add(new Problem(ErrorCode.TYPE_ZERO_WIDTH, hvd));
								} else if (le.equals(BigInteger.ZERO)) {
									problems.add(new Problem(ErrorCode.TYPE_POSSIBLY_ZERO_WIDTH, hvd));
								}
							}
						}
						break;
					case ANY_BIT:
					case ANY_INT:
					case ANY_UINT:
						if (hvd.getVariables().size() != 1) {
							problems.add(new Problem(ErrorCode.ONLY_ONE_VARIABLE_WITH_ANY, hvd));
						}
						final HDLVariable variable = hvd.getVariables().get(0);
						if (variable.getDefaultValue() == null) {
							problems.add(new Problem(ErrorCode.ANY_TYPE_REQUIRES_DEFAULT, variable));
						}
						final Collection<HDLVariableRef> refs = HDLQuery.getVarRefs(unit, variable);
						for (final HDLVariableRef ref : refs) {
							if (ref.getContainingFeature() == HDLAssignment.fLeft) {
								problems.add(new Problem(ErrorCode.ANY_TYPE_ONLY_DEFAULT, ref.getContainer()));
							}
						}
						break;
					}
				}
			}
		}

		final HDLOpExpression[] ops = unit.getAllObjectsOf(HDLOpExpression.class, true);
		for (final HDLOpExpression ope : ops) {
			if (skipExp(ope)) {
				continue;
			}
			checkOpExpression(problems, ope, ope);
		}
		final HDLManip[] manips = unit.getAllObjectsOf(HDLManip.class, true);
		for (final HDLManip manip : manips) {
			final Optional<? extends HDLType> targetType = TypeExtension.typeOf(manip.getTarget());
			if (targetType.isPresent()) {
				final HDLType tt = targetType.get();
				switch (manip.getType()) {
				case ARITH_NEG:
					if (tt instanceof HDLPrimitive) {
						final HDLPrimitive primitive = (HDLPrimitive) tt;
						if (!primitive.isNumber()) {
							problems.add(new Problem(UNSUPPORTED_TYPE_FOR_OP, manip, "Can not use arithmetic negate on a non-number"));
						}
					} else {
						problems.add(new Problem(UNSUPPORTED_TYPE_FOR_OP, manip, "Can not use arithmetic negate on a non-number"));
					}
					break;
				case BIT_NEG:
					if (manip.getTarget().getClassType() == HDLClass.HDLLiteral) {
						problems.add(new Problem(UNSUPPORTED_TYPE_FOR_OP, manip, "Can not use binary negate on literals as they have no width"));
					}
					if (tt instanceof HDLPrimitive) {
						final HDLPrimitive primitive = (HDLPrimitive) tt;
						if (!primitive.isBits()) {
							problems.add(new Problem(UNSUPPORTED_TYPE_FOR_OP, manip, "Can not use binary negate on a non-bits"));
						}
					} else {
						problems.add(new Problem(UNSUPPORTED_TYPE_FOR_OP, manip, "Can not use binary negate on a non-bits"));
					}
					break;
				case LOGIC_NEG:
					if (tt instanceof HDLPrimitive) {
						final HDLPrimitive primitive = (HDLPrimitive) tt;
						if ((primitive.getType() != HDLPrimitiveType.BOOL) && (primitive.getType() != HDLPrimitiveType.BIT)) {
							problems.add(new Problem(BOOL_NEGATE_NUMERIC_NOT_SUPPORTED, manip, "Can not use logic negate on a non boolean/bit"));
						}
					} else {
						problems.add(new Problem(UNSUPPORTED_TYPE_FOR_OP, manip, "Can not use logic negate on a non boolean"));
					}
					break;
				case CAST:
					final HDLType castTo = manip.getCastTo();
					if (castTo instanceof HDLInterface) {
						if (!(tt instanceof HDLInterface)) {
							problems.add(new Problem(UNSUPPORTED_TYPE_FOR_OP, manip, "Can not cast from interface to non interface type:" + castTo));
						}
					}
					if (castTo instanceof HDLEnum) {
						problems.add(new Problem(UNSUPPORTED_TYPE_FOR_OP, manip, "Enums can not be casted to anything"));
					}
					if (castTo instanceof HDLPrimitive) {
						if (!(tt instanceof HDLPrimitive)) {
							problems.add(new Problem(UNSUPPORTED_TYPE_FOR_OP, manip, "Can not cast from primitve to non primitive type:" + castTo));
						}
					}
					break;
				}
			}
		}
	}

	private static void checkOpExpression(Set<Problem> problems, HDLOpExpression ope, IHDLObject node) {
		HDLTypeInferenceInfo info = null;
		final HDLPrimitives instance = HDLPrimitives.getInstance();
		switch (ope.getClassType()) {
		case HDLArithOp:
			info = instance.getArithOpType((HDLArithOp) ope);
			break;
		case HDLBitOp:
			info = instance.getBitOpType((HDLBitOp) ope);
			break;
		case HDLShiftOp:
			info = instance.getShiftOpType((HDLShiftOp) ope);
			break;
		case HDLEqualityOp:
			info = instance.getEqualityOpType((HDLEqualityOp) ope);
			break;
		default:
			throw new IllegalArgumentException("Did not expect class:" + ope.getClassType());
		}
		if (info == null) {
			throw new IllegalArgumentException("Info should not be null");
		}
		if (info.error != null) {
			problems.add(new Problem(UNSUPPORTED_TYPE_FOR_OP, node, info.error));
		}
	}

	private static void checkAnnotations(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLAnnotation[] annos = unit.getAllObjectsOf(HDLAnnotation.class, true);
		for (final HDLAnnotation hdlAnnotation : annos) {
			final Problem[] p = HDLAnnotations.validate(hdlAnnotation);
			for (final Problem problem : p) {
				problems.add(problem);
			}
		}
	}

	private static void checkCombinedAssignment(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final Collection<HDLAssignment> all = HDLQuery.select(HDLAssignment.class).from(unit).where(HDLAssignment.fType).isNotEqualTo(HDLAssignment.HDLAssignmentType.ASSGN)
				.getAll();
		for (final HDLAssignment ass : all) {
			final HDLReference ref = ass.getLeft();
			if (ref instanceof HDLUnresolvedFragment) {
				return;
			}
			final HDLOpExpression opExpression = Insulin.toOpExpression(ass);
			if (opExpression != null) {
				final IHDLObject freeze = opExpression.copyDeepFrozen(ass.getContainer());
				checkOpExpression(problems, (HDLOpExpression) freeze, ass);
				final Optional<HDLVariable> var = ((HDLResolvedRef) ref).resolveVar();
				if ((var.isPresent()) && (var.get().getRegisterConfig() == null)) {
					final HDLBlock container = ass.getContainer(HDLBlock.class);
					if ((container != null) && container.getProcess()) {
						// If the assignment is happening within a process,
						// chances
						// are that the dev is trying something legal
						continue;
					}
					problems.add(new Problem(COMBINED_ASSIGNMENT_NOT_ALLOWED, ass));
				}
			}
		}
	}

	private static void checkConstantEquals(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLEqualityOp[] equalities = unit.getAllObjectsOf(HDLEqualityOp.class, true);
		for (final HDLEqualityOp op : equalities) {
			if (skipExp(op)) {
				continue;
			}
			final Optional<BigInteger> res = ConstantEvaluate.valueOf(op, getContext(hContext, op));
			if (res.isPresent()) {
				if (res.get().equals(BigInteger.ONE)) {
					problems.add(new Problem(EQUALITY_ALWAYS_TRUE, op));
				} else {
					problems.add(new Problem(EQUALITY_ALWAYS_FALSE, op));
				}
			}
		}
	}

	public static boolean skipExp(IHDLObject op) {
		return (op.getContainer(HDLInlineFunction.class) != null) || (op.getContainer(HDLSubstituteFunction.class) != null);
	}

	private static void checkConstantBoundaries(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		// XXX Check Array Dimensions
		final Collection<HDLVariableDeclaration> constants = HDLQuery.select(HDLVariableDeclaration.class)//
				.from(unit).where(HDLVariableDeclaration.fDirection)//
				.matches(isEqualTo(HDLDirection.CONSTANT))//
				.or(isEqualTo(HDLDirection.PARAMETER)) //
				.getAll();
		for (final HDLVariableDeclaration hvd : constants) {
			for (final HDLVariable var : hvd.getVariables()) {
				final HDLExpression def = var.getDefaultValue();
				if (var.getDefaultValue() == null) {
					problems.add(new Problem(CONSTANT_NEED_DEFAULTVALUE, var));
				} else {
					HDLEvaluationContext lContext = getContext(hContext, var);
					if (lContext != null) {
						lContext = lContext.withEnumAndBool(true, true);
					}
					if (def instanceof HDLArrayInit) {
						checkConstantsArrayInit(problems, (HDLArrayInit) def, lContext);
					} else {
						assumeConstant(problems, CONSTANT_DEFAULT_VALUE_NOT_CONSTANT, null, def, lContext);
					}
				}
			}
		}
		final HDLForLoop[] forLoops = unit.getAllObjectsOf(HDLForLoop.class, true);
		for (final HDLForLoop hdlForLoop : forLoops) {
			for (final HDLRange r : hdlForLoop.getRange()) {
				final Optional<BigInteger> evalTo = ConstantEvaluate.valueOf(r.getTo(), getContext(hContext, r));
				if (!evalTo.isPresent()) {
					problems.add(new Problem(FOR_LOOP_RANGE_NOT_CONSTANT, r.getTo(), r, null));
				}
				if (r.getFrom() != null) {
					final Optional<BigInteger> evalFrom = ConstantEvaluate.valueOf(r.getFrom(), getContext(hContext, r));
					if (!evalFrom.isPresent()) {
						problems.add(new Problem(FOR_LOOP_RANGE_NOT_CONSTANT, r.getFrom(), r, null));
					}
				}
			}
		}
	}

	public static void assumeConstant(Set<Problem> problems, IErrorCode code, final HDLExpression contextNode, final HDLExpression exp, HDLEvaluationContext lContext) {
		final Optional<BigInteger> constant = ConstantEvaluate.valueOf(exp, lContext);
		if (!constant.isPresent()) {
			final Optional<? extends HDLType> typeOf = TypeExtension.typeOf(exp);
			if (typeOf.isPresent()) {
				if (typeOf.get() instanceof HDLPrimitive) {
					final HDLPrimitive prim = (HDLPrimitive) typeOf.get();
					if (prim.isNumber()) {
						problems.add(new Problem(code, exp, contextNode, null));
					} else {
						final HDLVariableRef[] refs = exp.getAllObjectsOf(HDLVariableRef.class, true);
						for (final HDLVariableRef hdlReference : refs) {
							final Optional<HDLVariable> resolveVar = hdlReference.resolveVar();
							if (resolveVar.isPresent()) {
								final HDLDirection direction = resolveVar.get().getDirection();
								if (direction != HDLDirection.CONSTANT) {
									problems.add(new Problem(code, exp, contextNode, null));
								}
							}
						}
					}
				}
			} else {
				if (!(exp instanceof HDLEnumRef)) {
					problems.add(new Problem(code, exp, contextNode, null));
				}
			}
		}
	}

	private static void checkConstantsArrayInit(Set<Problem> problems, HDLArrayInit arrayInit, HDLEvaluationContext context) {
		for (final HDLExpression exp : arrayInit.getExp()) {
			final Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(exp);
			if (!valueOf.isPresent()) {
				if (exp instanceof HDLArrayInit) {
					final HDLArrayInit hai = (HDLArrayInit) exp;
					checkConstantsArrayInit(problems, hai, context);
				} else {
					assumeConstant(problems, CONSTANT_DEFAULT_VALUE_NOT_CONSTANT, exp, arrayInit, context);
				}
			}
		}
	}

	private static void checkArrayBoundaries(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		final HDLVariable[] vars = unit.getAllObjectsOf(HDLVariable.class, true);
		for (final HDLVariable var : vars) {
			final HDLDirection dir = var.getDirection();
			if ((dir == HDLDirection.IN) || (dir == HDLDirection.INOUT) || (dir == HDLDirection.OUT)) {
				for (final HDLExpression dim : var.getDimensions()) {
					final Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(dim);
					if (!valueOf.isPresent()) {
						problems.add(new Problem(ARRAY_DIMENSIONS_NOT_CONSTANT, dim));
					}
				}
			}
		}
		final HDLVariableRef[] refs = unit.getAllObjectsOf(HDLVariableRef.class, true);
		for (final HDLVariableRef ref : refs) {
			if (skipExp(ref)) {
				continue;
			}
			final Optional<HDLVariable> resolveVar = ref.resolveVar();
			if (resolveVar.isPresent()) {
				final ArrayList<HDLExpression> dimensions = resolveVar.get().getDimensions();
				compareBoundaries(problems, hContext, ref, dimensions, ref.getArray());
				if (ref instanceof HDLInterfaceRef) {
					final HDLInterfaceRef hir = (HDLInterfaceRef) ref;
					final Optional<HDLVariable> var = hir.resolveHIf();
					if (var.isPresent()) {
						compareBoundaries(problems, hContext, ref, var.get().getDimensions(), hir.getIfArray());
					}
				}
			}
		}
	}

	/**
	 * Compares whether the actual access array fits within the declared array dimensions
	 *
	 * @param problems
	 * @param hContext
	 * @param ref
	 *            the subject upon which the error will be declared
	 * @param dimensions
	 *            the declared dimensions of the variable
	 * @param array
	 *            the accessed dimensions of the variable
	 */
	private static void compareBoundaries(Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext, HDLVariableRef ref, ArrayList<HDLExpression> dimensions,
			ArrayList<HDLExpression> array) {
		if (dimensions.size() < array.size()) {
			problems.add(new Problem(ARRAY_REFERENCE_TOO_MANY_DIMENSIONS, ref));
			return;
		} else if ((dimensions.size() > array.size())) {
			// Check whether dimensions have been left out. This is only ok when
			// it is an assignment and the other dimension is the same
			final IHDLObject container = ref.getContainer();
			if (container instanceof HDLAssignment) {
				final HDLAssignment ass = (HDLAssignment) container;
				final HDLReference left = ass.getLeft();
				if (left instanceof HDLUnresolvedFragment) {
					return;
				}
				if (left.getClassType() == HDLClass.HDLEnumRef) {
					problems.add(new Problem(ASSIGNMENT_ENUM_NOT_WRITABLE, left));
				} else {
					final HDLVariableRef varRef = (HDLVariableRef) left;
					final Optional<HDLVariable> var = varRef.resolveVar();
					if (var.isPresent()) {
						final HDLEvaluationContext context = getContext(hContext, ass);
						final ArrayList<HDLExpression> targetDim = var.get().getDimensions();
						for (int i = 0; i < varRef.getArray().size(); i++) {
							if (targetDim.size() == 0) {
								problems.add(new Problem(ARRAY_REFERENCE_TOO_MANY_DIMENSIONS, varRef));
								return;
							}
							targetDim.remove(0);
						}
						if (left != ref) {
							validateArrayAssignment(problems, context, ref, ass, left, targetDim);
						} else {
							final HDLClass classType = ass.getRight().getClassType();
							if ((classType != HDLClass.HDLVariableRef) && (classType != HDLClass.HDLInterfaceRef) && (classType != HDLClass.HDLArrayInit)) {
								problems.add(new Problem(ARRAY_WRITE_MULTI_DIMENSION, ass));
							}
						}
					}
				}
			} else if (container instanceof HDLVariable) {
				final HDLVariable var = (HDLVariable) container;
				final HDLEvaluationContext context = getContext(hContext, var);
				validateArrayAssignment(problems, context, ref, var, var, var.getDimensions());
			} else {
				problems.add(new Problem(ARRAY_REFERENCE_TOO_FEW_DIMENSIONS_IN_EXPRESSION, ref));
			}
		}
		int dim = 0;
		for (final HDLExpression arr : array) {
			final HDLEvaluationContext context = getContext(hContext, arr);
			final Optional<Range<BigInteger>> accessRangeRaw = RangeExtension.rangeOf(arr, context);
			if (!accessRangeRaw.isPresent()) {
				problems.add(new Problem(ARRAY_INDEX_NO_RANGE, arr));
				break;
			}
			final Optional<Range<BigInteger>> arrayRangeRaw = RangeExtension.rangeOf(dimensions.get(dim), context);
			if (!arrayRangeRaw.isPresent()) {
				problems.add(new Problem(ARRAY_INDEX_NO_RANGE, dimensions.get(dim)));
				break;
			}
			final Range<BigInteger> accessRange = accessRangeRaw.get();
			final Range<BigInteger> arrayRange = arrayRangeRaw.get();
			checkAccessBoundaries(accessRange, arrayRange, problems, arr, ref, false);
			dim++;
		}
	}

	/**
	 * @param accessRange
	 *            the range in which the array/bits can be acccessed
	 * @param indexRange
	 *            the range of the size that array size/ width of the type can be in
	 * @param problems
	 *            problems will be added here
	 * @param arr
	 *            the accessing {@link HDLExpression}
	 * @param ref
	 *            the reference that is accessed
	 * @param bit
	 *            when true bit access errors will be reported
	 */
	private static void checkAccessBoundaries(Range<BigInteger> accessRange, Range<BigInteger> declaredRange, Set<Problem> problems, IHDLObject arr, HDLVariableRef ref,
			boolean bit) {
		// Reduce the declaredRange to the index limits
		Range<BigInteger> indexRange;
		if (declaredRange.hasUpperBound()) {
			final BigInteger upperEndpoint = declaredRange.upperEndpoint();
			final BigInteger subtract = upperEndpoint.subtract(BigInteger.ONE);
			if (subtract.compareTo(BigInteger.ZERO) < 0) {
				// Maybe generate a warning here?
				return;
			}
			indexRange = RangeTool.createRange(BigInteger.ZERO, subtract);
		} else {
			indexRange = Range.atLeast(BigInteger.ZERO);
		}
		final String info = "Expected value range:" + indexRange;
		// Check if highest idx is negative (Definitely a problem)
		if (accessRange.hasUpperBound() && (accessRange.upperEndpoint().signum() < 0)) {
			problems.add(new Problem(bit ? BIT_ACCESS_NEGATIVE : ARRAY_INDEX_NEGATIVE, arr, ref, info).addMeta(ACCESS_RANGE, accessRange).addMeta(ARRAY_RANGE, indexRange));
			// Check if lowest idx is negative (Might be a problem)
		} else if (accessRange.hasLowerBound() && (accessRange.lowerEndpoint().signum() < 0)) {
			problems.add(new Problem(bit ? BIT_ACCESS_POSSIBLY_NEGATIVE : ARRAY_INDEX_POSSIBLY_NEGATIVE, arr, ref, info).addMeta(ACCESS_RANGE, accessRange).addMeta(ARRAY_RANGE,
					indexRange));
		}
		// Check whether the index and the access have at least something in
		// common (index 0..5 access 7..9)
		if (!indexRange.isConnected(accessRange)) {
			problems.add(
					new Problem(bit ? BIT_ACCESS_OUT_OF_BOUNDS : ARRAY_INDEX_OUT_OF_BOUNDS, arr, ref, info).addMeta(ACCESS_RANGE, accessRange).addMeta(ARRAY_RANGE, indexRange));
		} else if (accessRange.hasUpperBound() && indexRange.hasUpperBound() && (accessRange.upperEndpoint().compareTo(indexRange.upperEndpoint()) > 0)) {
			problems.add(new Problem(bit ? BIT_ACCESS_POSSIBLY_OUT_OF_BOUNDS : ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS, arr, ref, info).addMeta(ACCESS_RANGE, accessRange)
					.addMeta(ARRAY_RANGE, indexRange));
		}
	}

	private static void validateArrayAssignment(Set<Problem> problems, HDLEvaluationContext context, HDLVariableRef ref, IHDLObject ass, IHDLObject left,
			ArrayList<HDLExpression> targetDim) {
		final Optional<HDLVariable> right = ref.resolveVar();
		if (!right.isPresent()) {
			return;
		}
		final ArrayList<HDLExpression> sourceDim = right.get().getDimensions();
		for (int i = 0; i < ref.getArray().size(); i++) {
			if (sourceDim.size() == 0) {
				problems.add(new Problem(ARRAY_REFERENCE_TOO_MANY_DIMENSIONS, ref));
				return;
			}
			sourceDim.remove(0);
		}
		if (targetDim.size() != sourceDim.size()) {
			problems.add(new Problem(ARRAY_REFERENCE_NOT_SAME_DIMENSIONS, ass));
		} else {
			final HDLDirection dir = right.get().getDirection();
			if ((dir == HDLDirection.IN) || (dir == HDLDirection.INOUT) || (dir == HDLDirection.OUT)) {
				context = null;
			}
			for (int i = 0; i < targetDim.size(); i++) {
				final HDLExpression source = sourceDim.get(i);
				final Optional<BigInteger> s = ConstantEvaluate.valueOf(source, context);
				if (!s.isPresent()) {
					problems.add(new Problem(ARRAY_DIMENSIONS_NOT_CONSTANT, right.get()));
				}
				final HDLExpression target = targetDim.get(i);
				final Optional<BigInteger> t = ConstantEvaluate.valueOf(target, context);
				if (!t.isPresent()) {
					problems.add(new Problem(ARRAY_DIMENSIONS_NOT_CONSTANT, left));
				}
				if ((t.isPresent()) && (s.isPresent())) {
					if (!s.get().equals(t.get())) {
						problems.add(new Problem(ARRAY_ASSIGNMENT_NOT_SAME_DIMENSIONS, ass));
					}
				}
			}
		}
	}

	private static HDLEvaluationContext getContext(Map<HDLQualifiedName, HDLEvaluationContext> hContext, IHDLObject var) {
		final HDLUnit container = var.getContainer(HDLUnit.class);
		if (container == null) {
			return null;
		}
		if (var.getClassType() == HDLClass.HDLInterfaceRef) {
			final HDLInterfaceRef hir = (HDLInterfaceRef) var;
			final HDLInterfaceInstantiation hii = HDLQuery.select(HDLInterfaceInstantiation.class).from(container).where(HDLInstantiation.fVar)
					.lastSegmentIs(hir.getHIfRefName().getLastSegment()).getFirst();
			if (hii != null) {
				final Optional<HDLInterface> resolveHIf = hii.resolveHIf();
				if (resolveHIf.isPresent()) {
					final HDLInterface hdlInterface = resolveHIf.get();
					final HDLUnit unit = container.getLibrary().getUnit(hdlInterface.asRef());
					HDLEvaluationContext defaultContext;
					if (unit != null) {
						defaultContext = HDLEvaluationContext.createDefault(unit);
					} else {
						defaultContext = HDLEvaluationContext.createDefault(hdlInterface);
					}
					return hii.getContext(defaultContext);
				}
			}
		}
		final HDLEvaluationContext hdlEvaluationContext = hContext.get(fullNameOf(container));
		return hdlEvaluationContext;
	}

	private static void checkClockAndResetAnnotation(HDLPackage pkg, Set<Problem> problems) {
		final ArrayList<HDLUnit> units = pkg.getUnits();
		for (final HDLUnit unit : units) {
			final Collection<HDLAnnotation> clocks = HDLQuery.select(HDLAnnotation.class).from(unit).where(HDLAnnotation.fName).isEqualTo(HDLBuiltInAnnotations.clock.toString())
					.getAll();
			if (clocks.size() > 1) {
				for (final HDLAnnotation anno : clocks) {
					problems.add(new Problem(ONLY_ONE_CLOCK_ANNOTATION_ALLOWED, anno));
				}
			}
			final Collection<HDLAnnotation> resets = HDLQuery.select(HDLAnnotation.class).from(unit).where(HDLAnnotation.fName).isEqualTo(HDLBuiltInAnnotations.reset.toString())
					.getAll();
			if (resets.size() > 1) {
				for (final HDLAnnotation anno : resets) {
					problems.add(new Problem(ONLY_ONE_RESET_ANNOTATION_ALLOWED, anno));
				}
			}
		}
	}

	@Override
	public Class<?> getErrorClass() {
		return ErrorCode.class;
	}

	@Override
	public HDLAdvise advise(Problem problem) {
		return BuiltInAdvisor.advise(problem);
	}

	@Override
	public String getName() {
		return "PSHDL Validator";
	}

}

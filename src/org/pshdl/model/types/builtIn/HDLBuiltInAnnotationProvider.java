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
package org.pshdl.model.types.builtIn;

import java.math.BigInteger;
import java.util.Set;

import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.extensions.RangeExtension;
import org.pshdl.model.extensions.TypeExtension;
import org.pshdl.model.parser.PSHDLParser;
import org.pshdl.model.simulation.RangeTool;
import org.pshdl.model.utils.services.CompilerInformation.AnnotationInformation;
import org.pshdl.model.utils.services.IHDLAnnotation;
import org.pshdl.model.utils.services.IHDLAnnotationProvider;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.builtin.ErrorCode;

import com.google.common.base.Optional;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

public class HDLBuiltInAnnotationProvider implements IHDLAnnotationProvider {
	protected enum RangeCompType {
		less, less_equal, exact, greater, greater_equal
	}

	public static enum HDLBuiltInAnnotations implements IHDLAnnotation {
		/**
		 * This annotation can be used to logically group ports by name
		 */
		portGroup,
		/**
		 * Automatically generate an interface for the HDLUnit
		 */
		autoInterface,
		/**
		 * Exported signal, the assigned signal is to be exported. This is an
		 * internal annotation
		 */
		exportedSignal,
		/**
		 * Generated signal
		 */
		genSignal,
		/**
		 * Designates a bit signal as clock
		 */
		clock,
		/**
		 * Designates a bit signal as reset
		 */
		reset,
		/**
		 * Indicates a range of values that are allowed for this variable. The
		 * value are the lower bound separated by a semicolon and the upper
		 * bound. For example: -1;6 indicates that the variable can have a value
		 * of either -1,0,1,2,3,4,5,6
		 */
		range,
		/**
		 * The name of the type that should be used instead of an automatically
		 * created type during VHDL code generation. The name should start with
		 * VHDL.
		 */
		VHDLType,
		/**
		 * Indicates that this interface should be instantiated as component
		 * rather entity.
		 */
		VHDLComponent,
		/**
		 * This annotation causes the default initialization to 0 to be omitted.
		 * This MAY cause a latch to be created.
		 */
		VHDLLatchable,
		/**
		 * This annotation causes the reset value of a register to be assigned
		 * as default in the value in the declaration, instead of describing a
		 * reset behaviour.
		 */
		VHDLNoExplicitReset;
		@Override
		public String toString() {
			return "@" + name();
		}

		public boolean is(HDLAnnotation anno) {
			return anno.getName().substring(1).equals(name());
		}

		public HDLAnnotation create(String value) {
			return new HDLAnnotation().setName(toString()).setValue(value);
		}

		@Override
		public String validate(HDLAnnotation annotation) {
			final String value = annotation.getValue();
			switch (this) {
			case autoInterface:
			case genSignal:
			case VHDLLatchable:
			case VHDLNoExplicitReset:
			case reset:
			case clock:
				if (value != null)
					return this + " does not expect any arguments";
				break;
			case VHDLComponent:
				if (!((value == null) || "declare".equals(value) || "import".equals(value)))
					return this + " only accepts 'declare' or 'import' as parameter. If no parameter is supplied import is assumed.";
				break;
			case range:
				if (value == null)
					return this + " expects an argument with the expected range of the variable. The format is from;to";
				final Set<Problem> problems = Sets.newHashSet();
				checkRangeAnnotation(annotation, problems);
				if (problems.isEmpty()) {
					break;
				}
				final StringBuilder sb = new StringBuilder();
				sb.append("There are problems with the annoation:\n");
				for (final Problem problem : problems) {
					sb.append(problem.toString()).append('\n');
				}
				break;
			case VHDLType:
				if (value == null)
					return this + " expects an argument with the name of the VHDL entity to use.";
				break;
			}
			return null;
		}

		public static Optional<Range<BigInteger>> checkRangeAnnotation(HDLAnnotation range, Set<Problem> problems) {
			if (range != null) {
				if (!range.getName().equals(HDLBuiltInAnnotations.range.toString()))
					throw new IllegalArgumentException("This is for range annotations only");
				try {
					HDLExpression lowerBoundExpr = null;
					HDLExpression upperBoundExpr = null;
					final String annoValue = range.getValue().trim();
					final RangeCompType ct;
					if (annoValue.charAt(0) == '>') {
						if (annoValue.charAt(1) == '=') {
							ct = RangeCompType.greater_equal;
							lowerBoundExpr = PSHDLParser.parseExpressionString(annoValue.substring(2), problems);
						} else {
							ct = RangeCompType.greater;
							lowerBoundExpr = PSHDLParser.parseExpressionString(annoValue.substring(1), problems);
						}
					} else if (annoValue.charAt(0) == '<') {
						if (annoValue.charAt(1) == '=') {
							ct = RangeCompType.less_equal;
							upperBoundExpr = PSHDLParser.parseExpressionString(annoValue.substring(2), problems);
						} else {
							ct = RangeCompType.less;
							upperBoundExpr = PSHDLParser.parseExpressionString(annoValue.substring(1), problems);
						}
					} else {
						final String[] value = annoValue.split(";");
						lowerBoundExpr = PSHDLParser.parseExpressionString(value[0], problems);
						upperBoundExpr = PSHDLParser.parseExpressionString(value[1], problems);
						ct = RangeCompType.exact;
					}
					Optional<Range<BigInteger>> lowerRangeOpt = Optional.absent();
					if (lowerBoundExpr != null) {
						lowerRangeOpt = RangeExtension.rangeOf(lowerBoundExpr.copyDeepFrozen(range));
					}
					Optional<Range<BigInteger>> upperRangeOpt = Optional.absent();
					if (upperBoundExpr != null) {
						upperRangeOpt = RangeExtension.rangeOf(upperBoundExpr.copyDeepFrozen(range));
					}
					final Range<BigInteger> enclosingRange = calculateEnclosingRange(range);
					final Optional<Range<BigInteger>> newRange = calcNewRange(ct, lowerRangeOpt, upperRangeOpt, enclosingRange);
					if (!newRange.isPresent()) {
						problems.add(new Problem(ErrorCode.ANNOTATION_INVALID, range, "The range could not be computed"));
					}
					return newRange;
				} catch (final Exception e) {
					return Optional.absent();
				}
			}
			return Optional.absent();
		}

		public static Optional<Range<BigInteger>> calcNewRange(final RangeCompType ct, Optional<Range<BigInteger>> lowerRangeOpt, Optional<Range<BigInteger>> upperRangeOpt,
				final Range<BigInteger> enclosingRange) {
			switch (ct) {
			case greater:
				if (lowerRangeOpt.isPresent()) {
					final Range<BigInteger> lowerRange = lowerRangeOpt.get();
					if (lowerRange.hasLowerBound())
						return Optional.of(enclosingRange.intersection(Range.atLeast(lowerRange.lowerEndpoint().add(BigInteger.ONE))));
				}
				break;
			case greater_equal:
				if (lowerRangeOpt.isPresent()) {
					final Range<BigInteger> lowerRange = lowerRangeOpt.get();
					if (lowerRange.hasLowerBound())
						return Optional.of(enclosingRange.intersection(Range.atLeast(lowerRange.lowerEndpoint())));
				}
				break;
			case less:
				if (upperRangeOpt.isPresent()) {
					final Range<BigInteger> upperRange = upperRangeOpt.get();
					if (upperRange.hasUpperBound())
						return Optional.of(enclosingRange.intersection(Range.atMost(upperRange.upperEndpoint().subtract(BigInteger.ONE))));
				}
				break;
			case less_equal:
				if (upperRangeOpt.isPresent()) {
					final Range<BigInteger> upperRange = upperRangeOpt.get();
					if (upperRange.hasUpperBound())
						return Optional.of(enclosingRange.intersection(Range.atMost(upperRange.upperEndpoint())));
				}
				break;
			case exact:
				if (lowerRangeOpt.isPresent() && upperRangeOpt.isPresent()) {
					final Range<BigInteger> lowerRange = lowerRangeOpt.get();
					final Range<BigInteger> upperRange = upperRangeOpt.get();
					if (lowerRange.hasLowerBound() && upperRange.hasUpperBound())
						return Optional.of(enclosingRange.intersection(RangeTool.createRange(lowerRange.lowerEndpoint(), upperRange.upperEndpoint())));
					if (!lowerRange.hasLowerBound() && !upperRange.hasUpperBound())
						return Optional.of(enclosingRange);
					if (!lowerRange.hasLowerBound())
						return Optional.of(enclosingRange.intersection(Range.atMost(upperRange.upperEndpoint())));
					return Optional.of(enclosingRange.intersection(Range.atLeast(lowerRange.lowerEndpoint())));
				}
			}
			return Optional.of(enclosingRange);
		}

		protected static Range<BigInteger> calculateEnclosingRange(HDLAnnotation range) {
			Range<BigInteger> enclosingRange = Range.all();
			final HDLVariableDeclaration hv = range.getContainer(HDLVariableDeclaration.class);
			if (hv != null) {
				final Optional<? extends HDLType> typeOf = TypeExtension.typeOf(hv);
				if (typeOf.isPresent()) {
					final HDLType type = typeOf.get();
					if (type instanceof HDLPrimitive) {
						final HDLPrimitive prim = (HDLPrimitive) type;
						final Integer width = HDLPrimitives.getWidth(type, null);
						if (width != null) {
							switch (prim.getType()) {
							case BIT:
							case BITVECTOR:
							case STRING:
								break;
							case BOOL:
								enclosingRange = RangeTool.createRange(BigInteger.ZERO, BigInteger.ONE);
								break;
							case INT:
							case INTEGER:
								enclosingRange = HDLPrimitives.intRange(BigInteger.valueOf(width));
								break;
							case NATURAL:
							case UINT:
								enclosingRange = HDLPrimitives.uintRange(BigInteger.valueOf(width));
								break;
							}
						}
					}
				}
			}
			return enclosingRange;
		}

		@Override
		public AnnotationInformation getAnnotationInformation() {
			switch (this) {
			case autoInterface:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"Units that have this Annotation will get an interface declaration atop it's declaration", null);
			case exportedSignal:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"This is an internal annotation to show that the assigned value is to be exported", null);
			case VHDLLatchable:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"This annotation causes the default initialization to 0 to be omitted. This MAY cause a latch to be created.", null);
			case VHDLNoExplicitReset:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"This annotation causes the reset value of a register to be assigned as default in the value in the declaration, instead of describing a reset behaviour.",
						null);
			case VHDLType:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"The name of the type that should be used instead of an automatically created type during VHDL code generation. The name should start with VHDL.",
						"The name that should be used instead. It usually starts with VHDL.");
			case clock:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"Designates a bit signal to be used for $clk, which also is used by registers", null);
			case genSignal:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"When a signal is automatically generated by a generator, then it has this annotation", "The instance that caused this signal");
			case range:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"Indicates a range of values that are allowed for this variable. The value are the lower bound separated by a semicolon and the upper bound.",
						"The range in the format: 'from;to'. For example: -1;6 indicates that the variable can have a value of either -1,0,1,2,3,4,5,6");
			case reset:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"Designates a bit signal to be used for $rst, which also is used by registers", null);
			case VHDLComponent:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"Designates an interface that should be instantiated as component rather than as entity", null);
			case portGroup:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(), "Adds a signal to a logical group of signals",
						"a simple group name. Should be a simple identifier");
			}
			throw new IllegalArgumentException("Forgot to implement AnnotationInformation for:" + this);
		}
	}

	@Override
	public IHDLAnnotation[] getAnnotations() {
		return HDLBuiltInAnnotations.values();
	}

}

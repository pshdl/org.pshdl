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

import static org.pshdl.model.validation.Problem.ProblemSeverity.*;

import org.pshdl.model.utils.services.IHDLValidator.IErrorCode;
import org.pshdl.model.validation.Problem.ProblemSeverity;

public enum ErrorCode implements IErrorCode {
	// Registers
	REGISTER_UNKNOWN_ARGUMENT(ERROR),
	// RWValidation
	WRITE_ACCESS_TO_IN_PORT(ERROR), IN_PORT_NEVER_READ(WARNING), OUT_PORT_NEVER_WRITTEN(WARNING), UNUSED_VARIABLE(WARNING), INTERNAL_SIGNAL_WRITTEN_BUT_NEVER_READ(WARNING), PARAMETER_OR_CONSTANT_NEVER_READ(
			WARNING), INTERNAL_SIGNAL_READ_BUT_NEVER_WRITTEN(WARNING),
	// RWValidation Interface
	INTERFACE_OUT_PORT_NEVER_READ(WARNING), INTERFACE_OUT_WRITTEN(ERROR), INTERFACE_IN_PORT_NEVER_WRITTEN(WARNING), INTERFACE_UNUSED_PORT(WARNING),
	// Annotation check
	ONLY_ONE_CLOCK_ANNOTATION_ALLOWED(ERROR), ONLY_ONE_RESET_ANNOTATION_ALLOWED(ERROR), ANNOTATION_UNKNOWN(ERROR), ANNOTATION_INVALID(ERROR),
	// Constants
	CONSTANT_NEED_DEFAULTVALUE(ERROR), CONSTANT_DEFAULT_VALUE_NOT_CONSTANT(ERROR), FOR_LOOP_RANGE_NOT_CONSTANT(ERROR), CONSTANT_WIDTH_MISMATCH(WARNING),
	// Resolution
	UNRESOLVED_INTERFACE(ERROR), UNRESOLVED_TYPE(ERROR), UNRESOLVED_ENUM(ERROR), UNRESOLVED_VARIABLE(ERROR), UNRESOLVED_REFERENCE(ERROR), UNRESOLVED_FRAGMENT(ERROR), UNRESOLVED_FUNCTION(
			ERROR),
	// Array access
	ARRAY_INDEX_POSSIBLY_NEGATIVE(WARNING), ARRAY_INDEX_NEGATIVE(ERROR), ARRAY_REFERENCE_NOT_SAME_DIMENSIONS(ERROR), ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS(WARNING), ARRAY_INDEX_OUT_OF_BOUNDS(
			ERROR), ARRAY_REFERENCE_TOO_MANY_DIMENSIONS(ERROR),
	// Possible programming problems
	EQUALITY_ALWAYS_TRUE(WARNING), EQUALITY_ALWAYS_FALSE(WARNING),
	// Various
	COMBINED_ASSIGNMENT_NOT_ALLOWED(ERROR), UNSUPPORTED_TYPE_FOR_OP(ERROR), MULTI_PROCESS_WRITE(ERROR), NO_SUCH_FUNCTION(ERROR), IN_PORT_CANT_REGISTER(ERROR), CONCAT_TYPE_NOT_ALLOWED(
			ERROR), BOOL_NEGATE_NUMERIC_NOT_SUPPORTED(ERROR),
	// Generator
	GENERATOR_NOT_KNOWN(ERROR), GENERATOR_ERROR(ERROR), GENERATOR_WARNING(WARNING), GENERATOR_INFO(INFO), INLINE_FUNCTION_NO_TYPE(ERROR), SUBSTITUTE_FUNCTION_NO_TYPE(ERROR),
	// Variable naming
	VARIABLE_SAME_NAME(ERROR), VARIABLE_KEYWORD_NAME(ERROR), VARIABLE_NAME_NOT_RECOMMENDED(WARNING), VARIABLE_SAME_NAME_DIFFERENT_CASE(ERROR), VARIABLE_SCOPE_SAME_NAME(ERROR), VARIABLE_SCOPE_SAME_NAME_DIFFERENT_CASE(
			ERROR), TYPE_SAME_NAME(ERROR), FUNCTION_SAME_NAME(ERROR), GLOBAL_VAR_SAME_NAME(ERROR),
	// PARAMETER
	PARAMETER_NOT_FOUND(ERROR),
	// Switch
	SWITCH_MULTIPLE_DEFAULT(ERROR), SWITCH_NO_DEFAULT(ERROR), SWITCH_LABEL_NOT_CONSTANT(ERROR), SWITCH_LABEL_DUPLICATE(ERROR), SWITCH_CASE_NEEDS_WIDTH(ERROR), SWITCH_CASE_NEEDS_CONSTANT_WIDTH(
			ERROR), SWITCH_LABEL_WRONG_ENUM(ERROR),
	// Assignment
	ASSIGNMENT_NOT_ENUM(ERROR), ASSIGNMENT_NOT_SUPPORTED(ERROR), ASSIGNMENT_NOT_PRIMITIVE(ERROR), ASSIGNMENT_CLIPPING_WILL_OCCUR(WARNING), ARRAY_INDEX_NO_RANGE(ERROR), ARRAY_REFERENCE_TOO_FEW_DIMENSIONS_IN_EXPRESSION(
			ERROR), ASSIGNMENT_ENUM_NOT_WRITABLE(ERROR), ARRAY_WRITE_MULTI_DIMENSION(ERROR), ARRAY_DIMENSIONS_NOT_CONSTANT(ERROR), ARRAY_ASSIGNMENT_NOT_SAME_DIMENSIONS(ERROR), BIT_ACCESS_NOT_POSSIBLE(
			ERROR), BIT_ACCESS_NOT_POSSIBLE_ON_TYPE(ERROR), UNKNOWN_RANGE(WARNING), RANGE_OVERLAP(WARNING), RANGE_NOT_UP(ERROR), RANGE_NOT_DOWN(ERROR), CONSTANT_PORT_CANT_REGISTER(
			ERROR),
	// Clock /Reset
	CLOCK_UNKNOWN_WIDTH(ERROR), CLOCK_NOT_BIT(ERROR), RESET_UNKNOWN_WIDTH(ERROR), RESET_NOT_BIT(ERROR),
	// Globals
	GLOBAL_CANT_REGISTER(ERROR), GLOBAL_NOT_CONSTANT(ERROR);

	public final ProblemSeverity severity;

	ErrorCode(ProblemSeverity severity) {
		this.severity = severity;
	}

	@Override
	public ProblemSeverity getSeverity() {
		return severity;
	}
}

package de.tuhh.ict.pshdl.model.validation;

import static de.tuhh.ict.pshdl.model.validation.Problem.ProblemSeverity.*;
import de.tuhh.ict.pshdl.model.validation.Problem.ProblemSeverity;

public enum ErrorCode {
	// RWValidation
	WRITE_ACCESS_TO_IN_PORT(ERROR), IN_PORT_NEVER_READ(WARNING), OUT_PORT_NEVER_WRITTEN(WARNING), UNUSED_VARIABLE(WARNING), INTERNAL_SIGNAL_WRITTEN_BUT_NEVER_READ(WARNING), PARAMETER_OR_CONSTANT_NEVER_READ(
			WARNING), INTERNAL_SIGNAL_READ_BUT_NEVER_WRITTEN(WARNING),
	// RWValidation Interface
	INTERFACE_OUT_PORT_NEVER_READ(WARNING), INTERFACE_OUT_WRITTEN(ERROR), INTERFACE_IN_PORT_NEVER_WRITTEN(WARNING), INTERFACE_UNUSED_PORT(WARNING),
	// Annotation check
	ONLY_ONE_CLOCK_ANNOTATION_ALLOWED(ERROR), ONLY_ONE_RESET_ANNOTATION_ALLOWED(ERROR),
	// Constants
	CONSTANT_NEED_DEFAULTVALUE(ERROR), CONSTANT_DEFAULT_VALUE_NOT_CONSTANT(ERROR), FOR_LOOP_RANGE_NOT_CONSTANT(ERROR),
	// Resolution
	UNRESOLVED_INTERFACE(ERROR), UNRESOLVED_TYPE(ERROR), UNRESOLVED_ENUM(ERROR), UNRESOLVED_VARIABLE(ERROR), UNRESOLVED_REFERENCE(ERROR),
	// Array access
	ARRAY_INDEX_NOT_PRIMITIVE(ERROR), ARRAY_INDEX_POSSIBLY_NEGATIVE(WARNING), ARRAY_INDEX_NEGATIVE(ERROR), ARRAY_REFERENCE_NOT_SAME_DIMENSIONS(ERROR), ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS(
			WARNING), ARRAY_INDEX_OUT_OF_BOUNDS(ERROR),
	// Possible programming problems
	EQUALITY_ALWAYS_TRUE(WARNING), EQUALITY_ALWAYS_FALSE(WARNING);

	public final ProblemSeverity severity;

	ErrorCode(ProblemSeverity severity) {
		this.severity = severity;
	}
}
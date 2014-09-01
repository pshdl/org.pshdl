package org.pshdl.model.simulation.codegenerator;

public @interface Option {

	String description();

	String optionName();

	boolean hasArg() default false;
}

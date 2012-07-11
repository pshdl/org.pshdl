package de.tuhh.ict.pshdl.model.utils.services;

public interface IHDLAnnotation {
	/**
	 * Returns the name of the annotation without the @ symbol
	 * 
	 * @return
	 */
	String name();

	/**
	 * Validates the contents of the annotation and returns a message to the
	 * user if it is not correct
	 * 
	 * @return
	 */
	String validate(String value);
}

package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

public class HDLVariableDeclaration extends AbstractHDLVariableDeclaration {
	/**
	 * Constructs a new instance of {@link HDLVariableDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>,
	 *            {@link HDLDirection#INTERNAL} is used as default.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param primitive
	 *            the value for primitive. Can be <code>null</code>.
	 * @param variables
	 *            the value for variables. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLVariableDeclaration(HDLObject container, HDLRegisterConfig register, HDLDirection direction, ArrayList<HDLAnnotation> annotations, HDLQualifiedName type,
			HDLPrimitive primitive, ArrayList<HDLVariable> variables, boolean validate) {
		super(container, register, direction, annotations, type, primitive, variables, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLVariableDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>,
	 *            {@link HDLDirection#INTERNAL} is used as default.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param primitive
	 *            the value for primitive. Can be <code>null</code>.
	 * @param variables
	 *            the value for variables. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 */
	public HDLVariableDeclaration(HDLObject container, HDLRegisterConfig register, HDLDirection direction, ArrayList<HDLAnnotation> annotations, HDLQualifiedName type,
			HDLPrimitive primitive, ArrayList<HDLVariable> variables) {
		this(container, register, direction, annotations, type, primitive, variables, true);
	}

	public HDLVariableDeclaration() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLVariableDeclaration;
	}

	public static enum HDLDirection {
		IN("in"), OUT("out"), INOUT("inout"), PARAMETER("param"), CONSTANT("const"), INTERNAL(""), HIDDEN("<HIDDEN>");
		String str;

		HDLDirection(String op) {
			this.str = op;
		}

		public static HDLDirection getOp(String op) {
			for (HDLDirection ass : values()) {
				if (ass.str.equals(op)) {
					return ass;
				}
			}
			return null;
		}

		@Override
		public String toString() {
			return str;
		}
	}

	// $CONTENT-BEGIN$

	public HDLVariableDeclaration(HDLObject container, HDLRegisterConfig register, HDLDirection direction, ArrayList<HDLAnnotation> annotations, HDLPrimitive primitive,
			ArrayList<HDLVariable> variables) {
		this(container, register, direction, annotations, primitive.asRef(), primitive, variables, true);
	}

	@Override
	public HDLType resolveType() {
		if (getPrimitive() != null)
			return getPrimitive();
		return super.resolveType();
	}

	public AbstractHDLVariableDeclaration setType(HDLType resolveType) {
		HDLVariableDeclaration setType = super.setType(resolveType.asRef());
		if (resolveType instanceof HDLPrimitive) {
			HDLPrimitive prim = (HDLPrimitive) resolveType;
			return setType.setPrimitive(prim);
		}
		return setType;
	}

	// $CONTENT-END$

}

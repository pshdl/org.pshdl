package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

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
	 * @param variables
	 *            the value for variables. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLVariableDeclaration(HDLObject container, HDLRegisterConfig register, HDLDirection direction, ArrayList<HDLAnnotation> annotations, HDLQualifiedName type,
			ArrayList<HDLVariable> variables, boolean validate) {
		super(container, register, direction, annotations, type, variables, validate);
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
	 * @param variables
	 *            the value for variables. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 */
	public HDLVariableDeclaration(HDLObject container, HDLRegisterConfig register, HDLDirection direction, ArrayList<HDLAnnotation> annotations, HDLQualifiedName type,
			ArrayList<HDLVariable> variables) {
		this(container, register, direction, annotations, type, variables, true);
	}

	public HDLVariableDeclaration() {
		super();
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
	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append("\n").append(spacing).append("new HDLVariableDeclaration(");
		sb.append("null");
		if (type == null)
			sb.append(", null");
		else {
			// Fix for HDLPrimitives, do not just create reference!
			if (type.getLastSegment().startsWith("#"))
				sb.append(", ").append(resolveType().toConstructionString(spacing));
			else
				sb.append(", HDLQualifiedName.create(\"").append(type).append("\")");
		}
		if (variables == null)
			sb.append(", null");
		else {
			if (variables.size() > 0) {
				sb.append(",\n");
				sb.append(spacing + "\t").append("HDLObject.asList(");
				first = true;
				for (HDLVariable o : variables) {
					if (!first)
						sb.append(", ");
					first = false;
					sb.append(o.toConstructionString(spacing + "\t\t"));
				}
				sb.append("\n" + spacing + "\t").append(")");
			} else {
				sb.append(", new ArrayList<HDLVariable>()");
			}
		}
		sb.append(")");
		return sb.toString();
	}

	// $CONTENT-END$

}

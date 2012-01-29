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
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param variables
	 *            the value for variables. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLVariableDeclaration(HDLObject container, HDLQualifiedName type, ArrayList<HDLVariable> variables, boolean validate) {
		super(container, type, variables, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLVariableDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param variables
	 *            the value for variables. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 */
	public HDLVariableDeclaration(HDLObject container, HDLQualifiedName type, ArrayList<HDLVariable> variables) {
		this(container, type, variables, true);
	}
	public HDLVariableDeclaration() {
		super();
	}
	
//$CONTENT-BEGIN$
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

//$CONTENT-END$
	
}	

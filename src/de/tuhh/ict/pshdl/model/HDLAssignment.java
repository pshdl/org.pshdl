package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;


public class HDLAssignment extends AbstractHDLAssignment {
	/**
	 * Constructs a new instance of {@link HDLAssignment}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. If <code>null</code>, {@link HDLAssignmentType#ASSGN} is used as default.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLAssignment(HDLObject container, HDLReference left, HDLAssignmentType type, HDLExpression right, boolean validate) {
		super(container, left, type, right, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLAssignment}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. If <code>null</code>, {@link HDLAssignmentType#ASSGN} is used as default.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 */
	public HDLAssignment(HDLObject container, HDLReference left, HDLAssignmentType type, HDLExpression right) {
		this(container, left, type, right, true);
	}
	public HDLAssignment() {
		super();
	}
	 public static enum HDLAssignmentType {
	ASSGN("="), ADD_ASSGN("+="), SUB_ASSGN("-="), MUL_ASSGN("*="), DIV_ASSGN("/="), MOD_ASSGN("%="), AND_ASSGN("&="), XOR_ASSGN("^="), OR_ASSGN("|="), SLL_ASSGN("<<="), SRL_ASSGN(">>>="), SRA_ASSGN(">>=");	
		String str;
	
		HDLAssignmentType(String op) {
			this.str = op;
		}
	
		public static HDLAssignmentType getOp(String op) {
			for (HDLAssignmentType ass : values()) {
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
	
//$CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return Collections.emptyList();
	}

	@Override
	protected List<HDLInterface> doGetInterfaceDeclarations() {
		return Collections.emptyList();
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		return Collections.emptyList();
	}
//$CONTENT-END$
	
}	

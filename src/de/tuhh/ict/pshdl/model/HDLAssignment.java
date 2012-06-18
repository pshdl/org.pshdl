package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLAssignment contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLReference left. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLAssignmentType type. If <code>null</code>,
 * {@link HDLAssignmentType#ASSGN} is used as default.</li>
 * <li>HDLExpression right. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLAssignment extends AbstractHDLAssignment {
	/**
	 * Constructs a new instance of {@link HDLAssignment}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. If <code>null</code>,
	 *            {@link HDLAssignmentType#ASSGN} is used as default.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLAssignment(int containerID, @Nullable HDLObject container, @NonNull HDLReference left, @Nullable HDLAssignmentType type, @NonNull HDLExpression right,
			boolean validate) {
		super(containerID, container, left, type, right, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLAssignment}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. If <code>null</code>,
	 *            {@link HDLAssignmentType#ASSGN} is used as default.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 */
	public HDLAssignment(int containerID, @Nullable HDLObject container, @NonNull HDLReference left, @Nullable HDLAssignmentType type, @NonNull HDLExpression right) {
		this(containerID, container, left, type, right, true);
	}

	public HDLAssignment() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLAssignment;
	}

	public static enum HDLAssignmentType {
		ASSGN("="), ADD_ASSGN("+="), SUB_ASSGN("-="), MUL_ASSGN("*="), DIV_ASSGN("/="), MOD_ASSGN("%="), AND_ASSGN("&="), XOR_ASSGN("^="), OR_ASSGN("|="), SLL_ASSGN("<<="), SRL_ASSGN(
				">>>="), SRA_ASSGN(">>=");
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

	/**
	 * The accessor for the field left which is of type HDLReference.
	 */
	public static HDLFieldAccess<HDLAssignment, HDLReference> fLeft = new HDLFieldAccess<HDLAssignment, HDLReference>() {
		@Override
		public HDLReference getValue(HDLAssignment obj) {
			if (obj == null)
				return null;
			return obj.getLeft();
		}
	};
	/**
	 * The accessor for the field type which is of type HDLAssignmentType.
	 */
	public static HDLFieldAccess<HDLAssignment, HDLAssignmentType> fType = new HDLFieldAccess<HDLAssignment, HDLAssignmentType>() {
		@Override
		public HDLAssignmentType getValue(HDLAssignment obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}
	};
	/**
	 * The accessor for the field right which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLAssignment, HDLExpression> fRight = new HDLFieldAccess<HDLAssignment, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLAssignment obj) {
			if (obj == null)
				return null;
			return obj.getRight();
		}
	};

	// $CONTENT-BEGIN$
	@Override
	public List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return Collections.emptyList();
	}

	@Override
	public List<HDLInterface> doGetInterfaceDeclarations() {
		return Collections.emptyList();
	}

	@Override
	public List<HDLVariable> doGetVariables() {
		return Collections.emptyList();
	}
	// $CONTENT-END$

}

package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLAssignment contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
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
	public HDLAssignment(@Nullable IHDLObject container, @NonNull HDLReference left, @Nullable HDLAssignmentType type, @NonNull HDLExpression right, boolean validate) {
		super(container, left, type, right, validate);
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

		@Nullable
		public static HDLAssignmentType getOp(String op) {
			for (HDLAssignmentType ass : values()) {
				if (ass.str.equals(op))
					return ass;
			}
			return null;
		}

		@Override
		@NonNull
		public String toString() {
			return str;
		}
	}

	/**
	 * The accessor for the field left which is of type HDLReference.
	 */
	public static HDLFieldAccess<HDLAssignment, HDLReference> fLeft = new HDLFieldAccess<HDLAssignment, HDLReference>("left") {
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
	public static HDLFieldAccess<HDLAssignment, HDLAssignmentType> fType = new HDLFieldAccess<HDLAssignment, HDLAssignmentType>("type") {
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
	public static HDLFieldAccess<HDLAssignment, HDLExpression> fRight = new HDLFieldAccess<HDLAssignment, HDLExpression>("right") {
		@Override
		public HDLExpression getValue(HDLAssignment obj) {
			if (obj == null)
				return null;
			return obj.getRight();
		}
	};
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}

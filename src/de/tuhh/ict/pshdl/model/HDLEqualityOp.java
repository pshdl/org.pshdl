package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLEqualityOp contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression left. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression right. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLEqualityOpType type. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLEqualityOp extends AbstractHDLEqualityOp {
	/**
	 * Constructs a new instance of {@link HDLEqualityOp}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLEqualityOp(@Nullable IHDLObject container, @NonNull HDLExpression left, @NonNull HDLExpression right, @NonNull HDLEqualityOpType type, boolean validate) {
		super(container, left, right, type, validate);
	}

	public HDLEqualityOp() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLEqualityOp;
	}

	public static enum HDLEqualityOpType {
		EQ("=="), NOT_EQ("!="), LESS("<"), LESS_EQ("<="), GREATER(">"), GREATER_EQ(">=");
		String str;

		HDLEqualityOpType(String op) {
			this.str = op;
		}

		public static HDLEqualityOpType getOp(String op) {
			for (HDLEqualityOpType ass : values()) {
				if (ass.str.equals(op))
					return ass;
			}
			return null;
		}

		@Override
		public String toString() {
			return str;
		}
	}

	/**
	 * The accessor for the field type which is of type HDLEqualityOpType.
	 */
	public static HDLFieldAccess<HDLEqualityOp, HDLEqualityOpType> fType = new HDLFieldAccess<HDLEqualityOp, HDLEqualityOpType>("type") {
		@Override
		public HDLEqualityOpType getValue(HDLEqualityOp obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

package de.tuhh.ict.pshdl.model;

import java.math.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLRange contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression from. Can be <code>null</code>.</li>
 * <li>HDLExpression to. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLRange extends AbstractHDLRange {
	/**
	 * Constructs a new instance of {@link HDLRange}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param from
	 *            the value for from. Can be <code>null</code>.
	 * @param to
	 *            the value for to. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLRange(@Nullable IHDLObject container, @Nullable HDLExpression from, @NonNull HDLExpression to, boolean validate) {
		super(container, from, to, validate);
	}

	public HDLRange() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLRange;
	}

	/**
	 * The accessor for the field from which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLRange, HDLExpression> fFrom = new HDLFieldAccess<HDLRange, HDLExpression>("from") {
		@Override
		public HDLExpression getValue(HDLRange obj) {
			if (obj == null) {
				return null;
			}
			return obj.getFrom();
		}
	};
	/**
	 * The accessor for the field to which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLRange, HDLExpression> fTo = new HDLFieldAccess<HDLRange, HDLExpression>("to") {
		@Override
		public HDLExpression getValue(HDLRange obj) {
			if (obj == null) {
				return null;
			}
			return obj.getTo();
		}
	};

	// $CONTENT-BEGIN$
	/**
	 * Calculates the width of the Expression as if it used as a downto (the
	 * most common case when the width is needed)
	 * 
	 * @return
	 */
	public HDLExpression getWidth() {
		if (getFrom() == null) {
			return new HDLLiteral().setVal("1");
		}
		if (getTo() != null) {
			if (BigInteger.ZERO.equals(getTo().constantEvaluate(null))) {
				HDLArithOp simpleWith = new HDLArithOp().setLeft(getFrom()).setType(HDLArithOpType.PLUS).setRight(HDLLiteral.get(1));
				return HDLPrimitives.simplifyWidth(this, simpleWith);
			}
		}
		HDLArithOp rangeDist = new HDLArithOp().setLeft(getFrom()).setType(HDLArithOpType.MINUS).setRight(getTo());
		HDLExpression absRange = PSHDLLib.ABS.getReplacementExpressionArgs(this, rangeDist);
		HDLArithOp width = new HDLArithOp().setLeft(absRange).setType(HDLArithOpType.PLUS).setRight(HDLLiteral.get(1));
		return HDLPrimitives.simplifyWidth(this, width);
	}
	// $CONTENT-END$

}

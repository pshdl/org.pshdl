package de.tuhh.ict.pshdl.model;

import java.math.*;

import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLRange contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
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
	public HDLRange(HDLObject container, HDLExpression from, HDLExpression to, boolean validate) {
		super(container, from, to, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLRange}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param from
	 *            the value for from. Can be <code>null</code>.
	 * @param to
	 *            the value for to. Can <b>not</b> be <code>null</code>.
	 */
	public HDLRange(HDLObject container, HDLExpression from, HDLExpression to) {
		this(container, from, to, true);
	}

	public HDLRange() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLRange;
	}

	public static HDLFieldAccess<HDLRange, HDLExpression> fFrom = new HDLFieldAccess<HDLRange, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLRange obj) {
			if (obj == null)
				return null;
			return obj.getFrom();
		}
	};
	public static HDLFieldAccess<HDLRange, HDLExpression> fTo = new HDLFieldAccess<HDLRange, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLRange obj) {
			if (obj == null)
				return null;
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
		if (getFrom() == null)
			return new HDLLiteral().setVal("1");
		if (getTo() != null) {
			if (BigInteger.ZERO.equals(getTo().constantEvaluate(null)))
				return new HDLArithOp().setLeft(getFrom()).setType(HDLArithOpType.PLUS).setRight(HDLLiteral.get(1));
		}
		HDLArithOp rangeDist = new HDLArithOp().setLeft(getFrom()).setType(HDLArithOpType.MINUS).setRight(getTo());
		HDLFunction absRange = new HDLFunction().setName("abs").addParams(rangeDist);
		HDLArithOp width = new HDLArithOp().setLeft(absRange).setType(HDLArithOpType.PLUS).setRight(new HDLLiteral(null, "1"));
		return HDLPrimitives.simplifyWidth(this, width);
	}
	// $CONTENT-END$

}

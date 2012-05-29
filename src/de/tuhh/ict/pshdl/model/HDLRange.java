package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLRange contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression from. Can be <code>null</code>.</li>
 * <li>HDLExpression to. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
@SuppressWarnings("all")
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

	public HDLClass getClassType() {
		return HDLClass.HDLRange;
	}

	// $CONTENT-BEGIN$
	public HDLExpression getWidth() {
		if (getFrom() == null)
			return new HDLLiteral().setVal("1");
		HDLArithOp rangeDist = new HDLArithOp().setLeft(getTo()).setType(HDLArithOpType.MINUS).setRight(getFrom());
		HDLFunction absRange = new HDLFunction().setName("abs").addParams(rangeDist);
		return new HDLArithOp().setLeft(absRange).setType(HDLArithOpType.PLUS).setRight(new HDLLiteral(null, "1"));
	}
	// $CONTENT-END$

}

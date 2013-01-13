package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLIfStatement contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression ifExp. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> thenDo. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> elseDo. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLIfStatement extends AbstractHDLIfStatement {
	/**
	 * Constructs a new instance of {@link HDLIfStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param ifExp
	 *            the value for ifExp. Can <b>not</b> be <code>null</code>.
	 * @param thenDo
	 *            the value for thenDo. Can be <code>null</code>.
	 * @param elseDo
	 *            the value for elseDo. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLIfStatement(@Nullable IHDLObject container, @NonNull HDLExpression ifExp, @Nullable ArrayList<HDLStatement> thenDo, @Nullable ArrayList<HDLStatement> elseDo,
			boolean validate) {
		super(container, ifExp, thenDo, elseDo, validate);
	}

	public HDLIfStatement() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLIfStatement;
	}

	/**
	 * The accessor for the field ifExp which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLIfStatement, HDLExpression> fIfExp = new HDLFieldAccess<HDLIfStatement, HDLExpression>("ifExp") {
		@Override
		public HDLExpression getValue(HDLIfStatement obj) {
			if (obj == null) {
				return null;
			}
			return obj.getIfExp();
		}
	};
	/**
	 * The accessor for the field thenDo which is of type
	 * ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLIfStatement, ArrayList<HDLStatement>> fThenDo = new HDLFieldAccess<HDLIfStatement, ArrayList<HDLStatement>>("thenDo") {
		@Override
		public ArrayList<HDLStatement> getValue(HDLIfStatement obj) {
			if (obj == null) {
				return null;
			}
			return obj.getThenDo();
		}
	};
	/**
	 * The accessor for the field elseDo which is of type
	 * ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLIfStatement, ArrayList<HDLStatement>> fElseDo = new HDLFieldAccess<HDLIfStatement, ArrayList<HDLStatement>>("elseDo") {
		@Override
		public ArrayList<HDLStatement> getValue(HDLIfStatement obj) {
			if (obj == null) {
				return null;
			}
			return obj.getElseDo();
		}
	};
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}

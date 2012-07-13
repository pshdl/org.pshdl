package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLSwitchStatement contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression caseExp. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLSwitchCaseStatement> cases. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLSwitchStatement extends AbstractHDLSwitchStatement {
	/**
	 * Constructs a new instance of {@link HDLSwitchStatement}
	 * 
	 * @param objectID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param caseExp
	 *            the value for caseExp. Can <b>not</b> be <code>null</code>.
	 * @param cases
	 *            the value for cases. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLSwitchStatement(int objectID, @Nullable HDLObject container, @NonNull HDLExpression caseExp, @Nullable ArrayList<HDLSwitchCaseStatement> cases, boolean validate,
			boolean updateContainer) {
		super(objectID, container, caseExp, cases, validate, updateContainer);
	}

	/**
	 * Constructs a new instance of {@link HDLSwitchStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param caseExp
	 *            the value for caseExp. Can <b>not</b> be <code>null</code>.
	 * @param cases
	 *            the value for cases. Can be <code>null</code>.
	 */
	public HDLSwitchStatement(int objectID, @Nullable HDLObject container, @NonNull HDLExpression caseExp, @Nullable ArrayList<HDLSwitchCaseStatement> cases) {
		this(objectID, container, caseExp, cases, true, true);
	}

	public HDLSwitchStatement() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLSwitchStatement;
	}

	/**
	 * The accessor for the field caseExp which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLSwitchStatement, HDLExpression> fCaseExp = new HDLFieldAccess<HDLSwitchStatement, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLSwitchStatement obj) {
			if (obj == null)
				return null;
			return obj.getCaseExp();
		}
	};
	/**
	 * The accessor for the field cases which is of type
	 * ArrayList<HDLSwitchCaseStatement>.
	 */
	public static HDLFieldAccess<HDLSwitchStatement, ArrayList<HDLSwitchCaseStatement>> fCases = new HDLFieldAccess<HDLSwitchStatement, ArrayList<HDLSwitchCaseStatement>>() {
		@Override
		public ArrayList<HDLSwitchCaseStatement> getValue(HDLSwitchStatement obj) {
			if (obj == null)
				return null;
			return obj.getCases();
		}
	};
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}

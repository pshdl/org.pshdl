package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLSwitchCaseStatement contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression label. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> dos. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLSwitchCaseStatement extends AbstractHDLSwitchCaseStatement {
	/**
	 * Constructs a new instance of {@link HDLSwitchCaseStatement}
	 * 
	 * @param objectID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param label
	 *            the value for label. Can be <code>null</code>.
	 * @param dos
	 *            the value for dos. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLSwitchCaseStatement(int objectID, @Nullable HDLObject container, @Nullable HDLExpression label, @Nullable ArrayList<HDLStatement> dos, boolean validate,
			boolean updateContainer) {
		super(objectID, container, label, dos, validate, updateContainer);
	}

	/**
	 * Constructs a new instance of {@link HDLSwitchCaseStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param label
	 *            the value for label. Can be <code>null</code>.
	 * @param dos
	 *            the value for dos. Can be <code>null</code>.
	 */
	public HDLSwitchCaseStatement(int objectID, @Nullable HDLObject container, @Nullable HDLExpression label, @Nullable ArrayList<HDLStatement> dos) {
		this(objectID, container, label, dos, true, true);
	}

	public HDLSwitchCaseStatement() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLSwitchCaseStatement;
	}

	/**
	 * The accessor for the field label which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLSwitchCaseStatement, HDLExpression> fLabel = new HDLFieldAccess<HDLSwitchCaseStatement, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLSwitchCaseStatement obj) {
			if (obj == null)
				return null;
			return obj.getLabel();
		}
	};
	/**
	 * The accessor for the field dos which is of type ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLSwitchCaseStatement, ArrayList<HDLStatement>> fDos = new HDLFieldAccess<HDLSwitchCaseStatement, ArrayList<HDLStatement>>() {
		@Override
		public ArrayList<HDLStatement> getValue(HDLSwitchCaseStatement obj) {
			if (obj == null)
				return null;
			return obj.getDos();
		}
	};
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}

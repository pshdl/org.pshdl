/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for
 *     reasonable and customary use in describing the origin of the Work.
 *
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package org.pshdl.model;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.impl.AbstractHDLSwitchStatement;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLSwitchStatement contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression caseExp. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLSwitchCaseStatement> cases. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLSwitchStatement extends AbstractHDLSwitchStatement {
	/**
	 * Constructs a new instance of {@link HDLSwitchStatement}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param caseExp
	 *            the value for caseExp. Can <b>not</b> be <code>null</code>.
	 * @param cases
	 *            the value for cases. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLSwitchStatement(int id, @Nullable IHDLObject container, @Nonnull HDLExpression caseExp, @Nullable Iterable<HDLSwitchCaseStatement> cases, boolean validate) {
		super(id, container, caseExp, cases, validate);
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
	public static HDLFieldAccess<HDLSwitchStatement, HDLExpression> fCaseExp = new HDLFieldAccess<HDLSwitchStatement, HDLExpression>("caseExp") {
		@Override
		public HDLExpression getValue(HDLSwitchStatement obj) {
			if (obj == null)
				return null;
			return obj.getCaseExp();
		}

		@Override
		public HDLSwitchStatement setValue(HDLSwitchStatement obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setCaseExp(value);
		}
	};
	/**
	 * The accessor for the field cases which is of type
	 * ArrayList<HDLSwitchCaseStatement>.
	 */
	public static HDLFieldAccess<HDLSwitchStatement, ArrayList<HDLSwitchCaseStatement>> fCases = new HDLFieldAccess<HDLSwitchStatement, ArrayList<HDLSwitchCaseStatement>>("cases") {
		@Override
		public ArrayList<HDLSwitchCaseStatement> getValue(HDLSwitchStatement obj) {
			if (obj == null)
				return null;
			return obj.getCases();
		}

		@Override
		public HDLSwitchStatement setValue(HDLSwitchStatement obj, ArrayList<HDLSwitchCaseStatement> value) {
			if (obj == null)
				return null;
			return obj.setCases(value);
		}
	};
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}

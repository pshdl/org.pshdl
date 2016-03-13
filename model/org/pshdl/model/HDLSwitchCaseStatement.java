/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *
 *     Copyright (C) 2014 Karsten Becker (feedback (at) pshdl (dot) org)
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

import javax.annotation.Nullable;

import org.pshdl.model.impl.AbstractHDLSwitchCaseStatement;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLSwitchCaseStatement contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression label. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLStatement&gt; dos. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLSwitchCaseStatement extends AbstractHDLSwitchCaseStatement {
	/**
	 * Constructs a new instance of {@link HDLSwitchCaseStatement}
	 *
	 * @param id
	 *            a unique ID for this particular node
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param label
	 *            the value for label. Can be <code>null</code>.
	 * @param dos
	 *            the value for dos. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLSwitchCaseStatement(int id, @Nullable IHDLObject container, @Nullable HDLExpression label, @Nullable Iterable<HDLStatement> dos, boolean validate) {
		super(id, container, label, dos, validate);
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
	public static HDLFieldAccess<HDLSwitchCaseStatement, HDLExpression> fLabel = new HDLFieldAccess<HDLSwitchCaseStatement, HDLExpression>("label", HDLExpression.class,
			HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLExpression getValue(HDLSwitchCaseStatement obj) {
			if (obj == null)
				return null;
			return obj.getLabel();
		}

		@Override
		public HDLSwitchCaseStatement setValue(HDLSwitchCaseStatement obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setLabel(value);
		}
	};
	/**
	 * The accessor for the field dos which is of type
	 * ArrayList&lt;HDLStatement&gt;.
	 */
	public static HDLFieldAccess<HDLSwitchCaseStatement, ArrayList<HDLStatement>> fDos = new HDLFieldAccess<HDLSwitchCaseStatement, ArrayList<HDLStatement>>("dos",
			HDLStatement.class, HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLStatement> getValue(HDLSwitchCaseStatement obj) {
			if (obj == null)
				return null;
			return obj.getDos();
		}

		@Override
		public HDLSwitchCaseStatement setValue(HDLSwitchCaseStatement obj, ArrayList<HDLStatement> value) {
			if (obj == null)
				return null;
			return obj.setDos(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (label == obj)
			return fLabel;
		if (dos.contains(obj))
			return fDos;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}

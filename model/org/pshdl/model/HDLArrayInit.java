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

import org.pshdl.model.impl.AbstractHDLArrayInit;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLArrayInit contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLExpression&gt; exp. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLArrayInit extends AbstractHDLArrayInit {
	/**
	 * Constructs a new instance of {@link HDLArrayInit}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param exp
	 *            the value for exp. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLArrayInit(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLExpression> exp, boolean validate) {
		super(id, container, exp, validate);
	}

	public HDLArrayInit() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLArrayInit;
	}

	/**
	 * The accessor for the field exp which is of type
	 * ArrayList&lt;HDLExpression&gt;.
	 */
	public static HDLFieldAccess<HDLArrayInit, ArrayList<HDLExpression>> fExp = new HDLFieldAccess<HDLArrayInit, ArrayList<HDLExpression>>("exp", HDLExpression.class,
			HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLExpression> getValue(HDLArrayInit obj) {
			if (obj == null)
				return null;
			return obj.getExp();
		}

		@Override
		public HDLArrayInit setValue(HDLArrayInit obj, ArrayList<HDLExpression> value) {
			if (obj == null)
				return null;
			return obj.setExp(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (exp.contains(obj))
			return fExp;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}

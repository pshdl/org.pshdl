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

import java.util.*;

import javax.annotation.*;

import org.pshdl.model.impl.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLFunctionCall contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> params. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLFunctionCall extends AbstractHDLFunctionCall implements HDLStatement {
	/**
	 * Constructs a new instance of {@link HDLFunctionCall}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLFunctionCall(int id, @Nullable IHDLObject container, @Nonnull HDLQualifiedName name, @Nullable Iterable<HDLExpression> params, boolean validate) {
		super(id, container, name, params, validate);
	}

	public HDLFunctionCall() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLFunctionCall;
	}

	/**
	 * The accessor for the field name which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLFunctionCall, HDLQualifiedName> fName = new HDLFieldAccess<HDLFunctionCall, HDLQualifiedName>("name") {
		@Override
		public HDLQualifiedName getValue(HDLFunctionCall obj) {
			if (obj == null)
				return null;
			return obj.getNameRefName();
		}
	};
	/**
	 * The accessor for the field params which is of type
	 * ArrayList<HDLExpression>.
	 */
	public static HDLFieldAccess<HDLFunctionCall, ArrayList<HDLExpression>> fParams = new HDLFieldAccess<HDLFunctionCall, ArrayList<HDLExpression>>("params") {
		@Override
		public ArrayList<HDLExpression> getValue(HDLFunctionCall obj) {
			if (obj == null)
				return null;
			return obj.getParams();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

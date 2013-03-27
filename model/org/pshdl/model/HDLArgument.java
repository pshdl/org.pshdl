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

import javax.annotation.*;

import org.pshdl.model.*;
import org.pshdl.model.impl.*;
import org.pshdl.model.utils.HDLQuery.*;

/**
 * The class HDLArgument contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression expression. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLArgument extends AbstractHDLArgument {
	/**
	 * Constructs a new instance of {@link HDLArgument}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param expression
	 *            the value for expression. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLArgument(@Nullable IHDLObject container, @Nonnull String name, @Nonnull HDLExpression expression, boolean validate) {
		super(container, name, expression, validate);
	}

	public HDLArgument() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLArgument;
	}

	/**
	 * The accessor for the field name which is of type String.
	 */
	public static HDLFieldAccess<HDLArgument, String> fName = new HDLFieldAccess<HDLArgument, String>("name") {
		@Override
		public String getValue(HDLArgument obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};
	/**
	 * The accessor for the field expression which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLArgument, HDLExpression> fExpression = new HDLFieldAccess<HDLArgument, HDLExpression>("expression") {
		@Override
		public HDLExpression getValue(HDLArgument obj) {
			if (obj == null)
				return null;
			return obj.getExpression();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

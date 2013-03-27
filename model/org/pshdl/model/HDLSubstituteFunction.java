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

import org.pshdl.model.*;
import org.pshdl.model.impl.*;
import org.pshdl.model.utils.HDLQuery.*;

/**
 * The class HDLSubstituteFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLVariable> args. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> stmnts. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLSubstituteFunction extends AbstractHDLSubstituteFunction {
	/**
	 * Constructs a new instance of {@link HDLSubstituteFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param args
	 *            the value for args. Can be <code>null</code>.
	 * @param stmnts
	 *            the value for stmnts. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLSubstituteFunction(@Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String name, @Nullable Iterable<HDLVariable> args,
			@Nullable Iterable<HDLStatement> stmnts, boolean validate) {
		super(container, annotations, name, args, stmnts, validate);
	}

	public HDLSubstituteFunction() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLSubstituteFunction;
	}

	/**
	 * The accessor for the field args which is of type ArrayList<HDLVariable>.
	 */
	public static HDLFieldAccess<HDLSubstituteFunction, ArrayList<HDLVariable>> fArgs = new HDLFieldAccess<HDLSubstituteFunction, ArrayList<HDLVariable>>("args") {
		@Override
		public ArrayList<HDLVariable> getValue(HDLSubstituteFunction obj) {
			if (obj == null)
				return null;
			return obj.getArgs();
		}
	};
	/**
	 * The accessor for the field stmnts which is of type
	 * ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLSubstituteFunction, ArrayList<HDLStatement>> fStmnts = new HDLFieldAccess<HDLSubstituteFunction, ArrayList<HDLStatement>>("stmnts") {
		@Override
		public ArrayList<HDLStatement> getValue(HDLSubstituteFunction obj) {
			if (obj == null)
				return null;
			return obj.getStmnts();
		}
	};

	// $CONTENT-BEGIN$
	public HDLStatement[] getReplacementStatements(HDLFunctionCall hdi) {
		ArrayList<HDLVariable> args = getArgs();
		ArrayList<HDLExpression> params = hdi.getParams();
		return createStatements(args, params, hdi);
	}

	public static final String META = "INLINED_FROM";

	private HDLStatement[] createStatements(Iterable<HDLVariable> args, Iterable<HDLExpression> params, IHDLObject origin) {
		HDLStatement[] res = new HDLStatement[getStmnts().size()];
		int pos = 0;
		for (HDLStatement stmnt : getStmnts()) {
			res[pos++] = substitute(args, params, stmnt, origin);
		}
		return res;
	}

	public HDLStatement[] getReplacementExpressionArgs(IHDLObject origin, HDLExpression... args) {
		return createStatements(getArgs(), asList(args), origin);
	}
	// $CONTENT-END$

}

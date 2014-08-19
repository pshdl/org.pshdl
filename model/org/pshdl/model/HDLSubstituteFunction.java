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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.impl.AbstractHDLSubstituteFunction;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLSubstituteFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLFunctionParameter> args. Can be <code>null</code>.</li>
 * <li>HDLFunctionParameter returnType. Can be <code>null</code>.</li>
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
	 * @param returnType
	 *            the value for returnType. Can be <code>null</code>.
	 * @param stmnts
	 *            the value for stmnts. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLSubstituteFunction(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String name,
			@Nullable Iterable<HDLFunctionParameter> args, @Nullable HDLFunctionParameter returnType, @Nullable Iterable<HDLStatement> stmnts, boolean validate) {
		super(id, container, annotations, name, args, returnType, stmnts, validate);
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
	 * The accessor for the field stmnts which is of type
	 * ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLSubstituteFunction, ArrayList<HDLStatement>> fStmnts = new HDLFieldAccess<HDLSubstituteFunction, ArrayList<HDLStatement>>("stmnts",
			HDLStatement.class, HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLStatement> getValue(HDLSubstituteFunction obj) {
			if (obj == null)
				return null;
			return obj.getStmnts();
		}

		@Override
		public HDLSubstituteFunction setValue(HDLSubstituteFunction obj, ArrayList<HDLStatement> value) {
			if (obj == null)
				return null;
			return obj.setStmnts(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (stmnts.contains(obj))
			return fStmnts;
		return super.getContainingFeature(obj);
	}

	// $CONTENT-BEGIN$
	public HDLStatement[] getReplacementStatements(HDLFunctionCall hdi) {
		final ArrayList<HDLFunctionParameter> args = getArgs();
		final ArrayList<HDLExpression> params = hdi.getParams();
		final HDLStatement[] res = new HDLStatement[getStmnts().size()];
		int pos = 0;
		for (final HDLStatement stmnt : getStmnts()) {
			res[pos++] = substitute(args, params, stmnt, hdi);
		}
		return res;
	}

	public static final String META = "INLINED_FROM";

	// $CONTENT-END$

}

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
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import org.pshdl.model.utils.*;

/**
 * The class HDLForLoop contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLRange> range. Can <b>not</b> be <code>null</code>,
 * additionally the collection must contain at least one element.</li>
 * <li>HDLVariable param. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> dos. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLForLoop extends AbstractHDLForLoop {
	/**
	 * Constructs a new instance of {@link HDLForLoop}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param range
	 *            the value for range. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param param
	 *            the value for param. Can <b>not</b> be <code>null</code>.
	 * @param dos
	 *            the value for dos. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLForLoop(int id, @Nullable IHDLObject container, @Nonnull Iterable<HDLRange> range, @Nonnull HDLVariable param, @Nullable Iterable<HDLStatement> dos, boolean validate) {
		super(id, container, range, param, dos, validate);
	}

	public HDLForLoop() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLForLoop;
	}

	/**
	 * The accessor for the field range which is of type ArrayList<HDLRange>.
	 */
	public static HDLFieldAccess<HDLForLoop, ArrayList<HDLRange>> fRange = new HDLFieldAccess<HDLForLoop, ArrayList<HDLRange>>("range") {
		@Override
		public ArrayList<HDLRange> getValue(HDLForLoop obj) {
			if (obj == null)
				return null;
			return obj.getRange();
		}

		@Override
		public HDLForLoop setValue(HDLForLoop obj, ArrayList<HDLRange> value) {
			if (obj == null)
				return null;
			return obj.setRange(value);
		}
	};
	/**
	 * The accessor for the field param which is of type HDLVariable.
	 */
	public static HDLFieldAccess<HDLForLoop, HDLVariable> fParam = new HDLFieldAccess<HDLForLoop, HDLVariable>("param") {
		@Override
		public HDLVariable getValue(HDLForLoop obj) {
			if (obj == null)
				return null;
			return obj.getParam();
		}

		@Override
		public HDLForLoop setValue(HDLForLoop obj, HDLVariable value) {
			if (obj == null)
				return null;
			return obj.setParam(value);
		}
	};
	/**
	 * The accessor for the field dos which is of type ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLForLoop, ArrayList<HDLStatement>> fDos = new HDLFieldAccess<HDLForLoop, ArrayList<HDLStatement>>("dos") {
		@Override
		public ArrayList<HDLStatement> getValue(HDLForLoop obj) {
			if (obj == null)
				return null;
			return obj.getDos();
		}

		@Override
		public HDLForLoop setValue(HDLForLoop obj, ArrayList<HDLStatement> value) {
			if (obj == null)
				return null;
			return obj.setDos(value);
		}
	};

	// $CONTENT-BEGIN$
	public static HDLForLoop tempLoop(HDLExpression lower, HDLExpression upper) {
		HDLForLoop loop = new HDLForLoop();
		loop = loop.setParam(new HDLVariable().setName(Insulin.getTempName("loop", null)));
		return loop.addRange(new HDLRange().setFrom(lower).setTo(upper));
	}

	// $CONTENT-END$

}

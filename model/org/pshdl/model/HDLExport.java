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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.impl.AbstractHDLExport;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLExport contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression exportRef. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLExport extends AbstractHDLExport {
	/**
	 * Constructs a new instance of {@link HDLExport}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param exportRef
	 *            the value for exportRef. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLExport(int id, @Nullable IHDLObject container, @Nonnull HDLExpression exportRef, boolean validate) {
		super(id, container, exportRef, validate);
	}

	public HDLExport() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLExport;
	}

	/**
	 * The accessor for the field exportRef which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLExport, HDLExpression> fExportRef = new HDLFieldAccess<HDLExport, HDLExpression>("exportRef", HDLExpression.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLExpression getValue(HDLExport obj) {
			if (obj == null)
				return null;
			return obj.getExportRef();
		}

		@Override
		public HDLExport setValue(HDLExport obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setExportRef(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (exportRef == obj)
			return fExportRef;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

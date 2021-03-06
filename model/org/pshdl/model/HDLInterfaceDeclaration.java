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

import org.pshdl.model.impl.AbstractHDLInterfaceDeclaration;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLInterfaceDeclaration contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLAnnotation&gt; annotations. Can be <code>null</code>.</li>
 * <li>HDLInterface hIf. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLInterfaceDeclaration extends AbstractHDLInterfaceDeclaration {
	/**
	 * Constructs a new instance of {@link HDLInterfaceDeclaration}
	 *
	 * @param id
	 *            a unique ID for this particular node
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLInterfaceDeclaration(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull HDLInterface hIf, boolean validate) {
		super(id, container, annotations, hIf, validate);
	}

	public HDLInterfaceDeclaration() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLInterfaceDeclaration;
	}

	/**
	 * The accessor for the field hIf which is of type HDLInterface.
	 */
	public static HDLFieldAccess<HDLInterfaceDeclaration, HDLInterface> fHIf = new HDLFieldAccess<HDLInterfaceDeclaration, HDLInterface>("hIf", HDLInterface.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLInterface getValue(HDLInterfaceDeclaration obj) {
			if (obj == null) {
				return null;
			}
			return obj.getHIf();
		}

		@Override
		public HDLInterfaceDeclaration setValue(HDLInterfaceDeclaration obj, HDLInterface value) {
			if (obj == null) {
				return null;
			}
			return obj.setHIf(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (hIf == obj) {
			return fHIf;
		}
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}

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

import org.pshdl.model.extensions.*;
import org.pshdl.model.impl.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

import com.google.common.base.*;

/**
 * The class HDLInterfaceRef contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName var. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> array. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLRange> bits. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName hIf. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> ifArray. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLInterfaceRef extends AbstractHDLInterfaceRef {
	/**
	 * Constructs a new instance of {@link HDLInterfaceRef}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param ifArray
	 *            the value for ifArray. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLInterfaceRef(int id, @Nullable IHDLObject container, @Nonnull HDLQualifiedName var, @Nullable Iterable<HDLExpression> array, @Nullable Iterable<HDLRange> bits,
			@Nonnull HDLQualifiedName hIf, @Nullable Iterable<HDLExpression> ifArray, boolean validate) {
		super(id, container, var, array, bits, hIf, ifArray, validate);
	}

	public HDLInterfaceRef() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLInterfaceRef;
	}

	/**
	 * The accessor for the field hIf which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLInterfaceRef, HDLQualifiedName> fHIf = new HDLFieldAccess<HDLInterfaceRef, HDLQualifiedName>("hIf") {
		@Override
		public HDLQualifiedName getValue(HDLInterfaceRef obj) {
			if (obj == null)
				return null;
			return obj.getHIfRefName();
		}

		@Override
		public HDLInterfaceRef setValue(HDLInterfaceRef obj, HDLQualifiedName value) {
			if (obj == null)
				return null;
			return obj.setHIf(value);
		}
	};
	/**
	 * The accessor for the field ifArray which is of type
	 * ArrayList<HDLExpression>.
	 */
	public static HDLFieldAccess<HDLInterfaceRef, ArrayList<HDLExpression>> fIfArray = new HDLFieldAccess<HDLInterfaceRef, ArrayList<HDLExpression>>("ifArray") {
		@Override
		public ArrayList<HDLExpression> getValue(HDLInterfaceRef obj) {
			if (obj == null)
				return null;
			return obj.getIfArray();
		}

		@Override
		public HDLInterfaceRef setValue(HDLInterfaceRef obj, ArrayList<HDLExpression> value) {
			if (obj == null)
				return null;
			return obj.setIfArray(value);
		}
	};

	// $CONTENT-BEGIN$

	@Override
	@Nullable
	public Optional<HDLVariable> resolveVar() {
		final Optional<HDLVariable> resolveHIf = resolveHIf();
		if (!resolveHIf.isPresent())
			return Optional.absent();
		final Optional<? extends HDLType> type = TypeExtension.typeOf(resolveHIf.get());
		if (type.isPresent() && (type.get() instanceof HDLInterface)) {
			final HDLInterface hIf = (HDLInterface) type.get();
			final String lastSegment2 = getVarRefName().getLastSegment();
			for (final HDLVariableDeclaration vd : hIf.getPorts()) {
				for (final HDLVariable hv : vd.getVariables()) {
					final String lastSegment = hv.getName();
					if (lastSegment.equals(lastSegment2))
						return Optional.of(hv);
				}
			}
		}
		return Optional.absent();
	}

	// $CONTENT-END$

}

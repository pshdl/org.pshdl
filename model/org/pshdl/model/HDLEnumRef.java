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

import org.pshdl.model.extensions.ScopingExtension;
import org.pshdl.model.impl.AbstractHDLEnumRef;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

import com.google.common.base.Optional;

/**
 * The class HDLEnumRef contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName var. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLQualifiedName hEnum. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLEnumRef extends AbstractHDLEnumRef {
	/**
	 * Constructs a new instance of {@link HDLEnumRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLEnumRef(int id, @Nullable IHDLObject container, @Nonnull HDLQualifiedName var, @Nonnull HDLQualifiedName hEnum, boolean validate) {
		super(id, container, var, hEnum, validate);
	}

	public HDLEnumRef() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLEnumRef;
	}

	/**
	 * The accessor for the field hEnum which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLEnumRef, HDLQualifiedName> fHEnum = new HDLFieldAccess<HDLEnumRef, HDLQualifiedName>("hEnum", HDLQualifiedName.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLQualifiedName getValue(HDLEnumRef obj) {
			if (obj == null)
				return null;
			return obj.getHEnumRefName();
		}

		@Override
		public HDLEnumRef setValue(HDLEnumRef obj, HDLQualifiedName value) {
			if (obj == null)
				return null;
			return obj.setHEnum(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (hEnum == obj)
			return fHEnum;
		return super.getContainingFeature(obj);
	}

	// $CONTENT-BEGIN$
	@Override
	public Optional<HDLVariable> resolveVar() {
		final Optional<HDLEnum> resolveHEnum = resolveHEnum();
		if (!resolveHEnum.isPresent())
			return Optional.absent();
		final Optional<HDLVariable> var = ScopingExtension.getVariable(resolveHEnum.get(), getVarRefName().getLastSegment());
		return var;
	}

	// $CONTENT-END$

}

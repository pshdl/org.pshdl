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

import org.pshdl.model.impl.AbstractHDLNativeFunction;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLNativeFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLAnnotation&gt; annotations. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLFunctionParameter&gt; args. Can be <code>null</code>.</li>
 * <li>HDLFunctionParameter returnType. Can be <code>null</code>.</li>
 * <li>Boolean simOnly. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLNativeFunction extends AbstractHDLNativeFunction {
	/**
	 * Constructs a new instance of {@link HDLNativeFunction}
	 *
	 * @param id
	 *            a unique ID for this particular node
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
	 * @param simOnly
	 *            the value for simOnly. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLNativeFunction(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String name,
			@Nullable Iterable<HDLFunctionParameter> args, @Nullable HDLFunctionParameter returnType, @Nonnull Boolean simOnly, boolean validate) {
		super(id, container, annotations, name, args, returnType, simOnly, validate);
	}

	public HDLNativeFunction() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLNativeFunction;
	}

	/**
	 * The accessor for the field simOnly which is of type Boolean.
	 */
	public static HDLFieldAccess<HDLNativeFunction, Boolean> fSimOnly = new HDLFieldAccess<HDLNativeFunction, Boolean>("simOnly", Boolean.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public Boolean getValue(HDLNativeFunction obj) {
			if (obj == null) {
				return null;
			}
			return obj.getSimOnly();
		}

		@Override
		public HDLNativeFunction setValue(HDLNativeFunction obj, Boolean value) {
			if (obj == null) {
				return null;
			}
			return obj.setSimOnly(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (simOnly == obj) {
			return fSimOnly;
		}
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

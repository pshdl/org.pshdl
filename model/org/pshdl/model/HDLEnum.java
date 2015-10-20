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

import org.pshdl.model.impl.AbstractHDLEnum;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLEnum contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLExpression&gt; dim. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLVariable&gt; enums. Can <b>not</b> be <code>null</code>,
 * additionally the collection must contain at least one element.</li>
 * </ul>
 */
public class HDLEnum extends AbstractHDLEnum {
	/**
	 * Constructs a new instance of {@link HDLEnum}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dim
	 *            the value for dim. Can be <code>null</code>.
	 * @param enums
	 *            the value for enums. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLEnum(int id, @Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dim, @Nonnull Iterable<HDLVariable> enums, boolean validate) {
		super(id, container, name, dim, enums, validate);
	}

	public HDLEnum() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLEnum;
	}

	/**
	 * The accessor for the field enums which is of type
	 * ArrayList&lt;HDLVariable&gt;.
	 */
	public static HDLFieldAccess<HDLEnum, ArrayList<HDLVariable>> fEnums = new HDLFieldAccess<HDLEnum, ArrayList<HDLVariable>>("enums", HDLVariable.class,
			HDLFieldAccess.Quantifier.ONE_OR_MORE) {
		@Override
		public ArrayList<HDLVariable> getValue(HDLEnum obj) {
			if (obj == null)
				return null;
			return obj.getEnums();
		}

		@Override
		public HDLEnum setValue(HDLEnum obj, ArrayList<HDLVariable> value) {
			if (obj == null)
				return null;
			return obj.setEnums(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (enums.contains(obj))
			return fEnums;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$

	public static class AnyEnum extends HDLEnum {
	}

	public static AnyEnum anyEnum() {
		return new AnyEnum();
	}

	// $CONTENT-END$

}

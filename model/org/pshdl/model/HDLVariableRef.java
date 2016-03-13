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

import org.pshdl.model.impl.AbstractHDLVariableRef;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

import com.google.common.base.Optional;

/**
 * The class HDLVariableRef contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName var. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLExpression&gt; array. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLRange&gt; bits. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLVariableRef extends AbstractHDLVariableRef {
	/**
	 * Constructs a new instance of {@link HDLVariableRef}
	 *
	 * @param id
	 *            a unique ID for this particular node
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLVariableRef(int id, @Nullable IHDLObject container, @Nonnull HDLQualifiedName var, @Nullable Iterable<HDLExpression> array, @Nullable Iterable<HDLRange> bits,
			boolean validate) {
		super(id, container, var, array, bits, validate);
	}

	public HDLVariableRef() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLVariableRef;
	}

	/**
	 * The accessor for the field array which is of type
	 * ArrayList&lt;HDLExpression&gt;.
	 */
	public static HDLFieldAccess<HDLVariableRef, ArrayList<HDLExpression>> fArray = new HDLFieldAccess<HDLVariableRef, ArrayList<HDLExpression>>("array", HDLExpression.class,
			HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLExpression> getValue(HDLVariableRef obj) {
			if (obj == null)
				return null;
			return obj.getArray();
		}

		@Override
		public HDLVariableRef setValue(HDLVariableRef obj, ArrayList<HDLExpression> value) {
			if (obj == null)
				return null;
			return obj.setArray(value);
		}
	};
	/**
	 * The accessor for the field bits which is of type
	 * ArrayList&lt;HDLRange&gt;.
	 */
	public static HDLFieldAccess<HDLVariableRef, ArrayList<HDLRange>> fBits = new HDLFieldAccess<HDLVariableRef, ArrayList<HDLRange>>("bits", HDLRange.class,
			HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLRange> getValue(HDLVariableRef obj) {
			if (obj == null)
				return null;
			return obj.getBits();
		}

		@Override
		public HDLVariableRef setValue(HDLVariableRef obj, ArrayList<HDLRange> value) {
			if (obj == null)
				return null;
			return obj.setBits(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (array.contains(obj))
			return fArray;
		if (bits.contains(obj))
			return fBits;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$

	private HDLVariable resolveVarCache;

	@Override
	public Optional<HDLVariable> resolveVar() {
		if (resolveVarCache != null)
			return Optional.of(resolveVarCache);
		final Optional<HDLVariable> resolveVar = super.resolveVar();
		if ((resolveVar.isPresent()) && frozen) {
			resolveVarCache = resolveVar.get();
		}
		return resolveVar;
	}
	// $CONTENT-END$

}

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
import java.util.EnumSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.impl.AbstractHDLVariableDeclaration;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

import com.google.common.base.Optional;

/**
 * The class HDLVariableDeclaration contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLAnnotation&gt; annotations. Can be <code>null</code>.
 * </li>
 * <li>HDLRegisterConfig register. Can be <code>null</code>.</li>
 * <li>HDLDirection direction. If <code>null</code>,
 * {@link HDLDirection#INTERNAL} is used as default.</li>
 * <li>HDLQualifiedName type. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLPrimitive primitive. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLVariable&gt; variables. Can <b>not</b> be
 * <code>null</code>, additionally the collection must contain at least one
 * element.</li>
 * </ul>
 */
public class HDLVariableDeclaration extends AbstractHDLVariableDeclaration {
	/**
	 * Constructs a new instance of {@link HDLVariableDeclaration}
	 *
	 * @param id
	 *            a unique ID for this particular node
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>,
	 *            {@link HDLDirection#INTERNAL} is used as default.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param primitive
	 *            the value for primitive. Can be <code>null</code>.
	 * @param variables
	 *            the value for variables. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLVariableDeclaration(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nullable HDLRegisterConfig register,
			@Nullable HDLDirection direction, @Nonnull HDLQualifiedName type, @Nullable HDLPrimitive primitive, @Nonnull Iterable<HDLVariable> variables, boolean validate) {
		super(id, container, annotations, register, direction, type, primitive, variables, validate);
	}

	public HDLVariableDeclaration() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLVariableDeclaration;
	}

	public static enum HDLDirection {
		IN("in"), OUT("out"), INOUT("inout"), PARAMETER("param"), CONSTANT("const"), INTERNAL(""), HIDDEN("<HIDDEN>");
		String str;

		HDLDirection(String op) {
			this.str = op;
		}

		@Nullable
		public static HDLDirection getOp(String op) {
			for (final HDLDirection ass : values()) {
				if (ass.str.equals(op))
					return ass;
			}
			return null;
		}

		@Override
		@Nonnull
		public String toString() {
			return str;
		}
	}

	/**
	 * The accessor for the field register which is of type HDLRegisterConfig.
	 */
	public static HDLFieldAccess<HDLVariableDeclaration, HDLRegisterConfig> fRegister = new HDLFieldAccess<HDLVariableDeclaration, HDLRegisterConfig>("register",
			HDLRegisterConfig.class, HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLRegisterConfig getValue(HDLVariableDeclaration obj) {
			if (obj == null)
				return null;
			return obj.getRegister();
		}

		@Override
		public HDLVariableDeclaration setValue(HDLVariableDeclaration obj, HDLRegisterConfig value) {
			if (obj == null)
				return null;
			return obj.setRegister(value);
		}
	};
	/**
	 * The accessor for the field direction which is of type HDLDirection.
	 */
	public static HDLFieldAccess<HDLVariableDeclaration, HDLDirection> fDirection = new HDLFieldAccess<HDLVariableDeclaration, HDLDirection>("direction", HDLDirection.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLDirection getValue(HDLVariableDeclaration obj) {
			if (obj == null)
				return null;
			return obj.getDirection();
		}

		@Override
		public HDLVariableDeclaration setValue(HDLVariableDeclaration obj, HDLDirection value) {
			if (obj == null)
				return null;
			return obj.setDirection(value);
		}
	};
	/**
	 * The accessor for the field type which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLVariableDeclaration, HDLQualifiedName> fType = new HDLFieldAccess<HDLVariableDeclaration, HDLQualifiedName>("type", HDLQualifiedName.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLQualifiedName getValue(HDLVariableDeclaration obj) {
			if (obj == null)
				return null;
			return obj.getTypeRefName();
		}

		@Override
		public HDLVariableDeclaration setValue(HDLVariableDeclaration obj, HDLQualifiedName value) {
			if (obj == null)
				return null;
			return obj.setType(value);
		}
	};
	/**
	 * The accessor for the field primitive which is of type HDLPrimitive.
	 */
	public static HDLFieldAccess<HDLVariableDeclaration, HDLPrimitive> fPrimitive = new HDLFieldAccess<HDLVariableDeclaration, HDLPrimitive>("primitive", HDLPrimitive.class,
			HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLPrimitive getValue(HDLVariableDeclaration obj) {
			if (obj == null)
				return null;
			return obj.getPrimitive();
		}

		@Override
		public HDLVariableDeclaration setValue(HDLVariableDeclaration obj, HDLPrimitive value) {
			if (obj == null)
				return null;
			return obj.setPrimitive(value);
		}
	};
	/**
	 * The accessor for the field variables which is of type
	 * ArrayList&lt;HDLVariable&gt;.
	 */
	public static HDLFieldAccess<HDLVariableDeclaration, ArrayList<HDLVariable>> fVariables = new HDLFieldAccess<HDLVariableDeclaration, ArrayList<HDLVariable>>("variables",
			HDLVariable.class, HDLFieldAccess.Quantifier.ONE_OR_MORE) {
		@Override
		public ArrayList<HDLVariable> getValue(HDLVariableDeclaration obj) {
			if (obj == null)
				return null;
			return obj.getVariables();
		}

		@Override
		public HDLVariableDeclaration setValue(HDLVariableDeclaration obj, ArrayList<HDLVariable> value) {
			if (obj == null)
				return null;
			return obj.setVariables(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (register == obj)
			return fRegister;
		if (direction == obj)
			return fDirection;
		if (type == obj)
			return fType;
		if (primitive == obj)
			return fPrimitive;
		if (variables.contains(obj))
			return fVariables;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$

	@Override
	public Optional<? extends HDLType> resolveType() {
		final HDLPrimitive prim = getPrimitive();
		if (prim != null)
			return Optional.<HDLType>of(prim);
		return super.resolveType();
	}

	public HDLVariableDeclaration setType(HDLType resolveType) {
		final HDLVariableDeclaration setType = super.setType(resolveType.asRef());
		if (resolveType instanceof HDLPrimitive) {
			final HDLPrimitive prim = (HDLPrimitive) resolveType;
			return setType.setPrimitive(prim);
		}
		return setType;
	}

	private static final EnumSet<HDLDirection> external = EnumSet.of(HDLDirection.IN, HDLDirection.OUT, HDLDirection.INOUT, HDLDirection.PARAMETER);

	public boolean isExternal() {
		return external.contains(getDirection());
	}

	// $CONTENT-END$

}

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
package de.tuhh.ict.pshdl.model;

import java.util.*;

import javax.annotation.*;

import com.google.common.base.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLVariableDeclaration contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * <li>HDLRegisterConfig register. Can be <code>null</code>.</li>
 * <li>HDLDirection direction. If <code>null</code>,
 * {@link HDLDirection#INTERNAL} is used as default.</li>
 * <li>HDLQualifiedName type. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLPrimitive primitive. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLVariable> variables. Can <b>not</b> be <code>null</code>,
 * additionally the collection must contain at least one element.</li>
 * </ul>
 */
public class HDLVariableDeclaration extends AbstractHDLVariableDeclaration {
	/**
	 * Constructs a new instance of {@link HDLVariableDeclaration}
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
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLVariableDeclaration(@Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nullable HDLRegisterConfig register,
			@Nullable HDLDirection direction, @Nonnull HDLQualifiedName type, @Nullable HDLPrimitive primitive, @Nonnull Iterable<HDLVariable> variables, boolean validate) {
		super(container, annotations, register, direction, type, primitive, variables, validate);
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
			for (HDLDirection ass : values()) {
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

		public boolean isIO() {
			switch (this) {
			case CONSTANT:
			case HIDDEN:
			case INTERNAL:
				return false;
			case IN:
			case INOUT:
			case OUT:
			case PARAMETER:
				return true;
			}
			return false;
		}
	}

	/**
	 * The accessor for the field register which is of type HDLRegisterConfig.
	 */
	public static HDLFieldAccess<HDLVariableDeclaration, HDLRegisterConfig> fRegister = new HDLFieldAccess<HDLVariableDeclaration, HDLRegisterConfig>("register") {
		@Override
		public HDLRegisterConfig getValue(HDLVariableDeclaration obj) {
			if (obj == null)
				return null;
			return obj.getRegister();
		}
	};
	/**
	 * The accessor for the field direction which is of type HDLDirection.
	 */
	public static HDLFieldAccess<HDLVariableDeclaration, HDLDirection> fDirection = new HDLFieldAccess<HDLVariableDeclaration, HDLDirection>("direction") {
		@Override
		public HDLDirection getValue(HDLVariableDeclaration obj) {
			if (obj == null)
				return null;
			return obj.getDirection();
		}
	};
	/**
	 * The accessor for the field type which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLVariableDeclaration, HDLQualifiedName> fType = new HDLFieldAccess<HDLVariableDeclaration, HDLQualifiedName>("type") {
		@Override
		public HDLQualifiedName getValue(HDLVariableDeclaration obj) {
			if (obj == null)
				return null;
			return obj.getTypeRefName();
		}
	};
	/**
	 * The accessor for the field primitive which is of type HDLPrimitive.
	 */
	public static HDLFieldAccess<HDLVariableDeclaration, HDLPrimitive> fPrimitive = new HDLFieldAccess<HDLVariableDeclaration, HDLPrimitive>("primitive") {
		@Override
		public HDLPrimitive getValue(HDLVariableDeclaration obj) {
			if (obj == null)
				return null;
			return obj.getPrimitive();
		}
	};
	/**
	 * The accessor for the field variables which is of type
	 * ArrayList<HDLVariable>.
	 */
	public static HDLFieldAccess<HDLVariableDeclaration, ArrayList<HDLVariable>> fVariables = new HDLFieldAccess<HDLVariableDeclaration, ArrayList<HDLVariable>>("variables") {
		@Override
		public ArrayList<HDLVariable> getValue(HDLVariableDeclaration obj) {
			if (obj == null)
				return null;
			return obj.getVariables();
		}
	};

	// $CONTENT-BEGIN$

	@Override
	public Optional<? extends HDLType> resolveType() {
		if (getPrimitive() != null)
			return Optional.<HDLType> fromNullable(getPrimitive());
		return super.resolveType();
	}

	public HDLVariableDeclaration setType(HDLType resolveType) {
		HDLVariableDeclaration setType = super.setType(resolveType.asRef());
		if (resolveType instanceof HDLPrimitive) {
			HDLPrimitive prim = (HDLPrimitive) resolveType;
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

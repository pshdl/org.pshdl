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
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLFunctionParameter contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>RWType rw. If <code>null</code>, {@link RWType#READ} is used as default.</li>
 * <li>Type type. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLQualifiedName enumSpec. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName ifSpec. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLFunctionParameter> funcSpec. Can be <code>null</code>.</li>
 * <li>HDLFunctionParameter funcReturnSpec. Can be <code>null</code>.</li>
 * <li>HDLVariable name. Can be <code>null</code>.</li>
 * <li>HDLExpression width. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> dim. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLFunctionParameter extends AbstractHDLFunctionParameter {
	/**
	 * Constructs a new instance of {@link HDLFunctionParameter}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param rw
	 *            the value for rw. If <code>null</code>, {@link RWType#READ} is
	 *            used as default.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param enumSpec
	 *            the value for enumSpec. Can be <code>null</code>.
	 * @param ifSpec
	 *            the value for ifSpec. Can be <code>null</code>.
	 * @param funcSpec
	 *            the value for funcSpec. Can be <code>null</code>.
	 * @param funcReturnSpec
	 *            the value for funcReturnSpec. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can be <code>null</code>.
	 * @param width
	 *            the value for width. Can be <code>null</code>.
	 * @param dim
	 *            the value for dim. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLFunctionParameter(@Nullable IHDLObject container, @Nullable RWType rw, @Nonnull Type type, @Nullable HDLQualifiedName enumSpec, @Nullable HDLQualifiedName ifSpec,
			@Nullable Iterable<HDLFunctionParameter> funcSpec, @Nullable HDLFunctionParameter funcReturnSpec, @Nullable HDLVariable name, @Nullable HDLExpression width,
			@Nullable Iterable<HDLExpression> dim, boolean validate) {
		super(container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, validate);
	}

	public HDLFunctionParameter() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLFunctionParameter;
	}

	public static enum RWType {
		RETURN(""), READ("-"), WRITE("+"), READWRITE("*");
		String str;

		RWType(String op) {
			this.str = op;
		}

		@Nullable
		public static RWType getOp(String op) {
			for (RWType ass : values()) {
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

	public static enum Type {
		ANY_INT("int<>"), ANY_UINT("uint<>"), ANY_BIT("bit<>"), ANY_IF("interface<>"), ANY_ENUM("enum<>"), IF("interface"), ENUM("enum"), FUNCTION("function"), REG_BIT("bit"), REG_UINT(
				"uint"), REG_INT("int"), STRING_TYPE("string"), BOOL_TYPE("bool");
		String str;

		Type(String op) {
			this.str = op;
		}

		@Nullable
		public static Type getOp(String op) {
			for (Type ass : values()) {
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
	 * The accessor for the field rw which is of type RWType.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, RWType> fRw = new HDLFieldAccess<HDLFunctionParameter, RWType>("rw") {
		@Override
		public RWType getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getRw();
		}
	};
	/**
	 * The accessor for the field type which is of type Type.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, Type> fType = new HDLFieldAccess<HDLFunctionParameter, Type>("type") {
		@Override
		public Type getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}
	};
	/**
	 * The accessor for the field enumSpec which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, HDLQualifiedName> fEnumSpec = new HDLFieldAccess<HDLFunctionParameter, HDLQualifiedName>("enumSpec") {
		@Override
		public HDLQualifiedName getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getEnumSpecRefName();
		}
	};
	/**
	 * The accessor for the field ifSpec which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, HDLQualifiedName> fIfSpec = new HDLFieldAccess<HDLFunctionParameter, HDLQualifiedName>("ifSpec") {
		@Override
		public HDLQualifiedName getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getIfSpecRefName();
		}
	};
	/**
	 * The accessor for the field funcSpec which is of type
	 * ArrayList<HDLFunctionParameter>.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, ArrayList<HDLFunctionParameter>> fFuncSpec = new HDLFieldAccess<HDLFunctionParameter, ArrayList<HDLFunctionParameter>>(
			"funcSpec") {
		@Override
		public ArrayList<HDLFunctionParameter> getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getFuncSpec();
		}
	};
	/**
	 * The accessor for the field funcReturnSpec which is of type
	 * HDLFunctionParameter.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, HDLFunctionParameter> fFuncReturnSpec = new HDLFieldAccess<HDLFunctionParameter, HDLFunctionParameter>("funcReturnSpec") {
		@Override
		public HDLFunctionParameter getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getFuncReturnSpec();
		}
	};
	/**
	 * The accessor for the field name which is of type HDLVariable.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, HDLVariable> fName = new HDLFieldAccess<HDLFunctionParameter, HDLVariable>("name") {
		@Override
		public HDLVariable getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};
	/**
	 * The accessor for the field width which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, HDLExpression> fWidth = new HDLFieldAccess<HDLFunctionParameter, HDLExpression>("width") {
		@Override
		public HDLExpression getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getWidth();
		}
	};
	/**
	 * The accessor for the field dim which is of type ArrayList<HDLExpression>.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, ArrayList<HDLExpression>> fDim = new HDLFieldAccess<HDLFunctionParameter, ArrayList<HDLExpression>>("dim") {
		@Override
		public ArrayList<HDLExpression> getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getDim();
		}
	};

	// $CONTENT-BEGIN$
	public static final HDLExpression EMPTY_ARR() {
		return new HDLLiteral().setStr(true).setVal("");
	}
	// $CONTENT-END$

}

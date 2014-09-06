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

import org.pshdl.model.impl.AbstractHDLFunctionParameter;
import org.pshdl.model.utils.HDLQualifiedName;
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
 * <li>Boolean constant. Can <b>not</b> be <code>null</code>.</li>
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
	 * @param constant
	 *            the value for constant. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLFunctionParameter(int id, @Nullable IHDLObject container, @Nullable RWType rw, @Nonnull Type type, @Nullable HDLQualifiedName enumSpec,
			@Nullable HDLQualifiedName ifSpec, @Nullable Iterable<HDLFunctionParameter> funcSpec, @Nullable HDLFunctionParameter funcReturnSpec, @Nullable HDLVariable name,
			@Nullable HDLExpression width, @Nullable Iterable<HDLExpression> dim, @Nonnull Boolean constant, boolean validate) {
		super(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, constant, validate);
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
			for (final RWType ass : values()) {
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
			for (final Type ass : values()) {
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
	public static HDLFieldAccess<HDLFunctionParameter, RWType> fRw = new HDLFieldAccess<HDLFunctionParameter, RWType>("rw", RWType.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public RWType getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getRw();
		}

		@Override
		public HDLFunctionParameter setValue(HDLFunctionParameter obj, RWType value) {
			if (obj == null)
				return null;
			return obj.setRw(value);
		}
	};
	/**
	 * The accessor for the field type which is of type Type.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, Type> fType = new HDLFieldAccess<HDLFunctionParameter, Type>("type", Type.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public Type getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}

		@Override
		public HDLFunctionParameter setValue(HDLFunctionParameter obj, Type value) {
			if (obj == null)
				return null;
			return obj.setType(value);
		}
	};
	/**
	 * The accessor for the field enumSpec which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, HDLQualifiedName> fEnumSpec = new HDLFieldAccess<HDLFunctionParameter, HDLQualifiedName>("enumSpec", HDLQualifiedName.class,
			HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLQualifiedName getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getEnumSpecRefName();
		}

		@Override
		public HDLFunctionParameter setValue(HDLFunctionParameter obj, HDLQualifiedName value) {
			if (obj == null)
				return null;
			return obj.setEnumSpec(value);
		}
	};
	/**
	 * The accessor for the field ifSpec which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, HDLQualifiedName> fIfSpec = new HDLFieldAccess<HDLFunctionParameter, HDLQualifiedName>("ifSpec", HDLQualifiedName.class,
			HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLQualifiedName getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getIfSpecRefName();
		}

		@Override
		public HDLFunctionParameter setValue(HDLFunctionParameter obj, HDLQualifiedName value) {
			if (obj == null)
				return null;
			return obj.setIfSpec(value);
		}
	};
	/**
	 * The accessor for the field funcSpec which is of type
	 * ArrayList<HDLFunctionParameter>.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, ArrayList<HDLFunctionParameter>> fFuncSpec = new HDLFieldAccess<HDLFunctionParameter, ArrayList<HDLFunctionParameter>>(
			"funcSpec", HDLFunctionParameter.class, HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLFunctionParameter> getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getFuncSpec();
		}

		@Override
		public HDLFunctionParameter setValue(HDLFunctionParameter obj, ArrayList<HDLFunctionParameter> value) {
			if (obj == null)
				return null;
			return obj.setFuncSpec(value);
		}
	};
	/**
	 * The accessor for the field funcReturnSpec which is of type
	 * HDLFunctionParameter.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, HDLFunctionParameter> fFuncReturnSpec = new HDLFieldAccess<HDLFunctionParameter, HDLFunctionParameter>("funcReturnSpec",
			HDLFunctionParameter.class, HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLFunctionParameter getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getFuncReturnSpec();
		}

		@Override
		public HDLFunctionParameter setValue(HDLFunctionParameter obj, HDLFunctionParameter value) {
			if (obj == null)
				return null;
			return obj.setFuncReturnSpec(value);
		}
	};
	/**
	 * The accessor for the field name which is of type HDLVariable.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, HDLVariable> fName = new HDLFieldAccess<HDLFunctionParameter, HDLVariable>("name", HDLVariable.class,
			HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLVariable getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}

		@Override
		public HDLFunctionParameter setValue(HDLFunctionParameter obj, HDLVariable value) {
			if (obj == null)
				return null;
			return obj.setName(value);
		}
	};
	/**
	 * The accessor for the field width which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, HDLExpression> fWidth = new HDLFieldAccess<HDLFunctionParameter, HDLExpression>("width", HDLExpression.class,
			HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLExpression getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getWidth();
		}

		@Override
		public HDLFunctionParameter setValue(HDLFunctionParameter obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setWidth(value);
		}
	};
	/**
	 * The accessor for the field dim which is of type ArrayList<HDLExpression>.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, ArrayList<HDLExpression>> fDim = new HDLFieldAccess<HDLFunctionParameter, ArrayList<HDLExpression>>("dim",
			HDLExpression.class, HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLExpression> getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getDim();
		}

		@Override
		public HDLFunctionParameter setValue(HDLFunctionParameter obj, ArrayList<HDLExpression> value) {
			if (obj == null)
				return null;
			return obj.setDim(value);
		}
	};
	/**
	 * The accessor for the field constant which is of type Boolean.
	 */
	public static HDLFieldAccess<HDLFunctionParameter, Boolean> fConstant = new HDLFieldAccess<HDLFunctionParameter, Boolean>("constant", Boolean.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public Boolean getValue(HDLFunctionParameter obj) {
			if (obj == null)
				return null;
			return obj.getConstant();
		}

		@Override
		public HDLFunctionParameter setValue(HDLFunctionParameter obj, Boolean value) {
			if (obj == null)
				return null;
			return obj.setConstant(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (rw == obj)
			return fRw;
		if (type == obj)
			return fType;
		if (enumSpec == obj)
			return fEnumSpec;
		if (ifSpec == obj)
			return fIfSpec;
		if (funcSpec.contains(obj))
			return fFuncSpec;
		if (funcReturnSpec == obj)
			return fFuncReturnSpec;
		if (name == obj)
			return fName;
		if (width == obj)
			return fWidth;
		if (dim.contains(obj))
			return fDim;
		if (constant == obj)
			return fConstant;
		return super.getContainingFeature(obj);
	}

	// $CONTENT-BEGIN$
	public static HDLFunctionParameter returnType(Type type) {
		return new HDLFunctionParameter().setType(type).setRw(RWType.RETURN);
	}

	public static HDLFunctionParameter param(Type type, String paramName) {
		return param(type, paramName, RWType.READ);
	}

	public static HDLFunctionParameter param(Type type, String paramName, RWType rwType) {
		return new HDLFunctionParameter().setType(type).setName(new HDLVariable().setName(paramName)).setRw(rwType);
	}

	public static final HDLExpression EMPTY_ARR() {
		return new HDLLiteral().setStr(true).setVal("");
	}
	// $CONTENT-END$

}

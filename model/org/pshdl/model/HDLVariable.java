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

import static org.pshdl.model.extensions.FullNameExtension.*;

import java.util.*;

import javax.annotation.*;

import org.pshdl.model.*;
import org.pshdl.model.impl.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.HDLQuery.*;

/**
 * The class HDLVariable contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> dimensions. Can be <code>null</code>.</li>
 * <li>HDLExpression defaultValue. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLVariable extends AbstractHDLVariable {
	/**
	 * Constructs a new instance of {@link HDLVariable}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dimensions
	 *            the value for dimensions. Can be <code>null</code>.
	 * @param defaultValue
	 *            the value for defaultValue. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLVariable(@Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dimensions, @Nullable HDLExpression defaultValue,
			@Nullable Iterable<HDLAnnotation> annotations, boolean validate) {
		super(container, name, dimensions, defaultValue, annotations, validate);
	}

	public HDLVariable() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLVariable;
	}

	/**
	 * The accessor for the field name which is of type String.
	 */
	public static HDLFieldAccess<HDLVariable, String> fName = new HDLFieldAccess<HDLVariable, String>("name") {
		@Override
		public String getValue(HDLVariable obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};
	/**
	 * The accessor for the field dimensions which is of type
	 * ArrayList<HDLExpression>.
	 */
	public static HDLFieldAccess<HDLVariable, ArrayList<HDLExpression>> fDimensions = new HDLFieldAccess<HDLVariable, ArrayList<HDLExpression>>("dimensions") {
		@Override
		public ArrayList<HDLExpression> getValue(HDLVariable obj) {
			if (obj == null)
				return null;
			return obj.getDimensions();
		}
	};
	/**
	 * The accessor for the field defaultValue which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLVariable, HDLExpression> fDefaultValue = new HDLFieldAccess<HDLVariable, HDLExpression>("defaultValue") {
		@Override
		public HDLExpression getValue(HDLVariable obj) {
			if (obj == null)
				return null;
			return obj.getDefaultValue();
		}
	};
	/**
	 * The accessor for the field annotations which is of type
	 * ArrayList<HDLAnnotation>.
	 */
	public static HDLFieldAccess<HDLVariable, ArrayList<HDLAnnotation>> fAnnotations = new HDLFieldAccess<HDLVariable, ArrayList<HDLAnnotation>>("annotations") {
		@Override
		public ArrayList<HDLAnnotation> getValue(HDLVariable obj) {
			if (obj == null)
				return null;
			return obj.getAnnotations();
		}
	};

	// $CONTENT-BEGIN$

	public HDLRegisterConfig getRegisterConfig() {
		if (container instanceof HDLVariableDeclaration) {
			HDLVariableDeclaration vhd = (HDLVariableDeclaration) container;
			return vhd.getRegister();
		}
		return null;
	}

	public HDLVariableDeclaration.HDLDirection getDirection() {
		if (container instanceof HDLVariableDeclaration) {
			HDLVariableDeclaration vhd = (HDLVariableDeclaration) container;
			return vhd.getDirection();
		}
		return null;
	}

	@Nonnull
	public HDLQualifiedName asRef() {
		return fullNameOf(this);
	}

	@Nonnull
	public HDLVariableRef asHDLRef() {
		return new HDLVariableRef().setVar(asRef());
	}

	@Override
	protected String validateName(String name) {
		if ((name != null) && name.contains("."))
			throw new IllegalArgumentException("Variable names may not contain a dot");
		return super.validateName(name);
	}

	public HDLAnnotation getAnnotation(Enum<?> range) {
		for (HDLAnnotation anno : getAnnotations())
			if (anno.getName().equals(range.toString()))
				return anno;
		if (container instanceof HDLVariableDeclaration) {
			HDLVariableDeclaration vhd = (HDLVariableDeclaration) container;
			return vhd.getAnnotation(range);
		}
		return null;
	}

	// $CONTENT-END$

}

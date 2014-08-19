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

import org.pshdl.model.impl.AbstractHDLInstantiation;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLInstantiation contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * <li>HDLVariable var. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLArgument> arguments. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLInstantiation extends AbstractHDLInstantiation {
	/**
	 * Constructs a new instance of {@link HDLInstantiation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLInstantiation(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull HDLVariable var,
			@Nullable Iterable<HDLArgument> arguments, boolean validate) {
		super(id, container, annotations, var, arguments, validate);
	}

	public HDLInstantiation() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLInstantiation;
	}

	/**
	 * The accessor for the field annotations which is of type
	 * ArrayList<HDLAnnotation>.
	 */
	public static HDLFieldAccess<HDLInstantiation, ArrayList<HDLAnnotation>> fAnnotations = new HDLFieldAccess<HDLInstantiation, ArrayList<HDLAnnotation>>("annotations",
			HDLAnnotation.class, HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLAnnotation> getValue(HDLInstantiation obj) {
			if (obj == null)
				return null;
			return obj.getAnnotations();
		}

		@Override
		public HDLInstantiation setValue(HDLInstantiation obj, ArrayList<HDLAnnotation> value) {
			if (obj == null)
				return null;
			return obj.setAnnotations(value);
		}
	};
	/**
	 * The accessor for the field var which is of type HDLVariable.
	 */
	public static HDLFieldAccess<HDLInstantiation, HDLVariable> fVar = new HDLFieldAccess<HDLInstantiation, HDLVariable>("var", HDLVariable.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLVariable getValue(HDLInstantiation obj) {
			if (obj == null)
				return null;
			return obj.getVar();
		}

		@Override
		public HDLInstantiation setValue(HDLInstantiation obj, HDLVariable value) {
			if (obj == null)
				return null;
			return obj.setVar(value);
		}
	};
	/**
	 * The accessor for the field arguments which is of type
	 * ArrayList<HDLArgument>.
	 */
	public static HDLFieldAccess<HDLInstantiation, ArrayList<HDLArgument>> fArguments = new HDLFieldAccess<HDLInstantiation, ArrayList<HDLArgument>>("arguments",
			HDLArgument.class, HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLArgument> getValue(HDLInstantiation obj) {
			if (obj == null)
				return null;
			return obj.getArguments();
		}

		@Override
		public HDLInstantiation setValue(HDLInstantiation obj, ArrayList<HDLArgument> value) {
			if (obj == null)
				return null;
			return obj.setArguments(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (annotations.contains(obj))
			return fAnnotations;
		if (var == obj)
			return fVar;
		if (arguments.contains(obj))
			return fArguments;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

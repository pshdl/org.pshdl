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

import org.pshdl.model.impl.AbstractHDLDirectGeneration;
import org.pshdl.model.types.builtIn.HDLGenerators;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLDirectGeneration contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLAnnotation&gt; annotations. Can be <code>null</code>.</li>
 * <li>HDLVariable var. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLArgument&gt; arguments. Can be <code>null</code>.</li>
 * <li>HDLInterface hIf. Can be <code>null</code>.</li>
 * <li>String generatorID. Can <b>not</b> be <code>null</code>.</li>
 * <li>String generatorContent. Can <b>not</b> be <code>null</code>.</li>
 * <li>Boolean include. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLDirectGeneration extends AbstractHDLDirectGeneration {
	/**
	 * Constructs a new instance of {@link HDLDirectGeneration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can be <code>null</code>.
	 * @param generatorID
	 *            the value for generatorID. Can <b>not</b> be <code>null</code>
	 *            .
	 * @param generatorContent
	 *            the value for generatorContent. Can <b>not</b> be
	 *            <code>null</code>.
	 * @param include
	 *            the value for include. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLDirectGeneration(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull HDLVariable var,
			@Nullable Iterable<HDLArgument> arguments, @Nullable HDLInterface hIf, @Nonnull String generatorID, @Nonnull String generatorContent, @Nonnull Boolean include,
			boolean validate) {
		super(id, container, annotations, var, arguments, hIf, generatorID, generatorContent, include, validate);
	}

	public HDLDirectGeneration() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLDirectGeneration;
	}

	/**
	 * The accessor for the field hIf which is of type HDLInterface.
	 */
	public static HDLFieldAccess<HDLDirectGeneration, HDLInterface> fHIf = new HDLFieldAccess<HDLDirectGeneration, HDLInterface>("hIf", HDLInterface.class,
			HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLInterface getValue(HDLDirectGeneration obj) {
			if (obj == null)
				return null;
			return obj.getHIf();
		}

		@Override
		public HDLDirectGeneration setValue(HDLDirectGeneration obj, HDLInterface value) {
			if (obj == null)
				return null;
			return obj.setHIf(value);
		}
	};
	/**
	 * The accessor for the field generatorID which is of type String.
	 */
	public static HDLFieldAccess<HDLDirectGeneration, String> fGeneratorID = new HDLFieldAccess<HDLDirectGeneration, String>("generatorID", String.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public String getValue(HDLDirectGeneration obj) {
			if (obj == null)
				return null;
			return obj.getGeneratorID();
		}

		@Override
		public HDLDirectGeneration setValue(HDLDirectGeneration obj, String value) {
			if (obj == null)
				return null;
			return obj.setGeneratorID(value);
		}
	};
	/**
	 * The accessor for the field generatorContent which is of type String.
	 */
	public static HDLFieldAccess<HDLDirectGeneration, String> fGeneratorContent = new HDLFieldAccess<HDLDirectGeneration, String>("generatorContent", String.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public String getValue(HDLDirectGeneration obj) {
			if (obj == null)
				return null;
			return obj.getGeneratorContent();
		}

		@Override
		public HDLDirectGeneration setValue(HDLDirectGeneration obj, String value) {
			if (obj == null)
				return null;
			return obj.setGeneratorContent(value);
		}
	};
	/**
	 * The accessor for the field include which is of type Boolean.
	 */
	public static HDLFieldAccess<HDLDirectGeneration, Boolean> fInclude = new HDLFieldAccess<HDLDirectGeneration, Boolean>("include", Boolean.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public Boolean getValue(HDLDirectGeneration obj) {
			if (obj == null)
				return null;
			return obj.getInclude();
		}

		@Override
		public HDLDirectGeneration setValue(HDLDirectGeneration obj, Boolean value) {
			if (obj == null)
				return null;
			return obj.setInclude(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (hIf == obj)
			return fHIf;
		if (generatorID == obj)
			return fGeneratorID;
		if (generatorContent == obj)
			return fGeneratorContent;
		if (include == obj)
			return fInclude;
		return super.getContainingFeature(obj);
	}

	// $CONTENT-BEGIN$

	private HDLInterface hif = null;

	@Override
	public HDLInterface getHIf() {
		if (hif != null)
			return hif;
		hif = HDLGenerators.getInterface(this).orNull();
		return hif;
	}

	public String getIfName() {
		return super.getHIf().getName();
	}

	// $CONTENT-END$

}

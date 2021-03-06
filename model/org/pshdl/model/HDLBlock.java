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

import org.pshdl.model.impl.AbstractHDLBlock;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLBlock contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>Boolean process. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLStatement&gt; statements. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLBlock extends AbstractHDLBlock {
	/**
	 * Constructs a new instance of {@link HDLBlock}
	 *
	 * @param id
	 *            a unique ID for this particular node
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param process
	 *            the value for process. Can <b>not</b> be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLBlock(int id, @Nullable IHDLObject container, @Nonnull Boolean process, @Nullable Iterable<HDLStatement> statements, boolean validate) {
		super(id, container, process, statements, validate);
	}

	public HDLBlock() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLBlock;
	}

	/**
	 * The accessor for the field process which is of type Boolean.
	 */
	public static HDLFieldAccess<HDLBlock, Boolean> fProcess = new HDLFieldAccess<HDLBlock, Boolean>("process", Boolean.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public Boolean getValue(HDLBlock obj) {
			if (obj == null) {
				return null;
			}
			return obj.getProcess();
		}

		@Override
		public HDLBlock setValue(HDLBlock obj, Boolean value) {
			if (obj == null) {
				return null;
			}
			return obj.setProcess(value);
		}
	};
	/**
	 * The accessor for the field statements which is of type ArrayList&lt;HDLStatement&gt;.
	 */
	public static HDLFieldAccess<HDLBlock, ArrayList<HDLStatement>> fStatements = new HDLFieldAccess<HDLBlock, ArrayList<HDLStatement>>("statements", HDLStatement.class,
			HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLStatement> getValue(HDLBlock obj) {
			if (obj == null) {
				return null;
			}
			return obj.getStatements();
		}

		@Override
		public HDLBlock setValue(HDLBlock obj, ArrayList<HDLStatement> value) {
			if (obj == null) {
				return null;
			}
			return obj.setStatements(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (process == obj) {
			return fProcess;
		}
		if (statements.contains(obj)) {
			return fStatements;
		}
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

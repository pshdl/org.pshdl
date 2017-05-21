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
package org.pshdl.model.utils.services;

import java.util.List;

import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLVariableDeclaration;

/**
 * This interface allows the addition of ports to an existing {@link HDLUnit}. This can be useful for annotation post-processing in addition
 * to an {@link IInsulinParticitant}
 *
 * @author Karsten Becker
 */
public interface IPortAdder {
	/**
	 * The returned ports will be added to the given unit. The usage of those ports can be contributed via {@link IInsulinParticitant}.
	 *
	 * @param unit
	 *            the unit to which ports could potentially be contributed
	 * @return <code>null</code> if no ports should be added to this unit. The ports otherwise
	 */
	public List<HDLVariableDeclaration> getPortAddition(HDLUnit unit);
}

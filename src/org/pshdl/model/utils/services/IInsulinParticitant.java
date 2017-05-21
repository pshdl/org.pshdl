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

import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.Insulin;

public interface IInsulinParticitant {

	/**
	 * A human readable name of the Participant
	 *
	 * @return a non-null String
	 */
	public String getName();

	/**
	 * This is called before anything is done with the package within {@link Insulin}
	 *
	 * @param hdlPackage
	 *            the package that will be Insulated
	 * @return either a new package or the input one
	 */
	public <T extends IHDLObject> T preInsulin(T hdlPackage, String src);

	/**
	 * This is called after all {@link Insulin} actions are done
	 *
	 * @param hdlPackage
	 *            the package that went through {@link Insulin}
	 * @return either a new package or the input one
	 */
	public <T extends IHDLObject> T postInsulin(T hdlPackage, String src);
}

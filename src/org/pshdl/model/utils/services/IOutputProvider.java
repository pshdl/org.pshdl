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
package org.pshdl.model.utils.services;

public interface IOutputProvider {
	/**
	 * The hook under which this {@link IOutputProvider} is activated
	 * 
	 * @return a short, non space containing id
	 */
	public String getHookName();

	/**
	 * An informal information about which arguments this
	 * {@link IOutputProvider} is expecting and how it can be used.
	 * 
	 * @return multiple strings, each explaining one option. The first one is
	 *         some general information
	 */
	public String[] getUsage();

	/**
	 * Invokes this {@link IOutputProvider} and passes the given arguments to
	 * it.
	 * 
	 * @param args
	 *            the arguments including the hook
	 * @return an explanation of what went wrong or <code>null</code> if it was
	 *         successful
	 */
	public String invoke(String args[]) throws Exception;
}

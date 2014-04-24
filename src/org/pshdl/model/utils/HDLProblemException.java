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
package org.pshdl.model.utils;

import java.util.Arrays;

import org.pshdl.model.validation.Problem;

public class HDLProblemException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -1516706647395219835L;
	public final Problem[] problems;

	public HDLProblemException(String message, Problem... problems) {
		super(message);
		this.problems = problems;
	}

	public HDLProblemException(Problem... problems) {
		super("The following problems where encountered:" + Arrays.toString(problems));
		this.problems = problems;
	}

	public Problem[] getProblems() {
		return problems;
	}
}

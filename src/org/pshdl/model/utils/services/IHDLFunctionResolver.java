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

import java.math.BigInteger;
import java.util.List;

import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation;

import com.google.common.base.Optional;
import com.google.common.collect.Range;

public interface IHDLFunctionResolver {
	public HDLTypeInferenceInfo resolve(HDLFunctionCall function);

	public Optional<BigInteger> evaluate(HDLFunctionCall function, List<BigInteger> args, HDLEvaluationContext context);

	public Optional<Range<BigInteger>> range(HDLFunctionCall function, HDLEvaluationContext context);

	public String[] getFunctionNames();

	public FunctionInformation getFunctionInfo(String funcName);
}

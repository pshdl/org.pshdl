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

import java.math.*;
import java.util.*;

import org.pshdl.generator.vhdl.*;
import org.pshdl.model.*;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.utils.services.CompilerInformation.*;

import com.google.common.base.*;
import com.google.common.collect.*;

import de.upb.hni.vmagic.expression.*;

public interface IHDLFunctionResolver {
	public HDLTypeInferenceInfo resolve(HDLFunctionCall function);

	public Optional<BigInteger> evaluate(HDLFunctionCall function, List<BigInteger> args, HDLEvaluationContext context);

	public Range<BigInteger> range(HDLFunctionCall function, HDLEvaluationContext context);

	public String[] getFunctionNames();

	public VHDLContext toVHDL(HDLFunctionCall function, int pid);

	public Expression<?> toVHDLExpression(HDLFunctionCall function);

	public FunctionInformation getFunctionInfo(String funcName);
}

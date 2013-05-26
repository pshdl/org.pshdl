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
package org.pshdl.model.types.builtIn;

import java.math.*;
import java.util.*;

import org.pshdl.generator.vhdl.*;
import org.pshdl.model.*;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation.FunctionType;
import org.pshdl.model.utils.services.*;

import com.google.common.base.*;
import com.google.common.collect.Range;

import de.upb.hni.vmagic.Range.Direction;
import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.literal.*;

public class HDLBuiltInFunctions implements IHDLFunctionResolver {

	public static enum BuiltInFunctions {
		highZ
	}

	@Override
	public HDLTypeInferenceInfo resolve(HDLFunctionCall function) {
		final String name = function.getNameRefName().getLastSegment();
		try {
			final BuiltInFunctions func = BuiltInFunctions.valueOf(name);
			switch (func) {
			case highZ:
				if (function.getParams().size() == 1)
					return new HDLTypeInferenceInfo(HDLPrimitive.getBit(), HDLPrimitive.getNatural());
				return new HDLTypeInferenceInfo(HDLPrimitive.getBit());
			}
		} catch (final Exception e) {
		}
		return null;
	}

	@Override
	public Optional<BigInteger> evaluate(HDLFunctionCall function, List<BigInteger> args, HDLEvaluationContext context) {
		switch (getFuncEnum(function)) {
		case highZ:
			return Optional.absent();
		}
		return Optional.absent();
	}

	private BuiltInFunctions getFuncEnum(HDLFunctionCall function) {
		final String name = function.getNameRefName().getLastSegment();
		final BuiltInFunctions func = BuiltInFunctions.valueOf(name);
		return func;
	}

	@Override
	public Range<BigInteger> range(HDLFunctionCall function, HDLEvaluationContext context) {
		switch (getFuncEnum(function)) {
		case highZ:
			return null;
		}
		return null;
	}

	@Override
	public String[] getFunctionNames() {
		final String[] res = new String[BuiltInFunctions.values().length];
		final BuiltInFunctions[] values = BuiltInFunctions.values();
		for (int i = 0; i < values.length; i++) {
			final BuiltInFunctions bif = values[i];
			res[i] = bif.name();
		}
		return res;
	}

	@Override
	public VHDLContext toVHDL(HDLFunctionCall function, int pid) {
		return null;
	}

	@Override
	public Expression<?> toVHDLExpression(HDLFunctionCall function) {
		switch (getFuncEnum(function)) {
		case highZ:
			if (function.getParams().size() == 0)
				return new CharacterLiteral('Z');

			final Aggregate aggregate = new Aggregate();
			final HDLRange range = new HDLRange().setFrom(HDLLiteral.get(1)).setTo(function.getParams().get(0));
			aggregate.createAssociation(new CharacterLiteral('Z'), VHDLExpressionExtension.INST.toVHDL(range, Direction.TO));
			return aggregate;
		}
		return null;
	}

	@Override
	public FunctionInformation getFunctionInfo(String funcName) {
		switch (BuiltInFunctions.valueOf(funcName)) {
		case highZ: {
			final FunctionInformation fi = new FunctionInformation(funcName, HDLBuiltInFunctions.class.getSimpleName(),
					"Returns a high Z. This is useful for tri-state busses, high z however is not supported in PSHDL as computational value.", "highZ", false, FunctionType.NATIVE);
			return fi;
		}
		}
		return null;
	}
}

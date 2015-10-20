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

import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLFunctionParameter.RWType;
import org.pshdl.model.HDLFunctionParameter.Type;
import org.pshdl.model.HDLNativeFunction;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.types.builtIn.HDLFunctionImplementation.HDLDefaultFunctionImpl;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.services.INativeFunctionProvider;

public class TestbenchFunctions extends HDLDefaultFunctionImpl implements INativeFunctionProvider {

	public TestbenchFunctions() {
		super(true, false);
	}

	public static enum SimulationFunctions {
		waitFor, waitUntil, wait, pulse;
		public HDLQualifiedName getName() {
			return HDLQualifiedName.create("pshdl", name());
		}
	}

	public static HDLFunction WAIT = (HDLFunction) createWait().freeze(null);
	public static HDLFunction WAITFOR = (HDLFunction) createWaitFor().freeze(null);
	public static HDLFunction WAITUNTIL = (HDLFunction) createWaitUntil().freeze(null);
	public static HDLFunction PULSE = (HDLFunction) createPulse().freeze(null);

	private static HDLFunction createPulse() {
		return new HDLNativeFunction().setSimOnly(true).setName("pshdl." + SimulationFunctions.pulse.name())
				.setReturnType(new HDLFunctionParameter().setType(Type.PARAM_BOOL).setRw(RWType.RETURN))
				.addArgs(new HDLFunctionParameter().setType(Type.PARAM_BIT).setName(new HDLVariable().setName("toggleExpression")).setRw(RWType.WRITE))
				.addArgs(new HDLFunctionParameter().setType(Type.PARAM_ANY_UINT).setName(new HDLVariable().setName("amount")).setRw(RWType.READ)).addArgs(new HDLFunctionParameter()
						.setType(Type.PARAM_ENUM).setEnumSpec(new HDLQualifiedName("pshdl.TimeUnit")).setName(new HDLVariable().setName("timeUnit")).setRw(RWType.READ));
	}

	private static HDLFunction createWait() {
		return new HDLNativeFunction().setSimOnly(true).setName("pshdl." + SimulationFunctions.wait.name())
				.setReturnType(new HDLFunctionParameter().setType(Type.PARAM_BOOL).setRw(RWType.RETURN));
	}

	private static HDLFunction createWaitFor() {
		return new HDLNativeFunction().setSimOnly(true).setName("pshdl." + SimulationFunctions.waitFor.name())
				.setReturnType(new HDLFunctionParameter().setType(Type.PARAM_BOOL).setRw(RWType.RETURN))
				.addArgs(new HDLFunctionParameter().setType(Type.PARAM_ANY_UINT).setName(new HDLVariable().setName("amount")).setRw(RWType.READ).setConstant(true))
				.addArgs(new HDLFunctionParameter().setType(Type.PARAM_ENUM).setEnumSpec(new HDLQualifiedName("pshdl.TimeUnit")).setName(new HDLVariable().setName("timeUnit"))
						.setRw(RWType.READ));
	}

	private static HDLFunction createWaitUntil() {
		return new HDLNativeFunction().setSimOnly(true).setName("pshdl." + SimulationFunctions.waitUntil.name())
				.setReturnType(new HDLFunctionParameter().setType(Type.PARAM_BOOL).setRw(RWType.RETURN))
				.addArgs(new HDLFunctionParameter().setType(Type.PARAM_BOOL).setName(new HDLVariable().setName("expression")).setRw(RWType.READ));
	}

	@Override
	public HDLFunctionImplementation[] getStaticFunctions() {
		return new HDLFunctionImplementation[] { this };
	}

	@Override
	public String getDocumentation(HDLFunction function) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HDLFunction[] signatures() {
		return new HDLFunction[] { PULSE, WAIT, WAITFOR, WAITUNTIL };
	}

}

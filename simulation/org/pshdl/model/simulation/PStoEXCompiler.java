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
package org.pshdl.model.simulation;

import java.io.*;
import java.util.*;

import org.pshdl.interpreter.*;
import org.pshdl.interpreter.utils.Graph.CycleException;
import org.pshdl.interpreter.utils.*;
import org.pshdl.model.*;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.services.*;
import org.pshdl.model.validation.*;

public class PStoEXCompiler extends PSAbstractCompiler implements IOutputProvider {

	public PStoEXCompiler() {

	}

	@Override
	public String getHookName() {
		return "psex";
	}

	@Override
	public String[] getUsage() {
		return null;
	}

	@Override
	public String invoke(String[] args) throws Exception {
		if (args.length == 0)
			return "Not enough arguments, try help:" + getHookName();
		final HDLQualifiedName unitName = new HDLQualifiedName(args[0]);
		System.out.println("Parsing files:");
		boolean syntaxerror = false;
		final Set<Problem> problems = new HashSet<Problem>();
		for (int i = 1; i < args.length; i++) {
			final File source = new File(args[i]);
			System.out.println("\t" + source);
			if (add(source, problems)) {
				syntaxerror = true;
			}
		}
		if (syntaxerror)
			return "Exiting because of syntax errors in the input";
		System.out.println("Validating:");
		if (validatePackages(problems))
			return "Exiting because of errors in the input";
		final HDLUnit unit = findUnit(unitName);
		if (unit == null)
			return "Unit:" + unitName + " not found";
		final String src = unit.getLibrary().getSrc(unitName);
		try {
			final ExecutableModel em = createExecutable(unit, src);
			IOUtil.writeExecutableModel(src, new Date().getTime(), em, new File("a.em"));
		} catch (final CycleException e) {
			e.explain(System.err);
		}
		close();
		return "Success";
	}

	public ExecutableModel createExecutable(HDLUnit unit, String src) throws CycleException {
		final HDLEvaluationContext context = HDLEvaluationContext.createDefault(unit);
		final HDLUnit simulationModel = HDLSimulator.createSimulationModel(unit, context, src);
		final FluidFrame model = SimulationTransformationExtension.simulationModelOf(simulationModel, context);
		final ExecutableModel em = model.getExecutable();
		try {
			em.sortTopological();
		} catch (final CycleException e) {
			e.model = em;
			throw e;
		}
		return em;
	}

	public static void main(String[] args) throws Exception {
		final String invoke = new PStoEXCompiler().invoke(args);
		if (invoke != null) {
			System.err.println(invoke);
		}
	}

	public ExecutableModel createExecutable(HDLQualifiedName name, String src) throws CycleException {
		final HDLUnit findUnit = findUnit(name);
		if (findUnit == null)
			throw new IllegalArgumentException("No unit with name:" + name);
		return createExecutable(findUnit, src);
	}
}

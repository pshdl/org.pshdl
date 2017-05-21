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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pshdl.model.HDLDirectGeneration;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import org.pshdl.model.utils.services.CompilerInformation;
import org.pshdl.model.utils.services.IHDLGenerator;
import org.pshdl.model.utils.services.IHDLGenerator.HDLGenerationInfo;
import org.pshdl.model.utils.services.IServiceProvider;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.builtin.ErrorCode;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

public class HDLGenerators {
	private static Map<String, IHDLGenerator> generators;

	public static void init(CompilerInformation info, IServiceProvider sp) {
		generators = Maps.newLinkedHashMap();
		for (final IHDLGenerator gen : sp.getAllGenerators()) {
			for (final String name : gen.getNames()) {
				generators.put(name, gen);
				info.registeredGenerators.put(name, gen.getGeneratorInfo(name));
			}
		}
	}

	public static List<HDLVariableDeclaration> getPortAdditions(HDLDirectGeneration hdl) {
		final IHDLGenerator generator = generators.get(hdl.getGeneratorID());
		if (generator != null) {
			final List<HDLVariableDeclaration> portAdditions = generator.getPortAdditions(hdl);
			if (portAdditions != null) {
				for (final HDLVariableDeclaration hvd : portAdditions) {
					hvd.addAnnotations(HDLBuiltInAnnotations.genSignal.create(hdl.getGeneratorID()));
				}
			}
			return portAdditions;
		}
		return Collections.emptyList();
	}

	public static Optional<HDLInterface> getInterface(HDLDirectGeneration hdl) {
		final IHDLGenerator generator = generators.get(hdl.getGeneratorID());
		if (generator != null) {
			final Optional<HDLInterface> hif = generator.getInterface(hdl);
			if (hif.isPresent()) {
				return Optional.of(hif.get().copyDeepFrozen(hdl));
			}
			return hif;
		}
		return Optional.absent();
	}

	public static Optional<HDLGenerationInfo> getImplementation(HDLDirectGeneration hdl) {
		final IHDLGenerator generator = generators.get(hdl.getGeneratorID());
		if (generator != null) {
			return generator.getImplementation(hdl);
		}
		return Optional.absent();
	}

	public static boolean validate(HDLDirectGeneration hdg, Set<Problem> problems, HDLEvaluationContext context) {
		final IHDLGenerator generator = generators.get(hdg.getGeneratorID());
		if (generator != null) {
			return generator.validate(hdg, problems, context);
		}
		problems.add(new Problem(ErrorCode.GENERATOR_NOT_KNOWN, hdg));
		return false;
	}

	public static Collection<IHDLGenerator> getAllGenerators() {
		return generators.values();
	}

}

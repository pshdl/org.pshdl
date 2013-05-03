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

import java.util.*;

public interface IServiceProvider {
	public Collection<IHDLAnnotation> getAllAnnotations();

	public Collection<IHDLFunctionResolver> getAllFunctions();

	public Collection<IHDLGenerator> getAllGenerators();

	public Collection<IHDLValidator> getAllValidators();

	public static class ServiceLoaderProvider implements IServiceProvider {

		@Override
		public Collection<IHDLAnnotation> getAllAnnotations() {
			ServiceLoader<IHDLAnnotationProvider> annos = ServiceLoader.load(IHDLAnnotationProvider.class);
			List<IHDLAnnotation> res = new LinkedList<IHDLAnnotation>();
			for (IHDLAnnotationProvider ap : annos) {
				for (IHDLAnnotation anno : ap.getAnnotations()) {
					res.add(anno);
				}
			}
			return res;
		}

		@Override
		public Collection<IHDLFunctionResolver> getAllFunctions() {
			ServiceLoader<IHDLFunctionResolver> functions = ServiceLoader.load(IHDLFunctionResolver.class);
			List<IHDLFunctionResolver> res = new LinkedList<IHDLFunctionResolver>();
			for (IHDLFunctionResolver func : functions) {
				res.add(func);
			}
			return res;
		}

		@Override
		public Collection<IHDLGenerator> getAllGenerators() {
			ServiceLoader<IHDLGenerator> generators = ServiceLoader.load(IHDLGenerator.class);
			List<IHDLGenerator> res = new LinkedList<IHDLGenerator>();
			for (IHDLGenerator gen : generators) {
				res.add(gen);
			}
			return res;
		}

		@Override
		public Collection<IHDLValidator> getAllValidators() {
			ServiceLoader<IHDLValidator> generators = ServiceLoader.load(IHDLValidator.class);
			List<IHDLValidator> res = new LinkedList<IHDLValidator>();
			for (IHDLValidator gen : generators) {
				res.add(gen);
			}
			return res;
		}

	}
}
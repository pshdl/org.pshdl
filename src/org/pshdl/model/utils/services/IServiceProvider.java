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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

import com.google.common.collect.Lists;

public interface IServiceProvider {
	public Collection<IHDLAnnotation> getAllAnnotations();

	public Collection<INativeFunctionProvider> getAllNativeFunctionProvider();

	public Collection<IHDLGenerator> getAllGenerators();

	public Collection<IHDLValidator> getAllValidators();

	public Collection<IInsulinParticitant> getAllInsulinParticipants();

	public <T> Collection<T> getAllImplementations(Class<T> clazz);

	public static class ServiceLoaderProvider implements IServiceProvider {

		@Override
		public Collection<IHDLAnnotation> getAllAnnotations() {
			final ServiceLoader<IHDLAnnotationProvider> annos = ServiceLoader.load(IHDLAnnotationProvider.class);
			final List<IHDLAnnotation> res = new LinkedList<>();
			for (final IHDLAnnotationProvider ap : annos) {
				for (final IHDLAnnotation anno : ap.getAnnotations()) {
					res.add(anno);
				}
			}
			return res;
		}

		@Override
		public Collection<INativeFunctionProvider> getAllNativeFunctionProvider() {
			final ServiceLoader<INativeFunctionProvider> functions = ServiceLoader.load(INativeFunctionProvider.class);
			final List<INativeFunctionProvider> res = new LinkedList<>();
			for (final INativeFunctionProvider func : functions) {
				res.add(func);
			}
			return res;
		}

		@Override
		public Collection<IHDLGenerator> getAllGenerators() {
			final ServiceLoader<IHDLGenerator> generators = ServiceLoader.load(IHDLGenerator.class);
			final List<IHDLGenerator> res = new LinkedList<>();
			for (final IHDLGenerator gen : generators) {
				res.add(gen);
			}
			return res;
		}

		@Override
		public Collection<IHDLValidator> getAllValidators() {
			final ServiceLoader<IHDLValidator> generators = ServiceLoader.load(IHDLValidator.class);
			final List<IHDLValidator> res = new LinkedList<>();
			for (final IHDLValidator gen : generators) {
				res.add(gen);
			}
			return res;
		}

		@Override
		public <T> Collection<T> getAllImplementations(Class<T> clazz) {
			final ServiceLoader<T> load = ServiceLoader.load(clazz);
			return Lists.newLinkedList(load);
		}

		@Override
		public Collection<IInsulinParticitant> getAllInsulinParticipants() {
			final ServiceLoader<IInsulinParticitant> functions = ServiceLoader.load(IInsulinParticitant.class);
			final List<IInsulinParticitant> res = new LinkedList<>();
			for (final IInsulinParticitant func : functions) {
				res.add(func);
			}
			return res;
		}

		@Override
		public Collection<IDynamicFunctionProvider> getAllDynamicFunctionProvider() {
			final ServiceLoader<IDynamicFunctionProvider> functions = ServiceLoader.load(IDynamicFunctionProvider.class);
			final List<IDynamicFunctionProvider> res = new LinkedList<>();
			for (final IDynamicFunctionProvider func : functions) {
				res.add(func);
			}
			return res;
		}

	}

	public Collection<IDynamicFunctionProvider> getAllDynamicFunctionProvider();
}

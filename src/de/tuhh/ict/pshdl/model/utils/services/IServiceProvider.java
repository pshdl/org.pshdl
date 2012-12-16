package de.tuhh.ict.pshdl.model.utils.services;

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

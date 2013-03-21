package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.IServiceProvider.ServiceLoaderProvider;
import de.tuhh.ict.pshdl.model.validation.*;

/**
 * HDLCore is the central place to register custom services like
 * {@link IHDLAnnotation}, {@link IHDLGenerator}, {@link IHDLValidator},
 * {@link IHDLFunctionResolver} and {@link IHDLAnnotationProvider}. Those
 * services are added via an {@link IServiceProvider}. The default
 * {@link IServiceProvider}, {@link IServiceProvider.ServiceLoaderProvider} uses
 * the {@link ServiceLoader} facility of the JRE
 * 
 * @author Karsten Becker
 * 
 */
public class HDLCore {
	private static final CompilerInformation info = new CompilerInformation("0.1b");
	private static boolean initialized = false;

	/**
	 * Initializes the PSHDL Compiler infrastructure with the annotations,
	 * generators, validators and functions provided by this
	 * {@link IServiceProvider}
	 * 
	 * @param serviceProvider
	 *            an implementation of the {@link IServiceProvider}
	 */
	public static synchronized void init(IServiceProvider serviceProvider) {
		info.registeredAnnotations.clear();
		info.registeredFunctions.clear();
		info.registeredGenerators.clear();
		info.registeredValidators.clear();
		HDLFunctions.init(info, serviceProvider);
		HDLAnnotations.init(info, serviceProvider);
		HDLGenerators.init(info, serviceProvider);
		HDLValidator.init(info, serviceProvider);
		initialized = true;
	}

	/**
	 * Returns all known information about the current compiler
	 * 
	 * @return {@link CompilerInformation} which contain all known services
	 */
	public static CompilerInformation getCompilerInformation() {
		if (!initialized)
			throw new RuntimeException("HDLCore needs to be initialized first!");
		return info;
	}

	/**
	 * Initialize the {@link HDLCore} using the {@link ServiceLoaderProvider}.
	 * If the {@link HDLCore} is already initialized, the known configuration
	 * will be overwritten
	 */
	public static void defaultInit() {
		init(new IServiceProvider.ServiceLoaderProvider());
	}

	/**
	 * Returns whether the {@link HDLCore} was initialized
	 * 
	 * @return <code>true</code> when {@link #init(IServiceProvider)} was called
	 *         at least once
	 */
	public static boolean isInitialized() {
		return initialized;
	}
}

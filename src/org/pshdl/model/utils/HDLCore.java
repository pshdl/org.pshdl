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
package org.pshdl.model.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.ServiceLoader;

import org.pshdl.model.types.builtIn.HDLAnnotations;
import org.pshdl.model.types.builtIn.HDLFunctions;
import org.pshdl.model.types.builtIn.HDLGenerators;
import org.pshdl.model.utils.services.CompilerInformation;
import org.pshdl.model.utils.services.IHDLAnnotation;
import org.pshdl.model.utils.services.IHDLAnnotationProvider;
import org.pshdl.model.utils.services.IHDLFunctionResolver;
import org.pshdl.model.utils.services.IHDLGenerator;
import org.pshdl.model.utils.services.IHDLValidator;
import org.pshdl.model.utils.services.IServiceProvider;
import org.pshdl.model.utils.services.IServiceProvider.ServiceLoaderProvider;
import org.pshdl.model.validation.HDLValidator;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

/**
 * HDLCore is the central place to register custom services like
 * {@link IHDLAnnotation}, {@link IHDLGenerator}, {@link IHDLValidator},
 * {@link IHDLFunctionResolver} and {@link IHDLAnnotationProvider}. Those
 * services are added via an {@link IServiceProvider}. The default
 * {@link IServiceProvider},
 * {@link org.pshdl.model.utils.services.IServiceProvider.ServiceLoaderProvider}
 * uses the {@link ServiceLoader} facility of the JRE
 *
 * @author Karsten Becker
 *
 */
public class HDLCore {
	public static final String VERSION = getVersion();
	private static final CompilerInformation info = new CompilerInformation(VERSION);
	private static boolean initialized = false;
	private static IServiceProvider serviceProvider = new ServiceLoaderProvider();

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
		info.registeredInsulinParticipant.clear();
		HDLFunctions.init(info, serviceProvider);
		HDLAnnotations.init(info, serviceProvider);
		HDLGenerators.init(info, serviceProvider);
		HDLValidator.init(info, serviceProvider);
		Insulin.init(info, serviceProvider);
		initialized = true;
		HDLCore.serviceProvider = serviceProvider;
	}

	private static String getVersion() {
		try (final InputStream versionStream = HDLCore.class.getResourceAsStream("/version")) {
			if (versionStream == null) {
				System.err.println("Failed to read version");
				System.exit(-1);
			}
			return new String(ByteStreams.toByteArray(versionStream), Charsets.UTF_8).trim();
		} catch (final IOException e) {
			System.err.println("Failed to read version");
			System.exit(-1);
		}
		throw new RuntimeException("Can not read version");
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

	public static <T> Collection<T> getAllImplementations(Class<T> clazz) {
		return serviceProvider.getAllImplementations(clazz);
	}

}

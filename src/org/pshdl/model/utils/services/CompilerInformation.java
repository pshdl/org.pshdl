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

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompilerInformation {
	public static class FunctionInformation {

		public static enum FunctionType {
			NATIVE, INLINE, SUBSTITUTION
		}

		public final String name;
		public final String provider;
		public final String description;
		public final Map<String, String> arguments = new LinkedHashMap<String, String>();
		public final String returnInfo;
		public final boolean simulationOnly;
		public final FunctionType type;

		public FunctionInformation(String name, String provider, String description, String returnInfo, boolean simulationOnly, FunctionType type) {
			super();
			this.name = name;
			this.returnInfo = returnInfo;
			this.simulationOnly = simulationOnly;
			this.provider = provider;
			this.description = description;
			this.type = type;
		}
	}

	public static class AnnotationInformation {
		public final String provider;
		public final String name;
		public final String summary;
		public final String arguments;

		public AnnotationInformation(String provider, String name, String summary, String arguments) {
			super();
			this.provider = provider;
			this.name = name;
			this.summary = summary;
			this.arguments = arguments;
		}
	}

	public static class GeneratorInformation implements Serializable {
		private static final long serialVersionUID = -6351106148480464153L;
		public final String provider;
		public final String name;
		public final String summary;
		public final Map<String, String> arguments = new LinkedHashMap<String, String>();

		public GeneratorInformation(String provider, String name, String summary) {
			super();
			this.provider = provider;
			this.name = name;
			this.summary = summary;
		}
	}

	public final String version;
	public final Map<String, AnnotationInformation> registeredAnnotations = new LinkedHashMap<String, AnnotationInformation>();
	public final Map<String, GeneratorInformation> registeredGenerators = new LinkedHashMap<String, GeneratorInformation>();
	public final Map<String, FunctionInformation> registeredFunctions = new LinkedHashMap<String, FunctionInformation>();
	public final Map<String, IHDLValidator> registeredValidators = new LinkedHashMap<String, IHDLValidator>();
	public final Map<String, IInsulinParticitant> registeredInsulinParticipant = new LinkedHashMap<String, IInsulinParticitant>();

	public CompilerInformation(String version) {
		super();
		this.version = version;
	}

}

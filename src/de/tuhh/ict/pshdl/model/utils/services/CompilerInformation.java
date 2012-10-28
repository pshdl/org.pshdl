package de.tuhh.ict.pshdl.model.utils.services;

import java.io.*;
import java.util.*;

public class CompilerInformation implements Serializable {
	public static class FunctionInformation implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -731752567686038980L;

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

	public static class AnnotationInformation implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -847723181271503799L;
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
		/**
		 * 
		 */
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 7392136487645025249L;
	public final String version;
	public final Map<String, AnnotationInformation> registeredAnnotations = new LinkedHashMap<String, AnnotationInformation>();
	public final Map<String, GeneratorInformation> registeredGenerators = new LinkedHashMap<String, GeneratorInformation>();
	public final Map<String, FunctionInformation> registeredFunctions = new LinkedHashMap<String, FunctionInformation>();

	public CompilerInformation(String version) {
		super();
		this.version = version;
	}

}

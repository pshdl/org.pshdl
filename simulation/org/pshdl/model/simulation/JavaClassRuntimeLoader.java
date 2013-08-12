package org.pshdl.model.simulation;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;

import javax.tools.*;
import javax.tools.JavaCompiler;

import org.pshdl.interpreter.*;

public class JavaClassRuntimeLoader {

	public static IHDLInterpreter compileAndLoad(String name, String source, boolean disableEdge, boolean disableOutputLogic) throws Exception {
		final File tempDir = new File("pshdl2java");
		tempDir.mkdir();
		tempDir.deleteOnExit();
		final String pathName = name.replace('.', File.separatorChar) + ".java";
		final File sourceFile = new File(tempDir, pathName);
		final File pkgDir = sourceFile.getParentFile();
		pkgDir.mkdirs();
		final FileWriter fw = new FileWriter(sourceFile);
		fw.append(source);
		fw.close();

		// Compile source file.
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler.run(null, null, null, sourceFile.getPath()) != 0)
			return null;

		// Load and instantiate compiled class.
		final URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { tempDir.toURI().toURL() });
		final Class<?> cls = Class.forName(name, true, classLoader);
		final Constructor<?> constructor = cls.getConstructor(Boolean.TYPE, Boolean.TYPE);
		final IHDLInterpreter instance = (IHDLInterpreter) constructor.newInstance(disableEdge, disableOutputLogic);
		tempDir.delete();
		return instance;
	}

}

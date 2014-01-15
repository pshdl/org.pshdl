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

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

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.pshdl.interpreter.IHDLInterpreter;

import com.google.common.io.Files;

public class JavaClassRuntimeLoader {

	public static IHDLInterpreter compileAndLoad(String name, String source, boolean disableEdge, boolean disableOutputLogic) throws Exception {
		final File tempDir = Files.createTempDir();
		tempDir.deleteOnExit();
		final String pathName = name.replace('.', File.separatorChar) + ".java";
		final File sourceFile = new File(tempDir, pathName);
		final File pkgDir = sourceFile.getParentFile();
		if (!pkgDir.mkdirs())
			throw new IllegalArgumentException("Failed to create package directories:" + pkgDir);
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
		if (!tempDir.delete())
			throw new IllegalArgumentException("Failed to delete temp directory:" + tempDir);
		return instance;
	}

}

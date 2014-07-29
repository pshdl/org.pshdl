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
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.pshdl.interpreter.IChangeListener;
import org.pshdl.interpreter.IHDLInterpreter;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class JavaClassRuntimeLoader implements AutoCloseable {

	public static class DiagnosticsException extends RuntimeException {
		private static final long serialVersionUID = -1283967107290479965L;
		public final List<Diagnostic<? extends JavaFileObject>> diagnostics;

		public DiagnosticsException(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
			this.diagnostics = diagnostics;
		}

		@Override
		public String getMessage() {
			for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
				if (diagnostic.getKind() == Kind.ERROR)
					return diagnostic.getMessage(null);
			}
			return super.getMessage();
		}
	}

	private final static class ErrorCheckDiagnostic implements DiagnosticListener<JavaFileObject> {

		public Kind kind;
		public List<Diagnostic<? extends JavaFileObject>> diagnostics = Lists.newArrayList();

		@Override
		public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
			final Kind newKind = diagnostic.getKind();
			if (newKind == Kind.ERROR) {
				kind = newKind;
			}
			diagnostics.add(diagnostic);
		}
	}

	private final File tempDir;
	private final URLClassLoader classLoader;
	private final JavaCompiler compiler;
	private final StandardJavaFileManager fileManager;

	public JavaClassRuntimeLoader() throws Exception {
		tempDir = Files.createTempDir();
		tempDir.deleteOnExit();
		classLoader = URLClassLoader.newInstance(new URL[] { tempDir.toURI().toURL() });
		compiler = ToolProvider.getSystemJavaCompiler();
		fileManager = compiler.getStandardFileManager(null, null, null);
		final String property = System.getProperty("java.class.path");
		final List<File> cp = new ArrayList<>();
		final Iterable<String> split = Splitter.on(File.pathSeparatorChar).split(property);
		for (final String entry : split) {
			cp.add(new File(entry));
		}
		cp.add(tempDir);
		fileManager.setLocation(StandardLocation.CLASS_PATH, cp);
		fileManager.setLocation(StandardLocation.SOURCE_PATH, Collections.singleton(tempDir));
	}

	public IHDLInterpreter compileAndLoad(String mainClassFQN, String sourceCode, boolean disableEdge, boolean disableOutputLogic) throws Exception {
		final Class<?> cls = compileClass(mainClassFQN, sourceCode);
		final Constructor<?> constructor = cls.getConstructor(Boolean.TYPE, Boolean.TYPE);
		final IHDLInterpreter instance = (IHDLInterpreter) constructor.newInstance(disableEdge, disableOutputLogic);
		return instance;
	}

	public Class<?> compileClass(String mainClassFQN, String sourceCode) throws Exception {
		final String pathName = mainClassFQN.replace('.', File.separatorChar) + ".java";
		final File sourceFile = new File(tempDir, pathName);
		final File pkgDir = sourceFile.getParentFile();
		if (pkgDir == null)
			throw new IllegalArgumentException("Failed to get parent of:" + sourceFile);
		if (!pkgDir.exists() && !pkgDir.mkdirs())
			throw new IllegalArgumentException("Failed to create package directories:" + pkgDir);
		Files.write(sourceCode, sourceFile, StandardCharsets.UTF_8);

		final StringWriter error = new StringWriter();
		final ErrorCheckDiagnostic diagnostic = new ErrorCheckDiagnostic();
		compiler.getTask(error, fileManager, diagnostic, null, null, fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile))).call();
		if (diagnostic.kind == Kind.ERROR)
			throw new DiagnosticsException(diagnostic.diagnostics);

		// Load and instantiate compiled class.
		return Class.forName(mainClassFQN, true, classLoader);
	}

	@Override
	public void close() throws Exception {
		fileManager.close();
	}

	public File getTempDir() {
		return tempDir;
	}

	public IHDLInterpreter compileAndLoadChangeAdapter(String mainClassFQN, String sourceCode, IHDLInterpreter mainInterpreter, IChangeListener... listeners) throws Exception {
		final Class<?> adapterClass = compileClass(mainClassFQN, sourceCode);
		Constructor<?> constructor = adapterClass.getConstructor(mainInterpreter.getClass(), IChangeListener[].class);
		if (constructor == null) {
			constructor = adapterClass.getConstructor(IHDLInterpreter.class, IChangeListener[].class);
		}
		return (IHDLInterpreter) constructor.newInstance(mainInterpreter, listeners);
	}
}

package org.pshdl.model.utils;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

import org.pshdl.model.*;
import org.pshdl.model.parser.*;
import org.pshdl.model.validation.*;
import org.pshdl.model.validation.Problem.ProblemSeverity;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;

public class PSAbstractCompiler {

	protected final String uri;
	protected final HDLLibrary lib;
	protected final Map<String, HDLPackage> pkgs = Maps.newConcurrentMap();
	protected final Multimap<String, Problem> issues = Multimaps.synchronizedMultimap(LinkedHashMultimap.<String, Problem> create());

	public PSAbstractCompiler() {
		this("PSHDLLib" + new Random().nextLong());
	}

	public PSAbstractCompiler(String uri) {
		super();
		if (!HDLCore.isInitialized()) {
			HDLCore.defaultInit();
		}
		this.uri = uri;
		lib = new HDLLibrary();
		HDLLibrary.registerLibrary(uri, lib);
	}

	public HDLUnit findUnit(HDLQualifiedName unitName) {
		for (final Entry<String, HDLPackage> e : pkgs.entrySet()) {
			final HDLPackage parse = e.getValue();
			final HDLUnit first = HDLQuery.select(HDLUnit.class).from(parse).whereObj().fullNameIs(unitName).getFirst();
			if (first != null)
				return first;
		}
		return null;
	}

	public boolean validatePackages(Set<Problem> problems) throws IOException, FileNotFoundException {
		boolean validationError = false;
		for (final Entry<String, HDLPackage> e : pkgs.entrySet()) {
			if (validateFile(e.getValue(), problems)) {
				validationError = true;
			}
		}
		return validationError;
	}

	public boolean addFilesMultiThreaded(Collection<File> files, final ConcurrentMap<String, Set<Problem>> problems, ExecutorService service) throws InterruptedException,
			ExecutionException {
		final List<Future<Boolean>> futures = Lists.newArrayListWithCapacity(files.size());
		for (final File file : files) {
			final Future<Boolean> future = service.submit(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					final Set<Problem> localProblems = Sets.newHashSet();
					final boolean error = add(file, localProblems);
					problems.put(getSourceName(file), localProblems);
					return error;
				}
			});
			futures.add(future);
		}
		return collectResults(futures);
	}

	private boolean collectResults(final List<Future<Boolean>> futures) throws InterruptedException, ExecutionException {
		boolean validationError = false;
		for (final Future<Boolean> future : futures) {
			final Boolean b = future.get();
			if (b) {
				validationError = true;
			}
		}
		return validationError;
	}

	public boolean validatePackagesMultiThreaded(final ConcurrentMap<String, Set<Problem>> problems, ExecutorService service) throws IOException, FileNotFoundException,
			InterruptedException, ExecutionException {
		final List<Future<Boolean>> futures = Lists.newArrayListWithCapacity(pkgs.size());
		for (final Entry<String, HDLPackage> e : pkgs.entrySet()) {
			final Future<Boolean> future = service.submit(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					final Set<Problem> localProblems = Sets.newHashSet();
					final boolean error = validateFile(e.getValue(), localProblems);
					problems.put(e.getKey(), localProblems);
					return error;
				}

			});
			futures.add(future);
		}
		return collectResults(futures);
	}

	public boolean add(File source, Set<Problem> syntaxProblems) throws IOException, FileNotFoundException {
		final Set<Problem> problems = add(source);
		syntaxProblems.addAll(problems);
		return reportProblem(problems);
	}

	/**
	 * Parse and add a unit to the HDLLibrary so that all references can be
	 * resolved later
	 * 
	 * @param contents
	 *            the PSHDL module to add
	 * @param absolutePath
	 *            a source file name for this module
	 * @throws IOException
	 */
	public Set<Problem> add(File f) throws IOException {
		return add(Files.toString(f, Charsets.UTF_8), getSourceName(f));
	}

	/**
	 * Parse and add a unit to the HDLLibrary so that all references can be
	 * resolved later
	 * 
	 * @param contents
	 *            the PSHDL module to add
	 * @param src
	 *            a source file name for this module
	 * @param problems
	 *            syntax errors will be added to this Set when encountered
	 * @throws IOException
	 */
	public Set<Problem> add(InputStream contents, String src) throws IOException {
		final InputStreamReader r = new InputStreamReader(contents, Charsets.UTF_8);
		final String text = CharStreams.toString(r);
		r.close();
		return add(text, src);
	}

	/**
	 * Parse and add a unit to the HDLLibrary so that all references can be
	 * resolved later
	 * 
	 * @param contents
	 *            the PSHDL module to add
	 * @param src
	 *            a source file name for this module
	 * @param problems
	 *            syntax errors will be added to this Set when encountered
	 */
	public Set<Problem> add(String contents, String src) {
		final Set<Problem> problems = Sets.newHashSet();
		issues.removeAll(src);
		final HDLPackage pkg = PSHDLParser.parseString(contents, uri, problems, src);
		pkgs.put(src, pkg);
		issues.putAll(src, problems);
		return problems;
	}

	/**
	 * Removes all resources related to this src. This can be important for
	 * incremental compilation.
	 * 
	 * @param src
	 *            the src name that was used to register a added input
	 */
	public void remove(String src) {
		lib.removeAllSrc(src);
		pkgs.remove(src);
		issues.removeAll(src);
	}

	public boolean reportProblem(Set<Problem> syntaxProblems) {
		boolean error = false;
		if (syntaxProblems.size() != 0) {
			for (final Problem problem : syntaxProblems) {
				if (problem.severity == ProblemSeverity.ERROR) {
					error = true;
				}
			}
		}
		return error;
	}

	public boolean validateFile(HDLPackage parse, Set<Problem> problems) throws IOException, FileNotFoundException {
		final Set<Problem> validate = HDLValidator.validate(parse, null);
		problems.addAll(validate);
		return reportProblem(validate);
	}

	public HDLUnit takeFirstUnit(String file) {
		if (file != null) {
			for (final Entry<String, HDLPackage> e : pkgs.entrySet()) {
				if (e.getKey().equals(file)) {
					final ArrayList<HDLUnit> units = e.getValue().getUnits();
					for (final HDLUnit hdlUnit : units) {
						if (!hdlUnit.getSimulation())
							return hdlUnit;
					}
				}
			}
			throw new IllegalArgumentException("No modules found for: " + file);
		}
		final HDLPackage pkg = pkgs.values().iterator().next();
		for (final HDLUnit unit : pkg.getUnits()) {
			if (!unit.getSimulation())
				return unit;
		}
		return null;
	}

	public void close() {
		HDLLibrary.unregister(uri);
	}

	public String getSourceName(final File file) {
		return file.getAbsolutePath();
	}

}
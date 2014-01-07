package org.pshdl.model.utils;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

import org.pshdl.model.*;
import org.pshdl.model.parser.*;
import org.pshdl.model.utils.services.IHDLGenerator.SideFile;
import org.pshdl.model.validation.*;
import org.pshdl.model.validation.HDLValidator.HDLAdvise;
import org.pshdl.model.validation.Problem.ProblemSeverity;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;

public class PSAbstractCompiler {

	private static final Random RANDOM = new Random();

	/**
	 * Do not report any progress and proceed whenever possible
	 * 
	 * @author Karsten Becker
	 * 
	 */
	public static final class NullListener implements ICompilationListener {
		@Override
		public boolean startModule(String src, HDLPackage parse) {
			return true;
		}

		@Override
		public boolean useSource(String src, Collection<Problem> collection, boolean hasError) {
			return !hasError;
		}

	}

	public interface ICompilationListener {

		/**
		 * Check whether you want to continue compiling this source
		 * 
		 * @param src
		 *            the src location of this file
		 * @param parse
		 *            the model before running {@link Insulin}
		 * @return <code>true</code> to continue with the compilation,
		 *         <code>false</code> otherwise
		 */
		boolean startModule(String src, HDLPackage parse);

		boolean useSource(String src, Collection<Problem> collection, boolean hasError);

	}

	/**
	 * A container for the results of the compilation
	 * 
	 * @author Karsten Becker
	 * 
	 */
	public static class CompileResult {
		/**
		 * Problems that occurred during validation
		 */
		public final Set<Problem> syntaxProblems;
		/**
		 * The generated code if the compilation was successful,
		 * <code>null</code> otherwise
		 */
		public final String code;

		public final String codeType;

		/**
		 * The name of the first module encountered for display purposes. Will
		 * be &lt;ERROR&gt; if not successful
		 */
		public final String entityName;

		public final String fileName;
		/**
		 * All additional files that have been generated
		 */
		public final Set<SideFile> sideFiles;
		/**
		 * The src for which this code was generated
		 */
		public final String src;

		public CompileResult(Set<Problem> syntaxProblems, String code, String entityName, Collection<SideFile> sideFiles, String src, String codeType, boolean unitName) {
			super();
			this.syntaxProblems = syntaxProblems;
			this.code = code;
			this.entityName = entityName;
			this.src = src;
			if (sideFiles != null) {
				this.sideFiles = new HashSet<SideFile>(sideFiles);
			} else {
				this.sideFiles = new HashSet<SideFile>();
			}
			this.codeType = codeType;
			this.fileName = getFileName(src, codeType, unitName);
		}

		public boolean hasError() {
			return code == null;
		}

		private String getFileName(String src, String codeType, boolean unitName) {
			String newName = new File(src).getName();
			if (codeType == null) {
				codeType = "";
			}
			if (unitName) {
				newName = entityName + "." + codeType.toLowerCase();
			} else {
				newName = newName.substring(0, newName.length() - 5) + codeType.toLowerCase();
			}
			return newName;
		}

	}

	public static File[] writeFiles(File outDir, CompileResult result, boolean unitName) throws FileNotFoundException, IOException {
		if (result.hasError())
			return new File[0];
		final List<File> res = new LinkedList<File>();
		final File target = new File(outDir, result.fileName);
		res.add(target);
		FileOutputStream fos = new FileOutputStream(target);
		fos.write(result.code.getBytes(Charsets.UTF_8));
		fos.close();
		if (result.sideFiles != null) {
			for (final SideFile sd : result.sideFiles) {
				final File file = new File(outDir + "/" + sd.relPath);
				res.add(file);
				final File parentFile = file.getParentFile();
				if ((parentFile != null) && !parentFile.exists()) {
					parentFile.mkdirs();
				}
				fos = new FileOutputStream(file);
				if (sd.contents == SideFile.THIS) {
					fos.write(result.code.getBytes(Charsets.UTF_8));
				} else {
					fos.write(sd.contents);
				}
				fos.close();
			}
		}
		return res.toArray(new File[res.size()]);
	}

	protected final String uri;
	protected final HDLLibrary lib;
	protected final ConcurrentMap<String, HDLPackage> pkgs = Maps.newConcurrentMap();
	protected final ConcurrentMap<String, Set<Problem>> issues = Maps.newConcurrentMap();
	protected final Set<String> srcs = Collections.synchronizedSet(Sets.<String> newLinkedHashSet());
	protected boolean validated = false;
	private final ExecutorService service;

	public PSAbstractCompiler() {
		this("PSHDLLib" + nextLong(), null);
	}

	public PSAbstractCompiler(String uri, ExecutorService service) {
		super();
		if (!HDLCore.isInitialized()) {
			HDLCore.defaultInit();
		}
		if (uri == null) {
			this.uri = "RANDOM" + Long.toHexString(nextLong());
		} else {
			this.uri = uri;
		}
		this.service = service;
		this.lib = new HDLLibrary();
		HDLLibrary.registerLibrary(this.uri, this.lib);
	}

	public static long nextLong() {
		synchronized (RANDOM) {
			return RANDOM.nextLong();
		}
	}

	public boolean add(File source) throws Exception {
		return singleAdd(source);
	}

	/**
	 * Parse and add a unit to the HDLLibrary so that all references can be
	 * resolved later
	 * 
	 * @param contents
	 *            the PSHDL module to add
	 * @param src
	 *            a source file name for this module
	 * @throws IOException
	 */
	public boolean add(InputStream contents, String src) throws IOException {
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
	 * @return <code>true</code> if the source contains errors
	 */
	public boolean add(String contents, String src) {
		validated = false;
		srcs.add(src);
		final Set<Problem> problems = Sets.newHashSet();
		issues.remove(src);
		final HDLPackage pkg = PSHDLParser.parseString(contents, uri, problems, src);
		if (pkg != null) {
			pkgs.put(src, pkg);
		}
		issues.put(src, problems);
		return hasError(problems);
	}

	public boolean addFiles(Collection<File> files) throws Exception {
		if (service != null)
			return addFilesMultiThreaded(files);
		boolean syntaxError = false;
		for (final File file : files) {
			if (singleAdd(file)) {
				syntaxError = true;
			}
		}
		return syntaxError;
	}

	protected boolean addFilesMultiThreaded(Collection<File> files) throws Exception {
		final List<Future<Void>> futures = Lists.newArrayListWithCapacity(files.size());
		for (final File file : files) {
			final Future<Void> future = service.submit(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					singleAdd(file);
					return null;
				}
			});
			futures.add(future);
		}
		for (final Future<Void> future : futures) {
			future.get();
		}
		for (final File file : files) {
			final Set<Problem> syntaxProblems = getProblems(file);
			for (final Problem problem : syntaxProblems) {
				if (problem.isSyntax && (problem.severity == ProblemSeverity.ERROR))
					return true;
			}
		}
		return false;
	}

	public void close() {
		HDLLibrary.unregister(uri);
	}

	protected static ExecutorService createExecutor() {
		return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	public void resetErrors() {
		validated = false;
	}

	public List<CompileResult> compile(ICompilationListener listener) throws Exception {
		if (!validated) {
			validatePackages();
		}
		if (listener == null) {
			listener = new NullListener();
		}
		final List<CompileResult> res = Lists.newArrayListWithCapacity(pkgs.size());
		synchronized (srcs) {
			for (final String src : srcs) {
				final Collection<Problem> issue = issues.get(src);
				if (listener.useSource(src, issue, hasError(issue))) {
					final HDLPackage parse = pkgs.get(src);
					if (listener.startModule(src, parse)) {
						res.add(doCompile(src, parse));
					} else {
						res.add(createResult(src, null, null, false));
					}
				} else {
					res.add(createResult(src, null, null, false));
				}
			}
		}
		return res;
	}

	protected CompileResult createResult(final String src, final String code, String codeType, boolean unitName) {
		final Set<Problem> syntaxProblems = new HashSet<Problem>(issues.get(src));
		String name = "<ERROR>";
		final HDLPackage hdlPackage = pkgs.get(src);
		if (hdlPackage != null) {
			final HDLUnit[] units = hdlPackage.getAllObjectsOf(HDLUnit.class, false);
			name = "<emptyFile>";
			if (units.length != 0) {
				name = units[0].getName();
			}
		}
		final CompileResult cr = new CompileResult(syntaxProblems, code, name, lib.sideFiles.values(), src, codeType, unitName);
		lib.sideFiles.clear();
		return cr;
	}

	protected CompileResult doCompile(final String src, final HDLPackage parse) {
		throw new RuntimeException("Not implemented");
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

	public String getSourceName(final File file) {
		return file.getAbsolutePath();
	}

	/**
	 * Removes all resources related to this src. This can be important for
	 * incremental compilation.
	 * 
	 * @param src
	 *            the src name that was used to register a added input
	 */
	public void remove(String src) {
		validated = false;
		srcs.remove(src);
		lib.removeAllSrc(src);
		pkgs.remove(src);
		issues.remove(src);
	}

	protected boolean printErrors() {
		boolean hasError = false;
		for (final Entry<String, Set<Problem>> e : issues.entrySet()) {
			final Set<Problem> value = e.getValue();
			if (!value.isEmpty()) {
				System.out.println("File:" + e.getKey());
			}
			for (final Problem p : value) {
				if (p.severity == ProblemSeverity.ERROR) {
					hasError = true;
				}
				final HDLAdvise advise = HDLValidator.advise(p);
				System.out.println("\t" + p.severity + " at line: " + p.line + ":" + p.offsetInLine);
				if (advise != null) {
					System.out.println("\t\t" + advise.message);
					System.out.println("\t\t" + advise.explanation);
					for (final String help : advise.solutions) {
						System.out.println("\t\t\t" + help);
					}
				} else {
					if (p.info != null) {
						System.out.println("\t\t" + p.info);
					}
					if (p.context != null) {
						System.out.println("\t\t Object:" + p.context);
					}
				}
			}
		}
		return hasError;
	}

	public boolean hasError(Collection<Problem> syntaxProblems) {
		final boolean error = false;
		if (syntaxProblems.size() != 0) {
			for (final Problem problem : syntaxProblems) {
				if (problem.severity == ProblemSeverity.ERROR)
					return true;
			}
		}
		return error;
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
			return null;
		}
		final HDLPackage pkg = pkgs.values().iterator().next();
		for (final HDLUnit unit : pkg.getUnits()) {
			if (!unit.getSimulation())
				return unit;
		}
		return null;
	}

	public boolean validateFile(HDLPackage parse, Set<Problem> problems) throws Exception {
		final Set<Problem> validate = HDLValidator.validate(parse, null);
		problems.addAll(validate);
		return hasError(validate);
	}

	/**
	 * 
	 * @return <code>true</code> when at least one error has been found
	 * @throws Exception
	 */
	public boolean validatePackages() throws Exception {
		validated = true;
		boolean validationError = false;
		if (service != null)
			return validatePackagesMultiThreaded();
		for (final Entry<String, HDLPackage> e : pkgs.entrySet()) {
			if (singleValidate(e)) {
				validationError = true;
			}
		}
		return validationError;
	}

	private boolean singleValidate(final Entry<String, HDLPackage> e) throws Exception {
		final Set<Problem> localProblems = Sets.newHashSet();
		final boolean error = validateFile(e.getValue(), localProblems);
		final String src = e.getKey();
		final Set<Problem> set = issues.get(src);
		if (set != null) {
			final Iterator<Problem> iterator = set.iterator();
			while (iterator.hasNext()) {
				final Problem problem = iterator.next();
				if (!problem.isSyntax) {
					iterator.remove();
				}
			}
			set.addAll(localProblems);
		} else {
			issues.put(src, localProblems);
		}
		return error;
	}

	protected boolean validatePackagesMultiThreaded() throws Exception {
		final List<Future<Void>> futures = Lists.newArrayListWithCapacity(pkgs.size());
		for (final Entry<String, HDLPackage> e : pkgs.entrySet()) {
			futures.add(service.submit(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					singleValidate(e);
					return null;
				}

			}));
		}
		for (final Future<Void> future : futures) {
			future.get();
		}
		for (final Set<Problem> p : issues.values()) {
			for (final Problem problem : p) {
				if (problem.severity == ProblemSeverity.ERROR)
					return true;
			}
		}
		return false;
	}

	private boolean singleAdd(final File file) throws Exception {
		return add(new FileInputStream(file), getSourceName(file));
	}

	public Set<Problem> getProblems(File file) {
		return issues.get(getSourceName(file));
	}

	public Map<String, Set<Problem>> getAllProblems() {
		return issues;
	}

	public Collection<HDLUnit> getUnits() {
		return lib.units.values();
	}

	public Map<String, HDLPackage> getFileUnits() {
		return pkgs;
	}
}
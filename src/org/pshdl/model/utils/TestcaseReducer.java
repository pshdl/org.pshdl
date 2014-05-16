package org.pshdl.model.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLCompound;
import org.pshdl.model.HDLDeclaration;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumDeclaration;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLInterfaceDeclaration;
import org.pshdl.model.HDLInterfaceInstantiation;
import org.pshdl.model.HDLInterfaceRef;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLObject.GenericMeta;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.extensions.FullNameExtension;
import org.pshdl.model.simulation.PStoEXCompiler;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class TestcaseReducer {

	public static interface Validator {
		/**
		 *
		 * @param pkg
		 * @return <code>true</code> when the code exhibits the unwanted
		 *         behaviour
		 */
		boolean inspect(HDLPackage pkg);
	}

	public static class CrashValidator implements Validator {
		public static interface CrashValidatorRunnable {
			public void run(String src, HDLPackage pkg) throws Throwable;
		}

		public static class SimulationCrasher implements CrashValidatorRunnable {

			private final HDLQualifiedName moduleName;

			public SimulationCrasher(String moduleName) {
				this.moduleName = new HDLQualifiedName(moduleName);
			}

			@Override
			public void run(String src, HDLPackage pkg) throws Throwable {
				Insulin.resetID();
				final HDLUnit unit = HDLQuery.select(HDLUnit.class).from(pkg).whereObj().fullNameIs(moduleName).getFirst();
				PStoEXCompiler.createExecutable(unit, src, false);
			}

		}

		public static class InsulinCrasher implements CrashValidatorRunnable {

			@Override
			public void run(String src, HDLPackage pkg) throws Throwable {
				Insulin.resetID();
				Insulin.transform(pkg, src);
			}

		}

		private String expectedStacktrace;
		private final CrashValidatorRunnable runnable;
		private final String src;

		public CrashValidator(String src, HDLPackage pkg, CrashValidatorRunnable runnable) {
			boolean crashed = true;
			this.runnable = runnable;
			this.src = src;
			try {
				runnable.run(src, pkg);
				crashed = false;
			} catch (final Throwable e) {
				expectedStacktrace = filter(Throwables.getStackTraceAsString(e));
				System.out.println("Looking for " + expectedStacktrace);
			}
			if (!crashed)
				throw new IllegalArgumentException("The runnable did not crash");
		}

		@Override
		public boolean inspect(HDLPackage pkg) {
			try {
				runnable.run(src, pkg);
			} catch (final Throwable e) {
				final String traceAsString = filter(Throwables.getStackTraceAsString(e));
				return traceAsString.equals(expectedStacktrace);
			}
			return false;
		}

		private String filter(String stackTraceAsString) {
			final Iterable<String> split = Splitter.on('\n').split(stackTraceAsString);
			final StringBuffer sb = new StringBuffer();
			final Iterator<String> iter = split.iterator();
			final String firstLine = iter.next();
			sb.append(Splitter.on(':').split(firstLine).iterator().next()).append('\n');
			while (iter.hasNext()) {
				final String string = iter.next();
				if (string.startsWith("\tat " + CrashValidator.class.getName()))
					return sb.toString();
				sb.append(string).append('\n');
			}
			return sb.toString();
		}
	}

	public static interface Reducer {
		/**
		 * Attempt to reduce the provided pkg
		 *
		 * @param pkg
		 * @return <code>null</code> when no reduction was possible
		 */
		public HDLPackage reduce(HDLPackage pkg);
	}

	public static class VariableRemover implements Reducer {

		private static GenericMeta<Boolean> ATTEMPTEDREMOVE = new GenericMeta<>("ATTEMPTED_REMOVE_VARIABLE", true);

		@Override
		public HDLPackage reduce(HDLPackage pkg) {
			final HDLVariable[] variables = pkg.getAllObjectsOf(HDLVariable.class, true);
			for (final HDLVariable hdlVariable : variables) {
				if (!hdlVariable.hasMeta(ATTEMPTEDREMOVE)) {
					// System.out.println("TestcaseReducer.VariableRemover.reduce()"
					// + hdlVariable.getName());
					hdlVariable.setMeta(ATTEMPTEDREMOVE);
					final HDLUnit unit = hdlVariable.getContainer(HDLUnit.class);
					if (unit != null) {
						final HDLUnit removed = Refactoring.removeVariable(unit, hdlVariable, Refactoring.SUBSTITUTE_RESOLVER);
						final ModificationSet ms = new ModificationSet();
						final HDLDirection direction = hdlVariable.getDirection();
						if (direction != null) {
							switch (direction) {
							case IN:
							case INOUT:
							case OUT:
								findAndRemoveReferences(pkg, hdlVariable, unit, ms);
								break;
							case PARAMETER:
							default:
							}
						}
						ms.replace(unit, removed);
						return ms.apply(pkg);
					}
				}
			}
			return null;
		}

		private void findAndRemoveReferences(HDLPackage pkg, final HDLVariable hdlVariable, final HDLUnit unit, final ModificationSet ms) {
			final HDLQualifiedName fqn = FullNameExtension.fullNameOf(unit);
			final HDLInterfaceInstantiation[] allII = pkg.getAllObjectsOf(HDLInterfaceInstantiation.class, true);
			for (final HDLInterfaceInstantiation hdi : allII) {
				final Optional<HDLInterface> resolveHIf = hdi.resolveHIf();
				if (resolveHIf.isPresent()) {
					final HDLQualifiedName resolved = resolveHIf.get().asRef();
					if (resolved.equals(fqn)) {
						final Collection<HDLInterfaceRef> iRefs = HDLQuery.getInterfaceRefs(pkg, hdi.getVar());
						for (final HDLInterfaceRef hiRef : iRefs) {
							final Optional<HDLVariable> resolveVar = hiRef.resolveVar();
							if (resolveVar.isPresent()) {
								final HDLVariable hiVar = resolveVar.get();
								if (hiVar.getName().equals(hdlVariable.getName())) {
									if (hiRef.getContainer() instanceof HDLAssignment) {
										ms.remove(hiRef.getContainer());
									} else {
										Refactoring.SubstituteExpressionResolver.replaceWithZeroOrConstant(hiRef, ms);
									}
								}
							}
						}
					}
				}
			}
		}

	}

	public static class StatementRemover implements Reducer {

		private static GenericMeta<Boolean> ATTEMPTEDREMOVE = new GenericMeta<>("ATTEMPTED_REMOVE_STMNT", false);

		@Override
		public HDLPackage reduce(HDLPackage pkg) {
			final HDLStatement[] stmnts = pkg.getAllObjectsOf(HDLStatement.class, true);
			for (final HDLStatement statement : stmnts) {
				if (!statement.hasMeta(ATTEMPTEDREMOVE)) {
					final HDLFieldAccess<?, ?> feature = statement.getContainingFeature();
					if ((feature != null) && !HDLStatement.class.isAssignableFrom(feature.type)) {
						continue;
					}
					statement.setMeta(ATTEMPTEDREMOVE);
					final ModificationSet ms = new ModificationSet();
					ms.remove(statement);
					return ms.apply(pkg);
				}
			}
			return null;
		}

	}

	public static class EmptyCompoundRemover implements Reducer {

		private static GenericMeta<Boolean> ATTEMPTEDREMOVE = new GenericMeta<>("ATTEMPTED_REMOVE_COMPOUND", false);

		@Override
		public HDLPackage reduce(HDLPackage pkg) {
			// final HDLUnit[] units = pkg.getAllObjectsOf(HDLUnit.class, true);
			// for (final HDLUnit unit : units) {
			// if (unit.hasMeta(ATTEMPTEDREMOVE)) {
			// continue;
			// }
			// if (unit.getInits().isEmpty() && unit.getStatements().isEmpty())
			// return remove(unit, pkg);
			// }
			final HDLCompound[] compounds = pkg.getAllObjectsOf(HDLCompound.class, true);
			for (final HDLCompound hdlCompound : compounds) {
				if (hdlCompound.hasMeta(ATTEMPTEDREMOVE)) {
					continue;
				}
				switch (hdlCompound.getClassType()) {
				case HDLIfStatement:
					final HDLIfStatement hif = (HDLIfStatement) hdlCompound;
					if (hif.getThenDo().isEmpty() && hif.getElseDo().isEmpty())
						return remove(hif, pkg);
					break;
				case HDLForLoop:
					final HDLForLoop forLoop = (HDLForLoop) hdlCompound;
					if (forLoop.getDos().isEmpty())
						return remove(forLoop, pkg);
					break;
				case HDLSwitchCaseStatement:
					final HDLSwitchCaseStatement caseStatement = (HDLSwitchCaseStatement) hdlCompound;
					if ((caseStatement.getLabel() != null) && caseStatement.getDos().isEmpty())
						return remove(caseStatement, pkg);
					break;
				case HDLSwitchStatement:
					final HDLSwitchStatement switchStatement = (HDLSwitchStatement) hdlCompound;
					final ArrayList<HDLSwitchCaseStatement> cases = switchStatement.getCases();
					if (cases.size() == 1) {
						final HDLSwitchCaseStatement defaultCase = cases.get(0);
						if (defaultCase.getDos().isEmpty())
							return remove(switchStatement, pkg);
					}
					break;
				default:
				}
			}
			return null;
		}

		private HDLPackage remove(IHDLObject hif, HDLPackage pkg) {
			hif.setMeta(ATTEMPTEDREMOVE);
			final ModificationSet ms = new ModificationSet();
			ms.remove(hif);
			return ms.apply(pkg);
		}

	}

	public static Reducer[] REDUCERS = new Reducer[] { new VariableRemover(), new EmptyCompoundRemover(), new StatementRemover() };

	public HDLPackage reduce(HDLPackage pkg, Validator validator, String src) {
		return reduce(pkg, src, validator, REDUCERS);
	}

	private static int libURICounter = 0;

	private HDLPackage reduce(HDLPackage pkg, String src, Validator validator, Reducer[] reductions) {
		final boolean inspect = validator.inspect(pkg);
		if (!inspect)
			throw new IllegalArgumentException("The input does not exhibit the unwanted effect");
		final HDLPackage asPackage = importInterfaces(pkg);
		pkg.getLibrary().addPkg(asPackage, src);
		if (validator.inspect(asPackage)) {
			pkg = asPackage;
		} else {
			System.out.println("Failed to use full library replacement");
		}
		System.out.println(pkg);
		boolean reduction = false;
		int reductionCount = 0;
		final List<String> regLibs = Lists.newArrayList();
		do {
			reduction = false;
			for (final Reducer reducer : reductions) {
				HDLPackage reduced = null;
				do {
					reduced = reducer.reduce(pkg);
					if (reduced != null) {
						try {
							reduced.validateAllFields(reduced.getContainer(), true);
							final HDLLibrary lib = new HDLLibrary();
							final String libURI = "reduceLib" + libURICounter++;
							HDLLibrary.registerLibrary(libURI, lib);
							regLibs.add(libURI);
							final HDLPackage setLibURI = reduced.setLibURI(libURI).copyDeepFrozen(null);
							lib.addPkg(setLibURI, src);
							if (validator.inspect(reduced)) {
								reduction = true;
								reductionCount++;
								pkg = reduced;
							}
						} catch (final Exception e) {
						}
					}
				} while (reduced != null);
			}
		} while (reduction);
		for (final String string : regLibs) {
			if (!string.equals(pkg.getLibURI())) {
				HDLLibrary.unregister(string);
			}
		}
		System.out.println("Applied " + reductionCount + " reductions");
		return pkg;
	}

	private HDLPackage importInterfaces(HDLPackage imp) {
		HDLPackage pkg = imp;
		final HDLDeclaration[] decls = pkg.getAllObjectsOf(HDLDeclaration.class, true);
		final Set<HDLQualifiedName> declared = Sets.newHashSet();
		for (final HDLDeclaration decl : decls) {
			HDLQualifiedName fqn = null;
			switch (decl.getClassType()) {
			case HDLFunction:
				fqn = FullNameExtension.fullNameOf(decl);
				break;
			case HDLEnumDeclaration:
				fqn = FullNameExtension.fullNameOf(((HDLEnumDeclaration) decl).getHEnum());
				break;
			case HDLInterfaceDeclaration:
				fqn = FullNameExtension.fullNameOf(((HDLInterfaceDeclaration) decl).getHIf());
				break;
			default:
			}
			if (fqn != null) {
				declared.add(fqn);
			}
		}
		for (final HDLUnit unit : pkg.getUnits()) {
			final HDLQualifiedName fqn = FullNameExtension.fullNameOf(unit);
			declared.add(fqn);
		}
		final HDLInterfaceInstantiation[] hii = pkg.getAllObjectsOf(HDLInterfaceInstantiation.class, true);
		for (final HDLInterfaceInstantiation hi : hii) {
			final Optional<HDLInterface> resolveHIf = hi.resolveHIf();
			if (resolveHIf.isPresent()) {
				final HDLInterface hif = resolveHIf.get();
				final HDLQualifiedName fqn = FullNameExtension.fullNameOf(hif);
				if (!declared.contains(fqn)) {
					pkg = pkg.addDeclarations(new HDLInterfaceDeclaration().setHIf(hif));
					declared.add(fqn);
				}
			}
		}
		final HDLEnumRef[] her = pkg.getAllObjectsOf(HDLEnumRef.class, true);
		for (final HDLEnumRef henumRef : her) {
			final Optional<HDLEnum> resolveHEnum = henumRef.resolveHEnum();
			if (resolveHEnum.isPresent()) {
				final HDLEnum hdlEnum = resolveHEnum.get();
				final HDLQualifiedName fqn = FullNameExtension.fullNameOf(hdlEnum);
				if (!declared.contains(fqn)) {
					pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(hdlEnum.setName(fqn.toString())));
					declared.add(fqn);
				}
			}
		}
		final HDLVariableDeclaration[] hvds = pkg.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (final HDLVariableDeclaration hvd : hvds) {
			final Optional<? extends HDLType> resolveType = hvd.resolveType();
			if (resolveType.isPresent()) {
				final HDLType type = resolveType.get();
				if (type.getClassType() == HDLClass.HDLEnum) {
					final HDLEnum hdlEnum = (HDLEnum) type;
					final HDLQualifiedName fqn = FullNameExtension.fullNameOf(hdlEnum);
					if (!declared.contains(fqn)) {
						pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(hdlEnum.setName(fqn.toString())));
						declared.add(fqn);
					}
				}
			}
		}
		final HDLVariableRef[] refs = pkg.getAllObjectsOf(HDLVariableRef.class, true);
		for (final HDLVariableRef varRef : refs) {
			final Optional<HDLVariable> resolveVar = varRef.resolveVar();
			if (resolveVar.isPresent()) {
				final HDLVariable variable = resolveVar.get();
				if (variable.getContainer(HDLPackage.class) != imp) {
					final HDLVariableDeclaration hvd = variable.getContainer(HDLVariableDeclaration.class);
					if (hvd != null) {
						pkg = pkg.addDeclarations(hvd.setVariables(HDLObject.asList(variable)));
					}
				}
			}
		}
		return pkg.copyDeepFrozen(null);
	}

}

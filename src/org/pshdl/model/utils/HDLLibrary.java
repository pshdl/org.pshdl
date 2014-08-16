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

import static org.pshdl.model.extensions.FullNameExtension.fullNameOf;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.pshdl.model.HDLDeclaration;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumDeclaration;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLInterfaceDeclaration;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLObject.GenericMeta;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.types.builtIn.PSHDLLib;
import org.pshdl.model.utils.services.AuxiliaryContent;

import com.google.common.base.Optional;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public class HDLLibrary {
	private static class Record {
		public static enum RecordType {
			function, type, unit, variable
		};

		public final HDLQualifiedName ref;
		public final String src;
		public final RecordType type;

		public Record(String src, RecordType type, HDLQualifiedName ref) {
			super();
			this.src = src;
			this.type = type;
			this.ref = ref;
		}
	}

	private static Map<String, HDLLibrary> libs = Maps.newLinkedHashMap();

	public static HDLLibrary getLibrary(String libURI) {
		if (libURI == null)
			throw new IllegalArgumentException("Library uri can not be null");
		final HDLLibrary hdlLibrary = libs.get(libURI);
		return hdlLibrary;
	}

	public static void registerLibrary(String libURI, HDLLibrary library) {
		if (libURI == null)
			throw new IllegalArgumentException("Library uri can not be null");
		final HDLLibrary put = libs.put(libURI, library);
		if ((put != null) && (put != library))
			throw new IllegalArgumentException("A library with this id already exists!");
	}

	public static void unregister(String libURI) {
		if (libURI == null)
			throw new IllegalArgumentException("Library uri can not be null");
		libs.remove(libURI);
	}

	public final Map<HDLQualifiedName, HDLFunction> functions = new ConcurrentHashMap<HDLQualifiedName, HDLFunction>();
	public final Multimap<String, Record> objects = LinkedListMultimap.create();

	public final Multimap<String, AuxiliaryContent> sideFiles = LinkedListMultimap.create();

	public final Map<HDLQualifiedName, HDLType> types = new ConcurrentHashMap<HDLQualifiedName, HDLType>();

	public final Map<HDLQualifiedName, HDLUnit> units = new ConcurrentHashMap<HDLQualifiedName, HDLUnit>();

	public final Map<HDLQualifiedName, HDLVariable> variables = new ConcurrentHashMap<HDLQualifiedName, HDLVariable>();

	public HDLLibrary() {
		addPkg(PSHDLLib.getLib(), "#PSHDLLib");
	}

	/**
	 * Adds the given enum to the library so that it can be resolved by
	 * {@link #resolve(Iterable, HDLQualifiedName)}
	 *
	 * @param hEnum
	 */
	public void addEnum(HDLEnum hEnum, String src) {
		checkFrozen(hEnum);
		final HDLQualifiedName fqn = fullNameOf(hEnum);
		types.put(fqn, hEnum);
		addRecord(src, new Record(src, Record.RecordType.type, fqn));
	}

	private void addRecord(String src, Record record) {
		final Collection<Record> collection = objects.get(src);
		for (final Iterator<Record> iter = collection.iterator(); iter.hasNext();) {
			final Record rec = iter.next();
			if (rec.type == record.type) {
				iter.remove();
			}
		}
		objects.put(src, record);
	}

	/**
	 * Adds the given function to the library so that it can be resolved by
	 * {@link #resolveFunction(Iterable, HDLQualifiedName)}
	 *
	 * @param func
	 */
	public void addFunction(HDLFunction func, String src) {
		checkFrozen(func);
		final HDLQualifiedName fqn = fullNameOf(func);
		functions.put(fqn, func);
		addRecord(src, new Record(src, Record.RecordType.function, fqn));
	}

	/**
	 * Adds the given interface to the library so that it can be resolved by
	 * {@link #resolve(Iterable, HDLQualifiedName)}
	 *
	 * @param hIf
	 */
	public void addInterface(HDLInterface hIf, String src) {
		checkFrozen(hIf);
		final HDLQualifiedName fqn = fullNameOf(hIf);
		types.put(fqn, hIf);
		addRecord(src, new Record(src, Record.RecordType.type, fqn));
	}

	/**
	 * Adds a package to this library. This includes all units and declarations
	 *
	 * @param pkg
	 *            the package to be added
	 * @param src
	 *            the source from which this package was derived
	 */
	public void addPkg(HDLPackage pkg, String src) {
		checkFrozen(pkg);
		for (final HDLUnit unit : pkg.getUnits()) {
			final HDLQualifiedName uq = fullNameOf(unit);
			addRecord(src, new Record(src, Record.RecordType.unit, uq));
			units.put(uq, unit);
			addInterface(unit.asInterface(), uq.toString());
			final HDLInterface[] list = unit.getAllObjectsOf(HDLInterface.class, true);
			for (final HDLInterface hdlInterface : list) {
				addInterface(hdlInterface, src);
			}
			final HDLEnum[] elist = unit.getAllObjectsOf(HDLEnum.class, true);
			for (final HDLEnum hdlEnum : elist) {
				addEnum(hdlEnum, src);
			}
			final HDLFunction[] functions = unit.getAllObjectsOf(HDLFunction.class, true);
			for (final HDLFunction hdlFunction : functions) {
				addFunction(hdlFunction, src);
			}
		}
		for (final HDLDeclaration decl : pkg.getDeclarations()) {
			switch (decl.getClassType()) {
			case HDLEnumDeclaration:
				final HDLEnumDeclaration ed = (HDLEnumDeclaration) decl;
				addEnum(ed.getHEnum(), src);
				break;
			case HDLInterfaceDeclaration:
				final HDLInterfaceDeclaration hid = (HDLInterfaceDeclaration) decl;
				addInterface(hid.getHIf(), src);
				break;
			case HDLVariableDeclaration:
				final HDLVariableDeclaration hvd = (HDLVariableDeclaration) decl;
				for (final HDLVariable var : hvd.getVariables()) {
					addVariable(var, src);
				}
				break;
			default:
				if (decl instanceof HDLFunction) {
					final HDLFunction func = (HDLFunction) decl;
					addFunction(func, src);
				} else
					throw new IllegalArgumentException("Did not handle:" + decl);
			}
		}
	}

	/**
	 * Add generated files to this library so that it can be retrieved later
	 *
	 * @param files
	 * @param src
	 */
	public void addSideFiles(List<AuxiliaryContent> files, String src) {
		sideFiles.putAll(src, files);
	}

	/**
	 * Adds the given variable to the library so that it can be resolved by
	 * {@link #resolveVariable(Iterable, HDLQualifiedName)}
	 *
	 * @param var
	 */
	public void addVariable(HDLVariable var, String src) {
		checkFrozen(var);
		final HDLQualifiedName fqn = fullNameOf(var);
		variables.put(fqn, var);
		addRecord(src, new Record(src, Record.RecordType.variable, fqn));
	}

	private void checkFrozen(IHDLObject hObject) {
		if (!hObject.isFrozen())
			throw new IllegalArgumentException("Objects need to be frozen to be added");
	}

	private <T extends IHDLObject> Optional<T> checkGenericImport(HDLQualifiedName type, String string, Map<HDLQualifiedName, T> map) {
		final HDLQualifiedName newTypeName = new HDLQualifiedName(string).skipLast(1).append(type);
		final T newType = map.get(newTypeName);
		if (newType != null)
			return Optional.of(Insulin.resolveFragments(newType));
		return Optional.absent();
	}

	/**
	 * Removes all types, variables, functions etc. which were derived from the
	 * given src. This is useful for incremental compilation.
	 *
	 * @param src
	 */
	public void removeAllSrc(String src) {
		final Collection<Record> collection = objects.get(src);
		for (final Record record : collection) {
			switch (record.type) {
			case function:
				functions.remove(record.ref);
				break;
			case type:
				types.remove(record.ref);
				break;
			case unit:
				units.remove(record.ref);
				types.remove(record.ref);
				break;
			case variable:
				variables.remove(record.ref);
				break;
			}
		}
		objects.removeAll(src);
	}

	public static ThreadLocal<Boolean> resolveFragments = new ThreadLocal<>();

	/**
	 * Resolves a type by firstly checking if it already exists given the
	 * qualified name. If not the specific imports are tried first, then the
	 * wild card ones in order of declaration.
	 *
	 * @param imports
	 *            a list of specific and wild card imports
	 * @param type
	 *            the fqn or local name of the type to look for
	 * @return the type if found
	 */
	public Optional<? extends HDLType> resolve(Iterable<String> imports, HDLQualifiedName type) {
		HDLType hdlType = types.get(type);
		if (hdlType == null) {
			for (final String string : imports)
				if (string.endsWith(type.toString())) {
					hdlType = types.get(new HDLQualifiedName(string));
					break;
				}
		}
		if (hdlType == null) {
			final Optional<HDLType> genericImport = checkGenericImport(type, "pshdl.*", types);
			if (genericImport.isPresent()) {
				hdlType = genericImport.get();
			}
		}
		if (hdlType == null) {
			for (final String string : imports)
				if (string.endsWith(".*")) {
					final Optional<HDLType> genericImport = checkGenericImport(type, string, types);
					if (genericImport.isPresent()) {
						hdlType = genericImport.get();
						break;
					}
				}
		}
		if (hdlType != null) {
			if ((resolveFragments.get() == null) || resolveFragments.get())
				return Optional.fromNullable(Insulin.resolveFragments(hdlType));
			return Optional.of(hdlType);
		}
		return Optional.absent();
	}

	/**
	 * Resolves a type by firstly checking if it already exists given the
	 * qualified name. If not the specific imports are tried first, then the
	 * wild card ones in order of declaration.
	 *
	 * @param imports
	 *            a list of specific and wild card imports
	 * @param type
	 *            the fqn or local name of the type to look for
	 * @return the type if found
	 */
	public Optional<HDLFunction> resolveFunction(Iterable<String> imports, HDLQualifiedName type) {
		HDLFunction hdlFunction = functions.get(type);
		if (hdlFunction == null) {
			// System.out.println("HDLLibrary.resolve() Checking imports for:" +
			// type + " @" + this);
			for (final String string : imports)
				if (string.endsWith(type.toString())) {
					hdlFunction = functions.get(new HDLQualifiedName(string));
					if (hdlFunction != null)
						return Optional.fromNullable(Insulin.resolveFragments(hdlFunction));
				}
			Optional<HDLFunction> genericImport = checkGenericImport(type, "pshdl.*", functions);
			if (genericImport.isPresent())
				return genericImport;
			for (final String string : imports)
				if (string.endsWith(".*")) {
					genericImport = checkGenericImport(type, string, functions);
					if (genericImport.isPresent())
						return genericImport;
				}
		}
		if (hdlFunction != null)
			return Optional.fromNullable(Insulin.resolveFragments(hdlFunction));
		return Optional.absent();
	}

	/**
	 * Resolves a type by firstly checking if it already exists given the
	 * qualified name. If not the specific imports are tried first, then the
	 * wild card ones in order of declaration.
	 *
	 * @param imports
	 *            a list of specific and wild card imports
	 * @param type
	 *            the fqn or local name of the type to look for
	 * @return the type if found
	 */
	public Optional<HDLVariable> resolveVariable(Iterable<String> imports, HDLQualifiedName type) {
		HDLVariable hdlVariable = variables.get(type);
		if (hdlVariable == null) {
			for (final String string : imports)
				if (string.endsWith(type.toString())) {
					hdlVariable = variables.get(new HDLQualifiedName(string));
					if (hdlVariable != null)
						return Optional.fromNullable(Insulin.resolveFragments(hdlVariable));
				}
			Optional<HDLVariable> genericImport = checkGenericImport(type, "pshdl.*", variables);
			if (genericImport.isPresent())
				return genericImport;
			for (final String string : imports)
				if (string.endsWith(".*")) {
					genericImport = checkGenericImport(type, string, variables);
					if (genericImport.isPresent())
						return genericImport;
				}
		}
		if (hdlVariable != null)
			return Optional.fromNullable(Insulin.resolveFragments(hdlVariable));
		return Optional.absent();
	}

	public void unregister() {
		final Iterator<Entry<String, HDLLibrary>> iter = libs.entrySet().iterator();
		while (iter.hasNext()) {
			final Entry<String, HDLLibrary> e = iter.next();
			if (e.getValue() == this) {
				iter.remove();
			}
		}
	}

	public Map<MetaAccess<?>, Object> metaData = Maps.newLinkedHashMap();

	public void addMeta(String key, Object value) {
		metaData.put(new GenericMeta<Object>(key, true), value);
	}

	public Object getMeta(String key) {
		return metaData.get(new GenericMeta<Object>(key, true));
	}

	public <K> void addMeta(MetaAccess<K> key, K value) {
		metaData.put(key, value);
	}

	public void setMeta(MetaAccess<Boolean> meta) {
		addMeta(meta, true);
	}

	public boolean hasMeta(MetaAccess<?> key) {
		return getMeta(key) != null;
	}

	@SuppressWarnings("unchecked")
	public <K> K getMeta(MetaAccess<K> key) {
		return (K) metaData.get(key);
	}

	public HDLUnit getUnit(HDLQualifiedName asRef) {
		return units.get(asRef);
	}

	public String getSrc(HDLQualifiedName asRef) {
		for (final Record r : objects.values()) {
			if (r.ref.equals(asRef))
				return r.src;
		}
		return null;
	}

	public HDLPackage getAsPackage(String libURI) {
		HDLPackage pkg = new HDLPackage().setLibURI(libURI);
		for (final Entry<HDLQualifiedName, HDLVariable> e : variables.entrySet()) {
			if (e.getKey().getSegment(0).equals("pshdl")) {
				continue;
			}
			final HDLVariable vars = e.getValue();
			final HDLVariableDeclaration hvd = vars.getContainer(HDLVariableDeclaration.class);
			if (hvd == null)
				throw new IllegalArgumentException("The variable: " + vars.getName() + " does not have a declaration");
			pkg = pkg.addDeclarations(hvd.setVariables(HDLObject.asList(vars.setName(e.getKey().toString()))));
		}
		for (final Entry<HDLQualifiedName, HDLFunction> e : functions.entrySet()) {
			if (e.getKey().getSegment(0).equals("pshdl")) {
				continue;
			}
			final HDLFunction function = e.getValue();
			pkg = pkg.addDeclarations(function);
		}
		for (final Entry<HDLQualifiedName, HDLType> e : types.entrySet()) {
			if (e.getKey().getSegment(0).equals("pshdl")) {
				continue;
			}
			final HDLType type = e.getValue();
			if (type.getContainer(HDLUnit.class) != null) {
				continue;
			}
			switch (type.getClassType()) {
			case HDLInterface:
				if (!units.containsKey(e.getKey())) {
					pkg = pkg.addDeclarations(new HDLInterfaceDeclaration().setHIf((HDLInterface) type.setName(e.getKey().toString())));
				}
				break;
			case HDLEnum:
				pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum((HDLEnum) type.setName(e.getKey().toString())));
				break;
			default:
				throw new IllegalArgumentException("Did not expect type:" + type);
			}
		}
		for (final Entry<HDLQualifiedName, HDLUnit> e : units.entrySet()) {
			if (e.getKey().getSegment(0).equals("pshdl")) {
				continue;
			}
			final HDLUnit unit = e.getValue();
			pkg = pkg.addUnits(unit.setName(e.getKey().toString()));
		}
		return pkg.copyDeepFrozen(null);
	}

}

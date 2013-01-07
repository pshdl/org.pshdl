package de.tuhh.ict.pshdl.model.utils;

import java.util.*;
import java.util.concurrent.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.services.IHDLGenerator.SideFile;

public class HDLLibrary {
	public Map<HDLQualifiedName, HDLUnit> units = new ConcurrentHashMap<HDLQualifiedName, HDLUnit>();
	public Map<HDLQualifiedName, HDLType> types = new ConcurrentHashMap<HDLQualifiedName, HDLType>();
	public Map<HDLQualifiedName, HDLFunction> functions = new ConcurrentHashMap<HDLQualifiedName, HDLFunction>();
	public Map<HDLQualifiedName, HDLVariable> variables = new ConcurrentHashMap<HDLQualifiedName, HDLVariable>();
	public List<SideFile> sideFiles = new LinkedList<SideFile>();
	private HDLConfig config = new HDLConfig();

	private static Map<String, HDLLibrary> libs = new HashMap<String, HDLLibrary>();

	public HDLLibrary() {
		addPkg(PSHDLLib.getLib());
	}

	public static void registerLibrary(String uri, HDLLibrary library) {
		libs.put(uri, library);
	}

	public static HDLLibrary getLibrary(String uri) {
		return libs.get(uri);
	}

	public void addPkg(HDLPackage pkg) {
		checkFrozen(pkg);
		for (HDLUnit unit : pkg.getUnits()) {
			HDLQualifiedName uq = unit.getFullName();
			units.put(uq, unit);
			types.put(uq, unit.asInterface());
			Collection<HDLInterface> list = unit.getAllObjectsOf(HDLInterface.class, true);
			for (HDLInterface hdlInterface : list) {
				addInterface(hdlInterface);
			}
			Collection<HDLEnum> elist = unit.getAllObjectsOf(HDLEnum.class, true);
			for (HDLEnum hdlEnum : elist) {
				addEnum(hdlEnum);
			}
			Set<HDLFunction> functions = unit.getAllObjectsOf(HDLFunction.class, true);
			for (HDLFunction hdlFunction : functions) {
				addFunction(hdlFunction);
			}
		}
		for (HDLDeclaration decl : pkg.getDeclarations()) {
			switch (decl.getClassType()) {
			case HDLEnumDeclaration:
				HDLEnumDeclaration ed = (HDLEnumDeclaration) decl;
				addEnum(ed.getHEnum());
				break;
			case HDLInterfaceDeclaration:
				HDLInterfaceDeclaration hid = (HDLInterfaceDeclaration) decl;
				addInterface(hid.getHIf());
				break;
			case HDLVariableDeclaration:
				HDLVariableDeclaration hvd = (HDLVariableDeclaration) decl;
				for (HDLVariable var : hvd.getVariables()) {
					addVariable(var);
				}
				break;
			default:
				if (decl instanceof HDLFunction) {
					HDLFunction func = (HDLFunction) decl;
					addFunction(func);
				} else
					throw new IllegalArgumentException("Did not handle:" + decl);
			}
		}
	}

	/**
	 * Adds the given variable to the library so that it can be resolved by
	 * resolveVariable
	 * 
	 * @param var
	 */
	public void addVariable(HDLVariable var) {
		checkFrozen(var);
		variables.put(var.getFullName(), var);
	}

	/**
	 * Adds the given function to the library so that it can be resolved by
	 * resolveFunction
	 * 
	 * @param func
	 */
	public void addFunction(HDLFunction func) {
		checkFrozen(func);
		functions.put(func.getFullName(), func);
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
	public HDLVariable resolveVariable(ArrayList<String> imports, HDLQualifiedName type) {
		imports.add("pshdl.*");
		HDLVariable hdlType = variables.get(type);
		if (hdlType == null) {
			// System.out.println("HDLLibrary.resolve() Checking imports for:" +
			// type + " @" + this);
			for (String string : imports) {
				if (string.endsWith(type.toString()))
					return variables.get(new HDLQualifiedName(string));
			}
			for (String string : imports) {
				if (string.endsWith(".*")) {
					HDLQualifiedName newTypeName = new HDLQualifiedName(string).skipLast(1).append(type);
					// System.out.println("HDLLibrary.resolve()" + newTypeName);
					HDLVariable newType = variables.get(newTypeName);
					if (newType != null) {
						return newType;
					}
				}
			}
		}
		return hdlType;
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
	public HDLFunction resolveFunction(ArrayList<String> imports, HDLQualifiedName type) {
		imports.add("pshdl.*");
		HDLFunction hdlType = functions.get(type);
		if (hdlType == null) {
			// System.out.println("HDLLibrary.resolve() Checking imports for:" +
			// type + " @" + this);
			for (String string : imports) {
				if (string.endsWith(type.toString()))
					return functions.get(new HDLQualifiedName(string));
			}
			for (String string : imports) {
				if (string.endsWith(".*")) {
					HDLQualifiedName newTypeName = new HDLQualifiedName(string).skipLast(1).append(type);
					// System.out.println("HDLLibrary.resolve()" + newTypeName);
					HDLFunction newType = functions.get(newTypeName);
					if (newType != null) {
						return newType;
					}
				}
			}
		}
		return hdlType;
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
	public HDLType resolve(ArrayList<String> imports, HDLQualifiedName type) {
		imports.add("pshdl.*");
		HDLType hdlType = types.get(type);
		if (hdlType == null) {
			// System.out.println("HDLLibrary.resolve() Checking imports for:" +
			// type + " @" + this);
			for (String string : imports) {
				if (string.endsWith(type.toString()))
					return types.get(new HDLQualifiedName(string));
			}
			for (String string : imports) {
				if (string.endsWith(".*")) {
					HDLQualifiedName newTypeName = new HDLQualifiedName(string).skipLast(1).append(type);
					// System.out.println("HDLLibrary.resolve()" + newTypeName);
					HDLType newType = types.get(newTypeName);
					if (newType != null) {
						return newType;
					}
				}
			}
		}
		return hdlType;
	}

	/**
	 * Adds the given interface to the library so that it can be resolved by
	 * resolveType
	 * 
	 * @param hIf
	 */
	public void addInterface(HDLInterface hIf) {
		checkFrozen(hIf);
		HDLQualifiedName fullName = hIf.getFullName();
		types.put(fullName, hIf);
	}

	private void checkFrozen(IHDLObject hObject) {
		if (!hObject.isFrozen())
			throw new IllegalArgumentException("Objects need to be frozen to be added");
	}

	/**
	 * Adds the given enum to the library so that it can be resolved by
	 * resolveType
	 * 
	 * @param hEnum
	 */
	public void addEnum(HDLEnum hEnum) {
		checkFrozen(hEnum);
		HDLQualifiedName fullName = hEnum.getFullName();
		types.put(fullName, hEnum);
	}

	public void addSideFiles(List<SideFile> files) {
		sideFiles.addAll(files);
	}

	public HDLConfig getConfig() {
		return config;
	}
}

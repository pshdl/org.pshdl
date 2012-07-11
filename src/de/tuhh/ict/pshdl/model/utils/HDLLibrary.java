package de.tuhh.ict.pshdl.model.utils;

import java.util.*;
import java.util.concurrent.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLLibrary {
	public Map<HDLQualifiedName, HDLUnit> units = new ConcurrentHashMap<HDLQualifiedName, HDLUnit>();
	public Map<HDLQualifiedName, HDLType> types = new ConcurrentHashMap<HDLQualifiedName, HDLType>();

	private static Map<String, HDLLibrary> libs = new HashMap<String, HDLLibrary>();

	public static void registerLibrary(String uri, HDLLibrary library) {
		libs.put(uri, library);
	}

	public static HDLLibrary getLibrary(String uri) {
		return libs.get(uri);
	}

	public void addPkg(HDLPackage pkg) {
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
			default:
				throw new IllegalArgumentException("Did not handle:" + decl);
			}
		}
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

	public void addInterface(HDLInterface hIf) {
		HDLQualifiedName fullName = hIf.getFullName();
		types.put(fullName, hIf);
	}

	public void addEnum(HDLEnum hEnum) {
		HDLQualifiedName fullName = hEnum.getFullName();
		types.put(fullName, hEnum);
	}

	public static void clear() {
		libs.clear();
	}
}

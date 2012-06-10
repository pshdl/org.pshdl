package de.tuhh.ict.pshdl.model.utils;

import java.util.*;
import java.util.concurrent.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLLibrary {
	public Map<HDLQualifiedName, HDLPackage> pkgs = new ConcurrentHashMap<HDLQualifiedName, HDLPackage>();
	public Map<HDLQualifiedName, HDLType> types = new ConcurrentHashMap<HDLQualifiedName, HDLType>();

	private static Map<String, HDLLibrary> libs = new HashMap<String, HDLLibrary>();

	public static void registerLibrary(String uri, HDLLibrary library) {
		// System.out.println("HDLLibrary.registerLibrary() " + library +
		// " for:" + uri);
		libs.put(uri, library);
	}

	public static HDLLibrary getLibrary(String uri) {
		// System.out.println("HDLLibrary.getLibrary() for " + uri);
		return libs.get(uri);
	}

	public void addPkg(HDLPackage pkg) {
		HDLQualifiedName hdlPkg = new HDLQualifiedName(pkg.getPkg());
		for (HDLUnit unit : pkg.getUnits()) {
			HDLQualifiedName uq = hdlPkg.append(new HDLQualifiedName(unit.getName()));
			System.out.println("HDLLibrary.addPkg()" + uq + " to " + this);
			HDLQualifiedName skipLast = uq.skipLast(1);
			HDLPackage hdlPackage = pkgs.get(skipLast);
			if (hdlPackage == null) {
				hdlPackage = new HDLPackage().setPkg(skipLast.toString());
			}
			ArrayList<HDLUnit> units = hdlPackage.getUnits();
			Iterator<HDLUnit> it = units.iterator();
			while (it.hasNext()) {
				HDLUnit type = it.next();
				HDLQualifiedName name = new HDLQualifiedName(type.getName());
				if (name.equals(uq))
					it.remove();
			}
			units.add(unit.setName(uq.getLastSegment()).copy());
			hdlPackage = hdlPackage.setUnits(units);
			pkgs.put(skipLast, hdlPackage);
			types.put(uq, unit.asInterface());
			List<HDLInterface> list = unit.getAllObjectsOf(HDLInterface.class, true);
			for (HDLInterface hdlInterface : list) {
				HDLQualifiedName append = uq.append(hdlInterface.getName());
				System.out.println("HDLLibrary.addPkg() Interface:" + append);
				types.put(append, hdlInterface);
			}
			List<HDLEnum> elist = unit.getAllObjectsOf(HDLEnum.class, true);
			for (HDLEnum hdlEnum : elist) {
				HDLQualifiedName append = uq.append(hdlEnum.getName());
				System.out.println("HDLLibrary.addPkg() Enum:" + append + " to " + this);
				types.put(append, hdlEnum);
			}
		}
		for (HDLDeclaration decl : pkg.getDeclarations()) {
			switch (decl.getClassType()) {
			case HDLEnumDeclaration:
				HDLEnumDeclaration ed = (HDLEnumDeclaration) decl;
				HDLEnum hdlEnum = ed.getHEnum();
				HDLQualifiedName append = hdlPkg.append(new HDLQualifiedName(hdlEnum.getName()));
				System.out.println("HDLLibrary.addPkg() Enum:" + append);
				types.put(append, hdlEnum);
				break;
			case HDLInterfaceDeclaration:
				HDLInterfaceDeclaration hid = (HDLInterfaceDeclaration) decl;
				HDLInterface hdlInterface = hid.getHIf();
				HDLQualifiedName newIFname = hdlPkg.append(new HDLQualifiedName(hdlInterface.getName()));
				System.out.println("HDLLibrary.addPkg() Interface:" + newIFname + " to " + this);
				types.put(newIFname, hdlInterface);
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
			System.out.println("HDLLibrary.resolve() Checking imports for:" + type + " @" + this);
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
		System.out.println("HDLLibrary.addInterface()" + hIf.asRef() + " to:" + this);
		types.put(hIf.asRef(), hIf);
	}

	public static void clear() {
		libs.clear();
	}
}

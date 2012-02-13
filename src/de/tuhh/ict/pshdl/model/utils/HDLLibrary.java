package de.tuhh.ict.pshdl.model.utils;

import java.util.*;
import java.util.concurrent.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLLibrary {
	public Map<HDLQualifiedName, HDLPackage> pkgs = new ConcurrentHashMap<HDLQualifiedName, HDLPackage>();
	public Map<HDLQualifiedName, HDLType> types = new ConcurrentHashMap<HDLQualifiedName, HDLType>();

	private static Map<String, HDLLibrary> libs = new HashMap<String, HDLLibrary>();

	public static void registerLibrary(String uri, HDLLibrary library) {
		libs.put(uri, library);
	}

	public static HDLLibrary getLibrary(String uri) {
		return libs.get(uri);
	}

	public void addPkg(HDLPackage pkg) {
		pkg.setLibrary(this);
		HDLQualifiedName hdlPkg = new HDLQualifiedName(pkg.getPkg());
		for (HDLUnit unit : pkg.getUnits()) {
			HDLQualifiedName uq = hdlPkg.append(new HDLQualifiedName(unit.getName()));
			System.out.println("HDLLibrary.addPkg()" + uq);
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
			units.add(unit);
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
				System.out.println("HDLLibrary.addPkg() Enum:" + append);
				types.put(append, hdlEnum);
			}
		}
	}

	public HDLType resolve(String name, ArrayList<String> imports, HDLQualifiedName type) {
		HDLType hdlType = types.get(type);
		if (hdlType == null) {
			for (String string : imports) {
				if (string.endsWith(type.toString()))
					return types.get(new HDLQualifiedName(string));
			}
			for (String string : imports) {
				if (string.endsWith(".*")) {
					HDLQualifiedName newTypeName = new HDLQualifiedName(string).skipLast(1).append(type);
					System.out.println("HDLLibrary.resolve()" + newTypeName);
					HDLType newType = types.get(newTypeName);
					if (newType != null) {
						return newType;
					}
				}
			}
		}
		return hdlType;
	}
}

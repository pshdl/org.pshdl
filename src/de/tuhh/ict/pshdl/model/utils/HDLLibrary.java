package de.tuhh.ict.pshdl.model.utils;

import java.util.*;
import java.util.concurrent.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLLibrary {
	public Map<String, HDLPackage> pkgs = new ConcurrentHashMap<String, HDLPackage>();

	public HDLPackage getOrCreatePackage(HDLQualifiedName qfn) {
		if ((qfn == null) || (qfn.length == 0))
			throw new IllegalArgumentException("Can not get null or empty String package");
		String rootpkg = qfn.getSegment(0);
		HDLPackage hdlPackage = pkgs.get(rootpkg);
		if (hdlPackage == null) {
			hdlPackage = new HDLPackage(rootpkg, null, this);
			pkgs.put(rootpkg, hdlPackage);
		}
		return hdlPackage.getOrCreateSubPackages(1, qfn);
	}

	public HDLPackage getPackage(HDLQualifiedName qfn) {
		if ((qfn == null) || (qfn.length == 0))
			throw new IllegalArgumentException("Can not get null or empty String package");
		String rootpkg = qfn.getSegment(0);
		HDLPackage hdlPackage = pkgs.get(rootpkg);
		if (hdlPackage == null) {
			return null;
		}
		return hdlPackage.getSubPackages(1, qfn);
	}

	public HDLUnit getUnit(HDLQualifiedName name) {
		HDLPackage pkg = getPackage(name.skipLast(1));
		return pkg.getUnit(name.getLastSegment());
	}

	private static Map<String, HDLLibrary> libs = new HashMap<String, HDLLibrary>();

	public static void registerLibrary(String uri, HDLLibrary library) {
		libs.put(uri, library);
	}

	public static HDLLibrary getLibrary(String uri) {
		return libs.get(uri);
	}

	public void addUnit(HDLUnit unit) {
		HDLQualifiedName fqn = new HDLQualifiedName(unit.getName());
		getOrCreatePackage(fqn.skipLast(1)).units.put(fqn.getLastSegment(), unit);
	}
}

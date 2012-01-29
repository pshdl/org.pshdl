package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLPackage {
	public final String name;
	public final Map<String, HDLPackage> pkgs = new HashMap<String, HDLPackage>();
	public final Map<String, HDLUnit> units = new HashMap<String, HDLUnit>();
	protected HDLPackage parent;
	public HDLLibrary library;

	public HDLPackage(String name, HDLPackage parent, HDLLibrary library) {
		this.name = name;
		this.parent = parent;
		if (parent != null)
			parent.addPackage(this);
		this.library = library;
	}

	public void addPackage(HDLPackage hdlPackage) {
		String localName = hdlPackage.name;
		pkgs.put(localName, hdlPackage);
	}

	protected HDLPackage getOrCreateSubPackages(int i, HDLQualifiedName qfn) {
		if (i == qfn.length) {
			if (qfn.getSegment(i - 1).equals(name)) {
				return this;
			}
			throw new IllegalArgumentException("This package should be named:" + qfn.getSegment(i));
		}
		HDLPackage hdlPackage = pkgs.get(qfn.getSegment(i));
		if (hdlPackage == null) {
			hdlPackage = new HDLPackage(qfn.getSegment(i), this, library);
		}
		return hdlPackage.getOrCreateSubPackages(i + 1, qfn);
	}

	protected HDLPackage getSubPackages(int i, HDLQualifiedName qfn) {
		if (i == qfn.length) {
			if (qfn.getSegment(i - 1).equals(name)) {
				return this;
			}
			throw new IllegalArgumentException("This package should be named:" + qfn.getSegment(i));
		}
		HDLPackage hdlPackage = pkgs.get(qfn.getSegment(i));
		if (hdlPackage == null) {
			return null;
		}
		return hdlPackage.getOrCreateSubPackages(i + 1, qfn);
	}

	public HDLQualifiedName getFullName() {
		if (parent == null)
			return HDLQualifiedName.create(name);
		return parent.getFullName().append(name);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("package ").append(getFullName());
		return sb.toString();
	}

	public HDLUnit getUnit(String lastSegment) {
		return units.get(lastSegment);
	}

}

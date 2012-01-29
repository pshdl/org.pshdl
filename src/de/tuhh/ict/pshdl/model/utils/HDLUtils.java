package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLUtils {

	public static List<HDLVariableDeclaration> getallVariableDeclarations(List<HDLStatement> stmnts) {
		List<HDLVariableDeclaration> res = new LinkedList<HDLVariableDeclaration>();
		for (HDLStatement hdlStatement : stmnts) {
			if (hdlStatement instanceof HDLVariableDeclaration) {
				HDLVariableDeclaration hid = (HDLVariableDeclaration) hdlStatement;
				res.add(hid);
			}
		}
		return res;
	}

	public static List<HDLEnumDeclaration> getallEnumDeclarations(List<HDLStatement> stmnts) {
		List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
		for (HDLStatement hdlStatement : stmnts) {
			if (hdlStatement instanceof HDLEnumDeclaration) {
				HDLEnumDeclaration hed = (HDLEnumDeclaration) hdlStatement;
				res.add(hed);
			}
		}
		return res;
	}

	public static List<HDLInterface> getallInterfaceDeclarations(List<HDLStatement> stmnts) {
		List<HDLInterface> res = new LinkedList<HDLInterface>();
		for (HDLStatement hdlStatement : stmnts) {
			if (hdlStatement instanceof HDLInterfaceDeclaration) {
				HDLInterfaceDeclaration hid = (HDLInterfaceDeclaration) hdlStatement;
				res.add(hid.getHIf());
			}
			if (hdlStatement instanceof HDLDirectGeneration) {
				HDLDirectGeneration hid = (HDLDirectGeneration) hdlStatement;
				res.add(hid.getHIf());
			}
		}
		return res;
	}

	public static List<HDLVariable> getallVariables(ArrayList<HDLStatement> stmnts) {
		List<HDLVariable> res = new LinkedList<HDLVariable>();
		for (HDLStatement hdlStatement : stmnts) {
			if (hdlStatement instanceof HDLVariableDeclaration) {
				HDLVariableDeclaration hid = (HDLVariableDeclaration) hdlStatement;
				for (HDLVariable hdlVariable : hid.getVariables()) {
					res.add(hdlVariable);
				}
			}
			if (hdlStatement instanceof HDLInterfaceInstantiation) {
				HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) hdlStatement;
				res.add(hii.getVar());
			}
		}
		return res;
	}
}

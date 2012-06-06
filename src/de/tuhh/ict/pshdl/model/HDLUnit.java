package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLUnit contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String libURI. Can <b>not</b> be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<String> imports. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> statements. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLUnit extends AbstractHDLUnit implements IStatementContainer {
	/**
	 * Constructs a new instance of {@link HDLUnit}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param libURI
	 *            the value for libURI. Can <b>not</b> be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param imports
	 *            the value for imports. Can be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLUnit(HDLObject container, String libURI, String name, ArrayList<String> imports, ArrayList<HDLStatement> statements, boolean validate) {
		super(container, libURI, name, imports, statements, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLUnit}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param libURI
	 *            the value for libURI. Can <b>not</b> be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param imports
	 *            the value for imports. Can be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 */
	public HDLUnit(HDLObject container, String libURI, String name, ArrayList<String> imports, ArrayList<HDLStatement> statements) {
		this(container, libURI, name, imports, statements, true);
	}

	public HDLUnit() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLUnit;
	}

	public static HDLFieldAccess<HDLUnit, String> fLibURI = new HDLFieldAccess<HDLUnit, String>() {
		@Override
		public String getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getLibURI();
		}
	};
	public static HDLFieldAccess<HDLUnit, String> fName = new HDLFieldAccess<HDLUnit, String>() {
		@Override
		public String getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};
	public static HDLFieldAccess<HDLUnit, ArrayList<String>> fImports = new HDLFieldAccess<HDLUnit, ArrayList<String>>() {
		@Override
		public ArrayList<String> getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getImports();
		}
	};
	public static HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>> fStatements = new HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>>() {
		@Override
		public ArrayList<HDLStatement> getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getStatements();
		}
	};

	// $CONTENT-BEGIN$
	private HDLInterface unitIF = null;

	public HDLInterface asInterface() {
		if (unitIF != null)
			return unitIF;
		HDLQualifiedName fullName = getFullName();
		unitIF = new HDLInterface().setName(fullName.toString());
		List<HDLVariableDeclaration> declarations = getAllObjectsOf(HDLVariableDeclaration.class, true);
		HDLVariable clk = null, rst = null;
		boolean hasReg = false;
		for (HDLVariableDeclaration hvd : declarations) {
			switch (hvd.getDirection()) {
			case PARAMETER:
			case CONSTANT:
			case IN:
			case INOUT:
			case OUT:
				unitIF = unitIF.addPorts(hvd.copyFiltered(CopyFilter.DEEP));
				break;
			default:
				break;
			}
			for (HDLAnnotation hdla : hvd.getAnnotations()) {
				if (HDLAnnotations.clock.is(hdla)) {
					clk = hvd.getVariables().get(0);
				}
				if (HDLAnnotations.reset.is(hdla)) {
					rst = hvd.getVariables().get(0);
				}
			}
			if (hvd.getRegister() != null)
				hasReg = true;
		}
		if (hasReg) {
			if ((clk == null) && (rst == null)) {
				unitIF = unitIF.addPorts(new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLPrimitive.getBit()) //
						.addVariables(new HDLVariable().setName("clk"))//
						.addVariables(new HDLVariable().setName("rst")));
			}
		}
		unitIF.setContainer(this);
		ModificationSet ms = new ModificationSet();
		List<HDLVariableRef> refs = unitIF.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : refs) {
			ms.replace(ref, ref.setVar(fullName.append(ref.getVarRefName().getLastSegment())));
		}
		return ms.apply(unitIF);
	}

	@Override
	public HDLQualifiedName getFullName() {
		HDLQualifiedName unitName = new HDLQualifiedName(getName());
		if (getContainer() != null) {
			HDLPackage p = (HDLPackage) getContainer();
			if (p.getPkg() != null) {
				return new HDLQualifiedName(p.getPkg()).append(unitName);
			}
		}
		return unitName;
	}

	private HDLResolver resolver = new HDLResolver(this, false);

	@Override
	public HDLEnum resolveEnum(HDLQualifiedName hEnum) {
		HDLEnum resolveEnum = resolver.resolveEnum(hEnum);
		if (resolveEnum != null)
			return resolveEnum;
		return (HDLEnum) resolveType(hEnum);
	}

	@Override
	public HDLInterface resolveInterface(HDLQualifiedName hIf) {
		HDLInterface resolveInterface = resolver.resolveInterface(hIf);
		if (resolveInterface != null)
			return resolveInterface;
		return (HDLInterface) resolveType(hIf);
	}

	@Override
	public HDLType resolveType(HDLQualifiedName type) {
		HDLType resolveType = resolver.resolveType(type);
		if (resolveType != null)
			return resolveType;
		if (library == null)
			library = HDLLibrary.getLibrary(libURI);
		return library.resolve(getName(), getImports(), type);
	}

	private HDLLibrary library;

	@Override
	public HDLVariable resolveVariable(HDLQualifiedName var) {
		HDLVariable hdlVariable = resolver.resolveVariable(var);
		if (hdlVariable != null)
			return hdlVariable;
		return null;
	}

	@Override
	public List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return HDLResolver.getallEnumDeclarations(statements);
	}

	@Override
	public List<HDLInterface> doGetInterfaceDeclarations() {
		return HDLResolver.getallInterfaceDeclarations(statements);
	}

	@Override
	public List<HDLVariableDeclaration> doGetVariableDeclarations() {
		List<HDLVariableDeclaration> res = HDLResolver.getallVariableDeclarations(statements);
		HDLVariableDeclaration hvd = new HDLVariableDeclaration().setType(HDLPrimitive.getBit()).setDirection(HDLDirection.IN);
		hvd.setContainer(this);
		res.add(hvd.addVariables(new HDLVariable(null, "$clk", null, null, null)));
		res.add(hvd.addVariables(new HDLVariable(null, "$rst", null, null, null)));
		return res;
	}

	// $CONTENT-END$

}

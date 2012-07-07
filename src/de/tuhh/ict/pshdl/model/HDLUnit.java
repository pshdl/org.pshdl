package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

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
public class HDLUnit extends AbstractHDLUnit implements de.tuhh.ict.pshdl.model.utils.IStatementContainer {
	/**
	 * Constructs a new instance of {@link HDLUnit}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
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
	public HDLUnit(int containerID, @Nullable HDLObject container, @NonNull String libURI, @NonNull String name, @Nullable ArrayList<String> imports,
			@Nullable ArrayList<HDLStatement> statements, boolean validate) {
		super(containerID, container, libURI, name, imports, statements, validate);
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
	public HDLUnit(int containerID, @Nullable HDLObject container, @NonNull String libURI, @NonNull String name, @Nullable ArrayList<String> imports,
			@Nullable ArrayList<HDLStatement> statements) {
		this(containerID, container, libURI, name, imports, statements, true);
	}

	public HDLUnit() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLUnit;
	}

	/**
	 * The accessor for the field libURI which is of type String.
	 */
	public static HDLFieldAccess<HDLUnit, String> fLibURI = new HDLFieldAccess<HDLUnit, String>() {
		@Override
		public String getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getLibURI();
		}
	};
	/**
	 * The accessor for the field name which is of type String.
	 */
	public static HDLFieldAccess<HDLUnit, String> fName = new HDLFieldAccess<HDLUnit, String>() {
		@Override
		public String getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};
	/**
	 * The accessor for the field imports which is of type ArrayList<String>.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<String>> fImports = new HDLFieldAccess<HDLUnit, ArrayList<String>>() {
		@Override
		public ArrayList<String> getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getImports();
		}
	};
	/**
	 * The accessor for the field statements which is of type
	 * ArrayList<HDLStatement>.
	 */
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
			for (HDLVariable var : hvd.getVariables()) {
				if (var.getAnnotation(HDLAnnotations.clock) != null)
					clk = var;
				if (var.getAnnotation(HDLAnnotations.reset) != null)
					rst = var;
			}
			if (hvd.getRegister() != null)
				hasReg = true;
		}
		if (hasReg) {
			if ((clk == null) && (rst == null)) {
				unitIF = unitIF.addPorts(new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLPrimitive.getBit()) //
						.addVariables(new HDLVariable().setName(HDLRegisterConfig.DEF_CLK.substring(1)))//
						.addVariables(new HDLVariable().setName(HDLRegisterConfig.DEF_RST.substring(1))));
			}
		}
		unitIF.setContainer(this);
		ModificationSet ms = new ModificationSet();
		List<HDLVariableRef> refs = unitIF.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : refs) {
			ms.replace(ref, ref.setVar(fullName.append(ref.getVarRefName().getLastSegment())));
		}
		// System.out.println("HDLUnit.asInterface()" + unitIF);
		return ms.apply(unitIF);
	}

	// $CONTENT-END$

}

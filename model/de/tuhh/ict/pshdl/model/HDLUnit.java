package de.tuhh.ict.pshdl.model;

import static de.tuhh.ict.pshdl.model.extensions.FullNameExtension.*;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLUnit contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * <li>String libURI. Can <b>not</b> be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<String> imports. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> inits. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> statements. Can be <code>null</code>.</li>
 * <li>Boolean simulation. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLUnit extends AbstractHDLUnit {
	/**
	 * Constructs a new instance of {@link HDLUnit}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param libURI
	 *            the value for libURI. Can <b>not</b> be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param imports
	 *            the value for imports. Can be <code>null</code>.
	 * @param inits
	 *            the value for inits. Can be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 * @param simulation
	 *            the value for simulation. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLUnit(@Nullable IHDLObject container, @Nullable ArrayList<HDLAnnotation> annotations, @Nonnull String libURI, @Nonnull String name,
			@Nullable ArrayList<String> imports, @Nullable ArrayList<HDLStatement> inits, @Nullable ArrayList<HDLStatement> statements, @Nonnull Boolean simulation,
			boolean validate) {
		super(container, annotations, libURI, name, imports, inits, statements, simulation, validate);
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
	 * The accessor for the field annotations which is of type
	 * ArrayList<HDLAnnotation>.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<HDLAnnotation>> fAnnotations = new HDLFieldAccess<HDLUnit, ArrayList<HDLAnnotation>>("annotations") {
		@Override
		public ArrayList<HDLAnnotation> getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getAnnotations();
		}
	};
	/**
	 * The accessor for the field libURI which is of type String.
	 */
	public static HDLFieldAccess<HDLUnit, String> fLibURI = new HDLFieldAccess<HDLUnit, String>("libURI") {
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
	public static HDLFieldAccess<HDLUnit, String> fName = new HDLFieldAccess<HDLUnit, String>("name") {
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
	public static HDLFieldAccess<HDLUnit, ArrayList<String>> fImports = new HDLFieldAccess<HDLUnit, ArrayList<String>>("imports") {
		@Override
		public ArrayList<String> getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getImports();
		}
	};
	/**
	 * The accessor for the field inits which is of type
	 * ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>> fInits = new HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>>("inits") {
		@Override
		public ArrayList<HDLStatement> getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getInits();
		}
	};
	/**
	 * The accessor for the field statements which is of type
	 * ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>> fStatements = new HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>>("statements") {
		@Override
		public ArrayList<HDLStatement> getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getStatements();
		}
	};
	/**
	 * The accessor for the field simulation which is of type Boolean.
	 */
	public static HDLFieldAccess<HDLUnit, Boolean> fSimulation = new HDLFieldAccess<HDLUnit, Boolean>("simulation") {
		@Override
		public Boolean getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getSimulation();
		}
	};
	// $CONTENT-BEGIN$
	private HDLInterface unitIF = null;

	public HDLInterface asInterface() {
		if (unitIF != null)
			return unitIF;
		HDLQualifiedName fullName = fullNameOf(this);
		unitIF = new HDLInterface().setName(fullName.toString());
		HDLVariableDeclaration hvds[] = getAllObjectsOf(HDLVariableDeclaration.class, true);

		Collection<HDLVariableDeclaration> declarations = new LinkedList<HDLVariableDeclaration>();
		declarations.addAll(Arrays.asList(hvds));
		HDLDirectGeneration[] generators = getAllObjectsOf(HDLDirectGeneration.class, true);
		for (HDLDirectGeneration hdgi : generators) {
			if (hdgi.getInclude()) {
				List<HDLVariableDeclaration> portAdditions = HDLGenerators.getPortAdditions(hdgi);
				if (portAdditions != null) {
					declarations.addAll(portAdditions);
				}
			}
		}
		HDLVariable clk = null, rst = null;
		boolean hasReg = false;
		for (HDLVariableDeclaration hvd : declarations) {
			switch (hvd.getDirection()) {
			case PARAMETER:
			case CONSTANT:
			case IN:
			case INOUT:
			case OUT:
				unitIF = unitIF.addPorts(hvd.copyFiltered(CopyFilter.DEEP_META));
				break;
			default:
				break;
			}
			for (HDLAnnotation hdla : hvd.getAnnotations()) {
				if (HDLBuiltInAnnotations.clock.is(hdla)) {
					clk = hvd.getVariables().get(0);
				}
				if (HDLBuiltInAnnotations.reset.is(hdla)) {
					rst = hvd.getVariables().get(0);
				}
			}
			for (HDLVariable var : hvd.getVariables()) {
				if (var.getAnnotation(HDLBuiltInAnnotations.clock) != null) {
					clk = var;
				}
				if (var.getAnnotation(HDLBuiltInAnnotations.reset) != null) {
					rst = var;
				}
			}
			if (hvd.getRegister() != null) {
				hasReg = true;
			}
		}
		if (hasReg) {
			if ((clk == null) && (rst == null)) {
				unitIF = unitIF.addPorts(new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLPrimitive.getBit()) //
						.addVariables(new HDLVariable().setName(HDLRegisterConfig.DEF_CLK.substring(1)))//
						.addVariables(new HDLVariable().setName(HDLRegisterConfig.DEF_RST.substring(1))));
			}
		}
		ModificationSet ms = new ModificationSet();
		HDLVariableRef[] refs = unitIF.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : refs) {
			if (ref.getVarRefName().length == 1) {
				ms.replace(ref, ref.setVar(fullName.append(ref.getVarRefName().getLastSegment())));
			}
		}
		// System.out.println("HDLUnit.asInterface()" + unitIF);
		unitIF = ms.apply(unitIF);
		if (!unitIF.isFrozen()) {
			unitIF.freeze(this);
		}
		unitIF.addMeta("FULLNAME", fullName);
		return unitIF;
	}

	@Override
	public HDLLibrary getLibrary() {
		return HDLLibrary.getLibrary(libURI);

	}

	// $CONTENT-END$

}
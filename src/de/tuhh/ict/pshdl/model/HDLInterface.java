package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLInterface contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLVariableDeclaration> ports. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLInterface extends AbstractHDLInterface {
	/**
	 * Constructs a new instance of {@link HDLInterface}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param ports
	 *            the value for ports. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLInterface(HDLObject container, String name, ArrayList<HDLVariableDeclaration> ports, boolean validate) {
		super(container, name, ports, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLInterface}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param ports
	 *            the value for ports. Can be <code>null</code>.
	 */
	public HDLInterface(HDLObject container, String name, ArrayList<HDLVariableDeclaration> ports) {
		this(container, name, ports, true);
	}

	public HDLInterface() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLInterface;
	}

	public static HDLFieldAccess<HDLInterface, ArrayList<HDLVariableDeclaration>> fPorts = new HDLFieldAccess<HDLInterface, ArrayList<HDLVariableDeclaration>>() {
		@Override
		public ArrayList<HDLVariableDeclaration> getValue(HDLInterface obj) {
			if (obj == null)
				return null;
			return obj.getPorts();
		}
	};

	// $CONTENT-BEGIN$

	@Override
	public HDLVariable resolveVariable(HDLQualifiedName var) {
		if (var.length == 1) {
			return getVariable(var.getLastSegment());
		}
		if (getFullName().equals(var.skipLast(1)))
			return getVariable(var.getLastSegment());
		return super.resolveVariable(var);
	}

	private HDLVariable getVariable(String lastSegment) {
		List<HDLVariable> vars = HDLQuery.selectAll(HDLVariable.class).from(this).where(HDLVariable.fName).lastSegmentIs(lastSegment);
		if (vars.isEmpty())
			return null;
		return vars.get(0);
	}

	@Override
	public de.tuhh.ict.pshdl.model.utils.HDLQualifiedName getFullName() {
		HDLQualifiedName asRef = asRef();
		if ((container != null) && (asRef.length != 1))
			container.getFullName().append(asRef);
		return asRef;
	}
	// $CONTENT-END$

}

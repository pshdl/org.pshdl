package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

public class HDLInterfaceRef extends AbstractHDLInterfaceRef {
	/**
	 * Constructs a new instance of {@link HDLInterfaceRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param ifArray
	 *            the value for ifArray. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLInterfaceRef(HDLObject container, HDLQualifiedName var, ArrayList<HDLExpression> array, ArrayList<HDLRange> bits, HDLQualifiedName hIf,
			ArrayList<HDLExpression> ifArray, boolean validate) {
		super(container, var, array, bits, hIf, ifArray, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLInterfaceRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param ifArray
	 *            the value for ifArray. Can be <code>null</code>.
	 */
	public HDLInterfaceRef(HDLObject container, HDLQualifiedName var, ArrayList<HDLExpression> array, ArrayList<HDLRange> bits, HDLQualifiedName hIf,
			ArrayList<HDLExpression> ifArray) {
		this(container, var, array, bits, hIf, ifArray, true);
	}

	public HDLInterfaceRef() {
		super();
	}

	public HDLClass getClassType() {
		return HDLClass.HDLInterfaceRef;
	}

	// $CONTENT-BEGIN$

	@Override
	public HDLVariable resolveVar() {
		HDLType type = resolveHIf().determineType();
		if (type instanceof HDLInterface) {
			HDLInterface hIf = (HDLInterface) type;
			String lastSegment2 = getVarRefName().getLastSegment();
			for (HDLVariableDeclaration vd : hIf.getPorts()) {
				for (HDLVariable hv : vd.getVariables()) {
					String lastSegment = hv.getName();
					if (lastSegment.equals(lastSegment2))
						return hv;
				}
			}
		}
		throw new IllegalArgumentException("Could not resolve interface variable:" + getVarRefName());
	}

	// $CONTENT-END$

}

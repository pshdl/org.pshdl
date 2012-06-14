package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import de.tuhh.ict.pshdl.model.validation.*;

/**
 * The class HDLInterfaceRef contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName var. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> array. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLRange> bits. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName hIf. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> ifArray. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLInterfaceRef extends AbstractHDLInterfaceRef {
	/**
	 * Constructs a new instance of {@link HDLInterfaceRef}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
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
	public HDLInterfaceRef(int containerID, HDLObject container, HDLQualifiedName var, ArrayList<HDLExpression> array, ArrayList<HDLRange> bits, HDLQualifiedName hIf,
			ArrayList<HDLExpression> ifArray, boolean validate) {
		super(containerID, container, var, array, bits, hIf, ifArray, validate);
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
	public HDLInterfaceRef(int containerID, HDLObject container, HDLQualifiedName var, ArrayList<HDLExpression> array, ArrayList<HDLRange> bits, HDLQualifiedName hIf,
			ArrayList<HDLExpression> ifArray) {
		this(containerID, container, var, array, bits, hIf, ifArray, true);
	}

	public HDLInterfaceRef() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLInterfaceRef;
	}

	/**
	 * The accessor for the field hIf which is of type HDLQualifiedName
	 */
	public static HDLFieldAccess<HDLInterfaceRef, HDLQualifiedName> fHIf = new HDLFieldAccess<HDLInterfaceRef, HDLQualifiedName>() {
		@Override
		public HDLQualifiedName getValue(HDLInterfaceRef obj) {
			if (obj == null)
				return null;
			return obj.getHIfRefName();
		}
	};
	/**
	 * The accessor for the field ifArray which is of type
	 * ArrayList<HDLExpression>
	 */
	public static HDLFieldAccess<HDLInterfaceRef, ArrayList<HDLExpression>> fIfArray = new HDLFieldAccess<HDLInterfaceRef, ArrayList<HDLExpression>>() {
		@Override
		public ArrayList<HDLExpression> getValue(HDLInterfaceRef obj) {
			if (obj == null)
				return null;
			return obj.getIfArray();
		}
	};

	// $CONTENT-BEGIN$

	@Override
	public HDLVariable resolveVar() {
		HDLVariable resolveHIf = resolveHIf();
		if (resolveHIf == null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getHIfRefName()));
		HDLType type = resolveHIf.determineType();
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
		throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getVarRefName()));
	}

	// $CONTENT-END$

}

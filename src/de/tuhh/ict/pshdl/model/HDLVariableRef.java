package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLVariableRef contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName var. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> array. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLRange> bits. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLVariableRef extends AbstractHDLVariableRef {
	/**
	 * Constructs a new instance of {@link HDLVariableRef}
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
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLVariableRef(int containerID, HDLObject container, HDLQualifiedName var, ArrayList<HDLExpression> array, ArrayList<HDLRange> bits, boolean validate) {
		super(containerID, container, var, array, bits, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLVariableRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 */
	public HDLVariableRef(int containerID, HDLObject container, HDLQualifiedName var, ArrayList<HDLExpression> array, ArrayList<HDLRange> bits) {
		this(containerID, container, var, array, bits, true);
	}

	public HDLVariableRef() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLVariableRef;
	}

	/**
	 * The accessor for the field array which is of type
	 * ArrayList<HDLExpression>
	 */
	public static HDLFieldAccess<HDLVariableRef, ArrayList<HDLExpression>> fArray = new HDLFieldAccess<HDLVariableRef, ArrayList<HDLExpression>>() {
		@Override
		public ArrayList<HDLExpression> getValue(HDLVariableRef obj) {
			if (obj == null)
				return null;
			return obj.getArray();
		}
	};
	/**
	 * The accessor for the field bits which is of type ArrayList<HDLRange>
	 */
	public static HDLFieldAccess<HDLVariableRef, ArrayList<HDLRange>> fBits = new HDLFieldAccess<HDLVariableRef, ArrayList<HDLRange>>() {
		@Override
		public ArrayList<HDLRange> getValue(HDLVariableRef obj) {
			if (obj == null)
				return null;
			return obj.getBits();
		}
	};

	// $CONTENT-BEGIN$

	@Override
	public HDLVariable resolveVar() {
		HDLVariable var = super.resolveVar();
		if (var == null)
			throw new IllegalArgumentException("This variable can not be resolved:" + getVarRefName());
		return var;
	}

	// $CONTENT-END$

}

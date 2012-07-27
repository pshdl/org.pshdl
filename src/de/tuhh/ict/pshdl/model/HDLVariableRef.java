package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import de.tuhh.ict.pshdl.model.validation.*;

/**
 * The class HDLVariableRef contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName var. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> array. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLRange> bits. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLVariableRef extends AbstractHDLVariableRef {
	/**
	 * Constructs a new instance of {@link HDLVariableRef}
	 * 
	 * @param objectID
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
	public HDLVariableRef(int objectID, @Nullable IHDLObject container, @NonNull HDLQualifiedName var, @Nullable ArrayList<HDLExpression> array,
			@Nullable ArrayList<HDLRange> bits, boolean validate, boolean updateContainer) {
		super(objectID, container, var, array, bits, validate, updateContainer);
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
	public HDLVariableRef(int objectID, @Nullable IHDLObject container, @NonNull HDLQualifiedName var, @Nullable ArrayList<HDLExpression> array, @Nullable ArrayList<HDLRange> bits) {
		this(objectID, container, var, array, bits, true, true);
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
	 * ArrayList<HDLExpression>.
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
	 * The accessor for the field bits which is of type ArrayList<HDLRange>.
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
		if (var == null) {
			HDLObject.printInfo(this);
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getVarRefName()));
		}
		return var;
	}

	// $CONTENT-END$

}

package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLFunctionCall contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> params. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLFunctionCall extends AbstractHDLFunctionCall implements HDLStatement {
	/**
	 * Constructs a new instance of {@link HDLFunctionCall}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLFunctionCall(@Nullable IHDLObject container, @NonNull HDLQualifiedName name, @Nullable ArrayList<HDLExpression> params, boolean validate) {
		super(container, name, params, validate);
	}

	public HDLFunctionCall() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLFunctionCall;
	}

	/**
	 * The accessor for the field name which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLFunctionCall, HDLQualifiedName> fName = new HDLFieldAccess<HDLFunctionCall, HDLQualifiedName>() {
		@Override
		public HDLQualifiedName getValue(HDLFunctionCall obj) {
			if (obj == null)
				return null;
			return obj.getNameRefName();
		}
	};
	/**
	 * The accessor for the field params which is of type
	 * ArrayList<HDLExpression>.
	 */
	public static HDLFieldAccess<HDLFunctionCall, ArrayList<HDLExpression>> fParams = new HDLFieldAccess<HDLFunctionCall, ArrayList<HDLExpression>>() {
		@Override
		public ArrayList<HDLExpression> getValue(HDLFunctionCall obj) {
			if (obj == null)
				return null;
			return obj.getParams();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLInlineFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>String args. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression expr. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLInlineFunction extends AbstractHDLInlineFunction {
	/**
	 * Constructs a new instance of {@link HDLInlineFunction}
	 * 
	 * @param objectID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param args
	 *            the value for args. Can <b>not</b> be <code>null</code>.
	 * @param expr
	 *            the value for expr. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLInlineFunction(int objectID, @Nullable IHDLObject container, @NonNull String name, @NonNull String args, @NonNull HDLExpression expr, boolean validate,
			boolean updateContainer) {
		super(objectID, container, name, args, expr, validate, updateContainer);
	}

	/**
	 * Constructs a new instance of {@link HDLInlineFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param args
	 *            the value for args. Can <b>not</b> be <code>null</code>.
	 * @param expr
	 *            the value for expr. Can <b>not</b> be <code>null</code>.
	 */
	public HDLInlineFunction(int objectID, @Nullable IHDLObject container, @NonNull String name, @NonNull String args, @NonNull HDLExpression expr) {
		this(objectID, container, name, args, expr, true, true);
	}

	public HDLInlineFunction() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLInlineFunction;
	}

	/**
	 * The accessor for the field args which is of type String.
	 */
	public static HDLFieldAccess<HDLInlineFunction, String> fArgs = new HDLFieldAccess<HDLInlineFunction, String>() {
		@Override
		public String getValue(HDLInlineFunction obj) {
			if (obj == null)
				return null;
			return obj.getArgs();
		}
	};
	/**
	 * The accessor for the field expr which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLInlineFunction, HDLExpression> fExpr = new HDLFieldAccess<HDLInlineFunction, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLInlineFunction obj) {
			if (obj == null)
				return null;
			return obj.getExpr();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

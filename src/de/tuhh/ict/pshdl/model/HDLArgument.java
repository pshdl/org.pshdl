package de.tuhh.ict.pshdl.model;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLArgument contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression expression. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLArgument extends AbstractHDLArgument {
	/**
	 * Constructs a new instance of {@link HDLArgument}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param expression
	 *            the value for expression. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLArgument(@Nullable IHDLObject container, @Nonnull String name, @Nonnull HDLExpression expression, boolean validate) {
		super(container, name, expression, validate);
	}

	public HDLArgument() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLArgument;
	}

	/**
	 * The accessor for the field name which is of type String.
	 */
	public static HDLFieldAccess<HDLArgument, String> fName = new HDLFieldAccess<HDLArgument, String>("name") {
		@Override
		public String getValue(HDLArgument obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};
	/**
	 * The accessor for the field expression which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLArgument, HDLExpression> fExpression = new HDLFieldAccess<HDLArgument, HDLExpression>("expression") {
		@Override
		public HDLExpression getValue(HDLArgument obj) {
			if (obj == null)
				return null;
			return obj.getExpression();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

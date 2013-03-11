package de.tuhh.ict.pshdl.model;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLInlineFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLVariable> args. Can be <code>null</code>.</li>
 * <li>HDLExpression expr. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLInlineFunction extends AbstractHDLInlineFunction {
	/**
	 * Constructs a new instance of {@link HDLInlineFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param args
	 *            the value for args. Can be <code>null</code>.
	 * @param expr
	 *            the value for expr. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLInlineFunction(@Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String name, @Nullable Iterable<HDLVariable> args,
			@Nonnull HDLExpression expr, boolean validate) {
		super(container, annotations, name, args, expr, validate);
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
	 * The accessor for the field args which is of type ArrayList<HDLVariable>.
	 */
	public static HDLFieldAccess<HDLInlineFunction, ArrayList<HDLVariable>> fArgs = new HDLFieldAccess<HDLInlineFunction, ArrayList<HDLVariable>>("args") {
		@Override
		public ArrayList<HDLVariable> getValue(HDLInlineFunction obj) {
			if (obj == null)
				return null;
			return obj.getArgs();
		}
	};
	/**
	 * The accessor for the field expr which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLInlineFunction, HDLExpression> fExpr = new HDLFieldAccess<HDLInlineFunction, HDLExpression>("expr") {
		@Override
		public HDLExpression getValue(HDLInlineFunction obj) {
			if (obj == null)
				return null;
			return obj.getExpr();
		}
	};

	// $CONTENT-BEGIN$

	public HDLExpression getReplacementExpression(HDLFunctionCall hdi) {
		ArrayList<HDLVariable> args = getArgs();
		ArrayList<HDLExpression> params = hdi.getParams();
		return createExpression(args, params, hdi);
	}

	private HDLExpression createExpression(Iterable<HDLVariable> args, Iterable<HDLExpression> params, IHDLObject origin) {
		return substitute(args, params, getExpr(), origin);
	}

	public HDLExpression getReplacementExpressionArgs(IHDLObject origin, HDLExpression... args) {
		return createExpression(getArgs(), asList(args), origin);
	}
	// $CONTENT-END$

}

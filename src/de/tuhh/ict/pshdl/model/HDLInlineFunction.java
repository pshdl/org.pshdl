package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLInlineFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLVariable> args. Can be <code>null</code>.</li>
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
	 *            the value for args. Can be <code>null</code>.
	 * @param expr
	 *            the value for expr. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLInlineFunction(int objectID, @Nullable IHDLObject container, @NonNull String name, @Nullable ArrayList<HDLVariable> args, @NonNull HDLExpression expr,
			boolean validate, boolean updateContainer) {
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
	 *            the value for args. Can be <code>null</code>.
	 * @param expr
	 *            the value for expr. Can <b>not</b> be <code>null</code>.
	 */
	public HDLInlineFunction(int objectID, @Nullable IHDLObject container, @NonNull String name, @Nullable ArrayList<HDLVariable> args, @NonNull HDLExpression expr) {
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
	 * The accessor for the field args which is of type ArrayList<HDLVariable>.
	 */
	public static HDLFieldAccess<HDLInlineFunction, ArrayList<HDLVariable>> fArgs = new HDLFieldAccess<HDLInlineFunction, ArrayList<HDLVariable>>() {
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
	public static HDLFieldAccess<HDLInlineFunction, HDLExpression> fExpr = new HDLFieldAccess<HDLInlineFunction, HDLExpression>() {
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
		return createExpression(args, params);
	}

	private HDLExpression createExpression(ArrayList<HDLVariable> args, ArrayList<HDLExpression> params) {
		ModificationSet msExp = new ModificationSet();
		HDLExpression orig = getExpr();
		for (int i = 0; i < args.size(); i++) {
			HDLVariable arg = args.get(i);
			Collection<HDLVariableRef> allArgRefs = HDLQuery.select(HDLVariableRef.class).from(this).where(HDLReference.fVar).isEqualTo(arg.getFullName()).getAll();
			for (HDLVariableRef argRef : allArgRefs) {
				msExp.replace(argRef, params.get(i).copyFiltered(CopyFilter.DEEP));
			}
		}
		return msExp.apply(orig).copy();
	}

	public HDLExpression getReplacementExpressionArgs(HDLExpression... args) {
		return createExpression(getArgs(), asList(args));
	}
	// $CONTENT-END$

}

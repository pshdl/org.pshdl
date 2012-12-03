package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLSubstituteFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLVariable> args. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> stmnts. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLSubstituteFunction extends AbstractHDLSubstituteFunction {
	/**
	 * Constructs a new instance of {@link HDLSubstituteFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param args
	 *            the value for args. Can be <code>null</code>.
	 * @param stmnts
	 *            the value for stmnts. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLSubstituteFunction(@Nullable IHDLObject container, @Nullable ArrayList<HDLAnnotation> annotations, @NonNull String name, @Nullable ArrayList<HDLVariable> args,
			@Nullable ArrayList<HDLStatement> stmnts, boolean validate) {
		super(container, annotations, name, args, stmnts, validate);
	}

	public HDLSubstituteFunction() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLSubstituteFunction;
	}

	/**
	 * The accessor for the field args which is of type ArrayList<HDLVariable>.
	 */
	public static HDLFieldAccess<HDLSubstituteFunction, ArrayList<HDLVariable>> fArgs = new HDLFieldAccess<HDLSubstituteFunction, ArrayList<HDLVariable>>() {
		@Override
		public ArrayList<HDLVariable> getValue(HDLSubstituteFunction obj) {
			if (obj == null)
				return null;
			return obj.getArgs();
		}
	};
	/**
	 * The accessor for the field stmnts which is of type
	 * ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLSubstituteFunction, ArrayList<HDLStatement>> fStmnts = new HDLFieldAccess<HDLSubstituteFunction, ArrayList<HDLStatement>>() {
		@Override
		public ArrayList<HDLStatement> getValue(HDLSubstituteFunction obj) {
			if (obj == null)
				return null;
			return obj.getStmnts();
		}
	};

	// $CONTENT-BEGIN$
	public HDLStatement[] getReplacementStatements(HDLFunctionCall hdi) {
		ArrayList<HDLVariable> args = getArgs();
		ArrayList<HDLExpression> params = hdi.getParams();
		return createStatements(args, params, hdi);
	}

	public static final String META = "INLINED_FROM";

	private HDLStatement[] createStatements(ArrayList<HDLVariable> args, ArrayList<HDLExpression> params, IHDLObject origin) {
		HDLStatement[] res = new HDLStatement[getStmnts().size()];
		int pos = 0;
		for (HDLStatement stmnt : getStmnts()) {
			res[pos++] = substitute(args, params, stmnt, origin);
		}
		return res;
	}

	public HDLStatement[] getReplacementExpressionArgs(IHDLObject origin, HDLExpression... args) {
		return createStatements(getArgs(), asList(args), origin);
	}
	// $CONTENT-END$

}

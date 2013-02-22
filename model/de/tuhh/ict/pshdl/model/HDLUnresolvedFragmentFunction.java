package de.tuhh.ict.pshdl.model;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLUnresolvedFragmentFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String frag. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> array. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLRange> bits. Can be <code>null</code>.</li>
 * <li>HDLUnresolvedFragment sub. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> params. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLUnresolvedFragmentFunction extends AbstractHDLUnresolvedFragmentFunction {
	/**
	 * Constructs a new instance of {@link HDLUnresolvedFragmentFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param frag
	 *            the value for frag. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 * @param sub
	 *            the value for sub. Can be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLUnresolvedFragmentFunction(@Nullable IHDLObject container, @Nonnull String frag, @Nullable ArrayList<HDLExpression> array, @Nullable ArrayList<HDLRange> bits,
			@Nullable HDLUnresolvedFragment sub, @Nullable ArrayList<HDLExpression> params, boolean validate) {
		super(container, frag, array, bits, sub, params, validate);
	}

	public HDLUnresolvedFragmentFunction() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLUnresolvedFragmentFunction;
	}

	/**
	 * The accessor for the field params which is of type
	 * ArrayList<HDLExpression>.
	 */
	public static HDLFieldAccess<HDLUnresolvedFragmentFunction, ArrayList<HDLExpression>> fParams = new HDLFieldAccess<HDLUnresolvedFragmentFunction, ArrayList<HDLExpression>>(
			"params") {
		@Override
		public ArrayList<HDLExpression> getValue(HDLUnresolvedFragmentFunction obj) {
			if (obj == null)
				return null;
			return obj.getParams();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

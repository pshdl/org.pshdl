package de.tuhh.ict.pshdl.model;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLUnresolvedFragment contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String frag. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> array. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLRange> bits. Can be <code>null</code>.</li>
 * <li>HDLUnresolvedFragment sub. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLUnresolvedFragment extends AbstractHDLUnresolvedFragment implements HDLStatement {
	/**
	 * Constructs a new instance of {@link HDLUnresolvedFragment}
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
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLUnresolvedFragment(@Nullable IHDLObject container, @Nonnull String frag, @Nullable ArrayList<HDLExpression> array, @Nullable ArrayList<HDLRange> bits,
			@Nullable HDLUnresolvedFragment sub, boolean validate) {
		super(container, frag, array, bits, sub, validate);
	}

	public HDLUnresolvedFragment() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLUnresolvedFragment;
	}

	/**
	 * The accessor for the field frag which is of type String.
	 */
	public static HDLFieldAccess<HDLUnresolvedFragment, String> fFrag = new HDLFieldAccess<HDLUnresolvedFragment, String>("frag") {
		@Override
		public String getValue(HDLUnresolvedFragment obj) {
			if (obj == null)
				return null;
			return obj.getFrag();
		}
	};
	/**
	 * The accessor for the field array which is of type
	 * ArrayList<HDLExpression>.
	 */
	public static HDLFieldAccess<HDLUnresolvedFragment, ArrayList<HDLExpression>> fArray = new HDLFieldAccess<HDLUnresolvedFragment, ArrayList<HDLExpression>>("array") {
		@Override
		public ArrayList<HDLExpression> getValue(HDLUnresolvedFragment obj) {
			if (obj == null)
				return null;
			return obj.getArray();
		}
	};
	/**
	 * The accessor for the field bits which is of type ArrayList<HDLRange>.
	 */
	public static HDLFieldAccess<HDLUnresolvedFragment, ArrayList<HDLRange>> fBits = new HDLFieldAccess<HDLUnresolvedFragment, ArrayList<HDLRange>>("bits") {
		@Override
		public ArrayList<HDLRange> getValue(HDLUnresolvedFragment obj) {
			if (obj == null)
				return null;
			return obj.getBits();
		}
	};
	/**
	 * The accessor for the field sub which is of type HDLUnresolvedFragment.
	 */
	public static HDLFieldAccess<HDLUnresolvedFragment, HDLUnresolvedFragment> fSub = new HDLFieldAccess<HDLUnresolvedFragment, HDLUnresolvedFragment>("sub") {
		@Override
		public HDLUnresolvedFragment getValue(HDLUnresolvedFragment obj) {
			if (obj == null)
				return null;
			return obj.getSub();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

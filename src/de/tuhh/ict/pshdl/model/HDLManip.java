package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLManip contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLManipType type. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression target. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLType castTo. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLManip extends AbstractHDLManip {
	/**
	 * Constructs a new instance of {@link HDLManip}
	 * 
	 * @param objectID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param target
	 *            the value for target. Can <b>not</b> be <code>null</code>.
	 * @param castTo
	 *            the value for castTo. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLManip(int objectID, @Nullable IHDLObject container, @NonNull HDLManipType type, @NonNull HDLExpression target, @Nullable HDLType castTo, boolean validate,
			boolean updateContainer) {
		super(objectID, container, type, target, castTo, validate, updateContainer);
	}

	/**
	 * Constructs a new instance of {@link HDLManip}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param target
	 *            the value for target. Can <b>not</b> be <code>null</code>.
	 * @param castTo
	 *            the value for castTo. Can be <code>null</code>.
	 */
	public HDLManip(int objectID, @Nullable IHDLObject container, @NonNull HDLManipType type, @NonNull HDLExpression target, @Nullable HDLType castTo) {
		this(objectID, container, type, target, castTo, true, true);
	}

	public HDLManip() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLManip;
	}

	public static enum HDLManipType {
		CAST, ARITH_NEG, BIT_NEG, LOGIC_NEG;
	}

	/**
	 * The accessor for the field type which is of type HDLManipType.
	 */
	public static HDLFieldAccess<HDLManip, HDLManipType> fType = new HDLFieldAccess<HDLManip, HDLManipType>() {
		@Override
		public HDLManipType getValue(HDLManip obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}
	};
	/**
	 * The accessor for the field target which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLManip, HDLExpression> fTarget = new HDLFieldAccess<HDLManip, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLManip obj) {
			if (obj == null)
				return null;
			return obj.getTarget();
		}
	};
	/**
	 * The accessor for the field castTo which is of type HDLType.
	 */
	public static HDLFieldAccess<HDLManip, HDLType> fCastTo = new HDLFieldAccess<HDLManip, HDLType>() {
		@Override
		public HDLType getValue(HDLManip obj) {
			if (obj == null)
				return null;
			return obj.getCastTo();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

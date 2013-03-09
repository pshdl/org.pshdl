package de.tuhh.ict.pshdl.model;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLForLoop contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLRange> range. Can <b>not</b> be <code>null</code>,
 * additionally the collection must contain at least one element.</li>
 * <li>HDLVariable param. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> dos. Can <b>not</b> be <code>null</code>,
 * additionally the collection must contain at least one element.</li>
 * </ul>
 */
public class HDLForLoop extends AbstractHDLForLoop {
	/**
	 * Constructs a new instance of {@link HDLForLoop}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param range
	 *            the value for range. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param param
	 *            the value for param. Can <b>not</b> be <code>null</code>.
	 * @param dos
	 *            the value for dos. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLForLoop(@Nullable IHDLObject container, @Nonnull Iterable<HDLRange> range, @Nonnull HDLVariable param, @Nonnull Iterable<HDLStatement> dos, boolean validate) {
		super(container, range, param, dos, validate);
	}

	public HDLForLoop() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLForLoop;
	}

	/**
	 * The accessor for the field range which is of type ArrayList<HDLRange>.
	 */
	public static HDLFieldAccess<HDLForLoop, ArrayList<HDLRange>> fRange = new HDLFieldAccess<HDLForLoop, ArrayList<HDLRange>>("range") {
		@Override
		public ArrayList<HDLRange> getValue(HDLForLoop obj) {
			if (obj == null)
				return null;
			return obj.getRange();
		}
	};
	/**
	 * The accessor for the field param which is of type HDLVariable.
	 */
	public static HDLFieldAccess<HDLForLoop, HDLVariable> fParam = new HDLFieldAccess<HDLForLoop, HDLVariable>("param") {
		@Override
		public HDLVariable getValue(HDLForLoop obj) {
			if (obj == null)
				return null;
			return obj.getParam();
		}
	};
	/**
	 * The accessor for the field dos which is of type ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLForLoop, ArrayList<HDLStatement>> fDos = new HDLFieldAccess<HDLForLoop, ArrayList<HDLStatement>>("dos") {
		@Override
		public ArrayList<HDLStatement> getValue(HDLForLoop obj) {
			if (obj == null)
				return null;
			return obj.getDos();
		}
	};
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}

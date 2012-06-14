package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLConcat contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> cats. Can <b>not</b> be <code>null</code>,
 * additionally the collection must contain at least one element.</li>
 * </ul>
 */
public class HDLConcat extends AbstractHDLConcat {
	/**
	 * Constructs a new instance of {@link HDLConcat}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param cats
	 *            the value for cats. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLConcat(int containerID, HDLObject container, ArrayList<HDLExpression> cats, boolean validate) {
		super(containerID, container, cats, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLConcat}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param cats
	 *            the value for cats. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 */
	public HDLConcat(int containerID, HDLObject container, ArrayList<HDLExpression> cats) {
		this(containerID, container, cats, true);
	}

	public HDLConcat() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLConcat;
	}

	/**
	 * The accessor for the field cats which is of type ArrayList<HDLExpression>
	 */
	public static HDLFieldAccess<HDLConcat, ArrayList<HDLExpression>> fCats = new HDLFieldAccess<HDLConcat, ArrayList<HDLExpression>>() {
		@Override
		public ArrayList<HDLExpression> getValue(HDLConcat obj) {
			if (obj == null)
				return null;
			return obj.getCats();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}

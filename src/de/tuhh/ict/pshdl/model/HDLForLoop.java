package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import de.tuhh.ict.pshdl.model.utils.*;

/**
 * The class HDLForLoop contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
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
	 * @param containerID
	 *            a unique ID that identifies this instance
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
	public HDLForLoop(int containerID, HDLObject container, ArrayList<HDLRange> range, HDLVariable param, ArrayList<HDLStatement> dos, boolean validate) {
		super(containerID, container, range, param, dos, validate);
	}

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
	 */
	public HDLForLoop(int containerID, HDLObject container, ArrayList<HDLRange> range, HDLVariable param, ArrayList<HDLStatement> dos) {
		this(containerID, container, range, param, dos, true);
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
	 * The accessor for the field range which is of type ArrayList<HDLRange>
	 */
	public static HDLFieldAccess<HDLForLoop, ArrayList<HDLRange>> fRange = new HDLFieldAccess<HDLForLoop, ArrayList<HDLRange>>() {
		@Override
		public ArrayList<HDLRange> getValue(HDLForLoop obj) {
			if (obj == null)
				return null;
			return obj.getRange();
		}
	};
	/**
	 * The accessor for the field param which is of type HDLVariable
	 */
	public static HDLFieldAccess<HDLForLoop, HDLVariable> fParam = new HDLFieldAccess<HDLForLoop, HDLVariable>() {
		@Override
		public HDLVariable getValue(HDLForLoop obj) {
			if (obj == null)
				return null;
			return obj.getParam();
		}
	};
	/**
	 * The accessor for the field dos which is of type ArrayList<HDLStatement>
	 */
	public static HDLFieldAccess<HDLForLoop, ArrayList<HDLStatement>> fDos = new HDLFieldAccess<HDLForLoop, ArrayList<HDLStatement>>() {
		@Override
		public ArrayList<HDLStatement> getValue(HDLForLoop obj) {
			if (obj == null)
				return null;
			return obj.getDos();
		}
	};

	// $CONTENT-BEGIN$

	@Override
	public List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return HDLResolver.getallEnumDeclarations(dos);
	}

	@Override
	public List<HDLInterface> doGetInterfaceDeclarations() {
		return HDLResolver.getallInterfaceDeclarations(dos);
	}

	@Override
	public List<HDLVariable> doGetVariables() {
		List<HDLVariable> res = new LinkedList<HDLVariable>();
		res.addAll(HDLResolver.getallVariableDeclarations(dos));
		res.add(param);
		return res;
	}

	// $CONTENT-END$

}

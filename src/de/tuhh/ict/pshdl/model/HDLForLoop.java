package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.impl.*;
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
	public HDLForLoop(HDLObject container, ArrayList<HDLRange> range, HDLVariable param, ArrayList<HDLStatement> dos, boolean validate) {
		super(container, range, param, dos, validate);
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
	public HDLForLoop(HDLObject container, ArrayList<HDLRange> range, HDLVariable param, ArrayList<HDLStatement> dos) {
		this(container, range, param, dos, true);
	}

	public HDLForLoop() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLForLoop;
	}

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
	public List<HDLVariableDeclaration> doGetVariableDeclarations() {
		List<HDLVariableDeclaration> res = new LinkedList<HDLVariableDeclaration>();
		res.addAll(HDLResolver.getallVariableDeclarations(dos));
		res.add(new HDLVariableDeclaration(this, null, HDLDirection.HIDDEN, null, HDLPrimitive.getNatural(), asList(param.copy())));
		return res;
	}

	// $CONTENT-END$

}

package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLValueType.HDLDirection;
import de.tuhh.ict.pshdl.model.impl.*;

@SuppressWarnings("all")
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

	// $CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return getallEnumDeclarations(dos);
	}

	@Override
	protected List<HDLInterfaceDeclaration> doGetInterfaceDeclarations() {
		return getallInterfaceDeclarations(dos);
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		List<HDLVariableDeclaration> res = new LinkedList<HDLVariableDeclaration>();
		res.addAll(getallVariableDeclarations(dos));
		HDLPrimitive primitive = new HDLPrimitive(null, HDLRegisterConfig.defaultConfig(), HDLDirection.HIDDEN, HDLPrimitiveType.INTEGER, null);
		res.add(new HDLVariableDeclaration(this, primitive, asList(param)));
		return res;
	}
	// $CONTENT-END$
}

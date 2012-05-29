package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLEnumDeclaration contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLEnum hEnum. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLEnumDeclaration extends AbstractHDLEnumDeclaration {
	/**
	 * Constructs a new instance of {@link HDLEnumDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLEnumDeclaration(HDLObject container, HDLEnum hEnum, boolean validate) {
		super(container, hEnum, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLEnumDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 */
	public HDLEnumDeclaration(HDLObject container, HDLEnum hEnum) {
		this(container, hEnum, true);
	}

	public HDLEnumDeclaration() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLEnumDeclaration;
	}

	// $CONTENT-BEGIN$
	@Override
	public List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return Collections.singletonList(this);
	}

	@Override
	public List<HDLInterface> doGetInterfaceDeclarations() {
		return Collections.emptyList();
	}

	@Override
	public List<HDLVariableDeclaration> doGetVariableDeclarations() {
		return Collections.emptyList();
	}
	// $CONTENT-END$

}

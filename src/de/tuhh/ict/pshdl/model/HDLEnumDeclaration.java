package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
public class HDLEnumDeclaration extends AbstractHDLEnumDeclaration {
	/**
	 * Constructs a new instance of {@link HDLEnumDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
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
//$CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return Collections.singletonList(this);
	}
//$CONTENT-END$
}	

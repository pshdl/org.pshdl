package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLInterfaceDeclaration contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLInterface hIf. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLInterfaceDeclaration extends AbstractHDLInterfaceDeclaration {
	/**
	 * Constructs a new instance of {@link HDLInterfaceDeclaration}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLInterfaceDeclaration(int containerID, @Nullable HDLObject container, @NonNull HDLInterface hIf, boolean validate) {
		super(containerID, container, hIf, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLInterfaceDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 */
	public HDLInterfaceDeclaration(int containerID, @Nullable HDLObject container, @NonNull HDLInterface hIf) {
		this(containerID, container, hIf, true);
	}

	public HDLInterfaceDeclaration() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLInterfaceDeclaration;
	}

	/**
	 * The accessor for the field hIf which is of type HDLInterface.
	 */
	public static HDLFieldAccess<HDLInterfaceDeclaration, HDLInterface> fHIf = new HDLFieldAccess<HDLInterfaceDeclaration, HDLInterface>() {
		@Override
		public HDLInterface getValue(HDLInterfaceDeclaration obj) {
			if (obj == null)
				return null;
			return obj.getHIf();
		}
	};

	// $CONTENT-BEGIN$
	@Override
	public List<HDLInterface> doGetInterfaceDeclarations() {
		return Collections.singletonList(getHIf());
	}

	@Override
	public List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return Collections.emptyList();
	}

	@Override
	public List<HDLVariable> doGetVariables() {
		return Collections.emptyList();
	}
	// $CONTENT-END$

}

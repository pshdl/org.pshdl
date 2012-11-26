package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLInterfaceDeclaration contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * <li>HDLInterface hIf. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLInterfaceDeclaration extends AbstractHDLInterfaceDeclaration {
	/**
	 * Constructs a new instance of {@link HDLInterfaceDeclaration}
	 * 
	 * @param objectID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLInterfaceDeclaration(int objectID, @Nullable IHDLObject container, @Nullable ArrayList<HDLAnnotation> annotations, @NonNull HDLInterface hIf, boolean validate,
			boolean updateContainer) {
		super(objectID, container, annotations, hIf, validate, updateContainer);
	}

	/**
	 * Constructs a new instance of {@link HDLInterfaceDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 */
	public HDLInterfaceDeclaration(int objectID, @Nullable IHDLObject container, @Nullable ArrayList<HDLAnnotation> annotations, @NonNull HDLInterface hIf) {
		this(objectID, container, annotations, hIf, true, true);
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

	// $CONTENT-END$

}

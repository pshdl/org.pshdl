package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLPackage contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String libURI. Can <b>not</b> be <code>null</code>.</li>
 * <li>String pkg. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLUnit> units. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLDeclaration> declarations. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLPackage extends AbstractHDLPackage {
	/**
	 * Constructs a new instance of {@link HDLPackage}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param libURI
	 *            the value for libURI. Can <b>not</b> be <code>null</code>.
	 * @param pkg
	 *            the value for pkg. Can be <code>null</code>.
	 * @param units
	 *            the value for units. Can be <code>null</code>.
	 * @param declarations
	 *            the value for declarations. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLPackage(int containerID, @Nullable HDLObject container, @NonNull String libURI, @Nullable String pkg, @Nullable ArrayList<HDLUnit> units,
			@Nullable ArrayList<HDLDeclaration> declarations, boolean validate) {
		super(containerID, container, libURI, pkg, units, declarations, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLPackage}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param libURI
	 *            the value for libURI. Can <b>not</b> be <code>null</code>.
	 * @param pkg
	 *            the value for pkg. Can be <code>null</code>.
	 * @param units
	 *            the value for units. Can be <code>null</code>.
	 * @param declarations
	 *            the value for declarations. Can be <code>null</code>.
	 */
	public HDLPackage(int containerID, @Nullable HDLObject container, @NonNull String libURI, @Nullable String pkg, @Nullable ArrayList<HDLUnit> units,
			@Nullable ArrayList<HDLDeclaration> declarations) {
		this(containerID, container, libURI, pkg, units, declarations, true);
	}

	public HDLPackage() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLPackage;
	}

	/**
	 * The accessor for the field libURI which is of type String.
	 */
	public static HDLFieldAccess<HDLPackage, String> fLibURI = new HDLFieldAccess<HDLPackage, String>() {
		@Override
		public String getValue(HDLPackage obj) {
			if (obj == null)
				return null;
			return obj.getLibURI();
		}
	};
	/**
	 * The accessor for the field pkg which is of type String.
	 */
	public static HDLFieldAccess<HDLPackage, String> fPkg = new HDLFieldAccess<HDLPackage, String>() {
		@Override
		public String getValue(HDLPackage obj) {
			if (obj == null)
				return null;
			return obj.getPkg();
		}
	};
	/**
	 * The accessor for the field units which is of type ArrayList<HDLUnit>.
	 */
	public static HDLFieldAccess<HDLPackage, ArrayList<HDLUnit>> fUnits = new HDLFieldAccess<HDLPackage, ArrayList<HDLUnit>>() {
		@Override
		public ArrayList<HDLUnit> getValue(HDLPackage obj) {
			if (obj == null)
				return null;
			return obj.getUnits();
		}
	};
	/**
	 * The accessor for the field declarations which is of type
	 * ArrayList<HDLDeclaration>.
	 */
	public static HDLFieldAccess<HDLPackage, ArrayList<HDLDeclaration>> fDeclarations = new HDLFieldAccess<HDLPackage, ArrayList<HDLDeclaration>>() {
		@Override
		public ArrayList<HDLDeclaration> getValue(HDLPackage obj) {
			if (obj == null)
				return null;
			return obj.getDeclarations();
		}
	};
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}

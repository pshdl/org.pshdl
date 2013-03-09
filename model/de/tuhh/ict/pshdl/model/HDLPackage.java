package de.tuhh.ict.pshdl.model;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLPackage contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
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
	public HDLPackage(@Nullable IHDLObject container, @Nonnull String libURI, @Nullable String pkg, @Nullable Iterable<HDLUnit> units,
			@Nullable Iterable<HDLDeclaration> declarations, boolean validate) {
		super(container, libURI, pkg, units, declarations, validate);
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
	public static HDLFieldAccess<HDLPackage, String> fLibURI = new HDLFieldAccess<HDLPackage, String>("libURI") {
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
	public static HDLFieldAccess<HDLPackage, String> fPkg = new HDLFieldAccess<HDLPackage, String>("pkg") {
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
	public static HDLFieldAccess<HDLPackage, ArrayList<HDLUnit>> fUnits = new HDLFieldAccess<HDLPackage, ArrayList<HDLUnit>>("units") {
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
	public static HDLFieldAccess<HDLPackage, ArrayList<HDLDeclaration>> fDeclarations = new HDLFieldAccess<HDLPackage, ArrayList<HDLDeclaration>>("declarations") {
		@Override
		public ArrayList<HDLDeclaration> getValue(HDLPackage obj) {
			if (obj == null)
				return null;
			return obj.getDeclarations();
		}
	};

	// $CONTENT-BEGIN$
	@Override
	public HDLLibrary getLibrary() {
		return HDLLibrary.getLibrary(libURI);

	}
	// $CONTENT-END$

}

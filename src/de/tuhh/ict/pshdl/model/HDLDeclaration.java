package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLDeclaration contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLDeclaration extends AbstractHDLDeclaration {
	/**
	 * Constructs a new instance of {@link HDLDeclaration}
	 * 
	 * @param objectID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLDeclaration(int objectID, @Nullable IHDLObject container, @Nullable ArrayList<HDLAnnotation> annotations, boolean validate, boolean updateContainer) {
		super(objectID, container, annotations, validate, updateContainer);
	}

	/**
	 * Constructs a new instance of {@link HDLDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 */
	public HDLDeclaration(int objectID, @Nullable IHDLObject container, @Nullable ArrayList<HDLAnnotation> annotations) {
		this(objectID, container, annotations, true, true);
	}

	public HDLDeclaration() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLDeclaration;
	}

	/**
	 * The accessor for the field annotations which is of type
	 * ArrayList<HDLAnnotation>.
	 */
	public static HDLFieldAccess<HDLDeclaration, ArrayList<HDLAnnotation>> fAnnotations = new HDLFieldAccess<HDLDeclaration, ArrayList<HDLAnnotation>>() {
		@Override
		public ArrayList<HDLAnnotation> getValue(HDLDeclaration obj) {
			if (obj == null)
				return null;
			return obj.getAnnotations();
		}
	};

	// $CONTENT-BEGIN$
	public HDLAnnotation getAnnotation(Enum<?> range) {
		for (HDLAnnotation anno : getAnnotations()) {
			if (anno.getName().equals(range.toString()))
				return anno;
		}
		return null;
	}
	// $CONTENT-END$

}

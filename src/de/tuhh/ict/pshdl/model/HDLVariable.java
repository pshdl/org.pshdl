package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLVariable contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> dimensions. Can be <code>null</code>.</li>
 * <li>HDLExpression defaultValue. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLVariable extends AbstractHDLVariable {
	/**
	 * Constructs a new instance of {@link HDLVariable}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dimensions
	 *            the value for dimensions. Can be <code>null</code>.
	 * @param defaultValue
	 *            the value for defaultValue. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLVariable(HDLObject container, String name, ArrayList<HDLExpression> dimensions, HDLExpression defaultValue, ArrayList<HDLAnnotation> annotations, boolean validate) {
		super(container, name, dimensions, defaultValue, annotations, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLVariable}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dimensions
	 *            the value for dimensions. Can be <code>null</code>.
	 * @param defaultValue
	 *            the value for defaultValue. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 */
	public HDLVariable(HDLObject container, String name, ArrayList<HDLExpression> dimensions, HDLExpression defaultValue, ArrayList<HDLAnnotation> annotations) {
		this(container, name, dimensions, defaultValue, annotations, true);
	}

	public HDLVariable() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLVariable;
	}

	public static HDLFieldAccess<HDLVariable, String> fName = new HDLFieldAccess<HDLVariable, String>() {
		@Override
		public String getValue(HDLVariable obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};
	public static HDLFieldAccess<HDLVariable, ArrayList<HDLExpression>> fDimensions = new HDLFieldAccess<HDLVariable, ArrayList<HDLExpression>>() {
		@Override
		public ArrayList<HDLExpression> getValue(HDLVariable obj) {
			if (obj == null)
				return null;
			return obj.getDimensions();
		}
	};
	public static HDLFieldAccess<HDLVariable, HDLExpression> fDefaultValue = new HDLFieldAccess<HDLVariable, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLVariable obj) {
			if (obj == null)
				return null;
			return obj.getDefaultValue();
		}
	};
	public static HDLFieldAccess<HDLVariable, ArrayList<HDLAnnotation>> fAnnotations = new HDLFieldAccess<HDLVariable, ArrayList<HDLAnnotation>>() {
		@Override
		public ArrayList<HDLAnnotation> getValue(HDLVariable obj) {
			if (obj == null)
				return null;
			return obj.getAnnotations();
		}
	};

	// $CONTENT-BEGIN$

	public HDLRegisterConfig getRegisterConfig() {
		if (container instanceof HDLVariableDeclaration) {
			HDLVariableDeclaration vhd = (HDLVariableDeclaration) container;
			return vhd.getRegister();
		}
		return null;
	}

	public HDLVariableDeclaration.HDLDirection getDirection() {
		if (container instanceof HDLVariableDeclaration) {
			HDLVariableDeclaration vhd = (HDLVariableDeclaration) container;
			return vhd.getDirection();
		}
		return null;
	}

	public HDLQualifiedName asRef() {
		return HDLQualifiedName.create(getName());
	}

	public HDLVariableRef asHDLRef() {
		return new HDLVariableRef().setVar(asRef());
	}

	@Override
	public HDLQualifiedName getFullName() {
		return container.getFullName().append(asRef());
	}

	// $CONTENT-END$

}

package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLVariable contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
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
	public HDLVariable(@Nullable IHDLObject container, @NonNull String name, @Nullable ArrayList<HDLExpression> dimensions, @Nullable HDLExpression defaultValue,
			@Nullable ArrayList<HDLAnnotation> annotations, boolean validate) {
		super(container, name, dimensions, defaultValue, annotations, validate);
	}

	public HDLVariable() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLVariable;
	}

	/**
	 * The accessor for the field name which is of type String.
	 */
	public static HDLFieldAccess<HDLVariable, String> fName = new HDLFieldAccess<HDLVariable, String>("name") {
		@Override
		public String getValue(HDLVariable obj) {
			if (obj == null) {
				return null;
			}
			return obj.getName();
		}
	};
	/**
	 * The accessor for the field dimensions which is of type
	 * ArrayList<HDLExpression>.
	 */
	public static HDLFieldAccess<HDLVariable, ArrayList<HDLExpression>> fDimensions = new HDLFieldAccess<HDLVariable, ArrayList<HDLExpression>>("dimensions") {
		@Override
		public ArrayList<HDLExpression> getValue(HDLVariable obj) {
			if (obj == null) {
				return null;
			}
			return obj.getDimensions();
		}
	};
	/**
	 * The accessor for the field defaultValue which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLVariable, HDLExpression> fDefaultValue = new HDLFieldAccess<HDLVariable, HDLExpression>("defaultValue") {
		@Override
		public HDLExpression getValue(HDLVariable obj) {
			if (obj == null) {
				return null;
			}
			return obj.getDefaultValue();
		}
	};
	/**
	 * The accessor for the field annotations which is of type
	 * ArrayList<HDLAnnotation>.
	 */
	public static HDLFieldAccess<HDLVariable, ArrayList<HDLAnnotation>> fAnnotations = new HDLFieldAccess<HDLVariable, ArrayList<HDLAnnotation>>("annotations") {
		@Override
		public ArrayList<HDLAnnotation> getValue(HDLVariable obj) {
			if (obj == null) {
				return null;
			}
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
		return getFullName();
	}

	public HDLVariableRef asHDLRef() {
		return new HDLVariableRef().setVar(asRef());
	}

	@Override
	protected String validateName(String name) {
		if ((name != null) && name.contains(".")) {
			throw new IllegalArgumentException("Variable names may not contain a dot");
		}
		return super.validateName(name);
	}

	public HDLAnnotation getAnnotation(Enum<?> range) {
		for (HDLAnnotation anno : getAnnotations()) {
			if (anno.getName().equals(range.toString())) {
				return anno;
			}
		}
		if (container instanceof HDLVariableDeclaration) {
			HDLVariableDeclaration vhd = (HDLVariableDeclaration) container;
			return vhd.getAnnotation(range);
		}
		return null;
	}

	// $CONTENT-END$

}

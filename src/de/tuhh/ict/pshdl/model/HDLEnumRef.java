package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import de.tuhh.ict.pshdl.model.validation.*;

/**
 * The class HDLEnumRef contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName var. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLQualifiedName hEnum. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLEnumRef extends AbstractHDLEnumRef {
	/**
	 * Constructs a new instance of {@link HDLEnumRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLEnumRef(@Nullable IHDLObject container, @NonNull HDLQualifiedName var, @NonNull HDLQualifiedName hEnum, boolean validate) {
		super(container, var, hEnum, validate);
	}

	public HDLEnumRef() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLEnumRef;
	}

	/**
	 * The accessor for the field hEnum which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLEnumRef, HDLQualifiedName> fHEnum = new HDLFieldAccess<HDLEnumRef, HDLQualifiedName>() {
		@Override
		public HDLQualifiedName getValue(HDLEnumRef obj) {
			if (obj == null)
				return null;
			return obj.getHEnumRefName();
		}
	};

	// $CONTENT-BEGIN$
	@Override
	public HDLVariable resolveVar() {
		HDLEnum resolveHEnum = resolveHEnum();
		HDLVariable var = resolveHEnum.getVariable(getVarRefName().getLastSegment());
		if (var == null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getVarRefName()));
		return var;
	}

	// $CONTENT-END$

}

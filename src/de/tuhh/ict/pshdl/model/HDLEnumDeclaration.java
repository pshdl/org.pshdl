package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

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
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLEnumDeclaration(int containerID, @Nullable HDLObject container, @NonNull HDLEnum hEnum, boolean validate) {
		super(containerID, container, hEnum, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLEnumDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 */
	public HDLEnumDeclaration(int containerID, @Nullable HDLObject container, @NonNull HDLEnum hEnum) {
		this(containerID, container, hEnum, true);
	}

	public HDLEnumDeclaration() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLEnumDeclaration;
	}

	/**
	 * The accessor for the field hEnum which is of type HDLEnum.
	 */
	public static HDLFieldAccess<HDLEnumDeclaration, HDLEnum> fHEnum = new HDLFieldAccess<HDLEnumDeclaration, HDLEnum>() {
		@Override
		public HDLEnum getValue(HDLEnumDeclaration obj) {
			if (obj == null)
				return null;
			return obj.getHEnum();
		}
	};
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}

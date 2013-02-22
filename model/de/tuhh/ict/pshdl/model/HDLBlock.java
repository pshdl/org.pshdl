package de.tuhh.ict.pshdl.model;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLBlock contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>Boolean process. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> statements. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLBlock extends AbstractHDLBlock {
	/**
	 * Constructs a new instance of {@link HDLBlock}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param process
	 *            the value for process. Can <b>not</b> be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLBlock(@Nullable IHDLObject container, @Nonnull Boolean process, @Nullable ArrayList<HDLStatement> statements, boolean validate) {
		super(container, process, statements, validate);
	}

	public HDLBlock() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLBlock;
	}

	/**
	 * The accessor for the field process which is of type Boolean.
	 */
	public static HDLFieldAccess<HDLBlock, Boolean> fProcess = new HDLFieldAccess<HDLBlock, Boolean>("process") {
		@Override
		public Boolean getValue(HDLBlock obj) {
			if (obj == null)
				return null;
			return obj.getProcess();
		}
	};
	/**
	 * The accessor for the field statements which is of type
	 * ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLBlock, ArrayList<HDLStatement>> fStatements = new HDLFieldAccess<HDLBlock, ArrayList<HDLStatement>>("statements") {
		@Override
		public ArrayList<HDLStatement> getValue(HDLBlock obj) {
			if (obj == null)
				return null;
			return obj.getStatements();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
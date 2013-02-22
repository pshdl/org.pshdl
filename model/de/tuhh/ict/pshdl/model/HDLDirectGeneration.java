package de.tuhh.ict.pshdl.model;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLDirectGeneration contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLVariable var. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLArgument> arguments. Can be <code>null</code>.</li>
 * <li>HDLInterface hIf. Can <b>not</b> be <code>null</code>.</li>
 * <li>String generatorID. Can <b>not</b> be <code>null</code>.</li>
 * <li>String generatorContent. Can <b>not</b> be <code>null</code>.</li>
 * <li>Boolean include. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLDirectGeneration extends AbstractHDLDirectGeneration {
	/**
	 * Constructs a new instance of {@link HDLDirectGeneration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param generatorID
	 *            the value for generatorID. Can <b>not</b> be <code>null</code>
	 *            .
	 * @param generatorContent
	 *            the value for generatorContent. Can <b>not</b> be
	 *            <code>null</code>.
	 * @param include
	 *            the value for include. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLDirectGeneration(@Nullable IHDLObject container, @Nonnull HDLVariable var, @Nullable ArrayList<HDLArgument> arguments, @Nonnull HDLInterface hIf,
			@Nonnull String generatorID, @Nonnull String generatorContent, @Nonnull Boolean include, boolean validate) {
		super(container, var, arguments, hIf, generatorID, generatorContent, include, validate);
	}

	public HDLDirectGeneration() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLDirectGeneration;
	}

	/**
	 * The accessor for the field hIf which is of type HDLInterface.
	 */
	public static HDLFieldAccess<HDLDirectGeneration, HDLInterface> fHIf = new HDLFieldAccess<HDLDirectGeneration, HDLInterface>("hIf") {
		@Override
		public HDLInterface getValue(HDLDirectGeneration obj) {
			if (obj == null)
				return null;
			return obj.getHIf();
		}
	};
	/**
	 * The accessor for the field generatorID which is of type String.
	 */
	public static HDLFieldAccess<HDLDirectGeneration, String> fGeneratorID = new HDLFieldAccess<HDLDirectGeneration, String>("generatorID") {
		@Override
		public String getValue(HDLDirectGeneration obj) {
			if (obj == null)
				return null;
			return obj.getGeneratorID();
		}
	};
	/**
	 * The accessor for the field generatorContent which is of type String.
	 */
	public static HDLFieldAccess<HDLDirectGeneration, String> fGeneratorContent = new HDLFieldAccess<HDLDirectGeneration, String>("generatorContent") {
		@Override
		public String getValue(HDLDirectGeneration obj) {
			if (obj == null)
				return null;
			return obj.getGeneratorContent();
		}
	};
	/**
	 * The accessor for the field include which is of type Boolean.
	 */
	public static HDLFieldAccess<HDLDirectGeneration, Boolean> fInclude = new HDLFieldAccess<HDLDirectGeneration, Boolean>("include") {
		@Override
		public Boolean getValue(HDLDirectGeneration obj) {
			if (obj == null)
				return null;
			return obj.getInclude();
		}
	};
	// $CONTENT-BEGIN$

	private HDLInterface hif = null;

	@Override
	public @Nonnull
	HDLInterface getHIf() {
		if (hif != null)
			return hif;
		hif = HDLGenerators.getInterface(this);
		return hif;
	}

	public HDLQualifiedName getIfName() {
		return super.getHIf().asRef();
	}

	// $CONTENT-END$

}

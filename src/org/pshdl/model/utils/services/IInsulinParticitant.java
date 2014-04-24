package org.pshdl.model.utils.services;

import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.Insulin;

public interface IInsulinParticitant {

	/**
	 * A human readable name of the Participant
	 *
	 * @return a non-null String
	 */
	public String getName();

	/**
	 * This is called before anything is done with the package within
	 * {@link Insulin}
	 *
	 * @param hdlPackage
	 *            the package that will be Insulated
	 * @return either a new package or the input one
	 */
	public <T extends IHDLObject> T preInsulin(T hdlPackage, String src);

	/**
	 * This is called after all {@link Insulin} actions are done
	 *
	 * @param hdlPackage
	 *            the package that went through {@link Insulin}
	 * @return either a new package or the input one
	 */
	public <T extends IHDLObject> T postInsulin(T hdlPackage, String src);
}

package org.pshdl.model.utils.services;

public interface IOutputProvider {
	/**
	 * The hook under which this {@link IOutputProvider} is activated
	 * 
	 * @return a short, non space containing id
	 */
	public String getHookName();

	/**
	 * An informal information about which arguments this
	 * {@link IOutputProvider} is expecting and how it can be used.
	 * 
	 * @return multiple strings, each explaining one option. The first one is
	 *         some general information
	 */
	public String[] getUsage();

	/**
	 * Invokes this {@link IOutputProvider} and passes the given arguments to
	 * it.
	 * 
	 * @param args
	 *            the arguments including the hook
	 * @return an explanation of what went wrong or <code>null</code> if it was
	 *         successful
	 */
	public String invoke(String args[]) throws Exception;
}

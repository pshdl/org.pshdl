package org.pshdl.model.simulation;

import java.util.*;

import org.apache.commons.cli.*;
import org.pshdl.interpreter.*;
import org.pshdl.model.utils.PSAbstractCompiler.CompileResult;
import org.pshdl.model.utils.services.*;
import org.pshdl.model.utils.services.IOutputProvider.MultiOption;
import org.pshdl.model.validation.*;

/**
 * Marker interface for type providers
 * 
 * @author Karsten Becker
 * 
 */
public interface ITypeOuptutProvider {
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
	public MultiOption getUsage();

	public List<CompileResult> invoke(CommandLine cli, ExecutableModel em, Set<Problem> syntaxProblems) throws Exception;

}

/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for
 *     reasonable and customary use in describing the origin of the Work.
 *
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package org.pshdl.model.utils.services;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.google.common.collect.Lists;

public interface IOutputProvider {
	public static class MultiOption {
		public final String before, after;
		public final Options options;
		public final List<MultiOption> subs = Lists.newLinkedList();

		public MultiOption(String before, String after, Options options, MultiOption... sub) {
			super();
			this.before = before;
			this.after = after;
			this.options = options;
			if (sub != null) {
				subs.addAll(Arrays.asList(sub));
			}
		}

		public void printHelp(PrintStream out) {
			final PrintWriter pw = new PrintWriter(out);
			printHelp(pw);
			pw.close();
		}

		private void printHelp(final PrintWriter pw) {
			final HelpFormatter hlp = new HelpFormatter();
			if (before != null) {
				hlp.printWrapped(pw, HelpFormatter.DEFAULT_WIDTH, before);
			}
			if (!options.getOptions().isEmpty()) {
				hlp.printOptions(pw, HelpFormatter.DEFAULT_WIDTH, options, HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD);
			}
			if (after != null) {
				hlp.printWrapped(pw, HelpFormatter.DEFAULT_WIDTH, after);
			}
			for (final MultiOption sub : subs) {
				sub.printHelp(pw);
			}
		}

		public Options allOptions() {
			final Options opt = new Options();
			addOptions(opt);
			return opt;
		}

		private void addOptions(Options opt) {
			for (final Object o : options.getOptions()) {
				opt.addOption((Option) o);
			}
			for (final MultiOption mo : subs) {
				mo.addOptions(opt);
			}
		}

		public CommandLine parse(String[] args) throws ParseException {
			final PosixParser pp = new PosixParser();
			return pp.parse(allOptions(), args);
		}
	}

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

	/**
	 * Invokes this {@link IOutputProvider} and passes the given arguments to
	 * it.
	 *
	 * @param args
	 *            the arguments excluding the hook
	 * @return an explanation of what went wrong or <code>null</code> if it was
	 *         successful
	 */
	public String invoke(CommandLine cli) throws Exception;
}

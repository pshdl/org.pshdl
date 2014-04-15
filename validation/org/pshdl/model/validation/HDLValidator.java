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
package org.pshdl.model.validation;

import java.util.*;

import org.pshdl.model.*;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.services.*;

public class HDLValidator {

	public static class HDLAdvise {
		public final Problem problem;
		public final String explanation;
		public final String message;
		public final String[] solutions;

		public HDLAdvise(Problem problem, String message, String explanation, String... solutions) {
			super();
			this.problem = problem;
			this.explanation = explanation;
			this.message = message;
			this.solutions = solutions;
		}
	}

	private static Map<Class<?>, IHDLValidator> validators;

	/**
	 * Initializes the {@link HDLValidator} with all validators using the
	 * {@link IServiceProvider}. Also registers those validators withe the
	 * {@link CompilerInformation}
	 *
	 * @param info
	 * @param sp
	 */
	public static void init(CompilerInformation info, IServiceProvider sp) {
		validators = new HashMap<Class<?>, IHDLValidator>();
		for (final IHDLValidator gen : sp.getAllValidators()) {
			final String name = gen.getName();
			validators.put(gen.getErrorClass(), gen);
			info.registeredValidators.put(name, gen);
		}
	}

	/**
	 * Validates the given {@link HDLPackage} with the given
	 * {@link HDLEvaluationContext}s.
	 *
	 * @param pkg
	 *            the package to validate
	 * @param context
	 *            the contexts to use. Can be <code>null</code>
	 * @return a list of problems that were found during validation
	 */
	public static Set<Problem> validate(HDLPackage pkg, Map<HDLQualifiedName, HDLEvaluationContext> context) {
		pkg = Insulin.resolveFragments(pkg);
		final Set<Problem> res = new LinkedHashSet<Problem>();
		if (context == null) {
			context = HDLEvaluationContext.createDefault(pkg);
		}
		for (final IHDLValidator validator : validators.values()) {
			validator.check(pkg, res, context);
		}
		return res;
	}

	/**
	 * Check if an {@link HDLAdvise} is available for the given problem
	 *
	 * @param problem
	 *            the problem to check
	 * @return <code>null</code> if no advise found, the advise otherwise
	 */
	public static HDLAdvise advise(Problem problem) {
		final IHDLValidator validator = validators.get(problem.code.getClass());
		if (validator == null)
			return null;
		return validator.advise(problem);
	}

}

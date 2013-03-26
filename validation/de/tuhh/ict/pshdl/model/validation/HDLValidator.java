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
package de.tuhh.ict.pshdl.model.validation;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;

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

	public static void init(CompilerInformation info, IServiceProvider sp) {
		validators = new HashMap<Class<?>, IHDLValidator>();
		for (IHDLValidator gen : sp.getAllValidators()) {
			String name = gen.getName();
			validators.put(gen.getErrorClass(), gen);
			info.registeredValidators.put(name, gen);
		}
	}

	public static Set<Problem> validate(HDLPackage pkg, Map<HDLQualifiedName, HDLEvaluationContext> context) {
		pkg = Insulin.resolveFragments(pkg);
		Set<Problem> res = new LinkedHashSet<Problem>();
		if (context == null) {
			context = HDLEvaluationContext.createDefault(pkg);
		}
		for (IHDLValidator validator : validators.values()) {
			validator.check(pkg, res, context);
		}
		return res;
	}

	public static HDLAdvise advise(Problem problem) {
		IHDLValidator validator = validators.get(problem.code.getClass());
		if (validator == null)
			return null;
		return validator.advise(problem);
	}

}

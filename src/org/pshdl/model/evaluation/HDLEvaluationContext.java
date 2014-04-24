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
package org.pshdl.model.evaluation;

import static org.pshdl.model.extensions.FullNameExtension.fullNameOf;
import static org.pshdl.model.utils.HDLQuery.isEqualTo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.HDLQuery;

/**
 * This is used to resolve parameter to constants. When a HDLUnit has parameter
 * ports, they can be initialized with a constant. However this constant does
 * not need to be the value the unit is instantiated with. This context stores a
 * mapping for those parameters to the actual value. See
 * {@link ConstantEvaluate#constantEvaluate(IHDLObject, HDLEvaluationContext)}
 *
 * @author Karsten Becker
 *
 */
public class HDLEvaluationContext {

	private final Map<String, HDLExpression> context;
	public boolean enumAsInt = false;
	public boolean boolAsInt = false;

	public HDLEvaluationContext(Map<String, HDLExpression> context) {
		this.context = context;
	}

	public HDLEvaluationContext withEnumAndBool(boolean enumAsInt, boolean boolAsInt) {
		final HDLEvaluationContext res = new HDLEvaluationContext(context);
		res.enumAsInt = enumAsInt;
		res.boolAsInt = boolAsInt;
		return res;
	}

	/**
	 * Returns the value for the given {@link HDLVariable}
	 *
	 * @param ref
	 *            the name of this variable is used to lookup the value
	 * @return the value if successful, <code>null</code> if no such value can
	 *         be found
	 */
	public HDLExpression get(HDLVariable ref) {
		final HDLExpression hdlExpression = context.get(ref.getName());
		if (hdlExpression != null)
			return hdlExpression;
		return ref.getDefaultValue();
	}

	/**
	 * Generates a default context where all parameter are assumed to be the
	 * constant they are initialized with
	 *
	 * @param unit
	 *            the unit to create the context for
	 * @return a HDLEvaluationContext with all parameters set to their default
	 */
	public static Map<HDLQualifiedName, HDLEvaluationContext> createDefault(HDLPackage pkg) {
		final Map<HDLQualifiedName, HDLEvaluationContext> res = new HashMap<HDLQualifiedName, HDLEvaluationContext>();
		for (final HDLUnit unit : pkg.getUnits()) {
			final HDLEvaluationContext hec = createDefault(unit);
			final HDLQualifiedName fullName = fullNameOf(unit);
			res.put(fullName, hec);
		}
		return res;
	}

	/**
	 * Generates a default context where all parameter are assumed to be the
	 * constant they are initialized with
	 *
	 * @param unit
	 *            the unit to create the context for
	 * @return a HDLEvaluationContext with all parameters set to their default
	 */
	public static HDLEvaluationContext createDefault(HDLUnit unit) {
		return createDefaultFromObj(unit);
	}

	private static HDLEvaluationContext createDefaultFromObj(IHDLObject unit) {
		final Map<String, HDLExpression> c = new HashMap<String, HDLExpression>();
		final Collection<HDLVariableDeclaration> constants = HDLQuery.select(HDLVariableDeclaration.class)//
				.from(unit).where(HDLVariableDeclaration.fDirection)//
				.matches(isEqualTo(HDLDirection.CONSTANT))//
				.or(isEqualTo(HDLDirection.PARAMETER)) //
				.getAll();
		for (final HDLVariableDeclaration hvd : constants) {
			for (final HDLVariable var : hvd.getVariables()) {
				c.put(var.getName(), var.getDefaultValue());
			}
		}
		return new HDLEvaluationContext(c);
	}

	public static HDLEvaluationContext createDefault(HDLInterface hdlInterface) {
		return createDefaultFromObj(hdlInterface);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		String spacer = "";
		for (final Entry<String, HDLExpression> unit : context.entrySet()) {
			sb.append(spacer).append(unit.getKey()).append(':').append(unit.getValue());
			spacer = ",";
		}
		return sb.toString();
	}

	public Map<String, HDLExpression> getMap() {
		return new HashMap<String, HDLExpression>(context);
	}

	public HDLEvaluationContext set(String key, HDLExpression val) {
		final Map<String, HDLExpression> map = getMap();
		map.put(key, val);
		return new HDLEvaluationContext(map);
	}

}

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
package de.tuhh.ict.pshdl.model.extensions;

import de.tuhh.ict.pshdl.model.utils.*;

public enum ProblemDescription implements MetaAccess<ProblemDescription> {
	DESCRIPTION,
	// Did not evaluate
	SUBEXPRESSION_DID_NOT_EVALUATE, SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE, SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT, VARIABLE_NOT_RESOLVED,
	// Unsupported type
	ARRAY_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS, BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS, ENUMS_NOT_SUPPORTED_FOR_CONSTANTS, TYPE_NOT_SUPPORTED_FOR_CONSTANTS,
	// Ranges
	BOOLEAN_NOT_SUPPORTED_FOR_RANGES, BIT_NOT_SUPPORTED_FOR_RANGES, ZERO_DIVIDE, POSSIBLY_ZERO_DIVIDE,
	// Others
	NON_PRIMITVE_TYPE_NOT_EVALUATED, CAN_NOT_USE_PARAMETER;

	@Override
	public boolean inherit() {
		return true;
	}
}

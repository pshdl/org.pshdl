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
package org.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Optional;

public class Unit {
	public Map<String, NamedElement> declarations = new LinkedHashMap<String, NamedElement>();
	public Memory memory;
	public static final int rowWidth = 32;

	public Unit(Map<String, NamedElement> declarations, Memory memory) {
		super();
		this.declarations = declarations;
		this.memory = memory;
	}

	public Unit() {
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (final NamedElement dec : declarations.values()) {
			sb.append(dec).append('\n');
		}
		sb.append(memory).append('\n');
		return sb.toString();
	}

	public String toCompactString() {
		final String fullString = toString();
		return fullString.replaceAll("\\s+", " ").replaceAll("\\s*([\\{\\};])\\s*", "$1");
	}

	public Optional<NamedElement> resolve(Reference ref) {
		final NamedElement decl = declarations.get(ref.getName());
		if (decl == null)
			return Optional.absent();
		return Optional.of(decl);
	}

	public int getCheckSum() {
		return toCompactString().hashCode();
	}
}

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

import java.util.LinkedList;

import org.antlr.v4.runtime.Token;

public class Alias implements NamedElement {
	public String name;
	public LinkedList<NamedElement> definitions = new LinkedList<>();

	public Alias(String name, NamedElement... definitions) {
		super();
		this.name = name;
		for (final NamedElement namedElement : definitions) {
			this.definitions.add(namedElement);
		}
	}

	public Alias(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("alias ").append(name).append(" {\n");
		for (final NamedElement dec : definitions) {
			sb.append('\t').append(dec).append('\n');
		}
		sb.append('}');
		return sb.toString();
	}

	public Token token;

	@Override
	public void setLocation(Token start) {
		this.token = start;
	}

	@Override
	public String getSimpleName() {
		return getName();
	}
}

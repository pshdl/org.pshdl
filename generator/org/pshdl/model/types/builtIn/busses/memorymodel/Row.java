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
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Token;

import com.google.common.collect.Maps;

public class Row implements NamedElement {
	private final String name;
	public Column column;
	public List<NamedElement> definitions = new LinkedList<NamedElement>();
	public int colIndex;

	public Row(String name, Column column, NamedElement... definitions) {
		super();
		this.name = name;
		this.column = column;
		for (final NamedElement namedElement : definitions) {
			this.definitions.add(namedElement);
		}
	}

	public Row(String name) {
		super();
		this.name = name;
	}

	public final Map<String, Integer> defCount = Maps.newLinkedHashMap();

	public void updateInfo() {
		int bitPos = 31;
		for (final NamedElement ne : definitions) {
			final Definition def = (Definition) ne;
			def.bitPos = bitPos;
			bitPos -= MemoryModel.getSize(def);
			final String rowName = def.getName(this);
			Integer integer = defCount.get(rowName);
			if (integer == null) {
				integer = 0;
			}
			def.arrayIndex = integer;
			defCount.put(rowName, ++integer);
		}
	}

	public String getOrigName() {
		return name;
	}

	@Override
	public String getName() {
		return getSimpleName();
	}

	@Override
	public String getSimpleName() {
		if (!isHidden())
			return name.substring(1);
		return name;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("row ").append(name).append(" {\n");
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

	public boolean isHidden() {
		return name.charAt(0) != '^';
	}
}

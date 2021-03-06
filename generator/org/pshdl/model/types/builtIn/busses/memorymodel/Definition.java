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

import org.antlr.v4.runtime.Token;

public class Definition implements NamedElement {
	public enum WarnType {
		mask, silentMask, limit, silentLimit, error, silentError;

		@Override
		public String toString() {
			switch (this) {
			case limit:
				return "limit";
			case silentLimit:
				return "silent limit";
			case mask:
				return "mask";
			case silentMask:
				return "silent mask";
			case error:
				return "error";
			case silentError:
				return "silent error";
			default:
				throw new IllegalArgumentException("Did not correctly implement this:" + this);
			}
		}
	}

	public enum Type {
		BIT, INT, UINT, UNUSED
	}

	public enum RWType {
		r, rw, w, constant;
	}

	public List<Integer> dimensions = new LinkedList<>();

	public String name = "fill";

	public boolean register;

	public boolean modFlag;

	public RWType rw = null;

	public Type type = Type.UNUSED;
	public int width = -1;

	public Integer arrayIndex;

	public WarnType warn = WarnType.limit;

	public int bitPos;

	public Definition() {
	}

	public Definition(String name, boolean register, boolean modFlag, RWType rw, Type type, int width, int... dimensions) {
		super();
		for (final int dim : dimensions) {
			this.dimensions.add(dim);
		}
		this.name = name;
		this.register = register;
		this.modFlag = modFlag;
		this.rw = rw;
		this.type = type;
		this.width = width;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Definition other = (Definition) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (register != other.register) {
			return false;
		}
		if (rw == null) {
			if (other.rw != null) {
				return false;
			}
		} else if (!rw.equals(other.rw)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		if (width != other.width) {
			return false;
		}
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getName(Row row) {
		if (row.isHidden()) {
			return name;
		}
		return row.getSimpleName() + '_' + name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + (register ? 1231 : 1237);
		result = (prime * result) + ((rw == null) ? 0 : rw.hashCode());
		result = (prime * result) + ((type == null) ? 0 : type.hashCode());
		result = (prime * result) + width;
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (final Integer arr : dimensions) {
			sb.append('[').append(arr).append(']');
		}
		final String reg = register ? "register " : "";
		final String w = width != -1 ? "<" + width + ">" : "";
		final String lowerCase = type.name().toLowerCase();
		final String rwString = rw != null ? rw + " " : "";
		if (type == Type.UNUSED) {
			return name + w + ";";
		}
		String mod = "";
		if (modFlag) {
			mod = " writtenFlag";
		}
		return rwString + reg + lowerCase + w + " " + name + sb + " " + warn + mod + ";";
	}

	public Definition withoutDim() {
		final Definition res = new Definition();
		res.type = type;
		res.width = width;
		res.name = name;
		res.rw = rw;
		res.register = register;
		res.warn = warn;
		res.modFlag = modFlag;
		return res;
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

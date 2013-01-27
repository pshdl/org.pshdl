package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

public class Definition implements NamedElement {
	public enum WarnType {
		mask, silentMask, limit, silentLimit, error, silentError;

		@Override
		public String toString() {
			switch (this) {
			case limit:
				return "limit";
			case mask:
				return "mask";
			case silentLimit:
				return "silent limit";
			case silentMask:
				return "silent mask";
			case error:
				return "error";
			case silentError:
				return "silent error";
			}
			return null;
		}
	}

	public enum Type {
		BIT, INT, UINT, UNUSED
	}

	public enum RWType {
		r, rw, w;
	}

	public List<Integer> dimensions = new LinkedList<Integer>();

	public String name = "fill";

	public boolean register;

	public RWType rw = null;

	public Type type = Type.UNUSED;
	public int width = -1;

	public Integer arrayIndex;

	public WarnType warn = WarnType.limit;

	public int bitPos;

	public Definition() {
	}

	public Definition(String name, boolean register, RWType rw, Type type, int width, int... dimensions) {
		super();
		for (int dim : dimensions) {
			this.dimensions.add(dim);
		}
		this.name = name;
		this.register = register;
		this.rw = rw;
		this.type = type;
		this.width = width;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Definition other = (Definition) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (register != other.register)
			return false;
		if (rw == null) {
			if (other.rw != null)
				return false;
		} else if (!rw.equals(other.rw))
			return false;
		if (type != other.type)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	@Override
	public String getName() {
		return name;
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
		StringBuilder sb = new StringBuilder();
		for (Integer arr : dimensions) {
			sb.append('[').append(arr).append(']');
		}
		String reg = register ? "register " : "";
		String w = width != -1 ? "<" + width + ">" : "";
		String lowerCase = type.name().toLowerCase();
		String rwString = rw != null ? rw + " " : "";
		return rwString + reg + lowerCase + w + " " + name + sb + " " + warn + ";";
	}

	public Definition withoutDim() {
		Definition res = new Definition();
		res.type = type;
		res.width = width;
		res.name = name;
		res.rw = rw;
		res.register = register;
		res.warn = warn;
		return res;
	}
}

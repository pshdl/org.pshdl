package de.tuhh.ict.pshdl.model.utils.services;

import java.math.*;

import org.eclipse.jdt.annotation.*;

public class ValueRange implements Comparable<ValueRange> {
	public final BigInteger from, to;

	public ValueRange(@NonNull BigInteger from, @NonNull BigInteger to) {
		super();
		if (to.compareTo(from) > 0) {
			this.from = from;
			this.to = to;
		} else {
			this.from = to;
			this.to = from;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((from == null) ? 0 : from.hashCode());
		result = (prime * result) + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValueRange other = (ValueRange) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (from.equals(to))
			return "[" + from + "]";
		return "[" + from + "->" + to + "]";
	}

	public ValueRange and(ValueRange other) {
		if (other.from.compareTo(to) > 0) {
			System.out.println("ValueRange.and() this:" + this + " and:" + other);
			return null;
		}
		if (from.compareTo(other.to) > 0) {
			System.out.println("ValueRange.and() this:" + this + " and:" + other);
			return null;
		}
		ValueRange andRange = new ValueRange(from.max(other.from), to.min(other.to));
		return andRange;
	}

	public ValueRange or(ValueRange other) {
		return new ValueRange(from.min(other.from), to.max(other.to));
	}

	@Override
	public int compareTo(ValueRange arg0) {
		int first = this.from.compareTo(arg0.from);
		if (first != 0)
			return first;
		return this.to.compareTo(arg0.to);
	}

	public boolean contains(ValueRange other) {
		if ((from.compareTo(other.from) <= 0) && (to.compareTo(other.to) >= 0))
			return true;
		return false;
	}
}
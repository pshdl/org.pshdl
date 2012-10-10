package de.tuhh.ict.pshdl.model.simulation;

import java.math.*;

public class HDLInfo {
	public Enum<?> var;
	public int lastCycle;
	public BigInteger current = BigInteger.ZERO, last = BigInteger.ZERO;

	public HDLInfo(Enum<?> var) {
		this.var = var;
	}

	public void commit(int cycle) {
		last = current;
		lastCycle = cycle;
	}

	@Override
	public String toString() {
		return "[" + last + "," + current + "]";
	}
}
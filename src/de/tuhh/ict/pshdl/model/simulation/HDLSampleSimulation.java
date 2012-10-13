package de.tuhh.ict.pshdl.model.simulation;

import java.math.*;

import de.tuhh.ict.pshdl.model.evaluation.*;

public class HDLSampleSimulation {
	// in uint a, b;
	// uint temp=a-b;
	// param uint bias;
	// out uint register c-=temp+bias;

	public static enum HDLSampleVariables {
		a, b, c, temp, clk, rst;
	}

	public static class HDLSampleSimulationInputParameter {
		int bias;
	}

	public static class HDLSampleSimulationInput {
		int a, b, clk, rst;
		int cycle;

		public HDLSampleSimulationInput(int a, int b, int clk, int rst, int cycle) {
			super();
			this.a = a;
			this.b = b;
			this.clk = clk;
			this.rst = rst;
		}
	}

	public static class HDLSampleSimulationOutput {
		int cycle;
		int c;

		public HDLSampleSimulationOutput(int c, int cycle) {
			super();
			this.c = c;
			this.cycle = cycle;
		}

		@Override
		public String toString() {
			return "HDLSampleSimulationOutput [c=" + c + "]";
		}
	}

	private static final BigInteger TEMP_BITMASK = BigInteger.ONE.shiftLeft(32).subtract(BigInteger.ONE);
	private static final BigInteger C_BITMASK = BigInteger.ONE.shiftLeft(32).subtract(BigInteger.ONE);

	public static void main(String[] args) {
		System.out.println(-10L >>> 1);
		System.out.println(new BigInteger(1, BigInteger.valueOf(-10).toByteArray()).shiftRight(1).intValue());
		HDLSampleSimulation sim = new HDLSampleSimulation(new HDLSampleSimulationInputParameter());
		System.out.println(sim.compute(null, new HDLSampleSimulationInput(5, 3, 0, 0, 0)));
		System.out.println(sim.compute(null, new HDLSampleSimulationInput(5, 3, 1, 0, 1)));
		System.out.println(sim.compute(null, new HDLSampleSimulationInput(5, 3, 0, 1, 2)));
		System.out.println(sim.compute(null, new HDLSampleSimulationInput(5, 3, 1, 1, 3)));
		System.out.println(sim.compute(null, new HDLSampleSimulationInput(7, 6, 0, 1, 4)));
		System.out.println(sim.compute(null, new HDLSampleSimulationInput(7, 6, 1, 1, 5)));
	}

	private HDLInfo c = new HDLInfo(HDLSampleVariables.c), clk = new HDLInfo(HDLSampleVariables.clk), rst = new HDLInfo(HDLSampleVariables.rst), a = new HDLInfo(
			HDLSampleVariables.a), b = new HDLInfo(HDLSampleVariables.b), temp = new HDLInfo(HDLSampleVariables.temp);
	private HDLSampleSimulationInputParameter context;

	public HDLSampleSimulation(HDLSampleSimulationInputParameter context) {
		this.context = context;
	}

	private void commitAllNonRegisters(int cycle) {
		clk.commit(cycle);
		rst.commit(cycle);
		a.commit(cycle);
		b.commit(cycle);
		temp.commit(cycle);
	}

	public HDLSampleSimulationOutput compute(HDLEvaluationContext context, HDLSampleSimulationInput input) {
		commitAllNonRegisters(input.cycle);
		updateAllNonRegisters(input);
		// Rising Edge condition
		if ((clk.current == BigInteger.ONE) && (clk.previous == BigInteger.ZERO)) {
			// Bypass if reset is active and it is async
			commitRegisterValuesForClk(input.cycle);
			updateRegisterValuesForClk(input);
		}
		dumpAll();
		return new HDLSampleSimulationOutput(c.current.intValue(), input.cycle);
	}

	private void commitRegisterValuesForClk(int cycle) {
		c.commit(cycle);
	}

	private void dumpAll() {
		System.out.println("c:" + c + " clk:" + clk + " rst:" + rst + " a:" + a + " b:" + b + " temp:" + temp);
	}

	private void updateAllNonRegisters(HDLSampleSimulationInput input) {
		a.current = a(input);
		b.current = b(input);
		temp.current = temp(input);
		clk.current = clk(input);
		rst.current = rst(input);
	}

	private BigInteger rst(HDLSampleSimulationInput input) {
		return BigInteger.valueOf(input.rst);
	}

	private BigInteger clk(HDLSampleSimulationInput input) {
		return BigInteger.valueOf(input.clk);
	}

	private BigInteger temp(HDLSampleSimulationInput input) {
		return a(input).subtract(b(input)).and(TEMP_BITMASK);
	}

	private BigInteger b(HDLSampleSimulationInput input) {
		return BigInteger.valueOf(input.b);
	}

	private BigInteger a(HDLSampleSimulationInput input) {
		return BigInteger.valueOf(input.a);
	}

	private void updateRegisterValuesForClk(HDLSampleSimulationInput input) {
		if (input.rst == 0)
			c.current = BigInteger.ZERO;
		else
			c.current = c_update(input);
	}

	private BigInteger c_update(HDLSampleSimulationInput input) {
		return c(input).subtract(temp(input).add(bias())).and(C_BITMASK);
	}

	private BigInteger bias() {
		return BigInteger.valueOf(context.bias);
	}

	private BigInteger c(HDLSampleSimulationInput input) {
		return c.previous;
	}

}

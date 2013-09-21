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
package org.pshdl.model.simulation;

import java.io.*;
import java.math.*;
import java.util.*;

import org.pshdl.interpreter.*;
import org.pshdl.interpreter.VariableInformation.Direction;
import org.pshdl.model.utils.*;

import com.google.common.collect.*;

public class TestbenchRecordingInterpreter implements IHDLInterpreter {
	private final IHDLInterpreter interpreter;
	private final Map<String, BigInteger> lastVal = Maps.newHashMap();
	private final Map<Integer, String> idxName = Maps.newHashMap();
	private final Map<String, Integer> widths = Maps.newHashMap();
	private final PrintStream printStream;
	private final OutputType outputType;

	public static enum OutputType {
		pshdl, vhdl
	}

	public TestbenchRecordingInterpreter(ExecutableModel model, IHDLInterpreter interpreter, HDLQualifiedName dutName, String tbName, String fileName, OutputType type)
			throws IOException {
		super();
		this.interpreter = interpreter;
		this.outputType = type;
		for (final VariableInformation vi : model.variables) {
			widths.put(vi.name, vi.width);
		}
		printStream = new PrintStream(fileName);
		switch (type) {
		case pshdl:
			printStream.format("module %s {\n", tbName);
			printStream.format("\t%s dut;\n", dutName);
			printStream.println("\tprocess {");
			break;
		case vhdl:
			printStream.print("library ieee;\n" + //
					"use ieee.std_logic_1164.all;\n" + //
					"use ieee.numeric_std.all;\n" + //
					"library pshdl;\n" + //
					"use pshdl.Casts.ALL;\n" + //
					"use pshdl.ShiftOps.ALL;\n" + //
					"use pshdl.types.all;\n" + //
					"entity " + tbName + " is\n" + //
					"end;\n" + //
					"architecture pshdlGenerated of " + tbName + " is\n");
			for (final VariableInformation vi : model.variables) {
				if (vi.dir == Direction.INTERNAL) {
					continue;
				}
				System.out.println("TestbenchRecordingInterpreter.TestbenchRecordingInterpreter()" + vi);
				String varType = null;
				if (vi.dimensions.length != 0) {
					varType = vi.name.replaceAll("\\.", "_") + "_array";
				} else {
					switch (vi.type) {
					case BIT:
						if (vi.width == 1) {
							varType = "std_logic";
						} else {
							varType = "std_logic_vector(" + (vi.width - 1) + " downto 0)";
						}
						break;
					case INT:
						varType = "signed(" + (vi.width - 1) + " downto 0)";
						break;
					case UINT:
						varType = "unsigned(" + (vi.width - 1) + " downto 0)";
						break;
					}
				}
				printStream.printf("\tsignal dut_%s : %s;\n", simpleName(vi.name), varType);
			}
			printStream.printf("begin\n" + //
					"\tdut : entity work.%s\n" + //
					"\t\tport map (\n", dutName.toString('_'));

			boolean first = true;
			for (final VariableInformation vi : model.variables) {
				if (vi.dir == Direction.INTERNAL) {
					continue;
				}
				if (!first) {
					printStream.println(",");
				} else {
					printStream.println("");
				}
				first = false;
				printStream.printf("\t\t\t%1$s => dut_%1$s", simpleName(vi.name));
			}
			printStream.print("\n        );\n" + //
					"    process\n" + //
					"    begin\n");
			for (final VariableInformation vi : model.variables) {
				if ((vi.dir == Direction.INTERNAL) || (vi.dir == Direction.OUT)) {
					continue;
				}
				if (vi.dimensions.length != 0) {
					for (final int dim : vi.dimensions) {
						for (int j = 0; j < dim; j++) {
							// TODO Support multiple dim
							setVar(vi.name, BigInteger.ZERO, new int[] { j });
						}
					}
				} else {
					setVar(vi.name, BigInteger.ZERO, new int[0]);
				}
			}
		}
	}

	public void close() {
		switch (outputType) {
		case pshdl:
			printStream.println("\t}");
			printStream.println("}");
			break;
		case vhdl:
			printStream.println("    end process;\n"//
					+ "end;");
		}
		printStream.close();
	}

	@Override
	public void setInput(String name, BigInteger value, int... arrayIdx) {
		if (!value.equals(lastVal.get(name))) {
			setVar(name, value, arrayIdx);
		}
		interpreter.setInput(name, value, arrayIdx);
	}

	private void setVar(String name, BigInteger value, int[] arrayIdx) {
		switch (outputType) {
		case pshdl: {
			final StringBuilder array = new StringBuilder();
			if ((arrayIdx != null) && (arrayIdx.length != 0)) {
				for (final int i : arrayIdx) {
					array.append('[').append(i).append(']');
				}
			}
			printStream.println("\t\tdut." + simpleName(name) + array + "=" + value + ";");
			break;
		}
		case vhdl: {
			final StringBuilder array = new StringBuilder();
			if ((arrayIdx != null) && (arrayIdx.length != 0)) {
				for (final int i : arrayIdx) {
					array.append('(').append(i).append(')');
				}
			}
			printStream.println("\t\tdut_" + simpleName(name) + array + "<=" + toBinString(value, name) + ";");
		}
		}
	}

	private String toBinString(BigInteger lit, String name) {
		final Integer widthInt = widths.get(name);
		if (widthInt == 1)
			return "'" + lit.toString(2) + "'";
		StringBuilder sb = new StringBuilder(widthInt);
		if (lit.signum() < 0) {
			final BigInteger mask = BigInteger.ONE.shiftLeft(widthInt).subtract(BigInteger.ONE);
			sb.append(lit.abs().and(mask).xor(mask).add(BigInteger.ONE).toString(2));
		} else {
			final String binLit = lit.toString(2);
			sb = zeroFill(widthInt, binLit);
		}
		return "B\"" + sb.toString() + '"';
	}

	private static StringBuilder zeroFill(int widthInt, String binLit) {
		final StringBuilder sb = new StringBuilder();
		for (int i = widthInt; i > binLit.length(); i--) {
			sb.append('0');
		}
		if (binLit.length() > widthInt) {
			sb.append(binLit.substring(binLit.length() - widthInt));
		} else {
			sb.append(binLit);
		}
		return sb;
	}

	public String simpleName(String name) {
		return new HDLQualifiedName(name).getLastSegment();
	}

	@Override
	public void setInput(int idx, BigInteger value, int... arrayIdx) {
		final String name = getName(idx);
		if (!value.equals(lastVal.get(name))) {
			setVar(name, value, arrayIdx);
		}
		interpreter.setInput(idx, value, arrayIdx);
	}

	@Override
	public void setInput(String name, long value, int... arrayIdx) {
		final BigInteger bVal = BigInteger.valueOf(value);
		if (!bVal.equals(lastVal.get(name))) {
			setVar(name, bVal, arrayIdx);
		}
		interpreter.setInput(name, value, arrayIdx);
	}

	@Override
	public void setInput(int idx, long value, int... arrayIdx) {
		final String name = getName(idx);
		final BigInteger bVal = BigInteger.valueOf(value);
		if (!bVal.equals(lastVal.get(name))) {
			setVar(name, bVal, arrayIdx);
		}
		interpreter.setInput(idx, value, arrayIdx);
	}

	@Override
	public int getIndex(String name) {
		return interpreter.getIndex(name);
	}

	@Override
	public long getOutputLong(String name, int... arrayIdx) {
		return interpreter.getOutputLong(name, arrayIdx);
	}

	@Override
	public long getOutputLong(int idx, int... arrayIdx) {
		return interpreter.getOutputLong(idx, arrayIdx);
	}

	@Override
	public BigInteger getOutputBig(String name, int... arrayIdx) {
		return interpreter.getOutputBig(name, arrayIdx);
	}

	@Override
	public BigInteger getOutputBig(int idx, int... arrayIdx) {
		return interpreter.getOutputBig(idx, arrayIdx);
	}

	@Override
	public void run() {
		switch (outputType) {
		case pshdl:
			printStream.println("\t\twaitFor(10, TimeUnit.NS);");
			break;
		case vhdl:
			printStream.println("\t\twait for 10 ns;");
			break;
		}
		interpreter.run();
	}

	@Override
	public String getName(int idx) {
		String name = idxName.get(idx);
		if (name != null)
			return name;
		name = interpreter.getName(idx);
		idxName.put(idx, name);
		return name;
	}

	@Override
	public int getDeltaCycle() {
		return interpreter.getDeltaCycle();
	}
}

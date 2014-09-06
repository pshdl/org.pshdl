/**
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 * 
 *     Copyright (C) 2014 Karsten Becker (feedback (at) pshdl (dot) org)
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
 */
package org.pshdl.model.simulation.codegenerator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.VariableInformation;

@SuppressWarnings("all")
public class CommonCompilerExtension {
	public ExecutableModel em;

	public Map<String, Integer> varIdx = new LinkedHashMap<String, Integer>();

	public CommonCompilerExtension(final ExecutableModel em, final int bitWidth) {
		this.em = em;
		final int _length = em.variables.length;
		final ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
		for (final Integer i : _doubleDotLessThan) {
			final VariableInformation _get = em.variables[(i).intValue()];
			this.varIdx.put(_get.name, i);
		}
	}

	public String getJSONDescription() {
		final ArrayList<String> intVar = new ArrayList<String>();
		final ArrayList<String> inVar = new ArrayList<String>();
		final ArrayList<String> inOutVar = new ArrayList<String>();
		final ArrayList<String> outVar = new ArrayList<String>();
		for (final VariableInformation vi : this.em.variables) {
			final VariableInformation.Direction _switchValue = vi.dir;
			if (_switchValue != null) {
				switch (_switchValue) {
				case IN:
					final String _port = this.toPort(vi);
					inVar.add(_port);
					break;
				case INOUT:
					final String _port_1 = this.toPort(vi);
					inOutVar.add(_port_1);
					break;
				case OUT:
					final String _port_2 = this.toPort(vi);
					outVar.add(_port_2);
					break;
				case INTERNAL:
					final String _port_3 = this.toPort(vi);
					intVar.add(_port_3);
					break;
				default:
					break;
				}
			}
		}
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("{\\\"moduleName\\\":\\\"");
		_builder.append(this.em.moduleName, "");
		_builder.append("\\\",\\\"inPorts\\\":[");
		{
			boolean _hasElements = false;
			for (final String port : inVar) {
				if (!_hasElements) {
					_hasElements = true;
				} else {
					_builder.appendImmediate(",", "");
				}
				_builder.append(port, "");
			}
		}
		_builder.append("],\\\"inOutPorts\\\":[");
		{
			boolean _hasElements_1 = false;
			for (final String port_1 : inOutVar) {
				if (!_hasElements_1) {
					_hasElements_1 = true;
				} else {
					_builder.appendImmediate(",", "");
				}
				_builder.append(port_1, "");
			}
		}
		_builder.append("],\\\"outPorts\\\":[");
		{
			boolean _hasElements_2 = false;
			for (final String port_2 : outVar) {
				if (!_hasElements_2) {
					_hasElements_2 = true;
				} else {
					_builder.appendImmediate(",", "");
				}
				_builder.append(port_2, "");
			}
		}
		_builder.append("],\\\"internalPorts\\\":[");
		{
			boolean _hasElements_3 = false;
			for (final String port_3 : intVar) {
				if (!_hasElements_3) {
					_hasElements_3 = true;
				} else {
					_builder.appendImmediate(",", "");
				}
				_builder.append(port_3, "");
			}
		}
		_builder.append("],\\\"nameIdx\\\":{");
		{
			final Set<Map.Entry<String, Integer>> _entrySet = this.varIdx.entrySet();
			boolean _hasElements_4 = false;
			for (final Map.Entry<String, Integer> entry : _entrySet) {
				if (!_hasElements_4) {
					_hasElements_4 = true;
				} else {
					_builder.appendImmediate(",", "");
				}
				_builder.append("\\\"");
				final String _key = entry.getKey();
				_builder.append(_key, "");
				_builder.append("\\\":");
				final Integer _value = entry.getValue();
				_builder.append(_value, "");
			}
		}
		_builder.append("}}");
		return _builder.toString();
	}

	public String toPort(final VariableInformation vi) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("{\\\"idx\\\":");
		final Integer _get = this.varIdx.get(vi.name);
		_builder.append(_get, "");
		_builder.append(",\\\"name\\\":\\\"");
		_builder.append(vi.name, "");
		_builder.append("\\\",\\\"width\\\":");
		_builder.append(vi.width, "");
		_builder.append(",\\\"clock\\\": ");
		_builder.append(vi.isClock, "");
		_builder.append(",\\\"reset\\\":");
		_builder.append(vi.isReset, "");
		_builder.append(",\\\"type\\\":");
		final int _bitJsonType = this.bitJsonType(vi);
		_builder.append(_bitJsonType, "");
		_builder.append("}");
		return _builder.toString();
	}

	public int bitJsonType(final VariableInformation vi) {
		final VariableInformation.Type _switchValue = vi.type;
		if (_switchValue != null) {
			switch (_switchValue) {
			case BIT:
				return 0;
			case INT:
				return 1;
			case UINT:
				return 2;
			case BOOL:
				return 3;
			default:
				break;
			}
		}
		return 0;
	}
}

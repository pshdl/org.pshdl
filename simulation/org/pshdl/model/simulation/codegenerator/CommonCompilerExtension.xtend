/*******************************************************************************
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
 ******************************************************************************/
package org.pshdl.model.simulation.codegenerator

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.Map
import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.VariableInformation

class CommonCompilerExtension {
	public ExecutableModel em
	public Map<String, Integer> varIdx = new LinkedHashMap

	new(ExecutableModel em, int bitWidth) {
		this.em = em
		for (i : 0 ..< em.variables.length) {
			varIdx.put(em.variables.get(i).name, i)
		}
	}

	def String getJSONDescription() {
		val intVar = new ArrayList
		val inVar = new ArrayList
		val inOutVar = new ArrayList
		val outVar = new ArrayList
		for (vi : em.variables) {
			switch (vi.dir) {
				case IN: inVar.add(vi.toPort)
				case INOUT: inOutVar.add(vi.toPort)
				case OUT: outVar.add(vi.toPort)
				case INTERNAL: intVar.add(vi.toPort)
			}
		}
		return '''{\"moduleName\":\"«em.moduleName»\",\"inPorts\":[«FOR port : inVar SEPARATOR ","»«port»«ENDFOR»],\"inOutPorts\":[«FOR port : inOutVar SEPARATOR ","»«port»«ENDFOR»],\"outPorts\":[«FOR port : outVar SEPARATOR ","»«port»«ENDFOR»],\"internalPorts\":[«FOR port : intVar SEPARATOR ","»«port»«ENDFOR»],\"nameIdx\":{«FOR entry : varIdx.
			entrySet SEPARATOR ","»\"«entry.key»\":«entry.value»«ENDFOR»}}'''
	}

	def String toPort(VariableInformation vi) '''{\"idx\":«varIdx.get(vi.name)»,\"name\":\"«vi.name»\",\"width\":«vi.
		width»,\"clock\": «vi.isClock»,\"reset\":«vi.isReset»,\"type\":«vi.bitJsonType»}'''

	def bitJsonType(VariableInformation vi) {
		switch (vi.type) {
			case BIT: return 0
			case INT: return 1
			case UINT: return 2
			case BOOL: return 3
			case STRING: return 4
			case ENUM: return 5
		}
	}

}

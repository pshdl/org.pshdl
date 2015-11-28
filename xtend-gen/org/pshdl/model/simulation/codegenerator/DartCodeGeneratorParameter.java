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

import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.model.simulation.codegenerator.CommonCodeGeneratorParameter;
import org.pshdl.model.simulation.codegenerator.Option;

@SuppressWarnings("all")
public class DartCodeGeneratorParameter extends CommonCodeGeneratorParameter {
  @Option(description = "The name of the library that should be declared. If unspecified, the package of the module will be used", optionName = "pkg", hasArg = true)
  public String library;
  
  @Option(description = "The name of the struct. If not specified, the name of the module will be used", optionName = "pkg", hasArg = true)
  public String unitName;
  
  @Option(description = "When present the code will try to locate simulation_comm.dart locally, instead of importing it as pacakge", optionName = "localImport", hasArg = false)
  public boolean useLocalImport = false;
  
  public DartCodeGeneratorParameter(final ExecutableModel em) {
    super(em, (-1));
    final String moduleName = em.moduleName;
    final int li = moduleName.lastIndexOf(".");
    this.library = null;
    if ((li != (-1))) {
      String _substring = moduleName.substring(0, (li - 1));
      this.library = _substring;
    }
    int _length = moduleName.length();
    String _substring_1 = moduleName.substring((li + 1), _length);
    this.unitName = _substring_1;
  }
  
  public DartCodeGeneratorParameter setLibrary(final String library) {
    this.library = library;
    return this;
  }
  
  public DartCodeGeneratorParameter setUnitName(final String unitName) {
    this.unitName = unitName;
    return this;
  }
  
  public DartCodeGeneratorParameter setUseLocalImport(final boolean useLocalImport) {
    this.useLocalImport = useLocalImport;
    return this;
  }
}

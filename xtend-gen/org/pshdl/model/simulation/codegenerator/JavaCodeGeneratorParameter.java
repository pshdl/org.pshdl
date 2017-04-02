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

import com.google.common.base.Objects;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.model.simulation.codegenerator.CommonCodeGeneratorParameter;
import org.pshdl.model.simulation.codegenerator.Option;

@SuppressWarnings("all")
public class JavaCodeGeneratorParameter extends CommonCodeGeneratorParameter {
  @Option(description = "The name of the package that should be declared. If unspecified, the package of the module will be used", optionName = "pkg", hasArg = true)
  public String packageName;
  
  @Option(description = "The name of the java class. If not specified, the name of the module will be used", optionName = "pkg", hasArg = true)
  public String unitName;
  
  @Option(description = "Generate a JUnit based test-bench", optionName = "junit", hasArg = false)
  public boolean junit = false;
  
  public int executionCores = 0;
  
  public JavaCodeGeneratorParameter(final ExecutableModel em) {
    super(em, 64);
    final String moduleName = em.moduleName;
    final int li = moduleName.lastIndexOf(".");
    this.packageName = null;
    if ((li != (-1))) {
      this.packageName = moduleName.substring(0, li);
    }
    this.unitName = moduleName.substring((li + 1), moduleName.length());
  }
  
  public JavaCodeGeneratorParameter setPackageName(final String packageName) {
    this.packageName = packageName;
    return this;
  }
  
  public JavaCodeGeneratorParameter setJUnitGeneration(final boolean generateJUnit) {
    this.junit = generateJUnit;
    return this;
  }
  
  public JavaCodeGeneratorParameter setUnitName(final String unitName) {
    this.unitName = unitName;
    return this;
  }
  
  public String javaChangeAdapterName(final boolean useInterface) {
    boolean _equals = Objects.equal(this.packageName, null);
    if (_equals) {
      return this.changeAdapterName(useInterface);
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.packageName);
    _builder.append(".");
    String _changeAdapterName = this.changeAdapterName(useInterface);
    _builder.append(_changeAdapterName);
    return _builder.toString();
  }
  
  public String changeAdapterName(final boolean useInterface) {
    String _xifexpression = null;
    if (useInterface) {
      _xifexpression = "GenericChangeAdapter";
    } else {
      _xifexpression = "ChangeAdapter";
    }
    return (_xifexpression + this.unitName);
  }
  
  public String javaClassName() {
    boolean _equals = Objects.equal(this.packageName, null);
    if (_equals) {
      return this.unitName;
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.packageName);
    _builder.append(".");
    _builder.append(this.unitName);
    return _builder.toString();
  }
}

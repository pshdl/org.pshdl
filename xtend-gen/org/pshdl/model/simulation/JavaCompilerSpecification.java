/**
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
 */
package org.pshdl.model.simulation;

import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;
import org.pshdl.interpreter.ExecutableModel;

@Data
@SuppressWarnings("all")
public class JavaCompilerSpecification {
  private final ExecutableModel _model;
  
  public ExecutableModel getModel() {
    return this._model;
  }
  
  private final String _pkg;
  
  public String getPkg() {
    return this._pkg;
  }
  
  private final String _unitName;
  
  public String getUnitName() {
    return this._unitName;
  }
  
  private final boolean _debug;
  
  public boolean isDebug() {
    return this._debug;
  }
  
  private final boolean _createCoverage;
  
  public boolean isCreateCoverage() {
    return this._createCoverage;
  }
  
  public JavaCompilerSpecification(final ExecutableModel model, final String pkg, final String unitName, final boolean debug, final boolean createCoverage) {
    super();
    this._model = model;
    this._pkg = pkg;
    this._unitName = unitName;
    this._debug = debug;
    this._createCoverage = createCoverage;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._model== null) ? 0 : this._model.hashCode());
    result = prime * result + ((this._pkg== null) ? 0 : this._pkg.hashCode());
    result = prime * result + ((this._unitName== null) ? 0 : this._unitName.hashCode());
    result = prime * result + (this._debug ? 1231 : 1237);
    result = prime * result + (this._createCoverage ? 1231 : 1237);
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JavaCompilerSpecification other = (JavaCompilerSpecification) obj;
    if (this._model == null) {
      if (other._model != null)
        return false;
    } else if (!this._model.equals(other._model))
      return false;
    if (this._pkg == null) {
      if (other._pkg != null)
        return false;
    } else if (!this._pkg.equals(other._pkg))
      return false;
    if (this._unitName == null) {
      if (other._unitName != null)
        return false;
    } else if (!this._unitName.equals(other._unitName))
      return false;
    if (other._debug != this._debug)
      return false;
    if (other._createCoverage != this._createCoverage)
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}

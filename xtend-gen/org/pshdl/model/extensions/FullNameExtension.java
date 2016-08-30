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
package org.pshdl.model.extensions;

import java.util.Arrays;
import java.util.Iterator;

import org.pshdl.model.HDLBlock;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.HDLQualifiedName;

import com.google.common.base.Optional;

/**
 * The FullNameExtension provides a {@link HDLQualifiedName} for every
 * IHDLObject. See {@link FullNameExtension#fullNameOf(IHDLObject)}
 *
 * @author Karsten Becker
 */
@SuppressWarnings("all")
public class FullNameExtension {
	/**
	 * This annotation is used to store {@link HDLQualifiedName} for the case
	 * that the resolution diverges from the actual tree
	 */
	public static HDLObject.GenericMeta<HDLQualifiedName> FULLNAME = new HDLObject.GenericMeta<HDLQualifiedName>("FULLNAME", true);

	private static FullNameExtension INST = new FullNameExtension();

	/**
	 * Returns the {@link HDLQualifiedName} for the given obj.
	 */
	public static HDLQualifiedName fullNameOf(final IHDLObject obj) {
		HDLQualifiedName _xblockexpression = null;
		{
			if ((obj == null))
				return null;
			final HDLQualifiedName cached = obj.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
			if ((cached != null))
				return cached;
			_xblockexpression = FullNameExtension.INST.getFullName(obj);
		}
		return _xblockexpression;
	}

	private static int countInstance(final HDLObject obj) {
		int count = 0;
		final IHDLObject _container = obj.getContainer();
		final boolean _tripleNotEquals = (_container != null);
		if (_tripleNotEquals) {
			final IHDLObject _container_1 = obj.getContainer();
			final Iterator<IHDLObject> iterator = _container_1.iterator();
			while (iterator.hasNext()) {
				{
					final IHDLObject hdlObject = iterator.next();
					if ((hdlObject == obj))
						return count;
					final HDLClass _classType = hdlObject.getClassType();
					final HDLClass _classType_1 = obj.getClassType();
					final boolean _tripleEquals = (_classType == _classType_1);
					if (_tripleEquals) {
						count = (count + 1);
					}
				}
			}
		}
		return count;
	}

	protected HDLQualifiedName _getFullName(final HDLForLoop loop) {
		final HDLQualifiedName cached = loop.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final HDLQualifiedName fullName = this.getSuperFullName(loop);
		final int count = FullNameExtension.countInstance(loop);
		final String _plus = (Character.valueOf(HDLQualifiedName.LOCAL_TYPE_SEP) + "for");
		final String _plus_1 = (_plus + Integer.valueOf(count));
		return fullName.append(_plus_1);
	}

	protected HDLQualifiedName _getFullName(final HDLBlock block) {
		final HDLQualifiedName cached = block.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final HDLQualifiedName fullName = this.getSuperFullName(block);
		final int count = FullNameExtension.countInstance(block);
		final String _plus = (Character.valueOf(HDLQualifiedName.LOCAL_TYPE_SEP) + "block");
		final String _plus_1 = (_plus + Integer.valueOf(count));
		return fullName.append(_plus_1);
	}

	protected HDLQualifiedName _getFullName(final HDLIfStatement ifStamnt) {
		final HDLQualifiedName cached = ifStamnt.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final HDLQualifiedName fullName = this.getSuperFullName(ifStamnt);
		final int count = FullNameExtension.countInstance(ifStamnt);
		final String _plus = (Character.valueOf(HDLQualifiedName.LOCAL_TYPE_SEP) + "if");
		final String _plus_1 = (_plus + Integer.valueOf(count));
		return fullName.append(_plus_1);
	}

	protected HDLQualifiedName _getFullName(final HDLSwitchStatement stmnt) {
		final HDLQualifiedName cached = stmnt.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final HDLQualifiedName fullName = this.getSuperFullName(stmnt);
		final int count = FullNameExtension.countInstance(stmnt);
		final String _plus = (Character.valueOf(HDLQualifiedName.LOCAL_TYPE_SEP) + "switch");
		final String _plus_1 = (_plus + Integer.valueOf(count));
		return fullName.append(_plus_1);
	}

	protected HDLQualifiedName _getFullName(final HDLSwitchCaseStatement stmnt) {
		final HDLQualifiedName cached = stmnt.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final HDLQualifiedName fullName = this.getSuperFullName(stmnt);
		final int count = FullNameExtension.countInstance(stmnt);
		final String _plus = (Character.valueOf(HDLQualifiedName.LOCAL_TYPE_SEP) + "case");
		final String _plus_1 = (_plus + Integer.valueOf(count));
		return fullName.append(_plus_1);
	}

	protected HDLQualifiedName _getFullName(final HDLUnit unit) {
		final HDLQualifiedName cached = unit.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final HDLQualifiedName fullName = this.getSuperFullName(unit);
		final String _name = unit.getName();
		final HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_name);
		return fullName.append(_hDLQualifiedName);
	}

	protected HDLQualifiedName _getFullName(final HDLInterface unit) {
		final HDLQualifiedName cached = unit.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final HDLQualifiedName fullName = this.getSuperFullName(unit);
		final String _name = unit.getName();
		final HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_name);
		return fullName.append(_hDLQualifiedName);
	}

	protected HDLQualifiedName _getFullName(final HDLEnum unit) {
		final HDLQualifiedName cached = unit.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final HDLQualifiedName fullName = this.getSuperFullName(unit);
		final String _name = unit.getName();
		final HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_name);
		return fullName.append(_hDLQualifiedName);
	}

	protected HDLQualifiedName _getFullName(final HDLFunction unit) {
		final HDLQualifiedName cached = unit.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final HDLQualifiedName fullName = this.getSuperFullName(unit);
		final String _name = unit.getName();
		final HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_name);
		return fullName.append(_hDLQualifiedName);
	}

	protected HDLQualifiedName _getFullName(final HDLPackage pkg) {
		final HDLQualifiedName cached = pkg.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final HDLQualifiedName fullName = this.getSuperFullName(pkg);
		final String _pkg = pkg.getPkg();
		final boolean _tripleNotEquals = (_pkg != null);
		if (_tripleNotEquals) {
			final String _pkg_1 = pkg.getPkg();
			final HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_pkg_1);
			return fullName.append(_hDLQualifiedName);
		}
		return fullName;
	}

	protected HDLQualifiedName _getFullName(final HDLEnumRef ref) {
		final HDLQualifiedName cached = ref.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final Optional<HDLVariable> varRef = ref.resolveVar();
		final boolean _isPresent = varRef.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return null;
		final HDLVariable _get = varRef.get();
		return this.getFullName(_get);
	}

	protected HDLQualifiedName _getFullName(final HDLVariableRef ref) {
		final HDLQualifiedName cached = ref.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final Optional<HDLVariable> varRef = ref.resolveVar();
		final boolean _isPresent = varRef.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return null;
		final HDLVariable _get = varRef.get();
		return this.getFullName(_get);
	}

	protected HDLQualifiedName _getFullName(final HDLFunctionCall call) {
		final HDLQualifiedName cached = call.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final Optional<HDLFunction> callRef = call.resolveFunction();
		final boolean _isPresent = callRef.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return null;
		final HDLFunction _get = callRef.get();
		return this.getFullName(_get);
	}

	protected HDLQualifiedName _getFullName(final HDLVariable unit) {
		final HDLQualifiedName cached = unit.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final HDLQualifiedName fullName = this.getSuperFullName(unit);
		final String _name = unit.getName();
		final HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_name);
		return fullName.append(_hDLQualifiedName);
	}

	protected HDLQualifiedName _getFullName(final IHDLObject obj) {
		final HDLQualifiedName cached = obj.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final IHDLObject _container = obj.getContainer();
		final boolean _tripleNotEquals = (_container != null);
		if (_tripleNotEquals) {
			final IHDLObject _container_1 = obj.getContainer();
			return this.getFullName(_container_1);
		}
		return HDLQualifiedName.EMPTY;
	}

	protected HDLQualifiedName _getFullName(final HDLObject obj) {
		final HDLQualifiedName cached = obj.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final IHDLObject _container = obj.getContainer();
		final boolean _tripleNotEquals = (_container != null);
		if (_tripleNotEquals) {
			final IHDLObject _container_1 = obj.getContainer();
			return this.getFullName(_container_1);
		}
		return HDLQualifiedName.EMPTY;
	}

	public HDLQualifiedName getSuperFullName(final HDLObject obj) {
		final HDLQualifiedName cached = obj.<HDLQualifiedName> getMeta(FullNameExtension.FULLNAME);
		if ((cached != null))
			return cached;
		final IHDLObject _container = obj.getContainer();
		final boolean _tripleNotEquals = (_container != null);
		if (_tripleNotEquals) {
			final IHDLObject _container_1 = obj.getContainer();
			final HDLQualifiedName fn = this.getFullName(_container_1);
			final IHDLObject _container_2 = obj.getContainer();
			if ((_container_2 instanceof HDLIfStatement)) {
				final IHDLObject _container_3 = obj.getContainer();
				final HDLIfStatement ifStmnt = ((HDLIfStatement) _container_3);
				final HDLIfStatement.TreeSide side = ifStmnt.treeSide(obj);
				if (side != null) {
					switch (side) {
					case thenTree:
						final String _string = fn.toString();
						final String _plus = (_string + "p");
						return new HDLQualifiedName(_plus);
					case elseTree:
						final String _string_1 = fn.toString();
						final String _plus_1 = (_string_1 + "n");
						return new HDLQualifiedName(_plus_1);
					default:
						break;
					}
				}
			}
			return fn;
		}
		return HDLQualifiedName.EMPTY;
	}

	public HDLQualifiedName getFullName(final IHDLObject unit) {
		if (unit instanceof HDLEnum)
			return _getFullName((HDLEnum) unit);
		else if (unit instanceof HDLEnumRef)
			return _getFullName((HDLEnumRef) unit);
		else if (unit instanceof HDLVariableRef)
			return _getFullName((HDLVariableRef) unit);
		else if (unit instanceof HDLBlock)
			return _getFullName((HDLBlock) unit);
		else if (unit instanceof HDLForLoop)
			return _getFullName((HDLForLoop) unit);
		else if (unit instanceof HDLFunction)
			return _getFullName((HDLFunction) unit);
		else if (unit instanceof HDLIfStatement)
			return _getFullName((HDLIfStatement) unit);
		else if (unit instanceof HDLInterface)
			return _getFullName((HDLInterface) unit);
		else if (unit instanceof HDLSwitchCaseStatement)
			return _getFullName((HDLSwitchCaseStatement) unit);
		else if (unit instanceof HDLSwitchStatement)
			return _getFullName((HDLSwitchStatement) unit);
		else if (unit instanceof HDLFunctionCall)
			return _getFullName((HDLFunctionCall) unit);
		else if (unit instanceof HDLPackage)
			return _getFullName((HDLPackage) unit);
		else if (unit instanceof HDLUnit)
			return _getFullName((HDLUnit) unit);
		else if (unit instanceof HDLVariable)
			return _getFullName((HDLVariable) unit);
		else if (unit instanceof HDLObject)
			return _getFullName((HDLObject) unit);
		else if (unit != null)
			return _getFullName(unit);
		else
			throw new IllegalArgumentException("Unhandled parameter types: " + Arrays.<Object> asList(unit).toString());
	}
}

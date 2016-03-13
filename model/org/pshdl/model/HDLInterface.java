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
package org.pshdl.model;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.impl.AbstractHDLInterface;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

import com.google.common.collect.Maps;

/**
 * The class HDLInterface contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLExpression&gt; dim. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLVariableDeclaration&gt; ports. Can be <code>null</code>.
 * </li>
 * </ul>
 */
public class HDLInterface extends AbstractHDLInterface {
	/**
	 * Constructs a new instance of {@link HDLInterface}
	 *
	 * @param id
	 *            a unique ID for this particular node
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dim
	 *            the value for dim. Can be <code>null</code>.
	 * @param ports
	 *            the value for ports. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLInterface(int id, @Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dim, @Nullable Iterable<HDLVariableDeclaration> ports,
			boolean validate) {
		super(id, container, name, dim, ports, validate);
	}

	public HDLInterface() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLInterface;
	}

	/**
	 * The accessor for the field ports which is of type
	 * ArrayList&lt;HDLVariableDeclaration&gt;.
	 */
	public static HDLFieldAccess<HDLInterface, ArrayList<HDLVariableDeclaration>> fPorts = new HDLFieldAccess<HDLInterface, ArrayList<HDLVariableDeclaration>>("ports",
			HDLVariableDeclaration.class, HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLVariableDeclaration> getValue(HDLInterface obj) {
			if (obj == null)
				return null;
			return obj.getPorts();
		}

		@Override
		public HDLInterface setValue(HDLInterface obj, ArrayList<HDLVariableDeclaration> value) {
			if (obj == null)
				return null;
			return obj.setPorts(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (ports.contains(obj))
			return fPorts;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$

	/**
	 * Returns <code>true</code> when all of this ports can be found in the
	 * given interface. When complementDirection is <code>true</code>, the
	 * direction will be inverted. Internals, constants and hidden variables are
	 * ignored.
	 *
	 * @param hdlInterface
	 * @param complementDirection
	 *            if <code>true</code> the directions of this interface will be
	 *            inverted
	 * @return <code>true</code> when all of this ports can be found in the
	 *         given interface
	 */
	public boolean conformsTo(HDLInterface hdlInterface, boolean complementDirection) {
		final ArrayList<HDLVariableDeclaration> thisPorts = getPorts();
		final Map<String, HDLVariable> thisPortsByName = Maps.newLinkedHashMap();
		outer: for (final HDLVariableDeclaration portDecl : thisPorts) {
			switch (portDecl.getDirection()) {
			case CONSTANT:
			case HIDDEN:
			case INTERNAL:
				continue outer;
			case IN:
			case INOUT:
			case PARAMETER:
			case OUT:
				break;
			}
			for (final HDLVariable hvar : portDecl.getVariables()) {
				thisPortsByName.put(hvar.getName(), hvar.setDefaultValue(null).setAnnotations(null));
			}
		}
		final ArrayList<HDLVariableDeclaration> otherPorts = hdlInterface.getPorts();
		outer: for (final HDLVariableDeclaration portDecl : otherPorts) {
			for (final HDLVariable hvar : portDecl.getVariables()) {
				final HDLVariable thisPort = thisPortsByName.get(hvar.getName());
				if (thisPort == null)
					return false;
				HDLDirection newDirection = portDecl.getDirection();
				switch (newDirection) {
				case CONSTANT:
				case HIDDEN:
				case INTERNAL:
					continue outer;
				case IN:
					if (complementDirection) {
						newDirection = HDLDirection.OUT;
					} else {
						newDirection = HDLDirection.IN;
					}
					break;
				case INOUT:
				case PARAMETER:
					break;
				case OUT:
					if (complementDirection) {
						newDirection = HDLDirection.IN;
					} else {
						newDirection = HDLDirection.OUT;
					}
					break;
				}
				if (hvar.getDirection() != newDirection)
					return false;
				if (!hvar.setAnnotations(null).setDefaultValue(null).equals(thisPort))
					return false;
				thisPortsByName.remove(hvar.getName());
			}
		}
		if (!thisPortsByName.isEmpty())
			return false;
		return true;
	}

	public static class AnyInterface extends HDLInterface {
	}

	public static AnyInterface anyInterface() {
		return new AnyInterface();
	}

	// $CONTENT-END$

}

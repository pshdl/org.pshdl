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
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.extensions.TypeExtension;
import org.pshdl.model.impl.AbstractHDLExport;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

/**
 * The class HDLExport contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName hIf. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLQualifiedName var. Can be <code>null</code>.</li>
 * <li>String match. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLExport extends AbstractHDLExport {
	/**
	 * Constructs a new instance of {@link HDLExport}
	 *
	 * @param id
	 *            a unique ID for this particular node
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param var
	 *            the value for var. Can be <code>null</code>.
	 * @param match
	 *            the value for match. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLExport(int id, @Nullable IHDLObject container, @Nonnull HDLQualifiedName hIf, @Nullable HDLQualifiedName var, @Nullable String match, boolean validate) {
		super(id, container, hIf, var, match, validate);
	}

	public HDLExport() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLExport;
	}

	/**
	 * The accessor for the field hIf which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLExport, HDLQualifiedName> fHIf = new HDLFieldAccess<HDLExport, HDLQualifiedName>("hIf", HDLQualifiedName.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLQualifiedName getValue(HDLExport obj) {
			if (obj == null)
				return null;
			return obj.getHIfRefName();
		}

		@Override
		public HDLExport setValue(HDLExport obj, HDLQualifiedName value) {
			if (obj == null)
				return null;
			return obj.setHIf(value);
		}
	};
	/**
	 * The accessor for the field var which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLExport, HDLQualifiedName> fVar = new HDLFieldAccess<HDLExport, HDLQualifiedName>("var", HDLQualifiedName.class,
			HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLQualifiedName getValue(HDLExport obj) {
			if (obj == null)
				return null;
			return obj.getVarRefName();
		}

		@Override
		public HDLExport setValue(HDLExport obj, HDLQualifiedName value) {
			if (obj == null)
				return null;
			return obj.setVar(value);
		}
	};
	/**
	 * The accessor for the field match which is of type String.
	 */
	public static HDLFieldAccess<HDLExport, String> fMatch = new HDLFieldAccess<HDLExport, String>("match", String.class, HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public String getValue(HDLExport obj) {
			if (obj == null)
				return null;
			return obj.getMatch();
		}

		@Override
		public HDLExport setValue(HDLExport obj, String value) {
			if (obj == null)
				return null;
			return obj.setMatch(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (hIf == obj)
			return fHIf;
		if (var == obj)
			return fVar;
		if (match == obj)
			return fMatch;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$

	public Optional<ArrayList<HDLInterfaceRef>> resolveVariables() {
		final Optional<HDLVariable> resolveHIf = resolveHIf();
		if (!resolveHIf.isPresent())
			return Optional.absent();
		final HDLVariable hIfVar = resolveHIf.get();
		final HDLQualifiedName hifRef = hIfVar.asRef();
		if (getVarRefName() != null) {
			final HDLInterfaceRef hir = new HDLInterfaceRef().setHIf(hifRef).setVar(getVarRefName());
			return Optional.of(Lists.newArrayList(hir));
		}
		final Optional<? extends HDLType> typeOf = TypeExtension.typeOf(hIfVar);
		if (!typeOf.isPresent())
			return Optional.absent();
		final HDLInterface hIf = (HDLInterface) typeOf.get();
		final ArrayList<HDLInterfaceRef> vars = Lists.newArrayList();
		final String matchName = getMatch();
		if (matchName != null) {
			if (matchName.startsWith("/")) {
				final String groupName = matchName.substring(1, matchName.length() - 1);
				for (final HDLVariableDeclaration hvd : hIf.getPorts()) {
					for (final HDLVariable hdlVariable : hvd.getVariables()) {
						final HDLAnnotation pg = hdlVariable.getAnnotation(HDLBuiltInAnnotations.portGroup);
						if ((pg != null) && groupName.equals(pg.getValue())) {
							vars.add(newIfRef(hifRef, hdlVariable));
						}
					}
				}
			} else {
				final String regex = matchName.replace("*", ".*?");
				final Pattern pattern = Pattern.compile(regex);
				for (final HDLVariableDeclaration hvd : hIf.getPorts()) {
					for (final HDLVariable hdlVariable : hvd.getVariables()) {
						if (pattern.matcher(hdlVariable.getName()).matches()) {
							vars.add(newIfRef(hifRef, hdlVariable));
						}
					}
				}
			}
		} else {
			for (final HDLVariableDeclaration hvd : hIf.getPorts()) {
				for (final HDLVariable hdlVariable : hvd.getVariables()) {
					vars.add(newIfRef(hifRef, hdlVariable));
				}
			}
		}
		return Optional.of(vars);
	}

	private HDLInterfaceRef newIfRef(final HDLQualifiedName hifRef, final HDLVariable hdlVariable) {
		return (HDLInterfaceRef) new HDLInterfaceRef().setHIf(hifRef).setVar(hdlVariable.asRef()).freeze(this);
	}

	public Optional<HDLInterfaceRef> toInterfaceRef() {
		if (getVarRefName() == null)
			return Optional.absent();
		return Optional.of((HDLInterfaceRef) new HDLInterfaceRef().setHIf(getHIfRefName()).setVar(getVarRefName()).freeze(this));
	}

	// $CONTENT-END$

}

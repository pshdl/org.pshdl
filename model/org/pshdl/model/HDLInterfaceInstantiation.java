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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.evaluation.ConstantEvaluate;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.impl.AbstractHDLInterfaceInstantiation;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.HDLQuery;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import org.pshdl.model.utils.Insulin;
import org.pshdl.model.utils.ModificationSet;

import com.google.common.base.Optional;

/**
 * The class HDLInterfaceInstantiation contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLAnnotation&gt; annotations. Can be <code>null</code>.</li>
 * <li>HDLVariable var. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLArgument&gt; arguments. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName hIf. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLInterfaceInstantiation extends AbstractHDLInterfaceInstantiation {
	/**
	 * Constructs a new instance of {@link HDLInterfaceInstantiation}
	 *
	 * @param id
	 *            a unique ID for this particular node
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLInterfaceInstantiation(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull HDLVariable var,
			@Nullable Iterable<HDLArgument> arguments, @Nonnull HDLQualifiedName hIf, boolean validate) {
		super(id, container, annotations, var, arguments, hIf, validate);
	}

	public HDLInterfaceInstantiation() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLInterfaceInstantiation;
	}

	/**
	 * The accessor for the field hIf which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLInterfaceInstantiation, HDLQualifiedName> fHIf = new HDLFieldAccess<HDLInterfaceInstantiation, HDLQualifiedName>("hIf", HDLQualifiedName.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLQualifiedName getValue(HDLInterfaceInstantiation obj) {
			if (obj == null) {
				return null;
			}
			return obj.getHIfRefName();
		}

		@Override
		public HDLInterfaceInstantiation setValue(HDLInterfaceInstantiation obj, HDLQualifiedName value) {
			if (obj == null) {
				return null;
			}
			return obj.setHIf(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (hIf == obj) {
			return fHIf;
		}
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$

	public static GenericMeta<String> ORIG_NAME = new GenericMeta<>("ORIG_NAME", true);

	@Override
	public Optional<HDLInterface> resolveHIf() {
		final Optional<HDLInterface> resolveHIf = super.resolveHIf();
		if (!resolveHIf.isPresent()) {
			return Optional.absent();
		}
		final ModificationSet ms = new ModificationSet();
		final HDLInterface getInterface = Insulin.resolveFragments(resolveHIf.get());
		final ArrayList<HDLVariableDeclaration> ports = getInterface.getPorts();
		final String prefix = getVar().getName();
		for (final HDLVariableDeclaration hvd : ports) {
			switch (hvd.getDirection()) {
			case PARAMETER: {
				final ArrayList<HDLVariable> variables = hvd.getVariables();
				for (final HDLVariable hdlVariable : variables) {
					final String newName = prefix + "_" + hdlVariable.getName();
					final HDLVariable newVar = hdlVariable.setName(newName);
					newVar.addMeta(ORIG_NAME, hdlVariable.getName());
					ms.replace(hdlVariable, newVar);
					final Collection<HDLVariableRef> refs = HDLQuery.select(HDLVariableRef.class).from(getInterface).where(HDLResolvedRef.fVar).isEqualTo(hdlVariable.asRef())
							.getAll();
					for (final HDLVariableRef ref : refs) {
						// Make local only so that it is resolved locally first
						ms.replace(ref, ref.setVar(HDLQualifiedName.create(newName)));
					}
				}
				break;
			}
			case CONSTANT: {
				final ArrayList<HDLVariable> variables = hvd.getVariables();
				for (final HDLVariable hdlVariable : variables) {
					final Optional<BigInteger> constant = ConstantEvaluate.valueOf(hdlVariable.getDefaultValue());
					if (!constant.isPresent()) {
						if (hdlVariable.getDefaultValue() instanceof HDLArrayInit) {
							final HDLArrayInit hdlArrayInit = (HDLArrayInit) hdlVariable.getDefaultValue();
							inlineConstants(ms, hdlArrayInit);
						}
					} else {
						final Collection<HDLVariableRef> refs = HDLQuery.select(HDLVariableRef.class).from(getInterface).where(HDLResolvedRef.fVar).isEqualTo(hdlVariable.asRef())
								.getAll();
						for (final HDLVariableRef ref : refs) {
							ms.replace(ref, HDLLiteral.get(constant.get()));
						}
					}
				}
				break;
			}
			default:
				break;

			}
		}
		return Optional.of(ms.apply(getInterface));
	}

	private void inlineConstants(ModificationSet ms, HDLArrayInit hdlArrayInit) {
		for (final HDLExpression exp : hdlArrayInit.getExp()) {
			final Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(exp);
			if (valueOf.isPresent()) {
				ms.replace(exp, HDLLiteral.get(valueOf.get()));
			} else {
				if (exp instanceof HDLArrayInit) {
					final HDLArrayInit hai = (HDLArrayInit) exp;
					inlineConstants(ms, hai);
				} else {
					throw new IllegalArgumentException(String.format("The evaluation of a constant should always return a constant. The constant was:%s", exp));
				}
			}
		}
	}

	public HDLEvaluationContext getContext(HDLEvaluationContext defaultContext) {
		final Map<String, HDLExpression> map = defaultContext.getMap();
		for (final HDLArgument arg : getArguments()) {
			map.put(arg.getName(), arg.getExpression());
		}
		return new HDLEvaluationContext(map);
	}

	// $CONTENT-END$

}

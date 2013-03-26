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
package de.tuhh.ict.pshdl.model;

import java.math.*;
import java.util.*;

import javax.annotation.*;

import com.google.common.base.*;

import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLInterfaceInstantiation contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLVariable var. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLArgument> arguments. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName hIf. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLInterfaceInstantiation extends AbstractHDLInterfaceInstantiation {
	/**
	 * Constructs a new instance of {@link HDLInterfaceInstantiation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLInterfaceInstantiation(@Nullable IHDLObject container, @Nonnull HDLVariable var, @Nullable Iterable<HDLArgument> arguments, @Nonnull HDLQualifiedName hIf,
			boolean validate) {
		super(container, var, arguments, hIf, validate);
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
	public static HDLFieldAccess<HDLInterfaceInstantiation, HDLQualifiedName> fHIf = new HDLFieldAccess<HDLInterfaceInstantiation, HDLQualifiedName>("hIf") {
		@Override
		public HDLQualifiedName getValue(HDLInterfaceInstantiation obj) {
			if (obj == null)
				return null;
			return obj.getHIfRefName();
		}
	};
	// $CONTENT-BEGIN$

	public static GenericMeta<String> ORIG_NAME = new GenericMeta<String>("ORIG_NAME", true);

	@Override
	public Optional<HDLInterface> resolveHIf() {
		Optional<HDLInterface> resolveHIf = super.resolveHIf();
		if (!resolveHIf.isPresent())
			return Optional.absent();
		ModificationSet ms = new ModificationSet();
		HDLInterface getInterface = resolveHIf.get();
		ArrayList<HDLVariableDeclaration> ports = getInterface.getPorts();
		String prefix = getVar().getName();
		for (HDLVariableDeclaration hvd : ports) {
			switch (hvd.getDirection()) {
			case PARAMETER: {
				ArrayList<HDLVariable> variables = hvd.getVariables();
				for (HDLVariable hdlVariable : variables) {
					String newName = prefix + "_" + hdlVariable.getName();
					HDLVariable newVar = hdlVariable.setName(newName);
					newVar.addMeta(ORIG_NAME, hdlVariable.getName());
					ms.replace(hdlVariable, newVar);
					Collection<HDLVariableRef> refs = HDLQuery.select(HDLVariableRef.class).from(getInterface).where(HDLResolvedRef.fVar).isEqualTo(hdlVariable.asRef()).getAll();
					for (HDLVariableRef ref : refs) {
						// Make local only so that it is resolved locally first
						ms.replace(ref, ref.setVar(HDLQualifiedName.create(newName)));
					}
				}
				break;
			}
			case CONSTANT: {
				ArrayList<HDLVariable> variables = hvd.getVariables();
				for (HDLVariable hdlVariable : variables) {
					Optional<BigInteger> constant = ConstantEvaluate.valueOf(hdlVariable.getDefaultValue());
					if (!constant.isPresent()) {
						if (hdlVariable.getDefaultValue() instanceof HDLArrayInit) {
							HDLArrayInit hdlArrayInit = (HDLArrayInit) hdlVariable.getDefaultValue();
							inlineConstants(ms, hdlArrayInit);
						} else
							throw new IllegalArgumentException(String.format("The evaluation of a constant should always return a constant. The constant was:%s", hdlVariable));
					} else {
						Collection<HDLVariableRef> refs = HDLQuery.select(HDLVariableRef.class).from(getInterface).where(HDLResolvedRef.fVar).isEqualTo(hdlVariable.asRef())
								.getAll();
						for (HDLVariableRef ref : refs) {
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
		for (HDLExpression exp : hdlArrayInit.getExp()) {
			Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(exp);
			if (valueOf.isPresent()) {
				ms.replace(exp, HDLLiteral.get(valueOf.get()));
			} else {
				if (exp instanceof HDLArrayInit) {
					HDLArrayInit hai = (HDLArrayInit) exp;
					inlineConstants(ms, hai);
				} else
					throw new IllegalArgumentException(String.format("The evaluation of a constant should always return a constant. The constant was:%s", exp));
			}
		}
	}

	// $CONTENT-END$

}

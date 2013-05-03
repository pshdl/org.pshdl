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
package org.pshdl.model;

import java.util.*;

import javax.annotation.*;

import org.pshdl.model.impl.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLFunctionParameter> args. Can be <code>null</code>.</li>
 * <li>HDLFunctionParameter returnType. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLFunction extends AbstractHDLFunction {
	/**
	 * Constructs a new instance of {@link HDLFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param args
	 *            the value for args. Can be <code>null</code>.
	 * @param returnType
	 *            the value for returnType. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLFunction(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String name, @Nullable Iterable<HDLFunctionParameter> args,
			@Nullable HDLFunctionParameter returnType, boolean validate) {
		super(id, container, annotations, name, args, returnType, validate);
	}

	public HDLFunction() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLFunction;
	}

	/**
	 * The accessor for the field name which is of type String.
	 */
	public static HDLFieldAccess<HDLFunction, String> fName = new HDLFieldAccess<HDLFunction, String>("name") {
		@Override
		public String getValue(HDLFunction obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};
	/**
	 * The accessor for the field args which is of type
	 * ArrayList<HDLFunctionParameter>.
	 */
	public static HDLFieldAccess<HDLFunction, ArrayList<HDLFunctionParameter>> fArgs = new HDLFieldAccess<HDLFunction, ArrayList<HDLFunctionParameter>>("args") {
		@Override
		public ArrayList<HDLFunctionParameter> getValue(HDLFunction obj) {
			if (obj == null)
				return null;
			return obj.getArgs();
		}
	};
	/**
	 * The accessor for the field returnType which is of type
	 * HDLFunctionParameter.
	 */
	public static HDLFieldAccess<HDLFunction, HDLFunctionParameter> fReturnType = new HDLFieldAccess<HDLFunction, HDLFunctionParameter>("returnType") {
		@Override
		public HDLFunctionParameter getValue(HDLFunction obj) {
			if (obj == null)
				return null;
			return obj.getReturnType();
		}
	};
	// $CONTENT-BEGIN$

	public static final GenericMeta<IHDLObject> META = new GenericMeta<IHDLObject>("INLINED_FROM", true);

	@Nonnull
	public <T extends IHDLObject> T substitute(Iterable<HDLFunctionParameter> paraneter, Iterable<HDLExpression> arguments, T stmnt, IHDLObject origin) {
		ModificationSet msExp = new ModificationSet();
		@SuppressWarnings("unchecked")
		T orig = (T) stmnt.copyFiltered(CopyFilter.DEEP_META);
		Iterator<HDLExpression> argIter = arguments.iterator();
		for (HDLFunctionParameter param : args) {
			if (!argIter.hasNext()) {
				continue;
			}
			HDLExpression arg = argIter.next();
			Collection<HDLVariableRef> allArgRefs = HDLQuery.select(HDLVariableRef.class).from(orig).where(HDLResolvedRef.fVar).lastSegmentIs(param.getName().getName()).getAll();
			for (HDLVariableRef argRef : allArgRefs) {
				HDLExpression exp = arg.copyFiltered(CopyFilter.DEEP_META);
				if ((argRef.getBits().size() != 0) || (argRef.getArray().size() != 0)) {
					if (exp instanceof HDLVariableRef) {
						HDLVariableRef ref = (HDLVariableRef) exp;
						HDLVariableRef nref = ref;
						for (HDLRange bit : argRef.getBits()) {
							nref = nref.addBits(substitute(args, arguments, bit, origin));
						}
						for (HDLExpression aExp : argRef.getArray()) {
							nref = nref.addArray(substitute(args, arguments, aExp, origin));
						}
						msExp.replace(argRef, nref);
					} else {
						msExp.replace(argRef, exp);
					}
				} else {
					msExp.replace(argRef, exp);
				}
			}
		}
		T newExp = msExp.apply(orig);
		Iterator<IHDLObject> iterator = newExp.deepIterator();
		while (iterator.hasNext()) {
			IHDLObject obj = iterator.next();
			obj.addMeta(META, origin);
		}
		newExp.addMeta(META, origin);
		return newExp;
	}
	// $CONTENT-END$

}
package de.tuhh.ict.pshdl.model;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.builtin.*;

/**
 * The class HDLFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
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
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLFunction(@Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String name, boolean validate) {
		super(container, annotations, name, validate);
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
	// $CONTENT-BEGIN$

	public static final String META = "INLINED_FROM";

	@Nonnull
	public <T extends IHDLObject> T substitute(Iterable<HDLVariable> paraneter, Iterable<HDLExpression> arguments, T stmnt, IHDLObject origin) {
		ModificationSet msExp = new ModificationSet();
		@SuppressWarnings("unchecked")
		T orig = (T) stmnt.copyFiltered(CopyFilter.DEEP_META);
		Iterator<HDLExpression> argIter = arguments.iterator();
		for (HDLVariable param : paraneter) {
			if (!argIter.hasNext())
				throw new HDLProblemException(new Problem(ErrorCode.FUNCTION_NOT_ENOUGH_ARGUMENTS, this));
			HDLExpression arg = argIter.next();
			Collection<HDLVariableRef> allArgRefs = HDLQuery.select(HDLVariableRef.class).from(orig).where(HDLResolvedRef.fVar).lastSegmentIs(param.getName()).getAll();
			for (HDLVariableRef argRef : allArgRefs) {
				HDLExpression exp = arg.copyFiltered(CopyFilter.DEEP_META);
				if ((argRef.getBits().size() != 0) || (argRef.getArray().size() != 0)) {
					if (exp instanceof HDLVariableRef) {
						HDLVariableRef ref = (HDLVariableRef) exp;
						HDLVariableRef nref = ref;
						for (HDLRange bit : argRef.getBits()) {
							nref = nref.addBits(substitute(paraneter, arguments, bit, origin));
						}
						for (HDLExpression aExp : argRef.getArray()) {
							nref = nref.addArray(substitute(paraneter, arguments, aExp, origin));
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
		Iterator<IHDLObject> iterator = newExp.iterator(true);
		while (iterator.hasNext()) {
			IHDLObject obj = iterator.next();
			obj.addMeta(META, origin);
		}
		newExp.addMeta(META, origin);
		return newExp;
	}
	// $CONTENT-END$

}

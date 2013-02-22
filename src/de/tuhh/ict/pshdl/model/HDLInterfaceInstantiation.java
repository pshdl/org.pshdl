package de.tuhh.ict.pshdl.model;

import static com.google.common.base.Preconditions.*;

import java.math.*;
import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.extensions.*;
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
	public HDLInterfaceInstantiation(@Nullable IHDLObject container, @Nonnull HDLVariable var, @Nullable ArrayList<HDLArgument> arguments, @Nonnull HDLQualifiedName hIf,
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
	public HDLInterface resolveHIf() {
		HDLInterface resolveHIf = super.resolveHIf();
		if (resolveHIf == null)
			return null;
		ModificationSet ms = new ModificationSet();
		ArrayList<HDLVariableDeclaration> ports = resolveHIf.getPorts();
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
					Collection<HDLVariableRef> refs = HDLQuery.select(HDLVariableRef.class).from(resolveHIf).where(HDLResolvedRef.fVar).isEqualTo(hdlVariable.asRef()).getAll();
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
					BigInteger constant = checkNotNull(ConstantEvaluate.valueOf(hdlVariable.getDefaultValue()),
							"The evaluation of a constant should always return a constant. The constant was:%s", hdlVariable);
					Collection<HDLVariableRef> refs = HDLQuery.select(HDLVariableRef.class).from(resolveHIf).where(HDLResolvedRef.fVar).isEqualTo(hdlVariable.asRef()).getAll();
					for (HDLVariableRef ref : refs) {
						ms.replace(ref, HDLLiteral.get(constant));
					}
				}
				break;
			}
			default:
				break;

			}
		}
		HDLInterface newIF = ms.apply(resolveHIf);
		return newIF;
	}

	// $CONTENT-END$

}

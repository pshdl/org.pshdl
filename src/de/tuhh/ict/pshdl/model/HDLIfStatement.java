package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import de.tuhh.ict.pshdl.model.utils.*;

/**
 * The class HDLIfStatement contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression ifExp. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> thenDo. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> elseDo. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLIfStatement extends AbstractHDLIfStatement {
	/**
	 * Constructs a new instance of {@link HDLIfStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param ifExp
	 *            the value for ifExp. Can <b>not</b> be <code>null</code>.
	 * @param thenDo
	 *            the value for thenDo. Can be <code>null</code>.
	 * @param elseDo
	 *            the value for elseDo. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLIfStatement(HDLObject container, HDLExpression ifExp, ArrayList<HDLStatement> thenDo, ArrayList<HDLStatement> elseDo, boolean validate) {
		super(container, ifExp, thenDo, elseDo, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLIfStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param ifExp
	 *            the value for ifExp. Can <b>not</b> be <code>null</code>.
	 * @param thenDo
	 *            the value for thenDo. Can be <code>null</code>.
	 * @param elseDo
	 *            the value for elseDo. Can be <code>null</code>.
	 */
	public HDLIfStatement(HDLObject container, HDLExpression ifExp, ArrayList<HDLStatement> thenDo, ArrayList<HDLStatement> elseDo) {
		this(container, ifExp, thenDo, elseDo, true);
	}

	public HDLIfStatement() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLIfStatement;
	}

	public static HDLFieldAccess<HDLIfStatement, HDLExpression> fIfExp = new HDLFieldAccess<HDLIfStatement, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLIfStatement obj) {
			if (obj == null)
				return null;
			return obj.getIfExp();
		}
	};
	public static HDLFieldAccess<HDLIfStatement, ArrayList<HDLStatement>> fThenDo = new HDLFieldAccess<HDLIfStatement, ArrayList<HDLStatement>>() {
		@Override
		public ArrayList<HDLStatement> getValue(HDLIfStatement obj) {
			if (obj == null)
				return null;
			return obj.getThenDo();
		}
	};
	public static HDLFieldAccess<HDLIfStatement, ArrayList<HDLStatement>> fElseDo = new HDLFieldAccess<HDLIfStatement, ArrayList<HDLStatement>>() {
		@Override
		public ArrayList<HDLStatement> getValue(HDLIfStatement obj) {
			if (obj == null)
				return null;
			return obj.getElseDo();
		}
	};

	// $CONTENT-BEGIN$
	@Override
	public List<HDLEnumDeclaration> doGetEnumDeclarations() {
		List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
		res.addAll(HDLResolver.getallEnumDeclarations(thenDo));
		res.addAll(HDLResolver.getallEnumDeclarations(elseDo));
		return res;
	}

	@Override
	public List<HDLInterface> doGetInterfaceDeclarations() {
		List<HDLInterface> res = new LinkedList<HDLInterface>();
		res.addAll(HDLResolver.getallInterfaceDeclarations(thenDo));
		res.addAll(HDLResolver.getallInterfaceDeclarations(elseDo));
		return res;
	}

	@Override
	public List<HDLVariableDeclaration> doGetVariableDeclarations() {
		List<HDLVariableDeclaration> res = new LinkedList<HDLVariableDeclaration>();
		res.addAll(HDLResolver.getallVariableDeclarations(thenDo));
		res.addAll(HDLResolver.getallVariableDeclarations(elseDo));
		return res;
	}
	// $CONTENT-END$

}

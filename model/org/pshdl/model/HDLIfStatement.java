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

import org.pshdl.model.impl.AbstractHDLIfStatement;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

import com.google.common.collect.Maps;

/**
 * The class HDLIfStatement contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression ifExp. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLStatement&gt; thenDo. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLStatement&gt; elseDo. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLIfStatement extends AbstractHDLIfStatement {
	/**
	 * Constructs a new instance of {@link HDLIfStatement}
	 *
	 * @param id
	 *            a unique ID for this particular node
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
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLIfStatement(int id, @Nullable IHDLObject container, @Nonnull HDLExpression ifExp, @Nullable Iterable<HDLStatement> thenDo, @Nullable Iterable<HDLStatement> elseDo,
			boolean validate) {
		super(id, container, ifExp, thenDo, elseDo, validate);
	}

	public HDLIfStatement() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLIfStatement;
	}

	/**
	 * The accessor for the field ifExp which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLIfStatement, HDLExpression> fIfExp = new HDLFieldAccess<HDLIfStatement, HDLExpression>("ifExp", HDLExpression.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLExpression getValue(HDLIfStatement obj) {
			if (obj == null)
				return null;
			return obj.getIfExp();
		}

		@Override
		public HDLIfStatement setValue(HDLIfStatement obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setIfExp(value);
		}
	};
	/**
	 * The accessor for the field thenDo which is of type
	 * ArrayList&lt;HDLStatement&gt;.
	 */
	public static HDLFieldAccess<HDLIfStatement, ArrayList<HDLStatement>> fThenDo = new HDLFieldAccess<HDLIfStatement, ArrayList<HDLStatement>>("thenDo", HDLStatement.class,
			HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLStatement> getValue(HDLIfStatement obj) {
			if (obj == null)
				return null;
			return obj.getThenDo();
		}

		@Override
		public HDLIfStatement setValue(HDLIfStatement obj, ArrayList<HDLStatement> value) {
			if (obj == null)
				return null;
			return obj.setThenDo(value);
		}
	};
	/**
	 * The accessor for the field elseDo which is of type
	 * ArrayList&lt;HDLStatement&gt;.
	 */
	public static HDLFieldAccess<HDLIfStatement, ArrayList<HDLStatement>> fElseDo = new HDLFieldAccess<HDLIfStatement, ArrayList<HDLStatement>>("elseDo", HDLStatement.class,
			HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLStatement> getValue(HDLIfStatement obj) {
			if (obj == null)
				return null;
			return obj.getElseDo();
		}

		@Override
		public HDLIfStatement setValue(HDLIfStatement obj, ArrayList<HDLStatement> value) {
			if (obj == null)
				return null;
			return obj.setElseDo(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (ifExp == obj)
			return fIfExp;
		if (thenDo.contains(obj))
			return fThenDo;
		if (elseDo.contains(obj))
			return fElseDo;
		return super.getContainingFeature(obj);
	}

	// $CONTENT-BEGIN$
	public static enum TreeSide {
		none, thenTree, elseTree, idConflict
	}

	private Map<Integer, TreeSide> treeSides;

	public TreeSide treeSide(IHDLObject stmnt) {
		if (treeSides == null) {
			treeSides = Maps.newLinkedHashMap();
			for (final HDLStatement t : thenDo) {
				final int tid = t.getID();
				final TreeSide put = treeSides.put(tid, TreeSide.thenTree);
				if (put != null) {
					treeSides.put(tid, TreeSide.idConflict);
				}
			}
			for (final HDLStatement t : elseDo) {
				final int tid = t.getID();
				final TreeSide put = treeSides.put(tid, TreeSide.elseTree);
				if (put != null) {
					treeSides.put(tid, TreeSide.idConflict);
				}
			}
		}
		final TreeSide side = treeSides.get(stmnt.getID());
		if (side == TreeSide.idConflict) {
			for (final HDLStatement t : thenDo)
				if (t == stmnt)
					return TreeSide.thenTree;
			for (final HDLStatement t : elseDo)
				if (t == stmnt)
					return TreeSide.elseTree;
		}
		if (side != null)
			return side;
		return TreeSide.none;
	}

	// $CONTENT-END$

}

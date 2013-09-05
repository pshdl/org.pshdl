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
package org.pshdl.model.utils.internal;

import gnu.trove.set.hash.*;

import java.util.*;

import com.google.common.collect.*;

public class NonSameList<T> extends AbstractSet<T> implements Set<T>, Cloneable {
	private final TIntHashSet index = new TIntHashSet();
	private final List<T> data = Lists.newLinkedList();

	public NonSameList(Collection<T> value) {
		super();
		addAll(value);
	}

	public NonSameList() {
	}

	@Override
	public boolean add(T item) {
		final boolean add = index.add(System.identityHashCode(item));
		if (add) {
			data.add(item);
		}
		return add;
	}

	@Override
	public void clear() {
		index.clear();
		data.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return index.contains(System.identityHashCode(arg0));
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public NonSameList<T> clone() {
		return new NonSameList<T>(this);
	}

	@Override
	public Iterator<T> iterator() {
		return data.iterator();
	}

	@Override
	public boolean remove(Object arg0) {
		final boolean remove = index.remove(System.identityHashCode(arg0));
		if (remove) {
			data.remove(arg0);
		}
		return remove;
	}

	@Override
	public int size() {
		return index.size();
	}

}

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

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class NonSameList<T> extends AbstractSet<T> implements Set<T>, Cloneable {
	private final Map<T, T> map = Maps.newIdentityHashMap();
	private final List<T> additions = Lists.newArrayList();

	public NonSameList(Collection<T> value) {
		super();
		addAll(value);
	}

	public NonSameList() {
	}

	@Override
	public boolean add(T item) {
		final T old = map.put(item, item);
		final boolean added = old != item;
		if (added) {
			if (old != null) {
				System.err.println("Collision");
			} else {
				additions.add(item);
			}
		}
		return added;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return map.containsKey(arg0);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public NonSameList<T> clone() {
		return new NonSameList<>(this);
	}

	@Override
	public Iterator<T> iterator() {
		return additions.iterator();
	}

	@Override
	public boolean remove(Object arg0) {
		final T removedItem = map.remove(arg0);
		final boolean removed = removedItem == arg0;
		if (removed) {
			additions.remove(arg0);
		}
		return removed;
	}

	@Override
	public int size() {
		return map.size();
	}

}

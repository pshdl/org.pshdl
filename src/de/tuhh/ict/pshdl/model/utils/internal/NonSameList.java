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
package de.tuhh.ict.pshdl.model.utils.internal;

import java.util.*;

public class NonSameList<T> extends AbstractSet<T> implements Set<T>, Cloneable {
	private Map<Integer, List<T>> map = new LinkedHashMap<Integer, List<T>>();

	public NonSameList(Collection<T> value) {
		super();
		addAll(value);
	}

	public NonSameList() {
	}

	@Override
	public boolean add(T arg0) {
		List<T> list = map.get(System.identityHashCode(arg0));
		if (list == null) {
			list = new LinkedList<T>();
			list.add(arg0);
			map.put(System.identityHashCode(arg0), list);
		} else {
			for (T t : list)
				if (t == arg0)
					return false;
			list.add(arg0);
		}
		return true;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		List<T> list = map.get(System.identityHashCode(arg0));
		if (list == null)
			return false;
		for (T t : list)
			if (t == arg0)
				return true;
		return false;
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty() || (size() == 0);
	}

	@Override
	public NonSameList<T> clone() {
		return new NonSameList<T>(this);
	}

	private static class MultiListIterator<T> implements Iterator<T> {
		private Iterator<Iterator<T>> delegate;
		private Iterator<T> current;

		public MultiListIterator(List<Iterator<T>> iters) {
			super();
			delegate = iters.iterator();
			if (delegate.hasNext()) {
				current = delegate.next();
			}
		}

		@Override
		public boolean hasNext() {
			if (current == null)
				return false;
			if (current.hasNext())
				return true;
			while (delegate.hasNext()) {
				current = delegate.next();
				if (current.hasNext())
					return true;
			}
			return false;
		}

		@Override
		public T next() {
			return current.next();
		}

		@Override
		public void remove() {
			current.remove();
		}

	}

	@Override
	public Iterator<T> iterator() {
		LinkedList<Iterator<T>> iters = new LinkedList<Iterator<T>>();
		for (List<T> lst : map.values()) {
			iters.add(lst.iterator());
		}
		return new MultiListIterator<T>(iters);
	}

	@Override
	public boolean remove(Object arg0) {
		List<T> list = map.get(System.identityHashCode(arg0));
		if (list == null)
			return false;
		return list.remove(arg0);
	}

	@Override
	public int size() {
		int count = 0;
		for (List<T> lsts : map.values()) {
			count += lsts.size();
		}
		return count;
	}

}

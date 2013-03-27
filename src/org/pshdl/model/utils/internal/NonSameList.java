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

import java.util.*;

public class NonSameList<T> extends AbstractSet<T> implements Set<T>, Cloneable {
	private Map<Integer, T> map = new LinkedHashMap<Integer, T>();

	public NonSameList(Collection<T> value) {
		super();
		addAll(value);
	}

	public NonSameList() {
	}

	@Override
	public boolean add(T item) {
		T old = map.put(System.identityHashCode(item), item);
		return old != item;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return map.containsKey(System.identityHashCode(arg0));
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public NonSameList<T> clone() {
		return new NonSameList<T>(this);
	}

	@Override
	public Iterator<T> iterator() {
		return map.values().iterator();
	}

	@Override
	public boolean remove(Object arg0) {
		return map.remove(System.identityHashCode(arg0)) != null;
	}

	@Override
	public int size() {
		return map.size();
	}

}

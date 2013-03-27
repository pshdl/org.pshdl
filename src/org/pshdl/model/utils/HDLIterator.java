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
package org.pshdl.model.utils;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.pshdl.model.*;

public class HDLIterator implements Iterator<IHDLObject> {
	private final class SingleObjectIterator implements Iterator<IHDLObject> {
		private HDLObject hdo;

		private SingleObjectIterator(HDLObject hdo) {
			this.hdo = hdo;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public HDLObject next() {
			if (hdo == null)
				throw new NoSuchElementException();
			HDLObject res = hdo;
			hdo = null;
			return res;
		}

		@Override
		public boolean hasNext() {
			return hdo != null;
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface Visit {

	}

	private List<Iterator<IHDLObject>> iters = new LinkedList<Iterator<IHDLObject>>();
	private Iterator<Iterator<IHDLObject>> delegate;
	private Iterator<IHDLObject> current;

	public HDLIterator(HDLObject obj, boolean deep) throws IllegalArgumentException, IllegalAccessException {
		Class<? extends HDLObject> clazz = obj.getClass();
		addAllFields(obj, clazz, deep);
		delegate = iters.iterator();
		if (delegate.hasNext()) {
			current = delegate.next();
		} else {
			current = new SingleObjectIterator(null);
		}
	}

	private void addAllFields(HDLObject obj, Class<?> clazz, boolean deep) throws IllegalAccessException {
		Class<?> superClazz = clazz.getSuperclass();
		if ((superClazz != null) && !superClazz.getName().endsWith("HDLObject")) {
			addAllFields(obj, superClazz, deep);
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields)
			if (field.getAnnotation(Visit.class) != null) {
				field.setAccessible(true);
				Object object = field.get(obj);
				if (object instanceof HDLObject) {
					final HDLObject hdo = (HDLObject) object;
					if (hdo != null) {
						iters.add(new SingleObjectIterator(hdo));
					}
				}
				if (object instanceof Collection) {
					@SuppressWarnings("unchecked")
					Collection<HDLObject> chdo = (Collection<HDLObject>) object;
					if (deep) {
						for (HDLObject hdlObject : chdo) {
							iters.add(hdlObject.iterator(deep));
						}
					} else {
						for (HDLObject hdlObject : chdo) {
							iters.add(new SingleObjectIterator(hdlObject));
						}
					}
				}
			}
	}

	@Override
	public boolean hasNext() {
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
	public IHDLObject next() {
		return current.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}

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

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;

import javax.annotation.*;

import org.pshdl.model.extensions.*;
import org.pshdl.model.impl.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import org.pshdl.model.utils.internal.*;

import com.google.common.base.*;

/**
 * The class HDLObject contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLObject extends AbstractHDLObject implements org.pshdl.model.IHDLObject {
	/**
	 * Constructs a new instance of {@link HDLObject}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLObject(@Nullable IHDLObject container, boolean validate) {
		super(container, validate);
	}

	public HDLObject() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLObject;
	}

	/**
	 * The accessor for the field container which is of type IHDLObject.
	 */
	public static HDLFieldAccess<HDLObject, IHDLObject> fContainer = new HDLFieldAccess<HDLObject, IHDLObject>("container") {
		@Override
		public IHDLObject getValue(HDLObject obj) {
			if (obj == null)
				return null;
			return obj.getContainer();
		}
	};

	// $CONTENT-BEGIN$

	public static void copyMetaData(IHDLObject src, IHDLObject target) {
		copyMetaData(src, target, false);
	}

	public static void copyMetaData(IHDLObject src, IHDLObject target, boolean all) {
		Map<MetaAccess<?>, Object> srcMeta = ((HDLObject) src).metaData;
		Map<MetaAccess<?>, Object> targetMeta = ((HDLObject) target).metaData;
		for (Entry<MetaAccess<?>, Object> entry : srcMeta.entrySet())
			if (all || entry.getKey().inherit()) {
				targetMeta.put(entry.getKey(), entry.getValue());
			}
	}

	@Override
	@Nonnull
	public abstract IHDLObject copyFiltered(CopyFilter filter);

	@Override
	@Nonnull
	public IHDLObject copyDeepFrozen(IHDLObject container) {
		IHDLObject copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	public Map<MetaAccess<?>, Object> metaData = new HashMap<MetaAccess<?>, Object>();

	public static class GenericMeta<T> implements MetaAccess<T> {

		private boolean inherit;
		private String name;

		public GenericMeta(String name, boolean inherit) {
			this.name = name;
			this.inherit = inherit;
		}

		@Override
		public String toString() {
			return "GenericMeta [inherit=" + inherit + ", name=" + name + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + (inherit ? 1231 : 1237);
			result = (prime * result) + (name == null ? 0 : name.hashCode());
			return result;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GenericMeta other = (GenericMeta) obj;
			if (inherit != other.inherit)
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public boolean inherit() {
			return inherit;
		}

	}

	@Override
	public <K> void addMeta(MetaAccess<K> key, K value) {
		metaData.put(key, value);
	}

	@Override
	public void setMeta(MetaAccess<Boolean> meta) {
		addMeta(meta, true);
	}

	@Override
	public boolean hasMeta(MetaAccess<?> key) {
		return getMeta(key) != null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <K> K getMeta(MetaAccess<K> key) {
		return (K) metaData.get(key);
	}

	@Nonnull
	public static <T> Iterable<T> asList(T items) {
		return Collections.singleton(items);
	}

	@Nonnull
	public static <T> Iterable<T> asList(T... items) {
		ArrayList<T> res = new ArrayList<T>();
		for (T t : items) {
			res.add(t);
		}
		return res;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (hashCode() != obj.hashCode())
			return false;
		if (!(obj instanceof HDLObject))
			return false;
		return true;
	}

	@Override
	public <T> T[] getAllObjectsOf(Class<? extends T> clazz, boolean deep) {
		HDLClass classFor = HDLClass.getClassFor(clazz);
		if (classFor == null)
			throw new IllegalArgumentException("Unkown class:" + clazz);
		return getAllObjectsOf(classFor, clazz, deep);
	}

	private IHDLObject[][] arrayClazzTypes;

	@SuppressWarnings("unchecked")
	public <T> T[] getAllObjectsOf(HDLClass clazz, Class<? extends T> type, boolean deep) {
		if (arrayClazzTypes == null) {
			HDLClass[] clazzes = HDLClass.values();
			arrayClazzTypes = new IHDLObject[clazzes.length][];
			Iterator<IHDLObject> iterator = iterator(false);
			EnumMap<HDLClass, List<IHDLObject[]>> map = new EnumMap<HDLClass, List<IHDLObject[]>>(HDLClass.class);
			addClazzArray(this, map);
			while (iterator.hasNext()) {
				HDLObject c = (HDLObject) iterator.next();
				addClazzArray(c, map);
				c.getAllObjectsOf(clazz, type, deep);
				for (int i = 0; i < c.arrayClazzTypes.length; i++) {
					IHDLObject[] array = c.arrayClazzTypes[i];
					if (array != null) {
						HDLClass hdlClass = clazzes[i];
						List<IHDLObject[]> list = map.get(hdlClass);
						if (list == null) {
							map.put(hdlClass, new LinkedList<IHDLObject[]>(Collections.singleton(array)));
						} else {
							list.add(array);
						}
					}
				}
			}
			for (HDLClass hClass : clazzes) {
				List<IHDLObject[]> set = map.get(hClass);
				if (set != null) {
					NonSameList<IHDLObject> list = new NonSameList<IHDLObject>();
					for (IHDLObject[] ihdlObjects : set) {
						for (IHDLObject ihdlObject : ihdlObjects) {
							list.add(ihdlObject);
						}
					}
					arrayClazzTypes[hClass.ordinal()] = list.toArray((IHDLObject[]) Array.newInstance(hClass.clazz, list.size()));
				}
			}
		}
		IHDLObject[] list = arrayClazzTypes[clazz.ordinal()];
		if (list == null)
			return (T[]) Array.newInstance(clazz.clazz, 0);
		if (deep == false) {
			LinkedList<IHDLObject> res = new LinkedList<IHDLObject>();
			for (IHDLObject ihdlObject : list)
				if (ihdlObject.getContainer() == this) {
					res.add(ihdlObject);
				}
			return res.toArray((T[]) Array.newInstance(clazz.clazz, res.size()));
		}
		// T[] res = (T[]) Array.newInstance(clazz.clazz, list.length);
		// System.arraycopy(list, 0, res, 0, res.length);
		return (T[]) list.clone();
	}

	private void addClazzArray(IHDLObject hdlObject, EnumMap<HDLClass, List<IHDLObject[]>> map) {
		EnumSet<HDLClass> set = hdlObject.getClassSet();
		for (HDLClass hdlClass : set) {
			List<IHDLObject[]> list = map.get(hdlClass);
			if (list == null) {
				list = new LinkedList<IHDLObject[]>();
				map.put(hdlClass, list);
			}
			list.add(new IHDLObject[] { hdlObject });
		}
	}

	@Override
	public <T, K> Set<T> getAllObjectsOf(Class<T> clazz, HDLQuery.HDLFieldAccess<T, K> field, Predicate<K>... matcher) {
		T[] allObjectsOf = getAllObjectsOf(clazz, true);
		Set<T> list = new NonSameList<T>();
		for (T t : allObjectsOf) {
			K value = field.getValue(t);
			for (Predicate<K> predicate : matcher)
				if (predicate.apply(value)) {
					list.add(t);
					break;
				}
		}
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IHDLObject> T getContainer(Class<T> clazz) {
		if (container != null) {
			if (container.getClass().equals(clazz))
				return (T) container;
			return container.getContainer(clazz);
		}
		return null;
	}

	@Override
	public Iterator<IHDLObject> iterator() {
		return iterator(false);
	}

	@Override
	public Iterator<IHDLObject> iterator(boolean deep) {
		try {
			return new HDLIterator(this, deep);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public @Nonnull
	HDLObject setContainer(@Nullable IHDLObject container) {
		if (container == this)
			throw new IllegalArgumentException("Object can not contain itself");
		if (this.container != null)
			throw new IllegalArgumentException("Container already set");
		if (frozen)
			throw new IllegalArgumentException("Frozen");
		this.container = container;
		return this;
	}

	public HDLLibrary getLibrary() {
		HDLUnit hdlUnit = getContainer(HDLUnit.class);
		if (hdlUnit != null)
			return hdlUnit.getLibrary();
		HDLPackage hdlPackage = getContainer(HDLPackage.class);
		if (hdlPackage != null)
			return hdlPackage.getLibrary();
		return null;
	}

	@Override
	public IHDLObject freeze(IHDLObject container) {
		setContainer(container);
		frozen = true;
		Iterator<IHDLObject> iterator = iterator();
		while (iterator.hasNext()) {
			IHDLObject hdlObject = iterator.next();
			hdlObject.freeze(this);
		}
		return this;
	}

	@Override
	public boolean isFrozen() {
		return frozen;
	}

	@Override
	public String toString() {
		return StringWriteExtension.asString(this, SyntaxHighlighter.none());
	}

	// $CONTENT-END$

}

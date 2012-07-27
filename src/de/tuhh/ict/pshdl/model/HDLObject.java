package de.tuhh.ict.pshdl.model;

import java.util.*;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.FieldMatcher;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLObject contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLObject extends AbstractHDLObject implements de.tuhh.ict.pshdl.model.IHDLObject {
	/**
	 * Constructs a new instance of {@link HDLObject}
	 * 
	 * @param objectID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLObject(int objectID, @Nullable IHDLObject container, boolean validate, boolean updateContainer) {
		super(objectID, container, validate, updateContainer);
	}

	/**
	 * Constructs a new instance of {@link HDLObject}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLObject(int objectID, @Nullable IHDLObject container) {
		this(objectID, container, true, true);
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
	public static HDLFieldAccess<HDLObject, IHDLObject> fContainer = new HDLFieldAccess<HDLObject, IHDLObject>() {
		@Override
		public IHDLObject getValue(HDLObject obj) {
			if (obj == null)
				return null;
			return obj.getContainer();
		}
	};

	// $CONTENT-BEGIN$

	@Override
	public void copyMetaData(IHDLObject src, IHDLObject target) {
		((HDLObject) target).metaData.putAll(((HDLObject) src).metaData);
	}

	@Override
	public abstract HDLObject copy();

	@Override
	public abstract HDLObject copyFiltered(CopyFilter filter);

	public Map<String, Object> metaData = new HashMap<String, Object>();

	@Override
	public void addMeta(String key, Object value) {
		metaData.put(key, value);
	}

	@Override
	public Object getMeta(String key) {
		return metaData.get(key);
	}

	@Override
	public <K> void addMeta(MetaAccess<K> key, K value) {
		metaData.put(key.name(), value);
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
		return (K) metaData.get(key.name());
	}

	public static <T> ArrayList<T> asList(T... items) {
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
		if (!(obj instanceof HDLObject))
			return false;
		return true;
	}

	private Map<Class<? extends HDLObject>, Set<HDLObject>> clazzTypes;
	private Map<Class<? extends HDLObject>, Set<HDLObject>> deepClazzTypes;

	@Override
	@SuppressWarnings("unchecked")
	public <T> Set<T> getAllObjectsOf(Class<? extends T> clazz, boolean deep) {
		if (clazzTypes == null) {
			clazzTypes = new HashMap<Class<? extends HDLObject>, Set<HDLObject>>();
			deepClazzTypes = new HashMap<Class<? extends HDLObject>, Set<HDLObject>>();
			Iterator<IHDLObject> iterator = iterator(false);
			// addClazz(this, clazzTypes);
			// addClazz(this, deepClazzTypes);
			while (iterator.hasNext()) {
				HDLObject c = (HDLObject) iterator.next();
				addClazz(c, clazzTypes);
				addClazz(c, deepClazzTypes);
				c.getAllObjectsOf(clazz, deep);
				for (Entry<Class<? extends HDLObject>, Set<HDLObject>> e : c.deepClazzTypes.entrySet()) {
					Set<HDLObject> list = deepClazzTypes.get(e.getKey());
					if (list == null)
						deepClazzTypes.put(e.getKey(), new NonSameList<HDLObject>(e.getValue()));
					else
						list.addAll(e.getValue());
				}
			}
		}
		NonSameList<T> list;
		if (deep) {
			list = (NonSameList<T>) deepClazzTypes.get(clazz);
		} else
			list = (NonSameList<T>) clazzTypes.get(clazz);
		if (list == null)
			return new NonSameList<T>();
		return list.clone();
	}

	@Override
	public <T, K> Set<T> getAllObjectsOf(Class<T> clazz, HDLQuery.HDLFieldAccess<T, K> field, FieldMatcher<K> matcher) {
		Set<T> list = getAllObjectsOf(clazz, true);
		for (Iterator<T> iter = list.iterator(); iter.hasNext();) {
			T t = iter.next();
			K value = field.getValue(t);
			if (!matcher.matches(value))
				iter.remove();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private void addClazz(HDLObject c, Map<Class<? extends HDLObject>, Set<HDLObject>> ct) {
		Class<? extends HDLObject> clazz = c.getClass();
		do {
			Class<?>[] interfaces = clazz.getInterfaces();
			for (Class<?> cIf : interfaces) {
				addClazz(c, ct, (Class<? extends HDLObject>) cIf);
			}
			addClazz(c, ct, clazz);
			clazz = (Class<? extends HDLObject>) clazz.getSuperclass();
		} while ((clazz != null) && !clazz.equals(HDLObject.class));
	}

	private void addClazz(HDLObject c, Map<Class<? extends HDLObject>, Set<HDLObject>> ct, Class<? extends HDLObject> clazz) {
		Set<HDLObject> list = ct.get(clazz);
		if (list == null)
			list = new NonSameList<HDLObject>();
		list.add(c);
		ct.put(clazz, list);
	}

	@SuppressWarnings("unchecked")
	public static <T extends IHDLObject> ArrayList<T> copyAll(ArrayList<T> array) {
		ArrayList<T> res = new ArrayList<T>(array.size());
		for (T hdlExpression : array) {
			res.add((T) hdlExpression.copy());
		}
		return res;
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
	public HDLObject setContainer(IHDLObject container) {
		if (container == this)
			throw new IllegalArgumentException("Object can not contain itself");
		if (this.container != null) {
			if (((HDLObject) this.container).objectID != ((HDLObject) container).objectID) {
				throw new IllegalArgumentException("The parents container ID does not match the new container ID!");
			}
		}
		this.container = container;
		return this;
	}

	// $CONTENT-END$

}

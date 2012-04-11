package de.tuhh.ict.pshdl.model;

import java.util.*;
import java.util.Map.Entry;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

public abstract class HDLObject extends AbstractHDLObject {
	/**
	 * Constructs a new instance of {@link HDLObject}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLObject(HDLObject container, boolean validate) {
		super(container, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLObject}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLObject(HDLObject container) {
		this(container, true);
	}

	public HDLObject() {
		super();
	}

	public HDLClass getClassType() {
		return HDLClass.HDLObject;
	}

	// $CONTENT-BEGIN$

	public abstract HDLObject copy();

	public abstract HDLObject copyFiltered(CopyFilter filter);

	public interface MetaAccess<T> {
		public String name();
	}

	public Map<String, Object> metaData = new HashMap<String, Object>();

	public void addMeta(String key, Object value) {
		metaData.put(key, value);
	}

	public Object getMeta(String key) {
		return metaData.get(key);
	}

	public <K> void addMeta(MetaAccess<K> key, K value) {
		metaData.put(key.name(), value);
	}

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

	private Map<Class<? extends HDLObject>, List<HDLObject>> clazzTypes;
	private Map<Class<? extends HDLObject>, List<HDLObject>> deepClazzTypes;

	public abstract Iterator<HDLObject> iterator();

	@SuppressWarnings("unchecked")
	public <T> List<T> getAllObjectsOf(Class<? extends T> clazz, boolean deep) {
		if (clazzTypes == null) {
			clazzTypes = new HashMap<Class<? extends HDLObject>, List<HDLObject>>();
			deepClazzTypes = new HashMap<Class<? extends HDLObject>, List<HDLObject>>();
			Iterator<HDLObject> iterator = iterator();
			while (iterator.hasNext()) {
				HDLObject c = iterator.next();
				addClazz(c, clazzTypes);
				addClazz(c, deepClazzTypes);
				c.getAllObjectsOf(clazz, deep);
				for (Entry<Class<? extends HDLObject>, List<HDLObject>> e : c.deepClazzTypes.entrySet()) {
					List<HDLObject> list = deepClazzTypes.get(e.getKey());
					if (list == null)
						deepClazzTypes.put(e.getKey(), e.getValue());
					else
						list.addAll(e.getValue());
				}
			}
		}
		List<T> list;
		if (deep) {
			list = (List<T>) deepClazzTypes.get(clazz);
		} else
			list = (List<T>) clazzTypes.get(clazz);
		if (list == null)
			return new LinkedList<T>();
		return list;
	}

	@SuppressWarnings("unchecked")
	private void addClazz(HDLObject c, Map<Class<? extends HDLObject>, List<HDLObject>> ct) {
		Class<? extends HDLObject> clazz = c.getClass();
		do {
			addClazz(c, ct, clazz);
			clazz = (Class<? extends HDLObject>) clazz.getSuperclass();
		} while ((clazz != null) && !clazz.equals(HDLObject.class));
	}

	private void addClazz(HDLObject c, Map<Class<? extends HDLObject>, List<HDLObject>> ct, Class<? extends HDLObject> clazz) {
		List<HDLObject> list = ct.get(clazz);
		if (list == null)
			list = new LinkedList<HDLObject>();
		list.add(c);
		ct.put(clazz, list);
	}

	// $CONTENT-END$

}

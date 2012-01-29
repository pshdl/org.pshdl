package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;


public abstract class HDLObject extends AbstractHDLObject {
	/**
	 * Constructs a new instance of {@link HDLObject}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
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
	
//$CONTENT-BEGIN$

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

//$CONTENT-END$
	
}	

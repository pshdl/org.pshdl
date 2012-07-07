package de.tuhh.ict.pshdl.model;

import java.util.*;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.FieldMatcher;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import de.tuhh.ict.pshdl.model.validation.*;

/**
 * The class HDLObject contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLObject extends AbstractHDLObject {
	/**
	 * Constructs a new instance of {@link HDLObject}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLObject(int containerID, @Nullable HDLObject container, boolean validate) {
		super(containerID, container, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLObject}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLObject(int containerID, @Nullable HDLObject container) {
		this(containerID, container, true);
	}

	public HDLObject() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	public HDLClass getClassType() {
		return HDLClass.HDLObject;
	}

	/**
	 * The accessor for the field container which is of type HDLObject.
	 */
	public static HDLFieldAccess<HDLObject, HDLObject> fContainer = new HDLFieldAccess<HDLObject, HDLObject>() {
		@Override
		public HDLObject getValue(HDLObject obj) {
			if (obj == null)
				return null;
			return obj.getContainer();
		}
	};

	public HDLVariable resolveVariable(HDLQualifiedName var) {
		if (container == null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_VARIABLE, this, "for variable:" + var));
		return container.resolveVariable(var);
	}

	public HDLEnum resolveEnum(HDLQualifiedName hEnum) {
		if (container == null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_ENUM, this, "for enum:" + hEnum));
		return container.resolveEnum(hEnum);
	}

	public HDLType resolveType(HDLQualifiedName type) {
		if (container == null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_TYPE, this, "for type:" + type));
		return container.resolveType(type);
	}

	public HDLInterface resolveInterface(HDLQualifiedName hIf) {
		if (container == null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_INTERFACE, this, "for interface:" + hIf));
		return container.resolveInterface(hIf);
	}

	// $CONTENT-BEGIN$

	public static void printInfo(HDLObject obj) {
		if (obj.getMeta("CONSTRUCTOR") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " constructed at:" + obj.getMeta("CONSTRUCTOR"));
		}
		Object meta = obj.getMeta("CONSTRUCTION_SRC");
		if (meta != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " constructed in:" + meta.getClass());
			if (meta instanceof HDLObject)
				printInfo((HDLObject) meta);
		}
		if (obj.getMeta("SETTER") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " setter called at:" + obj.getMeta("SETTER"));
		}
		if (obj.getMeta("SETTER_CONTAINER") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " setContainer called at:" + obj.getMeta("SETTER_CONTAINER"));
		}
		if (obj.getMeta("ADDER") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " add called at:" + obj.getMeta("ADDER"));
		}
		if (obj.getMeta("COPY") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " copy called at:" + obj.getMeta("COPY"));
		}
		if (obj.getMeta("COPY_FILTERED") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " copyFiltered called at:" + obj.getMeta("COPY_FILTERED"));
		}
		if (obj.getMeta("COPY_SOURCE") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " copied from:" + obj.getMeta("COPY_SOURCE"));
			printInfo((HDLObject) obj.getMeta("COPY_SOURCE"));
		}
	}

	@Override
	public void copyMetaData(HDLObject src, HDLObject target) {
		target.metaData.putAll(src.metaData);
	}

	@Override
	public abstract HDLObject copy();

	@Override
	public abstract HDLObject copyFiltered(CopyFilter filter);

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

	public void setMeta(MetaAccess<Boolean> meta) {
		addMeta(meta, true);
	}

	public boolean hasMeta(MetaAccess<?> key) {
		return getMeta(key) != null;
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
	public <T> LinkedList<T> getAllObjectsOf(Class<? extends T> clazz, boolean deep) {
		if (clazzTypes == null) {
			clazzTypes = new HashMap<Class<? extends HDLObject>, List<HDLObject>>();
			deepClazzTypes = new HashMap<Class<? extends HDLObject>, List<HDLObject>>();
			Iterator<HDLObject> iterator = iterator(false);
			// addClazz(this, clazzTypes);
			// addClazz(this, deepClazzTypes);
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
		LinkedList<T> list;
		if (deep) {
			list = (LinkedList<T>) deepClazzTypes.get(clazz);
		} else
			list = (LinkedList<T>) clazzTypes.get(clazz);
		if (list == null)
			return new LinkedList<T>();
		return (LinkedList<T>) list.clone();
	}

	public <T, K> LinkedList<T> getAllObjectsOf(Class<T> clazz, HDLQuery.HDLFieldAccess<T, K> field, FieldMatcher<K> matcher) {
		LinkedList<T> list = getAllObjectsOf(clazz, true);
		for (Iterator<T> iter = list.iterator(); iter.hasNext();) {
			T t = iter.next();
			K value = field.getValue(t);
			if (!matcher.matches(value))
				iter.remove();
		}
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

	@SuppressWarnings("unchecked")
	public static <T extends HDLObject> ArrayList<T> copyAll(ArrayList<T> array) {
		ArrayList<T> res = new ArrayList<T>(array.size());
		for (T hdlExpression : array) {
			res.add((T) hdlExpression.copy());
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public <T extends HDLObject> T getContainer(Class<T> clazz) {
		if (container != null) {
			if (container.getClass().equals(clazz))
				return (T) container;
			return container.getContainer(clazz);
		}
		return null;
	}

	public HDLIterator iterator(boolean deep) {
		try {
			return new HDLIterator(this, deep);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public HDLObject setContainer(HDLObject container) {
		if (container == this)
			throw new IllegalArgumentException("Object can not contain itself");
		if (this.container != null) {
			if (this.container.containerID != container.containerID) {
				throw new IllegalArgumentException("The parents container ID does not match the new container ID!");
			}
		}
		this.container = container;
		return this;
	}

	// $CONTENT-END$

}

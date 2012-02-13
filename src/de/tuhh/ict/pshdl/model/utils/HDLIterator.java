package de.tuhh.ict.pshdl.model.utils;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLIterator implements Iterator<HDLObject> {
	private final class SingleObjectIterator implements Iterator<HDLObject> {
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

	private List<Iterator<HDLObject>> iters = new LinkedList<Iterator<HDLObject>>();
	private Iterator<Iterator<HDLObject>> delegate;
	private Iterator<HDLObject> current;

	public HDLIterator(HDLObject obj) throws IllegalArgumentException, IllegalAccessException {
		Class<? extends HDLObject> clazz = obj.getClass();
		addAllFields(obj, clazz);
		delegate = iters.iterator();
		if (delegate.hasNext())
			current = delegate.next();
		else
			current = new SingleObjectIterator(null);
	}

	private void addAllFields(HDLObject obj, Class<?> clazz) throws IllegalAccessException {
		Class<?> superClazz = clazz.getSuperclass();
		if ((superClazz != null) && !superClazz.getName().endsWith("HDLObject"))
			addAllFields(obj, superClazz);
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getAnnotation(Visit.class) != null) {
				field.setAccessible(true);
				Object object = field.get(obj);
				if (object instanceof HDLObject) {
					final HDLObject hdo = (HDLObject) object;
					if (hdo != null)
						iters.add(new SingleObjectIterator(hdo));
				}
				if (object instanceof Collection) {
					@SuppressWarnings("unchecked")
					Collection<HDLObject> chdo = (Collection<HDLObject>) object;
					iters.add(chdo.iterator());
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
	public HDLObject next() {
		return current.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}

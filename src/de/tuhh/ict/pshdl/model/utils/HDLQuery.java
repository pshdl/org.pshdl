package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLQuery {
	public static abstract class HDLFieldAccess<T, K> {
		public abstract K getValue(T obj);
	}

	public static interface FieldMatcher {
		boolean matches(Object obj);
	}

	private static class EqualsMatcher implements FieldMatcher {
		private final Object equalsTo;

		public EqualsMatcher(Object equalsTo) {
			this.equalsTo = equalsTo;
		}

		@Override
		public boolean matches(Object obj) {
			if (equalsTo == null)
				return obj == null ? true : false;
			return equalsTo.equals(obj);
		}
	}

	public static class FieldSelector<T, K> {
		private HDLFieldAccess<T, K> field;
		private HDLObject from;
		private Class<T> clazz;

		public FieldSelector(Class<T> clazz, HDLObject from, HDLFieldAccess<T, K> field) {
			this.clazz = clazz;
			this.from = from;
			this.field = field;
		}

		public List<T> isEqualTo(K value) {
			return from.getAllObjectsOf(clazz, field, new EqualsMatcher(value));
		}

	}

	public static class Selector<T extends HDLObject> {

		private HDLObject from;
		private Class<T> clazz;

		public Selector(Class<T> clazz, HDLObject obj) {
			this.clazz = clazz;
			this.from = obj;
		}

		public <K> FieldSelector<T, K> where(HDLFieldAccess<T, K> field) {
			return new FieldSelector<T, K>(clazz, from, field);
		}
	}

	public static class Source<T extends HDLObject> {
		private Class<T> clazz;

		public Source(Class<T> clazz) {
			this.clazz = clazz;
		}

		public Selector<T> from(HDLObject obj) {
			return new Selector<T>(clazz, obj);
		}
	}

	public static <T extends HDLObject> Source<T> selectAll(Class<T> clazz) {
		return new Source<T>(clazz);
	}

}

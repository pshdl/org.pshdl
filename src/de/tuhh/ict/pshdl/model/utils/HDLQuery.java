package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLQuery {
	public static abstract class HDLFieldAccess<T, K> {
		public abstract K getValue(T obj);
	}

	public static interface FieldMatcher<T> {
		boolean matches(T obj);
	}

	private static class EqualsMatcher<T> implements FieldMatcher<T> {
		private final Object equalsTo;
		private boolean invert;

		public EqualsMatcher(Object equalsTo, boolean invert) {
			this.equalsTo = equalsTo;
			this.invert = invert;
		}

		@Override
		public boolean matches(Object obj) {
			if (equalsTo == null) {
				boolean b = obj == null ? true : false;
				if (invert)
					return !b;
				return b;
			}
			boolean equals = equalsTo.equals(obj);
			if (invert)
				return !equals;
			return equals;
		}
	}

	private static class StartsWithMatcher<K> implements FieldMatcher<K> {
		private final K equalsTo;

		public StartsWithMatcher(K equalsTo) {
			this.equalsTo = equalsTo;
		}

		@Override
		public boolean matches(K obj) {
			if (obj == null)
				return false;
			return obj.toString().startsWith(equalsTo.toString());
		}
	}

	@SuppressWarnings("rawtypes")
	private static class LastSegmentMatcher implements FieldMatcher {
		private final String equalsTo;

		public LastSegmentMatcher(String equalsTo) {
			this.equalsTo = equalsTo;
		}

		@Override
		public boolean matches(Object obj) {
			if (obj == null)
				return false;
			return new HDLQualifiedName(obj.toString()).getLastSegment().equals(equalsTo);
		}
	}

	public static class Result<T> {
		private List<T> res;

		public Result(List<T> res) {
			this.res = res;
		}

		public List<T> getAll() {
			return res;
		}

		public T getFirst() {
			if (res.size() == 0)
				return null;
			return res.get(0);
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

		public Result<T> isEqualTo(K value) {
			return new Result<T>(from.getAllObjectsOf(clazz, field, new EqualsMatcher<K>(value, false)));
		}

		public Result<T> startsWith(K ifRef) {
			return new Result<T>(from.getAllObjectsOf(clazz, field, new StartsWithMatcher<K>(ifRef)));
		}

		@SuppressWarnings("unchecked")
		public Result<T> lastSegmentIs(String lastSegment) {
			return new Result<T>(from.getAllObjectsOf(clazz, field, new LastSegmentMatcher(lastSegment)));
		}

		public Result<T> isNotEqualTo(T value) {
			return new Result<T>(from.getAllObjectsOf(clazz, field, new EqualsMatcher<K>(value, true)));
		}

	}

	public static class Selector<T extends HDLObject> {

		private HDLObject from;
		private Class<T> clazz;

		public Selector(Class<T> clazz, HDLObject obj) {
			this.clazz = clazz;
			this.from = obj;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public <K> FieldSelector<T, K> where(HDLFieldAccess<? super T, K> field) {
			return new FieldSelector(clazz, from, field);
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

	public static <T extends HDLObject> Source<T> select(Class<T> clazz) {
		return new Source<T>(clazz);
	}

}

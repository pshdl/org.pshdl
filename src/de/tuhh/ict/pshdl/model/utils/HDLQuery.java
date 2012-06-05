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
			return from.getAllObjectsOf(clazz, field, new EqualsMatcher<K>(value));
		}

		public List<T> startsWith(K ifRef) {
			return from.getAllObjectsOf(clazz, field, new StartsWithMatcher<K>(ifRef));
		}

		@SuppressWarnings("unchecked")
		public List<T> lastSegmentIs(String lastSegment) {
			return from.getAllObjectsOf(clazz, field, new LastSegmentMatcher(lastSegment));
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

	public static <T extends HDLObject> Source<T> selectAll(Class<T> clazz) {
		return new Source<T>(clazz);
	}

}

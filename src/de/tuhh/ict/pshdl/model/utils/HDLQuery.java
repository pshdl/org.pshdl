package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLQuery {
	public static abstract class HDLFieldAccess<T, K> {
		public abstract K getValue(T obj);
	}

	public static interface FieldMatcher<T> {
		boolean matches(T obj);

		FieldMatcher<T> setParameter(T param);
	}

	private static class EqualsMatcher<T> implements FieldMatcher<T> {
		private Object equalsTo;
		private boolean invert;

		public EqualsMatcher(Object equalsTo, boolean invert) {
			this.equalsTo = equalsTo;
			this.invert = invert;
		}

		@Override
		public boolean matches(T obj) {
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

		@Override
		public EqualsMatcher<T> setParameter(T param) {
			return new EqualsMatcher<T>(param, invert);
		}
	}

	private static class StartsWithMatcher<T> implements FieldMatcher<T> {
		private T equalsTo;

		public StartsWithMatcher(T equalsTo) {
			this.equalsTo = equalsTo;
		}

		@Override
		public boolean matches(T obj) {
			if (obj == null)
				return false;
			return obj.toString().startsWith(equalsTo.toString());
		}

		@Override
		public StartsWithMatcher<T> setParameter(T param) {
			return new StartsWithMatcher<T>(param);
		}
	}

	@SuppressWarnings("rawtypes")
	private static class LastSegmentMatcher implements FieldMatcher {
		private HDLQualifiedName equalsTo;
		private boolean matchLocally = false;

		public LastSegmentMatcher(String equalsTo) {
			this.equalsTo = new HDLQualifiedName(equalsTo);
		}

		public LastSegmentMatcher(HDLQualifiedName fullName, boolean matchLocally) {
			this.equalsTo = fullName.getLocalPart();
			this.matchLocally = matchLocally;
		}

		@Override
		public boolean matches(Object obj) {
			if (obj == null)
				return false;
			if (matchLocally)
				return new HDLQualifiedName(obj.toString()).getLocalPart().equals(equalsTo);
			return new HDLQualifiedName(obj.toString()).getLastSegment().equals(equalsTo.getLastSegment());
		}

		@Override
		public LastSegmentMatcher setParameter(Object param) {
			return new LastSegmentMatcher(param.toString());
		}
	}

	public static class Result<T, K> {
		private HDLFieldAccess<T, K> field;
		private HDLObject from;
		private Class<T> clazz;
		private FieldMatcher<K> matcher;

		public Result(HDLObject from, Class<T> clazz, HDLFieldAccess<T, K> field, FieldMatcher<K> matcher) {
			this.from = from;
			this.clazz = clazz;
			this.field = field;
			this.matcher = matcher;
		}

		public Collection<T> getAll() {
			return from.getAllObjectsOf(clazz, field, matcher);
		}

		public T getFirst() {
			Collection<T> res = from.getAllObjectsOf(clazz, field, matcher);
			if (res.isEmpty())
				return null;
			Iterator<T> iterator = res.iterator();
			if (iterator.hasNext())
				return iterator.next();
			return null;
		}

		public Collection<T> or(K parameter) {
			Collection<T> res = from.getAllObjectsOf(clazz, field, matcher);
			res.addAll(from.getAllObjectsOf(clazz, field, matcher.setParameter(parameter)));
			return res;
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

		public Result<T, K> isEqualTo(K value) {
			return new Result<T, K>(from, clazz, field, new EqualsMatcher<K>(value, false));
		}

		public Result<T, K> startsWith(K ifRef) {
			return new Result<T, K>(from, clazz, field, new StartsWithMatcher<K>(ifRef));
		}

		@SuppressWarnings("unchecked")
		public Result<T, K> lastSegmentIs(String lastSegment) {
			return new Result<T, K>(from, clazz, field, new LastSegmentMatcher(lastSegment));
		}

		public Result<T, K> isNotEqualTo(K value) {
			return new Result<T, K>(from, clazz, field, new EqualsMatcher<K>(value, true));
		}

		public Result<T, K> matchesLocally(HDLQualifiedName fullName) {
			return new Result<T, K>(from, clazz, field, new LastSegmentMatcher(fullName, true));
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

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
package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import com.google.common.base.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLQuery {
	public static abstract class HDLFieldAccess<T, K> {
		public final String fieldName;

		protected HDLFieldAccess(String fieldName) {
			this.fieldName = fieldName;
		}

		public abstract K getValue(T obj);
	}

	private static class EqualsMatcher<T> implements Predicate<T> {
		private Object equalsTo;
		private boolean invert;

		public EqualsMatcher(Object equalsTo, boolean invert) {
			this.equalsTo = equalsTo;
			this.invert = invert;
		}

		@Override
		public boolean apply(T obj) {
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

	private static class StartsWithMatcher<T> implements Predicate<T> {
		private T equalsTo;

		public StartsWithMatcher(T equalsTo) {
			this.equalsTo = equalsTo;
		}

		@Override
		public boolean apply(T obj) {
			if (obj == null)
				return false;
			return obj.toString().startsWith(equalsTo.toString());
		}

	}

	private static class LastSegmentMatcher<T> implements Predicate<T> {
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
		public boolean apply(T obj) {
			if (obj == null)
				return false;
			if (matchLocally)
				return new HDLQualifiedName(obj.toString()).getLocalPart().equals(equalsTo);
			return new HDLQualifiedName(obj.toString()).getLastSegment().equals(equalsTo.getLastSegment());
		}

	}

	public static class Result<T, K> {
		private HDLFieldAccess<T, K> field;
		private IHDLObject from;
		private Class<T> clazz;
		private Predicate<K> matcher;

		public Result(IHDLObject from, Class<T> clazz, HDLFieldAccess<T, K> field, Predicate<K> matcher) {
			this.from = from;
			this.clazz = clazz;
			this.field = field;
			this.matcher = matcher;
		}

		@SuppressWarnings("unchecked")
		public Collection<T> getAll() {
			return from.getAllObjectsOf(clazz, field, matcher);
		}

		public T getFirst() {
			@SuppressWarnings("unchecked")
			Collection<T> res = from.getAllObjectsOf(clazz, field, matcher);
			if (res.isEmpty())
				return null;
			Iterator<T> iterator = res.iterator();
			if (iterator.hasNext())
				return iterator.next();
			return null;
		}

		public Result<T, K> or(Predicate<K> value) {
			return new Result<T, K>(from, clazz, field, Predicates.or(matcher, value));
		}

	}

	public static class FieldSelector<T, K> {
		private HDLFieldAccess<T, K> field;
		private IHDLObject from;
		private Class<T> clazz;

		public FieldSelector(Class<T> clazz, IHDLObject from, HDLFieldAccess<T, K> field) {
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

		public Result<T, K> lastSegmentIs(String lastSegment) {
			return new Result<T, K>(from, clazz, field, new LastSegmentMatcher<K>(lastSegment));
		}

		public Result<T, K> isNotEqualTo(K value) {
			return new Result<T, K>(from, clazz, field, new EqualsMatcher<K>(value, true));
		}

		public Result<T, K> matchesLocally(HDLQualifiedName fullName) {
			return new Result<T, K>(from, clazz, field, new LastSegmentMatcher<K>(fullName, true));
		}

		public Result<T, K> matches(Predicate<K> predicate) {
			return new Result<T, K>(from, clazz, field, predicate);
		}

	}

	public static class Selector<T extends IHDLObject> {

		private IHDLObject from;
		private Class<T> clazz;

		public Selector(Class<T> clazz, IHDLObject obj) {
			this.clazz = clazz;
			this.from = obj;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public <K> FieldSelector<T, K> where(HDLFieldAccess<? super T, K> field) {
			return new FieldSelector(clazz, from, field);
		}
	}

	public static class Source<T extends IHDLObject> {
		private Class<T> clazz;

		public Source(Class<T> clazz) {
			this.clazz = clazz;
		}

		public Selector<T> from(IHDLObject obj) {
			return new Selector<T>(clazz, obj);
		}
	}

	public static <T extends IHDLObject> Source<T> select(Class<T> clazz) {
		return new Source<T>(clazz);
	}

	public static <K> Predicate<K> isEqualTo(K value) {
		return new EqualsMatcher<K>(value, false);
	}

	public static <K> Predicate<K> startsWith(K value) {
		return new StartsWithMatcher<K>(value);
	}

	public static <K> Predicate<K> isNotEqualTo(K value) {
		return new EqualsMatcher<K>(value, true);
	}

	public static <K> Predicate<K> lastSegmentIs(String value) {
		return new LastSegmentMatcher<K>(value);
	}

	public static <K> Predicate<K> matchesLocally(HDLQualifiedName value) {
		return new LastSegmentMatcher<K>(value, true);
	}

}

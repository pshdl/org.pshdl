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
package org.pshdl.model.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLInterfaceRef;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.extensions.FullNameExtension;
import org.pshdl.model.utils.internal.NonSameList;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class HDLQuery {
	public static abstract class HDLFieldAccess<T extends IHDLObject, K> {
		public static enum Quantifier {
			ZERO_OR_ONE, ONE, ZERO_OR_MORE, ONE_OR_MORE
		}

		public final String fieldName;
		public final Class<?> type;
		public final Quantifier quantifier;

		protected HDLFieldAccess(String fieldName, Class<?> type, Quantifier quantifier) {
			this.fieldName = fieldName;
			this.type = type;
			this.quantifier = quantifier;
		}

		public abstract K getValue(T obj);

		public abstract T setValue(T obj, K value);
	}

	private static class EqualsMatcher<T> implements Predicate<T> {
		private final Object equalsTo;
		private final boolean invert;

		public EqualsMatcher(Object equalsTo, boolean invert) {
			this.equalsTo = equalsTo;
			this.invert = invert;
		}

		@Override
		public boolean apply(T obj) {
			if (equalsTo == null) {
				final boolean b = obj == null ? true : false;
				if (invert)
					return !b;
				return b;
			}
			final boolean equals = equalsTo.equals(obj);
			if (invert)
				return !equals;
			return equals;
		}

	}

	private static class StartsWithMatcher<T> implements Predicate<T> {
		private final T equalsTo;

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

	private static class FullNameMatcher<K extends IHDLObject> implements Predicate<K> {

		private final HDLQualifiedName asRef;

		public FullNameMatcher(HDLQualifiedName asRef) {
			this.asRef = asRef;
		}

		@Override
		public boolean apply(K input) {
			return asRef.equals(FullNameExtension.fullNameOf(input));
		}

	}

	private static class LastSegmentMatcher<T> implements Predicate<T> {
		private final HDLQualifiedName equalsTo;
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

	@SuppressWarnings("rawtypes")
	public static class Result<T extends IHDLObject, K> {
		private final HDLFieldAccess<T, K> field;
		private final IHDLObject from;
		private final Class<T> clazz;
		private final Predicate matcher;

		public Result(IHDLObject from, Class<T> clazz, HDLFieldAccess<T, K> field, Predicate matcher) {
			this.from = from;
			this.clazz = clazz;
			this.field = field;
			this.matcher = matcher;
		}

		public Collection<T> getAll() {
			return getAllMatchingObjects();
		}

		@SuppressWarnings("unchecked")
		private Set<T> getAllMatchingObjects() {
			final T[] allObjectsOf = from.getAllObjectsOf(clazz, true);
			final Set<T> list = new NonSameList<T>();
			if (field != null) {
				for (final T t : allObjectsOf) {
					final K value = field.getValue(t);
					if (matcher.apply(value)) {
						list.add(t);
					}
				}
			} else {
				for (final T t : allObjectsOf) {
					if (matcher.apply(t)) {
						list.add(t);
					}
				}
			}
			return list;
		}

		public T getFirst() {
			final Collection<T> res = getAllMatchingObjects();
			if (res.isEmpty())
				return null;
			final Iterator<T> iterator = res.iterator();
			if (iterator.hasNext())
				return iterator.next();
			return null;
		}

		@SuppressWarnings("unchecked")
		public Result<T, K> or(Predicate<K> value) {
			return new Result<T, K>(from, clazz, field, Predicates.or(matcher, value));
		}

	}

	public static class FieldSelector<T extends IHDLObject, K> {
		private final HDLFieldAccess<T, K> field;
		private final IHDLObject from;
		private final Class<T> clazz;

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

		public <I extends IHDLObject> Result<T, I> fullNameIs(HDLQualifiedName asRef) {
			if (field != null)
				throw new IllegalArgumentException("Can only use fullNameIs on whereObj");
			return new Result<T, I>(from, clazz, null, new FullNameMatcher<I>(asRef));
		}

		public Result<T, K> isType(final HDLClass clazz) {
			final Predicate<K> matcher = new Predicate<K>() {

				@Override
				public boolean apply(K input) {
					return ((IHDLObject) input).getClassSet().contains(clazz);
				}
			};
			return matches(matcher);
		}

	}

	public static class Selector<T extends IHDLObject> {

		private final IHDLObject from;
		private final Class<T> clazz;

		public Selector(Class<T> clazz, IHDLObject obj) {
			this.clazz = clazz;
			this.from = obj;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public <K> FieldSelector<T, K> where(HDLFieldAccess<? super T, K> field) {
			return new FieldSelector(clazz, from, field);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public <K> FieldSelector<T, K> whereObj() {
			return new FieldSelector(clazz, from, null);
		}
	}

	public static class Source<T extends IHDLObject> {
		private final Class<T> clazz;

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

	public static Collection<HDLEnumRef> getEnumRefs(IHDLObject from, HDLVariable hdlVariable) {
		return HDLQuery.select(HDLEnumRef.class).from(from).whereObj().fullNameIs(hdlVariable.asRef()).getAll();
	}

	public static Collection<HDLVariableRef> getVarRefs(IHDLObject from, HDLVariable hdlVariable) {
		return HDLQuery.select(HDLVariableRef.class).from(from).whereObj().fullNameIs(hdlVariable.asRef()).getAll();
	}

	public static Collection<HDLInterfaceRef> getInterfaceRefs(IHDLObject obj, HDLVariable hdlVariable) {
		final HDLQualifiedName asRef = hdlVariable.asRef();
		final Collection<HDLInterfaceRef> refs = HDLQuery.select(HDLInterfaceRef.class).from(obj).where(HDLInterfaceRef.fHIf).lastSegmentIs(asRef.getLastSegment()).getAll();
		for (final Iterator<HDLInterfaceRef> iterator = refs.iterator(); iterator.hasNext();) {
			final HDLInterfaceRef hir = iterator.next();
			final Optional<HDLVariable> resolveHIf = hir.resolveHIf();
			if (resolveHIf.isPresent()) {
				final HDLQualifiedName fullNameOf = FullNameExtension.fullNameOf(resolveHIf.get());
				if (!asRef.equals(fullNameOf)) {
					iterator.remove();
				}
			} else {
				if (!asRef.equals(hir.getHIfRefName())) {
					iterator.remove();
				}
			}
		}
		return refs;
	}

}

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess.Quantifier;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * A {@link ModificationSet} records changes that will be made to the AST
 * (HDL*)-models. Those changes can then be applied to rewrite a completly new
 * AST.
 *
 * @author Karsten Becker
 *
 */
public class ModificationSet {

	private class MSCopyFilter extends CopyFilter.DeepCloneFilter {

		@SuppressWarnings("unchecked")
		@Override
		public <T extends IHDLObject> T copyObject(String feature, IHDLObject container, T original) {
			if (original == null)
				return null;
			final List<Modification> mods = getModifications(original);
			if (mods != null) {
				for (final Modification modification : mods) {
					if (modification.type == ModificationType.REPLACE) {
						if (modification.with.size() > 1)
							throw new IllegalArgumentException(
									"Can not replace with more than one object into a single node feature:" + feature + " of " + container.getClassType());
						if (currentMods.contains(modification.modificationID)) {
							continue;
						}
						final IHDLObject replacement = (IHDLObject) modification.with.get(0);
						final T actualReplacement;
						currentMods.add(modification.modificationID);
						actualReplacement = (T) copyObject(feature, container, replacement);
						currentMods.remove(modification.modificationID);
						return actualReplacement;
					}
					if (modification.type == ModificationType.REMOVE)
						return null;
					throw new IllegalArgumentException("Can not use " + modification.type + " for single node feature:" + feature + " of " + container.getClassType());
				}
			}
			return super.copyObject(feature, container, original);
		}

		@Override
		public <T> ArrayList<T> copyContainer(String feature, IHDLObject container, Iterable<T> object) {
			final ArrayList<T> res = Lists.newArrayList();
			if (object != null) {
				for (final T t : object) {
					applyModifications(feature, container, res, t);
				}
				doAddFeature(res, feature, container);
				return res;
			}
			doAddFeature(res, feature, container);
			if (!res.isEmpty())
				return res;
			return null;
		}

		private <T> void applyModifications(String feature, IHDLObject container, final ArrayList<T> res, final T t) {
			final List<Modification> mods = getModifications(t);
			if (mods != null) {
				final List<T> before = Lists.newLinkedList();
				final List<T> after = Lists.newLinkedList();
				final List<T> replace = Lists.newLinkedList();
				boolean remove = false;
				final Set<Integer> appliedModIDs = Sets.newHashSet();
				for (final Modification modification : mods) {
					if (currentMods.contains(modification.modificationID)) {
						continue;
					}
					switch (modification.type) {
					case INSERT_AFTER:
						scheduleMod(after, appliedModIDs, modification);
						break;
					case INSERT_BEFORE:
						scheduleMod(before, appliedModIDs, modification);
						break;
					case REPLACE:
						scheduleMod(replace, appliedModIDs, modification);
						break;
					case REMOVE:
						remove = true;
						break;
					case ADD:
						break;
					}
				}
				multiAdd(res, before, container, feature);
				if (!replace.isEmpty()) {
					// A replacement is scheduled, so we just add the
					// replacement objects
					// and ignore the actual element
					multiAdd(res, replace, container, feature);
				} else {
					// The object is not scheduled for removal, so just add it
					if (!remove) {
						singleAdd(res, t, container, feature, true);
					}
				}
				multiAdd(res, after, container, feature);
				currentMods.removeAll(appliedModIDs);
			} else {
				// If there are no modifications for object t then we just add
				// it to the result
				singleAdd(res, t, container, feature, true);
			}
		}

		@SuppressWarnings("unchecked")
		private <T> void scheduleMod(final List<T> list, final Set<Integer> modIDs, final Modification modification) {
			modIDs.add(modification.modificationID);
			currentMods.add(modification.modificationID);
			list.addAll((Collection<? extends T>) modification.with);
		}

		/**
		 * Performs all the {@link ModificationType#ADD} modifications for the
		 * container and the feature.
		 *
		 * @param resultList
		 *            the list that will contain all the replacements
		 * @param feature
		 *            the feature of the container that will contain the obj
		 * @param container
		 *            the object that will contain the obj
		 */
		@SuppressWarnings("unchecked")
		private <T> void doAddFeature(final ArrayList<T> resultList, String feature, IHDLObject container) {
			final List<Modification> mods = getModifications(container);
			if (mods == null)
				return;
			final Set<Integer> appliableMods = Sets.newHashSet();
			final List<Modification> applyMod = Lists.newArrayList();
			for (final Modification mod : mods) {
				if ((mod.type == ModificationType.ADD) && feature.equals(mod.feature)) {
					if (currentMods.contains(mod.modificationID)) {
						continue;
					}
					appliableMods.add(mod.modificationID);
					applyMod.add(mod);
				}
			}
			currentMods.addAll(appliableMods);
			for (final Modification mod : applyMod) {
				multiAdd(resultList, (List<T>) mod.with, container, feature);
			}
			currentMods.removeAll(appliableMods);
		}

		/**
		 * Adds multiple items to the result list <code>resultList</code> which
		 * will be added to the <code>feature</code> of the
		 * <code>container</code>
		 *
		 * @param resultList
		 *            the list that will contain all the replacements
		 * @param objectList
		 *            the objects that should be inserted
		 * @param container
		 *            the object that will contain the objects
		 * @param feature
		 *            the feature of the container that will contain the objects
		 */
		private <T> void multiAdd(ArrayList<T> resultList, List<T> objectList, IHDLObject container, String feature) {
			for (final T element : objectList) {
				singleAdd(resultList, element, container, feature, false);
			}
		}

		/**
		 * Adds a single item to the result list <code>resultList</code> which
		 * will be added to the <code>feature</code> of the
		 * <code>container</code>
		 *
		 * @param resultList
		 *            the list that will contain all the replacements
		 * @param obj
		 *            the object that should be inserted
		 * @param container
		 *            the object that will contain the obj
		 * @param feature
		 *            the feature of the container that will contain the obj
		 * @param unmodified
		 *            if <code>true</code> no further substitution will be
		 *            attempted
		 */
		@SuppressWarnings("unchecked")
		private <T> void singleAdd(ArrayList<T> resultList, T obj, IHDLObject container, String feature, boolean unmodified) {
			if (obj instanceof IHDLObject) {
				if (unmodified) {
					resultList.add((T) ((IHDLObject) obj).copyFiltered(this));
				} else {
					resultList.addAll(copyContainer(feature, container, Collections.singleton(obj)));
				}
			} else {
				resultList.add(obj);
			}
		}

	}

	private static enum ModificationType {
		REPLACE, INSERT_BEFORE, INSERT_AFTER, ADD, REMOVE;
	}

	/**
	 * A storage class for all modifications that are scheduled
	 *
	 */
	private static class Modification {
		private static final AtomicInteger gid = new AtomicInteger();

		public final Integer modificationID = gid.incrementAndGet();
		public final IHDLObject subject;
		public final String feature;
		public final List<Object> with;
		public final ModificationType type;

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append("Modification [type=");
			builder.append(type);
			builder.append(", subject=");
			builder.append(subject);
			builder.append(", feature=");
			builder.append(feature);
			builder.append(", with=");
			builder.append(with);
			builder.append(", modificationID=");
			builder.append(modificationID);
			builder.append("]");
			return builder.toString();
		}

		public Modification(IHDLObject subject, ModificationType type, String feature, Object... with) {
			super();
			this.subject = subject;
			this.with = Arrays.asList(with);
			this.type = type;
			this.feature = feature;
		}
	}

	private final Map<IHDLObject, List<Modification>> replacements = Maps.newIdentityHashMap();
	/**
	 * Contains the modifications that are currently applied. A modification
	 * that is currently applied, should not be applied to avoid recursion
	 */
	private final Set<Integer> currentMods = Sets.newHashSet();

	private <T> List<Modification> getModifications(T object) {
		return replacements.get(object);
	}

	/**
	 * Removes a subject
	 *
	 * @param subject
	 */
	public void remove(IHDLObject subject) {
		final IHDLObject container = subject.getContainer();
		if (container != null) {
			final HDLFieldAccess<?, ?> feature = container.getContainingFeature(subject);
			// The feature can be null when the container is set
			// aritifically for resolution reasons
			if (feature != null) {
				switch (feature.quantifier) {
				case ONE:
				case ONE_OR_MORE:
					throw new IllegalArgumentException("Can not remove feature:" + feature.fieldName + " at least one is required");
				default:
					break;
				}
			}
		}
		final Modification mod = new Modification(subject, ModificationType.REMOVE, null);
		insert(subject, mod);
	}

	/**
	 * Replace the subject with other objects. If the with is null or empty, the
	 * subject will be removed.
	 *
	 * @param subject
	 * @param with
	 */
	public void replace(IHDLObject subject, IHDLObject... with) {
		if ((with == null) || (with.length == 0)) {
			remove(subject);
		} else {
			final IHDLObject container = subject.getContainer();
			if (container != null) {
				final HDLFieldAccess<?, ?> feature = container.getContainingFeature(subject);
				// The feature can be null when the container is set
				// aritifically for resolution reasons
				if (feature != null) {
					switch (feature.quantifier) {
					case ONE:
					case ZERO_OR_ONE:
						if (with.length != 1)
							throw new IllegalArgumentException("Can not replace feature:" + feature.fieldName + " with multiple objects");
						break;
					default:
						break;
					}
				}
			}
			final Modification mod = new Modification(subject, ModificationType.REPLACE, null, (Object[]) with);
			insert(subject, mod);
		}
	}

	/**
	 * Inserts the objects after the subject. This only works if the subject is
	 * contained in a List.
	 *
	 * @param subject
	 * @param with
	 */
	public void insertAfter(IHDLObject subject, IHDLObject... with) {
		if ((with == null) || (with.length == 0))
			return;
		final IHDLObject container = subject.getContainer();
		if (container != null) {
			final HDLFieldAccess<?, ?> feature = container.getContainingFeature(subject);
			if (feature != null) {
				if ((feature.quantifier == Quantifier.ZERO_OR_ONE) || (feature.quantifier == Quantifier.ONE))
					throw new IllegalArgumentException("Can not perform insertAfter on feature: " + feature.fieldName + " not a collection");
				for (final IHDLObject object : with) {
					if (!feature.type.isAssignableFrom(object.getClass()))
						throw new IllegalArgumentException("Can not insertAfter type:" + object.getClassType() + " to feature: " + feature.fieldName);
				}
			}
		}
		final Modification mod = new Modification(subject, ModificationType.INSERT_AFTER, null, (Object[]) with);
		insert(subject, mod);
	}

	/**
	 * Insert objects before the subject. This only works if the subject is
	 * contained in a List.
	 *
	 * @param subject
	 * @param with
	 */
	public void insertBefore(IHDLObject subject, IHDLObject... with) {
		if ((with == null) || (with.length == 0))
			return;
		final IHDLObject container = subject.getContainer();
		if (container != null) {
			final HDLFieldAccess<?, ?> feature = container.getContainingFeature(subject);
			if (feature != null) {
				if ((feature.quantifier == Quantifier.ZERO_OR_ONE) || (feature.quantifier == Quantifier.ONE))
					throw new IllegalArgumentException("Can not perform insertBefore on feature: " + feature.fieldName + " not a collection");
				for (final IHDLObject object : with) {
					if (!feature.type.isAssignableFrom(object.getClass()))
						throw new IllegalArgumentException("Can not insertBefore type:" + object.getClassType() + " to feature: " + feature.fieldName);
				}
			}
		}
		final Modification mod = new Modification(subject, ModificationType.INSERT_BEFORE, null, (Object[]) with);
		insert(subject, mod);
	}

	private void insert(IHDLObject subject, Modification mod) {
		List<Modification> list = replacements.get(subject);
		if (list == null) {
			list = new LinkedList<ModificationSet.Modification>();
		}
		list.add(mod);
		replacements.put(subject, list);
	}

	/**
	 * Adds objects to a feature of the subject
	 *
	 * @param subject
	 * @param field
	 * @param add
	 */
	public <T> void addTo(IHDLObject subject, HDLFieldAccess<?, ArrayList<T>> field, @SuppressWarnings("unchecked") T... add) {
		if ((add == null) || (add.length == 0))
			return;
		for (final T t : add) {
			if (!field.type.isAssignableFrom(t.getClass()))
				throw new IllegalArgumentException("Can not add type: " + t.getClass() + " to feature: " + field.fieldName + ", incompatible types");
		}
		final Modification mod = new Modification(subject, ModificationType.ADD, field.fieldName, add);
		insert(subject, mod);
	}

	/**
	 * Executes all outstanding modifications or returns the original object if
	 * nothing needs to be done
	 *
	 * @param orig
	 * @return a modified {@link IHDLObject} object
	 */
	@SuppressWarnings("unchecked")
	public <T extends IHDLObject> T apply(T orig) {
		if (replacements.size() == 0)
			return orig;
		final T newR = getReplacement(orig);
		final T res = (T) newR.copyFiltered(new MSCopyFilter());
		res.freeze(orig.getContainer());
		return res;
	}

	/**
	 * Checks whether any replacements are planned and returns the first object
	 * that should replace the subject
	 *
	 * @param reg
	 * @return an object that is scheduled to replace the oject or
	 *         <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public <T extends IHDLObject> T getReplacement(T reg) {
		final List<Modification> mods = getModifications(reg);
		if ((mods == null) || mods.isEmpty())
			return reg;
		final Modification mod = mods.get(0);
		if (mod.type != ModificationType.ADD)
			return (T) mod.with.get(0);
		return reg;
	}

	/**
	 * Replace the subject IHDLObject with the subjects, but discard all other
	 * planned modifications
	 *
	 * @param subject
	 * @param with
	 */
	public void replacePrune(IHDLObject subject, IHDLObject... with) {
		prune(subject);
		replace(subject, with);
	}

	private void prune(IHDLObject subject) {
		final List<Modification> list = replacements.get(subject);
		if (list != null) {
			final Iterator<Modification> iter = list.iterator();
			while (iter.hasNext()) {
				final ModificationSet.Modification mod = iter.next();
				if (mod.subject == subject) {
					iter.remove();
				}
			}
		}
	}

	@Override
	public String toString() {
		return replacements.toString();
	}

}

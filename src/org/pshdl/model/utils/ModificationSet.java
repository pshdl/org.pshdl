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

import java.util.*;
import java.util.concurrent.atomic.*;

import org.pshdl.model.*;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

public class ModificationSet {

	private static final AtomicInteger gid = new AtomicInteger();
	private int id = gid.incrementAndGet();

	private static enum ModID implements MetaAccess<Integer> {
		id;

		@Override
		public boolean inherit() {
			return false;
		}
	}

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
							throw new IllegalArgumentException("Can not replace with more than one object into a single node for feature:" + feature + " of "
									+ container.getClass());
						final IHDLObject replacement = (IHDLObject) modification.with.get(0);
						original.addMeta(ModID.id, id);
						final T copyFiltered = (T) replacement.copyFiltered(this);
						return copyFiltered;
					}
					throw new IllegalArgumentException("Can not insert into a single node for feature:" + feature + " of " + container);
				}
			}
			return super.copyObject(feature, container, original);
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> ArrayList<T> copyContainer(String feature, HDLObject container, Iterable<T> object) {
			if (object != null) {
				final ArrayList<T> res = new ArrayList<T>();
				for (final T t : object) {
					final List<Modification> mods = getModifications(t);
					if (t instanceof IHDLObject) {
						final IHDLObject newT = (IHDLObject) t;
						newT.addMeta(ModID.id, id);
					}
					if (mods != null) {
						final List<T> before = new LinkedList<T>();
						final List<T> after = new LinkedList<T>();
						final List<T> replace = new LinkedList<T>();
						boolean remove = false;
						for (final Modification modification : mods) {
							switch (modification.type) {
							case INSERT_AFTER:
								after.addAll((Collection<? extends T>) modification.with);
								break;
							case INSERT_BEFORE:
								before.addAll((Collection<? extends T>) modification.with);
								break;
							case REPLACE:
								replace.addAll((Collection<? extends T>) modification.with);
								break;
							case ADD:
								break;
							case REMOVE:
								remove = true;
								break;
							}
						}
						multiAdd(res, before, container);
						if (replace.size() != 0) {
							multiAdd(res, replace, container);
						} else {
							if (!remove) {
								singleAdd(res, t, container);
							}
						}
						multiAdd(res, after, container);
					} else {
						singleAdd(res, t, container);
					}
				}
				final List<Modification> mods = getModWithoutCheck(container);
				if (mods != null) {
					for (final Modification mod : mods)
						if ((mod.type == ModificationType.ADD) && feature.equals(mod.feature)) {
							multiAdd(res, (List<T>) mod.with, container);
						}
				}
				return res;
			}
			final List<Modification> mods = getModWithoutCheck(container);
			if (mods != null) {
				final ArrayList<T> res = new ArrayList<T>();
				for (final Modification mod : mods)
					if ((mod.type == ModificationType.ADD) && feature.equals(mod.feature)) {
						multiAdd(res, (List<T>) mod.with, container);
					}
				if (res.size() != 0)
					return res;
			}
			return null;
		}

		private <T> void multiAdd(ArrayList<T> res, List<T> list, IHDLObject container) {
			for (final T element : list) {
				singleAdd(res, element, container);
			}
		}

		@SuppressWarnings("unchecked")
		private <T> void singleAdd(ArrayList<T> res, T t, IHDLObject container) {
			if (t instanceof IHDLObject) {
				final IHDLObject newT = (IHDLObject) t;
				// newT.addMeta(ModID.id, id);
				// Marking not needed here as it will be taken care of in
				// copyObject
				final T copyFiltered = (T) newT.copyFiltered(this);
				res.add(copyFiltered);
			} else {
				res.add(t);
			}
		}

	}

	private <T> int getHash(T t) {
		return System.identityHashCode(t);
	}

	private static enum ModificationType {
		REPLACE, INSERT_BEFORE, INSERT_AFTER, ADD, REMOVE;
	}

	private static class Modification {
		public final IHDLObject subject;
		public final String feature;
		public final List<Object> with;
		public final ModificationType type;

		@Override
		public String toString() {
			return "Modification [subject=" + subject + ", with=" + with + ", type=" + type + "]";
		}

		public Modification(IHDLObject subject, ModificationType type, String feature, Object... with) {
			super();
			this.subject = subject;
			this.with = Arrays.asList(with);
			this.type = type;
			this.feature = feature;
		}
	}

	private final Map<Integer, List<Modification>> replacements = new HashMap<Integer, List<Modification>>();

	private <T> List<Modification> getModifications(T object) {
		if (object instanceof IHDLObject) {
			final IHDLObject original = (IHDLObject) object;
			final Integer modID = original.getMeta(ModID.id);
			if (modID != null)
				if (modID == id)
					return null;
		}
		return getModWithoutCheck(object);
	}

	private <T> List<Modification> getModWithoutCheck(T object) {
		final List<Modification> list = replacements.get(getHash(object));
		if (list != null) {
			final List<Modification> res = new LinkedList<ModificationSet.Modification>();
			for (final Modification modification : list)
				if (modification.subject == object) {
					res.add(modification);
				}
			return res;
		}
		return null;
	}

	public void remove(IHDLObject subject) {
		final Modification mod = new Modification(subject, ModificationType.REMOVE, null);
		insert(subject, mod);
	}

	public void replace(IHDLObject subject, IHDLObject... with) {
		final Modification mod = new Modification(subject, ModificationType.REPLACE, null, (Object[]) with);
		insert(subject, mod);
	}

	public void insertAfter(IHDLObject subject, IHDLObject... with) {
		final Modification mod = new Modification(subject, ModificationType.INSERT_AFTER, null, (Object[]) with);
		insert(subject, mod);
	}

	public void insertBefore(IHDLObject subject, IHDLObject... with) {
		final Modification mod = new Modification(subject, ModificationType.INSERT_BEFORE, null, (Object[]) with);
		insert(subject, mod);
	}

	private void insert(IHDLObject subject, Modification mod) {
		List<Modification> list = replacements.get(getHash(subject));
		if (list == null) {
			list = new LinkedList<ModificationSet.Modification>();
		}
		list.add(mod);
		replacements.put(getHash(subject), list);
	}

	public <T> void addTo(IHDLObject subject, HDLFieldAccess<?, ArrayList<T>> field, T... add) {
		final Modification mod = new Modification(subject, ModificationType.ADD, field.fieldName, add);
		insert(subject, mod);
	}

	/**
	 * Executes all outstanding modifications or returns the orignal object if
	 * nothing needs to be done
	 *
	 * @param orig
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends IHDLObject> T apply(T orig) {
		if (replacements.size() == 0)
			return orig;
		id = gid.incrementAndGet();
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
	 * @return
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
		final List<Modification> list = replacements.get(getHash(subject));
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

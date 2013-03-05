package de.tuhh.ict.pshdl.model.utils;

import java.util.*;
import java.util.concurrent.atomic.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

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
			List<Modification> mods = getModifications(original);
			if (mods != null) {
				for (Modification modification : mods) {
					if (modification.type == ModificationType.REPLACE) {
						if (modification.with.size() > 1)
							throw new IllegalArgumentException("Can not replace with more than one object into a single node for feature:" + feature + " of "
									+ container.getClass());
						IHDLObject replacement = modification.with.get(0);
						original.addMeta(ModID.id, id);
						T copyFiltered = (T) replacement.copyFiltered(this);
						return copyFiltered;
					}
					throw new IllegalArgumentException("Can not insert into a single node for feature:" + feature + " of " + container);
				}
			}
			return super.copyObject(feature, container, original);
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> ArrayList<T> copyContainer(String feature, HDLObject container, ArrayList<T> object) {
			if (object != null) {
				ArrayList<T> res = new ArrayList<T>();
				for (T t : object) {
					List<Modification> mods = getModifications(t);
					if (t instanceof IHDLObject) {
						IHDLObject newT = (IHDLObject) t;
						newT.addMeta(ModID.id, id);
					}
					if (mods != null) {
						List<T> before = new LinkedList<T>();
						List<T> after = new LinkedList<T>();
						List<T> replace = new LinkedList<T>();
						for (Modification modification : mods) {
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
							}
						}
						multiAdd(res, before, container);
						if (replace.size() != 0) {
							multiAdd(res, replace, container);
						} else {
							singleAdd(res, t, container);
						}
						multiAdd(res, after, container);
					} else {
						singleAdd(res, t, container);
					}
				}
				List<Modification> mods = getModWithoutCheck(container);
				if (mods != null) {
					for (Modification mod : mods)
						if ((mod.type == ModificationType.ADD) && feature.equals(mod.feature)) {
							multiAdd(res, (List<T>) mod.with, container);
						}
				}
				return res;
			}
			List<Modification> mods = getModWithoutCheck(container);
			if (mods != null) {
				ArrayList<T> res = new ArrayList<T>();
				for (Modification mod : mods)
					if ((mod.type == ModificationType.ADD) && feature.equals(mod.feature)) {
						multiAdd(res, (List<T>) mod.with, container);
					}
				if (res.size() != 0)
					return res;
			}
			return null;
		}

		private <T> void multiAdd(ArrayList<T> res, List<T> list, IHDLObject container) {
			for (T element : list) {
				singleAdd(res, element, container);
			}
		}

		@SuppressWarnings("unchecked")
		private <T> void singleAdd(ArrayList<T> res, T t, IHDLObject container) {
			if (t instanceof IHDLObject) {
				IHDLObject newT = (IHDLObject) t;
				// newT.addMeta(ModID.id, id);
				// Marking not needed here as it will be taken care of in
				// copyObject
				T copyFiltered = (T) newT.copyFiltered(this);
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
		REPLACE, INSERT_BEFORE, INSERT_AFTER, ADD;
	}

	private static class Modification {
		public final IHDLObject subject;
		public final String feature;
		public final List<IHDLObject> with;
		public final ModificationType type;

		@Override
		public String toString() {
			return "Modification [subject=" + subject + ", with=" + with + ", type=" + type + "]";
		}

		public Modification(IHDLObject subject, ModificationType type, String feature, IHDLObject... with) {
			super();
			List<IHDLObject> with1 = Arrays.asList(with);
			this.subject = subject;
			for (int i = 0; i < with1.size(); i++) {
				with1.set(i, with1.get(i));
			}
			this.with = with1;
			this.type = type;
			this.feature = feature;
		}
	}

	private Map<Integer, List<Modification>> replacements = new HashMap<Integer, List<Modification>>();

	private <T> List<Modification> getModifications(T object) {
		if (object instanceof IHDLObject) {
			IHDLObject original = (IHDLObject) object;
			Integer modID = original.getMeta(ModID.id);
			if (modID != null)
				if (modID == id)
					return null;
		}
		return getModWithoutCheck(object);
	}

	private <T> List<Modification> getModWithoutCheck(T object) {
		List<Modification> list = replacements.get(getHash(object));
		if (list != null) {
			List<Modification> res = new LinkedList<ModificationSet.Modification>();
			for (Modification modification : list)
				if (modification.subject == object) {
					res.add(modification);
				}
			return res;
		}
		return null;
	}

	public void replace(IHDLObject subject, IHDLObject... with) {
		Modification mod = new Modification(subject, ModificationType.REPLACE, null, with);
		insert(subject, mod);
	}

	public void insertAfter(IHDLObject subject, IHDLObject... with) {
		Modification mod = new Modification(subject, ModificationType.INSERT_AFTER, null, with);
		insert(subject, mod);
	}

	public void insertBefore(IHDLObject subject, IHDLObject... with) {
		Modification mod = new Modification(subject, ModificationType.INSERT_BEFORE, null, with);
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

	public void addTo(IHDLObject subject, HDLFieldAccess<?, ?> field, IHDLObject... add) {
		Modification mod = new Modification(subject, ModificationType.ADD, field.fieldName, add);
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
		T newR = getReplacement(orig);
		T res = (T) newR.copyFiltered(new MSCopyFilter());
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
		List<Modification> mods = getModifications(reg);
		if ((mods == null) || mods.isEmpty())
			return reg;
		Modification mod = mods.get(0);
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
		List<Modification> list = replacements.get(getHash(subject));
		if (list != null) {
			Iterator<Modification> iter = list.iterator();
			while (iter.hasNext()) {
				ModificationSet.Modification mod = iter.next();
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

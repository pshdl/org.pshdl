package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public class ModificationSet {

	private class MSCopyFilter extends CopyFilter.DeepCloneFilter {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends HDLObject> T copyObject(String feature, HDLObject container, T object) {
			List<Modification> mods = getModifications(object);
			if (mods != null) {
				for (Modification modification : mods) {
					if (modification.type == ModificationType.REPLACE) {
						if (modification.with.size() > 1)
							throw new IllegalArgumentException("Can not replace with more than one object into a single node for feature:" + feature + " of "
									+ container.getClass());
						// System.out.println("ModificationSet.MSCopyFilter.copyContainer() Applying modification:"
						// + modification);
						return (T) modification.with.get(0).copyFiltered(this);
					}
					throw new IllegalArgumentException("Can not insert into a single node for feature:" + feature + " of " + container);
				}
			}
			return super.copyObject(feature, container, object);
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> ArrayList<T> copyContainer(String feature, HDLObject container, ArrayList<T> object) {
			if (object != null) {
				ArrayList<T> res = new ArrayList<T>();
				for (T t : object) {
					List<Modification> mods = getModifications(t);
					if (mods != null) {
						List<T> before = new LinkedList<T>();
						List<T> after = new LinkedList<T>();
						List<T> replace = new LinkedList<T>();
						for (Modification modification : mods) {
							// System.out.println("ModificationSet.MSCopyFilter.copyContainer() Applying modification:"
							// + modification);
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
							}
						}
						multiAdd(res, before, container);
						if (replace.size() != 0)
							multiAdd(res, replace, container);
						else
							singleAdd(res, t, container);
						multiAdd(res, after, container);
					} else {
						singleAdd(res, t, container);
					}
				}
				return res;
			}
			return null;
		}

		private <T> void multiAdd(ArrayList<T> res, List<T> before, HDLObject container) {
			for (T element : before) {
				singleAdd(res, element, container);
			}
		}

		@SuppressWarnings("unchecked")
		private <T> void singleAdd(ArrayList<T> res, T t, HDLObject container) {
			if (t instanceof HDLObject) {
				HDLObject newT = (HDLObject) t;
				newT.copy().setContainer(container);
				res.add((T) newT.copyFiltered(this));
			} else {
				res.add(t);
			}
		}

	}

	private <T> int getHash(T t) {
		return System.identityHashCode(t);
	}

	private static enum ModificationType {
		REPLACE, INSERT_BEFORE, INSERT_AFTER;
	}

	private static class Modification {
		public final HDLObject subject;
		public final List<HDLObject> with;
		public final ModificationType type;

		@Override
		public String toString() {
			return "Modification [subject=" + subject + ", with=" + with + ", type=" + type + "]";
		}

		public Modification(HDLObject subject, ModificationType type, HDLObject... with) {
			this(subject, Arrays.asList(with), type);
		}

		public Modification(HDLObject subject, List<HDLObject> with, ModificationType type) {
			super();
			this.subject = subject;
			HDLObject container = subject.getContainer();
			for (int i = 0; i < with.size(); i++) {
				HDLObject obj = with.get(i);
				with.set(i, obj.copy().setContainer(container));
			}
			this.with = with;
			this.type = type;
		}
	}

	private Map<Integer, List<Modification>> replacements = new HashMap<Integer, List<Modification>>();

	private <T> List<Modification> getModifications(T object) {
		List<Modification> list = replacements.get(getHash(object));
		if (list != null) {
			List<Modification> res = new LinkedList<ModificationSet.Modification>();
			for (Modification modification : list) {
				if (modification.subject == object)
					res.add(modification);
			}
			return res;
		}
		return null;
	}

	public void replace(HDLObject subject, HDLObject... with) {
		Modification mod = new Modification(subject, ModificationType.REPLACE, with);
		insert(subject, mod);
	}

	public void insertAfter(HDLObject subject, HDLObject... with) {
		Modification mod = new Modification(subject, ModificationType.INSERT_AFTER, with);
		insert(subject, mod);
	}

	public void insertBefore(HDLObject subject, HDLObject... with) {
		Modification mod = new Modification(subject, ModificationType.INSERT_BEFORE, with);
		insert(subject, mod);
	}

	private void insert(HDLObject subject, Modification mod) {
		List<Modification> list = replacements.get(getHash(subject));
		if (list == null)
			list = new LinkedList<ModificationSet.Modification>();
		list.add(mod);
		replacements.put(getHash(subject), list);
	}

	/**
	 * Executes all outstanding modifications or returns the orignal object if
	 * nothing needs to be done
	 * 
	 * @param orig
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends HDLObject> T apply(T orig) {
		if (replacements.size() == 0)
			return orig;
		return (T) orig.copyFiltered(new MSCopyFilter());
	}

	/**
	 * Checks whether any replacements are planned and returns the first object
	 * that should replace the subject
	 * 
	 * @param reg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends HDLObject> T getReplacement(T reg) {
		List<Modification> mods = getModifications(reg);
		if ((mods == null) || mods.isEmpty())
			return reg;
		return (T) mods.get(0).with.get(0);
	}

	/**
	 * Replace the subject HDLObject with the subjects, but discard all other
	 * planned modifications
	 * 
	 * @param subject
	 * @param with
	 */
	public void replacePrune(HDLObject subject, HDLObject... with) {
		prune(subject);
		replace(subject, with);
	}

	private void prune(HDLObject subject) {
		List<Modification> list = replacements.get(getHash(subject));
		if (list != null) {
			Iterator<Modification> iter = list.iterator();
			while (iter.hasNext()) {
				ModificationSet.Modification mod = iter.next();
				if (mod.subject == subject)
					iter.remove();
			}
		}
	}

}

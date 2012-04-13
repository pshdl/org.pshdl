package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

//XXX It is a bad idea to rely on the idea that System.identityHashCode is returning a unique ID.
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
						System.out.println("ModificationSet.MSCopyFilter.copyContainer() Applying modification:" + modification);
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
							System.out.println("ModificationSet.MSCopyFilter.copyContainer() Applying modification:" + modification);
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
						res.addAll(before);
						if (replace.size() != 0)
							res.addAll(replace);
						else
							singleAdd(res, t);
						res.addAll(after);
					} else {
						singleAdd(res, t);
					}
				}
				return res;
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		private <T> void singleAdd(ArrayList<T> res, T t) {
			if (t instanceof HDLObject) {
				HDLObject newT = (HDLObject) t;
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
			for (HDLObject hdlObject : with) {
				hdlObject.setContainer(subject.getContainer());
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
		System.out.println("ModificationSet.replace()" + subject + " with " + Arrays.toString(with));
		Modification mod = new Modification(subject, ModificationType.REPLACE, with);
		insert(subject, mod);
	}

	public void insertAfter(HDLObject subject, HDLObject... with) {
		System.out.println("ModificationSet.insertAfter()" + subject + " with " + Arrays.toString(with));
		Modification mod = new Modification(subject, ModificationType.INSERT_AFTER, with);
		insert(subject, mod);
	}

	public void insertBefore(HDLObject subject, HDLObject... with) {
		System.out.println("ModificationSet.insertBefore()" + subject + " with " + Arrays.toString(with));
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

	public HDLPackage apply(HDLPackage orig) {
		if (replacements.size() == 0)
			return orig;
		return orig.copyFiltered(new MSCopyFilter());
	}

}

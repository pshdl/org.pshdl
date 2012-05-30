package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public interface CopyFilter {
	CopyFilter DEEP = new DeepCloneFilter();

	public <T extends Enum<T>> T copyObject(String feature, HDLObject container, T object);

	public String copyObject(String feature, HDLObject container, String object);

	public boolean copyObject(String feature, HDLObject container, boolean object);

	public HDLQualifiedName copyObject(String feature, HDLObject container, HDLQualifiedName object);

	public <T extends HDLObject> T copyObject(String feature, HDLObject container, T object);

	public <T> ArrayList<T> copyContainer(String feature, HDLObject container, ArrayList<T> object);

	public static class DeepCloneFilter implements CopyFilter {

		@Override
		public <T extends Enum<T>> T copyObject(String feature, HDLObject container, T object) {
			return object;
		}

		@Override
		public String copyObject(String feature, HDLObject container, String object) {
			return object;
		}

		@Override
		public HDLQualifiedName copyObject(String feature, HDLObject container, HDLQualifiedName object) {
			return object;
		}

		@Override
		public boolean copyObject(String feature, HDLObject container, boolean object) {
			return object;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends HDLObject> T copyObject(String feature, HDLObject container, T object) {
			if (object == null)
				return null;
			return (T) object.copyFiltered(this);
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> ArrayList<T> copyContainer(String feature, HDLObject container, ArrayList<T> object) {
			if (object == null)
				return null;
			ArrayList<T> res = new ArrayList<T>();
			for (T t : object) {
				if (t instanceof HDLObject) {
					HDLObject obj = (HDLObject) t;
					res.add((T) obj.copyFiltered(this));
				} else
					res.add(t);
			}
			return res;
		}

	}

}

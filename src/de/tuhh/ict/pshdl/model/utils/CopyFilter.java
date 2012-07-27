package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public interface CopyFilter {
	CopyFilter DEEP = new DeepCloneFilter();

	public <T extends Enum<T>> T copyObject(String feature, IHDLObject container, T object);

	public String copyObject(String feature, IHDLObject container, String object);

	public Boolean copyObject(String feature, IHDLObject container, Boolean object);

	public HDLQualifiedName copyObject(String feature, IHDLObject container, HDLQualifiedName object);

	public <T extends IHDLObject> T copyObject(String feature, IHDLObject container, T object);

	public <T> ArrayList<T> copyContainer(String feature, HDLObject container, ArrayList<T> object);

	public static class DeepCloneFilter implements CopyFilter {

		@Override
		public <T extends Enum<T>> T copyObject(String feature, IHDLObject container, T object) {
			return object;
		}

		@Override
		public String copyObject(String feature, IHDLObject container, String object) {
			return object;
		}

		@Override
		public HDLQualifiedName copyObject(String feature, IHDLObject container, HDLQualifiedName object) {
			return object;
		}

		@Override
		public Boolean copyObject(String feature, IHDLObject container, Boolean object) {
			return object;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends IHDLObject> T copyObject(String feature, IHDLObject container, T object) {
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
					IHDLObject obj = (IHDLObject) t;
					res.add((T) obj.copyFiltered(this));
				} else
					res.add(t);
			}
			return res;
		}

	}

}

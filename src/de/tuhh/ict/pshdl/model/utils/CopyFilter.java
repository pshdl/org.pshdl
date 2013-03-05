package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public interface CopyFilter {
	CopyFilter DEEP_META = new DeepCloneFilter();

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
			T copyFiltered = (T) object.copyFiltered(this);
			return copyFiltered;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> ArrayList<T> copyContainer(String feature, HDLObject container, ArrayList<T> object) {
			if (object == null)
				return null;
			ArrayList<T> res = new ArrayList<T>();
			for (T t : object)
				if (t instanceof HDLObject) {
					IHDLObject obj = (IHDLObject) t;
					T copyFiltered = (T) obj.copyFiltered(this);
					res.add(copyFiltered);
				} else {
					res.add(t);
				}
			return res;
		}

		@Override
		public <T extends IHDLObject> T postFilter(T src, T target) {
			HDLObject.copyMetaData(src, target, false);
			return target;
		}

	}

	public <T extends IHDLObject> T postFilter(T src, T target);
}

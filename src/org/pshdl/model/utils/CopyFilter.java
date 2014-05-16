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

import org.pshdl.model.HDLObject;
import org.pshdl.model.IHDLObject;

public interface CopyFilter {
	CopyFilter DEEP_META = new DeepCloneFilter();

	public <T extends Enum<T>> T copyObject(String feature, IHDLObject container, T object);

	public String copyObject(String feature, IHDLObject container, String object);

	public Boolean copyObject(String feature, IHDLObject container, Boolean object);

	public Integer copyObject(String feature, IHDLObject container, Integer object);

	public HDLQualifiedName copyObject(String feature, IHDLObject container, HDLQualifiedName object);

	public <T extends IHDLObject> T copyObject(String feature, IHDLObject container, T object);

	public <T> ArrayList<T> copyContainer(String feature, IHDLObject container, Iterable<T> object);

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
		public Integer copyObject(String feature, IHDLObject container, Integer object) {
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
		public <T> ArrayList<T> copyContainer(String feature, IHDLObject container, Iterable<T> object) {
			if (object == null)
				return null;
			final ArrayList<T> res = new ArrayList<T>();
			for (final T t : object)
				if (t instanceof HDLObject) {
					final IHDLObject obj = (IHDLObject) t;
					final T copyFiltered = (T) obj.copyFiltered(this);
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

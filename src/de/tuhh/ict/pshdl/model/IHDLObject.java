package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.FieldMatcher;

public interface IHDLObject {
	public HDLClass getClassType();

	public void copyMetaData(IHDLObject src, IHDLObject target);

	public abstract IHDLObject copy();

	public abstract IHDLObject copyFiltered(CopyFilter filter);

	public void addMeta(String key, Object value);

	public Object getMeta(String key);

	public <K> void addMeta(MetaAccess<K> key, K value);

	public void setMeta(MetaAccess<Boolean> meta);

	public boolean hasMeta(MetaAccess<?> key);

	public <K> K getMeta(MetaAccess<K> key);

	public <T> Set<T> getAllObjectsOf(Class<? extends T> clazz, boolean deep);

	public <T, K> Set<T> getAllObjectsOf(Class<T> clazz, HDLQuery.HDLFieldAccess<T, K> field, FieldMatcher<K> matcher);

	public <T extends IHDLObject> T getContainer(Class<T> clazz);

	public Iterator<IHDLObject> iterator();

	public Iterator<IHDLObject> iterator(boolean deep);

	public IHDLObject getContainer();

	public IHDLObject setContainer(IHDLObject container);

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve);

	public String toConstructionString(String spacing);

	public void freeze();

	public HDLQualifiedName getFullName();

	public HDLFunction resolveFunction(HDLQualifiedName name);

}

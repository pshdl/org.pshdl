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
package org.pshdl.model;

import java.util.*;

import org.pshdl.model.utils.*;

import com.google.common.base.*;

public interface IHDLObject {
	/**
	 * Returns the most precise class that this {@link IHDLObject} implements
	 * 
	 * @return the class that implements this interface
	 */
	public HDLClass getClassType();

	/**
	 * Copies this object using the provded {@link CopyFilter}
	 * 
	 * @param filter
	 *            an implementation of the {@link CopyFilter} such as
	 *            {@link CopyFilter.DeepCloneFilter}
	 * @return the newly created copy
	 */
	public abstract IHDLObject copyFiltered(CopyFilter filter);

	/**
	 * Applies {@link CopyFilter.DeepCloneFilter} and calls
	 * {@link #freeze(IHDLObject)} afterwards on the copy
	 * 
	 * @param container
	 *            the new {@link IHDLObject} that should become the container of
	 *            the copy
	 * @return a frozen copy of this object
	 */
	public abstract IHDLObject copyDeepFrozen(IHDLObject container);

	/**
	 * Attaches some information to this {@link IHDLObject}
	 * 
	 * @param key
	 *            {@link MetaAccess#name()} will be used to store the value. If
	 *            it already exists, it will be overwritten
	 * @param value
	 *            the value that should be attached to this object
	 */
	public <K> void addMeta(MetaAccess<K> key, K value);

	/**
	 * A boolean meta that can be used as a flag
	 * 
	 * @param meta
	 *            {@link MetaAccess#name()} will be used to store
	 *            <code>true</code> for this key
	 */
	public void setMeta(MetaAccess<Boolean> meta);

	/**
	 * Check whether the given {@link MetaAccess} is present on this object
	 * 
	 * @param key
	 *            the key to lookup
	 * @return <code>true</code> if meta with this key has been attached,
	 *         <code>false</code> otherwise
	 */
	public boolean hasMeta(MetaAccess<?> key);

	/**
	 * Retrieve the value stored by this {@link MetaAccess}
	 * 
	 * @param key
	 *            {@link MetaAccess#name()} will be used to retrieve the value
	 * @return the attached value or <code>null</code> if no value has been set
	 *         for this key
	 */
	public <K> K getMeta(MetaAccess<K> key);

	/**
	 * Retrieves all objects of the given class and subclasses that are children
	 * of this object, or the object itself as well. Consider the following
	 * example:
	 * 
	 * <pre>
	 * HDLForLoop (forLoop)
	 * +--HDLAssignment (assA)
	 *    +--HDLVariableRef (varRefA) HDLLiteral
	 * +--HDLAssignment (assB)
	 *    +--HDLVariableRef (varRefB) HDLEnumRef (enumRef)
	 * +--HDLIfStatement
	 *    +--HDLAssignment (assC)
	 *       +--HDLVariableRef (varRefC) HDLLiteral
	 * </pre>
	 * <ul>
	 * <li>The invocation of
	 * <code>forLoop.getAllObjectsOf(HDLForLoop.class, true)</code> will return
	 * <code>[forLoop]</code></li>
	 * <li>The invocation of
	 * <code>forLoop.getAllObjectsOf(HDLReference.class, true)</code> will
	 * return <code>[varRefA, varRefB, varRefC, enumRef]</code></li>
	 * <li>The invocation of
	 * <code>forLoop.getAllObjectsOf(HDLReference.class, false)</code> will
	 * return <code>[]</code></li>
	 * <li>The invocation of
	 * <code>forLoop.getAllObjectsOf(HDLAssignment.class, false)</code> will
	 * return <code>[assA, assB]</code></li>
	 * </ul>
	 * 
	 * @param clazz
	 *            all instances of this class and its subclasses will be
	 *            returned
	 * @param deep
	 *            if <code>true</code> all children of this object are returned,
	 *            if <code>false</code> only direct children of this object are
	 *            returned
	 * @return all found objects that are the direct or sub class of the given
	 *         clazz. An empty array is returned if no instance could be found
	 */
	public <T> T[] getAllObjectsOf(Class<? extends T> clazz, boolean deep);

	/**
	 * Retrieves all instances of the given class and then queries them for
	 * certain properties. The best way to use this API is to use it with
	 * {@link HDLQuery}
	 * 
	 * @param clazz
	 *            all instances of this class and its subclasses will be checked
	 *            for matching fields
	 * @param field
	 *            the field to check
	 * @param matcher
	 *            if any of the {@link Predicate} evaluates to true the
	 *            particular instance of the field will be returned
	 * @return all instances that match the predicate of on the field of the
	 *         given class
	 */
	public <T, K> Set<T> getAllObjectsOf(Class<T> clazz, HDLQuery.HDLFieldAccess<T, K> field, Predicate<K>... matcher);

	/**
	 * This iterator iterates over all children and their children in a depth
	 * first manner.
	 * 
	 * @return an iterator over all children and their children
	 */
	public Iterator<IHDLObject> iterator();

	public Iterator<IHDLObject> iterator(boolean deep);

	public IHDLObject getContainer();

	public <T extends IHDLObject> T getContainer(Class<T> clazz);

	public IHDLObject setContainer(IHDLObject container);

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve);

	public String toConstructionString(String spacing);

	public IHDLObject freeze(IHDLObject container);

	public boolean isFrozen();

	public EnumSet<HDLClass> getClassSet();

}

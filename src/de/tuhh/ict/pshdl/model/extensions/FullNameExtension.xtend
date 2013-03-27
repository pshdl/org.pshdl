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
package de.tuhh.ict.pshdl.model.extensions

import de.tuhh.ict.pshdl.model.HDLBlock
import de.tuhh.ict.pshdl.model.HDLEnum
import de.tuhh.ict.pshdl.model.HDLForLoop
import de.tuhh.ict.pshdl.model.HDLFunction
import de.tuhh.ict.pshdl.model.HDLIfStatement
import de.tuhh.ict.pshdl.model.HDLInterface
import de.tuhh.ict.pshdl.model.HDLObject
import de.tuhh.ict.pshdl.model.HDLObject$GenericMeta
import de.tuhh.ict.pshdl.model.HDLPackage
import de.tuhh.ict.pshdl.model.HDLSwitchCaseStatement
import de.tuhh.ict.pshdl.model.HDLSwitchStatement
import de.tuhh.ict.pshdl.model.HDLUnit
import de.tuhh.ict.pshdl.model.HDLVariable
import de.tuhh.ict.pshdl.model.IHDLObject
import de.tuhh.ict.pshdl.model.utils.HDLQualifiedName
import java.util.Iterator

import static de.tuhh.ict.pshdl.model.extensions.FullNameExtension.*
import javax.annotation.Nonnull

/**
 * The FullNameExtension provides a {@link HDLQualifiedName} for every IHDLObject. 
 * See {@link FullNameExtension#fullNameOf(IHDLObject)}
 * 
 * @author Karsten Becker
 */
class FullNameExtension {
	/**
	 * This annotation is used to store {@ink HDLQualifiedName} for the case that the resolution diverges from the actual tree 
	 */
	public static GenericMeta<HDLQualifiedName> FULLNAME = new GenericMeta<HDLQualifiedName>("FULLNAME", true)
	private static FullNameExtension INST = new FullNameExtension
	
	/**
	 * Returns the {@link HDLQualifiedName} for the given obj. 
	 */
	@Nonnull
	def static HDLQualifiedName fullNameOf(IHDLObject obj) {
		if (obj === null)
			throw new NullPointerException("Can not get a name for null")
		INST.getFullName(obj)
	}

	private def static int countInstance(HDLObject obj) {
		var int count = 0
		if (obj.container !== null) {
			val Iterator<IHDLObject> iterator = obj.container.iterator(false)
			while (iterator.hasNext) {
				val IHDLObject hdlObject = iterator.next
				if (hdlObject == obj)
					return count
				if (hdlObject.classType == obj.classType)
					count = count + 1
			}
		}
		return count
	}

	def dispatch HDLQualifiedName getFullName(HDLForLoop loop) {
		if (loop.getMeta(FULLNAME) !== null)
			return loop.getMeta(FULLNAME)
		val HDLQualifiedName fullName = loop.superFullName
		val count = countInstance(loop)
		return fullName.append("$for" + count)
	}

	def dispatch HDLQualifiedName getFullName(HDLBlock block) {
		if (block.getMeta(FULLNAME) !== null)
			return block.getMeta(FULLNAME)
		val HDLQualifiedName fullName = block.superFullName
		val count = countInstance(block)
		return fullName.append("$block" + count)
	}

	def dispatch HDLQualifiedName getFullName(HDLIfStatement ifStamnt) {
		if (ifStamnt.getMeta(FULLNAME) !== null)
			return ifStamnt.getMeta(FULLNAME)
		val HDLQualifiedName fullName = ifStamnt.superFullName
		val count = countInstance(ifStamnt)
		return fullName.append("$if" + count)
	}

	def dispatch HDLQualifiedName getFullName(HDLSwitchStatement stmnt) {
		if (stmnt.getMeta(FULLNAME) !== null)
			return stmnt.getMeta(FULLNAME)
		val HDLQualifiedName fullName = stmnt.superFullName
		val count = countInstance(stmnt)
		return fullName.append("$switch" + count)
	}

	def dispatch HDLQualifiedName getFullName(HDLSwitchCaseStatement stmnt) {
		if (stmnt.getMeta(FULLNAME) !== null)
			return stmnt.getMeta(FULLNAME)
		val HDLQualifiedName fullName = stmnt.superFullName
		val count = countInstance(stmnt)
		return fullName.append("$case" + count)
	}

	def dispatch HDLQualifiedName getFullName(HDLUnit unit) {
		if (unit.getMeta(FULLNAME) !== null)
			return unit.getMeta(FULLNAME)
		val HDLQualifiedName fullName = unit.superFullName
		return fullName.append(new HDLQualifiedName(unit.name))
	}

	def dispatch HDLQualifiedName getFullName(HDLInterface unit) {
		if (unit.getMeta(FULLNAME) !== null)
			return unit.getMeta(FULLNAME)
		val HDLQualifiedName fullName = unit.superFullName
		return fullName.append(new HDLQualifiedName(unit.name))
	}

	def dispatch HDLQualifiedName getFullName(HDLEnum unit) {
		if (unit.getMeta(FULLNAME) !== null)
			return unit.getMeta(FULLNAME)
		val HDLQualifiedName fullName = unit.superFullName
		return fullName.append(new HDLQualifiedName(unit.name))
	}

	def dispatch HDLQualifiedName getFullName(HDLFunction unit) {
		if (unit.getMeta(FULLNAME) !== null)
			return unit.getMeta(FULLNAME)
		val HDLQualifiedName fullName = unit.superFullName
		return fullName.append(new HDLQualifiedName(unit.name))
	}

	def dispatch HDLQualifiedName getFullName(HDLPackage pkg) {
		if (pkg.getMeta(FULLNAME) !== null)
			return pkg.getMeta(FULLNAME)
		val HDLQualifiedName fullName = pkg.superFullName
		if (pkg.pkg !== null)
			return fullName.append(new HDLQualifiedName(pkg.pkg))
		return fullName
	}

	def dispatch HDLQualifiedName getFullName(HDLVariable unit) {
		if (unit.getMeta(FULLNAME) !== null)
			return unit.getMeta(FULLNAME)
		val HDLQualifiedName fullName = unit.superFullName
		return fullName.append(new HDLQualifiedName(unit.name))
	}

	def dispatch HDLQualifiedName getFullName(IHDLObject obj) {
		if (obj.getMeta(FULLNAME) !== null)
			return obj.getMeta(FULLNAME)
		if (obj.container !== null)
			return getFullName(obj.container)
		return HDLQualifiedName::EMPTY
	}

	def dispatch HDLQualifiedName getFullName(HDLObject obj) {
		if (obj.getMeta(FULLNAME) !== null)
			return obj.getMeta(FULLNAME)
		if (obj.container !== null)
			return getFullName(obj.container)
		return HDLQualifiedName::EMPTY
	}

	def HDLQualifiedName getSuperFullName(HDLObject obj) {
		if (obj.getMeta(FULLNAME) !== null)
			return obj.getMeta(FULLNAME)
		if (obj.container !== null)
			return getFullName(obj.container)
		return HDLQualifiedName::EMPTY
	}

}

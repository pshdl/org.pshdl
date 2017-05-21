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
package org.pshdl.model.extensions

import java.util.Iterator
import org.pshdl.model.HDLBlock
import org.pshdl.model.HDLEnum
import org.pshdl.model.HDLEnumRef
import org.pshdl.model.HDLForLoop
import org.pshdl.model.HDLFunction
import org.pshdl.model.HDLFunctionCall
import org.pshdl.model.HDLIfStatement
import org.pshdl.model.HDLInterface
import org.pshdl.model.HDLObject
import org.pshdl.model.HDLObject.GenericMeta
import org.pshdl.model.HDLPackage
import org.pshdl.model.HDLSwitchCaseStatement
import org.pshdl.model.HDLSwitchStatement
import org.pshdl.model.HDLUnit
import org.pshdl.model.HDLVariable
import org.pshdl.model.HDLVariableRef
import org.pshdl.model.IHDLObject
import org.pshdl.model.utils.HDLQualifiedName

/**
 * The FullNameExtension provides a {@link HDLQualifiedName} for every IHDLObject. 
 * See {@link FullNameExtension#fullNameOf(IHDLObject)}
 * 
 * @author Karsten Becker
 */
class FullNameExtension {
	/**
	 * This annotation is used to store {@link HDLQualifiedName} for the case that the resolution diverges from the actual tree 
	 */
	public static GenericMeta<HDLQualifiedName> FULLNAME = new GenericMeta<HDLQualifiedName>("FULLNAME", true)
	private static FullNameExtension INST = new FullNameExtension
	
	/**
	 * Returns the {@link HDLQualifiedName} for the given obj. 
	 */
	def static HDLQualifiedName fullNameOf(IHDLObject obj) {
		if (obj === null)
			return null;
		val cached=obj.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		INST.getFullName(obj)
	}

	private def static int countInstance(HDLObject obj) {
		var int count = 0
		if (obj.container !== null) {
			val Iterator<IHDLObject> iterator = obj.container.iterator()
			while (iterator.hasNext) {
				val IHDLObject hdlObject = iterator.next
				if (hdlObject === obj)
					return count
				if (hdlObject.classType === obj.classType)
					count = count + 1
			}
		}
		return count
	}

	def dispatch HDLQualifiedName getFullName(HDLForLoop loop) {
		val cached=loop.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val HDLQualifiedName fullName = loop.superFullName
		val count = countInstance(loop)
		return fullName.append(HDLQualifiedName.LOCAL_TYPE_SEP+"for" + count)
	}

	def dispatch HDLQualifiedName getFullName(HDLBlock block) {
		val cached=block.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val HDLQualifiedName fullName = block.superFullName
		val count = countInstance(block)
		return fullName.append(HDLQualifiedName.LOCAL_TYPE_SEP+"block" + count)
	}

	def dispatch HDLQualifiedName getFullName(HDLIfStatement ifStamnt) {
		val cached=ifStamnt.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val HDLQualifiedName fullName = ifStamnt.superFullName
		val count = countInstance(ifStamnt)
		return fullName.append(HDLQualifiedName.LOCAL_TYPE_SEP+"if" + count)
	}

	def dispatch HDLQualifiedName getFullName(HDLSwitchStatement stmnt) {
		val cached=stmnt.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val HDLQualifiedName fullName = stmnt.superFullName
		val count = countInstance(stmnt)
		return fullName.append(HDLQualifiedName.LOCAL_TYPE_SEP+"switch" + count)
	}

	def dispatch HDLQualifiedName getFullName(HDLSwitchCaseStatement stmnt) {
		val cached=stmnt.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val HDLQualifiedName fullName = stmnt.superFullName
		val count = countInstance(stmnt)
		return fullName.append(HDLQualifiedName.LOCAL_TYPE_SEP+"case" + count)
	}

	def dispatch HDLQualifiedName getFullName(HDLUnit unit) {
		val cached=unit.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val HDLQualifiedName fullName = unit.superFullName
		return fullName.append(new HDLQualifiedName(unit.name))
	}

	def dispatch HDLQualifiedName getFullName(HDLInterface unit) {
		val cached=unit.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val HDLQualifiedName fullName = unit.superFullName
		return fullName.append(new HDLQualifiedName(unit.name))
	}

	def dispatch HDLQualifiedName getFullName(HDLEnum unit) {
		val cached=unit.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val HDLQualifiedName fullName = unit.superFullName
		return fullName.append(new HDLQualifiedName(unit.name))
	}

	def dispatch HDLQualifiedName getFullName(HDLFunction unit) {
		val cached=unit.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val HDLQualifiedName fullName = unit.superFullName
		return fullName.append(new HDLQualifiedName(unit.name))
	}

	def dispatch HDLQualifiedName getFullName(HDLPackage pkg) {
		val cached=pkg.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val HDLQualifiedName fullName = pkg.superFullName
		if (pkg.pkg !== null)
			return fullName.append(new HDLQualifiedName(pkg.pkg))
		return fullName
	}

	def dispatch HDLQualifiedName getFullName(HDLEnumRef ref) {
		val cached=ref.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val varRef = ref.resolveVar
		if (!varRef.present)
			return null
		return varRef.get.fullName
	}
	
	def dispatch HDLQualifiedName getFullName(HDLVariableRef ref) {
		val cached=ref.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val varRef = ref.resolveVar
		if (!varRef.present)
			return null
		return varRef.get.fullName
	}
	def dispatch HDLQualifiedName getFullName(HDLFunctionCall call) {
		val cached=call.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val callRef = call.resolveFunctionName
		if (!callRef.present)
			return null
		return callRef.get
	}
	
	def dispatch HDLQualifiedName getFullName(HDLVariable unit) {
		val cached=unit.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		val HDLQualifiedName fullName = unit.superFullName
		return fullName.append(new HDLQualifiedName(unit.name))
	}

	def dispatch HDLQualifiedName getFullName(IHDLObject obj) {
		val cached=obj.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		if (obj.container !== null)
			return getFullName(obj.container)
		return HDLQualifiedName.EMPTY
	}

	def dispatch HDLQualifiedName getFullName(HDLObject obj) {
		val cached=obj.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		if (obj.container !== null)
			return getFullName(obj.container)
		return HDLQualifiedName.EMPTY
	}

	def HDLQualifiedName getSuperFullName(HDLObject obj) {
		val cached=obj.getMeta(FULLNAME)
		if (cached !== null)
			return cached
		if (obj.container !== null){
			val fn=getFullName(obj.container)
			if (obj.container instanceof HDLIfStatement){
				val HDLIfStatement ifStmnt=obj.container as HDLIfStatement
				val side=ifStmnt.treeSide(obj)
				switch (side){
					case HDLIfStatement.TreeSide.thenTree: 
						return new HDLQualifiedName(fn.toString+"p")
					case HDLIfStatement.TreeSide.elseTree: 
						return new HDLQualifiedName(fn.toString+"n")
				}
			}			
			return fn
		}
		return HDLQualifiedName.EMPTY
	}

}

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

import org.pshdl.model.HDLAssignment
import org.pshdl.model.HDLBlock
import org.pshdl.model.HDLDirectGeneration
import org.pshdl.model.HDLEnum
import org.pshdl.model.HDLEnumDeclaration
import org.pshdl.model.HDLForLoop
import org.pshdl.model.HDLFunction
import org.pshdl.model.HDLIfStatement
import org.pshdl.model.HDLInlineFunction
import org.pshdl.model.HDLInterface
import org.pshdl.model.HDLInterfaceDeclaration
import org.pshdl.model.HDLInterfaceInstantiation
import org.pshdl.model.HDLObject
import org.pshdl.model.HDLObject$GenericMeta
import org.pshdl.model.HDLPackage
import org.pshdl.model.HDLStatement
import org.pshdl.model.HDLSubstituteFunction
import org.pshdl.model.HDLSwitchCaseStatement
import org.pshdl.model.HDLSwitchStatement
import org.pshdl.model.HDLType
import org.pshdl.model.HDLUnit
import org.pshdl.model.HDLVariable
import org.pshdl.model.HDLVariableDeclaration
import org.pshdl.model.IHDLObject
import org.pshdl.model.utils.HDLLibrary
import org.pshdl.model.utils.HDLProblemException
import org.pshdl.model.utils.HDLQualifiedName
import org.pshdl.model.utils.HDLQuery
import org.pshdl.model.utils.HDLResolver
import org.pshdl.model.utils.MetaAccess
import org.pshdl.model.validation.Problem
import org.pshdl.model.validation.builtin.ErrorCode
import java.util.Collections
import java.util.LinkedList
import java.util.List

import static org.pshdl.model.extensions.ScopingExtension.*
import com.google.common.base.Optional
import org.pshdl.model.HDLNativeFunction

/**
 * The ScopingExtension allows the resolution of Enums, Interface, Variables by name
 */
class ScopingExtension {

	public static ScopingExtension INST = new ScopingExtension

	def Optional<HDLVariable> resolveVariableDefault(IHDLObject obj, HDLQualifiedName hVar) {
		if (obj===null || obj.container === null) {
			return Optional::absent
		}
		return obj.container.resolveVariable(hVar)
	}

	def dispatch Optional<HDLVariable> resolveVariable(IHDLObject obj, HDLQualifiedName hVar) {
		return obj.resolveVariableDefault(hVar)
	}

	def dispatch Optional<HDLFunction> resolveFunction(IHDLObject obj, HDLQualifiedName hVar) {
		if (obj.container === null)
			return Optional::absent
		return obj.container.resolveFunction(hVar)
	}

	def dispatch Optional<HDLEnum> resolveEnum(IHDLObject obj, HDLQualifiedName hEnum) {
		if (obj.container === null)
			return Optional::absent
		return obj.container.resolveEnum(hEnum)
	}

	def dispatch Optional<? extends HDLType> resolveType(IHDLObject obj, HDLQualifiedName type) {
		if (obj.container === null)
			return Optional::absent
		return obj.container.resolveType(type)
	}

	def dispatch Optional<HDLInterface> resolveInterface(IHDLObject obj, HDLQualifiedName hIf) {
		if (obj.container === null)
			return Optional::absent
		return obj.container.resolveInterface(hIf)
	}

	private static MetaAccess<HDLResolver> RESOLVER = new GenericMeta<HDLResolver>("RESOLVER", false)

	private def resolver(IHDLObject statement, boolean descent) {
		var HDLResolver resolver = statement.getMeta(RESOLVER)
		if (resolver === null) {
			resolver = new HDLResolver(statement, descent)
			statement.addMeta(RESOLVER, resolver)
		}
		return resolver
	}

	def dispatch Optional<? extends HDLType> resolveType(HDLStatement obj, HDLQualifiedName hVar) {
		return obj.resolver(true).resolveType(hVar)
	}

	def dispatch Optional<HDLVariable> resolveVariable(HDLStatement obj, HDLQualifiedName hVar) {
		return obj.resolver(true).resolveVariable(hVar)
	}

	def dispatch Optional<HDLInterface> resolveInterface(HDLStatement obj, HDLQualifiedName hIf) {
		return obj.resolver(true).resolveInterface(hIf)
	}

	def dispatch Optional<HDLEnum> resolveEnum(HDLStatement obj, HDLQualifiedName hEnum) {
		return obj.resolver(true).resolveEnum(hEnum)
	}

	def dispatch Optional<HDLFunction> resolveFunction(HDLStatement obj, HDLQualifiedName hEnum) {
		return obj.resolver(true).resolveFunction(hEnum)
	}

	def dispatch List<HDLEnumDeclaration> doGetEnumDeclarations(HDLIfStatement obj) {
		val List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>
		res.addAll(HDLResolver::getallEnumDeclarations(obj.thenDo))
		res.addAll(HDLResolver::getallEnumDeclarations(obj.elseDo))
		return res
	}

	def dispatch List<HDLInterface> doGetInterfaceDeclarations(HDLIfStatement obj) {
		val List<HDLInterface> res = new LinkedList<HDLInterface>
		res.addAll(HDLResolver::getallInterfaceDeclarations(obj.thenDo))
		res.addAll(HDLResolver::getallInterfaceDeclarations(obj.elseDo))
		return res
	}

	def dispatch List<HDLVariable> doGetVariables(HDLIfStatement obj) {
		val List<HDLVariable> res = new LinkedList<HDLVariable>
		res.addAll(HDLResolver::getallVariableDeclarations(obj.thenDo))
		res.addAll(HDLResolver::getallVariableDeclarations(obj.elseDo))
		return res
	}

	def dispatch  List<HDLEnumDeclaration> doGetEnumDeclarations(IHDLObject gen) {
		return Collections::emptyList
	}

	def dispatch  List<HDLInterface> doGetInterfaceDeclarations(IHDLObject gen) {
		return Collections::emptyList
	}

	def dispatch  List<HDLVariable> doGetVariables(IHDLObject gen) {
		return Collections::emptyList
	}

	def dispatch  List<HDLEnumDeclaration> doGetEnumDeclarations(HDLDirectGeneration gen) {
		return Collections::emptyList
	}

	def dispatch  List<HDLInterface> doGetInterfaceDeclarations(HDLDirectGeneration gen) {
		if (gen.getHIf === null)
			return Collections::emptyList
		return Collections::singletonList(gen.getHIf)
	}

	def dispatch  List<HDLVariable> doGetVariables(HDLDirectGeneration gen) {
		return Collections::singletonList(gen.getVar)
	}

	def dispatch List<HDLVariable> doGetVariables(HDLInlineFunction obj) {
		val List<HDLVariable> res = new LinkedList<HDLVariable>
		for (v : obj.args)
			res.add(v.name)
		return res
	}

	def dispatch List<HDLVariable> doGetVariables(HDLNativeFunction obj) {
		val List<HDLVariable> res = new LinkedList<HDLVariable>
		for (v : obj.args)
			res.add(v.name)
		return res
	}

	def dispatch List<HDLVariable> doGetVariables(HDLSubstituteFunction obj) {
		val List<HDLVariable> res = new LinkedList<HDLVariable>
		for (v : obj.args)
			res.add(v.name)
		return res
	}

	def dispatch List<HDLEnumDeclaration> doGetEnumDeclarations(HDLForLoop obj) {
		return HDLResolver::getallEnumDeclarations(obj.dos)
	}

	def dispatch List<HDLInterface> doGetInterfaceDeclarations(HDLForLoop obj) {
		return HDLResolver::getallInterfaceDeclarations(obj.dos)
	}

	def dispatch List<HDLVariable> doGetVariables(HDLForLoop obj) {
		val List<HDLVariable> res = new LinkedList<HDLVariable>
		res.addAll(HDLResolver::getallVariableDeclarations(obj.dos))
		res.add(obj.param)
		return res
	}

	def dispatch List<HDLEnumDeclaration> doGetEnumDeclarations(HDLBlock obj) {
		return HDLResolver::getallEnumDeclarations(obj.statements)
	}

	def dispatch List<HDLInterface> doGetInterfaceDeclarations(HDLBlock obj) {
		return HDLResolver::getallInterfaceDeclarations(obj.statements)
	}

	def dispatch List<HDLVariable> doGetVariables(HDLBlock obj) {
		return HDLResolver::getallVariableDeclarations(obj.statements)
	}

	def dispatch Optional<HDLEnum> resolveEnum(HDLAssignment obj, org.pshdl.model.utils.HDLQualifiedName hEnum) {
		if (obj.container === null)
			throw new HDLProblemException(new Problem(ErrorCode::UNRESOLVED_ENUM, obj, "for hEnum:" + hEnum))
		return obj.container.resolveEnum(hEnum)
	}

	def dispatch Optional<HDLInterface> resolveInterface(HDLAssignment obj, HDLQualifiedName hIf) {
		if (obj.container === null)
			throw new HDLProblemException(new Problem(ErrorCode::UNRESOLVED_INTERFACE, obj, "for interface:" + hIf))
		return obj.container.resolveInterface(hIf)
	}

	def dispatch Optional<? extends HDLType> resolveType(HDLAssignment obj, HDLQualifiedName hVar) {
		if (obj.container === null)
			throw new HDLProblemException(new Problem(ErrorCode::UNRESOLVED_TYPE, obj, "for type:" + hVar))
		return obj.container.resolveType(hVar)
	}

	def dispatch Optional<HDLVariable> resolveVariable(HDLAssignment obj, HDLQualifiedName hVar) {
		if (obj.container === null)
			throw new HDLProblemException(new Problem(ErrorCode::UNRESOLVED_VARIABLE, obj, "for hVariable:" + hVar))
		return obj.container.resolveVariable(hVar)
	}

	def dispatch List<HDLEnumDeclaration> doGetEnumDeclarations(HDLEnumDeclaration obj) {
		return Collections::singletonList(obj)
	}

	//HDLVariableDeclaration
	def dispatch List<HDLInterface> doGetInterfaceDeclarations(HDLInterfaceDeclaration obj) {
		return Collections::singletonList(obj.HIf)
	}

	def dispatch List<HDLVariable> doGetVariables(HDLVariableDeclaration obj) {
		return obj.variables
	}

	def dispatch List<HDLVariable> doGetVariables(HDLInterfaceInstantiation obj) {
		return Collections::singletonList(obj.^var)
	}

	//HDLSwitchCaseStatement
	def dispatch List<HDLEnumDeclaration> doGetEnumDeclarations(HDLSwitchCaseStatement obj) {
		return HDLResolver::getallEnumDeclarations(obj.dos)
	}

	def dispatch List<HDLInterface> doGetInterfaceDeclarations(HDLSwitchCaseStatement obj) {
		return HDLResolver::getallInterfaceDeclarations(obj.dos)
	}

	def dispatch List<HDLVariable> doGetVariables(HDLSwitchCaseStatement obj) {
		return HDLResolver::getallVariableDeclarations(obj.dos)
	}

	//HDLSwitchStatement
	def dispatch List<HDLEnumDeclaration> doGetEnumDeclarations(HDLSwitchStatement obj) {
		val List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>
		for (HDLSwitchCaseStatement c : obj.cases) {
			res.addAll(c.doGetEnumDeclarations)
		}
		return res
	}

	def dispatch List<HDLInterface> doGetInterfaceDeclarations(HDLSwitchStatement obj) {
		val List<HDLInterface> res = new LinkedList<HDLInterface>
		for (HDLSwitchCaseStatement c : obj.cases) {
			res.addAll(c.doGetInterfaceDeclarations)
		}
		return res
	}

	def dispatch List<HDLVariable> doGetVariables(HDLSwitchStatement obj) {
		val List<HDLVariable> res = new LinkedList<HDLVariable>
		for (HDLSwitchCaseStatement c : obj.cases) {
			res.addAll(c.doGetVariables)
		}
		return res
	}

	//HDLPackage stuff
	def dispatch Optional<HDLFunction> resolveFunction(HDLPackage obj, HDLQualifiedName hFunc) {
		var HDLLibrary library = obj.library
		if (library === null)
			library = HDLLibrary::getLibrary(obj.getLibURI)
		return library.resolveFunction(HDLObject::asList(obj.pkg + ".*"), hFunc)
	}

	def dispatch Optional<HDLEnum> resolveEnum(HDLPackage obj, HDLQualifiedName hEnum) {
		val res = obj.resolveType(hEnum)
		if (res.present && res.get instanceof HDLEnum)
			return res as Optional<HDLEnum>
		return Optional::absent
	}

	def dispatch Optional<HDLInterface> resolveInterface(HDLPackage obj, HDLQualifiedName hIf) {
		val res = obj.resolveType(hIf)
		if (res.present && res.get instanceof HDLInterface)
			return res as Optional<HDLInterface>
		return Optional::absent
	}

	def dispatch Optional<? extends HDLType> resolveType(HDLPackage obj, HDLQualifiedName type) {
		var HDLLibrary library = obj.library
		if (obj.library === null)
			library = HDLLibrary::getLibrary(obj.getLibURI)
		return library.resolve(HDLObject::asList(obj.pkg + ".*"), type)
	}

	def dispatch Optional<HDLVariable> resolveVariable(HDLPackage obj, HDLQualifiedName hVar) {
		var HDLLibrary library = obj.library
		if (obj.library === null)
			library = HDLLibrary::getLibrary(obj.getLibURI)
		return library.resolveVariable(HDLObject::asList(obj.pkg + ".*"), hVar)
	}

	//HDLUnit begin
	def dispatch Optional<HDLEnum> resolveEnum(HDLUnit obj, HDLQualifiedName hEnum) {
		val resolveEnum = obj.resolver(false).resolveEnum(hEnum)
		if (resolveEnum.present)
			return resolveEnum
		val res = obj.resolveType(hEnum)
		if (res.present && res.get instanceof HDLEnum)
			return res as Optional<HDLEnum>
		return Optional::absent
	}

	def dispatch Optional<HDLFunction> resolveFunction(HDLUnit obj, HDLQualifiedName hFunc) {
		val resolveEnum = obj.resolver(false).resolveFunction(hFunc)
		if (resolveEnum.present)
			return resolveEnum
		var HDLLibrary library = obj.library
		if (obj.library === null)
			library = HDLLibrary::getLibrary(obj.getLibURI)
		val newImports = obj.imports
		newImports.add(FullNameExtension::fullNameOf(obj).skipLast(1).append("*").toString)
		return library.resolveFunction(newImports, hFunc)
	}

	def dispatch Optional<HDLInterface> resolveInterface(HDLUnit obj, HDLQualifiedName hIf) {
		val resolveInterface = obj.resolver(false).resolveInterface(hIf)
		if (resolveInterface.present)
			return resolveInterface
		val res = obj.resolveType(hIf)
		if (res.present && res.get instanceof HDLInterface)
			return res as Optional<HDLInterface>
		return Optional::absent
	}

	def dispatch Optional<? extends HDLType> resolveType(HDLUnit obj, HDLQualifiedName type) {
		val resolveType = obj.resolver(false).resolveType(type)
		if (resolveType.present)
			return resolveType
		var HDLLibrary library = obj.library
		if (obj.library === null)
			library = HDLLibrary::getLibrary(obj.getLibURI)
		val newImports = obj.imports
		newImports.add(FullNameExtension::fullNameOf(obj).skipLast(1).append("*").toString)
		return library.resolve(newImports, type)
	}

	def dispatch Optional<HDLVariable> resolveVariable(HDLUnit obj, HDLQualifiedName hVar) {
		val hdlVariable = obj.resolver(false).resolveVariable(hVar)
		if (hdlVariable.present)
			return hdlVariable
		var HDLLibrary library = obj.library
		if (obj.library === null)
			library = HDLLibrary::getLibrary(obj.getLibURI)
		val newImports = obj.imports
		newImports.add(FullNameExtension::fullNameOf(obj).skipLast(1).append("*").toString)
		return library.resolveVariable(newImports, hVar)
	}

	def dispatch List<HDLEnumDeclaration> doGetEnumDeclarations(HDLUnit obj) {
		val List<HDLEnumDeclaration> res = HDLResolver::getallEnumDeclarations(obj.inits)
		res.addAll(HDLResolver::getallEnumDeclarations(obj.statements))
		return res
	}

	def dispatch List<HDLInterface> doGetInterfaceDeclarations(HDLUnit obj) {
		val List<HDLInterface> res = HDLResolver::getallInterfaceDeclarations(obj.inits)
		res.addAll(HDLResolver::getallInterfaceDeclarations(obj.statements))
		return res
	}

	def dispatch List<HDLVariable> doGetVariables(HDLUnit obj) {
		val List<HDLVariable> res = HDLResolver::getallVariableDeclarations(obj.inits)
		res.addAll(HDLResolver::getallVariableDeclarations(obj.statements))
		return res
	}

	def dispatch Optional<HDLVariable> resolveVariable(HDLInterface hIf, HDLQualifiedName hVar) {
		val HDLVariable resolved = getVariable(hIf, hVar.getLastSegment)
		if (resolved !== null) {
			if (hVar.length == 1) {
				return Optional::of(resolved)
			}
			if (FullNameExtension::fullNameOf(hIf).equals(hVar.skipLast(1))) {
				return Optional::of(resolved)
			}
		}
		return resolveVariableDefault(hIf, hVar)
	}

	def private static HDLVariable getVariable(HDLInterface hIf, String lastSegment) {
		return HDLQuery::select(typeof(HDLVariable)).from(hIf).where(HDLVariable::fName).lastSegmentIs(lastSegment).
			first
	}

	def dispatch Optional<HDLVariable> resolveVariable(HDLEnum hEnum, HDLQualifiedName hVar) {
		if (hVar.length == 1) {
			return getVariable(hEnum, hVar.getLastSegment)
		}
		if (FullNameExtension::fullNameOf(hEnum).equals(hVar.skipLast(1))) {
			return getVariable(hEnum, hVar.getLastSegment)
		}
		return resolveVariable(hEnum, hVar)
	}

	def public static Optional<HDLVariable> getVariable(HDLEnum hEnum, String lastSegment) {
		for (HDLVariable hVar : hEnum.getEnums) {
			if (hVar.name.equals(lastSegment)) {
				return Optional::of(hVar)
			}
		}
		return Optional::absent
	}

}

package de.tuhh.ict.pshdl.model.extensions

import de.tuhh.ict.pshdl.model.HDLAssignment
import de.tuhh.ict.pshdl.model.HDLBlock
import de.tuhh.ict.pshdl.model.HDLDirectGeneration
import de.tuhh.ict.pshdl.model.HDLEnum
import de.tuhh.ict.pshdl.model.HDLEnumDeclaration
import de.tuhh.ict.pshdl.model.HDLForLoop
import de.tuhh.ict.pshdl.model.HDLFunction
import de.tuhh.ict.pshdl.model.HDLIfStatement
import de.tuhh.ict.pshdl.model.HDLInlineFunction
import de.tuhh.ict.pshdl.model.HDLInterface
import de.tuhh.ict.pshdl.model.HDLInterfaceDeclaration
import de.tuhh.ict.pshdl.model.HDLInterfaceInstantiation
import de.tuhh.ict.pshdl.model.HDLObject
import de.tuhh.ict.pshdl.model.HDLObject$GenericMeta
import de.tuhh.ict.pshdl.model.HDLPackage
import de.tuhh.ict.pshdl.model.HDLStatement
import de.tuhh.ict.pshdl.model.HDLSubstituteFunction
import de.tuhh.ict.pshdl.model.HDLSwitchCaseStatement
import de.tuhh.ict.pshdl.model.HDLSwitchStatement
import de.tuhh.ict.pshdl.model.HDLType
import de.tuhh.ict.pshdl.model.HDLUnit
import de.tuhh.ict.pshdl.model.HDLVariable
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration
import de.tuhh.ict.pshdl.model.IHDLObject
import de.tuhh.ict.pshdl.model.utils.HDLLibrary
import de.tuhh.ict.pshdl.model.utils.HDLProblemException
import de.tuhh.ict.pshdl.model.utils.HDLQualifiedName
import de.tuhh.ict.pshdl.model.utils.HDLQuery
import de.tuhh.ict.pshdl.model.utils.HDLResolver
import de.tuhh.ict.pshdl.model.utils.MetaAccess
import de.tuhh.ict.pshdl.model.validation.Problem
import de.tuhh.ict.pshdl.model.validation.builtin.ErrorCode
import java.util.Collections
import java.util.LinkedList
import java.util.List

import static de.tuhh.ict.pshdl.model.extensions.ScopingExtension.*

class ScopingExtension {
	
	public static ScopingExtension INST=new ScopingExtension
	
	def HDLVariable resolveVariableDefault(IHDLObject obj, HDLQualifiedName hVar) {
		if (obj.container == null) {
			return null
		}
		return obj.container.resolveVariable(hVar)
	}
	
	def dispatch HDLVariable resolveVariable(IHDLObject obj, HDLQualifiedName hVar) {
		return obj.resolveVariableDefault(hVar)
	}
	
	def dispatch HDLFunction resolveFunction(IHDLObject obj, HDLQualifiedName hVar) {
		if (obj.container == null)
			return null
		return obj.container.resolveFunction(hVar)
	}

	def dispatch HDLEnum resolveEnum(IHDLObject obj, HDLQualifiedName hEnum) {
		if (obj.container == null)
			return null
		return obj.container.resolveEnum(hEnum)
	}

	def dispatch HDLType resolveType(IHDLObject obj, HDLQualifiedName type) {
		if (obj.container == null)
			return null
		return obj.container.resolveType(type)
	}

	def dispatch HDLInterface resolveInterface(IHDLObject obj, HDLQualifiedName hIf) {
		if (obj.container == null)
			return null
		return obj.container.resolveInterface(hIf)
	}

	private static MetaAccess<HDLResolver> RESOLVER=new GenericMeta<HDLResolver>("RESOLVER",false)

	def resolver(IHDLObject statement, boolean descent) { 
		var HDLResolver resolver=statement.getMeta(RESOLVER)
		if (resolver==null) {
			resolver=new HDLResolver(statement, descent)
			statement.addMeta(RESOLVER, resolver)
		}
		return resolver
	}

	def dispatch HDLType resolveType(HDLStatement obj, HDLQualifiedName hVar) {
		return obj.resolver(true).resolveType(hVar)
	}

	def dispatch HDLVariable resolveVariable(HDLStatement obj, HDLQualifiedName hVar) {
		return obj.resolver(true).resolveVariable(hVar)
	}

	def dispatch HDLInterface resolveInterface(HDLStatement obj, HDLQualifiedName hIf) {
		return obj.resolver(true).resolveInterface(hIf)
	}

	def dispatch HDLEnum resolveEnum(HDLStatement obj, HDLQualifiedName hEnum) {
		return obj.resolver(true).resolveEnum(hEnum)
	}
	
	def dispatch HDLFunction resolveFunction(HDLStatement obj, HDLQualifiedName hEnum) {
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
		return Collections::emptyList;
	}

	def dispatch  List<HDLInterface> doGetInterfaceDeclarations(IHDLObject gen) {
		return Collections::emptyList;
	}

	def dispatch  List<HDLVariable> doGetVariables(IHDLObject gen) {
		return Collections::emptyList;
	}
	
	def dispatch  List<HDLEnumDeclaration> doGetEnumDeclarations(HDLDirectGeneration gen) {
		return Collections::emptyList;
	}

	def dispatch  List<HDLInterface> doGetInterfaceDeclarations(HDLDirectGeneration gen) {
		return Collections::singletonList(gen.getHIf);
	}

	def dispatch  List<HDLVariable> doGetVariables(HDLDirectGeneration gen) {
		return Collections::singletonList(gen.getVar);
	}
	
	def dispatch List<HDLVariable> doGetVariables(HDLInlineFunction obj) {
		val List<HDLVariable> res = new LinkedList<HDLVariable>
		res.addAll(obj.args)
		return res
	}
	def dispatch List<HDLVariable> doGetVariables(HDLSubstituteFunction obj) {
		val List<HDLVariable> res = new LinkedList<HDLVariable>
		res.addAll(obj.args)
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
	
	def dispatch HDLEnum resolveEnum(HDLAssignment obj, de.tuhh.ict.pshdl.model.utils.HDLQualifiedName hEnum) {
		if (obj.container==null)
			throw new HDLProblemException(new Problem(ErrorCode::UNRESOLVED_ENUM, obj, "for hEnum:" + hEnum))
		return obj.container.resolveEnum(hEnum)
	}

	def dispatch HDLInterface resolveInterface(HDLAssignment obj, HDLQualifiedName hIf) {
		if (obj.container==null)
			throw new HDLProblemException(new Problem(ErrorCode::UNRESOLVED_INTERFACE, obj, "for interface:" + hIf))
		return obj.container.resolveInterface(hIf)
	}

	def dispatch HDLType resolveType(HDLAssignment obj, HDLQualifiedName hVar) {
		if (obj.container==null)
			throw new HDLProblemException(new Problem(ErrorCode::UNRESOLVED_TYPE, obj, "for type:" + hVar))
		return obj.container.resolveType(hVar)
	}

	def dispatch HDLVariable resolveVariable(HDLAssignment obj, HDLQualifiedName hVar) {
		if (obj.container==null)
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
	
	def dispatch HDLFunction resolveFunction(HDLPackage obj, HDLQualifiedName hFunc) {
		var HDLLibrary library=obj.library
		if (library == null)
			library = HDLLibrary::getLibrary(obj.getLibURI)
		return library.resolveFunction(HDLObject::asList(obj.pkg+".*"), hFunc)
	}

	def dispatch HDLEnum resolveEnum(HDLPackage obj, HDLQualifiedName hEnum) {
		return obj.resolveType(hEnum) as HDLEnum
	}

	def dispatch HDLInterface resolveInterface(HDLPackage obj, HDLQualifiedName hIf) {
		return obj.resolveType(hIf) as HDLInterface
	}

	def dispatch HDLType resolveType(HDLPackage obj, HDLQualifiedName type) {
		var HDLLibrary library=obj.library
		if (obj.library == null)
			library = HDLLibrary::getLibrary(obj.getLibURI)
		return library.resolve(HDLObject::asList(obj.pkg+".*"), type)
	}

	def dispatch HDLVariable resolveVariable(HDLPackage obj, HDLQualifiedName hVar) {
		var HDLLibrary library=obj.library
		if (obj.library == null)
			library = HDLLibrary::getLibrary(obj.getLibURI)
		return library.resolveVariable(HDLObject::asList(obj.pkg+".*"), hVar)
	}
	
	//HDLUnit begin

	def dispatch HDLEnum resolveEnum(HDLUnit obj, HDLQualifiedName hEnum) {
		val HDLEnum resolveEnum = obj.resolver(false).resolveEnum(hEnum)
		if (resolveEnum != null)
			return resolveEnum
		return obj.resolveType(hEnum) as HDLEnum
	}
	def dispatch HDLFunction resolveFunction(HDLUnit obj, HDLQualifiedName hFunc) {
		val HDLFunction resolveEnum = obj.resolver(false).resolveFunction(hFunc)
		if (resolveEnum != null)
			return resolveEnum
		var HDLLibrary library=obj.library
		if (obj.library == null)
			library = HDLLibrary::getLibrary(obj.getLibURI)
		return library.resolveFunction(obj.imports, hFunc)
	}

	def dispatch HDLInterface resolveInterface(HDLUnit obj, HDLQualifiedName hIf) {
		val HDLInterface resolveInterface = obj.resolver(false).resolveInterface(hIf)
		if (resolveInterface != null)
			return resolveInterface
		return  obj.resolveType(hIf) as HDLInterface
	}

	def dispatch HDLType resolveType(HDLUnit obj, HDLQualifiedName type) {
		val HDLType resolveType = obj.resolver(false).resolveType(type)
		if (resolveType != null)
			return resolveType
		var HDLLibrary library=obj.library
		if (obj.library == null)
			library = HDLLibrary::getLibrary(obj.getLibURI)
		return library.resolve(obj.imports, type)
	}

	def dispatch HDLVariable resolveVariable(HDLUnit obj, HDLQualifiedName hVar) {
		val HDLVariable hdlVariable = obj.resolver(false).resolveVariable(hVar)
		if (hdlVariable != null)
			return hdlVariable
		var HDLLibrary library=obj.library
		if (obj.library == null)
			library = HDLLibrary::getLibrary(obj.getLibURI)
		return library.resolveVariable(obj.imports, hVar)
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
	
	def dispatch HDLVariable resolveVariable(HDLInterface hIf, HDLQualifiedName hVar) {
		val HDLVariable resolved = getVariable(hIf, hVar.getLastSegment);
		if (resolved != null) {
			if (hVar.length == 1) {
				return resolved;
			}
			if (FullNameExtension::fullNameOf(hIf).equals(hVar.skipLast(1))) {
				return resolved;
			}
		}
		return resolveVariableDefault(hIf, hVar);
	}

	def private static HDLVariable getVariable(HDLInterface hIf, String lastSegment) {
		return HDLQuery::select(typeof(HDLVariable)).from(hIf).where(HDLVariable::fName).lastSegmentIs(lastSegment).getFirst;
	}
	
	def dispatch HDLVariable resolveVariable(HDLEnum hEnum, HDLQualifiedName hVar) {
		if (hVar.length == 1) {
			return getVariable(hEnum, hVar.getLastSegment);
		}
		if (FullNameExtension::fullNameOf(hEnum).equals(hVar.skipLast(1))) {
			return getVariable(hEnum, hVar.getLastSegment);
		}
		return resolveVariable(hEnum, hVar);
	}

	def public static HDLVariable getVariable(HDLEnum hEnum, String lastSegment) {
		for (HDLVariable hVar : hEnum.getEnums) {
			if (hVar.name.equals(lastSegment)) {
				return hVar;
			}
		}
		return null;
	}
	
}

package de.tuhh.ict.pshdl.model.aspects;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.validation.*;

public aspect ScopingAspect {
	private HDLResolver HDLStatement.resolver = new HDLResolver(this, true);
	
	public HDLVariable IHDLObject.resolveVariable(HDLQualifiedName var) {
		if (getContainer() == null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_VARIABLE, this, "for variable:"+var));
		return getContainer().resolveVariable(var);
	}
	public HDLFunction IHDLObject.resolveFunction(HDLQualifiedName var) {
		if (getContainer() == null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_VARIABLE, this, "for function:"+var));
		return getContainer().resolveFunction(var);
	}

	public HDLEnum IHDLObject.resolveEnum(HDLQualifiedName hEnum) {
		if (getContainer() == null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_ENUM, this, "for enum:"+hEnum));
		return getContainer().resolveEnum(hEnum);
	}

	public HDLType IHDLObject.resolveType(HDLQualifiedName type) {
		if (getContainer() == null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_TYPE, this, "for type:"+type));
		return getContainer().resolveType(type);
	}

	public HDLInterface IHDLObject.resolveInterface(HDLQualifiedName hIf) {
		if (getContainer() == null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_INTERFACE, this, "for interface:"+hIf));
		return getContainer().resolveInterface(hIf);
	}


	public HDLType HDLStatement.resolveType(HDLQualifiedName var) {
		return resolver.resolveType(var);
	}

	public HDLVariable HDLStatement.resolveVariable(HDLQualifiedName var) {
		return resolver.resolveVariable(var);
	}

	public HDLInterface HDLStatement.resolveInterface(HDLQualifiedName hIf) {
		return resolver.resolveInterface(hIf);
	}

	public HDLEnum HDLStatement.resolveEnum(HDLQualifiedName hEnum) {
		return resolver.resolveEnum(hEnum);
	}
	
	public HDLFunction HDLStatement.resolveFunction(HDLQualifiedName hEnum) {
		return resolver.resolveFunction(hEnum);
	}
	
	public List<HDLEnumDeclaration> HDLIfStatement.doGetEnumDeclarations() {
		List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
		res.addAll(HDLResolver.getallEnumDeclarations(getThenDo()));
		res.addAll(HDLResolver.getallEnumDeclarations(getElseDo()));
		return res;
	}

	public List<HDLInterface> HDLIfStatement.doGetInterfaceDeclarations() {
		List<HDLInterface> res = new LinkedList<HDLInterface>();
		res.addAll(HDLResolver.getallInterfaceDeclarations(getThenDo()));
		res.addAll(HDLResolver.getallInterfaceDeclarations(getElseDo()));
		return res;
	}

	public List<HDLVariable> HDLIfStatement.doGetVariables() {
		List<HDLVariable> res = new LinkedList<HDLVariable>();
		res.addAll(HDLResolver.getallVariableDeclarations(getThenDo()));
		res.addAll(HDLResolver.getallVariableDeclarations(getElseDo()));
		return res;
	}
	
	public List<HDLVariable> HDLInlineFunction.doGetVariables(){
		List<HDLVariable> res = new LinkedList<HDLVariable>();
		res.addAll(getArgs());
		return res;
	}
	
	public List<HDLEnumDeclaration> HDLForLoop.doGetEnumDeclarations() {
		return HDLResolver.getallEnumDeclarations(getDos());
	}

	public List<HDLInterface> HDLForLoop.doGetInterfaceDeclarations() {
		return HDLResolver.getallInterfaceDeclarations(getDos());
	}

	public List<HDLVariable> HDLForLoop.doGetVariables() {
		List<HDLVariable> res = new LinkedList<HDLVariable>();
		res.addAll(HDLResolver.getallVariableDeclarations(getDos()));
		res.add(getParam());
		return res;
	}
	public List<HDLEnumDeclaration> HDLBlock.doGetEnumDeclarations() {
		return HDLResolver.getallEnumDeclarations(getStatements());
	}
	
	public List<HDLInterface> HDLBlock.doGetInterfaceDeclarations() {
		return HDLResolver.getallInterfaceDeclarations(getStatements());
	}
	
	public List<HDLVariable> HDLBlock.doGetVariables() {
		return HDLResolver.getallVariableDeclarations(getStatements());
	}
	
	public List<HDLEnumDeclaration> IStatementContainer.doGetEnumDeclarations() {
		return Collections.emptyList();
	}

	public List<HDLInterface> IStatementContainer.doGetInterfaceDeclarations() {
		return Collections.emptyList();
	}

	public List<HDLVariable> IStatementContainer.doGetVariables() {
		return Collections.emptyList();
	}
	public List<HDLFunction> IStatementContainer.doGetFunctions() {
		return Collections.emptyList();
	}
	
	public HDLEnum HDLAssignment.resolveEnum(de.tuhh.ict.pshdl.model.utils.HDLQualifiedName hEnum) {
		if (getContainer()==null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_ENUM, this, "for enum:" + hEnum));
		return getContainer().resolveEnum(hEnum);
	}

	public HDLInterface HDLAssignment.resolveInterface(HDLQualifiedName hIf) {
		if (getContainer()==null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_INTERFACE, this, "for interface:" + hIf));
		return getContainer().resolveInterface(hIf);
	}

	public HDLType HDLAssignment.resolveType(HDLQualifiedName var) {
		if (getContainer()==null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_TYPE, this, "for type:" + var));
		return getContainer().resolveType(var);
	}

	public HDLVariable HDLAssignment.resolveVariable(HDLQualifiedName var) {
		if (getContainer()==null)
			throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_VARIABLE, this, "for variable:" + var));
		return getContainer().resolveVariable(var);
	}
	
	public List<HDLEnumDeclaration> HDLEnumDeclaration.doGetEnumDeclarations() {
		return Collections.singletonList((HDLEnumDeclaration)this);
	}

	
	public List<HDLInterface> HDLInterfaceDeclaration.doGetInterfaceDeclarations() {
		return Collections.singletonList(getHIf());
	}

	
	public List<HDLVariable> HDLVariableDeclaration.doGetVariables() {
		return this.getVariables();
	}


	public List<HDLVariable> HDLInterfaceInstantiation.doGetVariables() {
		return Collections.singletonList(getVar());
	}
	
	public List<HDLEnumDeclaration> HDLSwitchCaseStatement.doGetEnumDeclarations() {
		return HDLResolver.getallEnumDeclarations(getDos());
	}

	public List<HDLInterface> HDLSwitchCaseStatement.doGetInterfaceDeclarations() {
		return HDLResolver.getallInterfaceDeclarations(getDos());
	}

	public List<HDLVariable> HDLSwitchCaseStatement.doGetVariables() {
		return HDLResolver.getallVariableDeclarations(getDos());
	}
	
	public List<HDLEnumDeclaration> HDLSwitchStatement.doGetEnumDeclarations() {
		List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
		for (HDLSwitchCaseStatement c : getCases()) {
			res.addAll(c.doGetEnumDeclarations());
		}
		return res;
	}

	public List<HDLInterface> HDLSwitchStatement.doGetInterfaceDeclarations() {
		List<HDLInterface> res = new LinkedList<HDLInterface>();
		for (HDLSwitchCaseStatement c : getCases()) {
			res.addAll(c.doGetInterfaceDeclarations());
		}
		return res;
	}

	public List<HDLVariable> HDLSwitchStatement.doGetVariables() {
		List<HDLVariable> res = new LinkedList<HDLVariable>();
		for (HDLSwitchCaseStatement c : getCases()) {
			res.addAll(c.doGetVariables());
		}
		return res;
	}
	
	private HDLResolver HDLUnit.resolver = new HDLResolver(this, false);

	public HDLEnum HDLUnit.resolveEnum(HDLQualifiedName hEnum) {
		HDLEnum resolveEnum = resolver.resolveEnum(hEnum);
		if (resolveEnum != null)
			return resolveEnum;
		return (HDLEnum) resolveType(hEnum);
	}
	public HDLFunction HDLUnit.resolveFunction(HDLQualifiedName hEnum) {
		HDLFunction resolveEnum = resolver.resolveFunction(hEnum);
		if (resolveEnum != null)
			return resolveEnum;
		if (library == null)
			library = HDLLibrary.getLibrary(getLibURI());
		return library.resolveFunction(getImports(), hEnum);
	}

	public HDLInterface HDLUnit.resolveInterface(HDLQualifiedName hIf) {
		HDLInterface resolveInterface = resolver.resolveInterface(hIf);
		if (resolveInterface != null)
			return resolveInterface;
		return (HDLInterface) resolveType(hIf);
	}

	public HDLType HDLUnit.resolveType(HDLQualifiedName type) {
		HDLType resolveType = resolver.resolveType(type);
		if (resolveType != null)
			return resolveType;
		if (library == null)
			library = HDLLibrary.getLibrary(getLibURI());
		return library.resolve(getImports(), type);
	}

	private HDLLibrary HDLUnit.library;

	public HDLVariable HDLUnit.resolveVariable(HDLQualifiedName var) {
		HDLVariable hdlVariable = resolver.resolveVariable(var);
		if (hdlVariable != null)
			return hdlVariable;
		return null;
	}

	public List<HDLEnumDeclaration> HDLUnit.doGetEnumDeclarations() {
		return HDLResolver.getallEnumDeclarations(getStatements());
	}

	public List<HDLInterface> HDLUnit.doGetInterfaceDeclarations() {
		return HDLResolver.getallInterfaceDeclarations(getStatements());
	}

	public List<HDLVariable> HDLUnit.doGetVariables() {
		return HDLResolver.getallVariableDeclarations(getStatements());
	}
}

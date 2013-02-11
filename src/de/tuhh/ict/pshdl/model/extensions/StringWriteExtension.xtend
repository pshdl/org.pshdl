package de.tuhh.ict.pshdl.model.extensions

import de.tuhh.ict.pshdl.model.HDLAnnotation
import de.tuhh.ict.pshdl.model.HDLArgument
import de.tuhh.ict.pshdl.model.HDLAssignment
import de.tuhh.ict.pshdl.model.HDLBitOp
import de.tuhh.ict.pshdl.model.HDLBitOp$HDLBitOpType
import de.tuhh.ict.pshdl.model.HDLBlock
import de.tuhh.ict.pshdl.model.HDLConcat
import de.tuhh.ict.pshdl.model.HDLDeclaration
import de.tuhh.ict.pshdl.model.HDLDirectGeneration
import de.tuhh.ict.pshdl.model.HDLEnum
import de.tuhh.ict.pshdl.model.HDLEnumDeclaration
import de.tuhh.ict.pshdl.model.HDLEnumRef
import de.tuhh.ict.pshdl.model.HDLEqualityOp
import de.tuhh.ict.pshdl.model.HDLExpression
import de.tuhh.ict.pshdl.model.HDLForLoop
import de.tuhh.ict.pshdl.model.HDLFunctionCall
import de.tuhh.ict.pshdl.model.HDLIfStatement
import de.tuhh.ict.pshdl.model.HDLInlineFunction
import de.tuhh.ict.pshdl.model.HDLInterface
import de.tuhh.ict.pshdl.model.HDLInterfaceDeclaration
import de.tuhh.ict.pshdl.model.HDLInterfaceInstantiation
import de.tuhh.ict.pshdl.model.HDLInterfaceRef
import de.tuhh.ict.pshdl.model.HDLLiteral
import de.tuhh.ict.pshdl.model.HDLManip
import de.tuhh.ict.pshdl.model.HDLManip$HDLManipType
import de.tuhh.ict.pshdl.model.HDLNativeFunction
import de.tuhh.ict.pshdl.model.HDLOpExpression
import de.tuhh.ict.pshdl.model.HDLPackage
import de.tuhh.ict.pshdl.model.HDLPrimitive
import de.tuhh.ict.pshdl.model.HDLRange
import de.tuhh.ict.pshdl.model.HDLRegisterConfig
import de.tuhh.ict.pshdl.model.HDLStatement
import de.tuhh.ict.pshdl.model.HDLSubstituteFunction
import de.tuhh.ict.pshdl.model.HDLSwitchCaseStatement
import de.tuhh.ict.pshdl.model.HDLSwitchStatement
import de.tuhh.ict.pshdl.model.HDLTernary
import de.tuhh.ict.pshdl.model.HDLType
import de.tuhh.ict.pshdl.model.HDLUnit
import de.tuhh.ict.pshdl.model.HDLVariable
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration
import de.tuhh.ict.pshdl.model.HDLVariableRef
import de.tuhh.ict.pshdl.model.IHDLObject
import de.tuhh.ict.pshdl.model.utils.SyntaxHighlighter
import de.tuhh.ict.pshdl.model.utils.SyntaxHighlighter$Context

import static de.tuhh.ict.pshdl.model.extensions.StringWriteExtension.*
import de.tuhh.ict.pshdl.model.HDLUnresolvedFragment
import de.tuhh.ict.pshdl.model.HDLUnresolvedFragmentFunction

class StringWriteExtension {
	
	public static StringWriteExtension INST=new StringWriteExtension

	def dispatch String toString(HDLExpression exp, SyntaxHighlighter highlight) {
		throw new RuntimeException("Did not implement toString for "+exp.classType)
	}
	def dispatch String toString(HDLStatement exp, SyntaxHighlighter highlight) {
		throw new RuntimeException("Did not implement toString for "+exp.classType)
	}
	
	def static String asString(IHDLObject exp, SyntaxHighlighter highlight) {
		return INST.toString(exp, highlight)
	}

	def dispatch String toString(HDLAnnotation anno, SyntaxHighlighter highlight){
		val StringBuilder sb=new StringBuilder
		sb.append(highlight.annotation(anno.name))
		if (anno.value!=null)
			sb.append("(").append(highlight.string("\""+anno.value+"\"")).append(")")
		return sb.toString
	}
	
	def dispatch String toString(HDLTernary tern, SyntaxHighlighter highlight) 
		'''(«tern.ifExpr.toString(highlight)»«highlight.operator("?")»«tern.thenExpr.toString(highlight)»«highlight.operator(":")»«tern.elseExpr.toString(highlight)»)'''
	
	
	def dispatch String toString(HDLOpExpression op, SyntaxHighlighter highlight) 
		'''(«op.left.toString(highlight)»«highlight.operator(op.type.toString)»«op.right.toString(highlight)»)'''
	
	def dispatch String toString(HDLEqualityOp op, SyntaxHighlighter highlight) 
		'''(«op.left.toString(highlight)»«highlight.simpleSpace»«highlight.operator(op.type.toString)»«highlight.simpleSpace»«op.right.toString(highlight)»)'''

	def dispatch String toString(HDLUnresolvedFragmentFunction frag, SyntaxHighlighter highlight) {
		val String sup=_toString(frag as HDLUnresolvedFragment, highlight)
		val res=sup+'''(«FOR HDLExpression p : frag.params SEPARATOR ','»«p.toString(highlight)»«ENDFOR»)'''
		return res
	}
	def dispatch String toString(HDLUnresolvedFragment frag, SyntaxHighlighter highlight) {
		val StringBuilder sb=new StringBuilder
		sb.append(frag.frag)
		sb.append('''«FOR HDLExpression p : frag.array BEFORE '[' SEPARATOR '][' AFTER ']'»«p.toString(highlight)»«ENDFOR»''')
		sb.append('''«FOR HDLRange p : frag.bits BEFORE '{' SEPARATOR ',' AFTER '}'»«p.toString(highlight)»«ENDFOR»''')
		if (frag.sub!=null){
			sb.append('.').append(frag.sub.toString(highlight))
		}
		return sb.toString
	}
	def dispatch String toString(HDLBitOp bitOp, SyntaxHighlighter highlight) {
		val StringBuilder sb = new StringBuilder
		sb.append("(").append(bitOp.left.toString(highlight))
		val type=bitOp.type
		if (type==HDLBitOp$HDLBitOpType::LOGI_AND ||  type==HDLBitOp$HDLBitOpType::LOGI_OR)
			sb.append(highlight.simpleSpace).append(highlight.operator(type.toString)).append(highlight.simpleSpace)
		else
			sb.append(highlight.operator(type.toString))
		sb.append(bitOp.right.toString(highlight)).append(")")
		return sb.toString
	}

	def dispatch String toString(HDLConcat concat, SyntaxHighlighter highlight) 
		'''«FOR HDLExpression cat : concat.cats SEPARATOR highlight.operator("#")»«highlight.operator(cat.toString(highlight))»«ENDFOR»'''

	def dispatch String toString(HDLFunctionCall func, SyntaxHighlighter highlight) {
		var boolean isStatement=false
		val container=func.container
		if (container instanceof HDLStatement)
			isStatement=true
		if (container instanceof HDLBlock)
			isStatement=true
		if (container instanceof HDLUnit)
			isStatement=true
		val StringBuilder sb=if (isStatement) highlight.spacing else new StringBuilder
		sb.append(highlight.functionCall(func.nameRefName.toString)).append('(')
		sb.append('''«FOR HDLExpression p : func.params SEPARATOR ','»«p.toString(highlight)»«ENDFOR»''')
		sb.append(')')
		if (isStatement)
			sb.append(';')
		return sb.toString
	}
	
	def dispatch String toString(HDLNativeFunction func, SyntaxHighlighter highlight) {
		val StringBuilder sb = new StringBuilder
		for (HDLAnnotation anno : func.annotations) {
			sb.append(anno.toString(highlight)).append(highlight.simpleSpace)
		}
		if (func?.simOnly)
			sb.append(highlight.keyword("simulation")).append(highlight.simpleSpace)
		sb.append(highlight.keyword("native"))
		sb.append(highlight.simpleSpace)
		sb.append(highlight.keyword("function"))
		sb.append(highlight.simpleSpace)
		sb.append(highlight.functionDecl(func.name)).append(";").append(highlight.newLine)
		return sb.toString
	}
	def dispatch String toString(HDLInlineFunction func, SyntaxHighlighter highlight) {
		val StringBuilder sb = new StringBuilder
		for (HDLAnnotation anno : func.annotations) {
			sb.append(anno.toString(highlight)).append(highlight.simpleSpace)
		}
		sb.append(highlight.keyword("inline")).append(highlight.simpleSpace).append(highlight.keyword("function")).append(highlight.simpleSpace)
		sb.append(highlight.functionDecl(func.name))
		sb.append('''(«FOR HDLVariable hVar:func.args SEPARATOR ','»«highlight.varName(hVar)»«ENDFOR»)''')
		sb.append(highlight.simpleSpace).append("->").append(highlight.simpleSpace).append("(").append(func.expr.toString(highlight)).append(")").append(highlight.newLine)
		return sb.toString
	}
	def dispatch String toString(HDLSubstituteFunction func, SyntaxHighlighter highlight) {
		val StringBuilder sb = new StringBuilder
		for (HDLAnnotation anno : func.annotations) {
			sb.append(anno.toString(highlight)).append(highlight.simpleSpace)
		}
		sb.append(highlight.keyword("substitute")).append(highlight.simpleSpace).append(highlight.keyword("function")).append(highlight.simpleSpace)
		sb.append(highlight.functionDecl(func.name))
		sb.append('''(«FOR HDLVariable hVar:func.args SEPARATOR ','»«highlight.varName(hVar)»«ENDFOR»)''')
		sb.append(highlight.simpleSpace).append("{").append(highlight.newLine)
		highlight.incSpacing
		for (HDLStatement string : func.stmnts) {
			sb.append(string.toString(highlight)).append(highlight.newLine)
		}
		highlight.decSpacing
		sb.append("}").append(highlight.newLine)
		return sb.toString
	}

	def dispatch String toString(HDLInterfaceRef ref, SyntaxHighlighter highlight) {
		val StringBuilder sb = new StringBuilder
		sb.append(highlight.interfaceRef(ref.HIfRefName.lastSegment))
		for (HDLExpression arr : ref.ifArray) {
			sb.append('[').append(arr.toString(highlight)).append(']')
		}
		sb.append('.')
		sb.append(varRef(ref, highlight))
		return sb.toString
	}

	def dispatch String toString(HDLVariableRef ref, SyntaxHighlighter highlight) {
		return varRef(ref, highlight).toString
	}

	def StringBuilder varRef(HDLVariableRef ref, SyntaxHighlighter highlight) {
		val StringBuilder sb = new StringBuilder
		sb.append(highlight.variableRefName(ref))
		for (HDLExpression a : ref.array) {
			sb.append('[').append(a.toString(highlight)).append(']')
		}
		if (ref.bits.size != 0) {
			sb.append('''{«FOR HDLRange bit : ref.bits SEPARATOR ','»«bit.toString(highlight)»«ENDFOR»}''')
		}
		return sb
	}

	def dispatch String toString(HDLLiteral lit, SyntaxHighlighter highlight) {
		if (lit.str)
			return highlight.literal('"'+lit.^val+'"')
		return highlight.literal(lit.^val)
	}

	def dispatch String toString(HDLManip manip, SyntaxHighlighter highlight) {
		val manipType=manip.type
		switch manipType {
		case HDLManip$HDLManipType::ARITH_NEG:
			return highlight.operator("-") + manip.target.toString(highlight)
		case HDLManip$HDLManipType::CAST: {
			val HDLPrimitive type = manip.castTo as HDLPrimitive
			val String width = if (type.width != null)  highlight.width("<" + type.width.toString(highlight) + ">") else  ""
			return "(" + highlight.keyword(type.type.toString.toLowerCase) + width + ")" + manip.target.toString(highlight)
		}
		case HDLManip$HDLManipType::BIT_NEG:
			return highlight.operator("~") + manip.target.toString(highlight)
		case HDLManip$HDLManipType::LOGIC_NEG:
			return highlight.operator("!") + manip.target.toString(highlight)
		}
		throw new IllegalArgumentException("Unexpected Type:" + manip.type)
	}


	def dispatch String toString(HDLBlock block, SyntaxHighlighter highlight) {
		val StringBuilder sb=new StringBuilder(highlight.spacing)
		if (block?.process){
			sb.append("process").append(highlight.simpleSpace)
		}
		sb.append('{').append(highlight.newLine)
		highlight.incSpacing
		for (HDLStatement string : block.statements) {
			sb.append(string.toString(highlight)).append(highlight.newLine)
		}
		highlight.decSpacing
		sb.append(highlight.spacing).append("}")
		return sb.toString
	}
	
	def dispatch String toString(HDLAssignment ass, SyntaxHighlighter highlight) {
		val StringBuilder builder = highlight.spacing
		builder.append(ass.left.toString(highlight))
		builder.append(highlight.operator(ass.type.toString))
		builder.append(ass.right.toString(highlight)).append(';')
		return builder.toString
	}

	def dispatch String toString(HDLPrimitive prim, SyntaxHighlighter highlight) {
		val StringBuilder sb = new StringBuilder
		sb.append(highlight.primitiveType(prim.type.toString.toLowerCase))
		if (prim.width != null) {
			sb.append(highlight.width('<'+prim.width.toString(highlight)+'>'))
		}
		return sb.toString
	}

	def dispatch String toString(HDLForLoop loop, SyntaxHighlighter highlight) {
		val StringBuilder space = highlight.spacing
		val StringBuilder sb = new StringBuilder
		sb.append(space).append(highlight.keyword("for")).append(highlight.simpleSpace).append("(").append(loop.param.name).append(highlight.simpleSpace).append("=").append(highlight.simpleSpace)
		sb.append('''{«FOR HDLRange range : loop.range SEPARATOR ','»«range.toString(highlight)»«ENDFOR»}''')
		sb.append(")").append(highlight.simpleSpace).append("{").append(highlight.newLine)
		highlight.incSpacing
		for (HDLStatement string : loop.dos) {
			sb.append(string.toString(highlight)).append(highlight.newLine)
		}
		highlight.decSpacing
		sb.append(space).append("}")
		return sb.toString
	}

	def dispatch String toString(HDLIfStatement ifStmnt, SyntaxHighlighter highlight) {
		val StringBuilder sb = highlight.spacing
		val String origSpacing=sb.toString
		sb.append(highlight.keyword("if")).append(highlight.simpleSpace).append('(').append(ifStmnt.ifExp.toString(highlight)).append(')').append(highlight.simpleSpace).append('{').append(highlight.newLine)
		highlight.incSpacing
		for (HDLStatement stmt : ifStmnt.thenDo) {
			sb.append(stmt.toString(highlight)).append(highlight.newLine)
		}
		if (ifStmnt.elseDo.size != 0) {
			sb.append(origSpacing).append('}').append(highlight.simpleSpace).append(highlight.keyword("else")).append(highlight.simpleSpace).append('{').append(highlight.newLine)
			for (HDLStatement stmt : ifStmnt.elseDo) {
				sb.append(stmt.toString(highlight)).append(highlight.newLine)
			}
		}
		highlight.decSpacing
		sb.append(origSpacing).append('}')
		return sb.toString
	}

	def dispatch String toString(HDLSwitchCaseStatement caseStmnt, SyntaxHighlighter highlight) {
		val StringBuilder sb = highlight.spacing
		if (caseStmnt.label == null)
			sb.append(highlight.keyword("default")).append(':').append(highlight.simpleSpace).append('{').append(highlight.newLine)
		else
			sb.append(highlight.keyword("case")).append(highlight.simpleSpace).append(caseStmnt.label.toString(highlight)).append(':').append(highlight.simpleSpace).append('{').append(highlight.newLine)
		highlight.incSpacing
		for (HDLStatement stmnt : caseStmnt.dos) {
			sb.append(stmnt.toString(highlight)).append(highlight.newLine)
		}
		highlight.decSpacing
		sb.append(highlight.spacing).append('}')
		return sb.toString
	}

	def dispatch String toString(HDLSwitchStatement switchStmnt, SyntaxHighlighter highlight) {
		val StringBuilder sb = highlight.spacing
		sb.append(highlight.keyword("switch")).append('(').append(switchStmnt.caseExp.toString(highlight)).append(')').append(highlight.simpleSpace).append('{').append(highlight.newLine)
		highlight.incSpacing
		for (HDLStatement stmnt : switchStmnt.cases) {
			sb.append(stmnt.toString(highlight)).append(highlight.newLine)
		}
		highlight.decSpacing
		sb.append(highlight.spacing).append('}')
		return sb.toString
	}

	def dispatch String toString(HDLVariableDeclaration hvd, SyntaxHighlighter highlight) {
		val StringBuilder sb = highlight.spacing
		val HDLType resolveType = hvd.resolveType
		if (hvd.annotations != null) {
			for (HDLAnnotation hdla : hvd.annotations) {
				sb.append(hdla.toString(highlight)).append(highlight.simpleSpace)
			}
		}
		val String dirString = hvd.direction.toString
		if (dirString.length > 0)
			sb.append(highlight.direction(dirString)).append(highlight.simpleSpace)
		if (hvd.register != null)
			sb.append(hvd.register.toString(highlight))
		if (resolveType instanceof HDLEnum) {
			sb.append(highlight.keyword("enum")).append(highlight.simpleSpace).append(resolveType.toString(highlight))
		} else
			sb.append(resolveType.toString(highlight))
		sb.append('''«FOR HDLVariable hvar : hvd.variables BEFORE highlight.simpleSpace SEPARATOR ','»«hvar.toString(highlight)»«ENDFOR»;''')
		if (highlight.context==SyntaxHighlighter$Context::HDLPackage)
			sb.append(highlight.newLine)
		return sb.toString
	}

	def dispatch String toString(HDLInterfaceDeclaration hid, SyntaxHighlighter highlight) {
		highlight.pushContext(SyntaxHighlighter$Context::HDLInterface)
		val StringBuilder annos=highlight.spacing
		for (HDLAnnotation anno : hid.annotations) {
			annos.append(anno.toString(highlight)).append(highlight.simpleSpace)
		}
		annos.append(hid.HIf.toString(highlight))
		highlight.popContext
		return annos.toString
	}
	def dispatch String toString(HDLInterface hif, SyntaxHighlighter highlight) {
		val StringBuilder sb = highlight.spacing
		sb.append(highlight.keyword("interface")).append(highlight.simpleSpace)
		sb.append(highlight.interfaceName(hif.name))
		sb.append("{").append(highlight.newLine)
		highlight.incSpacing
		for (HDLVariableDeclaration hvar : hif.ports) {
			sb.append(hvar.toString(highlight)).append(highlight.newLine)
		}
		highlight.decSpacing
		sb.append("}").append(highlight.newLine)
		return sb.toString
	}

	def dispatch String toString(HDLEnumRef ref, SyntaxHighlighter highlight) {
		//XXX Just using the last segment might not be correct as it might be non local
		return highlight.enumRefType(ref.HEnumRefName.toString) + "." + highlight.enumRefVar(ref.varRefName.lastSegment)
	}

	def dispatch String toString(HDLEnum e, SyntaxHighlighter highlight) {
		return highlight.enumName(e.name)
	}

	def dispatch String toString(HDLEnumDeclaration decl, SyntaxHighlighter highlight) {
		val StringBuilder sb = highlight.spacing
		for (HDLAnnotation anno : decl.annotations) {
			sb.append(anno.toString(highlight)).append(highlight.simpleSpace)
		}
		sb.append(highlight.keyword("enum")).append(highlight.simpleSpace)
		sb.append(highlight.enumName(decl.HEnum.name))
		sb.append(highlight.simpleSpace).append("=").append(highlight.simpleSpace)
		sb.append('''{«FOR HDLVariable henum : decl.HEnum.enums SEPARATOR ','+highlight.simpleSpace»«henum.toString(highlight)»«ENDFOR»}''')
		sb.append(highlight.newLine)
		return sb.toString
	}

	def dispatch String toString(HDLRegisterConfig reg, SyntaxHighlighter highlight) {
		val StringBuilder sb = new StringBuilder
		sb.append(highlight.keyword("register"))
		val HDLRegisterConfig defaultReg = HDLRegisterConfig::defaultConfig
		val StringBuilder params = new StringBuilder
		params.append('(')
		var boolean first = true
		if (!reg.clkRefName.equals(defaultReg.clkRefName)) {
			params.append(highlight.param(HDLRegisterConfig::CLOCK_PARAM)).append('=').append(highlight.variableRefName(reg.clkRefName))
			first = false
		}
		if (!reg.rstRefName.equals(defaultReg.rstRefName)) {
			if (!first)
				params.append(", ")
			params.append(highlight.param(HDLRegisterConfig::RESET_PARAM)).append('=').append(highlight.variableRefName(reg.rstRefName))
			first = false
		}
		if (reg.clockType!=null && !reg.clockType.equals(defaultReg.clockType)) {
			if (!first)
				params.append(", ")
			params.append(highlight.param(HDLRegisterConfig::EDGE_PARAM)).append('=').append(highlight.enumRefType("Edge")).append('.').append(highlight.enumRefVar(reg.clockType.toString))
			first = false
		}
		if (reg.syncType!=null && !reg.syncType.equals(defaultReg.syncType)) {
			if (!first)
				params.append(", ")
			params.append(highlight.param(HDLRegisterConfig::RESET_SYNC_PARAM)).append('=').append(highlight.enumRefType("Sync")).append('.').append(highlight.enumRefVar(reg.syncType.toString))
			first = false
		}
		if (reg.resetType!=null && !reg.resetType.equals(defaultReg.resetType)) {
			if (!first)
				params.append(", ")
			params.append(highlight.param(HDLRegisterConfig::RESET_TYPE_PARAM)).append('=').append(highlight.enumRefType("Active")).append('.').append(highlight.enumRefVar(reg.resetType.toString))
			first = false
		}
		if (!reg.resetValue.equals(defaultReg.resetValue)) {
			if (!first)
				params.append(", ")
			params.append(highlight.param(HDLRegisterConfig::RESET_VALUE_PARAM)).append('=').append(reg.resetValue.toString(highlight))
			first = false
		}
		params.append(')')
		if (!first)
			sb.append(params)
		sb.append(highlight.simpleSpace)
		return sb.toString
	}

	def dispatch String toString(HDLPackage pkg, SyntaxHighlighter highlight){
		val StringBuilder sb=new StringBuilder
		highlight.pushContext(SyntaxHighlighter$Context::HDLPackage)
		if (pkg.pkg!=null)
			sb.append(highlight.keyword("package")).append(highlight.simpleSpace).append(highlight.packageName(pkg.pkg)).append(";").append(highlight.newLine)
		for (HDLDeclaration decl : pkg.declarations) {
			sb.append(decl.toString(highlight))
		}
		for (HDLUnit unit : pkg.units) {
			sb.append(unit.toString(highlight))
		}
		highlight.popContext
		return sb.toString
	}
	
	def dispatch String toString(HDLUnit unit, SyntaxHighlighter highlight) {
		val StringBuilder sb = new StringBuilder
		highlight.pushContext(SyntaxHighlighter$Context::HDLUnit)
		if (!unit.simulation)
			sb.append(highlight.keyword("module")).append(highlight.simpleSpace)
		else
			sb.append(highlight.keyword("testbench")).append(highlight.simpleSpace)
		sb.append(highlight.unitName(unit.name)).append("{").append(highlight.newLine)
		highlight.incSpacing
		for (String imports : unit.imports) {
			sb.append(highlight.spacing).append(highlight.keyword("import")).append(highlight.simpleSpace).append(highlight.importName(imports)).append(";").append(highlight.newLine)
		}
		for (HDLStatement stmnt : unit.inits) {
			sb.append(stmnt.toString(highlight)).append(highlight.newLine)
		}
		for (HDLStatement stmnt : unit.statements) {
			sb.append(stmnt.toString(highlight)).append(highlight.newLine)
		}
		highlight.decSpacing
		sb.append("}").append(highlight.newLine)
		highlight.popContext
		return sb.toString

	}

	def dispatch String toString(HDLInterfaceInstantiation hii, SyntaxHighlighter highlight) {
		val StringBuilder sb = highlight.spacing
		sb.append(highlight.interfaceName(hii.HIfRefName.toString)).append(highlight.simpleSpace).append(hii.^var.toString(highlight))
		sb.append('''«FOR HDLArgument arg : hii.arguments BEFORE '(' SEPARATOR ',' AFTER ')'»«arg.toString(highlight)»«ENDFOR»''')
		sb.append(';')
		return sb.toString
	}
	
	

	def dispatch String toString(HDLArgument arg, SyntaxHighlighter highlight) {
		val StringBuilder sb=new StringBuilder
		sb.append(highlight.param(arg.name)).append('=').append(arg.expression.toString(highlight))
		return sb.toString
	}
	
	def dispatch String toString(HDLRange range, SyntaxHighlighter highlight) {
		if (range.from != null) {
			return range.from.toString(highlight) + ":" + range.to.toString(highlight)
		}
		return range.to.toString(highlight)
	}

	def dispatch String toString(HDLVariable hVar, SyntaxHighlighter highlight) {
		val StringBuilder sb = new StringBuilder
		for (HDLAnnotation anno : hVar.annotations) {
			sb.append(anno.toString(highlight)).append(highlight.simpleSpace)
		}
		sb.append(highlight.varName(hVar))
		for (HDLExpression arr : hVar.dimensions) {
			sb.append('[').append(arr.toString(highlight)).append(']')
		}
		if (hVar.defaultValue != null)
			sb.append('=').append(hVar.defaultValue.toString(highlight))
		return sb.toString
	}

	def dispatch String toString(HDLDirectGeneration hdg, SyntaxHighlighter highlight) {
		val StringBuilder sb = highlight.spacing
		sb.append(highlight.interfaceName(hdg.HIf.name)).append(highlight.simpleSpace).append(highlight.varName(hdg.^var)).append("=").append(highlight.generatorID(hdg.generatorID))
		sb.append('(')
		for (HDLArgument args : hdg.arguments) {
			sb.append(args.toString(highlight))
		}
		sb.append(')')
		if (hdg.generatorContent != null) {
			sb.append(highlight.generatorContent(hdg.generatorID, hdg.generatorContent))
		}
		sb.append(";")
		return sb.toString
	}
}

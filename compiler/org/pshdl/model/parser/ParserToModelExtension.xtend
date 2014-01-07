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
package org.pshdl.model.parser

import java.util.ArrayList
import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.pshdl.model.HDLAnnotation
import org.pshdl.model.HDLArgument
import org.pshdl.model.HDLArithOp
import org.pshdl.model.HDLArithOp.HDLArithOpType
import org.pshdl.model.HDLArrayInit
import org.pshdl.model.HDLAssignment
import org.pshdl.model.HDLAssignment.HDLAssignmentType
import org.pshdl.model.HDLBitOp
import org.pshdl.model.HDLBitOp.HDLBitOpType
import org.pshdl.model.HDLBlock
import org.pshdl.model.HDLConcat
import org.pshdl.model.HDLDeclaration
import org.pshdl.model.HDLDirectGeneration
import org.pshdl.model.HDLEnum
import org.pshdl.model.HDLEnumDeclaration
import org.pshdl.model.HDLEqualityOp
import org.pshdl.model.HDLEqualityOp.HDLEqualityOpType
import org.pshdl.model.HDLExpression
import org.pshdl.model.HDLForLoop
import org.pshdl.model.HDLFunction
import org.pshdl.model.HDLFunctionParameter
import org.pshdl.model.HDLFunctionParameter.RWType
import org.pshdl.model.HDLFunctionParameter.Type
import org.pshdl.model.HDLIfStatement
import org.pshdl.model.HDLInlineFunction
import org.pshdl.model.HDLInterface
import org.pshdl.model.HDLInterfaceDeclaration
import org.pshdl.model.HDLInterfaceInstantiation
import org.pshdl.model.HDLLiteral
import org.pshdl.model.HDLManip
import org.pshdl.model.HDLManip.HDLManipType
import org.pshdl.model.HDLNativeFunction
import org.pshdl.model.HDLPackage
import org.pshdl.model.HDLPrimitive
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType
import org.pshdl.model.HDLRange
import org.pshdl.model.HDLReference
import org.pshdl.model.HDLRegisterConfig
import org.pshdl.model.HDLShiftOp
import org.pshdl.model.HDLShiftOp.HDLShiftOpType
import org.pshdl.model.HDLStatement
import org.pshdl.model.HDLSubstituteFunction
import org.pshdl.model.HDLSwitchCaseStatement
import org.pshdl.model.HDLSwitchStatement
import org.pshdl.model.HDLTernary
import org.pshdl.model.HDLType
import org.pshdl.model.HDLUnit
import org.pshdl.model.HDLUnresolvedFragment
import org.pshdl.model.HDLUnresolvedFragmentFunction
import org.pshdl.model.HDLVariable
import org.pshdl.model.HDLVariableDeclaration
import org.pshdl.model.HDLVariableDeclaration.HDLDirection
import org.pshdl.model.IHDLObject
import org.pshdl.model.parser.PSHDLLangParser.PsAccessRangeContext
import org.pshdl.model.parser.PSHDLLangParser.PsAddContext
import org.pshdl.model.parser.PSHDLLangParser.PsAnnotationContext
import org.pshdl.model.parser.PSHDLLangParser.PsArgumentContext
import org.pshdl.model.parser.PSHDLLangParser.PsArrayInitContext
import org.pshdl.model.parser.PSHDLLangParser.PsArrayInitExpContext
import org.pshdl.model.parser.PSHDLLangParser.PsArrayInitSubContext
import org.pshdl.model.parser.PSHDLLangParser.PsArrayInitSubParensContext
import org.pshdl.model.parser.PSHDLLangParser.PsAssignmentOrFuncContext
import org.pshdl.model.parser.PSHDLLangParser.PsBitAndContext
import org.pshdl.model.parser.PSHDLLangParser.PsBitLogAndContext
import org.pshdl.model.parser.PSHDLLangParser.PsBitLogOrContext
import org.pshdl.model.parser.PSHDLLangParser.PsBitOrContext
import org.pshdl.model.parser.PSHDLLangParser.PsBitXorContext
import org.pshdl.model.parser.PSHDLLangParser.PsBlockContext
import org.pshdl.model.parser.PSHDLLangParser.PsCaseStatementsContext
import org.pshdl.model.parser.PSHDLLangParser.PsCastContext
import org.pshdl.model.parser.PSHDLLangParser.PsCompoundStatementContext
import org.pshdl.model.parser.PSHDLLangParser.PsConcatContext
import org.pshdl.model.parser.PSHDLLangParser.PsDeclAssignmentContext
import org.pshdl.model.parser.PSHDLLangParser.PsDeclarationContext
import org.pshdl.model.parser.PSHDLLangParser.PsDeclarationTypeContext
import org.pshdl.model.parser.PSHDLLangParser.PsDirectGenerationContext
import org.pshdl.model.parser.PSHDLLangParser.PsEnumContext
import org.pshdl.model.parser.PSHDLLangParser.PsEnumDeclarationContext
import org.pshdl.model.parser.PSHDLLangParser.PsEqualityCompContext
import org.pshdl.model.parser.PSHDLLangParser.PsEqualityContext
import org.pshdl.model.parser.PSHDLLangParser.PsExpressionContext
import org.pshdl.model.parser.PSHDLLangParser.PsForStatementContext
import org.pshdl.model.parser.PSHDLLangParser.PsFuncParamTypeContext
import org.pshdl.model.parser.PSHDLLangParser.PsFuncParamWithRWContext
import org.pshdl.model.parser.PSHDLLangParser.PsFuncRecturnTypeContext
import org.pshdl.model.parser.PSHDLLangParser.PsFuncSpecContext
import org.pshdl.model.parser.PSHDLLangParser.PsFunctionContext
import org.pshdl.model.parser.PSHDLLangParser.PsFunctionDeclarationContext
import org.pshdl.model.parser.PSHDLLangParser.PsIfStatementContext
import org.pshdl.model.parser.PSHDLLangParser.PsImportsContext
import org.pshdl.model.parser.PSHDLLangParser.PsInlineFunctionContext
import org.pshdl.model.parser.PSHDLLangParser.PsInstantiationContext
import org.pshdl.model.parser.PSHDLLangParser.PsInterfaceContext
import org.pshdl.model.parser.PSHDLLangParser.PsInterfaceDeclarationContext
import org.pshdl.model.parser.PSHDLLangParser.PsInterfaceInstantiationContext
import org.pshdl.model.parser.PSHDLLangParser.PsManipContext
import org.pshdl.model.parser.PSHDLLangParser.PsModelContext
import org.pshdl.model.parser.PSHDLLangParser.PsMulContext
import org.pshdl.model.parser.PSHDLLangParser.PsNativeFunctionContext
import org.pshdl.model.parser.PSHDLLangParser.PsParensContext
import org.pshdl.model.parser.PSHDLLangParser.PsPortDeclarationContext
import org.pshdl.model.parser.PSHDLLangParser.PsPrimitiveContext
import org.pshdl.model.parser.PSHDLLangParser.PsProcessContext
import org.pshdl.model.parser.PSHDLLangParser.PsQualifiedNameContext
import org.pshdl.model.parser.PSHDLLangParser.PsRefPartContext
import org.pshdl.model.parser.PSHDLLangParser.PsShiftContext
import org.pshdl.model.parser.PSHDLLangParser.PsStatementContext
import org.pshdl.model.parser.PSHDLLangParser.PsSubstituteFunctionContext
import org.pshdl.model.parser.PSHDLLangParser.PsSwitchStatementContext
import org.pshdl.model.parser.PSHDLLangParser.PsTernaryContext
import org.pshdl.model.parser.PSHDLLangParser.PsTypeDeclarationContext
import org.pshdl.model.parser.PSHDLLangParser.PsUnitContext
import org.pshdl.model.parser.PSHDLLangParser.PsValueContext
import org.pshdl.model.parser.PSHDLLangParser.PsValueExpContext
import org.pshdl.model.parser.PSHDLLangParser.PsVariableContext
import org.pshdl.model.parser.PSHDLLangParser.PsVariableDeclarationContext
import org.pshdl.model.parser.PSHDLLangParser.PsVariableRefContext
import org.pshdl.model.parser.PSHDLLangParser.PsWidthContext
import org.pshdl.model.utils.HDLLibrary
import org.pshdl.model.utils.HDLQualifiedName

class ParserToModelExtension {
	private BufferedTokenStream tokens

	new(BufferedTokenStream tokens) {
		this.tokens = tokens
	}

	def static HDLPackage toHDL(BufferedTokenStream tokens, PsModelContext ctx, String libURI, String src) {
		return new ParserToModelExtension(tokens).toHDLPkg(ctx, libURI, src)
	}

	def HDLPackage toHDLPkg(PsModelContext ctx, String libURI, String src) {
		var pkg = new HDLPackage().setLibURI(libURI)
		if (ctx.psQualifiedName !== null)
			pkg = pkg.setPkg(ctx.psQualifiedName.toName)
		pkg = pkg.setUnits(ctx.psUnit.map[toHDLUnit(libURI)])
		pkg = pkg.setDeclarations(ctx.psDeclaration.map[toHDL as HDLDeclaration])
		pkg.freeze(null)
		val library = HDLLibrary::getLibrary(libURI)
		if (library==null)
			throw new IllegalArgumentException("The library "+libURI+" is not valid")
		library.addPkg(pkg, src);
		return pkg.attachContext(ctx) as HDLPackage
	}

	def dispatch HDLDeclaration toHDL(PsDeclarationContext context) {
		var HDLDeclaration res = context.psDeclarationType.toHDL as HDLDeclaration;
		res = res.setAnnotations(context.psAnnotation.map[toHDL as HDLAnnotation])
		return res.attachContext(context)
	}

	def <T extends IHDLObject> T attachContext(T obj, ParserRuleContext context) {
		if (obj === null)
			throw new NullPointerException("Null is not allowed")
		obj.addMeta(SourceInfo::INFO, new SourceInfo(tokens, context))
		return obj //attachContext method body
	}

	def dispatch HDLArgument toHDL(PsArgumentContext context) {
		var res = new HDLArgument().setName(context.RULE_ID.text)
		res = res.setExpression(context.psExpression.toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def dispatch HDLBlock toHDL(PsProcessContext context) {
		var block = new HDLBlock
		if (context.isProcess !== null)
			block = block.setProcess(true)
		block = block.setStatements(context.psBlock.map[toHDL as HDLStatement])
		return block.attachContext(context)
	}

	def dispatch HDLAnnotation toHDL(PsAnnotationContext context) {
		val name = context.psAnnotationType.text
		var String value = null
		if (context.RULE_STRING !== null) {
			var str = context.RULE_STRING.text
			str = str.substring(1, str.length - 1)
			value = str
		}
		return new HDLAnnotation().setName(name).setValue(value).attachContext(context)
	}

	def dispatch HDLDeclaration toHDL(PsDeclarationTypeContext context) {
		if (context.psFunctionDeclaration !== null)
			return context.psFunctionDeclaration.toHDL.attachContext(context) as HDLDeclaration
		if (context.psTypeDeclaration !== null)
			return context.psTypeDeclaration.toHDL.attachContext(context) as HDLDeclaration
		if (context.psVariableDeclaration !== null)
			return context.psVariableDeclaration.toHDL.attachContext(context) as HDLDeclaration
		throw new IllegalArgumentException("Not implemented:" + context.getClass)
	}

	def dispatch HDLVariableDeclaration toHDL(PsVariableDeclarationContext context) {
		var res = new HDLVariableDeclaration
		res = res.setType(context.psPrimitive.toHDL as HDLType)
		var HDLDirection dir = HDLDirection::INTERNAL
		if (context.psDirection !== null)
			dir = HDLDirection::getOp(context.psDirection.text)
		res = res.setDirection(dir)
		for (varDecl : context.psDeclAssignment)
			res = res.addVariables(varDecl.toHDL as HDLVariable)
		if (context.psPrimitive.isRegister !== null) {
			var Iterable<HDLArgument> args = new ArrayList<HDLArgument>
			if (context.psPrimitive.psPassedArguments !== null)
				args = context.psPrimitive.psPassedArguments.psArgument.map[toHDL as HDLArgument]
			res = res.setRegister(HDLRegisterConfig::fromArgs(args))
		}
		return res.attachContext(context)
	}

	def dispatch IHDLObject toHDL(PsArrayInitContext context) {
		if (context.psExpression !== null) {
			return context.psExpression.toHDL.attachContext(context)
		}
		return context.psArrayInitSubParens.toHDL
	}
	def dispatch IHDLObject toHDL(PsArrayInitExpContext context) {
		return context.psArrayInitSubParens.toHDL
	}

	def dispatch IHDLObject toHDL(PsArrayInitSubContext context) {
		if (context.psExpression !== null) {
//			if (context.psExpression.size == 1)
//				return context.psExpression.get(0).toHDL.attachContext(context)
			val arr = new HDLArrayInit().setExp(context.psExpression.map[toHDL as HDLExpression])
			return arr.attachContext(context)
		}
		return context.psArrayInitSubParens.toHDL
	}
	
	def dispatch IHDLObject toHDL(PsArrayInitSubParensContext context){
		return context.psArrayInitSub.toHDL.attachContext(context)
	}

	def dispatch HDLType toHDL(PsPrimitiveContext context) {
		if (context.psQualifiedName !== null)
			return new HDLEnum().setName(context.psQualifiedName.toName).attachContext(context)
		val HDLPrimitiveType pt = HDLPrimitiveType::valueOf(context.psPrimitiveType.text.toUpperCase)
		val HDLExpression width = context.psWidth?.toHDL as HDLExpression
		return new HDLPrimitive().setType(pt.getResultingType(width)).setWidth(width).attachContext(context)
	}

	def dispatch HDLVariable toHDL(PsDeclAssignmentContext context) {
		var HDLVariable res = new HDLVariable().setName(context.psVariable.toName)
		res = res.setAnnotations(context.psAnnotation.map[toHDL as HDLAnnotation])
		if (context.psArray !== null)
			res = res.setDimensions(context.psArray.psExpression.map[toHDL as HDLExpression])
		if (context.psArrayInit !== null)
			res = res.setDefaultValue(context.psArrayInit.toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def String toName(PsVariableContext context) {
		return context.RULE_ID.text //NOATTACH PsVariableContext toName body
	}

	def HDLPrimitiveType getResultingType(HDLPrimitiveType pt, HDLExpression width) {
		if (width !== null) {
			switch (pt) {
				case HDLPrimitiveType::BIT:
					return HDLPrimitiveType::BITVECTOR //NOATTACH getResultingType method body
			}
		} else {
			switch (pt) {
				case HDLPrimitiveType::INT:
					return HDLPrimitiveType::INTEGER //NOATTACH getResultingType method body
				case HDLPrimitiveType::UINT:
					return HDLPrimitiveType::NATURAL //NOATTACH getResultingType method body
			}
		}
		return pt; //NOATTACH getResultingType method body
	}

	def dispatch IHDLObject toHDL(PsWidthContext context) {
		return context.psExpression.toHDL.attachContext(context)
	}

	def dispatch IHDLObject toHDL(PsValueContext context) {
		if (context.RULE_PS_LITERAL_TERMINAL !== null)
			return new HDLLiteral().setStr(false).setVal(context.RULE_PS_LITERAL_TERMINAL.text).attachContext(context)
		if (context.RULE_STRING !== null) {
			var str = context.RULE_STRING.text
			str = str.substring(1, str.length - 1)
			return new HDLLiteral().setStr(true).setVal(str).attachContext(context)
		}
		if (context.psVariableRef !== null)
			return context.psVariableRef.toHDL.attachContext(context)
		throw new IllegalArgumentException("Not correctly implemented:" + context.getClass)
	}

	def dispatch IHDLObject toHDL(PsValueExpContext context) {
		return context.psValue.toHDL.attachContext(context)
	}

	def dispatch HDLConcat toHDL(PsConcatContext context) {
		var cat = new HDLConcat
		cat = cat.setCats(context.psExpression.map[toHDL as HDLExpression])
		return cat.attachContext(context)
	}

	def dispatch HDLBitOp toHDL(PsBitLogOrContext context) {
		var res = new HDLBitOp().setType(HDLBitOp$HDLBitOpType::LOGI_OR)
		res = res.setLeft(context.psExpression(0).toHDL as HDLExpression)
		res = res.setRight(context.psExpression(1).toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def dispatch HDLBitOp toHDL(PsBitLogAndContext context) {
		var res = new HDLBitOp().setType(HDLBitOp$HDLBitOpType::LOGI_AND)
		res = res.setLeft(context.psExpression(0).toHDL as HDLExpression)
		res = res.setRight(context.psExpression(1).toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def dispatch HDLBitOp toHDL(PsBitXorContext context) {
		var res = new HDLBitOp().setType(HDLBitOp$HDLBitOpType::XOR)
		res = res.setLeft(context.psExpression(0).toHDL as HDLExpression)
		res = res.setRight(context.psExpression(1).toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def dispatch HDLBitOp toHDL(PsBitOrContext context) {
		var res = new HDLBitOp().setType(HDLBitOp$HDLBitOpType::OR)
		res = res.setLeft(context.psExpression(0).toHDL as HDLExpression)
		res = res.setRight(context.psExpression(1).toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def dispatch HDLBitOp toHDL(PsBitAndContext context) {
		var res = new HDLBitOp().setType(HDLBitOp$HDLBitOpType::AND)
		res = res.setLeft(context.psExpression(0).toHDL as HDLExpression)
		res = res.setRight(context.psExpression(1).toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def dispatch HDLShiftOp toHDL(PsShiftContext context) {
		val type = HDLShiftOp$HDLShiftOpType::getOp(context.op.text)
		var res = new HDLShiftOp().setType(type)
		res = res.setLeft(context.psExpression(0).toHDL as HDLExpression)
		res = res.setRight(context.psExpression(1).toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def dispatch HDLEqualityOp toHDL(PsEqualityCompContext context) {
		val type = HDLEqualityOp$HDLEqualityOpType::getOp(context.op.text)
		var res = new HDLEqualityOp().setType(type)
		res = res.setLeft(context.psExpression(0).toHDL as HDLExpression)
		res = res.setRight(context.psExpression(1).toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def dispatch HDLEqualityOp toHDL(PsEqualityContext context) {
		val type = HDLEqualityOp$HDLEqualityOpType::getOp(context.op.text)
		var res = new HDLEqualityOp().setType(type)
		res = res.setLeft(context.psExpression(0).toHDL as HDLExpression)
		res = res.setRight(context.psExpression(1).toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def dispatch HDLArithOp toHDL(PsMulContext context) {
		val type = HDLArithOp$HDLArithOpType::getOp(context.op.text)
		var res = new HDLArithOp().setType(type)
		res = res.setLeft(context.psExpression(0).toHDL as HDLExpression)
		res = res.setRight(context.psExpression(1).toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def dispatch HDLArithOp toHDL(PsAddContext context) {
		val type = HDLArithOp$HDLArithOpType::getOp(context.op.text)
		var res = new HDLArithOp().setType(type)
		res = res.setLeft(context.psExpression(0).toHDL as HDLExpression)
		res = res.setRight(context.psExpression(1).toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def dispatch HDLPrimitive toHDL(PsCastContext context) {
		val HDLPrimitiveType pt = HDLPrimitiveType::valueOf(context.psPrimitiveType.text.toUpperCase)
		val HDLExpression width = context.psWidth?.toHDL as HDLExpression
		return new HDLPrimitive().setType(pt.getResultingType(width)).setWidth(width).attachContext(context)
	}

	def dispatch HDLManip toHDL(PsManipContext context) {
		var res = new HDLManip().setTarget(context.psExpression.toHDL as HDLExpression)
		if (context.psCast !== null) {
			res = res.setType(HDLManip$HDLManipType::CAST)
			res = res.setCastTo(context.psCast.toHDL as HDLType)
		} else {
			switch (context.type.type) {
				case PSHDLLangLexer::LOGIC_NEG:
					res = res.setType(HDLManip$HDLManipType::LOGIC_NEG)
				case PSHDLLangLexer::ARITH_NEG:
					res = res.setType(HDLManip$HDLManipType::ARITH_NEG)
				case PSHDLLangLexer::BIT_NEG:
					res = res.setType(HDLManip$HDLManipType::BIT_NEG)
			}
		}
		return res.attachContext(context)
	}

	def dispatch HDLTernary toHDL(PsTernaryContext context) {
		var res = new HDLTernary().setIfExpr(context.psExpression(0).toHDL as HDLExpression)
		res = res.setThenExpr(context.psExpression(1).toHDL as HDLExpression)
		res = res.setElseExpr(context.psExpression(2).toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def dispatch IHDLObject toHDL(PsParensContext context) {
		return context.psExpression.toHDL.attachContext(context)
	}

	def dispatch HDLExpression toHDL(PsExpressionContext context) {
		throw new IllegalArgumentException("Not implemented:" + context.getClass)
	}

	def String toName(PsEnumContext context) {
		return context.psQualifiedName.toName //NOATTACH PsEnumContext toName
	}

	def String toName(PsInterfaceContext context) {
		return context.psQualifiedName.toName //NOATTACH PsInterfaceContext toName
	}

	def HDLQualifiedName toFQNName(PsQualifiedNameContext context) {
		return new HDLQualifiedName(context.text)
	}

	def String toName(PsQualifiedNameContext context) {
		return new HDLQualifiedName(context.text).toString //NOATTACH PsQualifiedNameContext toName
	}

	def dispatch IHDLObject toHDL(PsTypeDeclarationContext context) {
		if (context.psEnumDeclaration !== null)
			return context.psEnumDeclaration.toHDL.attachContext(context)
		if (context.psInterfaceDeclaration !== null)
			return context.psInterfaceDeclaration.toHDL.attachContext(context)
		throw new IllegalArgumentException("Not implemented:" + context.getClass)
	}

	def dispatch HDLInterfaceDeclaration toHDL(PsInterfaceDeclarationContext context) {
		var hIf = new HDLInterface().setName(context.psInterface.toName)
		hIf = hIf.setPorts(context.psInterfaceDecl.psPortDeclaration.map[toHDL as HDLVariableDeclaration])
		return new HDLInterfaceDeclaration().setHIf(hIf).attachContext(context)
	}

	def dispatch HDLVariableDeclaration toHDL(PsPortDeclarationContext context) {
		var HDLVariableDeclaration res = context.psVariableDeclaration.toHDL as HDLVariableDeclaration;
		res = res.setAnnotations(context.psAnnotation.map[toHDL as HDLAnnotation])
		return res.attachContext(context)
	}

	def dispatch IHDLObject toHDL(PsBlockContext context) {
		if (context.psDeclaration !== null)
			return context.psDeclaration.toHDL.attachContext(context)
		if (context.psInstantiation !== null)
			return context.psInstantiation.toHDL.attachContext(context)
		if (context.psStatement !== null)
			return context.psStatement.toHDL.attachContext(context)
		throw new IllegalArgumentException("Not correctly implemented type:" + context.getClass());
	}

	def dispatch HDLDirectGeneration toHDL(PsDirectGenerationContext context) {
		var gen = new HDLDirectGeneration().setGeneratorContent("")
		gen = gen.setInclude(context.isInclude !== null)
		gen = gen.setHIf(context.psInterface.toHDL as HDLInterface)
		gen = gen.setVar(context.psVariable.toHDL as HDLVariable)
		gen = gen.setGeneratorID(context.RULE_ID.text)
		if (context.psPassedArguments !== null)
			gen = gen.setArguments(context.psPassedArguments.psArgument.map[toHDL as HDLArgument])
		if (context.RULE_GENERATOR_CONTENT !== null)
			gen = gen.setGeneratorContent(context.RULE_GENERATOR_CONTENT.text)
		return gen.attachContext(context);
	}

	def dispatch HDLVariable toHDL(PsVariableContext context) {
		return new HDLVariable().setName(context.toName).attachContext(context)
	}

	def dispatch HDLInterface toHDL(PsInterfaceContext context) {
		return new HDLInterface().setName(context.toName).attachContext(context)
	}

	def dispatch IHDLObject toHDL(PsInstantiationContext context) {
		if (context.psDirectGeneration !== null)
			return context.psDirectGeneration.toHDL.attachContext(context)
		if (context.psInterfaceInstantiation !== null)
			return context.psInterfaceInstantiation.toHDL.attachContext(context)
		throw new IllegalArgumentException("Not implemented type:" + context.getClass());
	}

	def dispatch HDLEnum toHDL(PsEnumContext context) {
		return new HDLEnum().setName(context.toName).attachContext(context)
	}

	def dispatch HDLEnumDeclaration toHDL(PsEnumDeclarationContext context) {
		var he = context.psEnum.toHDL as HDLEnum
		he = he.setEnums(context.psVariable.map[toHDL as HDLVariable])
		return new HDLEnumDeclaration().setHEnum(he).attachContext(context)
	}

	def dispatch HDLSubstituteFunction toHDL(PsSubstituteFunctionContext context) {
		var func = new HDLSubstituteFunction
		func = func.setName(context.psFunction.toName)
		func = func.setStmnts(context.psStatement.map[toHDL as HDLStatement])
		func = func.setArgs(context.psFuncParam.psFuncSpec.map[toHDL as HDLFunctionParameter])
		if (context.psFuncRecturnType !== null)
			func = func.setReturnType(context.psFuncRecturnType.toHDL as HDLFunctionParameter)
		return func.attachContext(context)
	}

	def dispatch HDLNativeFunction toHDL(PsNativeFunctionContext context) {
		var func = new HDLNativeFunction
		func = func.setName(context.psFunction.toName)
		func = func.setSimOnly(context.isSim !== null)
		func = func.setArgs(context.psFuncParam.psFuncSpec.map[toHDL as HDLFunctionParameter])
		if (context.psFuncRecturnType !== null)
			func = func.setReturnType(context.psFuncRecturnType.toHDL as HDLFunctionParameter)
		return func.attachContext(context)
	}

	def dispatch HDLFunctionParameter toHDL(PsFuncRecturnTypeContext context) {
		var res = context.psFuncParamType.toHDL as HDLFunctionParameter
		res = res.setRw(HDLFunctionParameter$RWType::RETURN)
		res = res.setDim(
			context.dims.map[
				if(it.psExpression !== null) it.psExpression.toHDL as HDLExpression else HDLFunctionParameter::EMPTY_ARR])
		return res
	}

	def dispatch HDLFunctionParameter toHDL(PsFuncSpecContext context) {
		var res = context.psFuncParamWithRW.toHDL as HDLFunctionParameter
		res = res.setName(new HDLVariable().setName(context.RULE_ID.text))
		res = res.setDim(
			context.dims.map[
				if(it.psExpression !== null) it.psExpression.toHDL as HDLExpression else HDLFunctionParameter::EMPTY_ARR])
		return res
	}

	def dispatch HDLFunctionParameter toHDL(PsFuncParamWithRWContext context) {
		var res = context.psFuncParamType.toHDL as HDLFunctionParameter
		if (context.psFuncParamRWType !== null)
			res = res.setRw(HDLFunctionParameter$RWType::getOp(context.psFuncParamRWType.text))
		else
			res = res.setRw(HDLFunctionParameter$RWType::READ)
		return res
	}

	def dispatch HDLFunctionParameter toHDL(PsFuncParamTypeContext context) {
		var res = new HDLFunctionParameter
		switch (x:context) {
			case x.ANY_INT !== null:
				res = res.setType(Type::ANY_INT)
			case x.ANY_UINT !== null:
				res = res.setType(Type::ANY_UINT)
			case x.ANY_BIT !== null:
				res = res.setType(Type::ANY_BIT)
			case x.INT !== null:
				res = res.setType(Type::REG_INT)
			case x.UINT !== null:
				res = res.setType(Type::REG_UINT)
			case x.BIT !== null:
				res = res.setType(Type::REG_BIT)
			case x.BOOL !== null:
				res = res.setType(Type::BOOL_TYPE)
			case x.STRING !== null:
				res = res.setType(Type::STRING_TYPE)
			case x.ANY_IF !== null:
				res = res.setType(Type::ANY_IF)
			case x.ANY_ENUM !== null:
				res = res.setType(Type::ANY_ENUM)
			case x.INTERFACE !== null: {
				res = res.setType(^Type::^IF)
				res = res.setIfSpec(x.psQualifiedName.toFQNName)
			}
			case x.ENUM !== null: {
				res = res.setType(^Type::ENUM)
				res = res.setEnumSpec(x.psQualifiedName.toFQNName)
			}
			case x.FUNCTION !== null: {
				res = res.setType(^Type::FUNCTION)
				res = res.setFuncSpec(x.psFuncParamWithRW.map[toHDL as HDLFunctionParameter])
				if (x.psFuncParamType !== null)
					res = res.setFuncReturnSpec(x.psFuncParamType.toHDL as HDLFunctionParameter)
			}
		}
		if (context.psWidth !== null)
			res = res.setContainer(context.psWidth.toHDL as HDLExpression)
		return res
	}

	def dispatch HDLInlineFunction toHDL(PsInlineFunctionContext context) {
		var func = new HDLInlineFunction
		func = func.setName(context.psFunction.toName)
		func = func.setExpr(context.psExpression.toHDL as HDLExpression)
		func = func.setArgs(context.psFuncParam.psFuncSpec.map[toHDL as HDLFunctionParameter])
		func = func.setReturnType(context.psFuncRecturnType.toHDL as HDLFunctionParameter)
		return func.attachContext(context)
	}

	def String toName(PsFunctionContext context) {
		return context.RULE_ID.text //NOATTACH PsFunctionContext toName
	}

	def dispatch IHDLObject toHDL(PsFunctionDeclarationContext context) {
		if (context.psInlineFunction !== null)
			return context.psInlineFunction.toHDL.attachContext(context)
		if (context.psNativeFunction !== null)
			return context.psNativeFunction.toHDL.attachContext(context)
		if (context.psSubstituteFunction !== null)
			return context.psSubstituteFunction.toHDL.attachContext(context)
		throw new IllegalArgumentException("Not implemented type:" + context.getClass());
	}

	def dispatch HDLUnresolvedFragment toHDL(PsRefPartContext context) {
		var HDLUnresolvedFragment frag;
		if (context.psFuncArgs() !== null) {
			var HDLUnresolvedFragmentFunction uff = new HDLUnresolvedFragmentFunction().setFrag(context.RULE_ID.text);
			frag = uff.setParams(context.psFuncArgs().psExpression.map[toHDL as HDLExpression])
		} else {
			frag = new HDLUnresolvedFragment().setFrag(context.RULE_ID.text);
			if (context.psArray !== null)
				frag = frag.setArray(context.psArray.psExpression.map[toHDL as HDLExpression])
			if (context.psBitAccess !== null)
				frag = frag.setBits(context.psBitAccess.psAccessRange.map[toHDL as HDLRange])
		}
		return frag.attachContext(context);
	}

	def dispatch HDLReference toHDL(PsVariableRefContext context) {
		if (context.isClk !== null)
			return HDLRegisterConfig::defaultClk(true).asHDLRef.attachContext(context)
		if (context.isRst !== null)
			return HDLRegisterConfig::defaultRst(true).asHDLRef.attachContext(context)
		var HDLUnresolvedFragment current = null
		for (sub : context.psRefPart.reverseView) {
			var frag = sub.toHDL as HDLUnresolvedFragment
			if (current !== null) {
				frag = frag.setSub(current)
			}
			current = frag
		}
		return current.attachContext(context)
	}

	def dispatch HDLRange toHDL(PsAccessRangeContext context) {

		//Because of the language Structure a simple {a}
		//becomes from=a to=null
		//Whereas a range {a,b}
		//becomes from=a to=b
		var res = new HDLRange().setTo(context.from.toHDL as HDLExpression)
		if (context.to !== null)
			res = res.setFrom(context.from.toHDL as HDLExpression).setTo(context.to.toHDL as HDLExpression)
		if (context.inc !== null)
			res = res.setTo(context.from.toHDL as HDLExpression).setInc(context.inc.toHDL as HDLExpression)
		if (context.dec !== null)
			res = res.setTo(context.from.toHDL as HDLExpression).setDec(context.dec.toHDL as HDLExpression)
		return res.attachContext(context)
	}

	def dispatch HDLSwitchCaseStatement toHDL(PsCaseStatementsContext context) {
		var hCase = new HDLSwitchCaseStatement
		if (context.psValue !== null)
			hCase = hCase.setLabel(context.psValue.toHDL as HDLExpression)
		hCase = hCase.setDos(context.psBlock.map[toHDL as HDLStatement])
		return hCase.attachContext(context)
	}

	def dispatch HDLSwitchStatement toHDL(PsSwitchStatementContext context) {
		var switchStmnt = new HDLSwitchStatement().setCaseExp(context.psVariableRef.toHDL as HDLExpression)
		switchStmnt = switchStmnt.setCases(context.psCaseStatements.map[toHDL as HDLSwitchCaseStatement])
		return switchStmnt.attachContext(context)
	}

	def dispatch HDLInterfaceInstantiation toHDL(PsInterfaceInstantiationContext context) {
		var hVar = context.psVariable.toHDL as HDLVariable
		if (context.psArray !== null)
			hVar = hVar.setDimensions(context.psArray.psExpression.map[toHDL as HDLExpression])
		var hii = new HDLInterfaceInstantiation().setVar(hVar).setHIf(context.psQualifiedName.toFQNName)
		if (context.psPassedArguments !== null)
			hii = hii.setArguments(context.psPassedArguments.psArgument.map[toHDL as HDLArgument])
		return hii.attachContext(context)
	}

	def dispatch HDLForLoop toHDL(PsForStatementContext context) {
		var loop = new HDLForLoop().setParam(context.psVariable.toHDL as HDLVariable)
		loop = loop.setRange(context.psBitAccess.psAccessRange.map[toHDL as HDLRange])
		loop = loop.setDos(context.psSimpleBlock.psBlock.map[toHDL as HDLStatement])
		return loop.attachContext(context)
	}

	def dispatch HDLIfStatement toHDL(PsIfStatementContext context) {
		var res = new HDLIfStatement().setIfExp(context.psExpression.toHDL as HDLExpression)
		res = res.setThenDo(context.ifBlk.psBlock.map[toHDL as HDLStatement])
		if (context.elseBlk !== null)
			res = res.setElseDo(context.elseBlk.psBlock.map[toHDL as HDLStatement])
		return res.attachContext(context)
	}

	def dispatch IHDLObject toHDL(PsCompoundStatementContext context) {
		if (context.psForStatement !== null)
			return context.psForStatement.toHDL.attachContext(context)
		if (context.psIfStatement !== null)
			return context.psIfStatement.toHDL.attachContext(context)
		if (context.psSwitchStatement !== null)
			return context.psSwitchStatement.toHDL.attachContext(context)
		throw new IllegalArgumentException("Unhandled type:" + context.getClass());
	}

	def dispatch IHDLObject toHDL(PsStatementContext context) {
		if (context.psAssignmentOrFunc !== null)
			return context.psAssignmentOrFunc.toHDL.attachContext(context)
		if (context.psCompoundStatement !== null)
			return context.psCompoundStatement.toHDL.attachContext(context)
		if (context.psProcess !== null)
			return context.psProcess.toHDL.attachContext(context)
		throw new IllegalArgumentException("Unhandled type:" + context.getClass());
	}

	def dispatch IHDLObject toHDL(PsAssignmentOrFuncContext context) {
		val hVar = context.psVariableRef.toHDL as HDLReference
		if (context.psAssignmentOp !== null) {
			val type = HDLAssignment$HDLAssignmentType::getOp(context.psAssignmentOp.text)
			var ass = new HDLAssignment().setLeft(hVar).setType(type)
			ass = ass.setRight(context.psExpression.toHDL as HDLExpression)
			return ass.attachContext(context)
		}
		return hVar.attachContext(context)
	}

	def dispatch IHDLObject toHDL(Object context) {
		throw new IllegalArgumentException("Unhandled type:" + context.getClass());
	}

	def HDLUnit toHDLUnit(PsUnitContext context, String libURI) {
		var unit = new HDLUnit().setName(context.psInterface.toName).setLibURI(libURI)
		unit = unit.setSimulation(context.unitType.type == PSHDLLangLexer::TESTBENCH);
		unit = unit.setAnnotations(context.psAnnotation.map[toHDL as HDLAnnotation])
		unit = unit.setImports(context.psImports.map[toName()])
		unit = unit.setStatements(context.psBlock.map[toHDL as HDLStatement])
		return unit.attachContext(context)
	}

	def String toName(PsImportsContext context) {
		return context.psQualifiedNameImport.text //NOATTACH PsImportsContext toName
	}

}

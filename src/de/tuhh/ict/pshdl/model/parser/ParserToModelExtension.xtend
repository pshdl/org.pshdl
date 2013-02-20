package de.tuhh.ict.pshdl.model.parser

import de.tuhh.ict.pshdl.model.*
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsModelContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsUnitContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsDeclarationContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsDeclarationTypeContext
import de.tuhh.ict.pshdl.model.utils.*
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsArrayInitContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsAnnotationContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsFunctionDeclarationContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsTypeDeclarationContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsVariableDeclarationContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsEnumDeclarationContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsInterfaceDeclarationContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsInterfaceContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsPortDeclarationContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsPrimitiveContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsQualifiedNameContext
import de.tuhh.ict.pshdl.model.HDLPrimitive$HDLPrimitiveType
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsWidthContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsExpressionContext
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration$HDLDirection
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsDeclAssignmentContext
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser$PsVariableContext
import org.antlr.v4.runtime.ParserRuleContext

class ParserToModelExtension {
	public static ParserToModelExtension INST=new ParserToModelExtension 
	
	def static HDLPackage toHDL(PsModelContext ctx, String libURI) {
		return INST.toHDLPkg(ctx, libURI)
	}
	
	def HDLPackage toHDLPkg(PsModelContext ctx, String libURI){
		var pkg=new HDLPackage().setLibURI(libURI)
		if (ctx.psQualifiedName!=null)
			pkg=pkg.setPkg(ctx.psQualifiedName.toName)
		for (unit:ctx.psUnit)
			pkg=pkg.addUnits(unit.toHDLUnit(libURI))
		for (decl:ctx.psDeclaration)
			pkg=pkg.addDeclarations(decl.toHDL as HDLDeclaration)
		pkg.freeze(null)
		HDLLibrary::getLibrary(libURI).addPkg(pkg);
		return pkg.attachContext(ctx) as HDLPackage
	}
	
	def dispatch IHDLObject toHDL(PsDeclarationContext context) { 
		var HDLDeclaration res=context.psDeclarationType.toHDL as HDLDeclaration;
		for (anno:context.psAnnotation)
			res=res.addAnnotations(anno.toHDL as HDLAnnotation)
		return res.attachContext(context)
	}
	
	def IHDLObject attachContext(IHDLObject obj, ParserRuleContext context) { 
		
	}

	
	def dispatch IHDLObject toHDL(PsAnnotationContext context) {
		val name=context.psAnnotationType.RULE_ID.text
		val value=context.RULE_STRING.text
		return new HDLAnnotation().setName(name).setValue(value).attachContext(context)
	}

	def dispatch IHDLObject toHDL(PsDeclarationTypeContext context) { 
		if (context.psFunctionDeclaration!=null)
			return context.psFunctionDeclaration.toHDL.attachContext(context)
		if (context.psTypeDeclaration!=null)
			return context.psTypeDeclaration.toHDL.attachContext(context)
		if (context.psVariableDeclaration!=null)
			return context.psVariableDeclaration.toHDL.attachContext(context)
		throw new IllegalArgumentException("Not implemented")
	}
	
	def dispatch IHDLObject toHDL(PsVariableDeclarationContext context) {
		var res=new HDLVariableDeclaration
		res=res.setType(context.psPrimitive.toHDL as HDLType)
		var HDLDirection dir=HDLDirection::INTERNAL
		if (context.psDirection!=null)
			dir=HDLDirection::getOp(context.psDirection.text)
		res=res.setDirection(dir)
		for (varDecl:context.psDeclAssignment)
			res=res.addVariables(varDecl.toHDL as HDLVariable)
		return res.attachContext(context)
	}

	def dispatch IHDLObject toHDL(PsArrayInitContext context) {
		if (context.psExpression!=null){
			if (context.psExpression.size==1)
				return context.psExpression.get(0).toHDL.attachContext(context)
			var HDLArrayInit arr=new HDLArrayInit
			for (exp : context.psExpression){
				arr=arr.addExp(exp.toHDL as HDLExpression)
			}
			return arr.attachContext(context)
		}
		var HDLArrayInit arr=new HDLArrayInit
		for (sub:context.psArrayInit)
			arr=arr.addExp(sub.toHDL as HDLExpression)
		return arr.attachContext(context)
	}
	
	def dispatch IHDLObject toHDL(PsPrimitiveContext context) {
		if (context.psQualifiedName!=null){
			return new HDLEnum().setName(context.psQualifiedName.toName).attachContext(context)
		}
		val HDLPrimitiveType pt = HDLPrimitiveType::valueOf(context.psPrimitiveType.text.toUpperCase)
		val HDLExpression width=context.psWidth?.toHDL as HDLExpression
		return new HDLPrimitive().setType(pt.getResultingType(width)).setWidth(width).attachContext(context)
	}
	
	def dispatch IHDLObject toHDL(PsDeclAssignmentContext context){
		var HDLVariable res=new HDLVariable().setName(context.psVariable.toName)
		for (anno:context.psAnnotation)
			res=res.addAnnotations(anno.toHDL as HDLAnnotation)
		if (context.psArray!=null)
			for (arr:context.psArray.psExpression)
				res=res.addDimensions(arr.toHDL as HDLExpression)
		if (context.psArrayInit!=null)
			res=res.setDefaultValue(context.psArrayInit.toHDL as HDLExpression)
		return res.attachContext(context)
	}
	
	def String toName(PsVariableContext context) { 
		return context.RULE_ID.text
	}

	
	def HDLPrimitiveType getResultingType(HDLPrimitiveType pt, HDLExpression width) {
		if (width != null) {
			switch (pt) {
			case HDLPrimitiveType::BIT:
				return HDLPrimitiveType::BITVECTOR
			}
		}
		switch (pt) {
		case HDLPrimitiveType::INT:
			return HDLPrimitiveType::INTEGER
		case HDLPrimitiveType::UINT:
			return HDLPrimitiveType::NATURAL
		}
		return pt;
	}
	
	def dispatch IHDLObject toHDL(PsWidthContext context) { 
		return context.psExpression.toHDL.attachContext(context)
	}
	
	def dispatch IHDLObject toHDL(PsExpressionContext context) { 
		
	}
	
	def String toName(PsInterfaceContext context) { 
		return context.psQualifiedName.toName
	}
	
	def String toName(PsQualifiedNameContext context) { 
		return new HDLQualifiedName(context.text).toString
	}

	def dispatch IHDLObject toHDL(PsTypeDeclarationContext context) { 
		if (context.psEnumDeclaration!=null)
			return context.psEnumDeclaration.toHDL.attachContext(context)
		if (context.psInterfaceDeclaration!=null)
			return context.psInterfaceDeclaration.toHDL.attachContext(context)
		throw new IllegalArgumentException("Not implemented")
	}
	
	def dispatch IHDLObject toHDL(PsInterfaceDeclarationContext context) {
		var hIf=new HDLInterface().setName(context.psInterface.toName)
		for (port:context.psInterfaceDecl.psPortDeclaration)
			hIf=hIf.addPorts(port.toHDL as HDLVariableDeclaration)
		return new HDLInterfaceDeclaration().setHIf(hIf).attachContext(context)
	}
	
	def dispatch IHDLObject toHDL(PsPortDeclarationContext context) { 
		var HDLVariableDeclaration res=context.psVariableDeclaration.toHDL as HDLVariableDeclaration;
		for (anno:context.psAnnotation)
			res=res.addAnnotations(anno.toHDL as HDLAnnotation)
		return res.attachContext(context)
	}
	
	def dispatch IHDLObject toHDL(PsEnumDeclarationContext context) {
		
	}


	
	def dispatch IHDLObject toHDL(PsFunctionDeclarationContext context) { 
		
	}

	def dispatch IHDLObject toHDL(Object c){
		throw new IllegalArgumentException("Unhandled type:"+c.getClass());
	}


	def HDLUnit toHDLUnit(PsUnitContext context, String string) { 
		
	}

}
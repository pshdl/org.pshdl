package de.tuhh.ict.pshdl.model.aspects;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;

public aspect StringWriterAspect {

	public String IHDLObject.toString(){
		if (StringWriterConfig.oops)
			throw new RuntimeException("Oops");
		return toString(SyntaxHighlighter.none());
	}
	public String IHDLObject.toString(SyntaxHighlighter highlight){
		throw new IllegalArgumentException("Ooops, forgot to implement that");
	}
	public String HDLExpression.toString(SyntaxHighlighter highlight){
		throw new IllegalArgumentException("Ooops, forgot to implement that");
	}
	public String HDLStatement.toString(SyntaxHighlighter highlight){
		throw new IllegalArgumentException("Ooops, forgot to implement that");
	}
	public String HDLAnnotation.toString(SyntaxHighlighter highlight){
		StringBuilder sb=new StringBuilder();
		sb.append(highlight.annotation(getName()));
		if (getValue()!=null)
			sb.append("(").append(highlight.string("\""+getValue()+"\"")).append(")");
		return sb.toString();
	}
	
	public String HDLTernary.toString(SyntaxHighlighter highlight) {
		return '('+getIfExpr().toString(highlight)+highlight.operator("?")+getThenExpr().toString(highlight)+highlight.operator(":")+getElseExpr().toString(highlight)+')';
	}
	
	public String HDLShiftOp.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(getLeft().toString(highlight));
		sb.append(highlight.operator(getType().toString()));
		sb.append(getRight().toString(highlight)).append(")");
		return sb.toString();
	}

	public String HDLEqualityOp.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(getLeft().toString(highlight));
		sb.append(highlight.operator(getType().toString()));
		sb.append(getRight().toString(highlight)).append(")");
		return sb.toString();
	}

	public String HDLArithOp.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(getLeft().toString(highlight));
		sb.append(highlight.operator(getType().toString()));
		sb.append(getRight().toString(highlight)).append(")");
		return sb.toString();
	}

	public String HDLBitOp.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(getLeft().toString(highlight));
		sb.append(highlight.operator(getType().toString()));
		sb.append(getRight().toString(highlight)).append(")");
		return sb.toString();
	}

	public String HDLConcat.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		String spacer = "";
		for (HDLExpression cat : getCats()) {
			sb.append(spacer).append(highlight.operator(cat.toString(highlight)));
			spacer = highlight.operator("#");
		}
		return sb.toString();
	}

	public String HDLFunctionCall.toString(SyntaxHighlighter highlight) {
		boolean isStatement=false;
		if (getContainer() instanceof HDLStatement)
			isStatement=true;
		if (getContainer() instanceof HDLBlock)
			isStatement=true;
		if (getContainer() instanceof HDLUnit)
			isStatement=true;
		StringBuilder sb;
		if (isStatement)
			sb=highlight.getSpacing();
		else
			sb= new StringBuilder();
		sb.append(highlight.functionCall(getNameRefName().toString())).append('(');
		boolean first = true;
		for (HDLExpression p : getParams()) {
			if (!first)
				sb.append(',');
			sb.append(p.toString(highlight));
			first=false;
		}
		sb.append(')');
		if (isStatement)
			sb.append(';');
		return sb.toString();
	}
	
	public String HDLNativeFunction.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		for (HDLAnnotation anno : getAnnotations()) {
			sb.append(anno.toString(highlight)).append(highlight.simpleSpace());
		}
		if (getSimOnly()!= null && getSimOnly())
			sb.append(highlight.keyword("simulation")).append(highlight.simpleSpace());
		sb.append(highlight.keyword("native"));
		sb.append(highlight.simpleSpace());
		sb.append(highlight.keyword("function"));
		sb.append(highlight.simpleSpace());
		sb.append(highlight.functionDecl(getName())).append(";").append(highlight.newLine());
		return sb.toString();
	}
	public String HDLInlineFunction.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		for (HDLAnnotation anno : getAnnotations()) {
			sb.append(anno.toString(highlight)).append(highlight.simpleSpace());
		}
		sb.append(highlight.keyword("inline")).append(highlight.simpleSpace()).append(highlight.keyword("function")).append(highlight.simpleSpace());
		sb.append(highlight.functionDecl(getName()));
		sb.append('(');
		for (Iterator<HDLVariable> iter = getArgs().iterator(); iter.hasNext();) {
			HDLVariable var = (HDLVariable) iter.next();
			sb.append(highlight.varName(var));
			if (iter.hasNext())
				sb.append(',');
		}
		sb.append(")").append(highlight.simpleSpace()).append("->").append(highlight.simpleSpace()).append("(").append(getExpr().toString(highlight)).append(")").append(highlight.newLine());
		return sb.toString();
	}
	public String HDLSubstituteFunction.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		for (HDLAnnotation anno : getAnnotations()) {
			sb.append(anno.toString(highlight)).append(highlight.simpleSpace());
		}
		sb.append(highlight.keyword("substitute")).append(highlight.simpleSpace()).append(highlight.keyword("function")).append(highlight.simpleSpace());
		sb.append(highlight.functionDecl(getName()));
		sb.append('(');
		for (Iterator<HDLVariable> iter = getArgs().iterator(); iter.hasNext();) {
			HDLVariable var = (HDLVariable) iter.next();
			sb.append(highlight.varName(var));
			if (iter.hasNext())
				sb.append(',');
		}
		sb.append(")").append(highlight.simpleSpace()).append("{").append(highlight.newLine());
		highlight.incSpacing();
		for (HDLStatement string : getStmnts()) {
			sb.append(string.toString(highlight)).append(highlight.newLine());
		}
		highlight.decSpacing();
		sb.append("}").append(highlight.newLine());
		return sb.toString();
	}

	public String HDLInterfaceRef.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		sb.append(highlight.interfaceRef(getHIfRefName().getLastSegment()));
		for (HDLExpression arr : getIfArray()) {
			sb.append('[').append(arr.toString(highlight)).append(']');
		}
		sb.append('.');
		sb.append(getVarRef(this, highlight));
		return sb.toString();
	}

	public String HDLVariableRef.toString(SyntaxHighlighter highlight) {
		return getVarRef(this, highlight).toString();
	}

	private static StringBuilder getVarRef(HDLVariableRef ref, SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		sb.append(highlight.variableRefName(ref));
		for (HDLExpression a : ref.getArray()) {
			sb.append('[').append(a.toString(highlight)).append(']');
		}
		if (ref.getBits().size() != 0) {
			sb.append('{');
			String spacer = "";
			for (HDLRange bit : ref.getBits()) {
				sb.append(spacer).append(bit.toString(highlight));
				spacer = ",";
			}
			sb.append('}');
		}
		return sb;
	}

	public String HDLLiteral.toString(SyntaxHighlighter highlight) {
		if (getStr())
			return highlight.literal('"'+getVal()+'"');
		return highlight.literal(getVal());
	}

	public String HDLManip.toString(SyntaxHighlighter highlight) {
		switch (getType()) {
		case ARITH_NEG:
			return highlight.operator("-") + getTarget().toString(highlight);
		case CAST:
			HDLPrimitive type = (HDLPrimitive) getCastTo();
			String width = type.getWidth() != null ? highlight.width("<" + type.getWidth().toString(highlight) + ">") : "";
			return "(" + highlight.keyword(type.getType().toString().toLowerCase()) + width + ")" + getTarget().toString(highlight);
		case BIT_NEG:
			return highlight.operator("~") + getTarget().toString(highlight);
		case LOGIC_NEG:
			return highlight.operator("!") + getTarget().toString(highlight);
		}
		throw new IllegalArgumentException("Unexpected Type:" + getType());
	}


	public String HDLBlock.toString(SyntaxHighlighter highlight) {
		StringBuilder sb=new StringBuilder(highlight.getSpacing());
		if (getProcess()!=null && getProcess()){
			sb.append("process").append("\n{");
		}
		highlight.incSpacing();
		for (HDLStatement string : getStatements()) {
			sb.append(string.toString(highlight)).append(highlight.newLine());
		}
		highlight.decSpacing();
		sb.append(highlight.getSpacing()).append("}");
		return sb.toString();
	}
	
	public String HDLAssignment.toString(SyntaxHighlighter highlight) {
		StringBuilder builder = highlight.getSpacing();
		builder.append(getLeft().toString(highlight));
		builder.append(highlight.operator(getType().toString()));
		builder.append(getRight().toString(highlight)).append(';');
		return builder.toString();
	}

	public String HDLPrimitive.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		sb.append(getType().toString().toLowerCase());
		if (getWidth() != null) {
			sb.append(highlight.width('<'+getWidth().toString(highlight)+'>'));
		}
		return sb.toString();
	}

	public String HDLForLoop.toString(SyntaxHighlighter highlight) {
		StringBuilder space = highlight.getSpacing();
		StringBuilder sb = new StringBuilder();
		sb.append(space).append(highlight.keyword("for")).append(highlight.simpleSpace()).append("(").append(getParam().getName()).append(highlight.simpleSpace()).append("=").append(highlight.simpleSpace()).append("{");
		String spacer = "";
		for (HDLRange range : getRange()) {
			sb.append(spacer).append(range.toString(highlight));
			spacer = ",";
		}
		sb.append("})").append(highlight.simpleSpace()).append("{").append(highlight.newLine());
		highlight.incSpacing();
		for (HDLStatement string : getDos()) {
			sb.append(string.toString(highlight)).append(highlight.newLine());
		}
		highlight.decSpacing();
		sb.append(space).append("}");
		return sb.toString();
	}

	public String HDLIfStatement.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = highlight.getSpacing();
		sb.append(highlight.keyword("if")).append(highlight.simpleSpace()).append('(').append(getIfExp().toString(highlight)).append(')').append(highlight.simpleSpace()).append('{').append(highlight.newLine());
		highlight.incSpacing();
		for (HDLStatement stmt : getThenDo()) {
			sb.append(stmt.toString(highlight)).append(highlight.newLine());
		}
		if (getElseDo().size() != 0) {
			sb.append(highlight.getSpacing()).append('}').append(highlight.simpleSpace()).append(highlight.keyword("else")).append(highlight.simpleSpace()).append('{').append(highlight.newLine());
			for (HDLStatement stmt : getElseDo()) {
				sb.append(stmt.toString(highlight)).append(highlight.newLine());
			}
		}
		highlight.decSpacing();
		sb.append(highlight.getSpacing()).append('}');
		return sb.toString();
	}

	public String HDLSwitchCaseStatement.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = highlight.getSpacing();
		if (getLabel() == null)
			sb.append(highlight.keyword("default")).append(highlight.simpleSpace()).append(':').append(highlight.simpleSpace()).append('{').append(highlight.newLine());
		else
			sb.append(highlight.keyword("case")).append(highlight.simpleSpace()).append(getLabel().toString(highlight)).append(':').append(highlight.simpleSpace()).append('{').append(highlight.newLine());
		highlight.incSpacing();
		for (HDLStatement stmnt : getDos()) {
			sb.append(stmnt.toString(highlight)).append(highlight.newLine());
		}
		highlight.decSpacing();
		sb.append(highlight.getSpacing()).append('}');
		return sb.toString();
	}

	public String HDLSwitchStatement.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = highlight.getSpacing();
		sb.append(highlight.keyword("switch")).append('(').append(getCaseExp().toString(highlight)).append(')').append(highlight.simpleSpace()).append('{').append(highlight.newLine());
		highlight.incSpacing();
		for (HDLStatement stmnt : getCases()) {
			sb.append(stmnt.toString(highlight)).append(highlight.newLine());
		}
		highlight.decSpacing();
		sb.append(highlight.getSpacing()).append('}');
		return sb.toString();
	}

	public String HDLVariableDeclaration.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = highlight.getSpacing();
		HDLType resolveType = resolveType();
		if (getAnnotations() != null) {
			for (HDLAnnotation hdla : getAnnotations()) {
				sb.append(hdla.toString(highlight)).append(highlight.simpleSpace());
			}
		}
		String dirString = getDirection().toString();
		if (dirString.length() > 0)
			sb.append(highlight.direction(dirString)).append(highlight.simpleSpace());
		if (getRegister() != null)
			sb.append(getRegister().toString(highlight));
		if (resolveType instanceof HDLEnum) {
			sb.append(highlight.keyword("enum")).append(highlight.simpleSpace()).append(resolveType.toString(highlight));
		} else
			sb.append(resolveType.toString(highlight));
		String spacing = highlight.simpleSpace();
		for (HDLVariable var : getVariables()) {
			sb.append(spacing);
			sb.append(var.toString(highlight));
			spacing = ",";
		}
		return sb.append(';').toString();
	}

	public String HDLInterfaceDeclaration.toString(SyntaxHighlighter highlight) {
		StringBuilder annos=highlight.getSpacing();
		for (HDLAnnotation anno : getAnnotations()) {
			annos.append(anno.toString(highlight)).append(highlight.simpleSpace());
		}
		return annos.append(getHIf().toString(highlight)).toString();
	}
	public String HDLInterface.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = highlight.getSpacing();
		sb.append(highlight.keyword("interface")).append(highlight.simpleSpace());
		sb.append(highlight.interfaceName(getName()));
		sb.append("{").append(highlight.newLine());
		highlight.incSpacing();
		for (HDLVariableDeclaration var : getPorts()) {
			sb.append(var.toString(highlight)).append(highlight.newLine());
		}
		highlight.decSpacing();
		sb.append("}").append(highlight.newLine());
		return sb.toString();
	}

	public String HDLEnumRef.toString(SyntaxHighlighter highlight) {
		//XXX Just using the last segment might not be correct as it might be non local
		return highlight.enumRefType(getHEnumRefName().getLastSegment()) + "." + highlight.enumRefVar(getVarRefName().getLastSegment());
	}

	public String HDLEnum.toString(SyntaxHighlighter highlight) {
		return highlight.enumName(getName());
	}

	public String HDLEnumDeclaration.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = highlight.getSpacing();
		for (HDLAnnotation anno : getAnnotations()) {
			sb.append(anno.toString(highlight)).append(highlight.simpleSpace());
		}
		sb.append(highlight.keyword("enum")).append(highlight.simpleSpace());
		sb.append(highlight.enumName(getHEnum().getName()));
		sb.append(highlight.simpleSpace()).append("=").append(highlight.simpleSpace()).append("{");
		String spacer = "";
		for (HDLVariable henum : getHEnum().getEnums()) {
			sb.append(spacer).append(henum.toString(highlight));
			spacer = ","+highlight.simpleSpace();
		}
		sb.append("}").append(highlight.newLine());
		return sb.toString();
	}

	public String HDLRegisterConfig.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		sb.append(highlight.keyword("register"));
		HDLRegisterConfig def = HDLRegisterConfig.defaultConfig();
		StringBuilder params = new StringBuilder();
		params.append('(');
		boolean first = true;
		if (!getClkRefName().equals(def.getClkRefName())) {
			params.append(highlight.param(HDLRegisterConfig.CLOCK_PARAM)).append('=').append(highlight.variableRefName(getClkRefName()));
			first = false;
		}
		if (!getRstRefName().equals(def.getRstRefName())) {
			if (!first)
				params.append(", ");
			params.append(highlight.param(HDLRegisterConfig.RESET_PARAM)).append('=').append(highlight.variableRefName(getRstRefName()));
			first = false;
		}
		if (getClockType()!=null && !getClockType().equals(def.getClockType())) {
			if (!first)
				params.append(", ");
			params.append(highlight.param(HDLRegisterConfig.EDGE_PARAM)).append('=').append(highlight.enumRefType("Edge")).append('.').append(highlight.enumRefVar(getClockType().toString()));
			first = false;
		}
		if (getSyncType()!=null && !getSyncType().equals(def.getSyncType())) {
			if (!first)
				params.append(", ");
			params.append(highlight.param(HDLRegisterConfig.RESET_SYNC_PARAM)).append('=').append(highlight.enumRefType("Sync")).append('.').append(highlight.enumRefVar(getSyncType().toString()));
			first = false;
		}
		if (getResetType()!=null && !getResetType().equals(def.getResetType())) {
			if (!first)
				params.append(", ");
			params.append(highlight.param(HDLRegisterConfig.RESET_TYPE_PARAM)).append('=').append(highlight.enumRefType("Active")).append('.').append(highlight.enumRefVar(getResetType().toString()));
			first = false;
		}
		if (!getResetValue().equals(def.getResetValue())) {
			if (!first)
				params.append(", ");
			params.append(highlight.param(HDLRegisterConfig.RESET_VALUE_PARAM)).append('=').append(getResetValue().toString(highlight));
			first = false;
		}
		params.append(')');
		if (!first)
			sb.append(params);
		sb.append(highlight.simpleSpace());
		return sb.toString();
	}

	public String HDLPackage.toString(SyntaxHighlighter highlight){
		StringBuilder sb=new StringBuilder();
		if (getPkg()!=null)
			sb.append(highlight.keyword("package")).append(highlight.simpleSpace()).append(highlight.packageName(getPkg())).append(";").append(highlight.newLine());
		for (HDLDeclaration decl : getDeclarations()) {
			sb.append(decl.toString(highlight));
		}
		for (HDLUnit unit : getUnits()) {
			sb.append(unit.toString(highlight));
		}
		return sb.toString();
	}
	
	public String HDLUnit.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		if (!getSimulation())
			sb.append(highlight.keyword("module")).append(highlight.simpleSpace());
		else
			sb.append(highlight.keyword("testbench")).append(highlight.simpleSpace());
		sb.append(highlight.unitName(getName())).append("{").append(highlight.newLine());
		highlight.incSpacing();
		for (String imports : getImports()) {
			sb.append(highlight.getSpacing()).append(highlight.keyword("import")).append(highlight.simpleSpace()).append(highlight.importName(imports)).append(";").append(highlight.newLine());
		}
		for (HDLStatement stmnt : getStatements()) {
			sb.append(stmnt.toString(highlight)).append(highlight.newLine());
		}
		highlight.decSpacing();
		sb.append("}").append(highlight.newLine());
		return sb.toString();

	}

	public String HDLInterfaceInstantiation.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = highlight.getSpacing();
		sb.append(highlight.interfaceName(getHIfRefName().toString())).append(highlight.simpleSpace()).append(getVar().toString(highlight));
		if (getArguments().size()!=0){
			boolean first=true;
			sb.append('(');
			for (HDLArgument arg : getArguments()) {
				if (!first)
					sb.append(',');
				first=false;
				sb.append(arg.toString(highlight));
			}
			sb.append(')');
		}
		sb.append(';');
		return sb.toString();
	}
	
	

	public String HDLArgument.toString(SyntaxHighlighter highlight) {
		StringBuilder sb=new StringBuilder();
		sb.append(highlight.param(getName())).append('=').append(getExpression().toString(highlight));
		return sb.toString();
	}
	public String HDLRange.toString(SyntaxHighlighter highlight) {
		if (getFrom() != null) {
			return getFrom().toString(highlight) + ":" + getTo().toString(highlight);
		}
		return getTo().toString(highlight);
	}

	public String HDLVariable.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = new StringBuilder();
		for (HDLAnnotation anno : getAnnotations()) {
			sb.append(anno.toString(highlight)).append(highlight.simpleSpace());
		}
		sb.append(highlight.varName(this));
		for (HDLExpression arr : getDimensions()) {
			sb.append('[').append(arr.toString(highlight)).append(']');
		}
		if (getDefaultValue() != null)
			sb.append('=').append(getDefaultValue().toString(highlight));
		return sb.toString();
	}

	public String HDLDirectGeneration.toString(SyntaxHighlighter highlight) {
		StringBuilder sb = highlight.getSpacing();
		sb.append(highlight.interfaceName(getHIf().getName())).append(highlight.simpleSpace()).append(highlight.varName(getVar())).append("=").append(highlight.generatorID(getGeneratorID()));
		sb.append('(');
		for (HDLArgument args : getArguments()) {
			sb.append(args.toString(highlight));

		}
		sb.append(')');
		if (getGeneratorContent() != null) {
			sb.append("<[").append(highlight.generatorContent(getGeneratorID(), getGeneratorContent())).append("]>");
		}
		sb.append(";");
		return sb.toString();
	}
}

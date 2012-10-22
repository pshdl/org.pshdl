package de.tuhh.ict.pshdl.model.aspects;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public aspect StringWriterAspect {
	public String HDLAnnotation.toString(){
		StringBuilder sb=new StringBuilder();
		sb.append(getName());
		if (getValue()!=null)
			sb.append("(\"").append(getValue()).append("\")");
		return sb.toString();
	}
	
	public String HDLTernary.toString() {
		return "("+getIfExpr().toString()+"?"+getThenExpr().toString()+":"+getElseExpr().toString()+")";
	}
	
	public String HDLShiftOp.toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(getLeft());
		sb.append(getType());
		sb.append(getRight()).append(")");
		return sb.toString();
	}

	public String HDLEqualityOp.toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(getLeft());
		sb.append(getType());
		sb.append(getRight()).append(")");
		return sb.toString();
	}

	public String HDLArithOp.toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(getLeft());
		sb.append(getType());
		sb.append(getRight()).append(")");
		return sb.toString();
	}

	public String HDLBitOp.toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(getLeft());
		sb.append(getType());
		sb.append(getRight()).append(")");
		return sb.toString();
	}

	public String HDLConcat.toString() {
		StringBuilder sb = new StringBuilder();
		String spacer = "";
		for (HDLExpression cat : getCats()) {
			sb.append(spacer).append(cat);
			spacer = "#";
		}
		return sb.toString();
	}

	public String HDLFunctionCall.toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getNameRefName()).append('(');
		boolean first = true;
		for (HDLExpression p : getParams()) {
			if (!first)
				sb.append(',');
			sb.append(p);
			first=false;
		}
		sb.append(')');
		return sb.toString();
	}
	
	public String HDLNativeFunction.toString() {
		StringBuilder sb = new StringBuilder();
		if (getSimOnly()!= null && getSimOnly())
			sb.append("simulation ");
		sb.append("native ");
		sb.append(getName()).append(";\n");
		return sb.toString();
	}

	public String HDLInterfaceRef.toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getHIfRefName().getLastSegment());
		for (HDLExpression arr : getIfArray()) {
			sb.append('[').append(arr).append(']');
		}
		sb.append('.');
		sb.append(getVarRef(this));
		return sb.toString();
	}

	public String HDLVariableRef.toString() {
		return getVarRef(this).toString();
	}

	private static StringBuilder getVarRef(HDLVariableRef ref) {
		StringBuilder sb = new StringBuilder();
		sb.append(ref.getVarRefName().getLastSegment());
		for (HDLExpression a : ref.getArray()) {
			sb.append('[').append(a).append(']');
		}
		if (ref.getBits().size() != 0) {
			sb.append('{');
			String spacer = "";
			for (HDLRange bit : ref.getBits()) {
				sb.append(spacer).append(bit);
				spacer = ",";
			}
			sb.append('}');
		}
		return sb;
	}

	public String HDLLiteral.toString() {
		return getVal();
	}

	public String HDLManip.toString() {
		switch (getType()) {
		case ARITH_NEG:
			return "-" + getTarget();
		case CAST:
			HDLPrimitive type = (HDLPrimitive) getCastTo();
			String width = type.getWidth() != null ? "<" + type.getWidth() + ">" : "";
			return "(" + type.getType().toString().toLowerCase() + width + ")" + getTarget();
		case BIT_NEG:
			return "~" + getTarget();
		case LOGIC_NEG:
			return "!" + getTarget();
		}
		throw new IllegalArgumentException("Unexpected Type:" + getType());
	}

	private static ThreadLocal<Integer> spacing = new ThreadLocal<Integer>();

	private static StringBuilder getSpacing() {
		Integer j = spacing.get();
		if (j == null)
			j = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = j; i > 0; i--) {
			sb.append('\t');
		}
		return sb;
	}

	private static void incSpacing() {
		Integer current = spacing.get();
		if (current == null) {
			current = 0;
		}
		spacing.set(current + 1);
	}

	private static void decSpacing() {
		Integer current = spacing.get();
		spacing.set(current - 1);
	}

	public String HDLBlock.toString() {
		StringBuilder sb=new StringBuilder(getSpacing());
		if (getProcess()!=null && getProcess()){
			sb.append("process {");
		}
		incSpacing();
		for (HDLStatement string : getStatements()) {
			sb.append(string).append('\n');
		}
		decSpacing();
		sb.append(getSpacing()).append("}");
		return sb.toString();
	}
	
	public String HDLAssignment.toString() {
		StringBuilder builder = getSpacing();
		builder.append(getLeft());
		builder.append(getType());
		builder.append(getRight()).append(';');
		return builder.toString();
	}

	public String HDLPrimitive.toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getType().toString().toLowerCase());
		if (getWidth() != null) {
			sb.append('<').append(getWidth()).append('>');
		}
		return sb.toString();
	}

	public String HDLForLoop.toString() {
		StringBuilder space = getSpacing();
		StringBuilder sb = new StringBuilder();
		sb.append(space).append("for (").append(getParam().getName()).append(" = {");
		String spacer = "";
		for (HDLRange range : getRange()) {
			sb.append(spacer).append(range);
			spacer = ",";
		}
		sb.append("}) {\n");
		incSpacing();
		for (HDLStatement string : getDos()) {
			sb.append(string).append('\n');
		}
		decSpacing();
		sb.append(space).append("}");
		return sb.toString();
	}

	public String HDLIfStatement.toString() {
		Formatter f = new Formatter();
		StringBuilder sb = getSpacing();
		f.format("%sif (%s) {\n", sb, getIfExp());
		incSpacing();
		for (HDLStatement stmt : getThenDo()) {
			f.format("%s\n", stmt);
		}
		if (getElseDo().size() != 0) {
			f.format("%s} else {\n", sb);
			for (HDLStatement stmt : getElseDo()) {
				f.format("%s\n", stmt);
			}
		}
		decSpacing();
		f.format("%s}", sb);
		return f.toString();
	}

	public String HDLSwitchCaseStatement.toString() {
		StringBuilder sb = getSpacing();
		Formatter f = new Formatter();
		if (getLabel() == null)
			f.format("%sdefault : {\n", sb);
		else
			f.format("%scase %s: {\n", sb, getLabel());
		incSpacing();
		for (HDLStatement stmnt : getDos()) {
			f.format("%s\n", stmnt);
		}
		decSpacing();
		f.format("%s}", sb);
		return f.toString();
	}

	public String HDLSwitchStatement.toString() {
		StringBuilder sb = getSpacing();
		Formatter f = new Formatter();
		f.format("%sswitch(%s) {\n", sb, getCaseExp());
		incSpacing();
		for (HDLStatement cs : getCases()) {
			f.format("%s\n", cs);
		}
		decSpacing();
		f.format("%s}", sb);
		return f.toString();
	}

	public String HDLVariableDeclaration.toString() {
		StringBuilder sb = getSpacing();
		HDLType resolveType = resolveType();
		if (getAnnotations() != null) {
			for (HDLAnnotation hdla : getAnnotations()) {
				sb.append(hdla).append(' ');
			}
		}
		String dirString = getDirection().toString();
		if (dirString.length() > 0)
			sb.append(dirString).append(' ');
		if (getRegister() != null)
			sb.append(getRegister());
		if (resolveType instanceof HDLEnum) {
			sb.append("enum ").append(resolveType);
		} else
			sb.append(resolveType);
		String spacing = " ";
		for (HDLVariable var : getVariables()) {
			sb.append(spacing);
			sb.append(var);
			spacing = ",";
		}
		return sb.append(';').toString();
	}

	public String HDLInterfaceDeclaration.toString() {
		return getHIf().toString();
	}
	public String HDLInterface.toString() {
		StringBuilder sb = getSpacing();
		sb.append("interface ");
		sb.append(getName());
		sb.append("{\n");
		incSpacing();
		for (HDLVariableDeclaration var : getPorts()) {
			sb.append(var).append('\n');
		}
		decSpacing();
		sb.append("}\n");
		return sb.toString();
	}

	public String HDLEnumRef.toString() {
		return getHEnumRefName().getLastSegment() + "." + getVarRefName();
	}

	public String HDLEnum.toString() {
		return getName();
	}

	public String HDLEnumDeclaration.toString() {
		StringBuilder sb = getSpacing();
		sb.append("enum ");
		sb.append(getHEnum().getName());
		sb.append(" = {");
		String spacer = "";
		for (HDLVariable henum : getHEnum().getEnums()) {
			sb.append(spacer).append(henum);
			spacer = ", ";
		}
		sb.append("}\n");
		return sb.toString();
	}

	public String HDLRegisterConfig.toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("register");
		HDLRegisterConfig def = HDLRegisterConfig.defaultConfig();
		StringBuilder params = new StringBuilder();
		params.append('(');
		boolean first = true;
		if (!getClkRefName().equals(def.getClkRefName())) {
			params.append(HDLRegisterConfig.CLOCK_PARAM).append('=').append(getClkRefName());
			first = false;
		}
		if (!getRstRefName().equals(def.getRstRefName())) {
			if (!first)
				params.append(", ");
			params.append(HDLRegisterConfig.RESET_PARAM).append('=').append(getRstRefName());
			first = false;
		}
		if (getClockType()!=null && !getClockType().equals(def.getClockType())) {
			if (!first)
				params.append(", ");
			params.append(HDLRegisterConfig.EDGE_PARAM).append('=').append(getClockType());
			first = false;
		}
		if (getSyncType()!=null && !getSyncType().equals(def.getSyncType())) {
			if (!first)
				params.append(", ");
			params.append(HDLRegisterConfig.RESET_SYNC_PARAM).append('=').append(getSyncType());
			first = false;
		}
		if (getResetType()!=null && !getResetType().equals(def.getResetType())) {
			if (!first)
				params.append(", ");
			params.append(HDLRegisterConfig.RESET_TYPE_PARAM).append('=').append(getResetType());
			first = false;
		}
		if (!getResetValue().equals(def.getResetValue())) {
			if (!first)
				params.append(", ");
			params.append(HDLRegisterConfig.RESET_VALUE_PARAM).append('=').append(getResetValue());
			first = false;
		}
		params.append(')');
		if (!first)
			sb.append(params);
		sb.append(' ');
		return sb.toString();
	}

	public String HDLPackage.toString(){
		StringBuilder sb=new StringBuilder();
		if (getPkg()!=null)
		sb.append("package ").append(getPkg()).append(";\n");
		for (HDLDeclaration decl : getDeclarations()) {
			sb.append(decl);
		}
		for (HDLUnit unit : getUnits()) {
			sb.append(unit);
		}
		return sb.toString();
	}
	
	public String HDLUnit.toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("module ").append(getName()).append("{\n");
		incSpacing();
		for (String imports : getImports()) {
			sb.append(getSpacing()).append("import ").append(imports).append(";\n");
		}
		for (HDLStatement stmnt : getStatements()) {
			sb.append(stmnt).append('\n');
		}
		decSpacing();
		sb.append("}\n");
		return sb.toString();

	}

	public String HDLInterfaceInstantiation.toString() {
		StringBuilder sb = getSpacing();
		sb.append(getHIfRefName()).append(' ').append(getVar().getName());
		sb.append(';');
		return sb.toString();
	}

	public String HDLRange.toString() {
		if (getFrom() != null) {
			return getFrom() + ":" + getTo();
		}
		return getTo().toString();
	}

	public String HDLVariable.toString() {
		StringBuilder sb = new StringBuilder();
		// sb.append(qfn);
		for (HDLAnnotation anno : getAnnotations()) {
			sb.append(anno.toString()).append(' ');
		}
		sb.append(getName());
		for (HDLExpression arr : getDimensions()) {
			sb.append('[').append(arr).append(']');
		}
		if (getDefaultValue() != null)
			sb.append('=').append(getDefaultValue());
		return sb.toString();
	}

	public String HDLDirectGeneration.toString() {
		StringBuilder sb = getSpacing();
		sb.append(getHIf().getName()).append(' ').append(getVar().getName()).append("=").append(getGeneratorID());
		sb.append('(');
		for (HDLArgument args : getArguments()) {
			sb.append(args.getName()).append('=');
			if (args.getValue() != null) {
				sb.append('"').append(args.getValue()).append('"');
			} else
				sb.append(args.getExpression());

		}
		sb.append(')');
		if (getGeneratorContent() != null) {
			sb.append("<[").append(getGeneratorContent()).append("]>");
		}
		sb.append(";");
		return sb.toString();
	}
}

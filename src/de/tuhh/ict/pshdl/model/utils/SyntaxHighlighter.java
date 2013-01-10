package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public class SyntaxHighlighter {

	public static enum Context {
		HDLPackage, HDLUnit, HDLStatement, HDLExpression, HDLInterface;
	}

	private Stack<Context> context = new Stack<SyntaxHighlighter.Context>();

	private int spacing = 0;

	public StringBuilder getSpacing() {
		StringBuilder sb = new StringBuilder();
		for (int i = spacing; i > 0; i--) {
			sb.append('\t');
		}
		return sb;
	}

	public void incSpacing() {
		spacing++;
	}

	public void decSpacing() {
		spacing--;
	}

	public static final SyntaxHighlighter none() {
		return new SyntaxHighlighter();
	}

	public String annotation(String name) {
		return name;
	}

	public String string(String string) {
		return string;
	}

	public String operator(String op) {
		return op;
	}

	public String functionCall(String nameRefName) {
		return nameRefName;
	}

	public String keyword(String key) {
		return key;
	}

	public String simpleSpace() {
		return " ";
	}

	public String functionDecl(String name) {
		return name;
	}

	public String varName(HDLVariable var) {
		return var.getName();
	}

	public String interfaceRef(String ifRef) {
		return ifRef;
	}

	public String variableRefName(HDLVariableRef ref) {
		return ref.getVarRefName().getLastSegment();
	}

	public String literal(String literal) {
		return literal;
	}

	public String width(String width) {
		return width;
	}

	public String newLine() {
		return "\n";
	}

	public String direction(String dir) {
		return dir;
	}

	public String interfaceName(String name) {
		return name;
	}

	public String enumRefType(String type) {
		return type;
	}

	public String enumRefVar(String varRefName) {
		return varRefName;
	}

	public String enumName(String name) {
		return name;
	}

	public String variableRefName(HDLQualifiedName refName) {
		return refName.getLastSegment();
	}

	public String param(String param) {
		return param;
	}

	public String packageName(String pkg) {
		return pkg;
	}

	public String unitName(String name) {
		return name;
	}

	public String importName(String imports) {
		return imports;
	}

	public String generatorID(String generatorID) {
		return generatorID;
	}

	public String generatorContent(String generatorID, String generatorContent) {
		return generatorContent;
	}

	public void pushContext(Context context) {
		this.context.push(context);
	}

	public Context getContext() {
		if (context.isEmpty())
			return null;
		return context.peek();
	}

	public Context popContext() {
		return context.pop();
	}
}
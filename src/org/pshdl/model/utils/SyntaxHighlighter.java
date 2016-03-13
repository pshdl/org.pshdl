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
package org.pshdl.model.utils;

import java.util.Stack;

import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.parser.SourceInfo;

public class SyntaxHighlighter {

	public static class HTMLHighlighter extends SyntaxHighlighter {
		private final boolean preContext;

		public HTMLHighlighter(boolean preContext) {
			this.preContext = preContext;
		}

		@Override
		public StringBuilder getSpacing() {
			final StringBuilder sb = new StringBuilder();
			for (int i = spacing; i > 0; i--) {
				sb.append("&#160;&#160;&#160;");
			}
			return sb;
		}

		@Override
		public String newLine() {
			if (preContext)
				return "\n";
			return "<br />\n";
		}

		@Override
		public String simpleSpace() {
			return "&#160;";
		}

		@Override
		public String literal(String literal) {
			return span(super.literal(literal), "literal");
		}

		@Override
		public String keyword(String key) {
			return span(super.keyword(key), "keyword");
		}

		@Override
		public String width(String width) {
			return "&lt;" + width.substring(1, width.length() - 1) + "&gt;";
		}

		@Override
		public String generatorContent(String generatorID, String generatorContent) {
			return "&lt;" + generatorContent.substring(1, generatorContent.length() - 1) + "&gt;";
		}

		private String span(String key, String clazz) {
			return "<span class='" + clazz + "'>" + key + "</span>";
		}

		@Override
		public String annotation(String name) {
			return span(super.annotation(name), "annotation");
		}
	}

	private final boolean includeComments;

	public SyntaxHighlighter() {
		this(false);
	}

	public SyntaxHighlighter(boolean includeComments) {
		this.includeComments = includeComments;
	}

	public static enum Context {
		HDLPackage, HDLUnit, HDLStatement, HDLExpression, HDLInterface;
	}

	private final Stack<Context> context = new Stack<SyntaxHighlighter.Context>();

	protected int spacing = 0;

	public StringBuilder getSpacing() {
		final StringBuilder sb = new StringBuilder();
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

	public static final SyntaxHighlighter none(boolean includeComments) {
		return new SyntaxHighlighter(includeComments);
	}

	public static final SyntaxHighlighter none() {
		return new SyntaxHighlighter();
	}

	public String leaving(IHDLObject obj) {
		return "";
	}

	public String entering(IHDLObject obj) {
		if (includeComments) {
			final SourceInfo meta = obj.getMeta(SourceInfo.INFO);
			if ((meta != null) && !meta.comments.isEmpty()) {
				final StringBuilder sb = new StringBuilder();
				for (final String comment : meta.comments) {
					sb.append(comment);
				}
				return sb.toString();
			}
		}
		return "";
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
		return keyword(dir);
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
		return refName.toString();
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

	public String primitiveType(String prim) {
		return keyword(prim);
	}

	public String comment(String string) {
		if (string.contains("\n"))
			return "/*" + newLine() + string + newLine() + "*/";
		return "//" + string;
	}

	public String exportMatch(String string) {
		return string;
	}
}

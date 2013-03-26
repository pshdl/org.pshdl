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
package de.tuhh.ict.pshdl.model.utils;

public class HTMLHighlighter extends SyntaxHighlighter {
	private boolean preContext;

	public HTMLHighlighter(boolean preContext) {
		this.preContext = preContext;
	}

	@Override
	public StringBuilder getSpacing() {
		StringBuilder sb = new StringBuilder();
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

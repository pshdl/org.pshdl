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
package de.tuhh.ict.pshdl.model.parser;

import java.util.*;

import org.antlr.v4.runtime.*;

import de.tuhh.ict.pshdl.model.HDLObject.GenericMeta;

public class SourceInfo {

	public static final GenericMeta<SourceInfo> INFO = new GenericMeta<SourceInfo>("SourceInfo", true);

	public final ParserRuleContext context;
	public final int startLine, endLine;
	public final int startPosInLine, endPosInLine;
	public final int length;
	public final List<String> comments = new LinkedList<String>();

	public final int totalOffset;

	public SourceInfo(BufferedTokenStream tokens, ParserRuleContext context) {
		this.context = context;
		this.startLine = context.start.getLine();
		this.totalOffset = context.start.getStartIndex();
		this.startPosInLine = context.start.getCharPositionInLine();
		if (context.stop != null) {
			this.endLine = context.stop.getLine();
			this.endPosInLine = context.stop.getCharPositionInLine();
		} else {
			this.endLine = startLine;
			this.endPosInLine = startPosInLine;
		}
		this.length = tokens.getText(context.getSourceInterval()).length();
		if (tokens != null) {
			List<Token> hidden = tokens.getHiddenTokensToLeft(context.start.getTokenIndex(), PSHDLLangLexer.COMMENTS);
			if (hidden != null) {
				for (Token token : hidden) {
					comments.add(token.getText());
				}
			}
		}
	}

	@Override
	public String toString() {
		return "SourceInfo [context=" + context + ", startLine=" + startLine + ", endLine=" + endLine + ", startPosInLine=" + startPosInLine + ", endPosInLine=" + endPosInLine
				+ ", length=" + length + "]";
	}

}

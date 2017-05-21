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
package org.pshdl.model.parser;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.pshdl.model.HDLObject.GenericMeta;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.MetaAccess;

import com.google.common.collect.Lists;

public class SourceInfo {

	public static final MetaAccess<SourceInfo> INFO = new GenericMeta<>("SourceInfo", true);
	public static final MetaAccess<List<String>> COMMENT = new GenericMeta<>("Comment", true);

	public final ParserRuleContext context;
	public final int startLine, endLine;
	public final int startPosInLine, endPosInLine;
	public final int length;
	public final List<String> comments = new LinkedList<>();

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
		if (tokens != null) {
			final String tokenText = tokens.getText(context.getSourceInterval());
			this.length = tokenText.length();
			final List<Token> hidden = tokens.getHiddenTokensToLeft(context.start.getTokenIndex(), PSHDLLangLexer.COMMENTS);
			if (hidden != null) {
				for (final Token token : hidden) {
					final String text = token.getText();
					if (!text.startsWith("///<") && !text.startsWith("/**<")) {
						comments.add(text);
					}
				}
			}
			if (context.stop != null) {
				final List<Token> hiddenRight = tokens.getHiddenTokensToRight(context.stop.getTokenIndex(), PSHDLLangLexer.COMMENTS);
				if (hiddenRight != null) {
					for (final Token token : hiddenRight) {
						final String text = token.getText();
						if (text.startsWith("///<") || text.startsWith("/**<")) {
							comments.add(text);
						}
					}
				}
			}
		} else {
			this.length = -1;
		}
	}

	public static void addComment(IHDLObject vInterface, String comment) {
		List<String> comments = vInterface.getMeta(SourceInfo.COMMENT);
		if (comments == null) {
			comments = Lists.newArrayList();
			vInterface.addMeta(SourceInfo.COMMENT, comments);
		}
		comments.add(comment);
	}

	@Override
	public String toString() {
		return "SourceInfo [context=" + context + ", startLine=" + startLine + ", endLine=" + endLine + ", startPosInLine=" + startPosInLine + ", endPosInLine=" + endPosInLine
				+ ", length=" + length + "]";
	}

}

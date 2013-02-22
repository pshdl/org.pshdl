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

	public SourceInfo(BufferedTokenStream tokens, ParserRuleContext context) {
		this.context = context;
		this.startLine = context.start.getLine();
		this.startPosInLine = context.start.getCharPositionInLine();
		this.endLine = context.stop.getLine();
		this.endPosInLine = context.stop.getCharPositionInLine();
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

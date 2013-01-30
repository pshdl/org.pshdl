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

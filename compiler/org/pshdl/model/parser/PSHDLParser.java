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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.parser.PSHDLLangParser.PsExpressionContext;
import org.pshdl.model.parser.PSHDLLangParser.PsModelContext;
import org.pshdl.model.utils.HDLLibrary;
import org.pshdl.model.utils.services.IHDLValidator.IErrorCode;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.Problem.ProblemSeverity;

public class PSHDLParser {

	public static String[] getKeywords() throws Exception {
		final InputStream resourceAsStream = PSHDLParser.class.getResourceAsStream("PSHDLLangLexer.tokens");
		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8))) {
			String line = null;
			final List<String> keywords = new ArrayList<String>();
			while ((line = reader.readLine()) != null)
				if (line.charAt(0) == '\'') {
					final String keyWord = line.substring(1, line.lastIndexOf('\''));
					if (keyWord.matches("[a-z]+")) {
						keywords.add(keyWord);
					}
				}
			return keywords.toArray(new String[keywords.size()]);
		}
	}

	public static HDLPackage parse(File file, String libURI, Set<Problem> syntaxProblems) throws IOException, FileNotFoundException {
		try (final FileInputStream fis = new FileInputStream(file)) {
			final HDLPackage hdl = parseStream(new ANTLRInputStream(fis), libURI, syntaxProblems, file.getAbsolutePath());
			return hdl;
		}
	}

	public static final class SyntaxErrorCollector extends BaseErrorListener {
		private final Set<Problem> syntaxProblems;
		private final CommonTokenStream ts;
		private int lineOffset = 0;

		public SyntaxErrorCollector(CommonTokenStream ts, Set<Problem> syntaxProblems) {
			this.syntaxProblems = syntaxProblems;
			this.ts = ts;
		}

		public SyntaxErrorCollector(CommonTokenStream tokens, Set<Problem> problems, int lineOffset) {
			this(tokens, problems);
			this.lineOffset = lineOffset;
		}

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
			int length = -1;
			int totalOffset = -1;
			SyntaxErrors error = SyntaxErrors.OtherException;
			if ((e == null) && PSHDLLangParser.MISSING_SEMI.equals(msg)) {
				error = SyntaxErrors.MissingSemicolon;
				msg = "Missing ';'";
			}
			if ((e == null) && PSHDLLangParser.WRONG_ORDER.equals(msg)) {
				error = SyntaxErrors.WrongOrder;
				msg = "The order for variable declarations is «direction?» «register?» «type» «name»";
			}
			if ((e == null) && PSHDLLangParser.MISSING_WIDTH.equals(msg)) {
				error = SyntaxErrors.MissingWidth;
				msg = "The empty width <> is only allowed in function declarations";
			}
			if ((e == null) && PSHDLLangParser.MISSING_TYPE.equals(msg)) {
				error = SyntaxErrors.MissingType;
				msg = "The variable declaration is missing its type";
			}
			if ((e == null) && PSHDLLangParser.MISSING_IFPAREN.equals(msg)) {
				error = SyntaxErrors.MissingIfParen;
				msg = "The syntax for if-statements is 'if («expression») { «thenStatements*» } else { «elseStatements*» }";
			}
			if (offendingSymbol instanceof Token) {
				Token t = (Token) offendingSymbol;
				if (error == SyntaxErrors.MissingSemicolon) {
					do {
						t = ts.get(t.getTokenIndex() - 1);
					} while (t.getChannel() != 0);
					line = t.getLine();
					charPositionInLine = t.getCharPositionInLine();
				}
				totalOffset = t.getStartIndex();
				final String text = t.getText();
				if (text != null) {
					length = text.length();
				}
			}
			if (e instanceof NoViableAltException) {
				final NoViableAltException noVi = (NoViableAltException) e;
				error = SyntaxErrors.NoViableAlternative;
				final Token t = noVi.getStartToken();
				charPositionInLine = t.getCharPositionInLine();
				line = t.getLine();
				totalOffset = t.getStartIndex();
				final String text = t.getText();
				if (text != null) {
					length = text.length();
				}
			}
			if (e instanceof LexerNoViableAltException) {
				error = SyntaxErrors.LexerNoViableAlternative;
			}
			if (e instanceof InputMismatchException) {
				error = SyntaxErrors.InputMismatch;
			}
			if (e instanceof FailedPredicateException) {
				error = SyntaxErrors.FailedPredicate;
			}
			line += lineOffset;
			syntaxProblems.add(new Problem(error, msg, line, charPositionInLine, length, totalOffset));
		}
	}

	public static enum SyntaxErrors implements IErrorCode {
		FailedPredicate, NoViableAlternative, LexerNoViableAlternative, InputMismatch, OtherException, MissingSemicolon, MissingType, WrongOrder, MissingWidth, MissingIfParen;

		@Override
		public ProblemSeverity getSeverity() {
			return ProblemSeverity.ERROR;
		}

	}

	/**
	 * Parses the given input String and generates a output {@link HDLPackage}
	 * if it succeed
	 *
	 * @param input
	 *            the String to parse and convert
	 * @param libURI
	 *            the library URI to retrieve a registered {@link HDLLibrary}
	 * @param syntaxProblems
	 *            a HashSet where syntax problems will be added to
	 * @param src
	 *            the resource from which this String was derived
	 * @return a {@link HDLPackage} if successful, <code>null</code>l otherwise
	 */
	public static HDLPackage parseString(String input, String libURI, final Set<Problem> syntaxProblems, String src) {
		return parseStream(new ANTLRInputStream(input), libURI, syntaxProblems, src);
	}

	private static HDLPackage parseStream(ANTLRInputStream input, String libURI, final Set<Problem> syntaxProblems, String src) {
		final PSHDLLangLexer lexer = new PSHDLLangLexer(input);
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final PSHDLLangParser parser = new PSHDLLangParser(tokens);
		final ANTLRErrorListener listener = new SyntaxErrorCollector(tokens, syntaxProblems);
		lexer.removeErrorListeners();
		lexer.addErrorListener(listener);
		parser.removeErrorListeners();
		parser.addErrorListener(listener);
		final PsModelContext psModel = parser.psModel();
		if (syntaxProblems.size() == 0) {
			final HDLPackage hdl = ParserToModelExtension.toHDL(tokens, psModel, libURI, src);
			return hdl;
		}
		return null;
	}

	/**
	 * Parses the given input String and generates a output {@link HDLPackage}
	 * if it succeed
	 *
	 * @param input
	 *            the String to parse and convert
	 * @param syntaxProblems
	 *            a HashSet where syntax problems will be added to
	 * @return a {@link HDLPackage} if successful, <code>null</code>l otherwise
	 */
	public static HDLExpression parseExpressionString(String input, final Set<Problem> syntaxProblems) {
		return parseExpressionStream(new ANTLRInputStream(input), syntaxProblems);
	}

	private static HDLExpression parseExpressionStream(ANTLRInputStream input, final Set<Problem> syntaxProblems) {
		final PSHDLLangLexer lexer = new PSHDLLangLexer(input);
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final ANTLRErrorListener parserListener = new SyntaxErrorCollector(tokens, syntaxProblems);
		final PSHDLLangParser parser = new PSHDLLangParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(parserListener);
		lexer.removeErrorListeners();
		lexer.addErrorListener(parserListener);
		final PsExpressionContext psExpression = parser.psExpression();
		if (syntaxProblems.size() == 0) {
			final HDLExpression hdl = ParserToModelExtension.toHDLExpression(tokens, psExpression);
			return hdl;
		}
		return null;
	}
}

package de.tuhh.ict.pshdl.model.parser;

import java.io.*;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.misc.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsModelContext;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.IHDLValidator.IErrorCode;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.Problem.ProblemSeverity;

public class PSHDLParser {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		File file = new File(args[0]);
		HDLCore.init(new IServiceProvider.ServiceLoaderProvider());
		String libURI = "bla";
		HDLLibrary.registerLibrary(libURI, new HDLLibrary());

		if (file.isDirectory()) {
			File[] listFiles = file.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File arg0, String arg1) {
					return arg1.endsWith(".pshdl");
				}
			});
			for (File pF : listFiles) {
				parse(pF, libURI, new HashSet<Problem>());
			}
		} else {
			parse(file, libURI, new HashSet<Problem>());
		}
	}

	public static String[] getKeywords() throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(PSHDLParser.class.getResourceAsStream("PSHDLLangLexer.tokens")));
		String line = null;
		List<String> keywords = new ArrayList<String>();
		while ((line = reader.readLine()) != null)
			if (line.charAt(0) == '\'') {
				String keyWord = line.substring(1, line.lastIndexOf('\''));
				if (keyWord.matches("[a-z]+")) {
					keywords.add(keyWord);
				}
			}
		reader.close();
		return keywords.toArray(new String[keywords.size()]);
	}

	public static HDLPackage parse(File file, String libURI, Set<Problem> syntaxProblems) throws IOException, FileNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		HDLPackage hdl = parseStream(new ANTLRInputStream(fis), libURI, syntaxProblems);
		fis.close();
		return hdl;
	}

	public static final class SyntaxErrorCollector extends BaseErrorListener {
		private final Set<Problem> syntaxProblems;

		public SyntaxErrorCollector(Set<Problem> syntaxProblems) {
			this.syntaxProblems = syntaxProblems;
		}

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, @Nullable Object offendingSymbol, int line, int charPositionInLine, String msg, @Nullable RecognitionException e) {
			int length = -1;
			int totalOffset = -1;
			if (offendingSymbol instanceof Token) {
				Token t = (Token) offendingSymbol;
				totalOffset = t.getStartIndex();
				String text = t.getText();
				if (text != null) {
					length = text.length();
				}
			}
			SyntaxErrors error = SyntaxErrors.OtherException;
			if (e instanceof NoViableAltException) {
				NoViableAltException noVi = (NoViableAltException) e;
				error = SyntaxErrors.NoViableAlternative;
				Token t = noVi.getStartToken();
				charPositionInLine = t.getCharPositionInLine();
				line = t.getLine();
				totalOffset = t.getStartIndex();
				String text = t.getText();
				if (text != null) {
					length = text.length();
				}
			}
			if (e instanceof LexerNoViableAltException) {
				error = SyntaxErrors.LexerNoViableAlternative;
			}
			if (e instanceof InputMismatchException) {
				error = SyntaxErrors.LexerNoViableAlternative;
			}
			if (e instanceof FailedPredicateException) {
				error = SyntaxErrors.FailedPredicate;
			}
			syntaxProblems.add(new Problem(error, msg, line, charPositionInLine, length, totalOffset));
		}
	}

	public static enum SyntaxErrors implements IErrorCode {
		FailedPredicate, NoViableAlternative, LexerNoViableAlternative, InputMismatch, OtherException;

		@Override
		public ProblemSeverity getSeverity() {
			return ProblemSeverity.ERROR;
		}

	}

	public static HDLPackage parseString(String input, String libURI, final Set<Problem> syntaxProblems) {
		return parseStream(new ANTLRInputStream(input), libURI, syntaxProblems);
	}

	private static HDLPackage parseStream(ANTLRInputStream input, String libURI, final Set<Problem> syntaxProblems) {
		PSHDLLangLexer lexer = new PSHDLLangLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PSHDLLangParser parser = new PSHDLLangParser(tokens);
		ANTLRErrorListener listener = new SyntaxErrorCollector(syntaxProblems);
		parser.getErrorListeners().clear();
		parser.addErrorListener(listener);
		PsModelContext psModel = parser.psModel();
		if (syntaxProblems.size() == 0) {
			HDLPackage hdl = ParserToModelExtension.toHDL(tokens, psModel, libURI);
			return hdl;
		}
		return null;
	}
}

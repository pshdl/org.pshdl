package de.tuhh.ict.pshdl.model.parser;

import java.io.*;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.misc.*;

import com.google.common.base.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.extensions.*;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsExtendsContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsModelContext;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
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
		HDLPackage hdl = parseStream(new ANTLRInputStream(fis), libURI, syntaxProblems, file.getAbsolutePath());
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

	public static class Rewriter extends PSHDLLangBaseListener {
		private TokenStreamRewriter rewriter;

		public Rewriter(TokenStream ts) {
			rewriter = new TokenStreamRewriter(ts);
		}

		@Override
		public void enterPsExtends(PsExtendsContext ctx) {
		}
	}

	public static String annotateUnit(String original, HDLUnit unit) {
		for (HDLAnnotation anno : unit.getAnnotations()) {
			if (HDLBuiltInAnnotations.autoInterface.is(anno)) {
				HDLInterface hif = unit.asInterface();
				HDLQualifiedName fqn = FullNameExtension.fullNameOf(unit);
				HDLQualifiedName lastFQN = fqn.skipLast(1).append("I" + fqn.getLastSegment());
				Optional<HDLInterface> resolveInterface = ScopingExtension.INST.resolveInterface(unit, lastFQN);
				boolean needExtend = !unit.getExtendRefName().contains(lastFQN);
				if (resolveInterface.isPresent()) {
					SourceInfo info = resolveInterface.get().getMeta(SourceInfo.INFO);

				} else {
				}
			}
		}
		return original;
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
		PSHDLLangLexer lexer = new PSHDLLangLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PSHDLLangParser parser = new PSHDLLangParser(tokens);
		ANTLRErrorListener listener = new SyntaxErrorCollector(syntaxProblems);
		parser.getErrorListeners().clear();
		parser.addErrorListener(listener);
		PsModelContext psModel = parser.psModel();
		if (syntaxProblems.size() == 0) {
			HDLPackage hdl = ParserToModelExtension.toHDL(tokens, psModel, libURI, src);
			return hdl;
		}
		return null;
	}
}

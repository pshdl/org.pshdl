package de.tuhh.ict.pshdl.model.parser;

import java.io.*;

import org.antlr.v4.runtime.*;

import de.tuhh.ict.pshdl.model.utils.*;

public class PSHDLParser {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		File file = new File(args[0]);
		HDLLibrary.registerLibrary("bla", new HDLLibrary());
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File arg0, String arg1) {
					return arg1.endsWith(".pshdl");
				}
			});
			for (File pF : listFiles) {
				parse(pF);
			}
		} else
			parse(file);
	}

	private static void parse(File file) throws IOException, FileNotFoundException {
		System.out.println("PSHDLParser.parse() " + file);
		FileInputStream fis = new FileInputStream(file);
		ANTLRInputStream input = new ANTLRInputStream(fis);
		PSHDLLangLexer lexer = new PSHDLLangLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PSHDLLangParser parser = new PSHDLLangParser(tokens);
		// PsModelContext psModel = parser.psModel();
		// System.out.println("PSHDLParser.parse()" +
		// ParserToModelExtension.toHDL(psModel, "bla").toString());
		fis.close();
	}
}

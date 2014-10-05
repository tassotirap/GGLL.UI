package org.ggll.grammar;

import ggll.core.lexical.YyFactory;
import ggll.core.lexical.Yylex;
import ggll.core.list.ExtendedList;
import ggll.core.syntax.parser.GGLLTable;

import java.io.File;

import org.ggll.facade.GGLLFacade;
import org.ggll.file.FileNames;
import org.ggll.grammar.validation.GrammarError;
import org.ggll.grammar.validation.GrammarValidation;
import org.ggll.grammar.validation.HeaderValidation;
import org.ggll.grammar.validation.LeftRecursionValidation;
import org.ggll.grammar.validation.LeftSideValidation;
import org.ggll.grammar.validation.NTerminalValidation;
import org.ggll.grammar.validation.TerminalValidation;
import org.ggll.output.AppOutput;
import org.ggll.output.HtmlViewer.TOPIC;
import org.ggll.output.Output;
import org.ggll.parser.ParsingEditor;
import org.ggll.parser.syntax.SyntacticLoader;
import org.ggll.util.Log;
import org.ggll.util.io.IOHelper;

public class GrammarParser
{
	private Yylex createLexFile()
	{
		final String outputDir = GGLLFacade.getInstance().getProjectsRootPath() + "/export";
		final YyFactory yyFactory = new YyFactory();
		try
		{
			yyFactory.createYylex(outputDir, GGLLFacade.getInstance().getLexicalFile().getPath());
			return yyFactory.getYylex(new File(outputDir + "/Yylex.java"));
		}
		catch (final Exception e)
		{
			Log.write(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public void parseGrammar()
	{
		GGLLFacade.getInstance().saveAllFiles();
		Output.getInstance().clear();

		AppOutput.clearStacks();
		AppOutput.clearGeneratedGrammar();

		AppOutput.displayText("Running grammar generation...", TOPIC.Output);

		if (validateGrammar())
		{
			final File dir = new File(GGLLFacade.getInstance().getProjectDir().getAbsolutePath(), "export");
			if (!dir.exists())
			{
				dir.mkdir();
			}

			final ParsingEditor parsingEditor = ParsingEditor.getInstance();

			final Yylex yylex = createLexFile();
			parsingEditor.setYylex(yylex);

			final SyntacticLoader syntacticLoader = new SyntacticLoader();
			parsingEditor.setSyntacticLoader(syntacticLoader);

			final GGLLTable analyzer = new GGLLTable(syntacticLoader.getTableNodes(), syntacticLoader.getNTerminalTable(), syntacticLoader.getTerminalTable());
			analyzer.serialize(GGLLFacade.getInstance().getProjectsRootPath() + "/export/data.ggll");
			final File semanticIn = new File(GGLLFacade.getInstance().getProjectsRootPath() + "/" + GGLLFacade.getInstance().getProjectDir().getName() + FileNames.SEM_EXTENSION);
			final File semanticOut = new File(GGLLFacade.getInstance().getProjectsRootPath() + "/export/" + GGLLFacade.getInstance().getProjectDir().getName() + FileNames.SEM_EXTENSION);
			IOHelper.copyFile(semanticIn, semanticOut);

			AppOutput.displayText("<font color='green'>Grammar successfully generated.</font>", TOPIC.Output);
			AppOutput.displayHorizontalLine(TOPIC.Output);
		}
	}

	public boolean validateGrammar()
	{
		final ExtendedList<GrammarValidation> rules = new ExtendedList<GrammarValidation>();
		rules.append(new HeaderValidation());
		rules.append(new LeftSideValidation());
		rules.append(new NTerminalValidation());
		rules.append(new TerminalValidation());
		rules.append(new LeftRecursionValidation());

		for (final GrammarValidation grammarRule : rules.getAll())
		{
			grammarRule.validate();
		}

		int errors = 1;
		String errorList = "";
		for (final GrammarValidation grammarRule : rules.getAll())
		{
			for (final GrammarError grammarError : grammarRule.getErrors().getAll())
			{
				if (grammarError.getNode() == null)
				{
					errorList += errors + " - " + grammarError.getError() + "<br />";
				}
				else
				{
					errorList += errors + " - Node: <b><a href='Label|" + grammarError.getNode().getTitle() + "'>" + grammarError.getNode().getTitle() + "</a></b> - " + grammarError.getError() + "<br />";
				}
				errors++;
			}
		}

		if (!errorList.equals(""))
		{
			AppOutput.displayText("<font color='red'>Grammar has some errors: <br/></font>", TOPIC.Output);
			AppOutput.displayText("<font color='red'>" + errorList + "</font>", TOPIC.Output);
			return false;
		}

		return true;
	}
}

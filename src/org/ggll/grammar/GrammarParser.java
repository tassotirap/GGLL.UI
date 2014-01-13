package org.ggll.grammar;

import ggll.core.lexical.YyFactory;
import ggll.core.list.ExtendedList;
import ggll.core.syntax.parser.GGLLTable;

import java.io.File;

import org.ggll.director.GGLLDirector;
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
import org.ggll.output.SyntaxErrorOutput;
import org.ggll.output.TokenOutput;
import org.ggll.parser.ParsingEditor;
import org.ggll.parser.syntax.SyntacticLoader;
import org.ggll.parser.syntax.TableCreate;
import org.ggll.util.io.IOHelper;

public class GrammarParser
{
	public void parseGrammar()
	{
		GGLLDirector.saveAllFiles();

		Output.getInstance().clear();
		TokenOutput.getInstance().clear();
		SyntaxErrorOutput.getInstance().clear();
		
		AppOutput.clearOutputBuffer();
		AppOutput.clearStacks();
		AppOutput.clearGeneratedGrammar();

		AppOutput.displayText("Run grammar generate...", TOPIC.Output);

		if (validateGrammar())
		{
			createYyler();
			
			final GrammarFactory grammarFactory = new GrammarFactory();
			final String grammar = grammarFactory.toTable();

			final TableCreate tableCreate = new TableCreate(grammar, false);
			final SyntacticLoader syntacticLoader = new SyntacticLoader(tableCreate);
			final ParsingEditor parsingEditor = ParsingEditor.getInstance().build();
			final File dir = new File(GGLLDirector.getProject().getProjectDir().getAbsolutePath(), "export");
			if (!dir.exists())
			{
				dir.mkdir();
			}
			parsingEditor.setSyntacticLoader(syntacticLoader);

			final GGLLTable analyzer = new GGLLTable(syntacticLoader.tabGraph(), syntacticLoader.tabNt(), syntacticLoader.tabT());
			analyzer.serialize(GGLLDirector.getProject().getProjectDir().getAbsolutePath() + "\\export\\data.ggll");

			final File semantic = new File(GGLLDirector.getProject().getProjectDir().getAbsolutePath() + "\\" + GGLLDirector.getProject().getProjectDir().getName() + FileNames.SEM_EXTENSION);
			IOHelper.copyFile(semantic, new File(GGLLDirector.getProject().getProjectDir().getAbsolutePath() + "\\export\\" + GGLLDirector.getProject().getProjectDir().getName() + FileNames.SEM_EXTENSION));
			AppOutput.displayText("<font color='green'>Grammar successfully generated.</font>", TOPIC.Output);
			AppOutput.displayHorizontalLine(TOPIC.Output);
		}
	}
	
	private void createYyler()
	{
		YyFactory yyFactory = new YyFactory();
		yyFactory.createYylex(GGLLDirector.getProject().getLexicalFile().getParent(), "export", GGLLDirector.getProject().getLexicalFile().getPath());
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
			for (GrammarError grammarError : grammarRule.getErrors().getAll())
			{
				if (grammarError.getNode() == null)
				{
					errorList += errors + " - " + grammarError.getError() + "<br />";
				}
				else
				{
					errorList += errors + " - Node: <b><a href='" + grammarError.getNode().getId() + "'>" + grammarError.getNode().getTitle() + "</a></b> - " + grammarError.getError() + "<br />";
				}
				errors++;
			}
		}

		if (!errorList.equals(""))
		{
			AppOutput.displayText("<font color='red'>Some errors occurred while attempting to generate the grammar: <br/></font>", TOPIC.Output);
			AppOutput.displayText("<font color='red'>" + errorList + "</font>", TOPIC.Output);
			return false;
		}

		return true;
	}
}

package org.ggll.parser.syntax.grammar;

import ggll.core.lexical.YyFactory;
import ggll.core.list.ExtendedList;
import ggll.core.syntax.parser.GGLLTable;

import java.io.File;

import javax.swing.JOptionPane;

import org.ggll.director.GGLLDirector;
import org.ggll.exceptions.InvalidGrammarException;
import org.ggll.file.FileNames;
import org.ggll.output.AppOutput;
import org.ggll.output.HtmlViewer.TOPIC;
import org.ggll.parser.ParsingEditor;
import org.ggll.parser.syntax.SyntacticLoader;
import org.ggll.parser.syntax.TableCreate;
import org.ggll.parser.syntax.validation.GrammarRule;
import org.ggll.parser.syntax.validation.HeaderValidation;
import org.ggll.parser.syntax.validation.LeftRecursionValidation;
import org.ggll.parser.syntax.validation.LeftSideValidation;
import org.ggll.util.io.IOHelper;

public class GrammarParser
{
	public void parseGrammar()
	{
		GGLLDirector.saveAllFiles();

		AppOutput.clearOutputBuffer();
		AppOutput.clearStacks();
		AppOutput.clearGeneratedGrammar();

		YyFactory.createYylex(GGLLDirector.getProject().getLexicalFile().getParent(), "export", GGLLDirector.getProject().getLexicalFile().getPath());

		if (validateGrammar())
		{
			AppOutput.displayHorizontalLine(TOPIC.Output);
			AppOutput.displayText("<a>Run grammar generate...</a><br>", TOPIC.Output);

			final GrammarFactory grammarFactory = new GrammarFactory();
			final String grammar = grammarFactory.run();

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
		}
	}

	public boolean validateGrammar()
	{
		String errors = "";

		final ExtendedList<GrammarRule> rules = new ExtendedList<GrammarRule>();
		rules.append(new HeaderValidation());
		rules.append(new LeftSideValidation());
		rules.append(new LeftRecursionValidation());

		for (final GrammarRule grammarRule : rules.getAll())
		{
			try
			{
				grammarRule.validate();

			}
			catch (final InvalidGrammarException ex)
			{
				errors += ex.getMessage() + "\n";
			}
		}

		if (!errors.equals(""))
		{
			JOptionPane.showMessageDialog(null, errors);
			return false;
		}

		return true;
	}
}

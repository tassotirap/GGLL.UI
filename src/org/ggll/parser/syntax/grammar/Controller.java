package org.ggll.parser.syntax.grammar;

import ggll.core.lexical.YyFactory;
import ggll.core.syntax.parser.GGLLTable;

import java.io.File;

import javax.swing.DefaultListModel;

import org.ggll.director.GGLLDirector;
import org.ggll.file.FileNames;
import org.ggll.output.AppOutput;
import org.ggll.output.HtmlViewer.TOPIC;
import org.ggll.parser.ParsingEditor;
import org.ggll.parser.syntax.SyntacticLoader;
import org.ggll.parser.syntax.TableCreate;
import org.ggll.parser.syntax.validation.GSLL1Rules;
import org.ggll.parser.syntax.validation.GrammarRule;
import org.ggll.parser.syntax.validation.InvalidGrammarException;
import org.ggll.util.io.IOHelper;

public class Controller
{
	private static void errorFound(Exception ex)
	{
		final DefaultListModel model = new DefaultListModel();
		model.addElement(ex.getMessage());
		AppOutput.clearGeneratedGrammar();
	}

	public static void generateAndParseCurrentGrammar()
	{
		YyFactory.createYylex(GGLLDirector.getProject().getLexicalFile().getParent(), "export", GGLLDirector.getProject().getLexicalFile().getPath());
		AppOutput.clearOutputBuffer();
		AppOutput.clearStacks();
		final GrammarFactory grammarFactory = new GrammarFactory();
		String grammar = null;
		boolean validated = false;
		try
		{
			AppOutput.clearGeneratedGrammar();
			AppOutput.displayHorizontalLine(TOPIC.Output);
			AppOutput.displayText("<a>Run grammar generate...</a><br>", TOPIC.Output);

			grammar = grammarFactory.run();
			validated = grammar != null && !grammar.equals("");
		}
		catch (final Exception ex)
		{
			validated = false;
			errorFound(ex);
		}
		final Grammar absGrammar = grammarFactory.getGrammar();
		if (absGrammar != null)
		{
			final GrammarRule gr = new GSLL1Rules(absGrammar, false);
			try
			{
				gr.validate();
			}
			catch (final InvalidGrammarException ex)
			{
				validated = false;
				errorFound(ex);
			}
		}
		if (validated)
		{
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
}

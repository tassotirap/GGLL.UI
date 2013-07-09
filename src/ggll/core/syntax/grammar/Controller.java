package ggll.core.syntax.grammar;

import ggll.core.lexical.YyFactory;
import ggll.core.syntax.SyntacticLoader;
import ggll.core.syntax.TableCreate;
import ggll.core.syntax.parser.GGLLTable;
import ggll.core.syntax.validation.GSLL1Rules;
import ggll.core.syntax.validation.GrammarRule;
import ggll.core.syntax.validation.InvalidGrammarException;
import ggll.file.FileNames;
import ggll.output.AppOutput;
import ggll.output.HtmlViewer.TOPIC;
import ggll.parser.ParsingEditor;
import ggll.project.GGLLManager;
import ggll.util.IOUtilities;

import java.io.File;

import javax.swing.DefaultListModel;

public class Controller
{
	private static void errorFound(Exception ex)
	{
		DefaultListModel model = new DefaultListModel();
		model.addElement(ex.getMessage());
		AppOutput.clearGeneratedGrammar();
	}

	public static void generateAndParseCurrentGrammar()
	{
		YyFactory.createYylex(GGLLManager.getProject().getLexicalFile().getParent(), "export", GGLLManager.getProject().getLexicalFile().getPath());
		AppOutput.clearOutputBuffer();
		AppOutput.clearStacks();
		GrammarFactory grammarFactory = new GrammarFactory();
		String grammar = null;
		boolean validated = false;
		try
		{
			AppOutput.clearGeneratedGrammar();
			AppOutput.displayHorizontalLine(TOPIC.Output);
			AppOutput.displayText("<a>Run grammar generate...</a><br>", TOPIC.Output);

			grammar = grammarFactory.run();
			validated = (grammar != null && !grammar.equals(""));
		}
		catch (Exception ex)
		{
			validated = false;
			errorFound(ex);
		}
		Grammar absGrammar = grammarFactory.getGrammar();
		if (absGrammar != null)
		{
			GrammarRule gr = new GSLL1Rules(absGrammar, false);
			try
			{
				gr.validate();
			}
			catch (InvalidGrammarException ex)
			{
				validated = false;
				errorFound(ex);
			}
		}
		if (validated)
		{
			TableCreate tableCreate = new TableCreate(grammar, false);
			SyntacticLoader syntacticLoader = new SyntacticLoader(tableCreate);
			ParsingEditor parsingEditor = ParsingEditor.getInstance().build();
			File dir = new File(GGLLManager.getProject().getProjectDir().getAbsolutePath(), "export");
			if (!dir.exists())
			{
				dir.mkdir();
			}
			parsingEditor.setSyntacticLoader(syntacticLoader);

			GGLLTable analyzer = new GGLLTable(syntacticLoader.tabGraph(), syntacticLoader.tabNt(), syntacticLoader.tabT());
			analyzer.serialize(GGLLManager.getProject().getProjectDir().getAbsolutePath() + "\\export\\data.ggll");

			File semantic = new File(GGLLManager.getProject().getProjectDir().getAbsolutePath() + "\\" + GGLLManager.getProject().getProjectDir().getName() + FileNames.SEM_EXTENSION);
			IOUtilities.copyFile(semantic, new File(GGLLManager.getProject().getProjectDir().getAbsolutePath() + "\\export\\" + GGLLManager.getProject().getProjectDir().getName() + FileNames.SEM_EXTENSION));
		}
	}
}

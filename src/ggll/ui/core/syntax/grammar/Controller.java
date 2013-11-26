package ggll.ui.core.syntax.grammar;

import ggll.core.lexical.YyFactory;
import ggll.core.syntax.parser.GGLLTable;
import ggll.ui.core.syntax.SyntacticLoader;
import ggll.ui.core.syntax.TableCreate;
import ggll.ui.core.syntax.validation.GSLL1Rules;
import ggll.ui.core.syntax.validation.GrammarRule;
import ggll.ui.core.syntax.validation.InvalidGrammarException;
import ggll.ui.file.FileNames;
import ggll.ui.output.AppOutput;
import ggll.ui.output.HtmlViewer.TOPIC;
import ggll.ui.parser.ParsingEditor;
import ggll.ui.project.GGLLManager;
import ggll.ui.util.io.IOHelper;

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
			IOHelper.copyFile(semantic, new File(GGLLManager.getProject().getProjectDir().getAbsolutePath() + "\\export\\" + GGLLManager.getProject().getProjectDir().getName() + FileNames.SEM_EXTENSION));
		}
	}
}

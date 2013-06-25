package org.ggll.core.syntax.grammar;

import ggll.core.lexical.YyFactory;
import ggll.core.syntax.parser.GGLLTable;

import java.io.File;

import javax.swing.DefaultListModel;

import org.ggll.core.syntax.SyntacticLoader;
import org.ggll.core.syntax.TableCreate;
import org.ggll.core.syntax.validation.GSLL1Rules;
import org.ggll.core.syntax.validation.GrammarRule;
import org.ggll.core.syntax.validation.InvalidGrammarException;
import org.ggll.model.FileNames;
import org.ggll.output.AppOutput;
import org.ggll.parser.ParsingEditor;
import org.ggll.project.GGLLManager;
import org.ggll.util.IOUtilities;

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
			grammar = grammarFactory.run(false);
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
			
			
			//GGLLManager.getMainWindow().getTabs().getRightTopTab().set
		}
	}
}

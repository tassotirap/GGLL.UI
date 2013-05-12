package org.ggll.core.syntax.grammar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.DefaultListModel;

import org.ggll.core.lexical.YyFactory;
import org.ggll.core.semantics.SemanticRoutinesIvoker;
import org.ggll.core.syntax.SyntacticLoader;
import org.ggll.core.syntax.TableCreate;
import org.ggll.core.syntax.model.TableGraphNode;
import org.ggll.core.syntax.model.TableNode;
import org.ggll.core.syntax.validation.GSLL1Rules;
import org.ggll.core.syntax.validation.GrammarRule;
import org.ggll.core.syntax.validation.InvalidGrammarException;
import org.ggll.output.AppOutput;
import org.ggll.parser.ParsingEditor;
import org.ggll.project.GGLLManager;
import org.ggll.ui.debug.ErrorDialog;
import org.ggll.util.IOUtilities;

public class Controller
{
	private static void errorFound(Exception ex)
	{
		ErrorDialog ed = new ErrorDialog(null);
		DefaultListModel model = new DefaultListModel();
		model.addElement(ex.getMessage());
		ed.getErrorList().setModel(model);
		ed.getTaErrorDescription().setText("No description available");
		ed.setVisible(true);
		AppOutput.clearGeneratedGrammar();
	}

	public static void generateAndParseCurrentGrammar()
	{
		YyFactory.createYylex(GGLLManager.getProject().getLexicalFile().getParent(), "generated_code", GGLLManager.getProject().getLexicalFile().getPath());
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
			SemanticRoutinesIvoker.getLastInstance().configureAndLoad();
			File dir = new File(GGLLManager.getProject().getProjectDir().getAbsolutePath(), "export");
			if (!dir.exists())
			{
				dir.mkdir();
			}
			parsingEditor.setSyntacticLoader(syntacticLoader);			
			toFileTerminalTab(syntacticLoader.tabT());
			toFileTnTerminalTab(syntacticLoader.tabNt());
			toFileTabGraphNodes(syntacticLoader.tabGraph());
			
			File semantic = new File(GGLLManager.getProject().getProjectDir().getAbsolutePath() + "\\" + GGLLManager.getProject().getProjectDir().getName() + ".sem");
			File lex = new File(GGLLManager.getProject().getProjectDir().getAbsolutePath() + "\\generated_code\\Yylex.java");
			
			IOUtilities.copyFile(semantic, new File(GGLLManager.getProject().getProjectDir().getAbsolutePath() + "\\export\\"+GGLLManager.getProject().getProjectDir().getName()+".sem"));
			IOUtilities.copyFile(lex, new File(GGLLManager.getProject().getProjectDir().getAbsolutePath() + "\\export\\Yylex.java"));
		}
	}
	
	private static void toFileTerminalTab(TableNode termialTab[])
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(GGLLManager.getProject().getProjectDir().getAbsolutePath() + "\\export\\termialTab.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(termialTab);
			oos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private static void toFileTnTerminalTab(TableNode nTerminalTab[])
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(GGLLManager.getProject().getProjectDir().getAbsolutePath() + "\\export\\nTerminalTab.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(nTerminalTab);
			oos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private static void toFileTabGraphNodes(TableGraphNode tabGraphNodes[])
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(GGLLManager.getProject().getProjectDir().getAbsolutePath() + "\\export\\tabGraphNodes.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(tabGraphNodes);
			oos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

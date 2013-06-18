package org.ggll.parser;

import ggll.core.compile.ClassLoader;
import ggll.core.compile.Compiler;
import ggll.core.lexical.YyFactory;
import ggll.core.lexical.Yylex;
import ggll.core.semantics.SemanticRoutineClass;
import ggll.core.syntax.model.ParseNode;
import ggll.core.syntax.model.ParseStack;
import ggll.core.syntax.parser.GGLLTable;
import ggll.core.syntax.parser.Parser;
import ggll.core.syntax.parser.ParserOutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.ggll.core.syntax.SyntacticLoader;
import org.ggll.output.AppOutput;
import org.ggll.output.HtmlViewer.TOPIC;
import org.ggll.output.Output;
import org.ggll.project.GGLLManager;
import org.ggll.ui.component.NewTextArea;
import org.ggll.util.Log;

public class ParsingEditor
{

	private static ParsingEditor instance;
	private SyntacticLoader syntacticLoader;
	private StringReader stringReader;
	private int lastLine;
	private Yylex yylex;
	private StringBuffer textToParse;
	private ArrayList<JButton> parsingButtons;
	private String rootPath;
	private Parser analyzer;
	private NewTextArea standaloneTextArea;

	public ParsingEditor(SyntacticLoader syntacticLoader, String rootPath)
	{
		instance = this;

		this.standaloneTextArea = new NewTextArea();
//		this.standaloneTextArea.getBuffer().setMode(mode);
//		this.standaloneTextArea.getBuffer().addBufferListener(this);
//		this.standaloneTextArea.addCaretListener(this);
//		this.standaloneTextArea.setCaretBlinkEnabled(true);

		this.parsingButtons = new ArrayList<JButton>();

		this.syntacticLoader = syntacticLoader;
		this.rootPath = rootPath;

		this.textToParse = new StringBuffer();

		this.stringReader = new StringReader("");
	}

	public static ParsingEditor getInstance()
	{
		return instance;
	}

	/**
	 * clear the local buffer, inserts a new line, and moves the caret to the
	 * new line
	 * 
	 * @param insertNewLine
	 *            , whether a new line will be inserted or not
	 */
	private void clearBufferAndGoToNextLine(boolean insertNewLine)
	{
//		standaloneTextArea.goToBufferEnd(false);
//		if (insertNewLine)
//		{
//			standaloneTextArea.insertEnterAndIndent();
//		}
//		standaloneTextArea.goToNextLine(false);
//		standaloneTextArea.scrollToCaret(true);
//		lastLine = standaloneTextArea.getLastPhysicalLine();
		textToParse = new StringBuffer();
	}

	private void updateParsingButtons()
	{
		if (textToParse.toString().equals(""))
		{
			for (JButton button : parsingButtons)
			{
				button.setEnabled(false);
			}
		}
		else
		{
			for (JButton button : parsingButtons)
			{
				button.setEnabled(true);
			}
		}
	}

	public void addParsingButtons(JButton... buttons)
	{
		for (JButton button : buttons)
		{
			parsingButtons.add(button);
		}
	}

	public ParsingEditor build()
	{
		try
		{
			yylex = YyFactory.getYylex(new File(rootPath + "/export/Yylex.java"));
			return instance;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}


	public void displayInputTextNewLine(String str)
	{
//		standaloneTextArea.goToBufferEnd(false);
//		standaloneTextArea.getBuffer().insert(standaloneTextArea.getText().length(), "\n" + str);
	}

	public void displayInputTextNoLine(String str)
	{
//		standaloneTextArea.goToBufferEnd(false);
//		standaloneTextArea.getBuffer().insert(standaloneTextArea.getText().length(), str);
	}

	public void displayOutputText(String str)
	{
//		clearBufferAndGoToNextLine(false);
//		standaloneTextArea.getBuffer().insert(standaloneTextArea.getText().length(), str);
//		clearBufferAndGoToNextLine(true);
//		Output.getInstance().displayTextExt("** Result: " + str, TOPIC.Parser);
	}


	/**
	 * @return the rootPath
	 */
	public String getRootPath()
	{
		return rootPath;
	}

	public JTextArea getTextArea()
	{
		return standaloneTextArea.getTextArea();
	}

	public JComponent getView()
	{
		return standaloneTextArea.getComponent();
	}

	public void open()
	{
		JFileChooser c = new JFileChooser();
		int rVal = c.showOpenDialog(getView());
		if (rVal == JFileChooser.APPROVE_OPTION)
		{
			File file = c.getSelectedFile();
			try
			{
				FileInputStream fis = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line = "";
				StringBuffer text = new StringBuffer();
				line = br.readLine();
				while (line != null)
				{
					text.append(line);
					line = br.readLine();
					if (line != null)
					{
						text.append("\n");
					}
				}
//				standaloneTextArea.getBuffer().beginCompoundEdit();
//				standaloneTextArea.setText(text.toString());
//				standaloneTextArea.setCaretPosition(text.length());
//				standaloneTextArea.scrollToCaret(true);
//				standaloneTextArea.getBuffer().endCompoundEdit();
				br.close();
				fis.close();
			}
			catch (Exception e)
			{
				Log.log(Log.ERROR, this, "Could not open file: " + file.getName(), e);
			}
		}
		if (rVal == JFileChooser.CANCEL_OPTION)
		{
			c.setVisible(false);
		}
	}


	private void startParse(boolean stepping)
	{
		Output.getInstance().displayTextExt("<< " + textToParse.toString().replace(">", "&gt;").replace("<", "&lt;"), TOPIC.Parser);
		stringReader = new StringReader(textToParse.toString());
		try
		{
			yylex.yyreset(stringReader);
		}
		catch (IOException e1)
		{
			Log.log(Log.ERROR, this, "An internal error has occurred!", e1);
		}

		try
		{
			Compiler compiler = new Compiler();
			compiler.compile(GGLLManager.getProject().getSemanticFile().getPath());
			ClassLoader<SemanticRoutineClass> classLoader = new ClassLoader<SemanticRoutineClass>(GGLLManager.getProject().getSemanticFile());
			analyzer = new Parser(new GGLLTable(syntacticLoader.tabGraph(), syntacticLoader.tabNt(), syntacticLoader.tabT()), yylex, classLoader.getInstance(), stepping);
			analyzer.setParserOutput(new ParserOutput()
			{
				@Override
				public void Output()
				{
					printStack(analyzer.getParseStacks().getParseStack());

				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private void endParser()
	{
		if (analyzer.isSucess())
		{
			AppOutput.displayText("<font color='green'>Expression Successfully recognized.</font>", TOPIC.Output);
		}
		else
		{
			AppOutput.displayText("<font color='red'>Expression can't be recognized.</font>", TOPIC.Output);
			for (String error : analyzer.getErrorList())
			{
				AppOutput.displayText("<font color='red'>" + error + "</font>", TOPIC.Output);
			}
		}
		analyzer = null;
	}

	public void run(boolean stepping)
	{
		try
		{
			if (analyzer != null)
			{
				analyzer.nextToEnd();
				endParser();
				return;
			}
			if (syntacticLoader == null)
			{
				return;
			}
			if (textToParse.toString().equals(""))
			{
				JOptionPane.showMessageDialog(null, "There is nothing to parse", "Can not parse", JOptionPane.WARNING_MESSAGE);
				return;
			}

			startParse(stepping);

			AppOutput.clearStacks();
			analyzer.run();
			if (!stepping)
			{
				endParser();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		clearBufferAndGoToNextLine(true);
	}

	public void printStack(ParseStack parseStackNode)
	{
		Iterator<ParseNode> iterator = parseStackNode.iterator();
		ParseNode parseStackNodeTemp = null;
		String lineSyntax = "";
		String lineSemantic = "";
		while (iterator.hasNext())
		{
			parseStackNodeTemp = iterator.next();
			lineSyntax += "<a style=\"color: #000000; font-weight: bold;\" href=\"" + parseStackNodeTemp.getFlag() + "\">" + parseStackNodeTemp.getType() + "</a>&nbsp;";
			lineSemantic += parseStackNodeTemp.getSemanticSymbol() + "&nbsp;";
		}

		AppOutput.showAndSelectNode((parseStackNode.peek()).getFlag());
		AppOutput.printlnSyntaxStack(lineSyntax, true);
		AppOutput.printlnSemanticStack(lineSemantic, true);
	}

	public void save()
	{
		JFileChooser c = new JFileChooser();
		int rVal = c.showSaveDialog(getView());
		if (rVal == JFileChooser.APPROVE_OPTION)
		{
			File file = c.getSelectedFile();
			try
			{
				if (!file.exists())
				{
					if (file.createNewFile())
					{
						FileWriter fw = new FileWriter(file);
						//fw.write(standaloneTextArea.getText());
						fw.close();
					}
				}
			}
			catch (Exception e)
			{
				Log.log(Log.ERROR, this, "Could not save file: " + file.getName(), e);
			}
		}
		if (rVal == JFileChooser.CANCEL_OPTION)
		{
			c.setVisible(false);
		}
	}


	public void setSyntacticLoader(SyntacticLoader cs)
	{
		this.syntacticLoader = cs;
	}

	public void stepRun()
	{
		try
		{
			if (analyzer == null)
			{
				run(true);
			}
			if (analyzer == null)
			{
				return;
			}
			if (!analyzer.next())
			{
				endParser();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}

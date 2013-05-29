package org.ggll.parser;

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
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.ggll.actions.Mode;
import org.ggll.core.CoreManager;
import org.ggll.core.lexical.YyFactory;
import org.ggll.core.lexical.Yylex;
import org.ggll.core.syntax.SyntacticLoader;
import org.ggll.core.syntax.analyzer.Analyzer;
import org.ggll.core.syntax.analyzer.AnalyzerTable;
import org.ggll.core.syntax.model.ParseNode;
import org.ggll.core.syntax.model.ParseStack;
import org.ggll.editor.StandaloneTextArea;
import org.ggll.editor.TextArea;
import org.ggll.editor.buffer.BufferListener;
import org.ggll.editor.buffer.JEditBuffer;
import org.ggll.output.AppOutput;
import org.ggll.output.HtmlViewer.TOPIC;
import org.ggll.output.Output;
import org.ggll.project.GGLLManager;
import org.ggll.util.Log;

public class ParsingEditor implements BufferListener, CaretListener
{

	private static ParsingEditor instance;
	private SyntacticLoader syntacticLoader;
	private StringReader stringReader;
	private int lastLine;
	private Yylex yylex;
	private StringBuffer textToParse;
	private ArrayList<JButton> parsingButtons;
	private String rootPath;
	private Analyzer analyzer;
	private StandaloneTextArea standaloneTextArea;

	public ParsingEditor(SyntacticLoader syntacticLoader, Mode mode, String rootPath)
	{
		instance = this;

		this.standaloneTextArea = StandaloneTextArea.createTextArea();
		this.standaloneTextArea.getBuffer().setMode(mode);
		this.standaloneTextArea.getBuffer().addBufferListener(this);
		this.standaloneTextArea.addCaretListener(this);
		this.standaloneTextArea.setCaretBlinkEnabled(true);

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
		standaloneTextArea.goToBufferEnd(false);
		if (insertNewLine)
		{
			standaloneTextArea.insertEnterAndIndent();
		}
		standaloneTextArea.goToNextLine(false);
		standaloneTextArea.scrollToCaret(true);
		lastLine = standaloneTextArea.getLastPhysicalLine();
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

	// ############### BUFFER LISTENER ###############################
	@Override
	public void bufferLoaded(JEditBuffer buffer)
	{

	}

	public ParsingEditor build()
	{
		yylex = YyFactory.getYylex(rootPath + "/generated_code", null, stringReader);
		return instance;
	}

	/* #################### CARET LISTENER ######################### */
	@Override
	public void caretUpdate(CaretEvent e)
	{
		if (standaloneTextArea.getLineOfOffset(e.getDot()) < lastLine)
		{
			if (standaloneTextArea.getLastPhysicalLine() == standaloneTextArea.getLineOfOffset(e.getDot()))
			{
				standaloneTextArea.insertEnterAndIndent();
			}
			else
			{
				standaloneTextArea.getBuffer().setReadOnly(true);
			}
		}
		else
		{
			standaloneTextArea.getBuffer().setReadOnly(false);
		}
	}

	@Override
	public void contentInserted(JEditBuffer buffer, int startLine, int offset, int numLines, int length)
	{
		if (startLine >= lastLine)
		{
			textToParse.append(buffer.getText(offset, length));
			updateParsingButtons();
		}

	}

	@Override
	public void contentRemoved(JEditBuffer buffer, int startLine, int offset, int numLines, int length)
	{
		if (startLine >= lastLine)
		{
			textToParse = new StringBuffer();
			textToParse.append(buffer.getText(buffer.getLineStartOffset(lastLine), buffer.getLength() - buffer.getLineStartOffset(lastLine)));
		}
	}

	public void displayInputTextNewLine(String str)
	{
		standaloneTextArea.goToBufferEnd(false);
		standaloneTextArea.getBuffer().insert(standaloneTextArea.getText().length(), "\n" + str);
	}

	public void displayInputTextNoLine(String str)
	{
		standaloneTextArea.goToBufferEnd(false);
		standaloneTextArea.getBuffer().insert(standaloneTextArea.getText().length(), str);
	}

	public void displayOutputText(String str)
	{
		clearBufferAndGoToNextLine(false);
		standaloneTextArea.getBuffer().insert(standaloneTextArea.getText().length(), str);
		clearBufferAndGoToNextLine(true);
		Output.getInstance().displayTextExt("** Result: " + str, TOPIC.Parser);
	}

	@Override
	public void foldHandlerChanged(JEditBuffer buffer)
	{

	}

	@Override
	public void foldLevelChanged(JEditBuffer buffer, int startLine, int endLine)
	{

	}

	public Mode getMode()
	{
		return standaloneTextArea.getBuffer().getMode();
	}

	/**
	 * @return the rootPath
	 */
	public String getRootPath()
	{
		return rootPath;
	}

	public TextArea getTextArea()
	{
		return standaloneTextArea;
	}

	public JComponent getView()
	{
		return standaloneTextArea;
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
				standaloneTextArea.getBuffer().beginCompoundEdit();
				standaloneTextArea.setText(text.toString());
				standaloneTextArea.setCaretPosition(text.length());
				standaloneTextArea.scrollToCaret(true);
				standaloneTextArea.getBuffer().endCompoundEdit();
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

	@Override
	public void preContentInserted(JEditBuffer buffer, int startLine, int offset, int numLines, int length)
	{

	}

	@Override
	public void preContentRemoved(JEditBuffer buffer, int startLine, int offset, int numLines, int length)
	{

	}

	public void run(boolean stepping)
	{
		if (syntacticLoader != null)
		{
			if (textToParse.toString().equals(""))
			{
				JOptionPane.showMessageDialog(null, "There is nothing to parse", "Can not parse", JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				Output.getInstance().displayTextExt("<< " + textToParse.toString(), TOPIC.Parser);
				stringReader = new StringReader(textToParse.toString());
				try
				{
					yylex.yyreset(stringReader);
				}
				catch (IOException e1)
				{
					Log.log(Log.ERROR, this, "An internal error has occurred!", e1);
				}

				analyzer = new Analyzer(new AnalyzerTable(syntacticLoader.tabGraph(), syntacticLoader.tabNt(), syntacticLoader.tabT()), null, yylex, GGLLManager.getProject().getSemanticFile(), true);
				
				if(!stepping)
				{
					AppOutput.clearStacks();
					analyzer.run();
					while(analyzer.next())
					{
						printStack(analyzer.getAnalyzerStacks().getParseStack());
					}
					if (CoreManager.isSucess())
					{
						AppOutput.displayText("<font color='green'>Expression Successfully recognized.</font>", TOPIC.Output);
					}
					else
					{
						AppOutput.displayText("<font color='red'>Expression can't be recognized.</font>", TOPIC.Output);
						for (String error : CoreManager.getErrorList())
						{
							AppOutput.displayText("<font color='red'>" + error + "</font>", TOPIC.Output);
						}
					}
				}				
				clearBufferAndGoToNextLine(true);
			}
		}
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
						fw.write(standaloneTextArea.getText());
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

	public void setMode(Mode mode)
	{
		standaloneTextArea.getBuffer().setMode(mode);
		String text = standaloneTextArea.getText();
		int carret = standaloneTextArea.getCaretPosition();
		standaloneTextArea.setText("");
		standaloneTextArea.setText(text);
		standaloneTextArea.setCaretPosition(carret);
		standaloneTextArea.scrollToCaret(true);

	}

	public void setSyntacticLoader(SyntacticLoader cs)
	{
		this.syntacticLoader = cs;
	}

	public void stepRun()
	{
		analyzer.next();
	}

	@Override
	public void transactionComplete(JEditBuffer buffer)
	{

	}

}

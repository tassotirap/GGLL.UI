package ggll.ui.parser;

import ggll.core.compile.ClassLoader;
import ggll.core.compile.Compiler;
import ggll.core.exceptions.ErrorRecoveryException;
import ggll.core.exceptions.LexicalException;
import ggll.core.exceptions.SemanticException;
import ggll.core.exceptions.SintaticException;
import ggll.core.lexical.YyFactory;
import ggll.core.lexical.Yylex;
import ggll.core.semantics.SemanticRoutineClass;
import ggll.core.syntax.model.ParseNode;
import ggll.core.syntax.model.ParseStack;
import ggll.core.syntax.parser.GGLLTable;
import ggll.core.syntax.parser.Parser;
import ggll.core.syntax.parser.ParserOutput;
import ggll.ui.core.syntax.SyntacticLoader;
import ggll.ui.output.AppOutput;
import ggll.ui.output.Output;
import ggll.ui.output.HtmlViewer.TOPIC;
import ggll.ui.project.GGLLManager;
import ggll.ui.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import javax.swing.JOptionPane;

public class ParsingEditor
{
	private static ParsingEditor instance;
	private SyntacticLoader syntacticLoader;
	private Yylex yylex;
	private Parser analyzer;

	private String rootPath;

	public ParsingEditor(SyntacticLoader syntacticLoader, String rootPath)
	{
		instance = this;
		this.rootPath = rootPath;
		this.syntacticLoader = syntacticLoader;
	}

	public static ParsingEditor getInstance()
	{
		return instance;
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
			for (Exception error : analyzer.getErrorList())
			{
				if(error instanceof SintaticException)
				{
					SintaticException sintaticException = (SintaticException)error;
					AppOutput.displayText("<font color='red'>Sintatic Error: " + sintaticException.getToken() + "</font>", TOPIC.Output);
				}
				else if(error instanceof ErrorRecoveryException)
				{
					ErrorRecoveryException errorRecoveryException = (ErrorRecoveryException)error;
					AppOutput.displayText("<font color='red'>Erro Recovery: " + errorRecoveryException.getMessage() + "</font>", TOPIC.Output);
				}
				else if(error instanceof LexicalException)
				{
					LexicalException lexicalException = (LexicalException)error;
					AppOutput.displayText("<font color='red'>Lexical Error: " + lexicalException.getToken() + "</font>", TOPIC.Output);
				}
				else if(error instanceof SemanticException)
				{
					SemanticException semanticException = (SemanticException)error;
					AppOutput.displayText("<font color='red'>Semantic Error: " + semanticException.getMessage() + "</font>", TOPIC.Output);
				}				
			}
		}
		analyzer = null;
	}

	private void startParse(boolean stepping, String text)
	{

		Output.getInstance().displayTextExt("<< " + text.replace(">", "&gt;").replace("<", "&lt;"), TOPIC.Parser);
		StringReader stringReader = new StringReader(text);
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
					printStack(analyzer.getParserStacks().getParseStack());

				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
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

	public void run(boolean stepping, String text)
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
			if (text.equals(""))
			{
				JOptionPane.showMessageDialog(null, "There is nothing to parse", "Can not parse", JOptionPane.WARNING_MESSAGE);
				return;
			}

			startParse(stepping, text);

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
	}

	public void setSyntacticLoader(SyntacticLoader cs)
	{
		this.syntacticLoader = cs;
	}

	public void stepRun(String text)
	{
		try
		{
			if (analyzer == null)
			{
				run(true, text);
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

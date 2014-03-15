package org.ggll.parser;

import ggll.core.compile.ClassLoader;
import ggll.core.compile.Compiler;
import ggll.core.exceptions.ErrorRecoveryException;
import ggll.core.exceptions.LexicalException;
import ggll.core.exceptions.SemanticException;
import ggll.core.exceptions.SintaticException;
import ggll.core.lexical.Yylex;
import ggll.core.semantics.SemanticRoutineClass;
import ggll.core.syntax.model.ParseNode;
import ggll.core.syntax.model.ParseStack;
import ggll.core.syntax.parser.GGLLTable;
import ggll.core.syntax.parser.Parser;
import ggll.core.syntax.parser.ParserOutput;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringEscapeUtils;
import org.ggll.facade.GGLLFacade;
import org.ggll.output.AppOutput;
import org.ggll.output.HtmlViewer.TOPIC;
import org.ggll.output.Output;
import org.ggll.parser.syntax.SyntacticLoader;
import org.ggll.util.Log;

public class ParsingEditor
{
	private static ParsingEditor instance;
	
	public static ParsingEditor getInstance()
	{
		if (ParsingEditor.instance == null)
		{
			ParsingEditor.instance = new ParsingEditor();
		}
		return ParsingEditor.instance;
	}
	
	private SyntacticLoader syntacticLoader;
	private Yylex yylex;
	
	private Parser analyzer;
	
	private void endParser()
	{
		if (analyzer.isSucess())
		{
			AppOutput.displayText("<font color='green'>Expression Successfully recognized.</font>", TOPIC.Output);
		}
		else
		{
			AppOutput.displayText("<font color='red'>Expression can't be recognized.</font>", TOPIC.Output);
			for (final Exception error : analyzer.getErrorList().getAll())
			{
				if (error instanceof SintaticException)
				{
					final SintaticException sintaticException = (SintaticException) error;
					AppOutput.displayText("<font color='red'>" + sintaticException.getMessage() + "</font>", TOPIC.Output);
				}
				else if (error instanceof ErrorRecoveryException)
				{
					final ErrorRecoveryException errorRecoveryException = (ErrorRecoveryException) error;
					AppOutput.displayText("<font color='red'>Erro Recovery: " + errorRecoveryException.getMessage() + "</font>", TOPIC.Output);
				}
				else if (error instanceof LexicalException)
				{
					final LexicalException lexicalException = (LexicalException) error;
					AppOutput.displayText("<font color='red'>Lexical Error: \"" + lexicalException.getToken() + "\"</font>", TOPIC.Output);
				}
				else if (error instanceof SemanticException)
				{
					final SemanticException semanticException = (SemanticException) error;
					AppOutput.displayText("<font color='red'>Semantic Error: " + semanticException.getMessage() + "</font>", TOPIC.Output);
				}
			}
		}
		analyzer = null;
	}
	
	public Yylex getYylex()
	{
		return yylex;
	}
	
	public void printStack(final ParseStack parseStackNode)
	{
		final Iterator<ParseNode> iterator = parseStackNode.iterator();
		ParseNode parseStackNodeTemp = null;
		String lineSyntax = "";
		String lineSemantic = "";
		while (iterator.hasNext())
		{
			parseStackNodeTemp = iterator.next();
			lineSyntax += "<a style='color: #000000; font-weight: bold;' href='Id|" + parseStackNodeTemp.getFlag() + "'>" + parseStackNodeTemp.getType() + "</a>&nbsp;";
			lineSemantic += parseStackNodeTemp.getSemanticSymbol() + "&nbsp;";
		}
		
		AppOutput.showAndSelectNode(parseStackNode.peek().getFlag());
		AppOutput.printlnSyntaxStack(lineSyntax, true);
		AppOutput.printlnSemanticStack(lineSemantic, true);
	}
	
	public void run(final boolean stepping, final String text)
	{
		try
		{
			if (analyzer != null)
			{
				analyzer.nextToEnd();
				endParser();
				return;
			}
			if (syntacticLoader == null) { return; }
			if (text.equals(""))
			{
				JOptionPane.showMessageDialog(null, "There is nothing to parse", "Can not parse", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			startParser(stepping, text);
			
			AppOutput.clearStacks();
			analyzer.run();
			if (!stepping)
			{
				endParser();
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setSyntacticLoader(final SyntacticLoader cs)
	{
		syntacticLoader = cs;
	}
	
	public void setYylex(final Yylex yylex)
	{
		this.yylex = yylex;
	}
	
	private void startParser(final boolean stepping, final String text)
	{
		Output.getInstance().displayTextExt(StringEscapeUtils.escapeHtml4(text), TOPIC.Parser);
		final StringReader stringReader = new StringReader(text);
		try
		{
			yylex.yyreset(stringReader);
		}
		catch (final IOException e1)
		{
			Log.write("An internal error has occurred!");
		}
		
		try
		{
			final Compiler compiler = new Compiler();
			compiler.compile(GGLLFacade.getInstance().getSemanticFile().getPath());
			final ClassLoader<SemanticRoutineClass> classLoader = new ClassLoader<SemanticRoutineClass>(GGLLFacade.getInstance().getSemanticFile());
			analyzer = new Parser(new GGLLTable(syntacticLoader.getTableNodes(), syntacticLoader.getNTerminalTable(), syntacticLoader.getTerminalTable()), yylex, classLoader.getInstance(), stepping);
			analyzer.setParserOutput(new ParserOutput()
			{
				@Override
				public void output()
				{
					printStack(analyzer.getParserStacks().getParseStack());
				}
			});
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void stepRun(final String text)
	{
		try
		{
			if (analyzer == null)
			{
				run(true, text);
			}
			if (analyzer == null) { return; }
			if (!analyzer.next())
			{
				endParser();
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
}

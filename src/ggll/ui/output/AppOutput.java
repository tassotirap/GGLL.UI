package ggll.ui.output;

import ggll.ui.output.HtmlViewer.TOPIC;

public abstract class AppOutput
{

	public static void clearGeneratedGrammar()
	{
		GeneratedGrammar.getInstance().clear();
	}

	public static void clearOutputBuffer()
	{
		Output.getInstance().clearOutputBuffer();
	}

	public static void clearStacks()
	{
		SyntaxStack.getInstance().clearStack();
		SemanticStack.getInstance().clearStack();
	}

	static public void displayGeneratedGrammar(String str)
	{
		GeneratedGrammar.getInstance().displayTextExt(str, Output.TOPIC.Grammar);
	}

	static public void displayHorizontalLine(Output.TOPIC topic)
	{
		Output.getInstance().displayHorizontalLineExt(topic);
	}

	static public void displayText(String str, Output.TOPIC topic)
	{
		Output.getInstance().displayTextExt(str, topic);
	}

	static public void errorRecoveryStatus(String str)
	{
		SyntaxErrorOutput.getInstance().displayTextExt(str, TOPIC.Error);
	}

	public static String getReport()
	{
		return Output.getInstance().getReport();
	}

	static public void printlnSemanticStack(String str)
	{
		printSemanticStack(str, false);
	}

	static public void printlnSemanticStack(String str, boolean showLine)
	{
		SemanticStack.getInstance().displayTextExt(str, showLine);
	}

	static public void printlnSyntaxStack(String str)
	{
		printSyntaxStack(str, false);
	}

	static public void printlnSyntaxStack(String str, boolean showLine)
	{
		printSyntaxStack(str, showLine);
	}

	static public void printlnToken(String str)
	{
		TokenOutput.getInstance().displayTextExt(str + "<br>", TOPIC.Tokens);
	}

	static public void printSemanticStack(String str)
	{
		printSemanticStack(str, false);
	}

	static public void printSemanticStack(String str, boolean showLine)
	{
		SemanticStack.getInstance().displayTextExt(str, showLine);
	}

	static public void printSyntaxStack(String str)
	{
		printSyntaxStack(str, false);
	}

	static public void printSyntaxStack(String str, boolean showLine)
	{
		SyntaxStack.getInstance().displayTextExt(str, showLine);
	}

	static public void printToken(String str)
	{
		TokenOutput.getInstance().displayTextExt(str, TOPIC.Tokens);
	}

	public static void showAndSelectNode(String flag)
	{
		Output.getInstance().getActiveScene().select(flag);
	}
}

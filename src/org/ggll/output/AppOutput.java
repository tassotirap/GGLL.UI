package org.ggll.output;

public abstract class AppOutput
{
	public static void clearGeneratedGrammar()
	{
		GeneratedGrammar.getInstance().clear();
	}
	
	public static void clearStacks()
	{
		SyntaxStack.getInstance().clearStack();
		SemanticStack.getInstance().clearStack();
	}
	
	static public void displayGeneratedGrammar(final String str)
	{
		GeneratedGrammar.getInstance().displayTextExt(str, Output.TOPIC.Grammar);
	}
	
	static public void displayHorizontalLine(final Output.TOPIC topic)
	{
		Output.getInstance().displayHorizontalLineExt(topic);
	}
	
	static public void displayText(final String str, final Output.TOPIC topic)
	{
		Output.getInstance().displayTextExt(str, topic);
	}
	
	static public void printlnSemanticStack(final String str)
	{
		AppOutput.printSemanticStack(str, false);
	}
	
	static public void printlnSemanticStack(final String str, final boolean showLine)
	{
		SemanticStack.getInstance().displayTextExt(str, showLine);
	}
	
	static public void printlnSyntaxStack(final String str)
	{
		AppOutput.printSyntaxStack(str, false);
	}
	
	static public void printlnSyntaxStack(final String str, final boolean showLine)
	{
		AppOutput.printSyntaxStack(str, showLine);
	}
	
	static public void printSemanticStack(final String str)
	{
		AppOutput.printSemanticStack(str, false);
	}
	
	static public void printSemanticStack(final String str, final boolean showLine)
	{
		SemanticStack.getInstance().displayTextExt(str, showLine);
	}
	
	static public void printSyntaxStack(final String str)
	{
		AppOutput.printSyntaxStack(str, false);
	}
	
	static public void printSyntaxStack(final String str, final boolean showLine)
	{
		SyntaxStack.getInstance().displayTextExt(str, showLine);
	}
	
	public static void showAndSelectNode(final String flag)
	{
		Output.getInstance().getActiveScene().select(flag);
	}
}

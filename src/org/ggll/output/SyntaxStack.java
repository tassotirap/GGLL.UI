package org.ggll.output;

public class SyntaxStack extends Output
{
	
	private static SyntaxStack instance;
	
	public static SyntaxStack getInstance()
	{
		if (SyntaxStack.instance == null)
		{
			SyntaxStack.instance = new SyntaxStack();
		}
		return SyntaxStack.instance;
	}
	
	private int lastLine;
	
	private SyntaxStack()
	{
		super();
	}
	
	public void clearStack()
	{
		lastLine = 0;
		clear();
	}
	
	public void displayTextExt(final String str, final boolean showLine)
	{
		if (showLine)
		{
			displayTextExt(String.format("<b>%d.&nbsp;&nbsp;</b>%s", ++lastLine, str), TOPIC.SyntaxStack);
		}
		else
		{
			displayTextExt(str, TOPIC.SyntaxStack);
		}
	}
}

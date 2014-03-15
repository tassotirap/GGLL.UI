package org.ggll.output;

public class SemanticStack extends Output
{
	
	private static SemanticStack instance;
	
	public static SemanticStack getInstance()
	{
		if (SemanticStack.instance == null)
		{
			SemanticStack.instance = new SemanticStack();
		}
		return SemanticStack.instance;
	}
	
	private int lastLine;
	
	private SemanticStack()
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
			displayTextExt(String.format("<b>%d.&nbsp;&nbsp;</b>%s", ++lastLine, str), TOPIC.SemanticStack);
		}
		else
		{
			displayTextExt(str, TOPIC.SemanticStack);
		}
	}
}

package ggll.ui.output;

public class SemanticStack extends Output
{

	private static SemanticStack instance;
	private int lastLine;

	private SemanticStack()
	{
		super();
	}

	public static SemanticStack getInstance()
	{
		if (instance == null)
		{
			instance = new SemanticStack();
		}
		return instance;
	}

	public void clearStack()
	{
		this.lastLine = 0;
		clear();
	}

	public void displayTextExt(String str, boolean showLine)
	{
		if (showLine)
		{
			displayTextExt(String.format("<b>%d.&nbsp;&nbsp;</b>%s", ++this.lastLine, str), TOPIC.SemanticStack);
		}
		else
		{
			displayTextExt(str, TOPIC.SemanticStack);
		}
	}
}

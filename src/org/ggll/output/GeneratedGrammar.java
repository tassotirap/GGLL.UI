package org.ggll.output;

public class GeneratedGrammar extends Output
{
	
	private static GeneratedGrammar instance;
	
	public static GeneratedGrammar getInstance()
	{
		if (GeneratedGrammar.instance == null)
		{
			GeneratedGrammar.instance = new GeneratedGrammar();
		}
		return GeneratedGrammar.instance;
	}
	
	private GeneratedGrammar()
	{
		super();
	}
}

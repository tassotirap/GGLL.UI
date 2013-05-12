package org.ggll.ui.component;

public abstract class GrammarFactory
{
	private static GrammarComponent gramComponent;

	public static void addGramComponent(GrammarComponent gramComponent)
	{
		GrammarFactory.gramComponent = gramComponent;
	}

	public static GrammarComponent getGrammarComponent()
	{
		return gramComponent;
	}

}

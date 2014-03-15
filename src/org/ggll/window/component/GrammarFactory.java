package org.ggll.window.component;

import java.util.Hashtable;

public abstract class GrammarFactory
{
	private static Hashtable<String, GrammarComponent> gramComponents;
	
	public static void addGramComponent(final GrammarComponent gramComponent, final String file)
	{
		if (GrammarFactory.gramComponents == null)
		{
			GrammarFactory.gramComponents = new Hashtable<String, GrammarComponent>();
		}
		GrammarFactory.gramComponents.put(file, gramComponent);
	}
	
	public static GrammarComponent getGrammarComponent(final String file)
	{
		return GrammarFactory.gramComponents.get(file);
	}
	
}

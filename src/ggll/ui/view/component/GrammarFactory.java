package ggll.ui.view.component;

import java.util.Hashtable;

public abstract class GrammarFactory
{
	private static Hashtable<String, GrammarComponent> gramComponents;

	public static void addGramComponent(GrammarComponent gramComponent, String file)
	{
		if (gramComponents == null)
		{
			gramComponents = new Hashtable<String, GrammarComponent>();
		}
		gramComponents.put(file, gramComponent);
	}

	public static GrammarComponent getGrammarComponent(String file)
	{
		return gramComponents.get(file);
	}

}

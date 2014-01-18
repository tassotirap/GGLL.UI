package org.ggll.syntax.graph;

import java.util.Collection;
import java.util.Hashtable;

import org.ggll.director.GGLLDirector;
import org.ggll.file.GrammarFile;
import org.ggll.resource.CanvasResource;

public class SyntaxGraphRepository
{
	private static String connStrategy = CanvasResource.R_ORTHOGONAL;
	private static String defaultCursor = CanvasResource.SELECT;
	private static String moveStrategy = CanvasResource.M_ALIGN;

	private static Hashtable<String, SyntaxGraph> instances;
	
	public static SyntaxGraph getInstance(String file)
	{
		if (instances == null)
		{
			instances = new Hashtable<String, SyntaxGraph>();
		}

		if (!instances.containsKey(file))
		{
			final SyntaxGraph canvasFactory = new SyntaxGraph(defaultCursor, connStrategy, moveStrategy, file);
			GGLLDirector.getProject().setGrammarFile(new GrammarFile(file));
			instances.put(file, canvasFactory);
		}

		return instances.get(file);
	}

	public static Collection<SyntaxGraph> getInstances()
	{
		refresh();
		return instances.values();
	}
	
	public static void refresh()
	{
		for (final GrammarFile grammar : GGLLDirector.getProject().getGrammarFile().getAll())
		{
			getInstance(grammar.getAbsolutePath());
		}
	}
}

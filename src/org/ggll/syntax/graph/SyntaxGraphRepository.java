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

	private SyntaxGraphRepository()
	{
	}

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

	public static int getLastCustomNode()
	{
		int lastCustomNode = 0;
		for (final SyntaxGraph canvas : getInstances())
		{
			lastCustomNode += canvas.getCanvasState().getLastCustomNode();
		}
		return lastCustomNode;
	}

	public static int getLastLAMBDA()
	{
		int lastLAMBDA = 0;
		for (final SyntaxGraph canvas : getInstances())
		{
			lastLAMBDA += canvas.getCanvasState().getLastLAMBDA();
		}
		return lastLAMBDA;
	}

	public static int getLastLeftSides()
	{
		int lastLeftSides = 0;
		for (final SyntaxGraph canvas : getInstances())
		{
			lastLeftSides += canvas.getCanvasState().getLastLeftSides();
		}
		return lastLeftSides;
	}

	public static int getLastNTerminalId()
	{
		int lastNTerminalId = 0;
		for (final SyntaxGraph canvas : getInstances())
		{
			lastNTerminalId += canvas.getCanvasState().getLastNTerminalId();
		}
		return lastNTerminalId;
	}

	public static int getLastSTART()
	{
		int lastSTART = 0;
		for (final SyntaxGraph canvas : getInstances())
		{
			lastSTART += canvas.getCanvasState().getLastSTART();
		}
		return lastSTART;
	}

	public static int getLastTerminalId()
	{
		int lastTerminalId = 0;
		for (final SyntaxGraph canvas : getInstances())
		{
			lastTerminalId += canvas.getCanvasState().getLastTerminalId();
		}
		return lastTerminalId;
	}

	public static void refresh()
	{
		for (final GrammarFile grammar : GGLLDirector.getProject().getGrammarFile().getAll())
		{
			getInstance(grammar.getAbsolutePath());
		}
	}
}

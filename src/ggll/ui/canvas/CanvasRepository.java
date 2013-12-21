package ggll.ui.canvas;

import ggll.ui.director.GGLLDirector;
import ggll.ui.file.GrammarFile;
import ggll.ui.resource.CanvasResource;

import java.util.Collection;
import java.util.Hashtable;

public class CanvasRepository
{
	private static String connStrategy = CanvasResource.R_ORTHOGONAL;
	private static String defaultCursor = CanvasResource.SELECT;
	private static String moveStrategy = CanvasResource.M_FREE;

	private static Hashtable<String, Canvas> instances;

	private CanvasRepository()
	{
	}

	public static Canvas getInstance(String file)
	{
		if (instances == null)
		{
			instances = new Hashtable<String, Canvas>();
		}

		if (!instances.containsKey(file))
		{
			final Canvas canvasFactory = new Canvas(defaultCursor, connStrategy, moveStrategy, file);
			GGLLDirector.getProject().setGrammarFile(new GrammarFile(file));
			instances.put(file, canvasFactory);
		}

		return instances.get(file);
	}

	public static Collection<Canvas> getInstances()
	{
		refresh();
		return instances.values();
	}

	public static int getLastCustomNode()
	{
		int lastCustomNode = 0;
		for (final Canvas canvas : getInstances())
		{
			lastCustomNode += canvas.getCurrentCanvasState().getLastCustomNode();
		}
		return lastCustomNode;
	}

	public static int getLastLAMBDA()
	{
		int lastLAMBDA = 0;
		for (final Canvas canvas : getInstances())
		{
			lastLAMBDA += canvas.getCurrentCanvasState().getLastLAMBDA();
		}
		return lastLAMBDA;
	}

	public static int getLastLeftSides()
	{
		int lastLeftSides = 0;
		for (final Canvas canvas : getInstances())
		{
			lastLeftSides += canvas.getCurrentCanvasState().getLastLeftSides();
		}
		return lastLeftSides;
	}

	public static int getLastNTerminalId()
	{
		int lastNTerminalId = 0;
		for (final Canvas canvas : getInstances())
		{
			lastNTerminalId += canvas.getCurrentCanvasState().getLastNTerminalId();
		}
		return lastNTerminalId;
	}

	public static int getLastSTART()
	{
		int lastSTART = 0;
		for (final Canvas canvas : getInstances())
		{
			lastSTART += canvas.getCurrentCanvasState().getLastSTART();
		}
		return lastSTART;
	}

	public static int getLastTerminalId()
	{
		int lastTerminalId = 0;
		for (final Canvas canvas : getInstances())
		{
			lastTerminalId += canvas.getCurrentCanvasState().getLastTerminalId();
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
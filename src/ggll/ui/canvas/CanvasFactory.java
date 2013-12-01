package ggll.ui.canvas;

import ggll.ui.file.GrammarFile;
import ggll.ui.project.Context;
import ggll.ui.resource.CanvasResource;

import java.util.Collection;
import java.util.Hashtable;

public class CanvasFactory
{
	private static String connStrategy = CanvasResource.R_ORTHOGONAL;	
	private static String defaultCursor = CanvasResource.SELECT;
	private static String moveStrategy = CanvasResource.M_FREE;

	private static Hashtable<String, AbstractCanvas> instances;

	private CanvasFactory()
	{
	}
	
	public static int getLastCustomNode()
	{
		int lastCustomNode = 0;
		for(AbstractCanvas canvas : getInstances())
		{
			lastCustomNode += canvas.getCanvasState().getLastCustomNode();
		}
		return lastCustomNode;
	}

	public static int getLastLAMBDA()
	{
		int lastLAMBDA = 0;
		for(AbstractCanvas canvas : getInstances())
		{
			lastLAMBDA += canvas.getCanvasState().getLastLAMBDA();
		}
		return lastLAMBDA;
	}

	public static int getLastLeftSides()
	{
		int lastLeftSides = 0;
		for(AbstractCanvas canvas : getInstances())
		{
			lastLeftSides += canvas.getCanvasState().getLastLeftSides();
		}
		return lastLeftSides;
	}

	public static int getLastNTerminalId()
	{
		int lastNTerminalId = 0;
		for(AbstractCanvas canvas : getInstances())
		{
			lastNTerminalId += canvas.getCanvasState().getLastNTerminalId();
		}
		return lastNTerminalId;
	}

	public static int getLastSTART()
	{
		int lastSTART = 0;
		for(AbstractCanvas canvas : getInstances())
		{
			lastSTART += canvas.getCanvasState().getLastSTART();
		}
		return lastSTART;
	}

	public static int getLastTerminalId()
	{
		int lastTerminalId = 0;
		for(AbstractCanvas canvas : getInstances())
		{
			lastTerminalId += canvas.getCanvasState().getLastTerminalId();
		}
		return lastTerminalId;
	} 
	
	public static AbstractCanvas getInstance(String file)
	{
		if (instances == null)
		{
			instances = new Hashtable<String, AbstractCanvas>();
		}
		
		if(!instances.containsKey(file))
		{
			AbstractCanvas canvasFactory = new Canvas(defaultCursor, connStrategy, moveStrategy, file);
			Context.getProject().setGrammarFile(new GrammarFile(file));
			instances.put(file, canvasFactory);
		}
		
		return instances.get(file);
	}

	public static Collection<AbstractCanvas> getInstances()
	{
		refresh();	
		return instances.values();
	}
	
	public static void refresh()
	{
		for(GrammarFile grammar : Context.getProject().getGrammarFile().getAll())
		{
			getInstance(grammar.getAbsolutePath());
		}	
	}
}

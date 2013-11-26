package ggll.ui.canvas;

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
	
	public static AbstractCanvas getInstance(String file)
	{
		if (instances == null)
		{
			instances = new Hashtable<String, AbstractCanvas>();
		}
		
		if(!instances.containsKey(file))
		{
			AbstractCanvas canvasFactory = new Canvas(defaultCursor, connStrategy, moveStrategy, file);
			instances.put(file, canvasFactory);
		}
		
		return instances.get(file);
	}

	public static Collection<AbstractCanvas> getInstances()
	{
		return instances.values();
	}
}

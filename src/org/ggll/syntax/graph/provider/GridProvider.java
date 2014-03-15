package org.ggll.syntax.graph.provider;

import java.util.HashMap;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.widget.GridWidget;

public class GridProvider
{
	
	private static HashMap<SyntaxGraph, GridProvider> gridProviders = new HashMap<SyntaxGraph, GridProvider>();
	
	public static GridProvider getInstance(final SyntaxGraph canvas)
	{
		if (!GridProvider.gridProviders.containsKey(canvas))
		{
			GridProvider.gridProviders.put(canvas, new GridProvider(canvas));
		}
		return GridProvider.gridProviders.get(canvas);
	}
	
	private final SyntaxGraph canvas;
	
	private GridWidget grid;
	
	private GridProvider(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	public boolean isVisible()
	{
		if (grid == null) { return false; }
		return grid.isVisible();
	}
	
	public void setVisible(final boolean visible)
	{
		if (grid != null)
		{
			grid.setVisible(visible);
			canvas.setShowingGrid(visible);
		}
		else if (visible)
		{
			grid = (GridWidget) canvas.addNode(GridWidget.class.getCanonicalName());
		}
		canvas.repaint();
	}
}

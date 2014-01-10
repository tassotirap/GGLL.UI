package org.ggll.syntax.graph.provider;

import java.util.HashMap;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.widget.GridWidget;

public class GridProvider
{

	private static HashMap<SyntaxGraph, GridProvider> gridProviders = new HashMap<SyntaxGraph, GridProvider>();
	private final SyntaxGraph canvas;
	private GridWidget grid;

	private GridProvider(SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}

	public static GridProvider getInstance(SyntaxGraph canvas)
	{
		if (!gridProviders.containsKey(canvas))
		{
			gridProviders.put(canvas, new GridProvider(canvas));
		}
		return gridProviders.get(canvas);
	}

	public boolean isVisible()
	{
		if (this.grid == null)
		{
			return false;
		}
		return this.grid.isVisible();
	}

	public void setVisible(boolean visible)
	{
		if (this.grid != null)
		{
			this.grid.setVisible(visible);
			this.canvas.setShowingGrid(visible);
		}
		else if (visible)
		{
			this.grid = (GridWidget) this.canvas.addNode(GridWidget.class.getCanonicalName());
		}
		this.canvas.repaint();
	}
}

package ggll.ui.canvas.provider;

import ggll.ui.canvas.Canvas;
import ggll.ui.canvas.widget.GridWidget;

import java.util.HashMap;

public class GridProvider
{

	private static HashMap<Canvas, GridProvider> gridProviders = new HashMap<Canvas, GridProvider>();
	private final Canvas canvas;
	private GridWidget grid;

	private GridProvider(Canvas canvas)
	{
		this.canvas = canvas;
	}

	public static GridProvider getInstance(Canvas canvas)
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

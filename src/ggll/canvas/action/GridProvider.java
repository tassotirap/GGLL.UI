package ggll.canvas.action;

import ggll.canvas.AbstractCanvas;
import ggll.canvas.widget.GridWidget;

import java.util.HashMap;

public class GridProvider
{

	private static HashMap<AbstractCanvas, GridProvider> gridProviders = new HashMap<AbstractCanvas, GridProvider>();
	private AbstractCanvas canvas;
	private GridWidget grid;

	private GridProvider(AbstractCanvas canvas)
	{
		this.canvas = canvas;
	}

	public static GridProvider getInstance(AbstractCanvas canvas)
	{
		if (!gridProviders.containsKey(canvas))
		{
			gridProviders.put(canvas, new GridProvider(canvas));
		}
		return gridProviders.get(canvas);
	}

	public boolean isVisible()
	{
		if (grid == null)
		{
			return false;
		}
		return grid.isVisible();
	}

	public void setVisible(boolean visible)
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

package ggll.canvas.provider;

import ggll.canvas.AbstractCanvas;

import java.awt.Point;

import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;

public abstract class CanvasSelectProvider implements SelectProvider
{
	private AbstractCanvas canvas;

	public CanvasSelectProvider(AbstractCanvas canvas)
	{
		this.canvas = canvas;
	}

	@Override
	public void select(Widget arg0, Point arg1, boolean arg2)
	{
		canvas.setFocused();
	}

}

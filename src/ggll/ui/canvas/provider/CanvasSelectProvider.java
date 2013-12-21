package ggll.ui.canvas.provider;

import ggll.ui.canvas.Canvas;

import java.awt.Point;

import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;

public abstract class CanvasSelectProvider implements SelectProvider
{
	private final Canvas canvas;

	public CanvasSelectProvider(Canvas canvas)
	{
		this.canvas = canvas;
	}

	@Override
	public void select(Widget arg0, Point arg1, boolean arg2)
	{
		this.canvas.setFocused();
	}

}

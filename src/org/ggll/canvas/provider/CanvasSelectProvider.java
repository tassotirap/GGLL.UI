package org.ggll.canvas.provider;

import java.awt.Point;

import org.ggll.canvas.Canvas;
import org.ggll.canvas.CanvasFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;

public abstract class CanvasSelectProvider implements SelectProvider
{
	public CanvasSelectProvider(Canvas canvas)
	{
	}

	@Override
	public void select(Widget arg0, Point arg1, boolean arg2)
	{
		CanvasFactory.getCanvas().setFocused(); // caso ainda não tenha
												// sido focado
	}

}

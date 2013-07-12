package ggll.canvas.provider;

import ggll.canvas.AbstractCanvas;
import ggll.canvas.CanvasFactory;

import java.awt.Point;

import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;

public abstract class CanvasSelectProvider implements SelectProvider
{
	public CanvasSelectProvider(AbstractCanvas canvas)
	{
	}

	@Override
	public void select(Widget arg0, Point arg1, boolean arg2)
	{
		CanvasFactory.getCanvas().setFocused(); // caso ainda não tenha
												// sido focado
	}

}

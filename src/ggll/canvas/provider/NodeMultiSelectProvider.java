package ggll.canvas.provider;

import ggll.canvas.Canvas;
import ggll.canvas.CanvasFactory;

import java.awt.Color;
import java.awt.Point;
import java.util.Collections;

import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;

public final class NodeMultiSelectProvider implements SelectProvider
{
	public NodeMultiSelectProvider(Canvas canvas)
	{
	}

	@Override
	public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection)
	{
		return false;
	}

	@Override
	public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection)
	{
		Canvas canvas = CanvasFactory.getCanvas();
		return canvas.findObject(widget) != null;
	}

	@Override
	public void select(Widget widget, Point localLocation, boolean invertSelection)
	{
		Canvas canvas = CanvasFactory.getCanvas();
		Object object = canvas.findObject(widget);
		canvas.setFocusedObject(object);
		if (object != null)
		{
			if (!invertSelection && canvas.getSelectedObjects().contains(object))
			{
				widget.setForeground(Color.WHITE);
				widget.setBackground(Color.BLUE);
				return;
			}
			canvas.userSelectionSuggested(Collections.singleton(object), invertSelection);
		}
		else
		{
			canvas.userSelectionSuggested(Collections.emptySet(), invertSelection);
		}
	}
}

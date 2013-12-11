package ggll.ui.canvas.provider;

import ggll.ui.canvas.Canvas;

import java.awt.Color;
import java.awt.Point;
import java.util.Collections;

import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;

public final class NodeMultiSelectProvider implements SelectProvider
{
	private Canvas canvas;

	public NodeMultiSelectProvider(Canvas canvas)
	{
		this.canvas = canvas;
	}

	@Override
	public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection)
	{
		return false;
	}

	@Override
	public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection)
	{
		return canvas.findObject(widget) != null;
	}

	@Override
	public void select(Widget widget, Point localLocation, boolean invertSelection)
	{
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

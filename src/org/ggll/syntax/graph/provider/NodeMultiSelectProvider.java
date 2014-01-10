package org.ggll.syntax.graph.provider;

import java.awt.Color;
import java.awt.Point;
import java.util.Collections;

import org.ggll.syntax.graph.SyntaxGraph;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;

public final class NodeMultiSelectProvider implements SelectProvider
{
	private final SyntaxGraph canvas;

	public NodeMultiSelectProvider(SyntaxGraph canvas)
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
		return this.canvas.findObject(widget) != null;
	}

	@Override
	public void select(Widget widget, Point localLocation, boolean invertSelection)
	{
		final Object object = this.canvas.findObject(widget);
		this.canvas.setFocusedObject(object);
		if (object != null)
		{
			if (!invertSelection && this.canvas.getSelectedObjects().contains(object))
			{
				widget.setForeground(Color.WHITE);
				widget.setBackground(Color.BLUE);
				return;
			}
			this.canvas.userSelectionSuggested(Collections.singleton(object), invertSelection);
		}
		else
		{
			this.canvas.userSelectionSuggested(Collections.emptySet(), invertSelection);
		}
	}
}

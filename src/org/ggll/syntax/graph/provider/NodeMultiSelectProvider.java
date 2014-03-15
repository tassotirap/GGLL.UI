package org.ggll.syntax.graph.provider;

import java.awt.Color;
import java.awt.Point;

import org.ggll.syntax.graph.SyntaxGraph;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;

public final class NodeMultiSelectProvider implements SelectProvider
{
	private final SyntaxGraph canvas;
	
	public NodeMultiSelectProvider(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	@Override
	public boolean isAimingAllowed(final Widget widget, final Point localLocation, final boolean invertSelection)
	{
		return false;
	}
	
	@Override
	public boolean isSelectionAllowed(final Widget widget, final Point localLocation, final boolean invertSelection)
	{
		return canvas.findObject(widget) != null;
	}
	
	@Override
	public void select(final Widget widget, final Point localLocation, final boolean invertSelection)
	{
		final Object object = canvas.findObject(widget);
		canvas.setFocusedObject(object);
		if (object != null)
		{
			if (!invertSelection)
			{
				widget.setForeground(Color.WHITE);
				widget.setBackground(Color.BLUE);
				return;
			}
			else
			{
				widget.setForeground(Color.WHITE);
				widget.setBackground(Color.WHITE);
				return;
			}
		}
		canvas.repaint();
	}
}

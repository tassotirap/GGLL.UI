package org.ggll.syntax.graph.provider;

import java.awt.Color;
import java.awt.Point;
import java.util.Collections;

import org.ggll.syntax.graph.SyntaxGraph;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class NodeSelectProvider extends CanvasSelectProvider
{
	private final SyntaxGraph canvas;
	
	public NodeSelectProvider(final SyntaxGraph canvas)
	{
		super(canvas);
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
		super.select(widget, localLocation, invertSelection);
		final Object object = canvas.findObject(widget);
		canvas.setFocusedObject(object);
		if (object != null)
		{
			if (!invertSelection && canvas.getSelectedObjects().contains(object)) { return; }
			canvas.userSelectionSuggested(Collections.singleton(object), invertSelection);
			for (final Object o : canvas.getLabels())
			{
				final LabelWidget lw = (LabelWidget) canvas.findWidget(o);
				lw.setBorder(BorderFactory.createEmptyBorder());
			}
			for (final Object o : canvas.getNodes())
			{
				final Widget lw = canvas.findWidget(o);
				if (lw instanceof LabelWidget)
				{
					lw.setBackground(Color.WHITE);
					lw.setForeground(Color.BLACK);
				}
			}
			for (final Object o : canvas.getSelectedObjects())
			{
				if (canvas.isNode(o) || canvas.isLabel(o))
				{
					final Widget lw = canvas.findWidget(o);
					if (canvas.isLabel(o))
					{
						lw.setForeground(Color.BLUE);
						((LabelWidget) canvas.findWidget(o)).setBorder(BorderFactory.createLineBorder(1, Color.BLUE));
					}
					else if (lw instanceof LabelWidget)
					{
						lw.setBackground(Color.BLUE);
						lw.setForeground(Color.WHITE);
					}
				}
			}
		}
		else
		{
			canvas.userSelectionSuggested(Collections.emptySet(), invertSelection);
		}
	}
}
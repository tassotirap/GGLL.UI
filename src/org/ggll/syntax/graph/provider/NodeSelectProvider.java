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

	public NodeSelectProvider(SyntaxGraph canvas)
	{
		super(canvas);
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
		super.select(widget, localLocation, invertSelection);
		final Object object = this.canvas.findObject(widget);
		this.canvas.setFocusedObject(object);
		if (object != null)
		{
			if (!invertSelection && this.canvas.getSelectedObjects().contains(object))
			{
				return;
			}
			this.canvas.userSelectionSuggested(Collections.singleton(object), invertSelection);
			for (final Object o : this.canvas.getLabels())
			{
				final LabelWidget lw = (LabelWidget) this.canvas.findWidget(o);
				lw.setBorder(BorderFactory.createEmptyBorder());
			}
			for (final Object o : this.canvas.getNodes())
			{
				final Widget lw = this.canvas.findWidget(o);
				if (lw instanceof LabelWidget)
				{
					lw.setBackground(Color.WHITE);
					lw.setForeground(Color.BLACK);
				}
			}
			for (final Object o : this.canvas.getSelectedObjects())
			{
				if (this.canvas.isNode(o) || this.canvas.isLabel(o))
				{
					final Widget lw = this.canvas.findWidget(o);
					if (this.canvas.isLabel(o))
					{
						lw.setForeground(Color.BLUE);
						((LabelWidget) this.canvas.findWidget(o)).setBorder(BorderFactory.createLineBorder(1, Color.BLUE));
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
			this.canvas.userSelectionSuggested(Collections.emptySet(), invertSelection);
		}
	}
}
package org.ggll.syntax.graph.provider;


import java.awt.Color;
import java.awt.Point;
import java.util.Collections;

import org.ggll.syntax.graph.SyntaxGraph;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class LabelSelectProvider extends CanvasSelectProvider
{
	private final SyntaxGraph canvas;

	public LabelSelectProvider(SyntaxGraph canvas)
	{
		super(canvas);
		this.canvas = canvas;
	}

	@Override
	public boolean isAimingAllowed(Widget arg0, Point arg1, boolean arg2)
	{
		return false;
	}

	@Override
	public boolean isSelectionAllowed(Widget widget, Point arg1, boolean arg2)
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
		}
		else
		{
			this.canvas.userSelectionSuggested(Collections.emptySet(), invertSelection);
		}
		for (final Object o : this.canvas.getLabels())
		{
			final LabelWidget lw = (LabelWidget) this.canvas.findWidget(o);
			lw.setBorder(BorderFactory.createEmptyBorder());
		}
		for (final Object o : this.canvas.getNodes())
		{
			final LabelWidget lw = (LabelWidget) this.canvas.findWidget(o);
			lw.setBackground(Color.WHITE);
			lw.setForeground(Color.BLACK);
		}
		for (final Object o : this.canvas.getSelectedObjects())
		{
			if (this.canvas.isLabel(o))
			{
				final LabelWidget lw = (LabelWidget) this.canvas.findWidget(o);
				lw.setForeground(Color.BLUE);
				((LabelWidget) this.canvas.findWidget(o)).setBorder(BorderFactory.createLineBorder(1, Color.BLUE));
			}
		}
	}

}
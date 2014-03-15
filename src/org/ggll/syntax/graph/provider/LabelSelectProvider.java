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
	
	public LabelSelectProvider(final SyntaxGraph canvas)
	{
		super(canvas);
		this.canvas = canvas;
	}
	
	@Override
	public boolean isAimingAllowed(final Widget arg0, final Point arg1, final boolean arg2)
	{
		return false;
	}
	
	@Override
	public boolean isSelectionAllowed(final Widget widget, final Point arg1, final boolean arg2)
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
		}
		else
		{
			canvas.userSelectionSuggested(Collections.emptySet(), invertSelection);
		}
		for (final Object o : canvas.getLabels())
		{
			final LabelWidget lw = (LabelWidget) canvas.findWidget(o);
			lw.setBorder(BorderFactory.createEmptyBorder());
		}
		for (final Object o : canvas.getNodes())
		{
			final Widget lw = canvas.findWidget(o);
			lw.setBackground(Color.WHITE);
			lw.setForeground(Color.BLACK);
		}
		for (final Object o : canvas.getSelectedObjects())
		{
			if (canvas.isLabel(o))
			{
				final LabelWidget lw = (LabelWidget) canvas.findWidget(o);
				lw.setForeground(Color.BLUE);
				((LabelWidget) canvas.findWidget(o)).setBorder(BorderFactory.createLineBorder(1, Color.BLUE));
			}
		}
	}
	
}
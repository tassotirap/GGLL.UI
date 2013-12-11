package ggll.ui.canvas.provider;

import ggll.ui.canvas.Canvas;

import java.awt.Color;
import java.awt.Point;
import java.util.Collections;

import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class NodeSelectProvider extends CanvasSelectProvider
{
	private Canvas canvas;

	public NodeSelectProvider(Canvas canvas)
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
		return canvas.findObject(widget) != null;
	}

	@Override
	public void select(Widget widget, Point localLocation, boolean invertSelection)
	{
		super.select(widget, localLocation, invertSelection);
		Object object = canvas.findObject(widget);
		canvas.setFocusedObject(object);
		if (object != null)
		{
			if (!invertSelection && canvas.getSelectedObjects().contains(object))
			{
				return;
			}
			canvas.userSelectionSuggested(Collections.singleton(object), invertSelection);
			for (Object o : canvas.getLabels())
			{
				LabelWidget lw = ((LabelWidget) canvas.findWidget(o));
				lw.setBorder(BorderFactory.createEmptyBorder());
			}
			for (Object o : canvas.getNodes())
			{
				Widget lw = canvas.findWidget(o);
				if (lw instanceof LabelWidget)
				{
					lw.setBackground(Color.WHITE);
					lw.setForeground(Color.BLACK);
				}
			}
			for (Object o : canvas.getSelectedObjects())
			{
				if (canvas.isNode(o) || canvas.isLabel(o))
				{
					Widget lw = canvas.findWidget(o);
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
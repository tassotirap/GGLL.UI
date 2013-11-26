package ggll.ui.canvas.provider;

import ggll.ui.canvas.AbstractCanvas;

import java.awt.Color;
import java.awt.Point;
import java.util.Collections;

import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class LabelSelectProvider extends CanvasSelectProvider
{
	private AbstractCanvas canvas;

	public LabelSelectProvider(AbstractCanvas canvas)
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
		}
		else
		{
			canvas.userSelectionSuggested(Collections.emptySet(), invertSelection);
		}
		for (Object o : canvas.getLabels())
		{
			LabelWidget lw = ((LabelWidget) canvas.findWidget(o));
			lw.setBorder(BorderFactory.createEmptyBorder());
		}
		for (Object o : canvas.getNodes())
		{
			LabelWidget lw = ((LabelWidget) canvas.findWidget(o));
			lw.setBackground(Color.WHITE);
			lw.setForeground(Color.BLACK);
		}
		for (Object o : canvas.getSelectedObjects())
		{
			if (canvas.isLabel(o))
			{
				LabelWidget lw = ((LabelWidget) canvas.findWidget(o));
				lw.setForeground(Color.BLUE);
				((LabelWidget) canvas.findWidget(o)).setBorder(BorderFactory.createLineBorder(1, Color.BLUE));
			}
		}
	}

}
package org.ggll.canvas.provider;

import java.awt.Color;

import org.ggll.canvas.Canvas;
import org.ggll.canvas.CanvasFactory;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class LabelHoverProvider implements TwoStateHoverProvider
{

	public LabelHoverProvider(Canvas canvas)
	{

	}

	@Override
	public void setHovering(Widget widget)
	{
		if (widget != null)
		{
			((LabelWidget) widget).setBorder(BorderFactory.createLineBorder(1, Color.BLUE));
		}

	}

	@Override
	public void unsetHovering(Widget widget)
	{
		if (widget != null)
		{
			if (!CanvasFactory.getCanvas().getSelectedObjects().contains(CanvasFactory.getCanvas().findObject(widget)))
			{
				((LabelWidget) widget).setBorder(BorderFactory.createEmptyBorder());
			}
		}

	}

}

package ggll.ui.canvas.provider;

import ggll.ui.canvas.Canvas;

import java.awt.Color;

import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class LabelHoverProvider implements TwoStateHoverProvider
{
	private final Canvas canvas;

	public LabelHoverProvider(Canvas canvas)
	{
		this.canvas = canvas;
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
			if (!this.canvas.getSelectedObjects().contains(this.canvas.findObject(widget)))
			{
				((LabelWidget) widget).setBorder(BorderFactory.createEmptyBorder());
			}
		}
	}

}

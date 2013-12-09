package ggll.ui.canvas.provider;

import ggll.ui.canvas.Canvas;
import ggll.ui.canvas.widget.MarkedWidget;

import java.awt.Color;

import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.widget.Widget;

public class NodeHoverProvider implements TwoStateHoverProvider
{
	Canvas canvas;
	
	public NodeHoverProvider(Canvas canvas)
	{
		this.canvas = canvas;
	}

	@Override
	public void setHovering(Widget widget)
	{
		if (widget != null)
		{
			widget.setBackground(Color.BLUE);
			widget.setForeground(Color.WHITE);
		}
	}

	@Override
	public void unsetHovering(Widget widget)
	{
		Object object = canvas.findObject(widget);

		if (object != null & widget != null && !canvas.getSelectedObjects().contains(object))
		{
			if (widget instanceof MarkedWidget)
			{
				((MarkedWidget) widget).setMarkBackground(MarkedWidget.DEFAULT_MARK_BACKGROUND);
				((MarkedWidget) widget).setMarkForeground(MarkedWidget.DEFAULT_MARK_FOREGROUND);
			}
			widget.setBackground(Color.WHITE);
			widget.setForeground(Color.BLACK);
		}
	}

}
package org.ggll.syntax.graph.provider;

import java.awt.Color;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.widget.MarkedWidget;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.widget.Widget;

public class NodeHoverProvider implements TwoStateHoverProvider
{
	SyntaxGraph canvas;
	
	public NodeHoverProvider(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	@Override
	public void setHovering(final Widget widget)
	{
		if (widget != null)
		{
			widget.setBackground(Color.BLUE);
			widget.setForeground(Color.WHITE);
		}
	}
	
	@Override
	public void unsetHovering(final Widget widget)
	{
		final Object object = canvas.findObject(widget);
		
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
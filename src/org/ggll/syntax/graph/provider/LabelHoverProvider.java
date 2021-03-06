package org.ggll.syntax.graph.provider;

import java.awt.Color;

import org.ggll.syntax.graph.SyntaxGraph;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class LabelHoverProvider implements TwoStateHoverProvider
{
	private final SyntaxGraph canvas;
	
	public LabelHoverProvider(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	@Override
	public void setHovering(final Widget widget)
	{
		if (widget != null)
		{
			((LabelWidget) widget).setBorder(BorderFactory.createLineBorder(1, Color.BLUE));
		}
	}
	
	@Override
	public void unsetHovering(final Widget widget)
	{
		if (widget != null)
		{
			if (!canvas.getSelectedObjects().contains(canvas.findObject(widget)))
			{
				((LabelWidget) widget).setBorder(BorderFactory.createEmptyBorder());
			}
		}
	}
	
}

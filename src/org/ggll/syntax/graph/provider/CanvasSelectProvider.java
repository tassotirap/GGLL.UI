package org.ggll.syntax.graph.provider;

import java.awt.Point;

import org.ggll.syntax.graph.SyntaxGraph;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;

public abstract class CanvasSelectProvider implements SelectProvider
{
	private final SyntaxGraph canvas;
	
	public CanvasSelectProvider(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	@Override
	public void select(final Widget arg0, final Point arg1, final boolean arg2)
	{
		canvas.setFocused();
	}
	
}

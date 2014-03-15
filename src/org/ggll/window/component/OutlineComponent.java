package org.ggll.window.component;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.SyntaxTopComponent;

public class OutlineComponent extends AbstractComponent
{
	public OutlineComponent(final SyntaxGraph canvas)
	{
		SyntaxTopComponent.getInstance().setCanvas(canvas);
		jComponent = SyntaxTopComponent.getInstance();
	}
	
	@Override
	public void fireContentChanged()
	{
	}
	
}

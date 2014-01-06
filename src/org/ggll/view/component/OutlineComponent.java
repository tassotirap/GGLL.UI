package org.ggll.view.component;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.SyntaxTopComponent;

public class OutlineComponent extends AbstractComponent
{
	public OutlineComponent(SyntaxGraph canvas)
	{
		SyntaxTopComponent.getInstance().setCanvas(canvas);
		this.jComponent = SyntaxTopComponent.getInstance();
	}

	@Override
	public void fireContentChanged()
	{
	}

}

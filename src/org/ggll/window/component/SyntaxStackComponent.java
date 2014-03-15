package org.ggll.window.component;

import javax.swing.JScrollPane;

import org.ggll.output.SyntaxStack;
import org.ggll.syntax.graph.SyntaxGraph;

public class SyntaxStackComponent extends AbstractComponent
{
	public SyntaxStackComponent(final SyntaxGraph canvas)
	{
		jComponent = new JScrollPane(SyntaxStack.getInstance().getView(canvas));
	}
	
	@Override
	public void fireContentChanged()
	{
	}
	
}

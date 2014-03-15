package org.ggll.window.component;

import javax.swing.JScrollPane;

import org.ggll.output.SemanticStack;
import org.ggll.syntax.graph.SyntaxGraph;

public class SemanticStackComponent extends AbstractComponent
{
	public SemanticStackComponent(final SyntaxGraph canvas)
	{
		jComponent = new JScrollPane(SemanticStack.getInstance().getView(canvas));
	}
	
	@Override
	public void fireContentChanged()
	{
	}
	
}

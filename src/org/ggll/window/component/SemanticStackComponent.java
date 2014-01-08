package org.ggll.window.component;


import javax.swing.JScrollPane;

import org.ggll.output.SemanticStack;
import org.ggll.syntax.graph.SyntaxGraph;

public class SemanticStackComponent extends AbstractComponent
{
	public SemanticStackComponent(SyntaxGraph canvas)
	{
		this.jComponent = new JScrollPane(SemanticStack.getInstance().getView(canvas));
	}

	@Override
	public void fireContentChanged()
	{
	}

}

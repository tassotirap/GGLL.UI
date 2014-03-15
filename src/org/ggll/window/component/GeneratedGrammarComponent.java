package org.ggll.window.component;

import javax.swing.JScrollPane;

import org.ggll.output.GeneratedGrammar;
import org.ggll.syntax.graph.SyntaxGraph;

public class GeneratedGrammarComponent extends AbstractComponent
{
	public GeneratedGrammarComponent(final SyntaxGraph canvas)
	{
		jComponent = new JScrollPane(GeneratedGrammar.getInstance().getView(canvas));
	}
	
	@Override
	public void fireContentChanged()
	{
	}
	
}

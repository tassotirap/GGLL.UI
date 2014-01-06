package org.ggll.view.component;


import javax.swing.JScrollPane;

import org.ggll.output.GeneratedGrammar;
import org.ggll.syntax.graph.SyntaxGraph;

public class GeneratedGrammarComponent extends AbstractComponent
{
	public GeneratedGrammarComponent(SyntaxGraph canvas)
	{
		this.jComponent = new JScrollPane(GeneratedGrammar.getInstance().getView(canvas));
	}

	@Override
	public void fireContentChanged()
	{
	}

}

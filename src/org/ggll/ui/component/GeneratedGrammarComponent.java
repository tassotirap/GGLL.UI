package org.ggll.ui.component;

import javax.swing.JScrollPane;

import org.ggll.canvas.Canvas;
import org.ggll.output.GeneratedGrammar;

public class GeneratedGrammarComponent extends AbstractComponent
{
	public GeneratedGrammarComponent(Canvas canvas)
	{
		GeneratedGrammar gg = GeneratedGrammar.getInstance();
		jComponent = new JScrollPane(gg.getView(canvas));
	}

	@Override
	public void fireContentChanged()
	{
	}

}

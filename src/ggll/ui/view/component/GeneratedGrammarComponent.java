package ggll.ui.view.component;

import ggll.ui.canvas.Canvas;
import ggll.ui.output.GeneratedGrammar;

import javax.swing.JScrollPane;

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

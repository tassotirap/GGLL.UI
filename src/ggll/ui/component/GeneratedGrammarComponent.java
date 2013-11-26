package ggll.ui.component;

import ggll.ui.canvas.AbstractCanvas;
import ggll.ui.output.GeneratedGrammar;

import javax.swing.JScrollPane;

public class GeneratedGrammarComponent extends AbstractComponent
{
	public GeneratedGrammarComponent(AbstractCanvas canvas)
	{
		GeneratedGrammar gg = GeneratedGrammar.getInstance();
		jComponent = new JScrollPane(gg.getView(canvas));
	}

	@Override
	public void fireContentChanged()
	{
	}

}

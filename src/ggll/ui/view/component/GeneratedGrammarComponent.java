package ggll.ui.view.component;

import ggll.ui.canvas.Canvas;
import ggll.ui.output.GeneratedGrammar;

import javax.swing.JScrollPane;

public class GeneratedGrammarComponent extends AbstractComponent
{
	public GeneratedGrammarComponent(Canvas canvas)
	{
		this.jComponent = new JScrollPane(GeneratedGrammar.getInstance().getView(canvas));
	}

	@Override
	public void fireContentChanged()
	{
	}

}

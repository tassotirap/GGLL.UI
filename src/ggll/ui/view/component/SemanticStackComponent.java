package ggll.ui.view.component;

import ggll.ui.canvas.Canvas;
import ggll.ui.output.SemanticStack;

import javax.swing.JScrollPane;

public class SemanticStackComponent extends AbstractComponent
{
	public SemanticStackComponent(Canvas canvas)
	{
		this.jComponent = new JScrollPane(SemanticStack.getInstance().getView(canvas));
	}

	@Override
	public void fireContentChanged()
	{
	}

}

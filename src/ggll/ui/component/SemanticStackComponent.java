package ggll.ui.component;

import ggll.canvas.Canvas;
import ggll.output.SemanticStack;

import javax.swing.JScrollPane;

public class SemanticStackComponent extends AbstractComponent
{
	public SemanticStackComponent(Canvas canvas)
	{
		jComponent = new JScrollPane(SemanticStack.getInstance().getView(canvas));
	}

	@Override
	public void fireContentChanged()
	{
	}

}

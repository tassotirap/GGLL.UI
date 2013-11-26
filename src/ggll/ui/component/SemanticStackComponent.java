package ggll.ui.component;

import ggll.ui.canvas.AbstractCanvas;
import ggll.ui.output.SemanticStack;

import javax.swing.JScrollPane;

public class SemanticStackComponent extends AbstractComponent
{
	public SemanticStackComponent(AbstractCanvas canvas)
	{
		jComponent = new JScrollPane(SemanticStack.getInstance().getView(canvas));
	}

	@Override
	public void fireContentChanged()
	{
	}

}

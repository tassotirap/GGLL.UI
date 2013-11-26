package ggll.ui.component;

import ggll.ui.canvas.AbstractCanvas;
import ggll.ui.output.SyntaxStack;

import javax.swing.JScrollPane;

public class SyntaxStackComponent extends AbstractComponent
{
	public SyntaxStackComponent(AbstractCanvas canvas)
	{
		jComponent = new JScrollPane(SyntaxStack.getInstance().getView(canvas));
	}

	@Override
	public void fireContentChanged()
	{
	}

}

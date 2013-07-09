package ggll.ui.component;

import ggll.canvas.Canvas;
import ggll.output.SyntaxStack;

import javax.swing.JScrollPane;

public class SyntaxStackComponent extends AbstractComponent
{
	public SyntaxStackComponent(Canvas canvas)
	{
		jComponent = new JScrollPane(SyntaxStack.getInstance().getView(canvas));
	}

	@Override
	public void fireContentChanged()
	{
	}

}

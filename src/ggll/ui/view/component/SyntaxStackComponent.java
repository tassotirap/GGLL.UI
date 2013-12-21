package ggll.ui.view.component;

import ggll.ui.canvas.Canvas;
import ggll.ui.output.SyntaxStack;

import javax.swing.JScrollPane;

public class SyntaxStackComponent extends AbstractComponent
{
	public SyntaxStackComponent(Canvas canvas)
	{
		this.jComponent = new JScrollPane(SyntaxStack.getInstance().getView(canvas));
	}

	@Override
	public void fireContentChanged()
	{
	}

}

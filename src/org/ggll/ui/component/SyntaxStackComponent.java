package org.ggll.ui.component;

import javax.swing.JScrollPane;

import org.ggll.canvas.Canvas;
import org.ggll.output.SyntaxStack;

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

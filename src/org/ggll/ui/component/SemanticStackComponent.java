package org.ggll.ui.component;

import javax.swing.JScrollPane;

import org.ggll.canvas.Canvas;
import org.ggll.output.SemanticStack;

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

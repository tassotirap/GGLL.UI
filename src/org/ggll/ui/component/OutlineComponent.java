package org.ggll.ui.component;

import org.ggll.canvas.Canvas;
import org.ggll.canvas.OutlineTopComponent;

public class OutlineComponent extends AbstractComponent
{
	public OutlineComponent(Canvas canvas)
	{
		jComponent = new OutlineTopComponent(canvas);
	}

	@Override
	public void fireContentChanged()
	{
	}

}

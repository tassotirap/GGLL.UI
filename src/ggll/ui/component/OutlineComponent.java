package ggll.ui.component;

import ggll.canvas.Canvas;
import ggll.canvas.OutlineTopComponent;

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

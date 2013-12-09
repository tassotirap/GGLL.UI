package ggll.ui.view.component;

import ggll.ui.canvas.Canvas;
import ggll.ui.canvas.OutlineTopComponent;

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

package ggll.ui.component;

import ggll.ui.canvas.AbstractCanvas;
import ggll.ui.canvas.OutlineTopComponent;

public class OutlineComponent extends AbstractComponent
{
	public OutlineComponent(AbstractCanvas canvas)
	{
		jComponent = new OutlineTopComponent(canvas);
	}

	@Override
	public void fireContentChanged()
	{
	}

}

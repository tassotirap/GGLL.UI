package ggll.ui.component;

import ggll.canvas.AbstractCanvas;
import ggll.canvas.OutlineTopComponent;

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

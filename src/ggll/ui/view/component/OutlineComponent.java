package ggll.ui.view.component;

import ggll.ui.canvas.Canvas;
import ggll.ui.canvas.OutlineTopComponent;

public class OutlineComponent extends AbstractComponent
{
	public OutlineComponent(Canvas canvas)
	{
		OutlineTopComponent.getInstance().setCanvas(canvas);		
		jComponent = OutlineTopComponent.getInstance();
	}

	@Override
	public void fireContentChanged()
	{
	}

}

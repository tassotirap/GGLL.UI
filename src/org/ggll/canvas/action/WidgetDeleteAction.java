package org.ggll.canvas.action;

import java.awt.event.KeyEvent;

import org.ggll.canvas.provider.WidgetDeleteProvider;
import org.netbeans.api.visual.action.WidgetAction.Adapter;
import org.netbeans.api.visual.widget.Widget;

public class WidgetDeleteAction extends Adapter
{
	WidgetDeleteProvider widgetDeleteProvider;
	public WidgetDeleteAction(WidgetDeleteProvider widgetDeleteProvider)
	{
		this.widgetDeleteProvider = widgetDeleteProvider;
	}

	@Override
	public State keyReleased(Widget widget, WidgetKeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.VK_DELETE)
		{
			if (widgetDeleteProvider.isDeletionAllowed())
			{
				widgetDeleteProvider.deleteSelected();
			}
		}
		return State.REJECTED;
	}

}

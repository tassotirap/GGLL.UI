package org.ggll.syntax.graph.adapter;

import java.awt.event.KeyEvent;

import org.ggll.syntax.graph.provider.WidgetDeleteProvider;
import org.netbeans.api.visual.action.WidgetAction.Adapter;
import org.netbeans.api.visual.widget.Widget;

public class DeleteAdapter extends Adapter
{
	WidgetDeleteProvider widgetDeleteProvider;

	public DeleteAdapter(WidgetDeleteProvider widgetDeleteProvider)
	{
		this.widgetDeleteProvider = widgetDeleteProvider;
	}

	@Override
	public State keyReleased(Widget widget, WidgetKeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.VK_DELETE)
		{
			if (this.widgetDeleteProvider.isDeletionAllowed())
			{
				this.widgetDeleteProvider.deleteSelected();
			}
		}
		return State.REJECTED;
	}

}

package org.ggll.syntax.graph.adapter;

import java.awt.event.KeyEvent;

import org.ggll.syntax.graph.provider.WidgetDeleteProvider;
import org.netbeans.api.visual.action.WidgetAction.Adapter;
import org.netbeans.api.visual.widget.Widget;

public class DeleteAdapter extends Adapter
{
	WidgetDeleteProvider widgetDeleteProvider;
	
	public DeleteAdapter(final WidgetDeleteProvider widgetDeleteProvider)
	{
		this.widgetDeleteProvider = widgetDeleteProvider;
	}
	
	@Override
	public State keyReleased(final Widget widget, final WidgetKeyEvent event)
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

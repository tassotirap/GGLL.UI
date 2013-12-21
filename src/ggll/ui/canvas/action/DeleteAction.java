package ggll.ui.canvas.action;

import ggll.ui.canvas.provider.WidgetDeleteProvider;

import java.awt.event.KeyEvent;

import org.netbeans.api.visual.action.WidgetAction.Adapter;
import org.netbeans.api.visual.widget.Widget;

public class DeleteAction extends Adapter
{
	WidgetDeleteProvider widgetDeleteProvider;

	public DeleteAction(WidgetDeleteProvider widgetDeleteProvider)
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

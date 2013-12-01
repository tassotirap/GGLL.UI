package ggll.ui.canvas.action;

import ggll.ui.canvas.provider.WidgetCopyPasteProvider;

import java.awt.event.KeyEvent;

import org.netbeans.api.visual.action.WidgetAction.Adapter;
import org.netbeans.api.visual.widget.Widget;

public class CopyPasteAction extends Adapter
{
	WidgetCopyPasteProvider copyPasteProvider;

	public CopyPasteAction(WidgetCopyPasteProvider copyPasteProvider)
	{
		this.copyPasteProvider = copyPasteProvider;
	}

	@Override
	public State keyPressed(Widget widget, WidgetKeyEvent event)
	{
		if (event.isControlDown())
		{
			if (event.getKeyCode() == KeyEvent.VK_C)
			{
				copyPasteProvider.copySelected();
			}
			else if (event.getKeyCode() == KeyEvent.VK_X)
			{
				copyPasteProvider.cutSelected(null);
			}
			else if (event.getKeyCode() == KeyEvent.VK_V)
			{
				copyPasteProvider.paste(null);
			}
		}
		return State.REJECTED;
	}
}

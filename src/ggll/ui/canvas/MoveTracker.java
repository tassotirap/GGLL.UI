package ggll.ui.canvas;

import ggll.core.list.ExtendedList;
import ggll.ui.canvas.widget.IconNodeWidgetExt;
import ggll.ui.canvas.widget.LabelWidgetExt;
import ggll.ui.resource.CanvasResource;

import java.util.Observable;

import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.WidgetAction.Chain;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modules.visual.action.MoveAction;

public class MoveTracker extends Observable
{
	private final Canvas canvas;

	public MoveTracker(Canvas canvas)
	{
		this.canvas = canvas;
	}

	private void removeAllMoveAction(Chain chainActions)
	{
		final ExtendedList<WidgetAction> tmpActions = new ExtendedList<WidgetAction>();
		for (final WidgetAction action : chainActions.getActions())
		{
			if (action instanceof MoveAction)
			{
				tmpActions.append(action);
			}
		}
		for (final WidgetAction action : tmpActions.getAll())
		{
			chainActions.removeAction(action);
		}

	}

	@Override
	public void notifyObservers(Object obj)
	{
		setChanged();
		super.notifyObservers(obj);
		this.canvas.getActionFactory().setActiveMoveAction((String) obj);
		final WidgetAction activeMovement = this.canvas.getActionFactory().getAction("Move");
		for (final String nodes : this.canvas.getNodes())
		{
			final Object obecjtWidget = this.canvas.findWidget(nodes);
			if (obecjtWidget != null)
			{
				final Widget widget = (Widget) obecjtWidget;
				if (widget instanceof LabelWidgetExt || widget instanceof IconNodeWidgetExt)
				{
					removeAllMoveAction(widget.getActions(CanvasResource.SELECT));
					widget.getActions(CanvasResource.SELECT).addAction(activeMovement);
				}
			}
		}
	}
}
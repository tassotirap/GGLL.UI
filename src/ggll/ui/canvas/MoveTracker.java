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
	private AbstractCanvas canvas;
	public MoveTracker(AbstractCanvas canvas)
	{
		this.canvas = canvas;
	}

	private void removeAllMoveAction(Chain chainActions)
	{
		ExtendedList<WidgetAction> tmpActions = new ExtendedList<WidgetAction>();
		for (WidgetAction action : chainActions.getActions())
		{
			if (action instanceof MoveAction)
			{
				tmpActions.append(action);
			}
		}
		for (WidgetAction action : tmpActions.getAll())
		{
			chainActions.removeAction(action);
		}

	}

	@Override
	public void notifyObservers(Object obj)
	{
		setChanged();
		super.notifyObservers(obj);
		canvas.actionFactory.setActiveMoveAction((String)obj);
		WidgetAction activeMovement = canvas.actionFactory.getAction("Move");
		for (String nodes : canvas.getNodes())
		{
			Object obecjtWidget = canvas.findWidget(nodes);
			if (obecjtWidget != null)
			{
				Widget widget = (Widget) obecjtWidget;
				if (widget instanceof LabelWidgetExt || widget instanceof IconNodeWidgetExt)
				{
					removeAllMoveAction(widget.getActions(CanvasResource.SELECT));
					widget.getActions(CanvasResource.SELECT).addAction(activeMovement);
				}
			}
		}
	}
}
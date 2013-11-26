package ggll.canvas;

import ggll.canvas.action.WidgetActionFactory;
import ggll.canvas.widget.IconNodeWidgetExt;
import ggll.canvas.widget.LabelWidgetExt;
import ggll.resource.CanvasResource;

import java.util.ArrayList;
import java.util.List;
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
		List<WidgetAction> tmpActions = new ArrayList<WidgetAction>();
		for (WidgetAction action : chainActions.getActions())
		{
			if (action instanceof MoveAction)
			{
				tmpActions.add(action);
			}
		}
		for (WidgetAction action : tmpActions)
		{
			chainActions.removeAction(action);
		}

	}

	@Override
	public void notifyObservers(Object obj)
	{
		setChanged();
		super.notifyObservers(obj);
		WidgetActionFactory.setActiveMoveAction((String)obj);
		WidgetAction activeMovement = WidgetActionFactory.getAction("Move", canvas);
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
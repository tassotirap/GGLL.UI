package ggll.canvas;

import ggll.canvas.action.WidgetActionFactory;
import ggll.canvas.widget.IconNodeWidgetExt;
import ggll.canvas.widget.LabelWidgetExt;

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
	private WidgetActionFactory widgetActionFactory;
	public MoveTracker(AbstractCanvas canvas)
	{
		this.canvas = canvas;
		this.widgetActionFactory = WidgetActionFactory.getInstance();
		addObserver(widgetActionFactory);
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
		WidgetAction activeMovement = widgetActionFactory.getAction("Move", canvas);
		for (String nd : canvas.getNodes())
		{
			Object obecjtWidget = canvas.findWidget(nd);
			if (obecjtWidget != null)
			{
				Widget widget = (Widget) obecjtWidget;
				if (widget instanceof LabelWidgetExt || widget instanceof IconNodeWidgetExt)
				{
					removeAllMoveAction(widget.getActions(CanvasStrings.SELECT));
					widget.getActions(CanvasStrings.SELECT).addAction(activeMovement);
				}
			}
		}
	}
}
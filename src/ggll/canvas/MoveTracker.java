package ggll.canvas;

import ggll.canvas.action.WidgetActionRepository;
import ggll.canvas.strategy.MoveStrategy;
import ggll.canvas.widget.IconNodeWidgetExt;
import ggll.canvas.widget.LabelWidgetExt;

import java.util.ArrayList;
import java.util.List;

import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.WidgetAction.Chain;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modules.visual.action.MoveAction;

class MoveTracker extends MoveStrategy
{
	private Canvas canvas;

	/**
	 * Creates a new MoveTracker, this class is supposed to look after changes
	 * in the policy of movement, and make the proper modifications on existing
	 * nodes on canvas, and also notify the active action repository.
	 * 
	 * @param canvas
	 */
	public MoveTracker(Canvas canvas, WidgetActionRepository arepo)
	{
		this.canvas = canvas;
		addObserver(arepo);
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
		WidgetAction activeMovement = canvas.actions.getAction("Move", canvas);
		for (String nd : canvas.getNodes())
		{
			Object obecjtWidget = canvas.findWidget(nd);
			if (obecjtWidget != null)
			{
				Widget widget = (Widget) obecjtWidget;
				if (widget instanceof LabelWidgetExt || widget instanceof IconNodeWidgetExt)
				{
					removeAllMoveAction(widget.getActions(CanvasData.SELECT));
					widget.getActions(CanvasData.SELECT).addAction(activeMovement);
				}
			}
		}
	}
}
package org.ggll.syntax.graph;

import ggll.core.list.ExtendedList;

import java.util.Observable;

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.widget.IconNodeWidgetExt;
import org.ggll.syntax.graph.widget.LabelWidgetExt;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.WidgetAction.Chain;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modules.visual.action.MoveAction;

public class MoveTracker extends Observable
{
	private final SyntaxGraph canvas;
	
	public MoveTracker(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	@Override
	public void notifyObservers(final Object obj)
	{
		setChanged();
		super.notifyObservers(obj);
		canvas.getActionFactory().setActiveMoveAction((String) obj);
		final WidgetAction activeMovement = canvas.getActionFactory().getAction("Move");
		for (final String nodes : canvas.getNodes())
		{
			final Object obecjtWidget = canvas.findWidget(nodes);
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
	
	private void removeAllMoveAction(final Chain chainActions)
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
}
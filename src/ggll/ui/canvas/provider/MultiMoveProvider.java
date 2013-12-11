package ggll.ui.canvas.provider;

import ggll.ui.canvas.Canvas;
import ggll.ui.core.syntax.grammar.model.SyntaxDefinitions;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.widget.Widget;

public class MultiMoveProvider implements MoveProvider
{
	private Canvas canvas;
	private Point originalLocation;
	private HashMap<Widget, Point> targets = new HashMap<Widget, Point>();

	public MultiMoveProvider(Canvas canvas)
	{
		this.canvas = canvas;
	}

	@Override
	public Point getOriginalLocation(Widget widget)
	{
		originalLocation = widget.getPreferredLocation();
		return originalLocation;
	}

	@Override
	public void movementFinished(Widget widget)
	{
		String context = null;
		if (targets.entrySet().size() > 1)
		{
			context = SyntaxDefinitions.Set;
		}
		else if (targets.entrySet().size() == 1)
		{
			context = SyntaxDefinitions.Node;
		}
		targets.clear();
		originalLocation = null;
		if (context != null)
		{
			canvas.getCanvasStateRepository().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Move"));
		}
	}

	@Override
	public void movementStarted(Widget widget)
	{
		Object object = canvas.findObject(widget);
		if (canvas.isNode(object) || canvas.isLabel(object))
		{
			for (Object o : canvas.getSelectedObjects())
				if (canvas.isNode(o) || canvas.isLabel(object))
				{
					Widget w = canvas.findWidget(o);
					if (w != null)
						targets.put(w, w.getPreferredLocation());
				}
		}
		else
		{
			targets.put(widget, widget.getPreferredLocation());
		}
	}

	@Override
	public void setNewLocation(Widget widget, Point location)
	{
		int dx = location.x - originalLocation.x;
		int dy = location.y - originalLocation.y;
		for (Map.Entry<Widget, Point> entry : targets.entrySet())
		{
			Point point = entry.getValue();
			entry.getKey().setPreferredLocation(new Point(point.x + dx, point.y + dy));
		}
	}

}

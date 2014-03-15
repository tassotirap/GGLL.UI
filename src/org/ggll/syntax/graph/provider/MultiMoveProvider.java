package org.ggll.syntax.graph.provider;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraph;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.widget.Widget;

public class MultiMoveProvider implements MoveProvider
{
	private final SyntaxGraph canvas;
	private Point originalLocation;
	private final HashMap<Widget, Point> targets = new HashMap<Widget, Point>();
	
	public MultiMoveProvider(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	@Override
	public Point getOriginalLocation(final Widget widget)
	{
		originalLocation = widget.getPreferredLocation();
		return originalLocation;
	}
	
	@Override
	public void movementFinished(final Widget widget)
	{
		String context = null;
		if (targets.entrySet().size() > 1)
		{
			context = CanvasResource.SET;
		}
		else if (targets.entrySet().size() == 1)
		{
			context = CanvasResource.NODE;
		}
		targets.clear();
		originalLocation = null;
		if (context != null)
		{
			canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Move"));
		}
	}
	
	@Override
	public void movementStarted(final Widget widget)
	{
		final Object object = canvas.findObject(widget);
		if (canvas.isNode(object) || canvas.isLabel(object))
		{
			for (final Object o : canvas.getSelectedObjects())
			{
				if (canvas.isNode(o) || canvas.isLabel(object))
				{
					final Widget w = canvas.findWidget(o);
					if (w != null)
					{
						targets.put(w, w.getPreferredLocation());
					}
				}
			}
		}
		else
		{
			targets.put(widget, widget.getPreferredLocation());
		}
	}
	
	@Override
	public void setNewLocation(final Widget widget, final Point location)
	{
		final int dx = location.x - originalLocation.x;
		final int dy = location.y - originalLocation.y;
		for (final Map.Entry<Widget, Point> entry : targets.entrySet())
		{
			final Point point = entry.getValue();
			entry.getKey().setPreferredLocation(new Point(point.x + dx, point.y + dy));
		}
	}
	
}

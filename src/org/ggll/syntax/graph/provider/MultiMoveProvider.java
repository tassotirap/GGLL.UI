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

	public MultiMoveProvider(SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}

	@Override
	public Point getOriginalLocation(Widget widget)
	{
		this.originalLocation = widget.getPreferredLocation();
		return this.originalLocation;
	}

	@Override
	public void movementFinished(Widget widget)
	{
		String context = null;
		if (this.targets.entrySet().size() > 1)
		{
			context = CanvasResource.SET;
		}
		else if (this.targets.entrySet().size() == 1)
		{
			context = CanvasResource.NODE;
		}
		this.targets.clear();
		this.originalLocation = null;
		if (context != null)
		{
			this.canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Move"));
		}
	}

	@Override
	public void movementStarted(Widget widget)
	{
		final Object object = this.canvas.findObject(widget);
		if (this.canvas.isNode(object) || this.canvas.isLabel(object))
		{
			for (final Object o : this.canvas.getSelectedObjects())
			{
				if (this.canvas.isNode(o) || this.canvas.isLabel(object))
				{
					final Widget w = this.canvas.findWidget(o);
					if (w != null)
					{
						this.targets.put(w, w.getPreferredLocation());
					}
				}
			}
		}
		else
		{
			this.targets.put(widget, widget.getPreferredLocation());
		}
	}

	@Override
	public void setNewLocation(Widget widget, Point location)
	{
		final int dx = location.x - this.originalLocation.x;
		final int dy = location.y - this.originalLocation.y;
		for (final Map.Entry<Widget, Point> entry : this.targets.entrySet())
		{
			final Point point = entry.getValue();
			entry.getKey().setPreferredLocation(new Point(point.x + dx, point.y + dy));
		}
	}

}

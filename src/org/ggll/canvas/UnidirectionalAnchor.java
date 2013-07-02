package org.ggll.canvas;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modules.visual.util.GeomUtil;

public class UnidirectionalAnchor extends Anchor
{
	private static HashMap<String, ArrayList<Direction>> targetDirections = new HashMap<String, ArrayList<Direction>>();
	private static ArrayList<String> connections = new ArrayList<String>();
	private Direction kind;
	private Direction preferredDirection;
	private String target;
	private String connection;

	public UnidirectionalAnchor(Widget widget, Direction kind)
	{
		this(widget, kind, null, null, null);
	}

	public UnidirectionalAnchor(Widget widget, Direction kind, String connection, String target, Direction preferredDirection)
	{
		super(widget);
		this.kind = kind;
		this.preferredDirection = preferredDirection;
		this.target = target;
		this.connection = connection;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Result compute(Entry entry)
	{
		Widget widget = getRelatedWidget();

		if (widget == null)
		{
			return null;
		}
		Rectangle bounds;
		Point center;
		bounds = widget.convertLocalToScene(widget.getBounds());
		center = GeomUtil.center(bounds);

		if (target != null && preferredDirection != null && connection != null)
		{
			if (!connections.contains(connection))
			{
				connections.add(connection);
				ArrayList<Direction> otherDirections = targetDirections.get(target);
				if (otherDirections != null)
				{
					if (otherDirections.contains(kind))
					{
						kind = preferredDirection;
					}
					otherDirections.add(kind);
				}
				else
				{
					otherDirections = new ArrayList<Direction>();
					otherDirections.add(kind);
					targetDirections.put(target, otherDirections);
				}
			}
		}

		switch (kind)
		{
			case LEFT:
				return new Anchor.Result(new Point(bounds.x, center.y), kind);
			case RIGHT:
				return new Anchor.Result(new Point(bounds.x + bounds.width, center.y), kind);
			case TOP:
				return new Anchor.Result(new Point(bounds.x + 4, center.y - 3), kind);
			case BOTTOM:
				return new Anchor.Result(new Point(bounds.x + 4, center.y + 3), kind);
		}
		return null;
	}
}

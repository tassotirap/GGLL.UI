package org.ggll.syntax.graph;

import java.awt.Point;
import java.awt.Rectangle;

import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modules.visual.util.GeomUtil;

public class UnidirectionalAnchor extends Anchor
{
	private Direction kind;
	private final Direction preferredDirection;
	private final String connection;
	private final SyntaxGraph canvas;
	
	public UnidirectionalAnchor(final SyntaxGraph canvas, final Widget widget, final Direction kind)
	{
		this(canvas, widget, kind, null, null);
	}
	
	public UnidirectionalAnchor(final SyntaxGraph canvas, final Widget widget, final Direction kind, final String connection, final Direction preferredDirection)
	{
		super(widget);
		this.canvas = canvas;
		this.kind = kind;
		this.preferredDirection = preferredDirection;
		this.connection = connection;
	}
	
	@Override
	public Result compute(final Entry entry)
	{
		final Widget widget = getRelatedWidget();
		
		if (widget == null) { return null; }
		
		final Object obj = canvas.findObject(widget);
		final Object[] edges = canvas.findNodeEdges((String) obj, false, true).toArray();
		if (preferredDirection != null && !isFirstConnection(edges))
		{
			kind = preferredDirection;
		}
		
		Rectangle bounds;
		Point center;
		bounds = widget.convertLocalToScene(widget.getBounds());
		center = GeomUtil.center(bounds);
		
		switch (kind)
		{
			case LEFT:
				return new Anchor.Result(new Point(bounds.x + 2, center.y), kind);
			case RIGHT:
				return new Anchor.Result(new Point(bounds.x + bounds.width, center.y), kind);
			case TOP:
				return new Anchor.Result(new Point(bounds.x + 4, center.y - 3), kind);
			case BOTTOM:
				return new Anchor.Result(new Point(bounds.x + 4, center.y + 3), kind);
		}
		return null;
	}
	
	private boolean isFirstConnection(final Object[] edges)
	{
		for (int i = 0; i < edges.length; i++)
		{
			final String edge = (String) edges[i];
			if (edge.equalsIgnoreCase(connection))
			{
				if (i > 0) { return false; }
			}
		}
		return true;
	}
}

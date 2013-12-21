package ggll.ui.canvas;

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
	private final Canvas canvas;

	public UnidirectionalAnchor(Canvas canvas, Widget widget, Direction kind)
	{
		this(canvas, widget, kind, null, null);
	}

	public UnidirectionalAnchor(Canvas canvas, Widget widget, Direction kind, String connection, Direction preferredDirection)
	{
		super(widget);
		this.canvas = canvas;
		this.kind = kind;
		this.preferredDirection = preferredDirection;
		this.connection = connection;
	}

	private boolean isFirstConnection(Object[] edges)
	{
		for (int i = 0; i < edges.length; i++)
		{
			final String edge = (String) edges[i];
			if (edge.equalsIgnoreCase(this.connection))
			{
				if (i > 0)
				{
					return false;
				}
			}
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Result compute(Entry entry)
	{
		final Widget widget = getRelatedWidget();

		if (widget == null)
		{
			return null;
		}

		final Object obj = this.canvas.findObject(widget);
		final Object[] edges = this.canvas.findNodeEdges((String) obj, false, true).toArray();
		if (this.preferredDirection != null && !isFirstConnection(edges))
		{
			this.kind = this.preferredDirection;
		}

		Rectangle bounds;
		Point center;
		bounds = widget.convertLocalToScene(widget.getBounds());
		center = GeomUtil.center(bounds);

		switch (this.kind)
		{
			case LEFT:
				return new Anchor.Result(new Point(bounds.x + 2, center.y), this.kind);
			case RIGHT:
				return new Anchor.Result(new Point(bounds.x + bounds.width, center.y), this.kind);
			case TOP:
				return new Anchor.Result(new Point(bounds.x + 4, center.y - 3), this.kind);
			case BOTTOM:
				return new Anchor.Result(new Point(bounds.x + 4, center.y + 3), this.kind);
		}
		return null;
	}
}

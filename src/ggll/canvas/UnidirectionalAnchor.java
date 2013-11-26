package ggll.canvas;

import java.awt.Point;
import java.awt.Rectangle;

import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modules.visual.util.GeomUtil;

public class UnidirectionalAnchor extends Anchor
{
	private Direction kind;
	private Direction preferredDirection;
	private String connection;
	private AbstractCanvas canvas;

	public UnidirectionalAnchor(AbstractCanvas canvas, Widget widget, Direction kind)
	{
		this(canvas, widget, kind, null, null);		
	}

	public UnidirectionalAnchor(AbstractCanvas canvas, Widget widget, Direction kind, String connection, Direction preferredDirection)
	{
		super(widget);
		this.canvas = canvas;
		this.kind = kind;
		this.preferredDirection = preferredDirection;
		this.connection = connection;
	}

	private boolean isFirstConnection(Object[] edges)
	{
		for(int i = 0; i < edges.length; i++)
		{
			String edge = (String)edges[i];
			if(edge.equalsIgnoreCase(connection))
			{
				if(i > 0)
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
		Widget widget = getRelatedWidget();

		if (widget == null)
		{
			return null;
		}
		
		Object obj = canvas.findObject(widget);
		Object[] edges = canvas.findNodeEdges((String)obj, false, true).toArray();		
		if(preferredDirection != null && !isFirstConnection(edges))
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
}

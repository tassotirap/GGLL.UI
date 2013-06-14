package org.ggll.canvas;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;

import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modules.visual.util.GeomUtil;

public class UnidirectionalAnchor extends Anchor
{

	public static enum UnidirectionalAnchorKind
	{
		BOTTOM, LEFT, RIGHT, TOP, LEFT_BOTTOM
	}

	private static final int PREFERRED_GAP_INC = 30;
	private static HashMap<String, Boolean> priorConnByConn = new HashMap<String, Boolean>();
	private static HashMap<Widget, ConnectionWidget> refConnectionByWidget = new HashMap<Widget, ConnectionWidget>();
	private static HashMap<String, Widget> refWidgetByConn = new HashMap<String, Widget>();

	private String connection;
	private UnidirectionalAnchorKind kind;

	private Direction preferredDirection;;

	public UnidirectionalAnchor(Widget widget, String connection, UnidirectionalAnchorKind kind)
	{
		this(widget, connection, kind, null);
	}

	public UnidirectionalAnchor(Widget widget, String connection, UnidirectionalAnchorKind kind, Direction preferredDirection)
	{
		super(widget);
		this.kind = kind;
		this.preferredDirection = preferredDirection;
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
		Direction direction;
		Direction thisPreferredDirection;
		int gap_inc = 0;
		ConnectionWidget refConnection = null;
		GraphScene gs = (GraphScene) widget.getScene();
		bounds = widget.convertLocalToScene(widget.getBounds());
		center = GeomUtil.center(bounds);
		if (!entry.isAttachedToConnectionSource() && connection != null)
		{
			if (!refWidgetByConn.containsKey(connection) || refWidgetByConn.get(connection) != widget)
			{
				priorConnByConn.put(connection, refConnectionByWidget.containsKey(widget));
				refWidgetByConn.put(connection, widget);
			}
			if (priorConnByConn.containsKey(connection) && priorConnByConn.get(connection))
			{
				Object oConn;
				if (refConnectionByWidget.containsKey(widget))
				{
					ConnectionWidget refConn = refConnectionByWidget.get(widget);
					if ((oConn = gs.findObject(refConn)) != null)
					{
						if (gs.getEdgeTarget(oConn).equals(gs.findObject(widget)))
						{
							refConnection = refConn;
						}
					}
				}
				if (refConnection == null)
				{
					for (Object oc : gs.findNodeEdges(gs.findObject(widget), false, true))
					{
						if (!oc.equals(connection))
						{
							refConnection = (ConnectionWidget) gs.findWidget(oc);
							break;
						}
					}
				}
				if ((oConn = gs.findObject(refConnectionByWidget.get(widget))) == null || (!gs.findNodeEdges(gs.findObject(widget), false, true).contains(oConn)))
				{
					if (refConnectionByWidget.get(widget) != null)
					{
						priorConnByConn.put((String) gs.findObject(refConnection), false);
						priorConnByConn.remove(refConnectionByWidget.get(widget));
					}
					refConnectionByWidget.put(widget, refConnection);
				}
			}
			else
			{
				Object oConn;
				if (!refConnectionByWidget.containsKey(widget) || (oConn = gs.findObject(refConnectionByWidget.get(widget))) == null || !gs.findNodeEdges(gs.findObject(widget), false, true).contains(oConn))
				{
					refConnectionByWidget.put(widget, refConnection);
				}
			}
		}
		if (refConnection != null)
		{
			gap_inc = PREFERRED_GAP_INC;
			thisPreferredDirection = preferredDirection;
		}
		else
		{
			thisPreferredDirection = null;
		}
		switch (kind)
		{
			case LEFT:
				direction = thisPreferredDirection == null ? Direction.LEFT : thisPreferredDirection;
				return new Anchor.Result(new Point(bounds.x - (gap_inc), center.y), direction);
			case RIGHT:
				direction = thisPreferredDirection == null ? Direction.RIGHT : thisPreferredDirection;
				return new Anchor.Result(new Point(bounds.x + bounds.width + (gap_inc), center.y), direction);
			case TOP:
				direction = thisPreferredDirection == null ? Direction.TOP : thisPreferredDirection;
				return new Anchor.Result(new Point(center.x, bounds.y - (gap_inc)), direction);
			case BOTTOM:
				direction = thisPreferredDirection == null ? Direction.BOTTOM : thisPreferredDirection;
				return new Anchor.Result(new Point(center.x, bounds.y + bounds.height + (gap_inc)), direction);
			case LEFT_BOTTOM:
				return new Anchor.Result(new Point(bounds.x - (PREFERRED_GAP_INC + gap_inc), center.y), Direction.BOTTOM);
		}
		return null;
	}
}

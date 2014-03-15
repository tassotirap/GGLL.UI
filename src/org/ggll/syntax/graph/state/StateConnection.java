package org.ggll.syntax.graph.state;

import java.awt.Point;
import java.io.Serializable;
import java.util.List;

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraph;
import org.netbeans.api.visual.widget.ConnectionWidget;

public class StateConnection implements Serializable
{
	private static final long serialVersionUID = -4162245206128920310L;
	private final String id;
	private final String source;
	private final String target;
	private String type;
	private final List<Point> points;
	
	public StateConnection(final String id, final SyntaxGraph canvas)
	{
		final ConnectionWidget connectionWidget = (ConnectionWidget) canvas.findWidget(id);
		this.id = id;
		source = canvas.getEdgeSource(id);
		target = canvas.getEdgeTarget(id);
		points = connectionWidget.getControlPoints();
		if (canvas.isAlternative(id))
		{
			type = CanvasResource.ALTERNATIVE;
		}
		else if (canvas.isSuccessor(id))
		{
			type = CanvasResource.SUCCESSOR;
		}
	}
	
	public String getId()
	{
		return id;
	}
	
	public List<Point> getPoints()
	{
		return points;
	}
	
	public String getSource()
	{
		return source;
	}
	
	public String getTarget()
	{
		return target;
	}
	
	public String getType()
	{
		return type;
	}
}
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
	private String id;
	private String source;
	private String target;
	private String type;
	private List<Point> points;
	
	public StateConnection(String id, SyntaxGraph canvas)
	{
		ConnectionWidget connectionWidget = (ConnectionWidget)canvas.findWidget(id);
		this.id = id;		
		this.source = canvas.getEdgeSource(id);
		this.target = canvas.getEdgeTarget(id);
		this.points = connectionWidget.getControlPoints();
		if (canvas.isAlternative(id))
		{
			this.type = CanvasResource.ALTERNATIVE;
		}
		else if (canvas.isSuccessor(id))
		{
			this.type = CanvasResource.SUCCESSOR;
		}
	}

	public String getId()
	{
		return this.id;
	}

	public List<Point> getPoints()
	{
		return this.points;
	}

	public String getSource()
	{
		return this.source;
	}

	public String getTarget()
	{
		return this.target;
	}

	public String getType()
	{
		return this.type;
	}
}
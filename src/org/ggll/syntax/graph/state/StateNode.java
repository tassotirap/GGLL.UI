package org.ggll.syntax.graph.state;


import java.awt.Point;
import java.io.Serializable;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.widget.MarkedWidget;
import org.ggll.syntax.graph.widget.TypedWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class StateNode implements Serializable
{
	private static final long serialVersionUID = -5146510630189874864L;
	private Point location;
	private String semanticRoutine;
	private String id;
	private String title;
	private String type;
	
	public StateNode(String id, SyntaxGraph canvas)
	{
		Widget widget = canvas.findWidget(id);
		this.id = id;
		this.location = widget.getPreferredLocation();		
		if (widget instanceof TypedWidget)
		{
			this.type = ((TypedWidget) widget).getType();
		}
		if (widget instanceof MarkedWidget)
		{			
			this.semanticRoutine = ((MarkedWidget) widget).getMark();
		}
		if (widget instanceof LabelWidget)
		{
			final LabelWidget labelWidget = (LabelWidget) widget;
			this.title = labelWidget.getLabel();			
		}		
	}

	public Point getLocation()
	{
		return this.location;
	}

	public String getSemanticRoutine()
	{
		return this.semanticRoutine;
	}

	public String getId()
	{
		return this.id;
	}

	public String getTitle()
	{
		return this.title;
	}

	public String getType()
	{
		return this.type;
	}

//	public void setLocation(Point location)
//	{
//		this.location = location;
//	}
//
//	public void setSemanticRoutine(String semanticRoutine)
//	{
//		this.semanticRoutine = semanticRoutine;
//	}
//
//	public void setId(String id)
//	{
//		this.id = id;
//	}
//
//	public void setTitle(String title)
//	{
//		this.title = title;
//	}
//
//	public void setType(String type)
//	{
//		this.type = type;
//	}

}
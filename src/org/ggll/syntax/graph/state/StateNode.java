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
	private final Point location;
	private String semanticRoutine;
	private final String id;
	private String title;
	private String type;
	private int number;
	private boolean flag;
	
	public StateNode(final String id, final SyntaxGraph canvas)
	{
		final Widget widget = canvas.findWidget(id);
		this.id = id;
		location = widget.getPreferredLocation();
		if (widget instanceof TypedWidget)
		{
			type = ((TypedWidget) widget).getType();
		}
		if (widget instanceof MarkedWidget)
		{
			semanticRoutine = ((MarkedWidget) widget).getMark();
		}
		if (widget instanceof LabelWidget)
		{
			final LabelWidget labelWidget = (LabelWidget) widget;
			title = labelWidget.getLabel();
		}
	}
	
	public String getId()
	{
		return id;
	}
	
	public Point getLocation()
	{
		return location;
	}
	
	public int getNumber()
	{
		return number;
	}
	
	public String getSemanticRoutine()
	{
		return semanticRoutine;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getType()
	{
		return type;
	}
	
	public boolean isFlag()
	{
		return flag;
	}
	
	public void setFlag(final boolean flag)
	{
		this.flag = flag;
	}
	
	public void setNumber(final int number)
	{
		this.number = number;
	}
}
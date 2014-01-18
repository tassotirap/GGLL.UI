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

	public StateNode(String id, SyntaxGraph canvas)
	{
		final Widget widget = canvas.findWidget(id);
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

	public String getId()
	{
		return this.id;
	}

	public Point getLocation()
	{
		return this.location;
	}

	public int getNumber()
	{
		return number;
	}

	public String getSemanticRoutine()
	{
		return this.semanticRoutine;
	}

	public String getTitle()
	{
		return this.title;
	}

	public String getType()
	{
		return this.type;
	}

	public boolean isFlag()
	{
		return flag;
	}

	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}
}
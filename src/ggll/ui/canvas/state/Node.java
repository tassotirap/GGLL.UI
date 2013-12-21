package ggll.ui.canvas.state;

import java.awt.Point;
import java.io.Serializable;

public class Node implements Serializable
{

	private static final long serialVersionUID = -5146510630189874864L;
	private Point location;
	private String mark;
	private String name;
	private String title;
	private String type;

	public Point getLocation()
	{
		return this.location;
	}

	public String getMark()
	{
		return this.mark;
	}

	public String getName()
	{
		return this.name;
	}

	public String getTitle()
	{
		return this.title;
	}

	public String getType()
	{
		return this.type;
	}

	public void setLocation(Point location)
	{
		this.location = location;
	}

	public void setMark(String mark)
	{
		this.mark = mark;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setType(String type)
	{
		this.type = type;
	}

}
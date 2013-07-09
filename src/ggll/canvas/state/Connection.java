package ggll.canvas.state;

import java.awt.Point;
import java.util.List;

public class Connection extends CanvasSerializableElement implements Comparable<Connection>
{

	private static final long serialVersionUID = -4162245206128920310L;
	private String name;
	private String source;
	private String target;
	private String type;
	private List<Point> points;

	@Override
	public int compareTo(Connection c)
	{
		return getName().compareTo(c.getName());
	}

	public String getName()
	{
		return name;
	}

	public List<Point> getPoints()
	{
		return this.points;
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

	public void setName(String name)
	{
		this.name = name;
	}

	public void setPoints(List<Point> points)
	{
		this.points = points;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public void setTarget(String target)
	{
		this.target = target;
	}

	public void setType(String type)
	{
		this.type = type;
	}

}
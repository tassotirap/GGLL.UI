package org.ggll.grammar.model;


abstract public class SyntaxElement
{
	private String id;
	private String type;
	
	public String getID()
	{
		return id;
	}

	public void setID(String id)
	{
		this.id = id;
	}
	
	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}

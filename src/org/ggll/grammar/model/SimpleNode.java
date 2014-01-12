package org.ggll.grammar.model;

import java.util.Hashtable;

/***
 * A simple node refers to any kind of node that can receive output input
 * connections as usual, and also has a usual node label.
 * 
 * 
 */
public class SimpleNode extends SyntaxModel implements AbstractNode
{

	/** gets Track of available IDs by type **/
	private static Hashtable<String, Integer> nextIDByType = new Hashtable<String, Integer>();

	/**
	 * determines the type of this node (ex: a terminal, a non terminal, etc...)
	 **/
	private String type;

	/**
	 * @param type
	 *            the type of this simple node
	 * @param label
	 *            the label of this simple node
	 */
	public SimpleNode(String type, String label)
	{
		super(getNewID(type));
		setLabel(label);
		this.type = type;
	}

	/**
	 * Gets the next available ID
	 */
	public static String getNewID(String type)
	{
		if (!nextIDByType.containsKey(type))
		{
			nextIDByType.put(type, 0);
		}
		return String.valueOf(nextIDByType.put(type, nextIDByType.get(type) + 1) + 1);
	}

	/*----------------------------- getters, setters ---------------------*/

	@Override
	public String getType()
	{
		return this.type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	@Override
	public String toString()
	{
		return this.type + " #" + getID();
	}

}

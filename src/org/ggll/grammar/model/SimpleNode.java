package org.ggll.grammar.model;

import ggll.core.list.ExtendedList;

/**
 * This class is a container to all essential elements of a grammar.
 */
public class SimpleNode extends SyntaxSubpart
{

	static final long serialVersionUID = 1;

	private static int count;

	private ExtendedList<SyntaxElement> children = new ExtendedList<SyntaxElement>();
	private SimpleNode semanticNode;
	private String label;
	private String type;

	protected Integer connectionRouter = null;

	public SimpleNode()
	{
		super(getNewID());
	}

	public SimpleNode(String type, String label)
	{
		super(getNewID());
		setLabel(label);
		this.type = type;
	}

	public SimpleNode(String id)
	{
		super(id);
	}

	public static String getNewID()
	{
		return SimpleNode.class.getName() + Integer.toString(count++);
	}

	public void addChild(SyntaxElement child)
	{
		addChild(child, -1);
	}

	public void addChild(SyntaxElement child, int index)
	{
		this.children.insertAt(index, child);
	}

	public SyntaxElement findElement(String id)
	{
		for (final SyntaxElement e : this.children.getAll())
		{
			if (e.getID().equals(id))
			{
				return e;
			}
		}
		return null;
	}

	public ExtendedList<SyntaxElement> getChildren()
	{
		return this.children;
	}

	public ExtendedList<SyntaxElement> getChildrenConnections()
	{
		final ExtendedList<SyntaxElement> cNodes = new ExtendedList<SyntaxElement>();
		for (final SyntaxElement e : this.children.getAll())
		{
			if (e instanceof Connection)
			{
				cNodes.append(e);
			}
		}
		return cNodes;
	}

	public ExtendedList<SyntaxElement> getChildrenNodes()
	{
		final ExtendedList<SyntaxElement> cNodes = new ExtendedList<SyntaxElement>();
		for (final SyntaxElement e : this.children.getAll())
		{
			if (e instanceof SimpleNode)
			{
				cNodes.append(e);
			}
		}
		return cNodes;
	}

	public SimpleNode getSemanticNode()
	{
		return this.semanticNode;
	}

	public boolean isConnection(SyntaxElement e)
	{
		return e instanceof Connection;
	}

	public boolean isNode(SyntaxElement e)
	{
		return e instanceof SimpleNode;
	}

	public void removeChild(SyntaxElement child)
	{
		if (child == null)
		{
			return;
		}
		final SyntaxElement[] remainingChildren = new SyntaxElement[this.children.count() - 1];
		int i = 0;
		for (final SyntaxElement se : this.children.getAll())
		{
			if (se != child)
			{
				remainingChildren[i++] = se;
			}
		}
		this.children = new ExtendedList<SyntaxElement>();
		this.children.addAll(remainingChildren);
	}

	public void setSemanticNode(SimpleNode simpleNode)
	{
		this.semanticNode = simpleNode;
	}

	public String getLabel()
	{
		if (label == null)
		{
			return "";
		}
		return label;
	}

	public void setLabel(String label)
	{
		if (label != null)
		{
			this.label = label;
		}
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

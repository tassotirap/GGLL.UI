package org.ggll.parser.syntax.grammar.model;

import ggll.core.list.ExtendedList;

/**
 * This class is a container to all essential elements of a grammar.
 */
public class SyntaxModel extends SyntaxSubpart
{

	static final long serialVersionUID = 1;

	private static int count;

	private ExtendedList<SyntaxElement> children = new ExtendedList<SyntaxElement>();
	private SimpleNode semanticNode;
	protected Integer connectionRouter = null;

	public SyntaxModel()
	{
		super(getNewID());
	}

	public SyntaxModel(String id)
	{
		super(id);
	}

	public static String getNewID()
	{
		return SyntaxModel.class.getName() + Integer.toString(count++);
	}

	public void addChild(SyntaxElement child)
	{
		addChild(child, -1);
	}

	public void addChild(SyntaxElement child, int index)
	{
		this.children.insertAt(index, child);
		fireStructureChange(CHILDREN, child);
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

	public ExtendedList<NodeLabel> getChildrenAsLabels()
	{
		final ExtendedList<NodeLabel> lNodes = new ExtendedList<NodeLabel>();
		for (final SyntaxElement e : this.children.getAll())
		{
			if (e instanceof NodeLabel)
			{
				lNodes.append((NodeLabel) e);
			}
		}
		return lNodes;
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

	public Object getPropertyValue(Object propName)
	{
		return getProperty((String) propName);
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
		fireStructureChange(CHILDREN, child);
	}

	public void setPropertyValue(Object id, Object value)
	{
		super.setProperty((String) id, (String) value);
	}

	public void setSemanticNode(SimpleNode sn)
	{
		this.semanticNode = sn;
	}

	@Override
	public String toString()
	{
		return SyntaxDefinitions.AsinDiagram_LabelText;
	}

}

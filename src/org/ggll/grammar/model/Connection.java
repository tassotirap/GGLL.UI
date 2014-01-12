package org.ggll.grammar.model;

import org.ggll.resource.CanvasResource;

public class Connection extends SyntaxElement
{
	static final long serialVersionUID = 1;
	protected SimpleNode source;
	protected SimpleNode target;

	public Connection(String id)
	{
		setID(id);
	}

	public void attachSource()
	{
		if (getSource() == null || getSource().getSourceConnections().contains(this))
		{
			return;
		}
		getSource().connectOutput(this);
	}

	public void attachTarget(Object context)
	{
		if (this.target == null || this.source == null)
		{
			return;
		}
		final SimpleNode target = getTarget();
		final SimpleNode source = getSource();
		if (context.equals(CanvasResource.SUCCESSOR))
		{
			source.setSucessor(target);
		}
		else if (context.equals(CanvasResource.ALTERNATIVE))
		{
			source.setAlternative(target);
		}

		getTarget().connectInput(this);
	}

	/** detach connection from source **/
	public void detachSource()
	{
		if (getSource() == null)
		{
			return;
		}
		getSource().disconnectOutput(this);
	}

	/** detach connection on target **/
	public void detachTarget()
	{
		if (getTarget() == null)
		{
			return;
		}

		final SyntaxSubpart e = getTarget();
		if (e instanceof SimpleNode)
		{
			if (getSource() instanceof SimpleNode)
			{
				final SimpleNode l = (SimpleNode) getSource();
				if (l.getSucessor() == e)
				{
					l.setSucessor(null);
				}
				else if (l.getAlternative() == e)
				{
					l.setAlternative(null);
				}
			}
		}
		getTarget().disconnectInput(this);
	}

	public SimpleNode getSource()
	{
		return this.source;
	}

	public String getSourceTerminal()
	{
		return this.source.getID();
	}

	public SimpleNode getTarget()
	{
		return this.target;
	}

	public String getTargetTerminal()
	{
		return this.target.getID();
	}

	public void setSource(SimpleNode source)
	{
		this.source = source;
	}

	public void setTarget(SimpleNode target)
	{
		this.target = target;
	}
}

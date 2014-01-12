package org.ggll.grammar.model;

import org.ggll.resource.CanvasResource;

/** A connection between nodes **/
public class Connection extends SyntaxElement
{
	static final long serialVersionUID = 1;
	protected SyntaxSubpart source;
	protected SyntaxSubpart target;

	protected boolean value;

	public Connection(String id)
	{
		setID(id);
	}

	/** Attach connection to the source node **/
	public void attachSource()
	{
		if (getSource() == null || getSource().getSourceConnections().contains(this))
		{
			return;
		}
		getSource().connectOutput(this);
	}

	/**
	 * Attach connection to the target node.
	 * 
	 * @param context
	 *            could be a successor or an alternative connection
	 */
	public void attachTarget(Object context)
	{
		if (this.target == null || this.source == null)
		{
			return;
		}
		final Object e = getTarget();
		if (e instanceof SimpleNode)
		{
			if (getSource() instanceof SyntaxModel)
			{
				final SyntaxModel l = (SyntaxModel) getSource();
				if (context.equals(CanvasResource.SUCCESSOR))
				{
					l.setSucessor((SyntaxSubpart) e);
				}
				else if (context.equals(CanvasResource.ALTERNATIVE))
				{
					l.setAlternative((SyntaxSubpart) e);
				}
			}
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
			if (getSource() instanceof SyntaxModel)
			{
				final SyntaxModel l = (SyntaxModel) getSource();
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

	/*------------------------------- GETTERS AND SETTERS ----------------------*/

	public SyntaxSubpart getSource()
	{
		return this.source;
	}

	public String getSourceTerminal()
	{
		return this.source.getID();
	}

	public SyntaxSubpart getTarget()
	{
		return this.target;
	}

	public String getTargetTerminal()
	{
		return this.target.getID();
	}

	public boolean getValue()
	{
		return this.value;
	}

	public void setSource(SyntaxSubpart e)
	{
		this.source = e;
	}

	public void setTarget(SyntaxSubpart e)
	{
		this.target = e;
	}

	public void setValue(boolean value)
	{
		if (value == this.value)
		{
			return;
		}
		this.value = value;
	}

	public String toString()
	{
		return "Wire(" + getSource() + "," + getSourceTerminal() + "->" + getTarget() + "," + getTargetTerminal() + ")";//$NON-NLS-5$//$NON-NLS-4$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
	}
}

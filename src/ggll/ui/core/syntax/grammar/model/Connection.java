package ggll.ui.core.syntax.grammar.model;

/** A connection between nodes **/
public class Connection extends SyntaxElement
{

	static final long serialVersionUID = 1;
	private String id;
	protected SyntaxSubpart source;
	protected SyntaxSubpart target;

	protected boolean value;

	public Connection()
	{
		this.id = toString();
	}

	public Connection(String id)
	{
		this.id = id;
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
				if (context.equals(SyntaxDefinitions.SucConnection))
				{
					l.setSucessor((SyntaxSubpart) e);
				}
				else if (context.equals(SyntaxDefinitions.AltConnection))
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

	@Override
	public String getID()
	{
		return this.id;
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

	@Override
	public void setID(String id)
	{
		this.id = id;
	}

	public void setSource(SyntaxSubpart e)
	{
		final Object old = this.source;
		this.source = e;
		firePropertyChange("source", old, this.source);//$NON-NLS-1$
	}

	public void setTarget(SyntaxSubpart e)
	{
		final Object old = this.target;
		this.target = e;
		firePropertyChange("target", old, this.target);
	}

	public void setValue(boolean value)
	{
		if (value == this.value)
		{
			return;
		}
		this.value = value;
		if (this.target != null)
		{
			this.target.update();
		}
		firePropertyChange("value", null, null);//$NON-NLS-1$
	}

	@Override
	public String toString()
	{
		return "Wire(" + getSource() + "," + getSourceTerminal() + "->" + getTarget() + "," + getTargetTerminal() + ")";//$NON-NLS-5$//$NON-NLS-4$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
	}
}

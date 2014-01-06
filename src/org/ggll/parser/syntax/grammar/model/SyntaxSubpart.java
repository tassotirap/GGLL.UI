package org.ggll.parser.syntax.grammar.model;

import java.util.Vector;

/**
 * This class refers to a element or set of elements that have input and output
 * connections
 **/
abstract public class SyntaxSubpart extends SyntaxElement
{
	static final long serialVersionUID = 1;
	private String id;
	protected SyntaxSubpart Alternative;
	protected boolean flag;
	protected Vector<Connection> inputs = new Vector<Connection>(4, 4);
	protected int number;
	protected Vector<Connection> outputs = new Vector<Connection>(4, 4);

	protected SyntaxSubpart Sucessor;

	public SyntaxSubpart(String id)
	{
		this.id = id;
	}

	public void connectInput(Connection w)
	{
		this.inputs.addElement(w);
		update();
		fireStructureChange(INPUTS, w);
	}

	public void connectOutput(Connection w)
	{
		this.outputs.addElement(w);
		update();
		fireStructureChange(OUTPUTS, w);
	}

	public void disconnectInput(Connection w)
	{
		this.inputs.remove(w);
		update();
		fireStructureChange(INPUTS, w);
	}

	public void disconnectOutput(Connection w)
	{
		this.outputs.removeElement(w);
		update();
		fireStructureChange(OUTPUTS, w);
	}

	public SyntaxSubpart getAlternative()
	{
		return this.Alternative;
	}

	public Vector<Connection> getConnections()
	{
		final Vector<Connection> v = new Vector<Connection>();
		v.addAll(this.outputs);
		v.addAll(this.inputs);
		return v;
	}

	public boolean getFlag()
	{
		return this.flag;
	}

	@Override
	public String getID()
	{
		return this.id;
	}

	public int getNumber()
	{
		return this.number;
	}

	public Vector<Connection> getSourceConnections()
	{
		return this.outputs;
	}

	public SyntaxSubpart getSucessor()
	{
		return this.Sucessor;
	}

	public Vector<Connection> getTargetConnections()
	{
		return this.inputs;
	}

	public void setAlternative(SyntaxSubpart e)
	{
		this.Alternative = e;
	}

	public void setFlag(boolean b)
	{
		this.flag = b;
	}

	@Override
	public void setID(String s)
	{
		this.id = s;
	}

	public void setNumber(int n)
	{
		this.number = n;
	}

	public void setSucessor(SyntaxSubpart e)
	{
		this.Sucessor = e;
	}
}

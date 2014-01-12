package org.ggll.grammar.model;

import java.util.ArrayList;
import java.util.Vector;

/**
 * This class refers to a element or set of elements that have input and output
 * connections
 **/
abstract public class SyntaxSubpart extends SyntaxElement
{
	protected boolean flag;
	protected int number;
	
	protected ArrayList<Connection> inputs = new ArrayList<Connection>();
	protected ArrayList<Connection> outputs = new ArrayList<Connection>();

	protected SyntaxSubpart alternative;
	protected SyntaxSubpart sucessor;

	public SyntaxSubpart(String id)
	{
		setID(id);
	}

	public void connectInput(Connection connection)
	{
		this.inputs.add(connection);
	}

	public void connectOutput(Connection connection)
	{
		this.outputs.add(connection);
	}

	public void disconnectInput(Connection connection)
	{
		this.inputs.remove(connection);
	}

	public void disconnectOutput(Connection connection)
	{
		this.outputs.remove(connection);
	}

	public SyntaxSubpart getAlternative()
	{
		return this.alternative;
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


	public int getNumber()
	{
		return this.number;
	}

	public ArrayList<Connection> getSourceConnections()
	{
		return this.outputs;
	}

	public SyntaxSubpart getSucessor()
	{
		return this.sucessor;
	}

	public ArrayList<Connection> getTargetConnections()
	{
		return this.inputs;
	}

	public void setAlternative(SyntaxSubpart e)
	{
		this.alternative = e;
	}

	public void setFlag(boolean b)
	{
		this.flag = b;
	}


	public void setNumber(int number)
	{
		this.number = number;
	}

	public void setSucessor(SyntaxSubpart e)
	{
		this.sucessor = e;
	}
}

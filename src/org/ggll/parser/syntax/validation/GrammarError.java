package org.ggll.parser.syntax.validation;

import org.ggll.syntax.graph.state.StateNode;

public class GrammarError
{
	private StateNode node;
	
	private String error;
	
	public GrammarError(String error)
	{
		this.error = error;
	}
	
	public GrammarError(String error, StateNode node)
	{
		this(error);
		this.node = node;
	}

	public StateNode getNode()
	{
		return node;
	}

	public String getError()
	{
		return error;
	}
}

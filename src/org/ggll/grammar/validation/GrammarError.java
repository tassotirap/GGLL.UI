package org.ggll.grammar.validation;

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

	public String getError()
	{
		return error;
	}

	public StateNode getNode()
	{
		return node;
	}
}

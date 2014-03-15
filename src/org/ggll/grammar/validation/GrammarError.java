package org.ggll.grammar.validation;

import org.ggll.syntax.graph.state.StateNode;

public class GrammarError
{
	private StateNode node;
	
	private final String error;
	
	public GrammarError(final String error)
	{
		this.error = error;
	}
	
	public GrammarError(final String error, final StateNode node)
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

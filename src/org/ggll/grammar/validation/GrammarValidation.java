package org.ggll.grammar.validation;

import ggll.core.list.ExtendedList;

import org.ggll.syntax.graph.state.StateNode;

/** A generic rule that defines if a grammar is valid or not **/
public abstract class GrammarValidation
{
	private final ExtendedList<GrammarError> errors;
	
	public GrammarValidation()
	{
		errors = new ExtendedList<GrammarError>();
	}
	
	public void addError(final String error)
	{
		errors.append(new GrammarError(error));
	}
	
	public void addError(final String error, final StateNode node)
	{
		errors.append(new GrammarError(error, node));
	}
	
	public ExtendedList<GrammarError> getErrors()
	{
		return errors;
	}
	
	public abstract void validate();
}

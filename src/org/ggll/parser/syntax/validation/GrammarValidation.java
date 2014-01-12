package org.ggll.parser.syntax.validation;

import ggll.core.list.ExtendedList;

import org.ggll.syntax.graph.state.StateNode;

/** A generic rule that defines if a grammar is valid or not **/
public abstract class GrammarValidation
{
	private ExtendedList<GrammarError> errors;

	public GrammarValidation()
	{
		errors = new ExtendedList<GrammarError>();
	}

	public abstract void validate();

	public ExtendedList<GrammarError> getErrors()
	{
		return errors;
	}
	
	public void addError(String error, StateNode node)
	{
		errors.append(new GrammarError(error, node));
	}
	
	public void addError(String error)
	{
		errors.append(new GrammarError(error));
	}
}

package ggll.core.syntax.validation;

import ggll.core.syntax.grammar.Grammar;

/** A generic rule that defines if a grammar is valid or not **/
public abstract class GrammarRule
{

	public GrammarRule(Grammar grammar, boolean onTheFly)
	{
	}

	public abstract void validate() throws InvalidGrammarException;
}

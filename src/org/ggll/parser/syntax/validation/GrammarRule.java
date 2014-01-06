package org.ggll.parser.syntax.validation;

import org.ggll.parser.syntax.grammar.Grammar;

/** A generic rule that defines if a grammar is valid or not **/
public abstract class GrammarRule
{

	public GrammarRule(Grammar grammar, boolean onTheFly)
	{
	}

	public abstract void validate() throws InvalidGrammarException;
}

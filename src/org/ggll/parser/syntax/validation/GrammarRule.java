package org.ggll.parser.syntax.validation;

import org.ggll.exceptions.InvalidGrammarException;

/** A generic rule that defines if a grammar is valid or not **/
public abstract class GrammarRule
{
	public abstract void validate() throws InvalidGrammarException;
}

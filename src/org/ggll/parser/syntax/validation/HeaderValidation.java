package org.ggll.parser.syntax.validation;

import org.ggll.exceptions.InvalidGrammarException;
import org.ggll.syntax.graph.SyntaxGraphRepository;

public class HeaderValidation extends GrammarRule
{
	@Override
	public void validate() throws InvalidGrammarException
	{
		if (SyntaxGraphRepository.getStarts().count() == 0)
		{
			throw new InvalidGrammarException("There must be an initial non-terminal.");
		}
		if (SyntaxGraphRepository.getStarts().count() > 1)
		{
			throw new InvalidGrammarException("There must only one initial non-terminal.");
		}
	}
}

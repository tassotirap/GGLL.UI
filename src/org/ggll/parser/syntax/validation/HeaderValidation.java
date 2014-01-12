package org.ggll.parser.syntax.validation;

import org.ggll.syntax.graph.SyntaxGraphRepository;

public class HeaderValidation extends GrammarValidation
{
	@Override
	public void validate()
	{
		if (SyntaxGraphRepository.getStarts().count() == 0)
		{
			addError("There must be an initial non-terminal.");
		}
		if (SyntaxGraphRepository.getStarts().count() > 1)
		{
			addError("There must only one initial non-terminal.");
		}
	}
}

package org.ggll.grammar.validation;

import org.ggll.syntax.graph.state.StateHelper;

public class HeaderValidation extends GrammarValidation
{
	@Override
	public void validate()
	{
		if (StateHelper.getStarts().count() == 0)
		{
			addError("There must be an initial non-terminal.");
		}
		if (StateHelper.getStarts().count() > 1)
		{
			addError("There must only one initial non-terminal.");
		}
	}
}

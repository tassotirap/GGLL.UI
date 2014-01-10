package org.ggll.parser.syntax.validation;

import ggll.core.list.ExtendedList;

import org.ggll.exceptions.InvalidGrammarException;
import org.ggll.syntax.graph.SyntaxGraphRepository;
import org.ggll.syntax.graph.state.StateConnection;
import org.ggll.syntax.graph.state.StateNode;

public class LeftSideValidation extends GrammarRule
{
	@Override
	public void validate() throws InvalidGrammarException
	{
		String errors = "";
		final ExtendedList<String> leftSidesLabels = new ExtendedList<String>();
		for (final StateNode leftside : SyntaxGraphRepository.getLeftSides().getAll())
		{
			if (!leftSidesLabels.contains(leftside.getTitle()))
			{
				leftSidesLabels.append(leftside.getTitle());
			}
			else
			{
				errors += "Only one left hand by label is allowed, \"" + leftside.getTitle() + "\" are duplicated.\n";
			}

			boolean hasSucessor = false;
			for (final StateConnection sucessor : SyntaxGraphRepository.getSucessors().getAll())
			{
				if (sucessor.getSource().equals(leftside.getId()))
				{
					hasSucessor = true;
				}
			}
			if (!hasSucessor)
			{
				errors += "Left side \"" + leftside.getTitle() + "\" must have one sucessor.\n";
			}
		}

		if (!errors.equals(""))
		{
			throw new InvalidGrammarException(errors);
		}
	}
}

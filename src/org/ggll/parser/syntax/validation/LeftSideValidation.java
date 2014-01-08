package org.ggll.parser.syntax.validation;

import ggll.core.list.ExtendedList;

import org.ggll.exceptions.InvalidGrammarException;
import org.ggll.syntax.graph.SyntaxGraphRepository;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;

public class LeftSideValidation extends GrammarRule
{
	@Override
	public void validate() throws InvalidGrammarException
	{
		String errors = "";
		ExtendedList<String> leftSidesLabels = new ExtendedList<String>();
		for (LabelWidget leftside : SyntaxGraphRepository.getLeftSides().getAll())
		{
			if (!leftSidesLabels.contains(leftside.getLabel()))
			{
				leftSidesLabels.append(leftside.getLabel());
			}
			else
			{
				errors += "Only one left hand by label is allowed, \"" + leftside.getLabel() + "\" are duplicated.\n";
			}
	
			boolean hasSucessor = false;
			for (ConnectionWidget sucessor : SyntaxGraphRepository.getSucessors().getAll())
			{
				if (sucessor.getSourceAnchor().getRelatedWidget() == leftside)
				{
					hasSucessor = true;
				}
			}
			if (!hasSucessor)
			{
				errors += "Left side \"" + leftside.getLabel() + "\" must have one sucessor.\n";				
			}
		}

		if (!errors.equals(""))
		{
			throw new InvalidGrammarException(errors);
		}
	}
}

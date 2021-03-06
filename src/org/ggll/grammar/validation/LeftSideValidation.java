package org.ggll.grammar.validation;

import ggll.core.list.ExtendedList;

import org.ggll.syntax.graph.state.StateConnection;
import org.ggll.syntax.graph.state.StateHelper;
import org.ggll.syntax.graph.state.StateNode;

public class LeftSideValidation extends GrammarValidation
{
	@Override
	public void validate()
	{
		final ExtendedList<String> leftSidesLabels = new ExtendedList<String>();
		for (final StateNode leftside : StateHelper.getLeftSides().getAll())
		{
			if (!leftSidesLabels.contains(leftside.getTitle()))
			{
				leftSidesLabels.append(leftside.getTitle());
			}
			else
			{
				addError("Only one left hand by label is allowed", leftside);
			}
			
			boolean hasSucessor = false;
			for (final StateConnection sucessor : StateHelper.getSucessors().getAll())
			{
				if (sucessor.getSource().equals(leftside.getId()))
				{
					hasSucessor = true;
				}
			}
			if (!hasSucessor)
			{
				addError("Left side must have a sucessor.", leftside);
			}
		}
	}
}

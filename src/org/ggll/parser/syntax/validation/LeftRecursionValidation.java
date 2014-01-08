package org.ggll.parser.syntax.validation;

import org.ggll.exceptions.InvalidGrammarException;
import org.ggll.parser.syntax.grammar.model.AbstractNode;
import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraphRepository;
import org.ggll.syntax.graph.widget.LabelWidgetExt;
import org.netbeans.api.visual.widget.LabelWidget;

public class LeftRecursionValidation extends GrammarRule
{
	@Override
	public void validate() throws InvalidGrammarException
	{
		for (LabelWidget leftside : SyntaxGraphRepository.getLeftSides().getAll())
		{
			String object = SyntaxGraphRepository.findObject(leftside);
			LabelWidgetExt nextWidget = (LabelWidgetExt) SyntaxGraphRepository.findSucessorWidget(object);
			while (nextWidget != null)
			{
				if (nextWidget.getType().equals(CanvasResource.N_TERMINAL))
				{
					if (leftside.getLabel().equals(nextWidget.getLabel()))
					{
						throw new InvalidGrammarException("Left Recursion at node \"" + leftside.getLabel() + "\".");
					}
					else
					{
						nextWidget = (LabelWidgetExt) SyntaxGraphRepository.findLeftSideByLabel(nextWidget.getLabel());
						continue;
					}
				}

				String nextObject = SyntaxGraphRepository.findObject(nextWidget);
				nextWidget = (LabelWidgetExt) SyntaxGraphRepository.findSucessorWidget(nextObject);
			}

		}

	}
}

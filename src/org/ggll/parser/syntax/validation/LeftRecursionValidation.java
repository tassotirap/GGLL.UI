package org.ggll.parser.syntax.validation;

import ggll.core.list.ExtendedList;

import org.ggll.exceptions.InvalidGrammarException;
import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraphRepository;
import org.ggll.syntax.graph.state.StateNode;

public class LeftRecursionValidation extends GrammarRule
{
	@Override
	public void validate() throws InvalidGrammarException
	{
		for (final StateNode leftside : SyntaxGraphRepository.getLeftSides().getAll())
		{
			final ExtendedList<StateNode> nTerminals = new ExtendedList<StateNode>();
			final ExtendedList<StateNode> nodes = new ExtendedList<StateNode>();
			walkGraph(SyntaxGraphRepository.findSucessorWidget(leftside), nTerminals, nodes);
			for (StateNode nTerminal : nTerminals.getAll())
			{
				if (nTerminal.getTitle().equals(leftside.getTitle()))
				{
					throw new InvalidGrammarException("Left Recursion at node \"" + leftside.getTitle() + "\".");
				}
			}
		}

	}

	private void walkGraph(StateNode node, ExtendedList<StateNode> nTerminals, ExtendedList<StateNode> nodes) throws InvalidGrammarException
	{
		if (node == null)
		{
			return;
		}
		nodes.append(node);
		if (node.getType().equals(CanvasResource.N_TERMINAL))
		{
			if (!nTerminals.contains(node))
			{
				nTerminals.append(node);
				walkGraph(SyntaxGraphRepository.findLeftSide(node), nTerminals, nodes);
			}
		}

		if (!nodes.contains(SyntaxGraphRepository.findSucessorWidget(node)))
		{
			walkGraph(SyntaxGraphRepository.findSucessorWidget(node), nTerminals, nodes);
		}
		if (!nodes.contains(SyntaxGraphRepository.findAlternativeWidget(node)))
		{
			walkGraph(SyntaxGraphRepository.findAlternativeWidget(node), nTerminals, nodes);
		}
	}
}

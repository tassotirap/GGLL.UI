package org.ggll.grammar.validation;

import ggll.core.list.ExtendedList;

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraphRepository;
import org.ggll.syntax.graph.state.StateNode;

public class LeftRecursionValidation extends GrammarValidation
{
	@Override
	public void validate()
	{
		for (final StateNode leftside : SyntaxGraphRepository.getLeftSides().getAll())
		{
			final ExtendedList<StateNode> nTerminals = new ExtendedList<StateNode>();
			final ExtendedList<StateNode> nodes = new ExtendedList<StateNode>();
			walkGraph(SyntaxGraphRepository.findSucessorNode(leftside), nTerminals, nodes);
			for (StateNode nTerminal : nTerminals.getAll())
			{
				if (nTerminal.getTitle().equals(leftside.getTitle()))
				{
					addError("Left recursion.", leftside);
				}
			}
		}
	}

	private void walkGraph(StateNode node, ExtendedList<StateNode> nTerminals, ExtendedList<StateNode> nodes)
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

		if (!nodes.contains(SyntaxGraphRepository.findSucessorNode(node)))
		{
			walkGraph(SyntaxGraphRepository.findSucessorNode(node), nTerminals, nodes);
		}
		if (!nodes.contains(SyntaxGraphRepository.findAlternativeNode(node)))
		{
			walkGraph(SyntaxGraphRepository.findAlternativeNode(node), nTerminals, nodes);
		}
	}
}

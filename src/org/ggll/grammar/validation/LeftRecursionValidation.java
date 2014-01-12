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
			walkGraph(leftside, nTerminals, nodes);
			for (StateNode nTerminal : nTerminals.getAll())
			{
				if (nTerminal.getTitle().equals(leftside.getTitle()))
				{
					addError("Left recursion.", leftside);
				}
			}
		}
	}

	private void alternativeNodes(StateNode node, ExtendedList<StateNode> nTerminals, ExtendedList<StateNode> nodes)
	{
		StateNode alternative = SyntaxGraphRepository.findAlternativeNode(node);
		while (alternative != null && !nodes.contains(alternative))
		{
			if (alternative.getType().equals(CanvasResource.N_TERMINAL))
			{
				if (!nTerminals.contains(alternative))
				{
					nTerminals.append(alternative);
					StateNode leftSide = SyntaxGraphRepository.findLeftSide(alternative);
					walkGraph(leftSide, nTerminals, nodes);
				}
			}
			alternative = SyntaxGraphRepository.findAlternativeNode(alternative);
		}
	}

	private void sucessorNodes(StateNode node, ExtendedList<StateNode> nTerminals, ExtendedList<StateNode> nodes)
	{
		StateNode sucessor = SyntaxGraphRepository.findSucessorNode(node);
		if (sucessor != null && !nodes.contains(sucessor))
		{
			if (sucessor.getType().equals(CanvasResource.N_TERMINAL))
			{
				if (!nTerminals.contains(sucessor))
				{
					nTerminals.append(sucessor);
					StateNode leftSide = SyntaxGraphRepository.findLeftSide(sucessor);
					walkGraph(leftSide, nTerminals, nodes);
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
		sucessorNodes(node, nTerminals, nodes);
		alternativeNodes(SyntaxGraphRepository.findSucessorNode(node), nTerminals, nodes);
	}
}

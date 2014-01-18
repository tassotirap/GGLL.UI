package org.ggll.grammar.validation;

import ggll.core.list.ExtendedList;

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.state.StateNode;
import org.ggll.syntax.graph.state.StateHelper;

public class LeftRecursionValidation extends GrammarValidation
{
	private void alternativeNodes(StateNode node, ExtendedList<StateNode> nTerminals, ExtendedList<StateNode> nodes)
	{
		StateNode alternative = StateHelper.findAlternativeNode(node);
		while (alternative != null && !nodes.contains(alternative))
		{
			if (alternative.getType().equals(CanvasResource.N_TERMINAL))
			{
				if (!nTerminals.contains(alternative))
				{
					nTerminals.append(alternative);
					StateNode leftSide = StateHelper.findLeftSide(alternative);
					walkGraph(leftSide, nTerminals, nodes);
				}
			}
			alternative = StateHelper.findAlternativeNode(alternative);
		}
	}

	private void sucessorNodes(StateNode node, ExtendedList<StateNode> nTerminals, ExtendedList<StateNode> nodes)
	{
		StateNode sucessor = StateHelper.findSucessorNode(node);
		if (sucessor != null && !nodes.contains(sucessor))
		{
			if (sucessor.getType().equals(CanvasResource.N_TERMINAL))
			{
				if (!nTerminals.contains(sucessor))
				{
					nTerminals.append(sucessor);
					StateNode leftSide = StateHelper.findLeftSide(sucessor);
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
		alternativeNodes(StateHelper.findSucessorNode(node), nTerminals, nodes);
	}

	@Override
	public void validate()
	{
		for (final StateNode leftside : StateHelper.getLeftSides().getAll())
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
}

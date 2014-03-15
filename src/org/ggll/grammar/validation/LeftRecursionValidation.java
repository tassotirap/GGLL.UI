package org.ggll.grammar.validation;

import ggll.core.list.ExtendedList;

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.state.StateHelper;
import org.ggll.syntax.graph.state.StateNode;

public class LeftRecursionValidation extends GrammarValidation
{
	private void alternativeNodes(final StateNode node, final ExtendedList<StateNode> nTerminals, final ExtendedList<StateNode> nodes)
	{
		StateNode alternative = StateHelper.findAlternativeNode(node);
		while (alternative != null && !nodes.contains(alternative))
		{
			if (alternative.getType().equals(CanvasResource.N_TERMINAL))
			{
				if (!nTerminals.contains(alternative))
				{
					nTerminals.append(alternative);
					final StateNode leftSide = StateHelper.findLeftSide(alternative);
					walkGraph(leftSide, nTerminals, nodes);
				}
			}
			alternative = StateHelper.findAlternativeNode(alternative);
		}
	}
	
	private void sucessorNodes(final StateNode node, final ExtendedList<StateNode> nTerminals, final ExtendedList<StateNode> nodes)
	{
		final StateNode sucessor = StateHelper.findSucessorNode(node);
		if (sucessor != null && !nodes.contains(sucessor))
		{
			if (sucessor.getType().equals(CanvasResource.N_TERMINAL))
			{
				if (!nTerminals.contains(sucessor))
				{
					nTerminals.append(sucessor);
					final StateNode leftSide = StateHelper.findLeftSide(sucessor);
					walkGraph(leftSide, nTerminals, nodes);
				}
			}
		}
	}
	
	@Override
	public void validate()
	{
		for (final StateNode leftside : StateHelper.getLeftSides().getAll())
		{
			final ExtendedList<StateNode> nTerminals = new ExtendedList<StateNode>();
			final ExtendedList<StateNode> nodes = new ExtendedList<StateNode>();
			walkGraph(leftside, nTerminals, nodes);
			for (final StateNode nTerminal : nTerminals.getAll())
			{
				if (nTerminal.getTitle().equals(leftside.getTitle()))
				{
					addError("Left recursion.", leftside);
				}
			}
		}
	}
	
	private void walkGraph(final StateNode node, final ExtendedList<StateNode> nTerminals, final ExtendedList<StateNode> nodes)
	{
		if (node == null) { return; }
		nodes.append(node);
		sucessorNodes(node, nTerminals, nodes);
		alternativeNodes(StateHelper.findSucessorNode(node), nTerminals, nodes);
	}
}

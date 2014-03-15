package org.ggll.grammar.validation;

import ggll.core.list.ExtendedList;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.state.StateConnection;
import org.ggll.syntax.graph.state.StateHelper;
import org.ggll.syntax.graph.state.StateNode;

public class TerminalValidation extends GrammarValidation
{
	
	private void alternativeNodes(final StateNode node, final ExtendedList<String> first, final ExtendedList<StateNode> nodes)
	{
		StateNode alternative = StateHelper.findAlternativeNode(node);
		while (alternative != null)
		{
			if (alternative.getType().equals(CanvasResource.N_TERMINAL))
			{
				final StateNode leftSide = StateHelper.findLeftSide(alternative);
				walkGraph(leftSide, first, nodes);
			}
			else if (alternative.getType().equals(CanvasResource.TERMINAL))
			{
				first.append(alternative.getTitle());
			}
			alternative = StateHelper.findAlternativeNode(alternative);
		}
	}
	
	private void sucessorNodes(final StateNode node, final ExtendedList<String> first, final ExtendedList<StateNode> nodes)
	{
		final StateNode sucessor = StateHelper.findSucessorNode(node);
		if (sucessor != null)
		{
			if (sucessor.getType().equals(CanvasResource.N_TERMINAL))
			{
				final StateNode leftSide = StateHelper.findLeftSide(sucessor);
				walkGraph(leftSide, first, nodes);
			}
			else if (sucessor.getType().equals(CanvasResource.TERMINAL))
			{
				first.append(sucessor.getTitle());
			}
		}
	}
	
	@Override
	public void validate()
	{
		for (final StateConnection connection : StateHelper.getSucessors().getAll())
		{
			final StateNode node = StateHelper.findNode(connection.getSource());
			final ExtendedList<String> first = new ExtendedList<String>();
			final ExtendedList<StateNode> nodes = new ExtendedList<StateNode>();
			walkGraph(node, first, nodes);
			final Set<String> unique = new HashSet<String>(first.getAll());
			for (final String key : unique)
			{
				if (Collections.frequency(first.getAll(), key) > 1)
				{
					addError("Duplicated terminal \"<a href='Label|" + key + "'>" + key + "</a>\" in a sequence of alternatives.", node);
				}
			}
		}
		
	}
	
	private void walkGraph(final StateNode node, final ExtendedList<String> first, final ExtendedList<StateNode> nodes)
	{
		if (node == null) { return; }
		if (!nodes.contains(node))
		{
			nodes.append(node);
			sucessorNodes(node, first, nodes);
			alternativeNodes(StateHelper.findSucessorNode(node), first, nodes);
		}
	}
	
}

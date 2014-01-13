package org.ggll.grammar.validation;

import ggll.core.list.ExtendedList;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraphRepository;
import org.ggll.syntax.graph.state.StateConnection;
import org.ggll.syntax.graph.state.StateNode;

public class TerminalValidation extends GrammarValidation
{

	@Override
	public void validate()
	{
		for (final StateConnection connection : SyntaxGraphRepository.getSucessors().getAll())
		{
			StateNode node = SyntaxGraphRepository.findNode(connection.getSource());
			ExtendedList<String> first = new ExtendedList<String>();
			ExtendedList<StateNode> nodes = new ExtendedList<StateNode>();
			walkGraph(node, first, nodes);
			Set<String> unique = new HashSet<String>(first.getAll());
			for (String key : unique)
			{
				if (Collections.frequency(first.getAll(), key) > 1)
				{
					addError("Duplicated terminal \"" + key + "\".", node);
				}
			}
		}

	}

	private void walkGraph(StateNode node, ExtendedList<String> first, ExtendedList<StateNode> nodes)
	{
		if (node == null)
		{
			return;
		}
		nodes.append(node);
		sucessorNodes(node, first, nodes);
		alternativeNodes(SyntaxGraphRepository.findSucessorNode(node), first, nodes);
	}

	private void alternativeNodes(StateNode node, ExtendedList<String> first, ExtendedList<StateNode> nodes)
	{
		StateNode alternative = SyntaxGraphRepository.findAlternativeNode(node);
		while (alternative != null && !nodes.contains(alternative))
		{
			if (alternative.getType().equals(CanvasResource.N_TERMINAL))
			{
				StateNode leftSide = SyntaxGraphRepository.findLeftSide(alternative);
				walkGraph(leftSide, first, nodes);
			}
			else if (alternative.getType().equals(CanvasResource.TERMINAL))
			{
				first.append(alternative.getTitle());
			}
			alternative = SyntaxGraphRepository.findAlternativeNode(alternative);
		}
	}

	private void sucessorNodes(StateNode node, ExtendedList<String> first, ExtendedList<StateNode> nodes)
	{
		StateNode sucessor = SyntaxGraphRepository.findSucessorNode(node);
		if (sucessor != null && !nodes.contains(sucessor))
		{
			if (sucessor.getType().equals(CanvasResource.N_TERMINAL))
			{
				StateNode leftSide = SyntaxGraphRepository.findLeftSide(sucessor);
				walkGraph(leftSide, first, nodes);
			}
			else if (sucessor.getType().equals(CanvasResource.TERMINAL))
			{
				first.append(sucessor.getTitle());
			}
		}
	}

}

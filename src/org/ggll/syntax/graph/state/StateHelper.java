package org.ggll.syntax.graph.state;

import ggll.core.list.ExtendedList;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.SyntaxGraphRepository;

public class StateHelper
{


	private StateHelper()
	{
	}

	public static StateNode findAlternativeNode(StateNode node)
	{
		if(node == null) return null;
		for (final SyntaxGraph syntaxGraph : SyntaxGraphRepository.getInstances())
		{
			for (final StateConnection edge : syntaxGraph.getCanvasState().getAlternatives().getAll())
			{
				if (edge.getSource().equals(node.getId()))
				{
					return syntaxGraph.getCanvasState().findNode(edge.getTarget());
				}
			}
		}
		return null;
	}
	

	public static StateNode findLeftSide(StateNode node)
	{
		if(node == null) return null;
		for (final StateNode leftside : getLeftSides().getAll())
		{
			if (leftside.getTitle().equals(node.getTitle()))
			{
				return leftside;
			}
		}
		return null;
	}
	
	public static StateNode findNode(String id)
	{
		if(id == null) return null;
		for (final SyntaxGraph syntaxGraph : SyntaxGraphRepository.getInstances())
		{
			if(syntaxGraph.getCanvasState().findNode(id) != null)
			{
				return syntaxGraph.getCanvasState().findNode(id);
				
			}
		}
		return null;
	}
	
	public static StateNode findSucessorNode(StateNode node)
	{
		if(node == null) return null;
		for (final SyntaxGraph syntaxGraph : SyntaxGraphRepository.getInstances())
		{
			for (final StateConnection edge : syntaxGraph.getCanvasState().getSucessors().getAll())
			{
				if (edge.getSource().equals(node.getId()))
				{
					return syntaxGraph.getCanvasState().findNode(edge.getTarget());
				}
			}
		}
		return null;
	}

	public static int getLastCustomNode()
	{
		int lastCustomNode = 0;
		for (final SyntaxGraph canvas : SyntaxGraphRepository.getInstances())
		{
			lastCustomNode += canvas.getCanvasState().getLastCustomNode();
		}
		return lastCustomNode;
	}

	public static int getLastLAMBDA()
	{
		int lastLAMBDA = 0;
		for (final SyntaxGraph canvas : SyntaxGraphRepository.getInstances())
		{
			lastLAMBDA += canvas.getCanvasState().getLastLAMBDA();
		}
		return lastLAMBDA;
	}

	public static int getLastLeftSides()
	{
		int lastLeftSides = 0;
		for (final SyntaxGraph canvas : SyntaxGraphRepository.getInstances())
		{
			lastLeftSides += canvas.getCanvasState().getLastLeftSides();
		}
		return lastLeftSides;
	}

	public static int getLastNTerminalId()
	{
		int lastNTerminalId = 0;
		for (final SyntaxGraph canvas : SyntaxGraphRepository.getInstances())
		{
			lastNTerminalId += canvas.getCanvasState().getLastNTerminalId();
		}
		return lastNTerminalId;
	}

	public static int getLastSTART()
	{
		int lastSTART = 0;
		for (final SyntaxGraph canvas : SyntaxGraphRepository.getInstances())
		{
			lastSTART += canvas.getCanvasState().getLastSTART();
		}
		return lastSTART;
	}

	public static int getLastTerminalId()
	{
		int lastTerminalId = 0;
		for (final SyntaxGraph canvas : SyntaxGraphRepository.getInstances())
		{
			lastTerminalId += canvas.getCanvasState().getLastTerminalId();
		}
		return lastTerminalId;
	}

	public static ExtendedList<StateNode> getLeftSides()
	{
		final ExtendedList<StateNode> leftSides = new ExtendedList<StateNode>();
		for (final SyntaxGraph syntaxGraph : SyntaxGraphRepository.getInstances())
		{
			leftSides.addAll(syntaxGraph.getCanvasState().getStarts());
		}
		for (final SyntaxGraph syntaxGraph : SyntaxGraphRepository.getInstances())
		{
			leftSides.addAll(syntaxGraph.getCanvasState().getLeftSide());
		}		
		return leftSides;
	}
	
	public static ExtendedList<StateNode> getNTerminals()
	{
		final ExtendedList<StateNode> nTerminals = new ExtendedList<StateNode>();
		for (final SyntaxGraph syntaxGraph : SyntaxGraphRepository.getInstances())
		{
			nTerminals.addAll(syntaxGraph.getCanvasState().getNTerminal());
		}
		return nTerminals;
	}

	public static ExtendedList<StateNode> getStarts()
	{
		final ExtendedList<StateNode> starts = new ExtendedList<StateNode>();
		for (final SyntaxGraph syntaxGraph : SyntaxGraphRepository.getInstances())
		{
			starts.addAll(syntaxGraph.getCanvasState().getStarts());
		}
		return starts;
	}
	
	public static ExtendedList<StateNode> getStateNodes()
	{
		final ExtendedList<StateNode> stateNodes = new ExtendedList<StateNode>();
		for (final SyntaxGraph syntaxGraph : SyntaxGraphRepository.getInstances())
		{
			stateNodes.addAll(syntaxGraph.getCanvasState().getStateNodes());
		}
		return stateNodes;
	}

	public static ExtendedList<StateConnection> getSucessors()
	{
		final ExtendedList<StateConnection> sucessors = new ExtendedList<StateConnection>();
		for (final SyntaxGraph syntaxGraph : SyntaxGraphRepository.getInstances())
		{
			sucessors.addAll(syntaxGraph.getCanvasState().getSucessors());
		}
		return sucessors;
	}
}

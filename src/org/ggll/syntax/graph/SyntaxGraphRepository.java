package org.ggll.syntax.graph;

import ggll.core.list.ExtendedList;

import java.util.Collection;
import java.util.Hashtable;

import org.ggll.director.GGLLDirector;
import org.ggll.file.GrammarFile;
import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.state.StateConnection;
import org.ggll.syntax.graph.state.StateNode;
import org.ggll.syntax.graph.widget.LabelWidgetExt;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;

public class SyntaxGraphRepository
{
	private static String connStrategy = CanvasResource.R_ORTHOGONAL;
	private static String defaultCursor = CanvasResource.SELECT;
	private static String moveStrategy = CanvasResource.M_ALIGN;

	private static Hashtable<String, SyntaxGraph> instances;

	private SyntaxGraphRepository()
	{
	}

	public static StateNode findLeftSide(StateNode node)
	{
		for (final StateNode leftside : getLeftSides().getAll())
		{
			if (leftside.getTitle().equals(node.getTitle()))
			{
				return leftside;
			}
		}
		return null;
	}

	public static Widget findLeftSideByLabel(String label)
	{
		for (final SyntaxGraph syntaxGraph : getInstances())
		{
			for (final String node : syntaxGraph.getNodes())
			{
				final Widget widget = syntaxGraph.findWidget(node);
				if (widget instanceof LabelWidgetExt)
				{
					final LabelWidgetExt labelWidgetExt = (LabelWidgetExt) widget;
					if (labelWidgetExt.getLabel().equals(label) && labelWidgetExt.getType().equals(CanvasResource.LEFT_SIDE))
					{
						return labelWidgetExt;
					}
				}
			}
		}
		return null;
	}

	public static String findObject(Widget widget)
	{
		for (final SyntaxGraph syntaxGraph : getInstances())
		{
			if (syntaxGraph.findObject(widget) != null)
			{
				return (String) syntaxGraph.findObject(widget);
			}
		}
		return null;
	}

	public static Widget findSucessorNode(String node)
	{
		final ConnectionWidget cnn = (ConnectionWidget) findWidget(node);
		return cnn.getSourceAnchor().getRelatedWidget();
	}

	public static StateNode findSucessorWidget(StateNode node)
	{
		for (final SyntaxGraph syntaxGraph : getInstances())
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
	
	public static StateNode findAlternativeWidget(StateNode node)
	{
		for (final SyntaxGraph syntaxGraph : getInstances())
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

	public static Widget findWidget(String node)
	{
		for (final SyntaxGraph syntaxGraph : getInstances())
		{
			if (syntaxGraph.findWidget(node) != null)
			{
				return syntaxGraph.findWidget(node);
			}
		}
		return null;
	}

	public static SyntaxGraph getInstance(String file)
	{
		if (instances == null)
		{
			instances = new Hashtable<String, SyntaxGraph>();
		}

		if (!instances.containsKey(file))
		{
			final SyntaxGraph canvasFactory = new SyntaxGraph(defaultCursor, connStrategy, moveStrategy, file);
			GGLLDirector.getProject().setGrammarFile(new GrammarFile(file));
			instances.put(file, canvasFactory);
		}

		return instances.get(file);
	}

	public static Collection<SyntaxGraph> getInstances()
	{
		refresh();
		return instances.values();
	}

	public static int getLastCustomNode()
	{
		int lastCustomNode = 0;
		for (final SyntaxGraph canvas : getInstances())
		{
			lastCustomNode += canvas.getCanvasState().getLastCustomNode();
		}
		return lastCustomNode;
	}

	public static int getLastLAMBDA()
	{
		int lastLAMBDA = 0;
		for (final SyntaxGraph canvas : getInstances())
		{
			lastLAMBDA += canvas.getCanvasState().getLastLAMBDA();
		}
		return lastLAMBDA;
	}

	public static int getLastLeftSides()
	{
		int lastLeftSides = 0;
		for (final SyntaxGraph canvas : getInstances())
		{
			lastLeftSides += canvas.getCanvasState().getLastLeftSides();
		}
		return lastLeftSides;
	}

	public static int getLastNTerminalId()
	{
		int lastNTerminalId = 0;
		for (final SyntaxGraph canvas : getInstances())
		{
			lastNTerminalId += canvas.getCanvasState().getLastNTerminalId();
		}
		return lastNTerminalId;
	}

	public static int getLastSTART()
	{
		int lastSTART = 0;
		for (final SyntaxGraph canvas : getInstances())
		{
			lastSTART += canvas.getCanvasState().getLastSTART();
		}
		return lastSTART;
	}

	public static int getLastTerminalId()
	{
		int lastTerminalId = 0;
		for (final SyntaxGraph canvas : getInstances())
		{
			lastTerminalId += canvas.getCanvasState().getLastTerminalId();
		}
		return lastTerminalId;
	}

	public static ExtendedList<StateNode> getLeftSides()
	{
		final ExtendedList<StateNode> leftSides = new ExtendedList<StateNode>();
		for (final SyntaxGraph syntaxGraph : getInstances())
		{
			leftSides.addAll(syntaxGraph.getCanvasState().getLeftSide());
		}
		for (final SyntaxGraph syntaxGraph : getInstances())
		{
			leftSides.addAll(syntaxGraph.getCanvasState().getStarts());
		}
		return leftSides;
	}

	public static ExtendedList<StateNode> getStarts()
	{
		final ExtendedList<StateNode> starts = new ExtendedList<StateNode>();
		for (final SyntaxGraph syntaxGraph : getInstances())
		{
			starts.addAll(syntaxGraph.getCanvasState().getStarts());
		}
		return starts;
	}

	public static ExtendedList<StateConnection> getSucessors()
	{
		final ExtendedList<StateConnection> sucessors = new ExtendedList<StateConnection>();
		for (final SyntaxGraph syntaxGraph : getInstances())
		{
			sucessors.addAll(syntaxGraph.getCanvasState().getSucessors());
		}
		return sucessors;
	}

	public static void refresh()
	{
		for (final GrammarFile grammar : GGLLDirector.getProject().getGrammarFile().getAll())
		{
			getInstance(grammar.getAbsolutePath());
		}
	}
}

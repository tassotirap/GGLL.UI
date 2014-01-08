package org.ggll.syntax.graph;

import ggll.core.list.ExtendedList;

import java.util.Collection;
import java.util.Hashtable;

import org.ggll.director.GGLLDirector;
import org.ggll.file.GrammarFile;
import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.widget.LabelWidgetExt;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
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

	public static ExtendedList<LabelWidget> getLeftSides()
	{
		ExtendedList<LabelWidget> leftSides = new ExtendedList<LabelWidget>();
		for (SyntaxGraph syntaxGraph : getInstances())
		{
			for (String leftSide : syntaxGraph.getLeftSides().getAll())
			{
				if (syntaxGraph.findWidget(leftSide) == null)
					continue;

				LabelWidget widget = (LabelWidget) syntaxGraph.findWidget(leftSide);

				leftSides.append(widget);
			}
		}
		return leftSides;
	}

	public static Widget findSucessorWidget(String node)
	{
		for (SyntaxGraph syntaxGraph : getInstances())
		{
			if(syntaxGraph.findWidget(node) == null)
				continue;
			
			Widget widget = syntaxGraph.findWidget(node);
			for (String edge : syntaxGraph.getEdges())
			{
				ConnectionWidget cnn = (ConnectionWidget)syntaxGraph.findWidget(edge);
				if(cnn.getSourceAnchor().getRelatedWidget() == widget)
				{
					return cnn.getTargetAnchor().getRelatedWidget();
				}
			}
		}
		return null;
	}
	
	public static Widget findSucessorWidget(Widget node)
	{
		for (SyntaxGraph syntaxGraph : getInstances())
		{
			if(syntaxGraph.findWidget(node) == null)
				continue;
			
			Widget widget = syntaxGraph.findWidget(node);
			for (String edge : syntaxGraph.getEdges())
			{
				ConnectionWidget cnn = (ConnectionWidget)syntaxGraph.findWidget(edge);
				if(cnn.getSourceAnchor().getRelatedWidget() == widget)
				{
					return cnn.getSourceAnchor().getRelatedWidget();
				}
			}
		}
		return null;
	}

	public static Widget findSucessorNode(String node)
	{
		ConnectionWidget cnn = (ConnectionWidget) findWidget(node);
		return cnn.getSourceAnchor().getRelatedWidget();
	}

	public static Widget findWidget(String node)
	{
		for (SyntaxGraph syntaxGraph : getInstances())
		{
			if (syntaxGraph.findWidget(node) != null)
			{
				return syntaxGraph.findWidget(node);
			}
		}
		return null;
	}

	public static String findObject(Widget widget)
	{
		for (SyntaxGraph syntaxGraph : getInstances())
		{
			if (syntaxGraph.findObject(widget) != null)
			{
				return (String) syntaxGraph.findObject(widget);
			}
		}
		return null;
	}
	
	public static Widget findLeftSideByLabel(String label)
	{
		for (SyntaxGraph syntaxGraph : getInstances())
		{
			for(String node : syntaxGraph.getNodes())
			{
				Widget widget = syntaxGraph.findWidget(node);
				if(widget instanceof LabelWidgetExt)
				{
					LabelWidgetExt labelWidgetExt = (LabelWidgetExt)widget;
					if(labelWidgetExt.getLabel().equals(label) && labelWidgetExt.getType().equals(CanvasResource.LEFT_SIDE))
					{
						return labelWidgetExt;
					}
				}
			}
		}
		return null;
	}

	public static ExtendedList<LabelWidget> getStarts()
	{
		ExtendedList<LabelWidget> starts = new ExtendedList<LabelWidget>();
		for (SyntaxGraph syntaxGraph : getInstances())
		{
			for (String start : syntaxGraph.getStart().getAll())
			{
				if (syntaxGraph.findWidget(start) == null)
					continue;

				LabelWidget widget = (LabelWidget) syntaxGraph.findWidget(start);

				starts.append(widget);
			}
		}
		return starts;
	}

	public static ExtendedList<ConnectionWidget> getSucessors()
	{
		ExtendedList<ConnectionWidget> sucessors = new ExtendedList<ConnectionWidget>();
		for (SyntaxGraph syntaxGraph : getInstances())
		{
			for (String start : syntaxGraph.getSuccessors().getAll())
			{
				if (syntaxGraph.findWidget(start) == null)
					continue;

				ConnectionWidget widget = (ConnectionWidget) syntaxGraph.findWidget(start);
				sucessors.append(widget);
			}
		}
		return sucessors;
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

	public static void refresh()
	{
		for (final GrammarFile grammar : GGLLDirector.getProject().getGrammarFile().getAll())
		{
			getInstance(grammar.getAbsolutePath());
		}
	}
}

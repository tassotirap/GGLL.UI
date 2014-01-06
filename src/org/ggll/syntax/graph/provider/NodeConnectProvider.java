package org.ggll.syntax.graph.provider;


import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.Collection;

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.widget.IconNodeWidgetExt;
import org.ggll.syntax.graph.widget.LabelWidgetExt;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

public class NodeConnectProvider implements ConnectProvider
{

	private String source = null;
	private String target = null;
	private final SyntaxGraph canvas;

	public NodeConnectProvider(SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}

	@Override
	public void createConnection(Widget sourceWidget, Widget targetWidget)
	{
		String edge = "";
		int numEdges = 0;
		final Collection<String> edges = this.canvas.getEdges();
		if (edges != null)
		{
			numEdges = edges.size();
		}

		int i = numEdges;
		do
		{
			if (this.canvas.getCanvasActiveTool().equals(CanvasResource.SUCCESSOR))
			{
				edge = CanvasResource.SUC_LBL + String.format("%02d", i);
			}
			else if (this.canvas.getCanvasActiveTool().equals(CanvasResource.ALTERNATIVE))
			{
				edge = CanvasResource.ALT_LBL + String.format("%02d", i);
			}
			else
			{
				edge = "edge" + i;
			}
			i++;
		}
		while (edges.contains(edge));

		this.canvas.addEdge(edge);
		this.canvas.setEdgeSource(edge, this.source);
		this.canvas.setEdgeTarget(edge, this.target);
		this.canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Connection"));
	}

	@Override
	public boolean hasCustomTargetWidgetResolver(Scene scene)
	{
		return false;
	}

	@Override
	public boolean isSourceWidget(Widget sourceWidget)
	{
		final Object object = this.canvas.findObject(sourceWidget);
		this.source = this.canvas.isNode(object) ? (String) object : null;
		if (this.source != null)
		{
			if (this.canvas.getCanvasActiveTool().equals(CanvasResource.ALTERNATIVE) && sourceWidget instanceof LabelWidgetExt)
			{
				final LabelWidgetExt labelWidgetExt = (LabelWidgetExt) sourceWidget;
				if (labelWidgetExt.getType().equals(CanvasResource.LEFT_SIDE) || labelWidgetExt.getType().equals(CanvasResource.START))
				{
					return false;
				}
			}

			final Collection<String> edges = this.canvas.findNodeEdges(this.source, true, false);
			if (edges.size() >= 2)
			{
				return false;
			}
			for (final String e : edges)
			{
				if (this.canvas.getCanvasActiveTool().equals(CanvasResource.SUCCESSOR) && this.canvas.isSuccessor(e))
				{
					return false;
				}
				if (this.canvas.getCanvasActiveTool().equals(CanvasResource.ALTERNATIVE) && this.canvas.isAlternative(e))
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public ConnectorState isTargetWidget(Widget sourceWidget, Widget targetWidget)
	{
		final Object object = this.canvas.findObject(targetWidget);
		this.target = this.canvas.isNode(object) ? (String) object : null;
		if (this.target != null)
		{
			if (targetWidget instanceof LabelWidgetExt)
			{
				final LabelWidgetExt labelWidgetExt = (LabelWidgetExt) targetWidget;
				if (labelWidgetExt.getType().equals(CanvasResource.TERMINAL) || labelWidgetExt.getType().equals(CanvasResource.N_TERMINAL))
				{
					return ConnectorState.ACCEPT;
				}
			}
			else if (targetWidget.getParentWidget() != null && targetWidget.getParentWidget() instanceof IconNodeWidgetExt)
			{
				final IconNodeWidgetExt iconNodeWidgetExt = (IconNodeWidgetExt) targetWidget.getParentWidget();
				if (iconNodeWidgetExt.getType().equals(CanvasResource.LAMBDA))
				{
					return ConnectorState.ACCEPT;
				}
			}
		}
		return object != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
	}

	@Override
	public Widget resolveTargetWidget(Scene scene, Point sceneLocation)
	{
		return null;
	}

}

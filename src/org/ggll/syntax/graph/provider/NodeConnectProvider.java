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
	
	public NodeConnectProvider(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	@Override
	public void createConnection(final Widget sourceWidget, final Widget targetWidget)
	{
		String edge = "";
		int numEdges = 0;
		final Collection<String> edges = canvas.getEdges();
		if (edges != null)
		{
			numEdges = edges.size();
		}
		
		int i = numEdges;
		do
		{
			if (canvas.getCanvasActiveTool().equals(CanvasResource.SUCCESSOR))
			{
				edge = CanvasResource.SUC_LBL + String.format("%02d", i);
			}
			else if (canvas.getCanvasActiveTool().equals(CanvasResource.ALTERNATIVE))
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
		
		canvas.addEdge(edge);
		canvas.setEdgeSource(edge, source);
		canvas.setEdgeTarget(edge, target);
		canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Connection"));
	}
	
	@Override
	public boolean hasCustomTargetWidgetResolver(final Scene scene)
	{
		return false;
	}
	
	@Override
	public boolean isSourceWidget(final Widget sourceWidget)
	{
		final Object object = canvas.findObject(sourceWidget);
		source = canvas.isNode(object) ? (String) object : null;
		if (source != null)
		{
			if (canvas.getCanvasActiveTool().equals(CanvasResource.ALTERNATIVE) && sourceWidget instanceof LabelWidgetExt)
			{
				final LabelWidgetExt labelWidgetExt = (LabelWidgetExt) sourceWidget;
				if (labelWidgetExt.getType().equals(CanvasResource.LEFT_SIDE) || labelWidgetExt.getType().equals(CanvasResource.START)) { return false; }
			}
			
			final Collection<String> edges = canvas.findNodeEdges(source, true, false);
			if (edges.size() >= 2) { return false; }
			for (final String e : edges)
			{
				if (canvas.getCanvasActiveTool().equals(CanvasResource.SUCCESSOR) && canvas.isSuccessor(e)) { return false; }
				if (canvas.getCanvasActiveTool().equals(CanvasResource.ALTERNATIVE) && canvas.isAlternative(e)) { return false; }
			}
			return true;
		}
		return false;
	}
	
	@Override
	public ConnectorState isTargetWidget(final Widget sourceWidget, final Widget targetWidget)
	{
		final Object object = canvas.findObject(targetWidget);
		target = canvas.isNode(object) ? (String) object : null;
		if (target != null)
		{
			if (targetWidget instanceof LabelWidgetExt)
			{
				final LabelWidgetExt labelWidgetExt = (LabelWidgetExt) targetWidget;
				if (labelWidgetExt.getType().equals(CanvasResource.TERMINAL) || labelWidgetExt.getType().equals(CanvasResource.N_TERMINAL)) { return ConnectorState.ACCEPT; }
			}
			else if (targetWidget.getParentWidget() != null && targetWidget.getParentWidget() instanceof IconNodeWidgetExt)
			{
				final IconNodeWidgetExt iconNodeWidgetExt = (IconNodeWidgetExt) targetWidget.getParentWidget();
				if (iconNodeWidgetExt.getType().equals(CanvasResource.LAMBDA)) { return ConnectorState.ACCEPT; }
			}
		}
		return object != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
	}
	
	@Override
	public Widget resolveTargetWidget(final Scene scene, final Point sceneLocation)
	{
		return null;
	}
	
}

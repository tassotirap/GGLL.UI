package org.ggll.syntax.graph.provider;

import java.awt.Point;
import java.beans.PropertyChangeEvent;

import org.ggll.syntax.graph.SyntaxGraph;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.ReconnectProvider;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

public class NodeReconnectProvider implements ReconnectProvider
{
	
	private String edge;
	private String originalNode;
	private String replacementNode;
	private final SyntaxGraph canvas;
	
	public NodeReconnectProvider(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	@Override
	public boolean hasCustomReplacementWidgetResolver(final Scene scene)
	{
		return false;
	}
	
	@Override
	public ConnectorState isReplacementWidget(final ConnectionWidget connectionWidget, final Widget replacementWidget, final boolean reconnectingSource)
	{
		final Object object = canvas.findObject(replacementWidget);
		replacementNode = canvas.isNode(object) ? (String) object : null;
		if (replacementNode != null && edge != null) { return ConnectorState.ACCEPT; }
		return ConnectorState.REJECT;
	}
	
	@Override
	public boolean isSourceReconnectable(final ConnectionWidget connectionWidget)
	{
		/*
		 * Canvas canvas = CanvasFactory.getCanvas(canvasID); Object object =
		 * canvas.findObject (connectionWidget); edge = canvas.isEdge (object) ?
		 * (String) object : null; originalNode = edge != null ?
		 * canvas.getEdgeSource (edge) : null; return originalNode != null;
		 */
		// Actually I prefer to avoid this
		return false;
	}
	
	@Override
	public boolean isTargetReconnectable(final ConnectionWidget connectionWidget)
	{
		final Object object = canvas.findObject(connectionWidget);
		edge = canvas.isEdge(object) ? (String) object : null;
		originalNode = edge != null ? canvas.getEdgeTarget(edge) : null;
		return originalNode != null;
	}
	
	@Override
	public void reconnect(final ConnectionWidget connectionWidget, final Widget replacementWidget, final boolean reconnectingSource)
	{
		if (replacementWidget == null)
		{
			canvas.removeEdge(edge);
			canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Disconnect"));
		}
		else if (reconnectingSource)
		{
			
			canvas.setEdgeSource(edge, replacementNode);
			canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Connection"));
			
		}
		else
		{
			
			canvas.setEdgeTarget(edge, replacementNode);
			canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Connection"));
			
		}
	}
	
	@Override
	public void reconnectingFinished(final ConnectionWidget connectionWidget, final boolean reconnectingSource)
	{
	}
	
	@Override
	public void reconnectingStarted(final ConnectionWidget connectionWidget, final boolean reconnectingSource)
	{
	}
	
	@Override
	public Widget resolveReplacementWidget(final Scene scene, final Point sceneLocation)
	{
		return null;
	}
	
}

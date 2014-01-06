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

	public NodeReconnectProvider(SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}

	@Override
	public boolean hasCustomReplacementWidgetResolver(Scene scene)
	{
		return false;
	}

	@Override
	public ConnectorState isReplacementWidget(ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource)
	{
		final Object object = this.canvas.findObject(replacementWidget);
		this.replacementNode = this.canvas.isNode(object) ? (String) object : null;
		if (this.replacementNode != null && this.edge != null)
		{
			return ConnectorState.ACCEPT;
		}
		return ConnectorState.REJECT;
	}

	@Override
	public boolean isSourceReconnectable(ConnectionWidget connectionWidget)
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
	public boolean isTargetReconnectable(ConnectionWidget connectionWidget)
	{
		final Object object = this.canvas.findObject(connectionWidget);
		this.edge = this.canvas.isEdge(object) ? (String) object : null;
		this.originalNode = this.edge != null ? this.canvas.getEdgeTarget(this.edge) : null;
		return this.originalNode != null;
	}

	@Override
	public void reconnect(ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource)
	{
		if (replacementWidget == null)
		{
			this.canvas.removeEdge(this.edge);
			this.canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Disconnect"));
		}
		else if (reconnectingSource)
		{

			this.canvas.setEdgeSource(this.edge, this.replacementNode);
			this.canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Connection"));

		}
		else
		{

			this.canvas.setEdgeTarget(this.edge, this.replacementNode);
			this.canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Connection"));

		}
	}

	@Override
	public void reconnectingFinished(ConnectionWidget connectionWidget, boolean reconnectingSource)
	{
	}

	@Override
	public void reconnectingStarted(ConnectionWidget connectionWidget, boolean reconnectingSource)
	{
	}

	@Override
	public Widget resolveReplacementWidget(Scene scene, Point sceneLocation)
	{
		return null;
	}

}

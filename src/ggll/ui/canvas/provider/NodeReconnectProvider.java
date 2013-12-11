package ggll.ui.canvas.provider;

import ggll.ui.canvas.Canvas;

import java.awt.Point;
import java.beans.PropertyChangeEvent;

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
	private Canvas canvas;

	public NodeReconnectProvider(Canvas canvas)
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
		Object object = canvas.findObject(replacementWidget);
		replacementNode = canvas.isNode(object) ? (String) object : null;
		if (replacementNode != null && edge != null)
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
		Object object = canvas.findObject(connectionWidget);
		edge = canvas.isEdge(object) ? (String) object : null;
		originalNode = edge != null ? canvas.getEdgeTarget(edge) : null;
		return originalNode != null;
	}

	@Override
	public void reconnect(ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource)
	{
		if (replacementWidget == null)
		{
			canvas.removeEdge(edge);
			canvas.getCanvasStateRepository().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Disconnect"));
		}
		else if (reconnectingSource)
		{

			canvas.setEdgeSource(edge, replacementNode);
			canvas.getCanvasStateRepository().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Connection"));

		}
		else
		{

			canvas.setEdgeTarget(edge, replacementNode);
			canvas.getCanvasStateRepository().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Connection"));

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

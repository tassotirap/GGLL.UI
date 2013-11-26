package ggll.ui.canvas.provider;

import ggll.ui.canvas.AbstractCanvas;

import java.awt.Point;
import java.beans.PropertyChangeSupport;

import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.ReconnectProvider;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

public class NodeReconnectProvider implements ReconnectProvider
{

	private String edge;
	private PropertyChangeSupport monitor;
	private String originalNode;
	private String replacementNode;
	private AbstractCanvas canvas;

	public NodeReconnectProvider(AbstractCanvas canvas)
	{
		this.canvas = canvas;
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener(canvas.getVolatileStateManager());
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
			monitor.firePropertyChange("undoable", null, "Disconnect");
		}
		else if (reconnectingSource)
		{

			canvas.setEdgeSource(edge, replacementNode);
			monitor.firePropertyChange("undoable", null, "Connection");

		}
		else
		{

			canvas.setEdgeTarget(edge, replacementNode);
			monitor.firePropertyChange("undoable", null, "Connection");

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

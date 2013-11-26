package ggll.ui.canvas.provider;

import ggll.ui.canvas.AbstractCanvas;
import ggll.ui.canvas.widget.IconNodeWidgetExt;
import ggll.ui.canvas.widget.LabelWidgetExt;
import ggll.ui.resource.CanvasResource;

import java.awt.Point;
import java.beans.PropertyChangeSupport;
import java.util.Collection;

import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

public class NodeConnectProvider implements ConnectProvider
{

	private PropertyChangeSupport monitor;
	private String source = null;

	private String target = null;
	private AbstractCanvas canvas;

	public NodeConnectProvider(AbstractCanvas canvas)
	{
		this.canvas = canvas;
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener(canvas.getVolatileStateManager());
	}

	@Override
	public void createConnection(Widget sourceWidget, Widget targetWidget)
	{
		String edge = "";
		int numEdges = 0;
		Collection<String> edges = canvas.getEdges();
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
		monitor.firePropertyChange("undoable", null, "Connection");

	}

	@Override
	public boolean hasCustomTargetWidgetResolver(Scene scene)
	{
		return false;
	}

	@Override
	public boolean isSourceWidget(Widget sourceWidget)
	{
		Object object = canvas.findObject(sourceWidget);
		source = canvas.isNode(object) ? (String) object : null;
		if (source != null)
		{
			if (canvas.getCanvasActiveTool().equals(CanvasResource.ALTERNATIVE) && sourceWidget instanceof LabelWidgetExt)
			{
				LabelWidgetExt labelWidgetExt = (LabelWidgetExt) sourceWidget;
				if (labelWidgetExt.getType().equals(CanvasResource.LEFT_SIDE) || labelWidgetExt.getType().equals(CanvasResource.START))
				{
					return false;
				}
			}

			Collection<String> edges = canvas.findNodeEdges(source, true, false);
			if (edges.size() >= 2)
			{
				return false;
			}
			for (String e : edges)
			{
				if (canvas.getCanvasActiveTool().equals(CanvasResource.SUCCESSOR) && canvas.isSuccessor(e))
				{
					return false;
				}
				if (canvas.getCanvasActiveTool().equals(CanvasResource.ALTERNATIVE) && canvas.isAlternative(e))
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
		Object object = canvas.findObject(targetWidget);
		target = canvas.isNode(object) ? (String) object : null;
		if (target != null)
		{
			if (targetWidget instanceof LabelWidgetExt)
			{
				LabelWidgetExt labelWidgetExt = (LabelWidgetExt) targetWidget;
				if (labelWidgetExt.getType().equals(CanvasResource.TERMINAL) || labelWidgetExt.getType().equals(CanvasResource.N_TERMINAL))
				{
					return ConnectorState.ACCEPT;
				}
			}
			else if (targetWidget.getParentWidget() != null && targetWidget.getParentWidget() instanceof IconNodeWidgetExt)
			{
				IconNodeWidgetExt iconNodeWidgetExt = (IconNodeWidgetExt) targetWidget.getParentWidget();
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

package org.ggll.canvas.action;

import java.awt.Point;
import java.beans.PropertyChangeSupport;
import java.util.Collection;

import org.ggll.canvas.Canvas;
import org.ggll.canvas.CanvasData;
import org.ggll.canvas.CanvasFactory;
import org.ggll.canvas.widget.IconNodeWidgetExt;
import org.ggll.canvas.widget.LabelWidgetExt;
import org.ggll.core.syntax.command.CommandFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

public class NodeConnectProvider implements ConnectProvider
{

	private PropertyChangeSupport monitor;
	private String source = null;

	private String target = null;

	public NodeConnectProvider(Canvas canvas)
	{
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener(CanvasFactory.getVolatileStateManager());
	}

	@Override
	public void createConnection(Widget sourceWidget, Widget targetWidget)
	{
		Canvas canvas = CanvasFactory.getCanvas();
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
			if (canvas.getCanvasActiveTool().equals(CanvasData.SUCCESSOR))
			{
				edge = CanvasData.SUC_LBL + i;
			}
			else if (canvas.getCanvasActiveTool().equals(CanvasData.ALTERNATIVE))
			{
				edge = CanvasData.ALT_LBL + i;
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
		monitor.firePropertyChange("undoable", null, CommandFactory.createConnectionCommand());

	}

	@Override
	public boolean hasCustomTargetWidgetResolver(Scene scene)
	{
		return false;
	}

	@Override
	public boolean isSourceWidget(Widget sourceWidget)
	{
		Canvas canvas = CanvasFactory.getCanvas();
		Object object = canvas.findObject(sourceWidget);
		source = canvas.isNode(object) ? (String) object : null;
		if (source != null)
		{
			if (canvas.getCanvasActiveTool().equals(CanvasData.ALTERNATIVE) && sourceWidget instanceof LabelWidgetExt)
			{
				LabelWidgetExt labelWidgetExt = (LabelWidgetExt) sourceWidget;
				if (labelWidgetExt.getType().equals(CanvasData.LEFT_SIDE) || labelWidgetExt.getType().equals(CanvasData.START))
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
				if (canvas.getCanvasActiveTool().equals(CanvasData.SUCCESSOR) && canvas.isSuccessor(e))
				{
					return false;
				}
				if (canvas.getCanvasActiveTool().equals(CanvasData.ALTERNATIVE) && canvas.isAlternative(e))
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
		Canvas canvas = CanvasFactory.getCanvas();
		Object object = canvas.findObject(targetWidget);
		target = canvas.isNode(object) ? (String) object : null;
		if (target != null)
		{
			if (targetWidget instanceof LabelWidgetExt)
			{
				LabelWidgetExt labelWidgetExt = (LabelWidgetExt) targetWidget;
				if (labelWidgetExt.getType().equals(CanvasData.TERMINAL) || labelWidgetExt.getType().equals(CanvasData.N_TERMINAL))
				{
					return ConnectorState.ACCEPT;
				}
			}
			else if(targetWidget.getParentWidget() != null && targetWidget.getParentWidget() instanceof IconNodeWidgetExt)
			{
				IconNodeWidgetExt iconNodeWidgetExt = (IconNodeWidgetExt) targetWidget.getParentWidget();
				if (iconNodeWidgetExt.getType().equals(CanvasData.LAMBDA))
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

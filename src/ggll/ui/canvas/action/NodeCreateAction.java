package ggll.ui.canvas.action;

import ggll.ui.canvas.AbstractCanvas;
import ggll.ui.resource.CanvasResource;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeSupport;

import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

public class NodeCreateAction extends WidgetAction.Adapter
{
	private PropertyChangeSupport monitor;
	private AbstractCanvas canvas;

	public NodeCreateAction(AbstractCanvas canvas)
	{
		this.canvas = canvas;
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener(canvas.getVolatileStateManager());
	}

	private String createDefaultName()
	{
		if (canvas.getCanvasActiveTool().equals(CanvasResource.TERMINAL))
		{
			canvas.getCanvasState().incLastTerminalId();
			return String.format("Terminal%d", canvas.getCanvasState().getLastTerminalId());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasResource.N_TERMINAL))
		{
			canvas.getCanvasState().incLastNTerminalId();
			return String.format("NTerminal%d", canvas.getCanvasState().getLastNTerminalId());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasResource.LEFT_SIDE))
		{
			canvas.getCanvasState().incLastLeftSides();
			return String.format("LeftSide%d", canvas.getCanvasState().getLastLeftSides());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasResource.LAMBDA))
		{
			canvas.getCanvasState().incLastLAMBDA();
			return String.format("Lambda%d", canvas.getCanvasState().getLastLAMBDA());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasResource.START))
		{
			canvas.getCanvasState().incLastSTART();
			return String.format("S%d", canvas.getCanvasState().getLastSTART());
		}
		canvas.getCanvasState().incLastCustomNode();
		return String.format("node%d", canvas.getCanvasState().getLastCustomNode());
	}

	private boolean isNode(AbstractCanvas canvas)
	{
		return canvas.getCanvasActiveTool().equals(CanvasResource.LEFT_SIDE) || canvas.getCanvasActiveTool().equals(CanvasResource.TERMINAL) || canvas.getCanvasActiveTool().equals(CanvasResource.N_TERMINAL) || canvas.getCanvasActiveTool().equals(CanvasResource.LAMBDA) || canvas.getCanvasActiveTool().equals(CanvasResource.LABEL) || canvas.getCanvasActiveTool().equals(CanvasResource.START);
	}

	@Override
	public State mousePressed(Widget widget, WidgetMouseEvent event)
	{		
		if (event.getClickCount() == 1)
		{
			if (event.getButton() == MouseEvent.BUTTON1 && isNode(canvas))
			{
				String name = createDefaultName();
				canvas.addNode(name).setPreferredLocation(widget.convertLocalToScene(event.getPoint()));
				monitor.firePropertyChange("undoable", null, "Add");
			}
		}
		return State.REJECTED;
	}

}

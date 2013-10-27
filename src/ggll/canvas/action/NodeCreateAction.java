package ggll.canvas.action;

import ggll.canvas.AbstractCanvas;
import ggll.canvas.CanvasFactory;
import ggll.canvas.CanvasStrings;
import ggll.core.syntax.command.CommandFactory;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeSupport;

import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

public class NodeCreateAction extends WidgetAction.Adapter
{

	private PropertyChangeSupport monitor;

	public NodeCreateAction(AbstractCanvas canvas)
	{
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener(CanvasFactory.getVolatileStateManager());
	}

	private String createDefaultName()
	{
		AbstractCanvas canvas = CanvasFactory.getCanvas();
		if (canvas.getCanvasActiveTool().equals(CanvasStrings.TERMINAL))
		{
			canvas.getCanvasState().incLastTerminalId();
			return String.format("Terminal%d", canvas.getCanvasState().getLastTerminalId());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasStrings.N_TERMINAL))
		{
			canvas.getCanvasState().incLastNTerminalId();
			return String.format("NTerminal%d", canvas.getCanvasState().getLastNTerminalId());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasStrings.LEFT_SIDE))
		{
			canvas.getCanvasState().incLastLeftSides();
			return String.format("LeftSide%d", canvas.getCanvasState().getLastLeftSides());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasStrings.LAMBDA))
		{
			canvas.getCanvasState().incLastLAMBDA();
			return String.format("Lambda%d", canvas.getCanvasState().getLastLAMBDA());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasStrings.START))
		{
			canvas.getCanvasState().incLastSTART();
			return String.format("S%d", canvas.getCanvasState().getLastSTART());
		}
		canvas.getCanvasState().incLastCustomNode();
		return String.format("node%d", canvas.getCanvasState().getLastCustomNode());
	}

	private boolean isNode(AbstractCanvas canvas)
	{
		return canvas.getCanvasActiveTool().equals(CanvasStrings.LEFT_SIDE) || canvas.getCanvasActiveTool().equals(CanvasStrings.TERMINAL) || canvas.getCanvasActiveTool().equals(CanvasStrings.N_TERMINAL) || canvas.getCanvasActiveTool().equals(CanvasStrings.LAMBDA) || canvas.getCanvasActiveTool().equals(CanvasStrings.LABEL) || canvas.getCanvasActiveTool().equals(CanvasStrings.START);
	}

	@Override
	public State mousePressed(Widget widget, WidgetMouseEvent event)
	{
		AbstractCanvas canvas = CanvasFactory.getCanvas();
		if (event.getClickCount() == 1)
		{
			if (event.getButton() == MouseEvent.BUTTON1 && isNode(canvas))
			{
				String name = createDefaultName();
				canvas.addNode(name).setPreferredLocation(widget.convertLocalToScene(event.getPoint()));
				monitor.firePropertyChange("undoable", null, CommandFactory.createAddCommand());
			}
		}
		return State.REJECTED;
	}

}

package ggll.ui.canvas.action;

import ggll.ui.canvas.Canvas;
import ggll.ui.canvas.CanvasRepository;
import ggll.ui.resource.CanvasResource;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

public class NodeCreateAction extends WidgetAction.Adapter
{
	private Canvas canvas;

	public NodeCreateAction(Canvas canvas)
	{
		this.canvas = canvas;
	}

	private String createDefaultName()
	{
		if (canvas.getCanvasActiveTool().equals(CanvasResource.TERMINAL))
		{
			canvas.getCurrentCanvasState().incLastTerminalId();
			return String.format("Terminal%d", CanvasRepository.getLastTerminalId());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasResource.N_TERMINAL))
		{
			canvas.getCurrentCanvasState().incLastNTerminalId();
			return String.format("NTerminal%d", CanvasRepository.getLastNTerminalId());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasResource.LEFT_SIDE))
		{
			canvas.getCurrentCanvasState().incLastLeftSides();
			return String.format("LeftSide%d", CanvasRepository.getLastLeftSides());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasResource.LAMBDA))
		{
			canvas.getCurrentCanvasState().incLastLAMBDA();
			return String.format("Lambda%d", CanvasRepository.getLastLAMBDA());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasResource.START))
		{
			canvas.getCurrentCanvasState().incLastSTART();
			return String.format("S%d", CanvasRepository.getLastSTART());
		}
		canvas.getCurrentCanvasState().incLastCustomNode();
		return String.format("node%d", CanvasRepository.getLastCustomNode());
	}

	private boolean isNode(Canvas canvas)
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
				canvas.getCanvasStateRepository().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Add"));
			}
		}
		return State.REJECTED;
	}

}

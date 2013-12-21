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
	private final Canvas canvas;

	public NodeCreateAction(Canvas canvas)
	{
		this.canvas = canvas;
	}

	private String createDefaultName()
	{
		if (this.canvas.getCanvasActiveTool().equals(CanvasResource.TERMINAL))
		{
			this.canvas.getCurrentCanvasState().incLastTerminalId();
			return String.format("Terminal%d", CanvasRepository.getLastTerminalId());
		}
		if (this.canvas.getCanvasActiveTool().equals(CanvasResource.N_TERMINAL))
		{
			this.canvas.getCurrentCanvasState().incLastNTerminalId();
			return String.format("NTerminal%d", CanvasRepository.getLastNTerminalId());
		}
		if (this.canvas.getCanvasActiveTool().equals(CanvasResource.LEFT_SIDE))
		{
			this.canvas.getCurrentCanvasState().incLastLeftSides();
			return String.format("LeftSide%d", CanvasRepository.getLastLeftSides());
		}
		if (this.canvas.getCanvasActiveTool().equals(CanvasResource.LAMBDA))
		{
			this.canvas.getCurrentCanvasState().incLastLAMBDA();
			return String.format("Lambda%d", CanvasRepository.getLastLAMBDA());
		}
		if (this.canvas.getCanvasActiveTool().equals(CanvasResource.START))
		{
			this.canvas.getCurrentCanvasState().incLastSTART();
			return String.format("S%d", CanvasRepository.getLastSTART());
		}
		this.canvas.getCurrentCanvasState().incLastCustomNode();
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
			if (event.getButton() == MouseEvent.BUTTON1 && isNode(this.canvas))
			{
				final String name = createDefaultName();
				this.canvas.addNode(name).setPreferredLocation(widget.convertLocalToScene(event.getPoint()));
				this.canvas.getCanvasStateRepository().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Add"));
			}
		}
		return State.REJECTED;
	}

}

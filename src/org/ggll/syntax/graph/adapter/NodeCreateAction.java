package org.ggll.syntax.graph.adapter;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.state.StateHelper;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

public class NodeCreateAction extends WidgetAction.Adapter
{
	private final SyntaxGraph canvas;
	
	public NodeCreateAction(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	private String createDefaultName()
	{
		if (canvas.getCanvasActiveTool().equals(CanvasResource.TERMINAL))
		{
			canvas.getCanvasState().incLastTerminalId();
			return String.format("Terminal%d", StateHelper.getLastTerminalId());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasResource.N_TERMINAL))
		{
			canvas.getCanvasState().incLastNTerminalId();
			return String.format("NTerminal%d", StateHelper.getLastNTerminalId());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasResource.LEFT_SIDE))
		{
			canvas.getCanvasState().incLastLeftSides();
			return String.format("LeftSide%d", StateHelper.getLastLeftSides());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasResource.LAMBDA))
		{
			canvas.getCanvasState().incLastLAMBDA();
			return String.format("Lambda%d", StateHelper.getLastLAMBDA());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasResource.START))
		{
			canvas.getCanvasState().incLastSTART();
			return String.format("S%d", StateHelper.getLastSTART());
		}
		canvas.getCanvasState().incLastCustomNode();
		return String.format("node%d", StateHelper.getLastCustomNode());
	}
	
	private boolean isNode(final SyntaxGraph canvas)
	{
		return canvas.getCanvasActiveTool().equals(CanvasResource.LEFT_SIDE) || canvas.getCanvasActiveTool().equals(CanvasResource.TERMINAL) || canvas.getCanvasActiveTool().equals(CanvasResource.N_TERMINAL) || canvas.getCanvasActiveTool().equals(CanvasResource.LAMBDA) || canvas.getCanvasActiveTool().equals(CanvasResource.LABEL) || canvas.getCanvasActiveTool().equals(CanvasResource.START);
	}
	
	@Override
	public State mousePressed(final Widget widget, final WidgetMouseEvent event)
	{
		if (event.getClickCount() == 1)
		{
			if (event.getButton() == MouseEvent.BUTTON1 && isNode(canvas))
			{
				final String name = createDefaultName();
				canvas.addNode(name).setPreferredLocation(widget.convertLocalToScene(event.getPoint()));
				canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Add"));
			}
		}
		return State.REJECTED;
	}
	
}

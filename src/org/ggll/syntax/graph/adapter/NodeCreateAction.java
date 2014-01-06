package org.ggll.syntax.graph.adapter;


import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.SyntaxGraphRepository;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

public class NodeCreateAction extends WidgetAction.Adapter
{
	private final SyntaxGraph canvas;

	public NodeCreateAction(SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}

	private String createDefaultName()
	{
		if (this.canvas.getCanvasActiveTool().equals(CanvasResource.TERMINAL))
		{
			this.canvas.getCanvasState().incLastTerminalId();
			return String.format("Terminal%d", SyntaxGraphRepository.getLastTerminalId());
		}
		if (this.canvas.getCanvasActiveTool().equals(CanvasResource.N_TERMINAL))
		{
			this.canvas.getCanvasState().incLastNTerminalId();
			return String.format("NTerminal%d", SyntaxGraphRepository.getLastNTerminalId());
		}
		if (this.canvas.getCanvasActiveTool().equals(CanvasResource.LEFT_SIDE))
		{
			this.canvas.getCanvasState().incLastLeftSides();
			return String.format("LeftSide%d", SyntaxGraphRepository.getLastLeftSides());
		}
		if (this.canvas.getCanvasActiveTool().equals(CanvasResource.LAMBDA))
		{
			this.canvas.getCanvasState().incLastLAMBDA();
			return String.format("Lambda%d", SyntaxGraphRepository.getLastLAMBDA());
		}
		if (this.canvas.getCanvasActiveTool().equals(CanvasResource.START))
		{
			this.canvas.getCanvasState().incLastSTART();
			return String.format("S%d", SyntaxGraphRepository.getLastSTART());
		}
		this.canvas.getCanvasState().incLastCustomNode();
		return String.format("node%d", SyntaxGraphRepository.getLastCustomNode());
	}

	private boolean isNode(SyntaxGraph canvas)
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
				this.canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Add"));
			}
		}
		return State.REJECTED;
	}

}

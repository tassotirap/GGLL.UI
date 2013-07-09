package ggll.canvas.action;

import ggll.canvas.Canvas;
import ggll.canvas.CanvasData;
import ggll.canvas.CanvasFactory;
import ggll.core.syntax.command.CommandFactory;
import ggll.project.GGLLManager;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeSupport;

import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

public class NodeCreateAction extends WidgetAction.Adapter
{

	private PropertyChangeSupport monitor;

	public NodeCreateAction(Canvas canvas)
	{
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener(CanvasFactory.getVolatileStateManager());
	}

	private String createDefaultName()
	{
		Canvas canvas = CanvasFactory.getCanvas();
		if (canvas.getCanvasActiveTool().equals(CanvasData.TERMINAL))
		{
			GGLLManager.getProject().getGrammarFile().incLastTerminalId();
			return String.format("Terminal%d", GGLLManager.getProject().getGrammarFile().getLastTerminalId());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasData.N_TERMINAL))
		{
			GGLLManager.getProject().getGrammarFile().incLastNTerminalId();
			return String.format("NTerminal%d", GGLLManager.getProject().getGrammarFile().getLastNTerminalId());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasData.LEFT_SIDE))
		{
			GGLLManager.getProject().getGrammarFile().incLastLeftSides();
			return String.format("LeftSide%d", GGLLManager.getProject().getGrammarFile().getLastLeftSides());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasData.LAMBDA))
		{
			GGLLManager.getProject().getGrammarFile().incLastLAMBDA();
			return String.format("Lambda%d", GGLLManager.getProject().getGrammarFile().getLastLAMBDA());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasData.START))
		{
			GGLLManager.getProject().getGrammarFile().incLastSTART();
			return String.format("S%d", GGLLManager.getProject().getGrammarFile().getLastSTART());
		}
		GGLLManager.getProject().getGrammarFile().incLastCustomNode();
		return String.format("node%d", GGLLManager.getProject().getGrammarFile().getLastCustomNode());
	}

	private boolean isNode(Canvas canvas)
	{
		return canvas.getCanvasActiveTool().equals(CanvasData.LEFT_SIDE) || canvas.getCanvasActiveTool().equals(CanvasData.TERMINAL) || canvas.getCanvasActiveTool().equals(CanvasData.N_TERMINAL) || canvas.getCanvasActiveTool().equals(CanvasData.LAMBDA) || canvas.getCanvasActiveTool().equals(CanvasData.LABEL) || canvas.getCanvasActiveTool().equals(CanvasData.START);
	}

	@Override
	public State mousePressed(Widget widget, WidgetMouseEvent event)
	{
		Canvas canvas = CanvasFactory.getCanvas();
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

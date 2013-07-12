package ggll.canvas.action;

import ggll.canvas.AbstractCanvas;
import ggll.canvas.CanvasStrings;
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
			GGLLManager.getProject().getGrammarFile().incLastTerminalId();
			return String.format("Terminal%d", GGLLManager.getProject().getGrammarFile().getLastTerminalId());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasStrings.N_TERMINAL))
		{
			GGLLManager.getProject().getGrammarFile().incLastNTerminalId();
			return String.format("NTerminal%d", GGLLManager.getProject().getGrammarFile().getLastNTerminalId());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasStrings.LEFT_SIDE))
		{
			GGLLManager.getProject().getGrammarFile().incLastLeftSides();
			return String.format("LeftSide%d", GGLLManager.getProject().getGrammarFile().getLastLeftSides());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasStrings.LAMBDA))
		{
			GGLLManager.getProject().getGrammarFile().incLastLAMBDA();
			return String.format("Lambda%d", GGLLManager.getProject().getGrammarFile().getLastLAMBDA());
		}
		if (canvas.getCanvasActiveTool().equals(CanvasStrings.START))
		{
			GGLLManager.getProject().getGrammarFile().incLastSTART();
			return String.format("S%d", GGLLManager.getProject().getGrammarFile().getLastSTART());
		}
		GGLLManager.getProject().getGrammarFile().incLastCustomNode();
		return String.format("node%d", GGLLManager.getProject().getGrammarFile().getLastCustomNode());
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

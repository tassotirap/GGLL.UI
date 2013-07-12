package ggll.canvas.action;

import ggll.canvas.AbstractCanvas;
import ggll.canvas.CanvasStrings;
import ggll.canvas.CanvasFactory;
import ggll.core.syntax.command.CommandFactory;
import ggll.core.syntax.command.RenameCommand;

import java.beans.PropertyChangeSupport;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class LabelTextFieldEditor implements TextFieldInplaceEditor
{

	private AbstractCanvas canvas;
	private PropertyChangeSupport monitor;

	public LabelTextFieldEditor(AbstractCanvas canvas)
	{
		this.canvas = canvas;
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener(CanvasFactory.getVolatileStateManager());
	}

	@Override
	public String getText(Widget widget)
	{
		return ((LabelWidget) widget).getLabel();
	}

	@Override
	public boolean isEnabled(Widget widget)
	{
		if (canvas.getActiveTool().equals(CanvasStrings.SELECT))
		{
			return true;
		}
		return false;
	}

	@Override
	public void setText(Widget widget, String text)
	{
		((LabelWidget) widget).setLabel(text);
		RenameCommand rc = CommandFactory.createRenameCommand();
		monitor.firePropertyChange("undoable", null, rc);
	}
}
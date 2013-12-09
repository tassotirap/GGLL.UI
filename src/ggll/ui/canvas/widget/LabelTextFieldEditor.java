package ggll.ui.canvas.widget;

import ggll.ui.canvas.Canvas;
import ggll.ui.resource.CanvasResource;

import java.beans.PropertyChangeSupport;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class LabelTextFieldEditor implements TextFieldInplaceEditor
{

	private Canvas canvas;
	private PropertyChangeSupport monitor;

	public LabelTextFieldEditor(Canvas canvas)
	{
		this.canvas = canvas;
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener(canvas.getCanvasStateRepository());
	}

	@Override
	public String getText(Widget widget)
	{
		return ((LabelWidget) widget).getLabel();
	}

	@Override
	public boolean isEnabled(Widget widget)
	{
		if (canvas.getActiveTool().equals(CanvasResource.SELECT))
		{
			return true;
		}
		return false;
	}

	@Override
	public void setText(Widget widget, String text)
	{
		((LabelWidget) widget).setLabel(text);
		monitor.firePropertyChange("undoable", null, "Rename");
	}
}
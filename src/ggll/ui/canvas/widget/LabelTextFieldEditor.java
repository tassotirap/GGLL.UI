package ggll.ui.canvas.widget;

import ggll.ui.canvas.Canvas;
import ggll.ui.resource.CanvasResource;

import java.beans.PropertyChangeEvent;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class LabelTextFieldEditor implements TextFieldInplaceEditor
{

	private Canvas canvas;

	public LabelTextFieldEditor(Canvas canvas)
	{
		this.canvas = canvas;
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
		canvas.getCanvasStateRepository().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Rename"));
	}
}
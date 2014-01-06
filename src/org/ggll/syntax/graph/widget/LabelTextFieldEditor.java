package org.ggll.syntax.graph.widget;


import java.beans.PropertyChangeEvent;

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraph;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class LabelTextFieldEditor implements TextFieldInplaceEditor
{

	private final SyntaxGraph canvas;

	public LabelTextFieldEditor(SyntaxGraph canvas)
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
		if (this.canvas.getActiveTool().equals(CanvasResource.SELECT))
		{
			return true;
		}
		return false;
	}

	@Override
	public void setText(Widget widget, String text)
	{
		((LabelWidget) widget).setLabel(text);
		this.canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Rename"));
	}
}
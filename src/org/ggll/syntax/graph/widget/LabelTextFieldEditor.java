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
	
	public LabelTextFieldEditor(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	@Override
	public String getText(final Widget widget)
	{
		return ((LabelWidget) widget).getLabel();
	}
	
	@Override
	public boolean isEnabled(final Widget widget)
	{
		if (canvas.getActiveTool().equals(CanvasResource.SELECT)) { return true; }
		return false;
	}
	
	@Override
	public void setText(final Widget widget, final String text)
	{
		((LabelWidget) widget).setLabel(text);
		canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "Rename"));
	}
}
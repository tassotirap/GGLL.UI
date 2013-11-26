package ggll.ui.canvas.provider;

import ggll.ui.canvas.AbstractCanvas;
import ggll.ui.core.syntax.grammar.model.SyntaxDefinitions;

import java.awt.Point;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.widget.Widget;

public class MultiMoveProvider implements MoveProvider
{
	private AbstractCanvas canvas;
	private PropertyChangeSupport monitor;
	private Point original;

	private HashMap<Widget, Point> originals = new HashMap<Widget, Point>();

	public MultiMoveProvider(AbstractCanvas canvas)
	{
		this.canvas = canvas;
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener(canvas.getVolatileStateManager());
	}

	@Override
	public Point getOriginalLocation(Widget widget)
	{
		original = widget.getPreferredLocation();
		return original;
	}

	@Override
	public void movementFinished(Widget widget)
	{
		String context = null;
		if (originals.entrySet().size() > 1)
		{
			context = SyntaxDefinitions.Set;
		}
		else if (originals.entrySet().size() == 1)
		{
			context = SyntaxDefinitions.Node;
		}
		originals.clear();
		original = null;
		if (context != null)
		{
			monitor.firePropertyChange("undoable", null, "Move");
		}
	}

	@Override
	public void movementStarted(Widget widget)
	{
		Object object = canvas.findObject(widget);
		if (canvas.isNode(object) || canvas.isLabel(object))
		{
			for (Object o : canvas.getSelectedObjects())
				if (canvas.isNode(o) || canvas.isLabel(object))
				{
					Widget w = canvas.findWidget(o);
					if (w != null)
						originals.put(w, w.getPreferredLocation());
				}
		}
		else
		{
			originals.put(widget, widget.getPreferredLocation());
		}
	}

	@Override
	public void setNewLocation(Widget widget, Point location)
	{
		int dx = location.x - original.x;
		int dy = location.y - original.y;
		for (Map.Entry<Widget, Point> entry : originals.entrySet())
		{
			Point point = entry.getValue();
			entry.getKey().setPreferredLocation(new Point(point.x + dx, point.y + dy));
		}
	}

}

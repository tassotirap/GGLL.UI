package org.ggll.syntax.graph.provider;

import ggll.core.list.ExtendedList;

import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Set;

import org.ggll.syntax.graph.SyntaxGraph;
import org.netbeans.api.visual.widget.Widget;

public class WidgetDeleteProvider
{

	PropertyChangeSupport monitor;
	private final SyntaxGraph canvas;

	public WidgetDeleteProvider(SyntaxGraph canvas)
	{
		this.canvas = canvas;
		this.monitor = new PropertyChangeSupport(this);
		this.monitor.addPropertyChangeListener(canvas.getCanvasStateHistory());
	}

	/** delete all selected widgets **/
	public void deleteSelected()
	{
		deleteThese(this.canvas.getSelectedObjects());
	}

	/**
	 * Delete all widgets
	 * 
	 * @param widgets
	 *            , Widgets, or a set of them
	 */
	public void deleteThese(Object... widgets)
	{
		final ExtendedList<Object> toRemove = new ExtendedList<Object>();
		for (final Object w : widgets)
		{
			if (w instanceof Set<?>)
			{
				for (final Object obj : (Set<?>) w)
				{
					toRemove.append(obj);
				}
			}
			else if (w instanceof Collection<?>)
			{
				for (final Object obj : (Collection<?>) w)
				{
					toRemove.append(obj);
				}
			}
			else if (w instanceof Widget)
			{
				final Object obj = this.canvas.findObject((Widget) w);
				if (obj != null)
				{
					toRemove.append(obj);
				}
			}
			else
			{
				toRemove.append(w);
			}
		}
		final Object[] objs = toRemove.toArray();
		for (final Object obj : objs)
		{
			if (this.canvas.isNode(obj) || this.canvas.isLabel(obj))
			{
				final Collection<String> edges = this.canvas.findNodeEdges(obj.toString(), true, true);
				deleteThese(edges);
				this.canvas.removeNodeSafely((String) obj);
				this.monitor.firePropertyChange("undoable", null, "Delete");
			}
			else if (this.canvas.isEdge(obj))
			{
				this.canvas.removeEdgeSafely((String) obj);
				this.monitor.firePropertyChange("undoable", null, "Delete");
			}
		}
	}

	public boolean isDeletionAllowed()
	{
		return isDeletionAllowed(this.canvas.getSelectedObjects());
	}

	public boolean isDeletionAllowed(Object... widgets)
	{
		return widgets != null && widgets.length >= 1;
	}
}

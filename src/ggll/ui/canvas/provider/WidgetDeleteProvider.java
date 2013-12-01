package ggll.ui.canvas.provider;

import ggll.core.list.ExtendedList;
import ggll.ui.canvas.AbstractCanvas;

import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Set;

import org.netbeans.api.visual.widget.Widget;

public class WidgetDeleteProvider
{

	PropertyChangeSupport monitor;
	private AbstractCanvas canvas;

	public WidgetDeleteProvider(AbstractCanvas canvas)
	{
		this.canvas = canvas;
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener(canvas.getVolatileStateManager());
	}

	/** delete all selected widgets **/
	public void deleteSelected()
	{
		deleteThese(canvas.getSelectedObjects());
	}

	/**
	 * Delete all widgets
	 * 
	 * @param widgets
	 *            , Widgets, or a set of them
	 */
	public void deleteThese(Object... widgets)
	{
		ExtendedList<Object> toRemove = new ExtendedList<Object>();
		for (Object w : widgets)
		{
			if (w instanceof Set<?>)
			{
				for (Object obj : ((Set<?>) w))
				{
					toRemove.append(obj);
				}
			}
			else if (w instanceof Collection<?>)
			{
				for (Object obj : ((Collection<?>) w))
				{
					toRemove.append(obj);
				}
			}
			else if (w instanceof Widget)
			{
				Object obj = canvas.findObject((Widget) w);
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
		Object[] objs = toRemove.toArray();
		for (int i = 0; i < objs.length; i++)
		{
			if (canvas.isNode(objs[i]) || canvas.isLabel(objs[i]))
			{
				Collection<String> edges = canvas.findNodeEdges(objs[i].toString(), true, true);
				deleteThese(edges);
				canvas.removeNodeSafely((String) objs[i]);
				monitor.firePropertyChange("undoable", null, "Delete");
			}
			else if (canvas.isEdge(objs[i]))
			{
				canvas.removeEdgeSafely((String) objs[i]);
				monitor.firePropertyChange("undoable", null, "Delete");
			}
		}
	}

	public boolean isDeletionAllowed()
	{
		return isDeletionAllowed(canvas.getSelectedObjects());
	}

	public boolean isDeletionAllowed(Object... widgets)
	{
		return (widgets != null && widgets.length >= 1);
	}
}

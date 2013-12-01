package ggll.ui.canvas.provider;

import ggll.core.list.ExtendedList;
import ggll.ui.canvas.AbstractCanvas;
import ggll.ui.canvas.action.SelectionAction;
import ggll.ui.canvas.state.Connection;
import ggll.ui.canvas.state.Node;
import ggll.ui.resource.CanvasResource;
import ggll.ui.util.clipboard.ClipboardHelper;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.netbeans.api.visual.widget.Widget;

public class WidgetCopyPasteProvider
{

	private AbstractCanvas canvas;
	private PropertyChangeSupport monitor;

	public WidgetCopyPasteProvider(AbstractCanvas canvas)
	{
		this.canvas = canvas;
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener(canvas.getVolatileStateManager());
	}

	/** copy all selected widgets to clipboard **/
	public void copySelected()
	{
		copyThese(canvas.getSelectedObjects());
	}

	/**
	 * copy all widgets to clipboard **
	 * 
	 * @param widgets
	 *            widgets to copied, or a set of them
	 */
	public void copyThese(Object... widgets)
	{
		Widget widget;
		boolean hasSelection = false;
		SelectionAction ws = new SelectionAction(canvas);
		for (Object w : widgets)
		{
			if (w instanceof Set<?>)
			{
				for (Object obj : (Set<?>) w)
				{
					if ((widget = canvas.findWidget(obj)) != null)
					{
						ws.addSelection(widget);
						hasSelection = true;
					}
				}
			}
			else if (w instanceof Widget)
			{
				ws.addSelection((Widget) w);
				hasSelection = true;
			}
			else if ((widget = canvas.findWidget(w)) != null)
			{
				ws.addSelection(widget);
				hasSelection = true;
			}
		}
		if (hasSelection)
		{
			ClipboardHelper.setClipboardContents(ws, ws);
		}
	}

	/**
	 * copy and delete all selected widgets
	 * 
	 * @param wdp
	 *            a Provider to decide how to delete widgets in scene. If null a
	 *            default one will be used.
	 * **/
	public void cutSelected(WidgetDeleteProvider wdp)
	{
		if (wdp == null)
			wdp = new WidgetDeleteProvider(canvas);
		copySelected();
		wdp.deleteSelected();
		canvas.updateState(canvas.getCanvasState());
	}

	public void cutThese(WidgetDeleteProvider wdp, Object... widgets)
	{
		if (wdp == null)
			wdp = new WidgetDeleteProvider(canvas);
		copyThese(widgets);
		wdp.deleteThese(widgets);
		canvas.updateState(canvas.getCanvasState());
	}

	/**
	 * paste copied widgets
	 * 
	 * @param p
	 *            origin point to paste the widgets
	 */
	@SuppressWarnings("unchecked")
	public void paste(Point p)
	{
		Object contents = ClipboardHelper.getClipboardContents();
		PointerInfo pi = MouseInfo.getPointerInfo();
		Point pm = canvas.convertLocalToScene(pi.getLocation());
		ExtendedList<Node> nodes = new ExtendedList<Node>();
		ExtendedList<Connection> connections = new ExtendedList<Connection>();
		Point firstNode = null;
		Point originalFirstNode = null;
		HashMap<String, String> oldNewNames = new HashMap<String, String>();
		if (contents != null && contents instanceof ArrayList)
		{
			for (Object obj : (ArrayList) contents)
			{
				if (obj instanceof Connection)
				{
					connections.append((Connection) obj);
				}
				else if (obj instanceof Node)
				{
					nodes.append((Node) obj);
				}
			}
		}
		for (Node n : nodes.getAll())
		{
			if (canvas.findWidget(n.getName()) != null)
			{
				int i = 0;
				String oldName = "";
				String newName = "";
				oldName = n.getName();
				do
				{
					if (n.getName().endsWith("_" + (i - 1)))
						n.setName(n.getName().substring(0, n.getName().length() - ("_" + Math.abs(i - 1)).length()));
					n.setName(n.getName() + "_" + (i++));
					newName = n.getName();
					oldNewNames.put(oldName, newName);
					if (n.getType().equals(CanvasResource.LEFT_SIDE) || n.getType().equals(CanvasResource.START))
					{
						if (n.getTitle().endsWith("_" + (i - 2)))
							n.setTitle(n.getTitle().substring(0, n.getTitle().length() - ("_" + Math.abs(i - 2)).length()));
						n.setTitle(n.getTitle() + "_" + (i - 1));
					}
				}
				while (canvas.findWidget(n.getName()) != null);
				if (firstNode == null)
				{
					originalFirstNode = n.getLocation();
					if (canvas.getInterractionLayer().getState().isHovered() && canvas.isHitAt(pm))
					{
						p = pm;
					}
					else if (p == null)
					{
						p = new Point();
						p.setLocation(n.getLocation().x + 50, n.getLocation().y - 50);
					}
					firstNode = p;
				}
				else if (p == null)
				{
					p = new Point();
					int x = firstNode.x + (n.getLocation().x - originalFirstNode.getLocation().x);
					int y = firstNode.y + (n.getLocation().y - originalFirstNode.getLocation().y);
					p.setLocation(x, y);
				}
			}
			else if (p == null)
			{
				p = n.getLocation();
			}
			n.setLocation(p);
			p = null;
			canvas.getCanvasState().update(canvas);
			monitor.firePropertyChange("undoable", null, "Add");
		}
		for (Connection c : connections.getAll())
		{
			String newSource = oldNewNames.get(c.getSource());
			if (newSource != null)
				c.setSource(newSource);
			String newTarget = oldNewNames.get(c.getTarget());
			c.setTarget(newTarget);
			int i = 0;
			String oldName = c.getName();
			do
			{
				if (c.getName().endsWith("_" + (i)))
					c.setName(c.getName().substring(0, c.getName().length() - ("_" + Math.abs(i)).length()));
				String newName = c.getName() + "_" + (i++);
				c.setName(newName);
				oldNewNames.put(oldName, newName);
			}
			while (canvas.findWidget(c.getName()) != null);
			canvas.getCanvasState().update(canvas);
			monitor.firePropertyChange("undoable", null, "Connection");
		}
		canvas.updateState(canvas.getCanvasState());
		for (String name : oldNewNames.values())
		{
			canvas.select(name, true);
		}
	}

}

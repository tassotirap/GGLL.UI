package ggll.ui.canvas.action;

import ggll.ui.canvas.AbstractCanvas;
import ggll.ui.canvas.state.Connection;
import ggll.ui.canvas.state.Node;
import ggll.ui.canvas.widget.LabelWidgetExt;
import ggll.ui.canvas.widget.MarkedWidget;
import ggll.ui.canvas.widget.TypedWidget;
import ggll.ui.resource.CanvasResource;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class SelectionAction implements ClipboardOwner, Transferable
{
	private AbstractCanvas canvas;

	public SelectionAction(AbstractCanvas canvas)
	{
		this.canvas = canvas;
	}

	public SelectionAction(Widget[] widgets, AbstractCanvas canvas)
	{
		this.canvas = canvas;
		for (Widget w : widgets)
		{
			addSelection(w);
		}
	}

	public void addSelection(Widget w)
	{
		if (w instanceof LabelWidget)
		{
			Node node = new Node();
			Object object = canvas.findObject(w);
			if (object != null)
			{
				node.setName((String) object);
			}
			else
			{
				node.setName(((LabelWidget) w).getLabel());
			}
			node.setTitle(((LabelWidget) w).getLabel());
			node.setLocation(w.getPreferredLocation());
			if (w instanceof TypedWidget)
			{
				node.setType(((LabelWidgetExt) w).getType());
			}
			if (w instanceof MarkedWidget)
			{
				node.setMark(((MarkedWidget) w).getMark());
			}
		}
		else if (w instanceof ConnectionWidget)
		{
			Connection c = new Connection();
			Object object = canvas.findObject(w);
			if (object != null)
			{
				c.setName((String) object);
				c.setSource(canvas.getEdgeSource((String) object));
				c.setTarget(canvas.getEdgeTarget((String) object));
				if (canvas.isAlternative((String) object))
				{
					c.setType(CanvasResource.ALTERNATIVE);
				}
				else if (canvas.isSuccessor((String) object))
				{
					c.setType(CanvasResource.SUCCESSOR);
				}
			}
		}
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		return null;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return new DataFlavor[]{ new DataFlavor(java.util.ArrayList.class, "Node") };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return flavor.isMimeTypeEqual(DataFlavor.javaSerializedObjectMimeType);
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents)
	{
	}
}

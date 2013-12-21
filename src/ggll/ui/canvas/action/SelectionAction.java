package ggll.ui.canvas.action;

import ggll.ui.canvas.Canvas;
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
import java.io.Serializable;
import java.util.ArrayList;

import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class SelectionAction implements ClipboardOwner, Transferable
{
	private final ArrayList<Serializable> elements = new ArrayList<Serializable>();
	private final Canvas canvas;

	public SelectionAction(Canvas canvas)
	{
		this.canvas = canvas;
	}

	public SelectionAction(Widget[] widgets, Canvas canvas)
	{
		this.canvas = canvas;
		for (final Widget widget : widgets)
		{
			addSelection(widget);
		}
	}

	public void addSelection(Widget widget)
	{
		if (widget instanceof LabelWidget)
		{
			final Node node = new Node();
			final Object object = this.canvas.findObject(widget);
			if (object != null)
			{
				node.setName((String) object);
			}
			else
			{
				node.setName(((LabelWidget) widget).getLabel());
			}
			node.setTitle(((LabelWidget) widget).getLabel());
			node.setLocation(widget.getPreferredLocation());
			if (widget instanceof TypedWidget)
			{
				node.setType(((LabelWidgetExt) widget).getType());
			}
			if (widget instanceof MarkedWidget)
			{
				node.setMark(((MarkedWidget) widget).getMark());
			}
			this.elements.add(node);
		}
		else if (widget instanceof ConnectionWidget)
		{
			final Connection conn = new Connection();
			final Object object = this.canvas.findObject(widget);
			if (object != null)
			{
				conn.setName((String) object);
				conn.setSource(this.canvas.getEdgeSource((String) object));
				conn.setTarget(this.canvas.getEdgeTarget((String) object));
				if (this.canvas.isAlternative((String) object))
				{
					conn.setType(CanvasResource.ALTERNATIVE);
				}
				else if (this.canvas.isSuccessor((String) object))
				{
					conn.setType(CanvasResource.SUCCESSOR);
				}
			}
			this.elements.add(conn);
		}
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		return this.elements;
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

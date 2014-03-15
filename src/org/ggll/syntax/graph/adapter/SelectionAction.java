package org.ggll.syntax.graph.adapter;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.state.StateConnection;
import org.ggll.syntax.graph.state.StateNode;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class SelectionAction implements ClipboardOwner, Transferable
{
	private final ArrayList<Serializable> elements = new ArrayList<Serializable>();
	private final SyntaxGraph canvas;
	
	public SelectionAction(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	public SelectionAction(final Widget[] widgets, final SyntaxGraph canvas)
	{
		this.canvas = canvas;
		for (final Widget widget : widgets)
		{
			addSelection(widget);
		}
	}
	
	public void addSelection(final Widget widget)
	{
		if (widget instanceof LabelWidget)
		{
			final String object = (String) canvas.findObject(widget);
			final StateNode node = new StateNode(object, canvas);
			elements.add(node);
		}
		else if (widget instanceof ConnectionWidget)
		{
			final String object = (String) canvas.findObject(widget);
			final StateConnection conn = new StateConnection(object, canvas);
			elements.add(conn);
		}
	}
	
	@Override
	public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		return elements;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return new DataFlavor[]
		{ new DataFlavor(java.util.ArrayList.class, "Node") };
	}
	
	@Override
	public boolean isDataFlavorSupported(final DataFlavor flavor)
	{
		return flavor.isMimeTypeEqual(DataFlavor.javaSerializedObjectMimeType);
	}
	
	@Override
	public void lostOwnership(final Clipboard clipboard, final Transferable contents)
	{
	}
}

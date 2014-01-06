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

	public SelectionAction(SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}

	public SelectionAction(Widget[] widgets, SyntaxGraph canvas)
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
			final String object = (String)this.canvas.findObject(widget);
			final StateNode node = new StateNode(object, this.canvas);
			this.elements.add(node);
		}
		else if (widget instanceof ConnectionWidget)
		{
			final String object = (String)this.canvas.findObject(widget);
			final StateConnection conn = new StateConnection(object, this.canvas);
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

package ggll.ui.canvas.state;

import ggll.core.list.ExtendedList;
import ggll.ui.canvas.Canvas;
import ggll.ui.canvas.CanvasRepository;
import ggll.ui.canvas.widget.IconNodeWidgetExt;
import ggll.ui.canvas.widget.LabelWidgetExt;
import ggll.ui.canvas.widget.MarkedWidget;
import ggll.ui.canvas.widget.TypedWidget;
import ggll.ui.resource.CanvasResource;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public class CanvasState implements Serializable, PropertyChangeListener
{
	private static final long serialVersionUID = -7729464439313780001L;
	private final HashMap<String, Connection> connections = new HashMap<String, Connection>();
	private final HashMap<String, Node> nodes = new HashMap<String, Node>();
	private final Preferences preferences = new Preferences();

	private int lastTerminalId = 0;
	private int lastNTerminalId = 0;
	private int lastLeftSides = 0;
	private int lastLAMBDA = 0;
	private int lastSTART = 0;
	private int lastCustomNode = 0;
	private String file;

	private CanvasState(String file)
	{
		this.file = file;
	}

	public static CanvasState read(String file) throws IOException, ClassNotFoundException
	{
		CanvasState canvasState = null;
		if (file.length() > 0)
		{
			try
			{
				final FileInputStream fileInputStream = new FileInputStream(file);
				final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				canvasState = (CanvasState) objectInputStream.readObject();
				objectInputStream.close();
				fileInputStream.close();
			}
			catch (final Exception e)
			{
				canvasState = new CanvasState(file);
			}
		}
		return canvasState;
	}

	public void addConnection(Canvas canvas, String object)
	{
		final Widget widget = canvas.findWidget(object);
		if (widget instanceof ConnectionWidget)
		{
			final ConnectionWidget connectionWidget = (ConnectionWidget) widget;
			final Connection connection = new Connection();
			connection.setName(object);
			connection.setSource(canvas.getEdgeSource(object));
			connection.setTarget(canvas.getEdgeTarget(object));
			if (canvas.isAlternative(object))
			{
				connection.setType(CanvasResource.ALTERNATIVE);
			}
			else if (canvas.isSuccessor(object))
			{
				connection.setType(CanvasResource.SUCCESSOR);
			}
			connection.setPoints(connectionWidget.getControlPoints());
			this.connections.put(connection.getName(), connection);
		}
	}

	public void addNode(Canvas canvas, String object)
	{
		final Widget widget = canvas.findWidget(object);
		final Node node = new Node();
		if (widget instanceof LabelWidget)
		{
			final LabelWidget labelWidget = (LabelWidget) widget;
			node.setName(object);
			node.setTitle(labelWidget.getLabel());
			node.setLocation(labelWidget.getPreferredLocation());
			if (labelWidget instanceof TypedWidget)
			{
				node.setType(((LabelWidgetExt) labelWidget).getType());
			}
		}
		else if (widget instanceof IconNodeWidgetExt)
		{
			final IconNodeWidgetExt iconNodeWidget = (IconNodeWidgetExt) widget;
			node.setName(object);
			node.setLocation(iconNodeWidget.getPreferredLocation());
			node.setType(iconNodeWidget.getType());
		}
		if (widget instanceof MarkedWidget)
		{
			node.setMark(((MarkedWidget) widget).getMark());
		}
		this.nodes.put(node.getName(), node);
	}

	public Connection findConnection(Object conn)
	{
		if (this.connections.containsKey(conn))
		{
			return this.connections.get(conn);
		}
		return null;
	}

	public Node findNode(Object node)
	{
		if (this.nodes.containsKey(node))
		{
			return this.nodes.get(node);
		}
		return null;
	}

	public List<String> getConnections()
	{
		final ExtendedList<String> list = new ExtendedList<String>();
		list.addAll(this.connections.keySet());
		Collections.sort(list.getAll());
		return list.getAll();
	}

	public String getFile()
	{
		return this.file;
	}

	public int getLastCustomNode()
	{
		return this.lastCustomNode;
	}

	public int getLastLAMBDA()
	{
		return this.lastLAMBDA;
	}

	public int getLastLeftSides()
	{
		return this.lastLeftSides;
	}

	public int getLastNTerminalId()
	{
		return this.lastNTerminalId;
	}

	public int getLastSTART()
	{
		return this.lastSTART;
	}

	public int getLastTerminalId()
	{
		return this.lastTerminalId;
	}

	public Set<String> getNodes()
	{
		return this.nodes.keySet();
	}

	public Preferences getPreferences()
	{
		return this.preferences;
	}

	public String getType(Object obj)
	{
		if (this.nodes.containsKey(obj))
		{
			return this.nodes.get(obj).getType();
		}
		if (this.connections.containsKey(obj))
		{
			return this.connections.get(obj).getType();
		}
		return null;
	}

	public void incLastCustomNode()
	{
		this.lastCustomNode++;
	}

	public void incLastLAMBDA()
	{
		this.lastLAMBDA++;
	}

	public void incLastLeftSides()
	{
		this.lastLeftSides++;
	}

	public void incLastNTerminalId()
	{
		this.lastNTerminalId++;
	}

	public void incLastSTART()
	{
		this.lastSTART++;
	}

	public void incLastTerminalId()
	{
		this.lastTerminalId++;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals("writing"))
		{
			final Canvas canvas = CanvasRepository.getInstance(this.file);
			if (canvas != null)
			{
				reloadFromCanvas(canvas);
			}
		}
	}

	public void reloadFromCanvas(Canvas canvas)
	{
		this.nodes.clear();
		this.connections.clear();
		for (final String object : canvas.getNodes())
		{
			addNode(canvas, object);
		}
		for (final String object : canvas.getEdges())
		{
			addConnection(canvas, object);
		}
	}

	public void setFile(String file)
	{
		this.file = file;
	}

	public void write() throws IOException
	{
		propertyChange(new PropertyChangeEvent(this, "writing", null, this));
		final FileOutputStream fileOutputStream = new FileOutputStream(this.file);
		final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(this);
		objectOutputStream.close();
		fileOutputStream.close();
	}
}

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
import java.beans.PropertyChangeSupport;
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
	private HashMap<String, Connection> connections = new HashMap<String, Connection>();
	private HashMap<String, Node> nodes = new HashMap<String, Node>();
	private Preferences preferences = new Preferences();
	private PropertyChangeSupport monitor;

	private int lastTerminalId = 0;
	private int lastNTerminalId = 0;
	private int lastLeftSides = 0;
	private int lastLAMBDA = 0;
	private int lastSTART = 0;
	private int lastCustomNode = 0;
	private String file;

	public Connection findConnection(Object conn)
	{
		if (connections.containsKey(conn))
		{
			return connections.get(conn);
		}
		return null;
	}

	public Node findNode(Object node)
	{
		if (nodes.containsKey(node))
		{
			return nodes.get(node);
		}
		return null;
	}

	public List<String> getConnections()
	{
		ExtendedList<String> list = new ExtendedList<String>();
		list.addAll(connections.keySet());
		Collections.sort(list.getAll());
		return list.getAll();
	}

	public int getLastCustomNode()
	{
		return lastCustomNode;
	}

	public int getLastLAMBDA()
	{
		return lastLAMBDA;
	}

	public int getLastLeftSides()
	{
		return lastLeftSides;
	}

	public int getLastNTerminalId()
	{
		return lastNTerminalId;
	}

	public int getLastSTART()
	{
		return lastSTART;
	}

	public int getLastTerminalId()
	{
		return lastTerminalId;
	}

	public Set<String> getNodes()
	{
		return nodes.keySet();
	}

	public Preferences getPreferences()
	{
		return preferences;
	}

	public String getType(Object obj)
	{
		if (nodes.containsKey(obj))
		{
			return nodes.get(obj).getType();
		}
		if (connections.containsKey(obj))
		{
			return connections.get(obj).getType();
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
			Canvas canvas = CanvasRepository.getInstance(file);
			if (canvas != null)
			{
				reloadFromCanvas(canvas);
			}
		}
	}
	
	public void addNode(Canvas canvas, String object)
	{
		Widget widget = canvas.findWidget(object);
		if (widget instanceof LabelWidget)
		{
			LabelWidget labelWidget = (LabelWidget) widget;
			Node node = new Node();
			node.setName(object);
			node.setTitle(labelWidget.getLabel());
			node.setLocation(labelWidget.getPreferredLocation());
			if (labelWidget instanceof TypedWidget)
			{
				node.setType(((LabelWidgetExt) labelWidget).getType());
			}
			if (labelWidget instanceof MarkedWidget)
			{
				node.setMark(((MarkedWidget) labelWidget).getMark());
			}
			nodes.put(node.getName(), node);
		}
		else if (widget instanceof IconNodeWidgetExt)
		{
			IconNodeWidgetExt iconNodeWidget = (IconNodeWidgetExt) widget;
			Node node = new Node();
			node.setName(object);
			node.setLocation(iconNodeWidget.getPreferredLocation());
			node.setType(iconNodeWidget.getType());
			nodes.put(node.getName(), node);
		}
	}
	
	public void addConnection(Canvas canvas, String object)
	{
		Widget widget = canvas.findWidget(object);
		if (widget instanceof ConnectionWidget)
		{
			ConnectionWidget connectionWidget = (ConnectionWidget) widget;
			Connection connection = new Connection();
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
			connections.put(connection.getName(), connection);
		}
	}

	public void reloadFromCanvas(Canvas canvas)
	{
		nodes.clear();
		connections.clear();
		for (String object : canvas.getNodes())
		{
			addNode(canvas, object);
		}
		for (String object : canvas.getEdges())
		{
			addConnection(canvas, object);
		}
	}

	public String getFile()
	{
		return file;
	}

	public void setFile(String file)
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
				FileInputStream fileInputStream = new FileInputStream(file);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				canvasState = (CanvasState) objectInputStream.readObject();
				objectInputStream.close();
				fileInputStream.close();				
			}
			catch (Exception e)
			{
				canvasState = new CanvasState(file);
			}
		}
		if (canvasState.monitor == null)
		{
			canvasState.monitor = new PropertyChangeSupport(canvasState);
		}
		return canvasState;
	}

	private CanvasState(String file)
	{
		this.file = file;
	}

	public void write() throws IOException
	{
		monitor.firePropertyChange("writing", null, this);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(this);
		objectOutputStream.close();
		fileOutputStream.close();
	}
}

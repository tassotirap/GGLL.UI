package org.ggll.syntax.graph.state;

import ggll.core.list.ExtendedList;

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

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.SyntaxGraphRepository;

public class State implements Serializable, PropertyChangeListener
{
	private static final long serialVersionUID = -7729464439313780001L;
	private final HashMap<String, StateConnection> connections = new HashMap<String, StateConnection>();
	private final HashMap<String, StateNode> nodes = new HashMap<String, StateNode>();
	private final StatePreferences preferences = new StatePreferences();

	private int lastTerminalId = 0;
	private int lastNTerminalId = 0;
	private int lastLeftSides = 0;
	private int lastLAMBDA = 0;
	private int lastSTART = 0;
	private int lastCustomNode = 0;
	private String file;

	private State(String file)
	{
		this.file = file;
	}

	public static State read(String file) throws IOException, ClassNotFoundException
	{
		State canvasState = null;
		if (file.length() > 0)
		{
			try
			{
				final FileInputStream fileInputStream = new FileInputStream(file);
				final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				canvasState = (State) objectInputStream.readObject();
				objectInputStream.close();
				fileInputStream.close();
			}
			catch (final Exception e)
			{
				canvasState = new State(file);
			}
		}
		return canvasState;
	}

	public void addConnection(String object, SyntaxGraph canvas)
	{
		final StateConnection connection = new StateConnection(object, canvas);
		this.connections.put(connection.getId(), connection);
	}

	public void addNode(String object, SyntaxGraph canvas)
	{
		final StateNode node = new StateNode(object, canvas);
		this.nodes.put(node.getId(), node);
	}

	public StateConnection findConnection(Object conn)
	{
		if (this.connections.containsKey(conn))
		{
			return this.connections.get(conn);
		}
		return null;
	}

	public StateNode findNode(String node)
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

	public ExtendedList<StateNode> getLeftSide()
	{
		final ExtendedList<StateNode> leftSides = new ExtendedList<StateNode>();
		for (final StateNode node : this.nodes.values())
		{
			if (node.getType().equals(CanvasResource.LEFT_SIDE))
			{
				leftSides.append(node);
			}
		}
		return leftSides;
	}
	
	public ExtendedList<StateNode> getLambda()
	{
		final ExtendedList<StateNode> leftSides = new ExtendedList<StateNode>();
		for (final StateNode node : this.nodes.values())
		{
			if (node.getType().equals(CanvasResource.LAMBDA))
			{
				leftSides.append(node);
			}
		}
		return leftSides;
	}
	
	public ExtendedList<StateNode> getNTerminal()
	{
		final ExtendedList<StateNode> nTerminals = new ExtendedList<StateNode>();
		for (final StateNode node : this.nodes.values())
		{
			if (node.getType().equals(CanvasResource.N_TERMINAL))
			{
				nTerminals.append(node);
			}
		}
		return nTerminals;
	}
	
	public ExtendedList<StateNode> getTerminal()
	{
		final ExtendedList<StateNode> terminals = new ExtendedList<StateNode>();
		for (final StateNode node : this.nodes.values())
		{
			if (node.getType().equals(CanvasResource.TERMINAL))
			{
				terminals.append(node);
			}
		}
		return terminals;
	}
	
	
	public ExtendedList<StateNode> getStateNodes()
	{
		return new ExtendedList<StateNode>(nodes.values());
	}

	public Set<String> getNodes()
	{
		return this.nodes.keySet();
	}

	public StatePreferences getPreferences()
	{
		return this.preferences;
	}

	public ExtendedList<StateNode> getStarts()
	{
		final ExtendedList<StateNode> starts = new ExtendedList<StateNode>();
		for (final StateNode node : this.nodes.values())
		{
			if (node.getType().equals(CanvasResource.START))
			{
				starts.append(node);
			}
		}
		return starts;
	}

	public ExtendedList<StateConnection> getSucessors()
	{
		final ExtendedList<StateConnection> sucessors = new ExtendedList<StateConnection>();
		for (final StateConnection edge : this.connections.values())
		{
			if (edge.getType().equals(CanvasResource.SUCCESSOR))
			{
				sucessors.append(edge);
			}
		}
		return sucessors;
	}
	
	public ExtendedList<StateConnection> getAlternatives()
	{
		final ExtendedList<StateConnection> alternative = new ExtendedList<StateConnection>();
		for (final StateConnection edge : this.connections.values())
		{
			if (edge.getType().equals(CanvasResource.ALTERNATIVE))
			{
				alternative.append(edge);
			}
		}
		return alternative;
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
			final SyntaxGraph canvas = SyntaxGraphRepository.getInstance(this.file);
			if (canvas != null)
			{
				refresh(canvas);
			}
		}
	}

	public void refresh(SyntaxGraph canvas)
	{
		this.nodes.clear();
		this.connections.clear();
		for (final String object : canvas.getNodes())
		{
			addNode(object, canvas);
		}
		for (final String object : canvas.getEdges())
		{
			addConnection(object, canvas);
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

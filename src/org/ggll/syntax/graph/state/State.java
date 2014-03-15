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
	
	public static State read(final String file) throws IOException, ClassNotFoundException
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
	
	private State(final String file)
	{
		this.file = file;
	}
	
	public void addConnection(final String object, final SyntaxGraph canvas)
	{
		final StateConnection connection = new StateConnection(object, canvas);
		connections.put(connection.getId(), connection);
	}
	
	public void addNode(final String object, final SyntaxGraph canvas)
	{
		final StateNode node = new StateNode(object, canvas);
		nodes.put(node.getId(), node);
	}
	
	public StateConnection findConnection(final Object conn)
	{
		if (connections.containsKey(conn)) { return connections.get(conn); }
		return null;
	}
	
	public StateNode findNode(final String node)
	{
		if (nodes.containsKey(node)) { return nodes.get(node); }
		return null;
	}
	
	public ExtendedList<StateConnection> getAlternatives()
	{
		final ExtendedList<StateConnection> alternative = new ExtendedList<StateConnection>();
		for (final StateConnection edge : connections.values())
		{
			if (edge.getType().equals(CanvasResource.ALTERNATIVE))
			{
				alternative.append(edge);
			}
		}
		return alternative;
	}
	
	public List<String> getConnections()
	{
		final ExtendedList<String> list = new ExtendedList<String>();
		list.addAll(connections.keySet());
		Collections.sort(list.getAll());
		return list.getAll();
	}
	
	public String getFile()
	{
		return file;
	}
	
	public ExtendedList<StateNode> getLambda()
	{
		final ExtendedList<StateNode> leftSides = new ExtendedList<StateNode>();
		for (final StateNode node : nodes.values())
		{
			if (node.getType().equals(CanvasResource.LAMBDA))
			{
				leftSides.append(node);
			}
		}
		return leftSides;
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
	
	public ExtendedList<StateNode> getLeftSide()
	{
		final ExtendedList<StateNode> leftSides = new ExtendedList<StateNode>();
		for (final StateNode node : nodes.values())
		{
			if (node.getType().equals(CanvasResource.LEFT_SIDE))
			{
				leftSides.append(node);
			}
		}
		return leftSides;
	}
	
	public Set<String> getNodes()
	{
		return nodes.keySet();
	}
	
	public ExtendedList<StateNode> getNTerminal()
	{
		final ExtendedList<StateNode> nTerminals = new ExtendedList<StateNode>();
		for (final StateNode node : nodes.values())
		{
			if (node.getType().equals(CanvasResource.N_TERMINAL))
			{
				nTerminals.append(node);
			}
		}
		return nTerminals;
	}
	
	public StatePreferences getPreferences()
	{
		return preferences;
	}
	
	public ExtendedList<StateNode> getStarts()
	{
		final ExtendedList<StateNode> starts = new ExtendedList<StateNode>();
		for (final StateNode node : nodes.values())
		{
			if (node.getType().equals(CanvasResource.START))
			{
				starts.append(node);
			}
		}
		return starts;
	}
	
	public ExtendedList<StateNode> getStateNodes()
	{
		return new ExtendedList<StateNode>(nodes.values());
	}
	
	public ExtendedList<StateConnection> getSucessors()
	{
		final ExtendedList<StateConnection> sucessors = new ExtendedList<StateConnection>();
		for (final StateConnection edge : connections.values())
		{
			if (edge.getType().equals(CanvasResource.SUCCESSOR))
			{
				sucessors.append(edge);
			}
		}
		return sucessors;
	}
	
	public ExtendedList<StateNode> getTerminal()
	{
		final ExtendedList<StateNode> terminals = new ExtendedList<StateNode>();
		for (final StateNode node : nodes.values())
		{
			if (node.getType().equals(CanvasResource.TERMINAL))
			{
				terminals.append(node);
			}
		}
		return terminals;
	}
	
	public String getType(final Object obj)
	{
		if (nodes.containsKey(obj)) { return nodes.get(obj).getType(); }
		if (connections.containsKey(obj)) { return connections.get(obj).getType(); }
		return null;
	}
	
	public void incLastCustomNode()
	{
		lastCustomNode++;
	}
	
	public void incLastLAMBDA()
	{
		lastLAMBDA++;
	}
	
	public void incLastLeftSides()
	{
		lastLeftSides++;
	}
	
	public void incLastNTerminalId()
	{
		lastNTerminalId++;
	}
	
	public void incLastSTART()
	{
		lastSTART++;
	}
	
	public void incLastTerminalId()
	{
		lastTerminalId++;
	}
	
	@Override
	public void propertyChange(final PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals("writing"))
		{
			final SyntaxGraph canvas = SyntaxGraphRepository.getInstance(file);
			if (canvas != null)
			{
				refresh(canvas);
			}
		}
	}
	
	public void refresh(final SyntaxGraph canvas)
	{
		nodes.clear();
		connections.clear();
		for (final String object : canvas.getNodes())
		{
			addNode(object, canvas);
		}
		for (final String object : canvas.getEdges())
		{
			addConnection(object, canvas);
		}
	}
	
	public void setFile(final String file)
	{
		this.file = file;
	}
	
	public void write() throws IOException
	{
		propertyChange(new PropertyChangeEvent(this, "writing", null, this));
		final FileOutputStream fileOutputStream = new FileOutputStream(file);
		final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(this);
		objectOutputStream.close();
		fileOutputStream.close();
	}
}

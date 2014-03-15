package org.ggll.syntax.graph.state;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.ggll.syntax.graph.SyntaxGraph;

public class StateHistory implements PropertyChangeListener
{
	public class CanvasStateSerialized
	{
		public byte[] serializedObject;
		
		public CanvasStateSerialized(final State canvasState)
		{
			try
			{
				final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
				objectOutputStream.writeObject(canvasState);
				serializedObject = byteArrayOutputStream.toByteArray();
			}
			catch (final Exception e)
			{
			}
		}
		
		public State toCanvasState()
		{
			try
			{
				final ByteArrayInputStream bais = new ByteArrayInputStream(serializedObject);
				final ObjectInputStream ois = new ObjectInputStream(bais);
				return (State) ois.readObject();
			}
			catch (final Exception e)
			{
			}
			return null;
		}
	}
	
	private final ArrayList<CanvasStateSerialized> states = new ArrayList<CanvasStateSerialized>();
	private int index = -1;
	private final SyntaxGraph canvas;
	
	private final PropertyChangeSupport monitor;
	
	public StateHistory(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
		states.add(new CanvasStateSerialized(canvas.getCanvasState()));
		
		index++;
		
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener("object_state", canvas);
		monitor.addPropertyChangeListener("writing", canvas);
		monitor.addPropertyChangeListener("undoable", canvas);
	}
	
	public boolean hasNextRedo()
	{
		return states.size() > index + 1;
	}
	
	public boolean hasNextUndo()
	{
		return index > 0;
	}
	
	@Override
	public void propertyChange(final PropertyChangeEvent event)
	{
		switch (event.getPropertyName())
		{
			case "undoable":
				try
				{
					monitor.firePropertyChange("writing", null, canvas.getCanvasState());
					final int size = states.size();
					for (int i = size - 1; i > index; i--)
					{
						states.remove(i);
					}
					states.add(new CanvasStateSerialized(canvas.getCanvasState()));
					index++;
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
				break;
		}
	}
	
	public void redo()
	{
		if (!hasNextRedo()) { return; }
		
		try
		{
			final State nobject = states.get(index + 1).toCanvasState();
			final State object = states.get(index).toCanvasState();
			index++;
			monitor.firePropertyChange("object_state", object, nobject);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void undo()
	{
		if (!hasNextUndo()) { return; }
		
		try
		{
			final State nobject = states.get(index - 1).toCanvasState();
			final State object = states.get(index).toCanvasState();
			index--;
			monitor.firePropertyChange("object_state", object, nobject);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}
}
package ggll.ui.canvas.state;

import ggll.ui.canvas.Canvas;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CanvasStateRepository implements PropertyChangeListener
{
	public class CanvasStateSerialized
	{
		public byte[] serializedObject;

		public CanvasStateSerialized(CanvasState canvasState)
		{
			try
			{
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
				objectOutputStream.writeObject(canvasState);
				serializedObject = byteArrayOutputStream.toByteArray();
			}
			catch (Exception e)
			{
			}
		}

		public CanvasState toCanvasState()
		{
			try
			{
				ByteArrayInputStream bais = new ByteArrayInputStream(serializedObject);
				ObjectInputStream ois = new ObjectInputStream(bais);
				return (CanvasState) ois.readObject();
			}
			catch (Exception e)
			{
			}
			return null;
		}
	}

	private ArrayList<CanvasStateSerialized> states = new ArrayList<CanvasStateSerialized>();
	private int index = -1;
	private Canvas canvas;

	private PropertyChangeSupport monitor;

	public CanvasStateRepository(Canvas canvas)
	{
		this.canvas = canvas;
		this.states.add(new CanvasStateSerialized(canvas.getCurrentCanvasState()));

		this.index++;

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
	public void propertyChange(PropertyChangeEvent event)
	{
		switch (event.getPropertyName())
		{
			case "undoable":
				try
				{
					monitor.firePropertyChange("writing", null, canvas.getCurrentCanvasState());
					int size = states.size();
					for (int i = size - 1; i > index; i--)
					{
						states.remove(i);
					}
					states.add(new CanvasStateSerialized(canvas.getCurrentCanvasState()));
					index++;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				break;
		}
	}

	public void redo()
	{
		if (!hasNextRedo())
		{
			return;
		}

		try
		{
			CanvasState nobject = states.get(index + 1).toCanvasState();
			CanvasState object = states.get(index).toCanvasState();			
			index++;
			monitor.firePropertyChange("object_state", object, nobject);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void undo()
	{
		if (!hasNextUndo())
		{
			return;
		}

		try
		{
			CanvasState nobject = states.get(index - 1).toCanvasState();
			CanvasState object = states.get(index).toCanvasState();
			index--;
			monitor.firePropertyChange("object_state", object, nobject);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
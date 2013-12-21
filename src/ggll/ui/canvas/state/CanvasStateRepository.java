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
				final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
				objectOutputStream.writeObject(canvasState);
				this.serializedObject = byteArrayOutputStream.toByteArray();
			}
			catch (final Exception e)
			{
			}
		}

		public CanvasState toCanvasState()
		{
			try
			{
				final ByteArrayInputStream bais = new ByteArrayInputStream(this.serializedObject);
				final ObjectInputStream ois = new ObjectInputStream(bais);
				return (CanvasState) ois.readObject();
			}
			catch (final Exception e)
			{
			}
			return null;
		}
	}

	private final ArrayList<CanvasStateSerialized> states = new ArrayList<CanvasStateSerialized>();
	private int index = -1;
	private final Canvas canvas;

	private final PropertyChangeSupport monitor;

	public CanvasStateRepository(Canvas canvas)
	{
		this.canvas = canvas;
		this.states.add(new CanvasStateSerialized(canvas.getCurrentCanvasState()));

		this.index++;

		this.monitor = new PropertyChangeSupport(this);
		this.monitor.addPropertyChangeListener("object_state", canvas);
		this.monitor.addPropertyChangeListener("writing", canvas);
		this.monitor.addPropertyChangeListener("undoable", canvas);
	}

	public boolean hasNextRedo()
	{
		return this.states.size() > this.index + 1;
	}

	public boolean hasNextUndo()
	{
		return this.index > 0;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event)
	{
		switch (event.getPropertyName())
		{
			case "undoable":
				try
				{
					this.monitor.firePropertyChange("writing", null, this.canvas.getCurrentCanvasState());
					final int size = this.states.size();
					for (int i = size - 1; i > this.index; i--)
					{
						this.states.remove(i);
					}
					this.states.add(new CanvasStateSerialized(this.canvas.getCurrentCanvasState()));
					this.index++;
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
		if (!hasNextRedo())
		{
			return;
		}

		try
		{
			final CanvasState nobject = this.states.get(this.index + 1).toCanvasState();
			final CanvasState object = this.states.get(this.index).toCanvasState();
			this.index++;
			this.monitor.firePropertyChange("object_state", object, nobject);
		}
		catch (final Exception e)
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
			final CanvasState nobject = this.states.get(this.index - 1).toCanvasState();
			final CanvasState object = this.states.get(this.index).toCanvasState();
			this.index--;
			this.monitor.firePropertyChange("object_state", object, nobject);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}
}
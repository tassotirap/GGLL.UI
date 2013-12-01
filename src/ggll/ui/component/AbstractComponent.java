package ggll.ui.component;

import ggll.core.list.ExtendedList;

import javax.swing.JComponent;

public abstract class AbstractComponent
{
	protected JComponent jComponent;
	protected ExtendedList<ComponentListener> listeners = new ExtendedList<ComponentListener>();

	public void addComponentListener(ComponentListener listener)
	{
		if (!listeners.contains(listener))
		{
			listeners.append(listener);
		}
	}

	public abstract void fireContentChanged();

	public JComponent getJComponent()
	{
		return jComponent;
	}

	public void removeComponentListener(ComponentListener listener)
	{
		if (listeners.contains(listener))
		{
			listeners.remove(listener);
		}
	}
}

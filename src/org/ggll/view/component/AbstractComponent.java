package org.ggll.view.component;

import ggll.core.list.ExtendedList;

import javax.swing.JComponent;

public abstract class AbstractComponent
{
	protected ExtendedList<ComponentListener> listeners = new ExtendedList<ComponentListener>();
	protected JComponent jComponent;

	public void addComponentListener(ComponentListener listener)
	{
		if (!this.listeners.contains(listener))
		{
			this.listeners.append(listener);
		}
	}

	public abstract void fireContentChanged();

	public JComponent getJComponent()
	{
		return this.jComponent;
	}

	public void removeComponentListener(ComponentListener listener)
	{
		if (this.listeners.contains(listener))
		{
			this.listeners.remove(listener);
		}
	}
}

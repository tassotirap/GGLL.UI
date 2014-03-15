package org.ggll.window.component;

import ggll.core.list.ExtendedList;

import javax.swing.JComponent;

public abstract class AbstractComponent
{
	protected ExtendedList<ComponentListener> listeners = new ExtendedList<ComponentListener>();
	protected JComponent jComponent;
	
	public void addComponentListener(final ComponentListener listener)
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
	
	public void removeComponentListener(final ComponentListener listener)
	{
		if (listeners.contains(listener))
		{
			listeners.remove(listener);
		}
	}
}

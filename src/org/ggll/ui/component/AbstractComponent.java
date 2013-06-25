package org.ggll.ui.component;

import java.util.ArrayList;

import javax.swing.JComponent;

public abstract class AbstractComponent
{
	protected JComponent jComponent;
	protected ArrayList<ComponentListener> listeners = new ArrayList<ComponentListener>();

	public void addComponentListener(ComponentListener listener)
	{
		if (!listeners.contains(listener))
		{
			listeners.add(listener);
		}
	}

	public abstract void fireContentChanged();

	/**
	 * 
	 * @return the current component
	 */
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

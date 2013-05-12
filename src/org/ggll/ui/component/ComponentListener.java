package org.ggll.ui.component;

public interface ComponentListener
{
	public abstract void ContentChanged(AbstractComponent source, Object oldValue, Object newValue);
}

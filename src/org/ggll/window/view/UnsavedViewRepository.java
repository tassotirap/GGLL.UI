package org.ggll.window.view;

import ggll.core.list.ExtendedList;

import java.util.HashMap;

public class UnsavedViewRepository
{
	private final HashMap<String, AbstractView> unsavedViews;
	
	public UnsavedViewRepository()
	{
		unsavedViews = new HashMap<String, AbstractView>();
	}
	
	public AbstractView getUnsavedView(final String key)
	{
		if (!unsavedViews.containsKey(key)) { return unsavedViews.get(key); }
		return null;
	}
	
	public ExtendedList<AbstractView> getUnsavedViews()
	{
		return new ExtendedList<AbstractView>(unsavedViews.values());
	}
	
	public boolean hasUnsavedView(final AbstractView value)
	{
		return unsavedViews.containsValue(value);
	}
	
	public boolean hasUnsavedView(final String key)
	{
		return unsavedViews.containsKey(key);
	}
	
	public void removeUnsavedView(final String key)
	{
		unsavedViews.remove(key);
	}
	
	public void setUnsavedView(final String key, final AbstractView value)
	{
		if (!unsavedViews.containsKey(key))
		{
			unsavedViews.put(key, value);
		}
	}
	
}

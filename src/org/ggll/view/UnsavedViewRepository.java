package org.ggll.view;

import ggll.core.list.ExtendedList;

import java.util.HashMap;

public class UnsavedViewRepository
{
	private final HashMap<String, AbstractView> unsavedViews;

	public UnsavedViewRepository()
	{
		this.unsavedViews = new HashMap<String, AbstractView>();
	}

	public AbstractView getUnsavedView(String key)
	{
		if (!this.unsavedViews.containsKey(key))
		{
			return this.unsavedViews.get(key);
		}
		return null;
	}

	public ExtendedList<AbstractView> getUnsavedViews()
	{
		return new ExtendedList<AbstractView>(this.unsavedViews.values());
	}

	public boolean hasUnsavedView(AbstractView value)
	{
		return this.unsavedViews.containsValue(value);
	}

	public boolean hasUnsavedView(String key)
	{
		return this.unsavedViews.containsKey(key);
	}

	public void removeUnsavedView(String key)
	{
		this.unsavedViews.remove(key);
	}

	public void setUnsavedView(String key, AbstractView value)
	{
		if (!this.unsavedViews.containsKey(key))
		{
			this.unsavedViews.put(key, value);
		}
	}

}

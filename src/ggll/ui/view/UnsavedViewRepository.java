package ggll.ui.view;

import ggll.core.list.ExtendedList;

import java.util.HashMap;

public class UnsavedViewRepository
{
	private HashMap<String, AbstractView> unsavedViews;

	public UnsavedViewRepository()
	{
		unsavedViews = new HashMap<String, AbstractView>();
	}

	public ExtendedList<AbstractView> getUnsavedViews()
	{
		return new ExtendedList<AbstractView>(unsavedViews.values());
	}

	public boolean hasUnsavedView(AbstractView value)
	{
		return unsavedViews.containsValue(value);
	}

	public boolean hasUnsavedView(String key)
	{
		return unsavedViews.containsKey(key);
	}

	public void removeUnsavedView(String key)
	{
		unsavedViews.remove(key);
	}

	public void setUnsavedView(String key, AbstractView value)
	{
		if (!unsavedViews.containsKey(key))
		{
			unsavedViews.put(key, value);
		}
	}

}

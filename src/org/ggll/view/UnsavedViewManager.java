package org.ggll.view;

import java.util.ArrayList;
import java.util.HashMap;

public class UnsavedViewManager
{
	private HashMap<String, GGLLView> unsavedViews;
	
	public UnsavedViewManager()
	{
		unsavedViews = new HashMap<String, GGLLView>();		
	}
	
	public ArrayList<GGLLView> getUnsavedViews()
	{
		return new ArrayList<GGLLView>(unsavedViews.values());
	}
	
	public boolean hasUnsavedView(GGLLView value)
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
	
	public void setUnsavedView(String key, GGLLView value)
	{
		if (!unsavedViews.containsKey(key))
		{
			unsavedViews.put(key, value);
		}
	}

}

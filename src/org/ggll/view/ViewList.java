package org.ggll.view;

import java.util.ArrayList;

public class ViewList
{
	ArrayList<GGLLView> viewList;

	public ViewList()
	{
		viewList = new ArrayList<GGLLView>();
	}

	public boolean add(GGLLView dynamicView)
	{
		return viewList.add(dynamicView);
	}

	public GGLLView get(int index)
	{
		return viewList.get(index);
	}

	public int size()
	{
		return viewList.size();
	}

	public GGLLView[] toArray()
	{
		return viewList.toArray(new GGLLView[viewList.size()]);
	}
}

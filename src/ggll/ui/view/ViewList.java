package ggll.ui.view;

import java.util.ArrayList;

public class ViewList
{
	ArrayList<AbstractView> viewList;

	public ViewList()
	{
		viewList = new ArrayList<AbstractView>();
	}

	public boolean add(AbstractView dynamicView)
	{
		return viewList.add(dynamicView);
	}

	public AbstractView get(int index)
	{
		return viewList.get(index);
	}

	public int size()
	{
		return viewList.size();
	}

	public AbstractView[] toArray()
	{
		return viewList.toArray(new AbstractView[viewList.size()]);
	}
}

package org.ggll.view;

import java.util.ArrayList;
import java.util.HashMap;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.View;
import net.infonode.docking.util.ViewMap;

import org.ggll.ui.TabItem;
import org.ggll.ui.component.AbstractComponent;
import org.ggll.view.ViewList;

public class ViewRepository
{
	private ArrayList<ViewList> defaultLayout;
	private static int DEFAULT_LAYOUT = 6;
	private HashMap<AbstractComponent, GGLLView> dynamicViewsByComponent = new HashMap<AbstractComponent, GGLLView>();
	private HashMap<Integer, GGLLView> dynamicViewsById = new HashMap<Integer, GGLLView>();
	private HashMap<String, GGLLView> dynamicViewsByPath = new HashMap<String, GGLLView>();

	public ViewRepository()
	{
		this.defaultLayout = new ArrayList<ViewList>();
	}

	private void addDynamicView(GGLLView dynamicView)
	{
		dynamicViewsById.put(new Integer(dynamicView.getId()), dynamicView);
		dynamicViewsByComponent.put(dynamicView.getComponentModel(), dynamicView);
		if (dynamicView.getFileName() != null)
		{
			dynamicViewsByPath.put(dynamicView.getFileName(), dynamicView);
		}
	}

	private void removeDynamicView(GGLLView dynamicView)
	{
		dynamicViewsById.remove(new Integer(dynamicView.getId()));
		dynamicViewsByComponent.remove(dynamicView.getComponentModel());
		if (dynamicViewsByPath.containsKey(dynamicView.getFileName()))
		{
			dynamicViewsByPath.remove(dynamicView.getFileName());
		}
	}

	private void updateChildDynamicViews(DockingWindow window, boolean added)
	{
		for (int i = 0; i < window.getChildWindowCount(); i++)
		{
			updateViews(window.getChildWindow(i), added);
		}
	}

	public boolean containsDynamicView(AbstractComponent component)
	{
		return dynamicViewsByComponent.containsKey(component);
	}

	public boolean containsDynamicView(int id)
	{
		return dynamicViewsById.containsKey(id);
	}

	public boolean containsDynamicView(String path)
	{
		return dynamicViewsByPath.containsKey(path);
	}

	public void createDefaultViews(ArrayList<TabItem> tabItems, ViewMap perspectiveMap)
	{
		try
		{
			for (int i = 0; i < DEFAULT_LAYOUT; i++)
				defaultLayout.add(new ViewList());

			for (int i = 0; i < tabItems.size(); i++)
			{
				int nextId = getDynamicViewId();
				GGLLView view = new GGLLView(tabItems.get(i).getTitle(), tabItems.get(i).getViewIcon(), tabItems.get(i).getComponent(), null, null, nextId);
				defaultLayout.get(tabItems.get(i).getLayoutOrder()).add(view);
				perspectiveMap.addView(i, view);
				updateViews(view, true);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<ViewList> getDefaultLayout()
	{
		return defaultLayout;
	}

	public GGLLView getDynamicView(AbstractComponent component)
	{
		return dynamicViewsByComponent.get(component);
	}

	public GGLLView getDynamicView(int id)
	{
		return dynamicViewsById.get(id);
	}

	public GGLLView getDynamicView(String path)
	{
		return dynamicViewsByPath.get(path);
	}

	public int getDynamicViewId()
	{
		int id = 0;

		while (dynamicViewsById.containsKey(new Integer(id)))
			id++;

		return id;
	}

	public void updateViews(DockingWindow window, boolean added)
	{
		if (window instanceof View)
		{
			if (window instanceof GGLLView)
			{
				GGLLView dynamicView = (GGLLView) window;
				if (added)
				{
					addDynamicView(dynamicView);
				}
				else
				{
					removeDynamicView(dynamicView);
				}
			}
		}
		else
		{
			updateChildDynamicViews(window, added);
		}
	}
}

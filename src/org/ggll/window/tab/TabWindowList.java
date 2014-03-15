package org.ggll.window.tab;

import ggll.core.list.ExtendedList;
import net.infonode.docking.TabWindow;

public class TabWindowList
{
	
	public enum TabPlace
	{
		TOP_TABS, CENTER_RIGHT_TOP_TABS, CENTER_RIGHT_BOTTOM_TABS, BOTTOM_TABS, CENTER_LEFT_TABS
	}
	
	private final ExtendedList<TabWindow> tabWindowList;
	
	public static final int TAB_SIZE = 4;
	
	public TabWindowList()
	{
		tabWindowList = new ExtendedList<TabWindow>(TabWindowList.TAB_SIZE);
	}
	
	public void add(final TabWindow tabWindow)
	{
		tabWindowList.append(tabWindow);
	}
	
	public TabWindow getBottonTab()
	{
		return tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal());
	}
	
	public TabWindow getCenterLeftTab()
	{
		return tabWindowList.get(TabPlace.CENTER_LEFT_TABS.ordinal());
	}
	
	public TabWindow getCenterRightBottomTab()
	{
		return tabWindowList.get(TabPlace.CENTER_RIGHT_BOTTOM_TABS.ordinal());
	}
	
	public TabWindow getCenterRightTopTab()
	{
		return tabWindowList.get(TabPlace.CENTER_RIGHT_TOP_TABS.ordinal());
	}
	
	public TabWindow getTabWindow(final int place)
	{
		return tabWindowList.get(place);
	}
	
	public TabWindow getTabWindow(final TabPlace place)
	{
		return tabWindowList.get(place.ordinal());
	}
	
	public TabWindow getTopTab()
	{
		
		return tabWindowList.get(TabPlace.TOP_TABS.ordinal());
	}
}

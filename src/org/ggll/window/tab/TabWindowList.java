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
		this.tabWindowList = new ExtendedList<TabWindow>(TAB_SIZE);
	}

	public void add(TabWindow tabWindow)
	{
		this.tabWindowList.append(tabWindow);
	}

	public TabWindow getBottonTab()
	{
		return this.tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal());
	}

	public TabWindow getCenterLeftTab()
	{
		return this.tabWindowList.get(TabPlace.CENTER_LEFT_TABS.ordinal());
	}

	public TabWindow getCenterRightBottomTab()
	{
		return this.tabWindowList.get(TabPlace.CENTER_RIGHT_BOTTOM_TABS.ordinal());
	}

	public TabWindow getCenterRightTopTab()
	{
		return this.tabWindowList.get(TabPlace.CENTER_RIGHT_TOP_TABS.ordinal());
	}

	public TabWindow getTabWindow(int place)
	{
		return this.tabWindowList.get(place);
	}

	public TabWindow getTabWindow(TabPlace place)
	{
		return this.tabWindowList.get(place.ordinal());
	}

	public TabWindow getTopTab()
	{

		return this.tabWindowList.get(TabPlace.TOP_TABS.ordinal());
	}
}

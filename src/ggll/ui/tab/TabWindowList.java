package ggll.ui.tab;

import java.util.ArrayList;

import net.infonode.docking.TabWindow;

public class TabWindowList
{

	public enum TabPlace
	{
		TOP_TABS, CENTER_RIGHT_TABS, BOTTOM_TABS, CENTER_LEFT_TABS
	}

	private ArrayList<TabWindow> tabWindowList;

	public static final int TAB_SIZE = 4;

	public TabWindowList()
	{
		tabWindowList = new ArrayList<TabWindow>(TAB_SIZE);
	}

	public boolean add(TabWindow tabWindow)
	{
		return tabWindowList.add(tabWindow);
	}

	public TabWindow getBottonTab()
	{
		return tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal());
	}

	public TabWindow getCenterLeftTab()
	{
		return tabWindowList.get(TabPlace.CENTER_LEFT_TABS.ordinal());
	}

	public TabWindow getTopTab()
	{

		return tabWindowList.get(TabPlace.TOP_TABS.ordinal());
	}

	public TabWindow getCenterRightTab()
	{
		return tabWindowList.get(TabPlace.CENTER_RIGHT_TABS.ordinal());
	}

	public TabWindow getTabWindow(int place)
	{
		return tabWindowList.get(place);
	}

	public TabWindow getTabWindow(TabPlace place)
	{
		return tabWindowList.get(place.ordinal());
	}
}

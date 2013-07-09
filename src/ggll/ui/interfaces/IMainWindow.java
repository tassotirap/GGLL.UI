package ggll.ui.interfaces;

import ggll.ui.TabWindowList;
import ggll.ui.ThemeManager.Theme;
import ggll.ui.component.AbstractComponent;
import ggll.ui.model.IconView;

import javax.swing.Icon;
import javax.swing.JFrame;

import net.infonode.docking.DockingWindow;

public interface IMainWindow
{
	public final static String DEFAULT_NAME = "GGLL Window";
	public final static String DEFAULT_TITLE = "GGLL";
	public final static String UNSAVED_PREFIX = "* ";
	public final static Icon VIEW_ICON = new IconView();

	public abstract void addEmptyDynamicView();

	public abstract void changeTheme(Theme theme);

	public abstract JFrame getFrame();

	public abstract TabWindowList getTabs();

	public abstract TabWindowList getTabWindowList();

	public abstract void removeEmptyDynamicView();

	public abstract void setSaved(String path);

	public abstract void updateFocusedComponent(AbstractComponent component);

	public abstract void updateWindow(DockingWindow window, boolean added);

}
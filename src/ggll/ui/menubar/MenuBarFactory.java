package ggll.ui.menubar;

import ggll.project.GGLLManager;
import ggll.ui.Menu;
import ggll.ui.Menu.MenuModel;
import ggll.ui.interfaces.IMainWindow;

import java.util.HashMap;

import javax.swing.JMenuBar;

public class MenuBarFactory
{
	private JMenuBar defaultMenuBar;
	private HashMap<Object, JMenuBar> menuBars = new HashMap<Object, JMenuBar>();
	private IMainWindow window;

	public MenuBarFactory()
	{
		this.window = GGLLManager.getMainWindow();
	}

	@SuppressWarnings("rawtypes")
	public JMenuBar createMenuBar(final Object context, MenuModel model)
	{
		if (context == null)
		{
			if (defaultMenuBar == null)
			{
				defaultMenuBar = createMenuBarExt(null, model);
				return defaultMenuBar;
			}
		}
		if (!menuBars.containsKey(context))
		{
			menuBars.put(context, createMenuBarExt(context, model));
		}
		return menuBars.get(context);
	}

	@SuppressWarnings("rawtypes")
	public JMenuBar createMenuBarExt(Object context, MenuModel model)
	{
		Menu menu = new Menu(new String[]{ Menu.FILE, Menu.EDIT, Menu.OPTIONS, Menu.PROJECT, Menu.WINDOW, Menu.HELP }, window, context, model);
		menu.build();
		return menu;
	}

}
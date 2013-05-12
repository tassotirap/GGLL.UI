package org.ggll.ui.menubar;

import java.util.HashMap;

import javax.swing.JMenuBar;

import org.ggll.actions.ActionContextHolder;
import org.ggll.project.GGLLManager;
import org.ggll.ui.Menu;
import org.ggll.ui.Menu.MenuModel;
import org.ggll.ui.interfaces.IMainWindow;

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
	public JMenuBar createMenuBarExt(ActionContextHolder context, MenuModel model)
	{
		Menu menu = new Menu(new String[]{ Menu.FILE, Menu.EDIT, Menu.OPTIONS, Menu.PROJECT, Menu.WINDOW, Menu.HELP }, window, context, model);
		menu.build();
		return menu;
	}
	
	@SuppressWarnings("rawtypes")
	public JMenuBar createMenuBar(final ActionContextHolder context, MenuModel model)
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

}

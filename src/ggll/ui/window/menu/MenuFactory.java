package ggll.ui.window.menu;

import ggll.ui.view.component.AbstractComponent;

import java.util.HashMap;

import javax.swing.JMenuBar;

public class MenuFactory
{
	private JMenuBar defaultMenuBar;
	private HashMap<AbstractComponent, JMenuBar> menuBars = new HashMap<AbstractComponent, JMenuBar>();

	public MenuFactory()
	{
	}

	private JMenuBar getDefaultMenuBar(MenuModel model)
	{
		if (defaultMenuBar == null)
		{
			defaultMenuBar = createMenuBarExt(null, model);
		}
		return defaultMenuBar;
	}

	@SuppressWarnings("rawtypes")
	public JMenuBar createMenuBar(final AbstractComponent context, MenuModel model)
	{
		if (context == null)
		{
			return getDefaultMenuBar(model);
		}
		if (!menuBars.containsKey(context))
		{
			menuBars.put(context, createMenuBarExt(context, model));
		}
		return menuBars.get(context);
	}

	@SuppressWarnings("rawtypes")
	public JMenuBar createMenuBarExt(AbstractComponent context, MenuModel model)
	{
		Menu menu = new Menu(new String[]{ Menu.FILE, Menu.HELP }, context, model);
		menu.build();
		return menu;
	}

}

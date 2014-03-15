package org.ggll.window.menu;

import java.util.HashMap;

import javax.swing.JMenuBar;

import org.ggll.window.component.AbstractComponent;

public class MenuFactory
{
	private JMenuBar defaultMenuBar;
	private final HashMap<AbstractComponent, JMenuBar> menuBars = new HashMap<AbstractComponent, JMenuBar>();
	
	public MenuFactory()
	{
	}
	
	public JMenuBar createMenuBar(final AbstractComponent context, final MenuModel model)
	{
		if (context == null) { return getDefaultMenuBar(model); }
		if (!menuBars.containsKey(context))
		{
			menuBars.put(context, createMenuBarExt(context, model));
		}
		return menuBars.get(context);
	}
	
	public JMenuBar createMenuBarExt(final AbstractComponent context, final MenuModel model)
	{
		final Menu menu = new Menu(new String[]
		{ Menu.FILE, Menu.HELP }, context, model);
		menu.build();
		return menu;
	}
	
	private JMenuBar getDefaultMenuBar(final MenuModel model)
	{
		if (defaultMenuBar == null)
		{
			defaultMenuBar = createMenuBarExt(null, model);
		}
		return defaultMenuBar;
	}
	
}

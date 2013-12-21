package ggll.ui.window.menu;

import ggll.ui.view.component.AbstractComponent;

import java.util.HashMap;

import javax.swing.JMenuBar;

public class MenuFactory
{
	private JMenuBar defaultMenuBar;
	private final HashMap<AbstractComponent, JMenuBar> menuBars = new HashMap<AbstractComponent, JMenuBar>();

	public MenuFactory()
	{
	}

	private JMenuBar getDefaultMenuBar(MenuModel model)
	{
		if (this.defaultMenuBar == null)
		{
			this.defaultMenuBar = createMenuBarExt(null, model);
		}
		return this.defaultMenuBar;
	}

	@SuppressWarnings("rawtypes")
	public JMenuBar createMenuBar(final AbstractComponent context, MenuModel model)
	{
		if (context == null)
		{
			return getDefaultMenuBar(model);
		}
		if (!this.menuBars.containsKey(context))
		{
			this.menuBars.put(context, createMenuBarExt(context, model));
		}
		return this.menuBars.get(context);
	}

	@SuppressWarnings("rawtypes")
	public JMenuBar createMenuBarExt(AbstractComponent context, MenuModel model)
	{
		final Menu menu = new Menu(new String[]{ Menu.FILE, Menu.HELP }, context, model);
		menu.build();
		return menu;
	}

}

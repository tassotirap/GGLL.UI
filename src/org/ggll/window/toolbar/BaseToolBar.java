package org.ggll.window.toolbar;

import javax.swing.JToolBar;

public abstract class BaseToolBar extends JToolBar
{
	private static final long serialVersionUID = 1L;

	protected Object context;

	protected final String imgPath = "/org/ggll/images/";

	public BaseToolBar(Object context)
	{
		this.context = context;
		initComponets();
		initActions();
		initLayout();
		this.setFloatable(false);
	}

	protected abstract void initActions();

	protected abstract void initComponets();

	protected abstract void initLayout();
}

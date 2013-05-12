package org.ggll.ui.toolbar;

import javax.swing.JToolBar;

import org.ggll.actions.ActionContextHolder;

public abstract class BaseToolBar extends JToolBar
{
	private static final long serialVersionUID = 1L;

	protected ActionContextHolder context;

	protected final String imgPath = "/org/ggll/images/";

	public BaseToolBar(ActionContextHolder context)
	{
		this.context = context;
		initComponets();
		initActions();
		initLayout();
	}

	protected abstract void initActions();

	protected abstract void initComponets();

	protected abstract void initLayout();
}

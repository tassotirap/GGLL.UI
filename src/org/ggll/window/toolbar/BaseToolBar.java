package org.ggll.window.toolbar;

import javax.swing.JToolBar;

public abstract class BaseToolBar extends JToolBar
{
	private static final long serialVersionUID = 1L;
	
	protected Object context;
	
	public BaseToolBar(final Object context)
	{
		this.context = context;
		initComponets();
		initActions();
		initLayout();
		setFloatable(false);
	}
	
	protected abstract void initActions();
	
	protected abstract void initComponets();
	
	protected abstract void initLayout();
}

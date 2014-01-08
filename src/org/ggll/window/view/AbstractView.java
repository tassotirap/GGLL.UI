package org.ggll.window.view;


import javax.swing.Icon;

import org.ggll.window.component.AbstractComponent;

import net.infonode.docking.View;

public class AbstractView extends View
{
	private static final long serialVersionUID = 1L;
	private final AbstractComponent componentModel;
	private String fileName;
	private final int id;

	public AbstractView(String title, Icon icon, AbstractComponent componentModel, int id)
	{
		super(title, icon, componentModel.getJComponent());
		this.id = id;
		this.componentModel = componentModel;
		getWindowProperties().setCloseEnabled(false);
	}

	public AbstractView(String title, Icon icon, AbstractComponent componentModel, String fileName, int id)
	{
		super(title, icon, componentModel.getJComponent());
		this.id = id;
		this.componentModel = componentModel;
		this.fileName = fileName;
	}

	public AbstractComponent getComponentModel()
	{
		return this.componentModel;
	}

	public String getFileName()
	{
		return this.fileName;
	}

	public int getId()
	{
		return this.id;
	}
}

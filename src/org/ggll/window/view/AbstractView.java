package org.ggll.window.view;

import javax.swing.Icon;

import net.infonode.docking.View;

import org.ggll.window.component.AbstractComponent;

public class AbstractView extends View
{
	private static final long serialVersionUID = 1L;
	private final AbstractComponent componentModel;
	private String fileName;
	private final int id;
	
	public AbstractView(final String title, final Icon icon, final AbstractComponent componentModel, final int id)
	{
		super(title, icon, componentModel.getJComponent());
		this.id = id;
		this.componentModel = componentModel;
		getWindowProperties().setCloseEnabled(false);
		getWindowProperties().setUndockEnabled(false);
	}
	
	public AbstractView(final String title, final Icon icon, final AbstractComponent componentModel, final String fileName, final int id)
	{
		super(title, icon, componentModel.getJComponent());
		this.id = id;
		this.componentModel = componentModel;
		this.fileName = fileName;
		getWindowProperties().setUndockEnabled(false);
	}
	
	public AbstractComponent getComponentModel()
	{
		return componentModel;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public int getId()
	{
		return id;
	}
}

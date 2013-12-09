package ggll.ui.view;

import ggll.ui.view.component.AbstractComponent;

import javax.swing.Icon;

import net.infonode.docking.View;

public class AbstractView extends View
{
	private static final long serialVersionUID = 1L;
	private AbstractComponent componentModel;
	private String fileName;
	private int id;

	public AbstractView(String title, Icon icon, AbstractComponent componentModel, int id)
	{
		super(title, icon, componentModel.getJComponent());
		this.id = id;
		this.componentModel = componentModel;
		this.getWindowProperties().setCloseEnabled(false);
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

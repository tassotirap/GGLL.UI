package org.ggll.view;

import java.awt.Component;

import javax.swing.Icon;

import net.infonode.docking.View;

import org.ggll.ui.component.AbstractComponent;

public class GGLLView extends View
{
	private static final long serialVersionUID = 1L;
	private AbstractComponent componentModel;
	private String fileName;
	private int id;

	public GGLLView(String title, Icon icon, Component component, AbstractComponent componentModel, String fileName, int id)
	{
		super(title, icon, component);
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

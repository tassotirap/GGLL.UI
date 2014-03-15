package org.ggll.window.component;

import javax.swing.JScrollPane;

import org.ggll.project.tree.Tree;

public class ProjectsComponent extends AbstractComponent
{
	Tree fileTree;
	
	public ProjectsComponent()
	{
		fileTree = new Tree();
		jComponent = new JScrollPane(fileTree.getTree());
	}
	
	@Override
	public void fireContentChanged()
	{
	}
}

package org.ggll.view.component;


import javax.swing.JScrollPane;

import org.ggll.project.Project;
import org.ggll.project.tree.Tree;

public class ProjectsComponent extends AbstractComponent
{
	Tree fileTree;

	public ProjectsComponent(Project project)
	{
		this.fileTree = new Tree();
		this.jComponent = new JScrollPane(this.fileTree.getTree());
	}

	@Override
	public void fireContentChanged()
	{
	}
}

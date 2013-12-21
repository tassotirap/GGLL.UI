package ggll.ui.view.component;

import ggll.ui.project.Project;
import ggll.ui.project.tree.Tree;

import javax.swing.JScrollPane;

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

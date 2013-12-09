package ggll.ui.view.component;

import ggll.ui.project.Project;
import ggll.ui.project.tree.Tree;

import javax.swing.JScrollPane;

public class ProjectsComponent extends AbstractComponent
{
	Tree fileTree;
	
	public ProjectsComponent(Project project)
	{
		fileTree = new Tree();
		jComponent = new JScrollPane(fileTree.getTree());
	}

	@Override
	public void fireContentChanged()
	{
	}
}

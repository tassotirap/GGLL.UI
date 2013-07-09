package ggll.ui.component;

import ggll.project.Project;
import ggll.project.tree.Tree;

import javax.swing.JScrollPane;

public class ProjectsComponent extends AbstractComponent
{
	public ProjectsComponent(Project project)
	{
		Tree fileTree = new Tree();
		jComponent = new JScrollPane(fileTree);
	}

	@Override
	public void fireContentChanged()
	{
	}

}

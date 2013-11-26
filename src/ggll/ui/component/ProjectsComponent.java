package ggll.ui.component;

import ggll.ui.project.Project;
import ggll.ui.project.tree.Tree;

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

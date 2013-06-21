package org.ggll.ui.component;

import javax.swing.JScrollPane;

import org.ggll.project.Project;
import org.ggll.project.tree.Tree;

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

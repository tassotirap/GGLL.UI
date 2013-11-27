package ggll.ui.component;

import ggll.ui.project.GGLLManager;
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
	
	public void refresh()
	{
		String rootPath = GGLLManager.getProject().getProjectsRootPath();
		fileTree.reload(rootPath);
	}

}

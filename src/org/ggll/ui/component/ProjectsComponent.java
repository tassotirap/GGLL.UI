package org.ggll.ui.component;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.ggll.project.Project;
import org.ggll.project.tree.FileTree;

public class ProjectsComponent extends AbstractComponent
{

	@Override
	public JComponent create(Object param) throws BadParameterException
	{
		if (param instanceof Project)
		{
			FileTree fileTree = new FileTree();
			JScrollPane jScrollPane = new JScrollPane(fileTree.getView());
			return jScrollPane;
		}
		else
		{
			throw new BadParameterException("A Reference to a directoty was expected.");
		}
	}

	@Override
	public void fireContentChanged()
	{
	}

}

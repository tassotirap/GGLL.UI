package org.ggll.ui.component;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.ggll.project.Project;
import org.ggll.project.tree.Tree;

public class ProjectsComponent extends AbstractComponent
{

	@Override
	public JComponent create(Object param) throws BadParameterException
	{
		if (param instanceof Project)
		{
			Tree fileTree = new Tree();
			JScrollPane jScrollPane = new JScrollPane(fileTree);
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

package ggll.ui.project.tree;

import ggll.core.list.ExtendedList;

import java.io.File;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class TreeFileModel implements TreeModel
{
	private final File root;
	private final ExtendedList<TreeModelListener> listeners = new ExtendedList<TreeModelListener>();

	public TreeFileModel(File rootDirectory)
	{
		this.root = rootDirectory;
	}

	@Override
	public void addTreeModelListener(TreeModelListener listener)
	{
		this.listeners.append(listener);
	}

	public void fireTreeNodesChanged(TreePath parentPath, int[] indices, Object[] children)
	{
		final TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
		for (final TreeModelListener treeModelListener : this.listeners.getAll())
		{
			treeModelListener.treeNodesChanged(event);
		}
	}

	public void fireTreeStructureChanged(Object source, TreePath path)
	{
		final TreeModelEvent event = new TreeModelEvent(source, path);
		for (final TreeModelListener treeModelListener : this.listeners.getAll())
		{
			treeModelListener.treeStructureChanged(event);
		}
	}

	@Override
	public Object getChild(Object parent, int index)
	{
		final File directory = (File) parent;
		final String[] children = directory.list(new TreeFileNameFilter());
		return new TreeFile(directory, children[index]);
	}

	@Override
	public int getChildCount(Object parent)
	{
		final File file = (File) parent;
		if (file.isDirectory())
		{
			final String[] fileList = file.list(new TreeFileNameFilter());
			if (fileList != null)
			{
				return fileList.length;
			}
		}
		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		final File directory = (File) parent;
		final File file = (File) child;
		final String[] children = directory.list(new TreeFileNameFilter());
		for (int i = 0; i < children.length; i++)
		{
			if (file.getName().equals(children[i]))
			{
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object getRoot()
	{
		return this.root;
	}

	@Override
	public boolean isLeaf(Object node)
	{
		final File file = (File) node;
		return file.isFile();
	}

	@Override
	public void removeTreeModelListener(TreeModelListener listener)
	{
		this.listeners.remove(listener);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object value)
	{

	}
}
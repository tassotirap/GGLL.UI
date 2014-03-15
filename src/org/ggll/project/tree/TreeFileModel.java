package org.ggll.project.tree;

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
	
	public TreeFileModel(final File rootDirectory)
	{
		root = rootDirectory;
	}
	
	@Override
	public void addTreeModelListener(final TreeModelListener listener)
	{
		listeners.append(listener);
	}
	
	public void fireTreeNodesChanged(final TreePath parentPath, final int[] indices, final Object[] children)
	{
		final TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
		for (final TreeModelListener treeModelListener : listeners.getAll())
		{
			treeModelListener.treeNodesChanged(event);
		}
	}
	
	public void fireTreeStructureChanged(final Object source, final TreePath path)
	{
		final TreeModelEvent event = new TreeModelEvent(source, path);
		for (final TreeModelListener treeModelListener : listeners.getAll())
		{
			treeModelListener.treeStructureChanged(event);
		}
	}
	
	@Override
	public Object getChild(final Object parent, final int index)
	{
		final File directory = (File) parent;
		final String[] children = directory.list(new TreeFileNameFilter());
		return new TreeFile(directory, children[index]);
	}
	
	@Override
	public int getChildCount(final Object parent)
	{
		final File file = (File) parent;
		if (file.isDirectory())
		{
			final String[] fileList = file.list(new TreeFileNameFilter());
			if (fileList != null) { return fileList.length; }
		}
		return 0;
	}
	
	@Override
	public int getIndexOfChild(final Object parent, final Object child)
	{
		final File directory = (File) parent;
		final File file = (File) child;
		final String[] children = directory.list(new TreeFileNameFilter());
		for (int i = 0; i < children.length; i++)
		{
			if (file.getName().equals(children[i])) { return i; }
		}
		return -1;
	}
	
	@Override
	public Object getRoot()
	{
		return root;
	}
	
	@Override
	public boolean isLeaf(final Object node)
	{
		final File file = (File) node;
		return file.isFile();
	}
	
	@Override
	public void removeTreeModelListener(final TreeModelListener listener)
	{
		listeners.remove(listener);
	}
	
	@Override
	public void valueForPathChanged(final TreePath path, final Object value)
	{
		
	}
}
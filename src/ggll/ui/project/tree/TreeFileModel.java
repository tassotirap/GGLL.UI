package ggll.ui.project.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class TreeFileModel implements TreeModel
{
	private File root;
	private ArrayList<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	public TreeFileModel(File rootDirectory)
	{
		root = rootDirectory;
	}

	@Override
	public void addTreeModelListener(TreeModelListener listener)
	{
		listeners.add(listener);
	}

	public void fireTreeNodesChanged(TreePath parentPath, int[] indices, Object[] children)
	{
		TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
		Iterator<TreeModelListener> iterator = listeners.iterator();
		TreeModelListener listener = null;
		while (iterator.hasNext())
		{
			listener = iterator.next();
			listener.treeNodesChanged(event);
		}
	}

	public void fireTreeStructureChanged(Object source, TreePath path)
	{
		Iterator<TreeModelListener> iterator = listeners.iterator();
		TreeModelEvent e = null;
		while (iterator.hasNext())
		{
			if (e == null)
				e = new TreeModelEvent(source, path);
			iterator.next().treeStructureChanged(e);
		}
	}

	@Override
	public Object getChild(Object parent, int index)
	{
		File directory = (File) parent;
		String[] children = directory.list(new TreeFileNameFilter());
		return new TreeFile(directory, children[index]);
	}

	@Override
	public int getChildCount(Object parent)
	{
		File file = (File) parent;
		if (file.isDirectory())
		{
			String[] fileList = file.list(new TreeFileNameFilter());
			if (fileList != null)
				return fileList.length;
		}
		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		File directory = (File) parent;
		File file = (File) child;
		String[] children = directory.list(new TreeFileNameFilter());
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
		return root;
	}

	@Override
	public boolean isLeaf(Object node)
	{
		File file = (File) node;
		return file.isFile();
	}

	@Override
	public void removeTreeModelListener(TreeModelListener listener)
	{
		listeners.remove(listener);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object value)
	{

	}
}
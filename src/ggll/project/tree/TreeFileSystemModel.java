package ggll.project.tree;

import java.io.File;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class TreeFileSystemModel implements TreeModel
{
	private File root;

	public TreeFileSystemModel(File rootDirectory)
	{
		root = rootDirectory;
	}

	@Override
	public void addTreeModelListener(TreeModelListener listener)
	{
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
	}

	@Override
	public void valueForPathChanged(TreePath path, Object value)
	{
	}
}
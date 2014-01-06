package org.ggll.project.tree;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.ggll.director.GGLLDirector;
import org.ggll.icon.IconFactory;
import org.ggll.icon.IconFactory.IconType;

/**
 * Display a file system in a JTree view
 * 
 * @author Gustavo H. Braga
 * @author Tasso Tirapani Silva Pinto
 */
public class Tree implements TreeModelListener
{

	private class CustomTreeCellRenderer extends DefaultTreeCellRenderer
	{

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{

			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			final IconFactory iconFactory = new IconFactory();
			if (leaf)
			{
				if (!value.toString().startsWith("."))
				{

					setIcon(iconFactory.getIcon(value.toString()));
				}
			}
			else
			{
				setIcon(iconFactory.getIcon(IconType.DIR_ICON));
			}

			return this;
		}
	}

	private class FileTreeMouseListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent evt)
		{
			if (evt.getClickCount() == 2)
			{
				open(Tree.this.selectedNode);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{

		}

		@Override
		public void mouseExited(MouseEvent e)
		{

		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			if (e.isPopupTrigger())
			{
				// new JTreePopupMenu(instance).show((Component) e.getSource(),
				// e.getX(), e.getY());
			}

		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			if (e.isPopupTrigger())
			{
				// new JTreePopupMenu(instance).show((Component) e.getSource(),
				// e.getX(), e.getY());
			}

		}

	}

	private class FileTreeSelectionListener implements TreeSelectionListener
	{

		@Override
		public void valueChanged(TreeSelectionEvent e)
		{
			instance.selectedNode = e.getPath();
		}

	}

	private static TreeFileModel fsmInstances;

	private static Tree instance;

	private TreePath selectedNode;

	private final JTree tree;

	public Tree()
	{
		instance = this;
		this.tree = new JTree();
		final CustomTreeCellRenderer renderer = new CustomTreeCellRenderer();
		this.tree.setCellRenderer(renderer);
		this.tree.setEditable(true);
		this.tree.setModel(getFileSystemModel(GGLLDirector.getProject().getProjectDir().getAbsolutePath()));
		this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.tree.addTreeSelectionListener(new FileTreeSelectionListener());
		this.tree.addMouseListener(new FileTreeMouseListener());
		getFileSystemModel(GGLLDirector.getProject().getProjectDir().getAbsolutePath()).addTreeModelListener(this);
	}

	private static TreeFileModel getFileSystemModel(String rootPath)
	{
		if (instance == null)
		{
			return null;
		}
		if (fsmInstances == null)
		{
			fsmInstances = new TreeFileModel(new File(rootPath));
		}
		return fsmInstances;
	}

	public static void reload(String rootPath)
	{
		getFileSystemModel(rootPath).fireTreeStructureChanged(getFileSystemModel(rootPath), new TreePath(rootPath));
	}

	public static void update(String rootPath, Object[] changedObjects)
	{
		final int[] indices = new int[changedObjects.length];
		int i = 0;
		final File root = new File(rootPath);
		for (final Object o : changedObjects)
		{
			indices[i++] = getFileSystemModel(rootPath).getIndexOfChild(root, o);
		}
		getFileSystemModel(rootPath).fireTreeNodesChanged(new TreePath(rootPath), indices, changedObjects);
	}

	public Dimension getMinimumSize()
	{
		return new Dimension(200, 400);
	}

	public Dimension getPreferredSize()
	{
		return new Dimension(200, 400);
	}

	public TreePath getSelectedNode()
	{
		return this.selectedNode;
	}

	public JTree getTree()
	{
		return this.tree;
	}

	public JComponent getView()
	{
		return this.tree;
	}

	public void open(TreePath treePath)
	{
		final TreeFile node = (TreeFile) treePath.getLastPathComponent();
		if (node.isFile())
		{
			final String path = node.getAbsolutePath();
			GGLLDirector.openFile(path);
		}
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e)
	{
		this.tree.validate();
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e)
	{
		this.tree.validate();
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e)
	{
		this.tree.validate();
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e)
	{
		this.tree.validate();
	}
}
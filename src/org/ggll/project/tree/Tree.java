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

import org.ggll.facade.GGLLFacade;
import org.ggll.images.IconFactory;
import org.ggll.images.IconFactory.IconType;

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
		public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus)
		{

			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			if (leaf)
			{
				if (!value.toString().startsWith("."))
				{

					setIcon(IconFactory.getIcon(value.toString()));
				}
			}
			else
			{
				setIcon(IconFactory.getIcon(IconType.DIR_ICON));
			}

			return this;
		}
	}

	private class FileTreeMouseListener implements MouseListener
	{

		@Override
		public void mouseClicked(final MouseEvent evt)
		{
			if (evt.getClickCount() == 2)
			{
				open(selectedNode);
			}
		}

		@Override
		public void mouseEntered(final MouseEvent e)
		{

		}

		@Override
		public void mouseExited(final MouseEvent e)
		{

		}

		@Override
		public void mousePressed(final MouseEvent e)
		{
			if (e.isPopupTrigger())
			{
				// new JTreePopupMenu(instance).show((Component) e.getSource(),
				// e.getX(), e.getY());
			}

		}

		@Override
		public void mouseReleased(final MouseEvent e)
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
		public void valueChanged(final TreeSelectionEvent e)
		{
			Tree.getInstance().selectedNode = e.getPath();
		}

	}

	private static TreeFileModel fsmInstances;

	private static Tree instance;

	private static TreeFileModel getFileSystemModel(final String rootPath)
	{
		if (Tree.fsmInstances == null)
		{
			Tree.fsmInstances = new TreeFileModel(new File(rootPath));
		}
		return Tree.fsmInstances;
	}

	public static void reload(final String rootPath)
	{
		Tree.getFileSystemModel(rootPath).fireTreeStructureChanged(Tree.getFileSystemModel(rootPath), new TreePath(rootPath));
	}

	public static void update(final String rootPath, final Object[] changedObjects)
	{
		final int[] indices = new int[changedObjects.length];
		int i = 0;
		final File root = new File(rootPath);
		for (final Object o : changedObjects)
		{
			indices[i++] = Tree.getFileSystemModel(rootPath).getIndexOfChild(root, o);
		}
		Tree.getFileSystemModel(rootPath).fireTreeNodesChanged(new TreePath(rootPath), indices, changedObjects);
	}

	private TreePath selectedNode;

	private final JTree tree;

	private Tree()
	{
		tree = new JTree();
		final CustomTreeCellRenderer renderer = new CustomTreeCellRenderer();
		tree.setCellRenderer(renderer);
		tree.setEditable(true);
		tree.setModel(Tree.getFileSystemModel(GGLLFacade.getInstance().getProjectDir().getAbsolutePath()));
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new FileTreeSelectionListener());
		tree.addMouseListener(new FileTreeMouseListener());
		Tree.getFileSystemModel(GGLLFacade.getInstance().getProjectDir().getAbsolutePath()).addTreeModelListener(this);
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
		return selectedNode;
	}

	public JTree getTree()
	{
		return tree;
	}

	public JComponent getView()
	{
		return tree;
	}

	public void open(final TreePath treePath)
	{
		final TreeFile node = (TreeFile) treePath.getLastPathComponent();
		if (node.isFile())
		{
			final String path = node.getAbsolutePath();
			GGLLFacade.getInstance().openFile(path);
		}
	}

	@Override
	public void treeNodesChanged(final TreeModelEvent e)
	{
		tree.validate();
	}

	@Override
	public void treeNodesInserted(final TreeModelEvent e)
	{
		tree.validate();
	}

	@Override
	public void treeNodesRemoved(final TreeModelEvent e)
	{
		tree.validate();
	}

	@Override
	public void treeStructureChanged(final TreeModelEvent e)
	{
		tree.validate();
	}

	public static Tree getInstance()
	{
		if(instance == null)
		{
			instance = new Tree();
		}
		return instance;
	}
}
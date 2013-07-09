package ggll.project.tree;

import ggll.project.GGLLManager;
import ggll.ui.model.IconFactory;
import ggll.ui.model.IconFactory.IconType;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * 
 * @author Tasso Tirapani Silva Pinto
 * 
 */
public class Tree extends JTree
{
	private class CustomTreeCellRenderer extends DefaultTreeCellRenderer
	{

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			IconFactory iconFactory = new IconFactory();
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
				open(selectedNode);
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
				new TreePopupMenu(instance).show((Component) e.getSource(), e.getX(), e.getY());
			}
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			if (e.isPopupTrigger())
			{
				new TreePopupMenu(instance).show((Component) e.getSource(), e.getX(), e.getY());
			}
		}
	}

	private class FileTreeSelectionListener implements TreeSelectionListener
	{
		@Override
		public void valueChanged(TreeSelectionEvent e)
		{
			selectedNode = e.getPath();
		}
	}

	private static final long serialVersionUID = 1L;

	private Tree instance;

	private TreePath selectedNode;

	public Tree()
	{
		instance = this;
		this.setEditable(false);
		this.setModel(new TreeFileSystemModel(new File(GGLLManager.getProject().getProjectDir().getAbsolutePath())));
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.addTreeSelectionListener(new FileTreeSelectionListener());
		this.addMouseListener(new FileTreeMouseListener());
		this.setCellRenderer(new CustomTreeCellRenderer());
	}

	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension(200, 400);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(200, 400);
	}

	public TreePath getSelectedNode()
	{
		return selectedNode;
	}

	public void open(TreePath treePath)
	{
		if(treePath == null || treePath.getLastPathComponent() == null)
			return;
		
		if (treePath.getLastPathComponent() instanceof TreeFile)
		{
			TreeFile node = (TreeFile) treePath.getLastPathComponent();
			if (node.isFile())
			{
				String path = node.getAbsolutePath();
				GGLLManager.openFile(path);
			}
		}
	}
}
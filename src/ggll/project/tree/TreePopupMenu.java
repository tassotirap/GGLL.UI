package ggll.project.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;

public class TreePopupMenu extends JPopupMenu
{

	private static final long serialVersionUID = 1L;
	private final Tree fileTree;

	public TreePopupMenu(Tree fileTree)
	{
		this.fileTree = fileTree;
		initialize();
	}

	public void initialize()
	{
		final TreePath node = fileTree.getSelectedNode();
		if (node != null && node.getLastPathComponent() instanceof TreeFile)
		{
			TreeFile tFile = (TreeFile) node.getLastPathComponent();
			if (tFile.isFile())
			{
				JMenuItem open = new JMenuItem("Open");
				open.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						fileTree.open(node);
					}
				});
				add(open);
			}
		}
	}
}

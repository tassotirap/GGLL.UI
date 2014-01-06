package org.ggll.window.menu;

import ggll.core.list.ExtendedList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.ggll.director.GGLLDirector;
import org.ggll.file.FileNames;
import org.ggll.project.tree.Tree;
import org.ggll.resource.LangResource;
import org.ggll.view.component.AbstractComponent;

public class Menu extends JMenuBar
{
	private static final long serialVersionUID = 1L;

	public final static String DOTS = "...";
	public final static String FILE = "File";
	public final static String HELP = "Help";

	AbstractComponent context;
	ExtendedList<String> menus;
	MenuModel model;

	public Menu(String[] menus, AbstractComponent context, MenuModel model)
	{
		this.menus = new ExtendedList<String>(menus);
		this.model = model;
		this.context = context;
	}

	private JMenu createFileMenu()
	{
		final JMenu mFile = new JMenu(FILE);
		final ExtendedList<String> PMbuttons = new ExtendedList<String>();
		final ExtendedList<String> Ebuttons = new ExtendedList<String>();

		final JMenuItem grammar = new JMenuItem(LangResource.new_gram);
		grammar.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				final String fileName = JOptionPane.showInputDialog("New Gramma Graph File name?");
				if (fileName != null && !fileName.equals(""))
				{
					try
					{
						GGLLDirector.createFile(fileName, new FileNames(FileNames.GRAM_EXTENSION));
						Tree.reload(GGLLDirector.getProject().getProjectsRootPath());
					}
					catch (final IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});

		final JMenuItem save = new JMenuItem(LangResource.save);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		save.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GGLLDirector.saveFile(Menu.this.context);
			}
		});

		final JMenuItem saveAll = new JMenuItem(LangResource.save_all);
		saveAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		saveAll.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GGLLDirector.saveAllFiles();
			}
		});

		final JMenuItem print = new JMenuItem(LangResource.print + DOTS);
		print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		print.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GGLLDirector.print(Menu.this.context);
			}
		});

		final JMenu exportAs = new JMenu("Export As");
		final JMenuItem png = new JMenuItem("PNG File" + DOTS);
		png.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, ActionEvent.CTRL_MASK));

		final JMenuItem ebnf = new JMenuItem("Extended BNF" + DOTS);
		ebnf.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, ActionEvent.CTRL_MASK));

		exportAs.add(png);
		exportAs.add(ebnf);

		png.setEnabled(this.model.isPngExport());
		ebnf.setEnabled(this.model.isEbnfExport());
		if (!this.model.isPngExport() && !this.model.isEbnfExport())
		{
			exportAs.setEnabled(false);
		}
		save.setEnabled(this.model.isSave());
		print.setEnabled(this.model.isPrint());
		final JMenuItem quit = new JMenuItem("Quit");
		quit.setEnabled(true);

		quit.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				GGLLDirector.exit();
			}
		});

		PMbuttons.append(saveAll.getText());
		PMbuttons.append(quit.getText());
		Ebuttons.append(save.getText());
		Ebuttons.append(print.getText());
		Ebuttons.append(png.getText());
		Ebuttons.append(ebnf.getText());

		mFile.add(grammar);
		mFile.add(new JSeparator());
		mFile.add(save);
		mFile.add(saveAll);
		mFile.add(new JSeparator());
		mFile.add(print);
		mFile.add(new JSeparator());
		mFile.add(exportAs);
		mFile.add(new JSeparator());
		mFile.add(quit);
		return mFile;
	}

	private JMenu createHelpMenu()
	{
		final JMenu helpMenu = new JMenu(HELP);
		helpMenu.setEnabled(false);
		return helpMenu;
	}

	public void build()
	{
		for (final String menu : this.menus.getAll())
		{
			if (menu.equals(FILE))
			{
				this.add(createFileMenu());
			}
			else if (menu.equals(HELP))
			{
				this.add(createHelpMenu());
			}
		}
	}
}

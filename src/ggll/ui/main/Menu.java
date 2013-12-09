package ggll.ui.main;

import ggll.core.list.ExtendedList;
import ggll.ui.file.FileNames;
import ggll.ui.main.ThemeManager.Theme;
import ggll.ui.project.Context;
import ggll.ui.project.tree.Tree;
import ggll.ui.resource.LangResource;
import ggll.ui.view.component.AbstractComponent;
import ggll.ui.view.component.GrammarComponent;
import ggll.ui.view.component.TextAreaComponent;

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

public class Menu extends JMenuBar
{
	public static class MenuModel
	{
		public boolean copy;
		public boolean cut;
		public boolean ebnfExport;
		public boolean find;
		public boolean paste;
		public boolean pngExport;
		public boolean print;
		public boolean redo;
		public boolean save;
		public boolean saveAll;
		public boolean saveAs;
		public boolean undo;
		public boolean zoomIn;
		public boolean zoomOut;
	}

	private static final long serialVersionUID = 1L;
	public final static int CANVAS_CONTEXT = 1;
	public final static int DEFAULT_CONTEXT = 0;
	public final static String DOTS = "...";
	public final static String EDIT = "Edit";
	public final static String FILE = "File";

	public final static String HELP = "Help";
	public final static String OPTIONS = "Options";
	public final static String PROJECT = "Project";

	public final static int TEXTAREA_CONTEXT = 2;

	public final static String WINDOW = "Window";
	AbstractComponent context;
	int contextDesc;
	ExtendedList<String> menus;
	MenuModel model;

	IMainWindow window;

	public Menu(String[] menus, IMainWindow window, AbstractComponent context, MenuModel model)
	{
		this.window = window;
		this.menus = new ExtendedList<String>();
		this.context = context;
		this.model = model;
		if (context instanceof GrammarComponent)
		{
			contextDesc = CANVAS_CONTEXT;
		}
		else if (context instanceof TextAreaComponent)
		{
			contextDesc = TEXTAREA_CONTEXT;
		}
		else
		{
			contextDesc = DEFAULT_CONTEXT;
		}
		this.menus.addAll(menus);
	}

	private JMenu createEditMenu()
	{
		JMenu edit = new JMenu("Edit");
		final ExtendedList<String> Ebuttons = new ExtendedList<String>();
		edit.setMnemonic(KeyEvent.VK_E);
		JMenuItem undo = new JMenuItem("Undo");
		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		JMenuItem redo = new JMenuItem("Redo");
		redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		JMenuItem copy = new JMenuItem("Copy");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		JMenuItem cut = new JMenuItem("Cut");
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		JMenuItem paste = new JMenuItem("Paste");
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		JMenuItem zoomIn = new JMenuItem("Zoom In");
		zoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK));
		JMenuItem zoomOut = new JMenuItem("Zoom Out");
		zoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
		JMenuItem findReplace = new JMenuItem("Find/Replace...");
		findReplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		JMenuItem preferences = new JMenuItem("Preferences...");
		Ebuttons.append(undo.getText());
		Ebuttons.append(redo.getText());
		Ebuttons.append(copy.getText());
		Ebuttons.append(cut.getText());
		Ebuttons.append(paste.getText());
		Ebuttons.append(zoomIn.getText());
		Ebuttons.append(zoomOut.getText());

		undo.setEnabled(model.undo);
		redo.setEnabled(model.redo);
		copy.setEnabled(model.copy);
		paste.setEnabled(model.paste);
		cut.setEnabled(model.cut);
		zoomIn.setEnabled(model.zoomIn);
		zoomOut.setEnabled(model.zoomOut);

		edit.add(undo);
		edit.add(redo);
		edit.add(new JSeparator());
		edit.add(copy);
		edit.add(cut);
		edit.add(paste);
		edit.add(new JSeparator());
		edit.add(zoomIn);
		edit.add(zoomOut);
		edit.add(new JSeparator());
		edit.add(preferences);
		edit.setEnabled(false);
		return edit;
	}

	private JMenu createFileMenu()
	{
		JMenu mFile = new JMenu(FILE);
		final ExtendedList<String> PMbuttons = new ExtendedList<String>();
		final ExtendedList<String> Ebuttons = new ExtendedList<String>();

		JMenuItem grammar = new JMenuItem(LangResource.new_gram);
		grammar.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String fileName = JOptionPane.showInputDialog("New Gramma Graph File name?");
				if (fileName != null && !fileName.equals(""))
				{
					try
					{
						Context.createFile(fileName, new FileNames(FileNames.GRAM_EXTENSION));
						Tree.reload(Context.getProject().getProjectsRootPath());
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});

		JMenuItem save = new JMenuItem(LangResource.save);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		save.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Context.saveFile(context);
			}
		});

		JMenuItem saveAll = new JMenuItem(LangResource.save_all);
		saveAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
		saveAll.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Context.saveAllFiles();
			}
		});

		JMenuItem print = new JMenuItem(LangResource.print + DOTS);
		print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		print.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Context.print(context);
			}
		});

		JMenu exportAs = new JMenu("Export As");
		JMenuItem png = new JMenuItem("PNG File" + DOTS);
		png.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, ActionEvent.CTRL_MASK));

		JMenuItem ebnf = new JMenuItem("Extended BNF" + DOTS);
		ebnf.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, ActionEvent.CTRL_MASK));

		exportAs.add(png);
		exportAs.add(ebnf);

		png.setEnabled(model.pngExport);
		ebnf.setEnabled(model.ebnfExport);
		if (!model.pngExport && !model.ebnfExport)
		{
			exportAs.setEnabled(false);
		}
		save.setEnabled(model.save);
		print.setEnabled(model.print);
		JMenuItem quit = new JMenuItem("Quit");
		quit.setEnabled(true);

		quit.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Context.exit();
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
		JMenu helpMenu = new JMenu(HELP);
		helpMenu.setEnabled(false);
		return helpMenu;
	}

	private JMenu createOptionsMenu()
	{
		JMenu optionsMenu = new JMenu(OPTIONS);
		optionsMenu.setEnabled(false);
		return optionsMenu;
	}

	private JMenu createProjectMenu()
	{
		JMenu projectMenu = new JMenu(PROJECT);
		projectMenu.setEnabled(false);
		return projectMenu;
	}

	private JMenu createWindowMenu()
	{
		JMenu windowMenu = new JMenu(WINDOW);
		JMenu theme = new JMenu("Theme");
		JMenuItem themeBlueHighlight = new JMenuItem("Blue Highlight");
		themeBlueHighlight.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Context.getMainWindow().changeTheme(Theme.BlueHighlightDockingTheme);
			}
		});
		theme.add(themeBlueHighlight);

		JMenuItem themeClassic = new JMenuItem("Classic");
		themeClassic.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Context.getMainWindow().changeTheme(Theme.ClassicDockingTheme);
			}
		});
		theme.add(themeClassic);

		JMenuItem themeDefault = new JMenuItem("Default");
		themeDefault.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Context.getMainWindow().changeTheme(Theme.DefaultDockingTheme);
			}
		});
		theme.add(themeDefault);

		JMenuItem themeGradient = new JMenuItem("Gradient");
		themeGradient.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Context.getMainWindow().changeTheme(Theme.GradientDockingTheme);
			}
		});
		theme.add(themeGradient);

		JMenuItem themeLookAndFeel = new JMenuItem("Look And Feel");
		themeLookAndFeel.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Context.getMainWindow().changeTheme(Theme.LookAndFeelDockingTheme);
			}
		});
		theme.add(themeLookAndFeel);

		JMenuItem themeShapedGradient = new JMenuItem("Shaped Gradient");
		themeShapedGradient.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Context.getMainWindow().changeTheme(Theme.ShapedGradientDockingTheme);
			}
		});
		theme.add(themeShapedGradient);

		JMenuItem themeSlimFlatt = new JMenuItem("Slim Flat");
		themeSlimFlatt.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Context.getMainWindow().changeTheme(Theme.SlimFlatDockingTheme);
			}
		});
		theme.add(themeSlimFlatt);

		JMenuItem themeSoftBlueIce = new JMenuItem("Soft BlueIce");
		themeSoftBlueIce.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Context.getMainWindow().changeTheme(Theme.SoftBlueIceDockingTheme);
			}
		});
		theme.add(themeSoftBlueIce);
		windowMenu.add(theme);
		return windowMenu;
	}

	public void build()
	{
		for(String menu : menus.getAll())		
		{
			if (menu.equals(EDIT))
			{
				this.add(createEditMenu());
			}
			else if (menu.equals(FILE))
			{
				this.add(createFileMenu());
			}
			else if (menu.equals(OPTIONS))
			{
				this.add(createOptionsMenu());
			}
			else if (menu.equals(PROJECT))
			{
				this.add(createProjectMenu());
			}
			else if (menu.equals(HELP))
			{
				this.add(createHelpMenu());
			}
			else if (menu.equals(WINDOW))
			{
				this.add(createWindowMenu());
			}
		}
	}
}

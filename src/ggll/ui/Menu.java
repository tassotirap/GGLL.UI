package ggll.ui;

import ggll.canvas.AbstractCanvas;
import ggll.project.GGLLManager;
import ggll.resource.LangResource;
import ggll.ui.ThemeManager.Theme;
import ggll.ui.interfaces.IMainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
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
	Object context;
	int contextDesc;
	ArrayList<String> menus;
	MenuModel model;

	IMainWindow window;

	public Menu(String[] menus, IMainWindow window, Object context, MenuModel model)
	{
		this.window = window;
		this.menus = new ArrayList<String>();
		this.context = context;
		this.model = model;
		if (context instanceof AbstractCanvas)
		{
			contextDesc = CANVAS_CONTEXT;
		}
		else if (context instanceof JTextArea)
		{
			contextDesc = TEXTAREA_CONTEXT;
		}
		else
		{
			contextDesc = DEFAULT_CONTEXT;
		}
		for (String m : menus)
		{
			this.menus.add(m);
		}
	}

	private JMenu createEditMenu()
	{
		JMenu edit = new JMenu("Edit");
		final ArrayList<String> Ebuttons = new ArrayList<String>();
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
		Ebuttons.add(undo.getText());
		Ebuttons.add(redo.getText());
		Ebuttons.add(copy.getText());
		Ebuttons.add(cut.getText());
		Ebuttons.add(paste.getText());
		Ebuttons.add(zoomIn.getText());
		Ebuttons.add(zoomOut.getText());

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
		final ArrayList<String> PMbuttons = new ArrayList<String>();
		final ArrayList<String> Ebuttons = new ArrayList<String>();

		JMenuItem save = new JMenuItem(LangResource.save);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		JMenuItem saveAll = new JMenuItem(LangResource.save_all);
		saveAll.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				GGLLManager.saveAllFiles();
			}
		});

		saveAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));

		save.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				GGLLManager.saveFile(context);
			}
		});

		JMenuItem print = new JMenuItem(LangResource.print + DOTS);

		print.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				GGLLManager.print(context);
			}
		});

		print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
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
				GGLLManager.exit();
			}
		});

		PMbuttons.add(saveAll.getText());
		PMbuttons.add(quit.getText());
		Ebuttons.add(save.getText());
		Ebuttons.add(print.getText());
		Ebuttons.add(png.getText());
		Ebuttons.add(ebnf.getText());

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
				GGLLManager.getMainWindow().changeTheme(Theme.BlueHighlightDockingTheme);
			}
		});
		theme.add(themeBlueHighlight);

		JMenuItem themeClassic = new JMenuItem("Classic");
		themeClassic.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				GGLLManager.getMainWindow().changeTheme(Theme.ClassicDockingTheme);
			}
		});
		theme.add(themeClassic);

		JMenuItem themeDefault = new JMenuItem("Default");
		themeDefault.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				GGLLManager.getMainWindow().changeTheme(Theme.DefaultDockingTheme);
			}
		});
		theme.add(themeDefault);

		JMenuItem themeGradient = new JMenuItem("Gradient");
		themeGradient.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				GGLLManager.getMainWindow().changeTheme(Theme.GradientDockingTheme);
			}
		});
		theme.add(themeGradient);

		JMenuItem themeLookAndFeel = new JMenuItem("Look And Feel");
		themeLookAndFeel.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				GGLLManager.getMainWindow().changeTheme(Theme.LookAndFeelDockingTheme);
			}
		});
		theme.add(themeLookAndFeel);

		JMenuItem themeShapedGradient = new JMenuItem("Shaped Gradient");
		themeShapedGradient.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				GGLLManager.getMainWindow().changeTheme(Theme.ShapedGradientDockingTheme);
			}
		});
		theme.add(themeShapedGradient);

		JMenuItem themeSlimFlatt = new JMenuItem("Slim Flat");
		themeSlimFlatt.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				GGLLManager.getMainWindow().changeTheme(Theme.SlimFlatDockingTheme);
			}
		});
		theme.add(themeSlimFlatt);

		JMenuItem themeSoftBlueIce = new JMenuItem("Soft BlueIce");
		themeSoftBlueIce.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				GGLLManager.getMainWindow().changeTheme(Theme.SoftBlueIceDockingTheme);
			}
		});
		theme.add(themeSoftBlueIce);
		windowMenu.add(theme);
		return windowMenu;
	}

	public void build()
	{
		for (int i = 0; i < menus.size(); i++)
		{
			String m = menus.get(i);
			if (m.equals(EDIT))
			{
				this.add(createEditMenu());
			}
			else if (m.equals(FILE))
			{
				this.add(createFileMenu());
			}
			else if (m.equals(OPTIONS))
			{
				this.add(createOptionsMenu());
			}
			else if (m.equals(PROJECT))
			{
				this.add(createProjectMenu());
			}
			else if (m.equals(HELP))
			{
				this.add(createHelpMenu());
			}
			else if (m.equals(WINDOW))
			{
				this.add(createWindowMenu());
			}
		}
	}
}

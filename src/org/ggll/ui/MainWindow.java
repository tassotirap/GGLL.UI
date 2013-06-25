package org.ggll.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.ViewSerializer;
import net.infonode.docking.mouse.DockingWindowActionMouseButtonListener;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.MixedViewHandler;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;

import org.ggll.canvas.CanvasFactory;
import org.ggll.model.FileNames;
import org.ggll.model.ui.IconFactory;
import org.ggll.project.GGLLManager;
import org.ggll.ui.Menu.MenuModel;
import org.ggll.ui.TabWindowList.TabPlace;
import org.ggll.ui.ThemeManager.Theme;
import org.ggll.ui.component.AbstractComponent;
import org.ggll.ui.component.ComponentListener;
import org.ggll.ui.component.ComponetFactory;
import org.ggll.ui.component.EmptyComponent;
import org.ggll.ui.component.FileComponent;
import org.ggll.ui.component.GrammarComponent;
import org.ggll.ui.component.ParserComponent;
import org.ggll.ui.component.TextAreaComponent;
import org.ggll.ui.interfaces.IMainWindow;
import org.ggll.ui.menubar.MenuBarFactory;
import org.ggll.ui.toolbar.ToolBarFactory;
import org.ggll.view.GGLLView;
import org.ggll.view.ViewRepository;

/***
 * 
 * @author Tasso Tirapani Silva Pinto Refactory Pending
 */
public class MainWindow implements ComponentListener, IMainWindow
{
	private ViewMap perspectiveMap = new ViewMap();
	private RootWindowProperties rootWindowProperties;

	private GGLLView emptyDynamicView = null;
	private JMenuBar currentMenuBar;
	private JComponent currentToolBar;
	private TabWindowList tabWindow;

	private JFrame frame;

	private RootWindow rootWindow;
	private MenuBarFactory menuBarFactory;
	private ToolBarFactory toolBarFactory;
	private ViewRepository viewRepository;

	public MainWindow()
	{
	}

	private void createDefaultViews()
	{
		GGLLManager.setActiveScene(CanvasFactory.createCanvas(GGLLManager.getProject().getGrammarFile()));
		viewRepository.createDefaultViews(perspectiveMap);
	}

	private void createDynamicViewMenu(View view)
	{
		MenuModel model = new MenuModel();
		if (((GGLLView) view).getTitle().equals("Parser"))
		{
			ParserComponent parserComponent = (ParserComponent) ((GGLLView) view).getComponentModel();
			addToolBar(toolBarFactory.createToolBar(parserComponent.getTextArea(), true, false), true, true);
			addMenuBar(menuBarFactory.createMenuBar(parserComponent.getTextArea(), model), true, true);
		}
		else
		{
			addToolBar(toolBarFactory.createToolBar(null, false, false), true, true);
			addMenuBar(menuBarFactory.createMenuBar(null, model), true, true);
		}
	}

	private void createMenuModel(String name, AbstractComponent component)
	{
		MenuModel model = new MenuModel();
		model.save = true;
		model.saveAs = true;
		model.saveAll = true;
		model.print = true;
		model.copy = true;
		model.cut = true;
		model.paste = true;
		model.undo = true;
		model.redo = true;
		model.find = true;
		if (name.endsWith(FileNames.GRAM_EXTENSION))
		{
			model.zoomIn = true;
			model.zoomOut = true;
			addToolBar(toolBarFactory.createToolBar(GGLLManager.getActiveScene(), true, true), false, false);
			addMenuBar(menuBarFactory.createMenuBar(GGLLManager.getActiveScene(), model), false, false);
		}
		else
		{
			addToolBar(toolBarFactory.createToolBar(component, true, false), false, false);
			addMenuBar(menuBarFactory.createMenuBar(component, model), false, false);
		}
	}

	private void createRootWindow()
	{

		MixedViewHandler handler = new MixedViewHandler(perspectiveMap, new ViewSerializer()
		{
			@Override
			public View readView(ObjectInputStream in) throws IOException
			{
				return viewRepository.getDynamicView(in.readInt());
			}

			@Override
			public void writeView(View view, ObjectOutputStream out) throws IOException
			{
				out.writeInt(((GGLLView) view).getId());
			}
		});

		rootWindowProperties.addSuperObject(ThemeManager.getCurrentTheme().getRootWindowProperties());
		rootWindow = DockingUtil.createRootWindow(perspectiveMap, handler, true);
		rootWindow.getRootWindowProperties().addSuperObject(rootWindowProperties);
		rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
		rootWindow.addListener(new WindowAdapter());
		rootWindow.addTabMouseButtonListener(DockingWindowActionMouseButtonListener.MIDDLE_BUTTON_CLOSE_LISTENER);
	}

	private void init()
	{
		init(DEFAULT_TITLE);
	}

	private void init(String title)
	{
		frame = new JFrame(title);
		frame.setName(DEFAULT_NAME);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		this.tabWindow = new TabWindowList();
		this.viewRepository = new ViewRepository();
	}

	private void openFiles()
	{
		List<File> filesToOpen = GGLLManager.getOpenedFiles();

		for (int i = 0; i < filesToOpen.size(); i++)
		{
			String name = filesToOpen.get(i).getName();
			AbstractComponent component = ComponetFactory.createFileComponent(name.substring(name.lastIndexOf(".")), filesToOpen.get(i).getAbsolutePath());

			if (component != null)
			{
				component.addComponentListener(this);
				IconFactory iconFactory = new IconFactory();
				Icon icon = iconFactory.getIcon(name);
				addComponent(component, name, filesToOpen.get(i).getAbsolutePath(), icon, TabPlace.CENTER_TABS);
				if (i == filesToOpen.size() - 1)
				{
					createMenuModel(name, component);
				}
			}
		}
	}

	private void setDefaultLayout()
	{
		for (int i = 0; i < TabWindowList.TAB_SIZE; i++)
		{
			TabWindow tabWindow = new TabWindow(viewRepository.getDefaultLayout().get(i).toArray());
			getTabWindowList().add(tabWindow);
			tabWindow.getTabWindowProperties().getCloseButtonProperties().setVisible(false);
		}
		rootWindow.setWindow(new SplitWindow(false, 0.75f, new SplitWindow(true, 0.8f, getTabWindowList().getCenterTab(), new SplitWindow(false, 0.5f, getTabWindowList().getRightTopTab(), getTabWindowList().getRightBottonTab())), new SplitWindow(true, 0.7f, getTabWindowList().getBottonLeftTab(), getTabWindowList().getBottonRightTab())));
	}

	private void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
		}
		catch (UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
	}

	protected void addMenuBar(JMenuBar menuBar, boolean replace, boolean repaint)
	{
		if (currentMenuBar != null)
		{
			frame.setMenuBar(null);
		}
		currentMenuBar = menuBar;
		frame.setJMenuBar(menuBar);
		if (repaint)
		{
			frame.validate();
			frame.repaint();
		}
	}

	protected void addToolBar(JComponent toolBar, boolean replace, boolean repaint)
	{
		if (replace && currentToolBar != null)
		{
			frame.getContentPane().remove(currentToolBar);
		}
		currentToolBar = toolBar;
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		if (repaint)
		{
			frame.validate();
			frame.repaint();
		}
	}

	protected void showFrame()
	{
		init();
		setLookAndFeel();
		this.menuBarFactory = new MenuBarFactory();
		this.toolBarFactory = new ToolBarFactory();
		this.rootWindowProperties = new RootWindowProperties();

		createRootWindow();
		createDefaultViews();
		setDefaultLayout();
		openFiles();


		frame.getContentPane().add(rootWindow, BorderLayout.CENTER);
		frame.setSize(900, 700);
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screenDim.width - frame.getWidth()) / 2, (screenDim.height - frame.getHeight()) / 2);
		frame.setVisible(true);
		frame.addWindowListener(new FrameAdapter());
	}

	public GGLLView addComponent(AbstractComponent componentModel, String title, String fileName, Icon icon, TabPlace place)
	{
		if (componentModel != null)
		{
			GGLLView view = new GGLLView(title, icon, componentModel, fileName, 2);
			if (componentModel instanceof GrammarComponent)
			{
				GGLLManager.setActiveScene(CanvasFactory.getCanvasFromFile(fileName));
			}

			componentModel.addComponentListener(this);
			getTabWindowList().getTabWindow(place).addTab(view);
			updateFocusedComponent(componentModel);

			return view;
		}
		return null;
	}

	@Override
	public void addEmptyDynamicView()
	{
		EmptyComponent emptyComponent = new EmptyComponent();
		emptyDynamicView = addComponent(emptyComponent, "Empty Page", null, VIEW_ICON, TabPlace.CENTER_TABS);
	}

	@Override
	public void changeTheme(Theme theme)
	{
		ThemeManager.changeTheme(rootWindowProperties, theme);
	}

	@Override
	public void ContentChanged(AbstractComponent source)
	{
		if (viewRepository.containsDynamicView(source))
		{
			GGLLView view = viewRepository.getDynamicView(source);
			if (!view.getTitle().startsWith(UNSAVED_PREFIX))
				view.getViewProperties().setTitle(UNSAVED_PREFIX + view.getTitle());
			GGLLManager.setUnsavedView(((FileComponent) source).getPath(), view);
		}

	}

	@Override
	public JFrame getFrame()
	{
		return frame;
	}

	@Override
	public TabWindowList getTabs()
	{
		return tabWindow;
	}

	@Override
	public TabWindowList getTabWindowList()
	{
		return tabWindow;
	}

	@Override
	public void removeEmptyDynamicView()
	{
		if (emptyDynamicView != null)
		{
			emptyDynamicView.close();
			emptyDynamicView = null;
		}
	}

	@Override
	public void setSaved(String path)
	{
		if (viewRepository.containsDynamicView(path))
		{
			GGLLView dynamicView = viewRepository.getDynamicView(path);

			if (GGLLManager.hasUnsavedView(dynamicView))
			{
				if (dynamicView.getTitle().startsWith(UNSAVED_PREFIX))
				{
					dynamicView.getViewProperties().setTitle(dynamicView.getTitle().replace(UNSAVED_PREFIX, ""));
				}
			}

			while (GGLLManager.hasUnsavedView(dynamicView))
			{
				GGLLManager.removeUnsavedView(path);
			}
		}
	}

	@Override
	public void updateFocusedComponent(AbstractComponent component)
	{
		if (component == null)
		{
			View view = rootWindow.getFocusedView();
			if (view instanceof GGLLView)
			{
				createDynamicViewMenu(view);
			}
			else
			{
				MenuModel model = new MenuModel();
				addToolBar(toolBarFactory.createToolBar(null, false, false), true, true);
				addMenuBar(menuBarFactory.createMenuBar(null, model), true, true);
			}
			return;
		}
		if (component instanceof FileComponent)
		{
			MenuModel model = new MenuModel();
			model.save = true;
			model.saveAs = true;
			model.saveAll = true;
			model.print = true;
			model.copy = true;
			model.cut = true;
			model.paste = true;
			model.undo = true;
			model.redo = true;
			model.find = true;
			if (component instanceof TextAreaComponent)
			{
				TextAreaComponent textArea = (TextAreaComponent) component;
				addToolBar(toolBarFactory.createToolBar(textArea, true, false), true, true);
				addMenuBar(menuBarFactory.createMenuBar(textArea, model), true, true);
			}
			else
			{
				if (component instanceof GrammarComponent)
				{
					model.zoomIn = true;
					model.zoomOut = true;
					addToolBar(toolBarFactory.createToolBar(GGLLManager.getActiveScene(), true, true), true, true);
					addMenuBar(menuBarFactory.createMenuBar(GGLLManager.getActiveScene(), model), true, true);
				}
			}
		}
	}

	@Override
	public void updateWindow(DockingWindow window, boolean added)
	{
		viewRepository.updateViews(window, added);
	}
}

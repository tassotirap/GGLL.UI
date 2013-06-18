package org.ggll.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.ViewSerializer;
import net.infonode.docking.WindowBar;
import net.infonode.docking.mouse.DockingWindowActionMouseButtonListener;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.MixedViewHandler;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;

import org.ggll.canvas.CanvasFactory;
import org.ggll.model.FileNames;
import org.ggll.model.ui.IconFactory;
import org.ggll.model.ui.IconFactory.IconType;
import org.ggll.parser.ParsingEditor;
import org.ggll.project.GGLLManager;
import org.ggll.ui.Menu.MenuModel;
import org.ggll.ui.TabWindowList.TabPlace;
import org.ggll.ui.ThemeManager.Theme;
import org.ggll.ui.component.AbstractComponent;
import org.ggll.ui.component.BadParameterException;
import org.ggll.ui.component.ComponentListener;
import org.ggll.ui.component.ComponetFactory;
import org.ggll.ui.component.EmptyComponent;
import org.ggll.ui.component.FileComponent;
import org.ggll.ui.component.GeneratedGrammarComponent;
import org.ggll.ui.component.GrammarComponent;
import org.ggll.ui.component.OutlineComponent;
import org.ggll.ui.component.OutputComponent;
import org.ggll.ui.component.ParserComponent;
import org.ggll.ui.component.ProjectsComponent;
import org.ggll.ui.component.SemanticStackComponent;
import org.ggll.ui.component.SyntaxStackComponent;
import org.ggll.ui.component.TextAreaRepo;
import org.ggll.ui.interfaces.IMainWindow;
import org.ggll.ui.menubar.MenuBarFactory;
import org.ggll.ui.toolbar.ToolBarFactory;
import org.ggll.view.GGLLView;
import org.ggll.view.ViewRepository;

/***
 * 
 * @author Tasso Tirapani Silva Pinto
 * Refactory Pending
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
	private ViewRepository dynamicaViewRepository;

	public MainWindow()
	{
	}

	private ArrayList<TabItem> createDefaultTabs() throws BadParameterException
	{
		IconFactory iconFactory = new IconFactory();
		ArrayList<TabItem> tabItems = new ArrayList<TabItem>();
		tabItems.add(new TabItem("Project", new ProjectsComponent().create(GGLLManager.getProject()), TabPlace.RIGHT_BOTTOM_TABS, iconFactory.getIcon(IconType.PROJECT_ICON)));
		tabItems.add(new TabItem("Outline", new OutlineComponent().create(GGLLManager.getActiveScene()), TabPlace.RIGHT_TOP_TABS, iconFactory.getIcon(IconType.OVERVIEW_CON)));
		tabItems.add(new TabItem("Grammar", new GeneratedGrammarComponent().create(GGLLManager.getActiveScene()), TabPlace.BOTTOM_LEFT_TABS, iconFactory.getIcon(IconType.GRAMMAR_ICON)));
		tabItems.add(new TabItem("Syntax Stack", new SyntaxStackComponent().create(GGLLManager.getActiveScene()), TabPlace.BOTTOM_LEFT_TABS, iconFactory.getIcon(IconType.SYNTACTIC_STACK_ICON)));
		tabItems.add(new TabItem("Sem. Stack", new SemanticStackComponent().create(GGLLManager.getActiveScene()), TabPlace.BOTTOM_LEFT_TABS, iconFactory.getIcon(IconType.SEMANTIC_STACK_ICON)));
		tabItems.add(new TabItem("Output", new OutputComponent().create(GGLLManager.getActiveScene()), TabPlace.BOTTOM_LEFT_TABS, iconFactory.getIcon(IconType.ACTIVE_OUTPUT_ICON)));
		tabItems.add(new TabItem("Parser", new ParserComponent().create(GGLLManager.getProject().getProjectsRootPath()), TabPlace.BOTTOM_RIGHT_TABS, iconFactory.getIcon(IconType.PARSER_ICON)));
		return tabItems;
	}

	private void createDefaultViews()
	{
		try
		{
			GGLLManager.setActiveScene(CanvasFactory.createCanvas(GGLLManager.getProject().getGrammarFile()));
			ArrayList<TabItem> tabItems = createDefaultTabs();
			dynamicaViewRepository.createDefaultViews(tabItems, perspectiveMap);
		}
		catch (BadParameterException e)
		{
			e.printStackTrace();
		}
	}

	private void createDynamicViewMenu(View view)
	{
		MenuModel model = new MenuModel();
		if (((GGLLView) view).getTitle().equals("Parser"))
		{
			JTextArea textArea = ParsingEditor.getInstance().getTextArea();
			addToolBar(toolBarFactory.createToolBar(textArea, true, false), true, true);
			addMenuBar(menuBarFactory.createMenuBar(textArea, model), true, true);
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
			addToolBar(toolBarFactory.createToolBar(TextAreaRepo.getTextArea(component), true, false), false, false);
			addMenuBar(menuBarFactory.createMenuBar(TextAreaRepo.getTextArea(component), model), false, false);
		}
	}

	private void createRootWindow()
	{

		MixedViewHandler handler = new MixedViewHandler(perspectiveMap, new ViewSerializer()
		{
			@Override
			public View readView(ObjectInputStream in) throws IOException
			{
				return dynamicaViewRepository.getDynamicView(in.readInt());
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
		this.dynamicaViewRepository = new ViewRepository();
	}

	private void openFiles() throws BadParameterException
	{
		List<File> filesToOpen = GGLLManager.getOpenedFiles();

		for (int i = 0; i < filesToOpen.size(); i++)
		{
			String name = filesToOpen.get(i).getName();
			AbstractComponent component = ComponetFactory.createFileComponent(name.substring(name.lastIndexOf(".")));

			if (component != null)
			{
				component.addComponentListener(this);
				IconFactory iconFactory = new IconFactory();
				Icon icon = iconFactory.getIcon(name);
				addComponent(component.create(filesToOpen.get(i).getAbsolutePath()), component, name, filesToOpen.get(i).getAbsolutePath(), icon, TabPlace.CENTER_TABS);
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
			if (dynamicaViewRepository.getDefaultLayout().size() > i)
			{
				TabWindow tabWindow = new TabWindow(dynamicaViewRepository.getDefaultLayout().get(i).toArray());
				getTabWindowList().add(tabWindow);
				getTabWindowList().getTabWindow(i).getTabWindowProperties().getCloseButtonProperties().setVisible(false);
			}
		}
		rootWindow.setWindow(new SplitWindow(false, 0.75f, new SplitWindow(true, 0.8f, getTabWindowList().getCenterTab(), new SplitWindow(false, 0.5f, getTabWindowList().getRightTopTab(), getTabWindowList().getRightBottonTab())), new SplitWindow(true, 0.7f, getTabWindowList().getBottonLeftTab(), getTabWindowList().getBottonRightTab())));

		WindowBar windowBar = rootWindow.getWindowBar(Direction.DOWN);

		while (windowBar.getChildWindowCount() > 0)
			windowBar.getChildWindow(0).close();
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
		try
		{
			openFiles();
		}
		catch (BadParameterException e)
		{
			e.printStackTrace();
		}
		
		frame.getContentPane().add(rootWindow, BorderLayout.CENTER);
		frame.setSize(900, 700);
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screenDim.width - frame.getWidth()) / 2, (screenDim.height - frame.getHeight()) / 2);
		frame.setVisible(true);
		frame.addWindowListener(new FrameAdapter());
	}

	public GGLLView addComponent(Component component, AbstractComponent componentModel, String title, String fileName, Icon icon, TabPlace place)
	{
		if (componentModel != null)
		{
			GGLLView view = new GGLLView(title, icon, component, componentModel, fileName, dynamicaViewRepository.getDynamicViewId());
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
	public void addEmptyDynamicView() throws BadParameterException
	{
		EmptyComponent emptyComponent = new EmptyComponent();
		emptyDynamicView = addComponent(emptyComponent.create(null), emptyComponent, "Empty Page", null, VIEW_ICON, TabPlace.CENTER_TABS);
	}

	@Override
	public void changeTheme(Theme theme)
	{
		ThemeManager.changeTheme(rootWindowProperties, theme);
	}

	@Override
	public void ContentChanged(AbstractComponent source, Object oldValue, Object newValue)
	{
		if (dynamicaViewRepository.containsDynamicView(source))
		{
			GGLLView view = dynamicaViewRepository.getDynamicView(source);
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
		if (dynamicaViewRepository.containsDynamicView(path))
		{
			GGLLView dynamicView = dynamicaViewRepository.getDynamicView(path);

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
			JTextArea textArea = TextAreaRepo.getTextArea(component);
			if (textArea != null)
			{
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
		dynamicaViewRepository.updateViews(window, added);
	}
}

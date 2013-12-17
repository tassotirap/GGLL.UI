package ggll.ui.window;

import ggll.ui.canvas.Canvas;
import ggll.ui.canvas.CanvasRepository;
import ggll.ui.canvas.OutlineTopComponent;
import ggll.ui.core.syntax.grammar.Grammar;
import ggll.ui.director.GGLLDirector;
import ggll.ui.file.GrammarFile;
import ggll.ui.icon.IconView;
import ggll.ui.output.GeneratedGrammar;
import ggll.ui.output.Output;
import ggll.ui.output.SemanticStack;
import ggll.ui.output.SyntaxStack;
import ggll.ui.tab.TabWindowList;
import ggll.ui.tab.TabWindowList.TabPlace;
import ggll.ui.view.AbstractView;
import ggll.ui.view.ViewRepository;
import ggll.ui.view.component.AbstractComponent;
import ggll.ui.view.component.AbstractFileComponent;
import ggll.ui.view.component.ComponentListener;
import ggll.ui.view.component.EmptyComponent;
import ggll.ui.view.component.GrammarComponent;
import ggll.ui.view.component.ParserComponent;
import ggll.ui.view.component.TextAreaComponent;
import ggll.ui.window.adapter.WindowAdapter;
import ggll.ui.window.adapter.WindowClosingAdapter;
import ggll.ui.window.menu.MenuFactory;
import ggll.ui.window.menu.MenuModel;
import ggll.ui.window.toolbar.ToolBarFactory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
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
import net.infonode.docking.theme.DefaultDockingTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.MixedViewHandler;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;

/***
 * 
 * @author Tasso Tirapani Silva Pinto
 */
public class MainWindow implements ComponentListener
{

	public final static String DEFAULT_NAME = "GGLL Window";
	public final static String DEFAULT_TITLE = "GGLL";
	public final static String UNSAVED_PREFIX = "* ";
	public final static Icon VIEW_ICON = new IconView();

	private ViewMap perspectiveMap = new ViewMap();
	private RootWindowProperties rootWindowProperties;

	private AbstractView emptyDynamicView = null;
	private JMenuBar currentMenuBar;
	private JComponent currentToolBar;
	private TabWindowList tabWindow;

	private JFrame frame;

	private RootWindow rootWindow;
	private MenuFactory menuBarFactory;
	private ToolBarFactory toolBarFactory;
	private ViewRepository viewRepository;

	public MainWindow()
	{
	}

	private void createDefaultViews()
	{
		GrammarFile grammarFile = GGLLDirector.getProject().getGrammarFile().get(0);
		GGLLDirector.setActiveScene(CanvasRepository.getInstance(grammarFile.getAbsolutePath()));
		viewRepository.createDefaultViews();
	}

	private void createDynamicViewMenu(View view)
	{
		MenuModel model = new MenuModel();
		if (((AbstractView) view).getTitle().equals("Parser"))
		{
			ParserComponent parserComponent = (ParserComponent) ((AbstractView) view).getComponentModel();
			addToolBar(toolBarFactory.createToolBar(parserComponent.getTextArea(), true, false), true, true);
			addMenuBar(menuBarFactory.createMenuBar(parserComponent.getTextArea(), model), true, true);
		}
		else
		{
			addToolBar(toolBarFactory.createToolBar(null, false, false), true, true);
			addMenuBar(menuBarFactory.createMenuBar(null, model), true, true);
		}
	}

	private void createRootWindow()
	{

		MixedViewHandler handler = new MixedViewHandler(perspectiveMap, new ViewSerializer()
		{
			@Override
			public View readView(ObjectInputStream in) throws IOException
			{
				return viewRepository.getView(in.readInt());
			}

			@Override
			public void writeView(View view, ObjectOutputStream out) throws IOException
			{
				out.writeInt(((AbstractView) view).getId());
			}
		});

		rootWindowProperties.addSuperObject(new DefaultDockingTheme().getRootWindowProperties());
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
		this.viewRepository = new ViewRepository(perspectiveMap);
	}

	private void openFiles()
	{
		List<File> filesToOpen = GGLLDirector.getOpenedFiles().getAll();
		for (int i = 0; i < filesToOpen.size(); i++)
		{
			GGLLDirector.openFile(filesToOpen.get(i).getAbsolutePath(), false);
		}
	}

	private void setDefaultLayout()
	{
		for (TabWindow tab : viewRepository.getTabWindowList().getAll())
		{
			tab.getTabWindowProperties().getCloseButtonProperties().setVisible(false);
			tabWindow.add(tab);
		}
		rootWindow.setWindow(new SplitWindow(false, 0.75f, new SplitWindow(true, 0.70f, getTabWindowList().getCenterLeftTab(), getTabWindowList().getCenterRightTab()), getTabWindowList().getBottonTab()));
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
		this.menuBarFactory = new MenuFactory();
		this.toolBarFactory = new ToolBarFactory();
		this.rootWindowProperties = new RootWindowProperties();

		createRootWindow();
		createDefaultViews();
		setDefaultLayout();
		openFiles();

		frame.getContentPane().add(rootWindow, BorderLayout.CENTER);
		frame.setSize(1024, 768);
		frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screenDim.width - frame.getWidth()) / 2, (screenDim.height - frame.getHeight()) / 2);
		frame.setVisible(true);
		frame.addWindowListener(new WindowClosingAdapter());
	}

	public AbstractView addComponent(AbstractComponent componentModel, String title, String fileName, Icon icon, TabPlace place)
	{
		if (componentModel != null)
		{
			AbstractView view = new AbstractView(title, icon, componentModel, fileName, 2);
			if (componentModel instanceof GrammarComponent)
			{
				GGLLDirector.setActiveScene(CanvasRepository.getInstance(fileName));
			}

			componentModel.addComponentListener(this);
			getTabWindowList().getTabWindow(place).addTab(view);
			updateFocusedComponent(componentModel);

			return view;
		}
		return null;
	}

	public void addEmptyDynamicView()
	{
		EmptyComponent emptyComponent = new EmptyComponent();
		emptyDynamicView = addComponent(emptyComponent, "Empty Page", null, VIEW_ICON, TabPlace.CENTER_LEFT_TABS);
	}

	public void ContentChanged(AbstractComponent source)
	{
		if (viewRepository.containsView(source))
		{
			AbstractView view = viewRepository.getView(source);
			if (!view.getTitle().startsWith(UNSAVED_PREFIX))
				view.getViewProperties().setTitle(UNSAVED_PREFIX + view.getTitle());
			GGLLDirector.setUnsavedView(((AbstractFileComponent) source).getPath(), view);
		}
	}

	public JFrame getFrame()
	{
		return frame;
	}

	public ViewMap getPerspectiveMap()
	{
		return perspectiveMap;
	}

	public TabWindowList getTabs()
	{
		return tabWindow;
	}

	public TabWindowList getTabWindowList()
	{
		return tabWindow;
	}

	public void removeEmptyDynamicView()
	{
		if (emptyDynamicView != null)
		{
			emptyDynamicView.close();
			emptyDynamicView = null;
		}
	}

	public void setSaved(String path)
	{
		if (viewRepository.containsView(path))
		{
			AbstractView dynamicView = viewRepository.getView(path);

			if (GGLLDirector.hasUnsavedView(dynamicView))
			{
				if (dynamicView.getTitle().startsWith(UNSAVED_PREFIX))
				{
					dynamicView.getViewProperties().setTitle(dynamicView.getTitle().replace(UNSAVED_PREFIX, ""));
				}
			}

			while (GGLLDirector.hasUnsavedView(dynamicView))
			{
				GGLLDirector.removeUnsavedView(path);
			}
		}
	}

	public void updateFocusedComponent(AbstractComponent component)
	{
		if (component == null)
		{
			View view = rootWindow.getFocusedView();
			if (view instanceof AbstractView)
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
		if (component instanceof AbstractFileComponent)
		{
			MenuModel model = new MenuModel();
			model.setSave(true);
			model.setSaveAs(true);
			model.setSaveAll(true);
			model.setPrint(true);
			model.setCopy(true);
			model.setCut(true);
			model.setPaste(true);
			model.setUndo(true);
			model.setRedo(true);
			model.setFind(true);
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
					GrammarComponent grammarComponent = (GrammarComponent) component;
					Canvas canvas = grammarComponent.getCanvas();
					
					GGLLDirector.setActiveScene(canvas);
					Output.getInstance().setActiveScene(canvas);
					SemanticStack.getInstance().setActiveScene(canvas);
					SyntaxStack.getInstance().setActiveScene(canvas);
					OutlineTopComponent.getInstance().setCanvas(canvas);
					GeneratedGrammar.getInstance().setActiveScene(canvas);					
					
					model.setZoomIn(true);
					model.setZoomOut(true);
					addToolBar(toolBarFactory.createToolBar(grammarComponent, true, true), true, true);
					addMenuBar(menuBarFactory.createMenuBar(grammarComponent, model), true, true);
				}
			}
		}
	}

	public void updateWindow(DockingWindow window, boolean added)
	{
		viewRepository.updateViews(window, added);
	}
}

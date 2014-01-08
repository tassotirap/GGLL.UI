package org.ggll.window;


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

import org.ggll.director.GGLLDirector;
import org.ggll.file.GrammarFile;
import org.ggll.images.IconView;
import org.ggll.output.GeneratedGrammar;
import org.ggll.output.Output;
import org.ggll.output.SemanticStack;
import org.ggll.output.SyntaxStack;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.SyntaxGraphRepository;
import org.ggll.syntax.graph.SyntaxTopComponent;
import org.ggll.window.adapter.WindowAdapter;
import org.ggll.window.adapter.WindowClosingAdapter;
import org.ggll.window.component.AbstractComponent;
import org.ggll.window.component.AbstractFileComponent;
import org.ggll.window.component.ComponentListener;
import org.ggll.window.component.EmptyComponent;
import org.ggll.window.component.GrammarComponent;
import org.ggll.window.component.ParserComponent;
import org.ggll.window.component.TextAreaComponent;
import org.ggll.window.menu.MenuFactory;
import org.ggll.window.menu.MenuModel;
import org.ggll.window.tab.TabWindowList;
import org.ggll.window.tab.TabWindowList.TabPlace;
import org.ggll.window.toolbar.ToolBarFactory;
import org.ggll.window.view.AbstractView;
import org.ggll.window.view.ViewRepository;

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

	private final ViewMap perspectiveMap = new ViewMap();
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
		final GrammarFile grammarFile = GGLLDirector.getProject().getGrammarFile().get(0);
		GGLLDirector.setActiveCanvas(SyntaxGraphRepository.getInstance(grammarFile.getAbsolutePath()));
		this.viewRepository.createDefaultViews();
	}

	private void createDynamicViewMenu(View view)
	{
		final MenuModel model = new MenuModel();
		if (((AbstractView) view).getTitle().equals("Parser"))
		{
			final ParserComponent parserComponent = (ParserComponent) ((AbstractView) view).getComponentModel();
			addToolBar(this.toolBarFactory.createToolBar(parserComponent.getTextArea(), true, false), true, true);
			addMenuBar(this.menuBarFactory.createMenuBar(parserComponent.getTextArea(), model), true, true);
		}
		else
		{
			addToolBar(this.toolBarFactory.createToolBar(null, false, false), true, true);
			addMenuBar(this.menuBarFactory.createMenuBar(null, model), true, true);
		}
	}

	private void createRootWindow()
	{

		final MixedViewHandler handler = new MixedViewHandler(this.perspectiveMap, new ViewSerializer()
		{
			@Override
			public View readView(ObjectInputStream in) throws IOException
			{
				return MainWindow.this.viewRepository.getView(in.readInt());
			}

			@Override
			public void writeView(View view, ObjectOutputStream out) throws IOException
			{
				out.writeInt(((AbstractView) view).getId());
			}
		});

		this.rootWindowProperties.addSuperObject(new DefaultDockingTheme().getRootWindowProperties());
		this.rootWindow = DockingUtil.createRootWindow(this.perspectiveMap, handler, true);
		this.rootWindow.getRootWindowProperties().addSuperObject(this.rootWindowProperties);
		this.rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
		this.rootWindow.addListener(new WindowAdapter());
		this.rootWindow.addTabMouseButtonListener(DockingWindowActionMouseButtonListener.MIDDLE_BUTTON_CLOSE_LISTENER);
	}

	private void init()
	{
		init(DEFAULT_TITLE);
	}

	private void init(String title)
	{
		this.frame = new JFrame(title);
		this.frame.setName(DEFAULT_NAME);
		this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		this.tabWindow = new TabWindowList();
		this.viewRepository = new ViewRepository(this.perspectiveMap);
	}

	private void openFiles()
	{
		final List<File> filesToOpen = GGLLDirector.getOpenedFiles().getAll();
		for (int i = 0; i < filesToOpen.size(); i++)
		{
			GGLLDirector.openFile(filesToOpen.get(i).getAbsolutePath(), false);
		}
	}

	private void setDefaultLayout()
	{
		for (final TabWindow tab : this.viewRepository.getTabWindowList().getAll())
		{
			tab.getTabWindowProperties().getCloseButtonProperties().setVisible(false);
			this.tabWindow.add(tab);
		}
		SplitWindow mainRigth = new SplitWindow(false, 0.60f, getTabWindowList().getCenterRightTopTab(), getTabWindowList().getCenterRightBottomTab());
		SplitWindow mainCenter = new SplitWindow(true, 0.70f, getTabWindowList().getCenterLeftTab(), mainRigth);		
		SplitWindow mainSplit = new SplitWindow(false, 0.75f, mainCenter, getTabWindowList().getBottonTab());		
		this.rootWindow.setWindow(mainSplit);
	}

	private void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
		}
		catch (final UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
	}

	protected void addMenuBar(JMenuBar menuBar, boolean replace, boolean repaint)
	{
		if (this.currentMenuBar != null)
		{
			this.frame.setMenuBar(null);
		}
		this.currentMenuBar = menuBar;
		this.frame.setJMenuBar(menuBar);
		if (repaint)
		{
			this.frame.validate();
			this.frame.repaint();
		}
	}

	protected void addToolBar(JComponent toolBar, boolean replace, boolean repaint)
	{
		if (replace && this.currentToolBar != null)
		{
			this.frame.getContentPane().remove(this.currentToolBar);
		}
		this.currentToolBar = toolBar;
		this.frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		if (repaint)
		{
			this.frame.validate();
			this.frame.repaint();
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

		this.frame.getContentPane().add(this.rootWindow, BorderLayout.CENTER);
		this.frame.setSize(1024, 768);
		this.frame.setExtendedState(this.frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
		final Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame.setLocation((screenDim.width - this.frame.getWidth()) / 2, (screenDim.height - this.frame.getHeight()) / 2);
		this.frame.setVisible(true);
		this.frame.addWindowListener(new WindowClosingAdapter());
	}

	public AbstractView addComponent(AbstractComponent componentModel, String title, String fileName, Icon icon, TabPlace place)
	{
		if (componentModel != null)
		{
			final AbstractView view = new AbstractView(title, icon, componentModel, fileName, 2);
			if (componentModel instanceof GrammarComponent)
			{
				GGLLDirector.setActiveCanvas(SyntaxGraphRepository.getInstance(fileName));
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
		final EmptyComponent emptyComponent = new EmptyComponent();
		this.emptyDynamicView = addComponent(emptyComponent, "Empty Page", null, VIEW_ICON, TabPlace.CENTER_LEFT_TABS);
	}

	@Override
	public void ContentChanged(AbstractComponent source)
	{
		if (this.viewRepository.containsView(source))
		{
			final AbstractView view = this.viewRepository.getView(source);
			if (!view.getTitle().startsWith(UNSAVED_PREFIX))
			{
				view.getViewProperties().setTitle(UNSAVED_PREFIX + view.getTitle());
			}
			GGLLDirector.setUnsavedView(((AbstractFileComponent) source).getPath(), view);
		}
	}

	public JFrame getFrame()
	{
		return this.frame;
	}

	public ViewMap getPerspectiveMap()
	{
		return this.perspectiveMap;
	}

	public TabWindowList getTabs()
	{
		return this.tabWindow;
	}

	public TabWindowList getTabWindowList()
	{
		return this.tabWindow;
	}

	public void removeEmptyDynamicView()
	{
		if (this.emptyDynamicView != null)
		{
			this.emptyDynamicView.close();
			this.emptyDynamicView = null;
		}
	}

	public void setSaved(String path)
	{
		if (this.viewRepository.containsView(path))
		{
			final AbstractView dynamicView = this.viewRepository.getView(path);

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
			final View view = this.rootWindow.getFocusedView();
			if (view instanceof AbstractView)
			{
				createDynamicViewMenu(view);
			}
			else
			{
				final MenuModel model = new MenuModel();
				addToolBar(this.toolBarFactory.createToolBar(null, false, false), true, true);
				addMenuBar(this.menuBarFactory.createMenuBar(null, model), true, true);
			}
			return;
		}
		if (component instanceof AbstractFileComponent)
		{
			final MenuModel model = new MenuModel();
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
				final TextAreaComponent textArea = (TextAreaComponent) component;
				addToolBar(this.toolBarFactory.createToolBar(textArea, true, false), true, true);
				addMenuBar(this.menuBarFactory.createMenuBar(textArea, model), true, true);
			}
			else
			{
				if (component instanceof GrammarComponent)
				{
					final GrammarComponent grammarComponent = (GrammarComponent) component;
					final SyntaxGraph canvas = grammarComponent.getCanvas();

					GGLLDirector.setActiveCanvas(canvas);
					Output.getInstance().setActiveScene(canvas);
					SemanticStack.getInstance().setActiveScene(canvas);
					SyntaxStack.getInstance().setActiveScene(canvas);
					SyntaxTopComponent.getInstance().setCanvas(canvas);
					GeneratedGrammar.getInstance().setActiveScene(canvas);

					model.setZoomIn(true);
					model.setZoomOut(true);
					addToolBar(this.toolBarFactory.createToolBar(grammarComponent, true, true), true, true);
					addMenuBar(this.menuBarFactory.createMenuBar(grammarComponent, model), true, true);
				}
			}
		}
	}

	public void updateWindow(DockingWindow window, boolean added)
	{
		this.viewRepository.updateViews(window, added);
	}
}

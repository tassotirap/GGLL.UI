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

import org.ggll.facade.GGLLFacade;
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
	
	private static MainWindow instance;
	
	private MainWindow()
	{
	}	
	
	public static MainWindow getInstance()
	{
		if(instance == null)
		{
			instance = new MainWindow();
		}
		return instance;
	}
	
	private void createDefaultViews()
	{
		final GrammarFile grammarFile = GGLLFacade.getInstance().getGrammarFile().get(0);
		GGLLFacade.getInstance().setActiveSyntaxGraph(SyntaxGraphRepository.getInstance(grammarFile.getAbsolutePath()));
		viewRepository.createDefaultViews();
	}
	
	private void createDynamicViewMenu(final View view)
	{
		final MenuModel model = new MenuModel();
		if (((AbstractView) view).getTitle().equals("Parser"))
		{
			final ParserComponent parserComponent = (ParserComponent) ((AbstractView) view).getComponentModel();
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
		
		final MixedViewHandler handler = new MixedViewHandler(perspectiveMap, new ViewSerializer()
		{
			@Override
			public View readView(final ObjectInputStream in) throws IOException
			{
				return viewRepository.getView(in.readInt());
			}
			
			@Override
			public void writeView(final View view, final ObjectOutputStream out) throws IOException
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
		init(MainWindow.DEFAULT_TITLE);
	}
	
	private void init(final String title)
	{
		frame = new JFrame(title);
		frame.setName(MainWindow.DEFAULT_NAME);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		tabWindow = new TabWindowList();
		viewRepository = new ViewRepository(perspectiveMap);
	}
	
	private void openFiles()
	{
		final List<File> filesToOpen = GGLLFacade.getInstance().getOpenedFiles().getAll();
		for (int i = 0; i < filesToOpen.size(); i++)
		{
			GGLLFacade.getInstance().openFile(filesToOpen.get(i).getAbsolutePath(), false);
		}
	}
	
	private void setDefaultLayout()
	{
		for (final TabWindow tab : viewRepository.getTabWindowList().getAll())
		{
			tab.getTabWindowProperties().getCloseButtonProperties().setVisible(false);
			tabWindow.add(tab);
		}
		final SplitWindow mainRigth = new SplitWindow(false, 0.60f, getTabWindowList().getCenterRightTopTab(), getTabWindowList().getCenterRightBottomTab());
		final SplitWindow mainCenter = new SplitWindow(true, 0.70f, getTabWindowList().getCenterLeftTab(), mainRigth);
		final SplitWindow mainSplit = new SplitWindow(false, 0.75f, mainCenter, getTabWindowList().getBottonTab());
		rootWindow.setWindow(mainSplit);
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
	
	protected void addMenuBar(final JMenuBar menuBar, final boolean replace, final boolean repaint)
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
	
	protected void addToolBar(final JComponent toolBar, final boolean replace, final boolean repaint)
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
		menuBarFactory = new MenuFactory();
		toolBarFactory = new ToolBarFactory();
		rootWindowProperties = new RootWindowProperties();
		
		createRootWindow();
		createDefaultViews();
		setDefaultLayout();
		openFiles();
		
		frame.getContentPane().add(rootWindow, BorderLayout.CENTER);
		frame.setSize(1024, 768);
		frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
		final Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screenDim.width - frame.getWidth()) / 2, (screenDim.height - frame.getHeight()) / 2);
		frame.setVisible(true);
		frame.addWindowListener(new WindowClosingAdapter());
	}
	
	public AbstractView addComponent(final AbstractComponent componentModel, final String title, final String fileName, final Icon icon, final TabPlace place)
	{
		if (componentModel != null)
		{
			final AbstractView view = new AbstractView(title, icon, componentModel, fileName, 2);
			if (componentModel instanceof GrammarComponent)
			{
				GGLLFacade.getInstance().setActiveSyntaxGraph(SyntaxGraphRepository.getInstance(fileName));
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
		emptyDynamicView = addComponent(emptyComponent, "Empty Page", null, MainWindow.VIEW_ICON, TabPlace.CENTER_LEFT_TABS);
	}
	
	@Override
	public void ContentChanged(final AbstractComponent source)
	{
		if (viewRepository.containsView(source))
		{
			final AbstractView view = viewRepository.getView(source);
			if (!view.getTitle().startsWith(MainWindow.UNSAVED_PREFIX))
			{
				view.getViewProperties().setTitle(MainWindow.UNSAVED_PREFIX + view.getTitle());
			}
			GGLLFacade.getInstance().setUnsavedView(((AbstractFileComponent) source).getPath(), view);
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
	
	public void setSaved(final String path)
	{
		if (viewRepository.containsView(path))
		{
			final AbstractView dynamicView = viewRepository.getView(path);
			
			if (GGLLFacade.getInstance().hasUnsavedView(dynamicView))
			{
				if (dynamicView.getTitle().startsWith(MainWindow.UNSAVED_PREFIX))
				{
					dynamicView.getViewProperties().setTitle(dynamicView.getTitle().replace(MainWindow.UNSAVED_PREFIX, ""));
				}
			}
			
			while (GGLLFacade.getInstance().hasUnsavedView(dynamicView))
			{
				GGLLFacade.getInstance().removeUnsavedView(path);
			}
		}
	}
	
	public void updateFocusedComponent(final AbstractComponent component)
	{
		if (component == null)
		{
			final View view = rootWindow.getFocusedView();
			if (view instanceof AbstractView)
			{
				createDynamicViewMenu(view);
			}
			else
			{
				final MenuModel model = new MenuModel();
				addToolBar(toolBarFactory.createToolBar(null, false, false), true, true);
				addMenuBar(menuBarFactory.createMenuBar(null, model), true, true);
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
				addToolBar(toolBarFactory.createToolBar(textArea, true, false), true, true);
				addMenuBar(menuBarFactory.createMenuBar(textArea, model), true, true);
			}
			else
			{
				if (component instanceof GrammarComponent)
				{
					final GrammarComponent grammarComponent = (GrammarComponent) component;
					final SyntaxGraph canvas = grammarComponent.getCanvas();
					
					GGLLFacade.getInstance().setActiveSyntaxGraph(canvas);
					Output.getInstance().setActiveScene(canvas);
					SemanticStack.getInstance().setActiveScene(canvas);
					SyntaxStack.getInstance().setActiveScene(canvas);
					SyntaxTopComponent.getInstance().setCanvas(canvas);
					GeneratedGrammar.getInstance().setActiveScene(canvas);
					
					model.setZoomIn(true);
					model.setZoomOut(true);
					addToolBar(toolBarFactory.createToolBar(grammarComponent, true, true), true, true);
					addMenuBar(menuBarFactory.createMenuBar(grammarComponent, model), true, true);
				}
			}
		}
	}
	
	public void updateWindow(final DockingWindow window, final boolean added)
	{
		viewRepository.updateViews(window, added);
	}
}

package ggll.ui.view;

import ggll.core.list.ExtendedList;
import ggll.ui.director.GGLLDirector;
import ggll.ui.icon.IconFactory;
import ggll.ui.icon.IconFactory.IconType;
import ggll.ui.tab.TabWindowList.TabPlace;
import ggll.ui.view.component.AbstractComponent;
import ggll.ui.view.component.GeneratedGrammarComponent;
import ggll.ui.view.component.OutlineComponent;
import ggll.ui.view.component.OutputComponent;
import ggll.ui.view.component.ParserComponent;
import ggll.ui.view.component.ProjectsComponent;
import ggll.ui.view.component.SemanticStackComponent;
import ggll.ui.view.component.SyntaxStackComponent;
import net.infonode.docking.DockingWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.util.ViewMap;

public class ViewRepository
{
	private static int DEFAULT_LAYOUT = 4;

	private ExtendedList<TabWindow> tabWindowList;
	private ExtendedList<AbstractView> views;
	private IconFactory iconFactory;
	private ViewMap viewMap;

	public ViewRepository(ViewMap viewMap)
	{
		this.tabWindowList = new ExtendedList<TabWindow>();
		this.views = new ExtendedList<AbstractView>();
		this.iconFactory = new IconFactory();
		this.viewMap = viewMap;
	}

	private void addView(AbstractView view)
	{
		views.append(view);
	}

	private void createFilesView()
	{
		AbstractView abstractView = new AbstractView("Files", iconFactory.getIcon(IconType.PROJECT_ICON), new ProjectsComponent(GGLLDirector.getProject()), getNextViewId());
		tabWindowList.get(TabPlace.CENTER_RIGHT_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(0, abstractView);
	}

	private void createGrammarView()
	{
		AbstractView abstractView = new AbstractView("Grammar", iconFactory.getIcon(IconType.GRAMMAR_ICON), new GeneratedGrammarComponent(GGLLDirector.getActiveScene()), getNextViewId());
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(2, abstractView);
	}

	private void createOutputView()
	{
		AbstractView abstractView = new AbstractView("Output", iconFactory.getIcon(IconType.ACTIVE_OUTPUT_ICON), new OutputComponent(GGLLDirector.getActiveScene()), getNextViewId());
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(5, abstractView);
	}

	private void createParserView()
	{
		AbstractView abstractView = new AbstractView("Parser", iconFactory.getIcon(IconType.PARSER_ICON), new ParserComponent(GGLLDirector.getProject().getProjectsRootPath()), getNextViewId());
		tabWindowList.get(TabPlace.CENTER_RIGHT_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(6, abstractView);
	}

	private void createSemanticStackView()
	{
		AbstractView abstractView = new AbstractView("Semantic Stack", iconFactory.getIcon(IconType.SEMANTIC_STACK_ICON), new SemanticStackComponent(GGLLDirector.getActiveScene()), getNextViewId());
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(4, abstractView);
	}

	private void createSyntaxStackView()
	{
		AbstractView abstractView = new AbstractView("Syntax Stack", iconFactory.getIcon(IconType.SYNTACTIC_STACK_ICON), new SyntaxStackComponent(GGLLDirector.getActiveScene()), getNextViewId());
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(3, abstractView);
	}

	private void createViewView()
	{
		AbstractView abstractView = new AbstractView("View", iconFactory.getIcon(IconType.OVERVIEW_CON), new OutlineComponent(GGLLDirector.getActiveScene()), getNextViewId());
		tabWindowList.get(TabPlace.CENTER_RIGHT_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(1, abstractView);
	}

	private void removeView(AbstractView abstractView)
	{
		views.remove(abstractView);
	}

	private void updateChildViews(DockingWindow window, boolean added)
	{
		for (int i = 0; i < window.getChildWindowCount(); i++)
		{
			updateViews(window.getChildWindow(i), added);
		}
	}

	public boolean containsView(AbstractComponent component)
	{
		for (AbstractView abstractView : views.getAll())
		{
			if (abstractView.getComponentModel().equals(component))
			{
				return true;
			}
		}
		return false;
	}

	public boolean containsView(int id)
	{
		for (AbstractView ggllView : views.getAll())
		{
			if (ggllView.getId() == id)
			{
				return true;
			}
		}
		return false;
	}

	public boolean containsView(String path)
	{
		for (AbstractView ggllView : views.getAll())
		{
			if (ggllView.getFileName().equals(path))
			{
				return true;
			}
		}
		return false;
	}

	public void createDefaultViews()
	{
		for (int i = 0; i < DEFAULT_LAYOUT; i++)
			tabWindowList.append(new TabWindow());

		createParserView();
		createFilesView();
		createViewView();
		tabWindowList.get(TabPlace.CENTER_RIGHT_TABS.ordinal()).setSelectedTab(0);

		createOutputView();
		createGrammarView();
		createSyntaxStackView();
		createSemanticStackView();
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).setSelectedTab(0);
	}

	public int getNextViewId()
	{
		int max = 0;
		for (AbstractView ggllView : views.getAll())
		{
			if (ggllView.getId() > max)
			{
				max = ggllView.getId();
			}
		}
		return max + 1;
	}

	public ExtendedList<TabWindow> getTabWindowList()
	{
		return tabWindowList;
	}

	public AbstractView getView(AbstractComponent component)
	{
		for (AbstractView ggllView : views.getAll())
		{
			if (ggllView.getComponentModel().equals(component))
			{
				return ggllView;
			}
		}
		return null;
	}

	public AbstractView getView(int id)
	{
		for (AbstractView abstractView : views.getAll())
		{
			if (abstractView.getId() == id)
			{
				return abstractView;
			}
		}
		return null;
	}

	public AbstractView getView(String path)
	{
		for (AbstractView abstractView : views.getAll())
		{
			if (abstractView.getFileName().equals(path))
			{
				return abstractView;
			}
		}
		return null;
	}

	public void updateViews(DockingWindow window, boolean added)
	{
		if (window instanceof AbstractView)
		{
			AbstractView abstractView = (AbstractView) window;
			if (added)
			{
				addView(abstractView);
			}
			else
			{
				removeView(abstractView);
			}
		}
		else
		{
			updateChildViews(window, added);
		}
	}
}

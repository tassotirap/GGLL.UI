package ggll.ui.view;

import ggll.ui.component.AbstractComponent;
import ggll.ui.component.GeneratedGrammarComponent;
import ggll.ui.component.OutlineComponent;
import ggll.ui.component.OutputComponent;
import ggll.ui.component.ParserComponent;
import ggll.ui.component.ProjectsComponent;
import ggll.ui.component.SemanticStackComponent;
import ggll.ui.component.SyntaxStackComponent;
import ggll.ui.icon.IconFactory;
import ggll.ui.icon.IconFactory.IconType;
import ggll.ui.project.Context;
import ggll.ui.tab.TabWindowList.TabPlace;

import java.util.ArrayList;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.util.ViewMap;

public class ViewRepository
{
	private static int DEFAULT_LAYOUT = 4;
	
	private ArrayList<TabWindow> tabWindowList;	
	private ArrayList<AbstractView> views;
	private IconFactory iconFactory;
	private ViewMap viewMap;

	public ViewRepository(ViewMap viewMap)
	{
		this.tabWindowList = new ArrayList<TabWindow>();
		this.views = new ArrayList<AbstractView>();
		this.iconFactory = new IconFactory();
		this.viewMap = viewMap;
	}

	private void addView(AbstractView view)
	{
		views.add(view);
	}

	private void createGrammarView()
	{
		AbstractView abstractView = new AbstractView("Grammar", iconFactory.getIcon(IconType.GRAMMAR_ICON), new GeneratedGrammarComponent(Context.getActiveScene()), getNextViewId());
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(2, abstractView);
	}

	private void createViewView()
	{
		AbstractView abstractView = new AbstractView("View", iconFactory.getIcon(IconType.OVERVIEW_CON), new OutlineComponent(Context.getActiveScene()), getNextViewId());
		tabWindowList.get(TabPlace.CENTER_RIGHT_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(1, abstractView);
	}

	private void createOutputView()
	{
		AbstractView abstractView = new AbstractView("Output", iconFactory.getIcon(IconType.ACTIVE_OUTPUT_ICON), new OutputComponent(Context.getActiveScene()), getNextViewId());
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(5, abstractView);
	}

	private void createParserView()
	{
		AbstractView abstractView = new AbstractView("Parser", iconFactory.getIcon(IconType.PARSER_ICON), new ParserComponent(Context.getProject().getProjectsRootPath()), getNextViewId());
		tabWindowList.get(TabPlace.CENTER_RIGHT_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(6, abstractView);
	}

	private void createFilesView()
	{
		AbstractView abstractView = new AbstractView("Files", iconFactory.getIcon(IconType.PROJECT_ICON), new ProjectsComponent(Context.getProject()), getNextViewId());
		tabWindowList.get(TabPlace.CENTER_RIGHT_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(0, abstractView);
	}

	private void createSemanticStackView()
	{
		AbstractView abstractView = new AbstractView("Semantic Stack", iconFactory.getIcon(IconType.SEMANTIC_STACK_ICON), new SemanticStackComponent(Context.getActiveScene()), getNextViewId());
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(4, abstractView);
	}

	private void createSyntaxStackView()
	{
		AbstractView abstractView = new AbstractView("Syntax Stack", iconFactory.getIcon(IconType.SYNTACTIC_STACK_ICON), new SyntaxStackComponent(Context.getActiveScene()), getNextViewId());
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(3, abstractView);
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
		for (AbstractView abstractView : views)
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
		for (AbstractView ggllView : views)
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
		for (AbstractView ggllView : views)
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
			tabWindowList.add(new TabWindow());

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
	

	public ArrayList<TabWindow> getTabWindowList()
	{
		return tabWindowList;
	}

	public int getNextViewId()
	{
		int max = 0;
		for (AbstractView ggllView : views)
		{
			if (ggllView.getId() > max)
			{
				max = ggllView.getId();
			}
		}
		return max + 1;
	}

	public AbstractView getView(AbstractComponent component)
	{
		for (AbstractView ggllView : views)
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
		for (AbstractView abstractView : views)
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
		for (AbstractView abstractView : views)
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

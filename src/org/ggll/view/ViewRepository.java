package org.ggll.view;

import org.ggll.director.GGLLDirector;
import org.ggll.icon.IconFactory;
import org.ggll.icon.IconFactory.IconType;
import org.ggll.view.component.AbstractComponent;
import org.ggll.view.component.GeneratedGrammarComponent;
import org.ggll.view.component.OutlineComponent;
import org.ggll.view.component.OutputComponent;
import org.ggll.view.component.ParserComponent;
import org.ggll.view.component.ProjectsComponent;
import org.ggll.view.component.SemanticStackComponent;
import org.ggll.view.component.SyntaxStackComponent;
import org.ggll.window.tab.TabWindowList.TabPlace;

import ggll.core.list.ExtendedList;
import net.infonode.docking.DockingWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.util.ViewMap;

public class ViewRepository
{
	private static int DEFAULT_LAYOUT = 5;

	private final ExtendedList<TabWindow> tabWindowList;
	private final ExtendedList<AbstractView> views;
	private final IconFactory iconFactory;
	private final ViewMap viewMap;

	public ViewRepository(ViewMap viewMap)
	{
		this.tabWindowList = new ExtendedList<TabWindow>();
		this.views = new ExtendedList<AbstractView>();
		this.iconFactory = new IconFactory();
		this.viewMap = viewMap;
	}

	private void addView(AbstractView view)
	{
		this.views.append(view);
	}

	private void createFilesView()
	{
		final AbstractView abstractView = new AbstractView("Files", this.iconFactory.getIcon(IconType.PROJECT_ICON), new ProjectsComponent(GGLLDirector.getProject()), getNextViewId());
		this.tabWindowList.get(TabPlace.CENTER_RIGHT_TOP_TABS.ordinal()).addTab(abstractView);
		this.viewMap.addView(0, abstractView);
	}

	private void createGrammarView()
	{
		final AbstractView abstractView = new AbstractView("Grammar", this.iconFactory.getIcon(IconType.GRAMMAR_ICON), new GeneratedGrammarComponent(GGLLDirector.getActiveCanvas()), getNextViewId());
		this.tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		this.viewMap.addView(2, abstractView);
	}

	private void createOutputView()
	{
		final AbstractView abstractView = new AbstractView("Output", this.iconFactory.getIcon(IconType.ACTIVE_OUTPUT_ICON), new OutputComponent(GGLLDirector.getActiveCanvas()), getNextViewId());
		this.tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		this.viewMap.addView(5, abstractView);
	}

	private void createParserView()
	{
		final AbstractView abstractView = new AbstractView("Parser", this.iconFactory.getIcon(IconType.PARSER_ICON), new ParserComponent(GGLLDirector.getProject().getProjectsRootPath()), getNextViewId());
		this.tabWindowList.get(TabPlace.CENTER_RIGHT_TOP_TABS.ordinal()).addTab(abstractView);
		this.viewMap.addView(6, abstractView);
	}

	private void createSemanticStackView()
	{
		final AbstractView abstractView = new AbstractView("Semantic Stack", this.iconFactory.getIcon(IconType.SEMANTIC_STACK_ICON), new SemanticStackComponent(GGLLDirector.getActiveCanvas()), getNextViewId());
		this.tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		this.viewMap.addView(4, abstractView);
	}

	private void createSyntaxStackView()
	{
		final AbstractView abstractView = new AbstractView("Syntax Stack", this.iconFactory.getIcon(IconType.SYNTACTIC_STACK_ICON), new SyntaxStackComponent(GGLLDirector.getActiveCanvas()), getNextViewId());
		this.tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		this.viewMap.addView(3, abstractView);
	}

	private void createViewView()
	{
		final AbstractView abstractView = new AbstractView("View", this.iconFactory.getIcon(IconType.OVERVIEW_CON), new OutlineComponent(GGLLDirector.getActiveCanvas()), getNextViewId());
		this.tabWindowList.get(TabPlace.CENTER_RIGHT_BOTTOM_TABS.ordinal()).addTab(abstractView);
		this.viewMap.addView(1, abstractView);
	}

	private void removeView(AbstractView abstractView)
	{
		this.views.remove(abstractView);
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
		for (final AbstractView abstractView : this.views.getAll())
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
		for (final AbstractView ggllView : this.views.getAll())
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
		for (final AbstractView ggllView : this.views.getAll())
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
		{
			this.tabWindowList.append(new TabWindow());
		}

		createParserView();
		createFilesView();
		createViewView();
		this.tabWindowList.get(TabPlace.CENTER_RIGHT_TOP_TABS.ordinal()).setSelectedTab(0);

		createOutputView();
		createGrammarView();
		createSyntaxStackView();
		createSemanticStackView();
		this.tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).setSelectedTab(0);
	}

	public int getNextViewId()
	{
		int max = 0;
		for (final AbstractView ggllView : this.views.getAll())
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
		return this.tabWindowList;
	}

	public AbstractView getView(AbstractComponent component)
	{
		for (final AbstractView ggllView : this.views.getAll())
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
		for (final AbstractView abstractView : this.views.getAll())
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
		for (final AbstractView abstractView : this.views.getAll())
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
			final AbstractView abstractView = (AbstractView) window;
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

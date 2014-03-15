package org.ggll.window.view;

import ggll.core.list.ExtendedList;
import net.infonode.docking.DockingWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.util.ViewMap;

import org.ggll.facade.GGLLFacade;
import org.ggll.images.IconFactory;
import org.ggll.images.IconFactory.IconType;
import org.ggll.window.component.AbstractComponent;
import org.ggll.window.component.GeneratedGrammarComponent;
import org.ggll.window.component.OutlineComponent;
import org.ggll.window.component.OutputComponent;
import org.ggll.window.component.ParserComponent;
import org.ggll.window.component.ProjectsComponent;
import org.ggll.window.component.SemanticStackComponent;
import org.ggll.window.component.SyntaxStackComponent;
import org.ggll.window.tab.TabWindowList.TabPlace;

public class ViewRepository
{
	private static int DEFAULT_LAYOUT = 5;
	
	private final ExtendedList<TabWindow> tabWindowList;
	private final ExtendedList<AbstractView> views;
	private final ViewMap viewMap;
	
	public ViewRepository(final ViewMap viewMap)
	{
		tabWindowList = new ExtendedList<TabWindow>();
		views = new ExtendedList<AbstractView>();
		this.viewMap = viewMap;
	}
	
	private void addView(final AbstractView view)
	{
		views.append(view);
	}
	
	public boolean containsView(final AbstractComponent component)
	{
		for (final AbstractView abstractView : views.getAll())
		{
			if (abstractView.getComponentModel().equals(component)) { return true; }
		}
		return false;
	}
	
	public boolean containsView(final int id)
	{
		for (final AbstractView ggllView : views.getAll())
		{
			if (ggllView.getId() == id) { return true; }
		}
		return false;
	}
	
	public boolean containsView(final String path)
	{
		for (final AbstractView ggllView : views.getAll())
		{
			if (ggllView.getFileName().equals(path)) { return true; }
		}
		return false;
	}
	
	public void createDefaultViews()
	{
		for (int i = 0; i < ViewRepository.DEFAULT_LAYOUT; i++)
		{
			final TabWindow tabWindow = new TabWindow();
			tabWindow.getWindowProperties().setUndockEnabled(false);
			tabWindowList.append(tabWindow);
		}
		
		createParserView();
		createFilesView();
		createViewView();
		tabWindowList.get(TabPlace.CENTER_RIGHT_TOP_TABS.ordinal()).setSelectedTab(0);
		
		createOutputView();
		createGrammarView();
		createSyntaxStackView();
		createSemanticStackView();
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).setSelectedTab(0);
	}
	
	private void createFilesView()
	{
		final AbstractView abstractView = new AbstractView("Files", IconFactory.getIcon(IconType.PROJECT_ICON), new ProjectsComponent(), getNextViewId());
		tabWindowList.get(TabPlace.CENTER_RIGHT_TOP_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(0, abstractView);
	}
	
	private void createGrammarView()
	{
		final AbstractView abstractView = new AbstractView("Grammar", IconFactory.getIcon(IconType.GRAMMAR_ICON), new GeneratedGrammarComponent(GGLLFacade.getInstance().getActiveSyntaxGraph()), getNextViewId());
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(2, abstractView);
	}
	
	private void createOutputView()
	{
		final AbstractView abstractView = new AbstractView("Output", IconFactory.getIcon(IconType.ACTIVE_OUTPUT_ICON), new OutputComponent(GGLLFacade.getInstance().getActiveSyntaxGraph()), getNextViewId());
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(5, abstractView);
	}
	
	private void createParserView()
	{
		final AbstractView abstractView = new AbstractView("Parser", IconFactory.getIcon(IconType.PARSER_ICON), new ParserComponent(GGLLFacade.getInstance().getProjectsRootPath()), getNextViewId());
		tabWindowList.get(TabPlace.CENTER_RIGHT_TOP_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(6, abstractView);
	}
	
	private void createSemanticStackView()
	{
		final AbstractView abstractView = new AbstractView("Semantic Stack", IconFactory.getIcon(IconType.SEMANTIC_STACK_ICON), new SemanticStackComponent(GGLLFacade.getInstance().getActiveSyntaxGraph()), getNextViewId());
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(4, abstractView);
	}
	
	private void createSyntaxStackView()
	{
		final AbstractView abstractView = new AbstractView("Syntax Stack", IconFactory.getIcon(IconType.SYNTACTIC_STACK_ICON), new SyntaxStackComponent(GGLLFacade.getInstance().getActiveSyntaxGraph()), getNextViewId());
		tabWindowList.get(TabPlace.BOTTOM_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(3, abstractView);
	}
	
	private void createViewView()
	{
		final AbstractView abstractView = new AbstractView("View", IconFactory.getIcon(IconType.OVERVIEW_CON), new OutlineComponent(GGLLFacade.getInstance().getActiveSyntaxGraph()), getNextViewId());
		tabWindowList.get(TabPlace.CENTER_RIGHT_BOTTOM_TABS.ordinal()).addTab(abstractView);
		viewMap.addView(1, abstractView);
	}
	
	public int getNextViewId()
	{
		int max = 0;
		for (final AbstractView ggllView : views.getAll())
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
	
	public AbstractView getView(final AbstractComponent component)
	{
		for (final AbstractView ggllView : views.getAll())
		{
			if (ggllView.getComponentModel().equals(component)) { return ggllView; }
		}
		return null;
	}
	
	public AbstractView getView(final int id)
	{
		for (final AbstractView abstractView : views.getAll())
		{
			if (abstractView.getId() == id) { return abstractView; }
		}
		return null;
	}
	
	public AbstractView getView(final String path)
	{
		for (final AbstractView abstractView : views.getAll())
		{
			if (abstractView.getFileName().equals(path)) { return abstractView; }
		}
		return null;
	}
	
	private void removeView(final AbstractView abstractView)
	{
		views.remove(abstractView);
	}
	
	private void updateChildViews(final DockingWindow window, final boolean added)
	{
		for (int i = 0; i < window.getChildWindowCount(); i++)
		{
			updateViews(window.getChildWindow(i), added);
		}
	}
	
	public void updateViews(final DockingWindow window, final boolean added)
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

package org.ggll.view;

import java.util.ArrayList;
import java.util.HashMap;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.View;
import net.infonode.docking.util.ViewMap;

import org.ggll.model.ui.IconFactory;
import org.ggll.model.ui.IconFactory.IconType;
import org.ggll.project.GGLLManager;
import org.ggll.ui.TabWindowList.TabPlace;
import org.ggll.ui.component.AbstractComponent;
import org.ggll.ui.component.GeneratedGrammarComponent;
import org.ggll.ui.component.OutlineComponent;
import org.ggll.ui.component.OutputComponent;
import org.ggll.ui.component.ParserComponent;
import org.ggll.ui.component.ProjectsComponent;
import org.ggll.ui.component.SemanticStackComponent;
import org.ggll.ui.component.SyntaxStackComponent;

public class ViewRepository
{
	private ArrayList<ViewList> defaultLayout;
	private static int DEFAULT_LAYOUT = 6;
	private HashMap<AbstractComponent, GGLLView> dynamicViewsByComponent = new HashMap<AbstractComponent, GGLLView>();
	private HashMap<Integer, GGLLView> dynamicViewsById = new HashMap<Integer, GGLLView>();
	private HashMap<String, GGLLView> dynamicViewsByPath = new HashMap<String, GGLLView>();
	
	public GGLLView projectsView;
	public GGLLView outlineView;
	public GGLLView grammarView;
	public GGLLView syntaxStackView;
	public GGLLView semStackView;
	public GGLLView outputView;
	public GGLLView parserView;

	public ViewRepository()
	{
		this.defaultLayout = new ArrayList<ViewList>();
	}

	private void addDynamicView(GGLLView dynamicView)
	{
		dynamicViewsById.put(new Integer(dynamicView.getId()), dynamicView);
		dynamicViewsByComponent.put(dynamicView.getComponentModel(), dynamicView);
		if (dynamicView.getFileName() != null)
		{
			dynamicViewsByPath.put(dynamicView.getFileName(), dynamicView);
		}
	}

	private void removeDynamicView(GGLLView dynamicView)
	{
		dynamicViewsById.remove(new Integer(dynamicView.getId()));
		dynamicViewsByComponent.remove(dynamicView.getComponentModel());
		if (dynamicViewsByPath.containsKey(dynamicView.getFileName()))
		{
			dynamicViewsByPath.remove(dynamicView.getFileName());
		}
	}

	private void updateChildDynamicViews(DockingWindow window, boolean added)
	{
		for (int i = 0; i < window.getChildWindowCount(); i++)
		{
			updateViews(window.getChildWindow(i), added);
		}
	}

	public boolean containsDynamicView(AbstractComponent component)
	{
		return dynamicViewsByComponent.containsKey(component);
	}

	public boolean containsDynamicView(int id)
	{
		return dynamicViewsById.containsKey(id);
	}

	public boolean containsDynamicView(String path)
	{
		return dynamicViewsByPath.containsKey(path);
	}

	public void createDefaultViews(ViewMap perspectiveMap)
	{
		try
		{
			IconFactory iconFactory = new IconFactory();
		
			for (int i = 0; i < DEFAULT_LAYOUT; i++)
				defaultLayout.add(new ViewList());
			
			projectsView = new GGLLView("Project", iconFactory.getIcon(IconType.PROJECT_ICON),new ProjectsComponent(GGLLManager.getProject()), getDynamicViewId());
			outlineView = new GGLLView("Outline", iconFactory.getIcon(IconType.OVERVIEW_CON),new OutlineComponent(GGLLManager.getActiveScene()), getDynamicViewId());
			grammarView = new GGLLView("Grammar", iconFactory.getIcon(IconType.GRAMMAR_ICON),new GeneratedGrammarComponent(GGLLManager.getActiveScene()), getDynamicViewId());
			syntaxStackView = new GGLLView("Syntax Stack", iconFactory.getIcon(IconType.SYNTACTIC_STACK_ICON),new SyntaxStackComponent(GGLLManager.getActiveScene()), getDynamicViewId());
			semStackView = new GGLLView("Sem. Stack", iconFactory.getIcon(IconType.SEMANTIC_STACK_ICON),new SemanticStackComponent(GGLLManager.getActiveScene()), getDynamicViewId());
			outputView = new GGLLView("Output", iconFactory.getIcon(IconType.ACTIVE_OUTPUT_ICON),new OutputComponent(GGLLManager.getActiveScene()), getDynamicViewId());
			parserView = new GGLLView("Parser", iconFactory.getIcon(IconType.PARSER_ICON),new ParserComponent(GGLLManager.getProject().getProjectsRootPath()), getDynamicViewId());
			
			defaultLayout.get(TabPlace.RIGHT_BOTTOM_TABS.ordinal()).add(projectsView);
			defaultLayout.get(TabPlace.RIGHT_TOP_TABS.ordinal()).add(outlineView);
			defaultLayout.get(TabPlace.BOTTOM_LEFT_TABS.ordinal()).add(grammarView);
			defaultLayout.get(TabPlace.BOTTOM_LEFT_TABS.ordinal()).add(syntaxStackView);
			defaultLayout.get(TabPlace.BOTTOM_LEFT_TABS.ordinal()).add(semStackView);
			defaultLayout.get(TabPlace.BOTTOM_LEFT_TABS.ordinal()).add(outputView);
			defaultLayout.get(TabPlace.BOTTOM_RIGHT_TABS.ordinal()).add(parserView);
			
			perspectiveMap.addView(0, projectsView);
			perspectiveMap.addView(1, outlineView);
			perspectiveMap.addView(2, grammarView);
			perspectiveMap.addView(3, syntaxStackView);
			perspectiveMap.addView(4, semStackView);
			perspectiveMap.addView(5, outputView);
			perspectiveMap.addView(6, parserView);
			
			updateViews(projectsView, true);
			updateViews(outlineView, true);
			updateViews(grammarView, true);
			updateViews(syntaxStackView, true);
			updateViews(semStackView, true);
			updateViews(parserView, true);
			updateViews(parserView, true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<ViewList> getDefaultLayout()
	{
		return defaultLayout;
	}

	public GGLLView getDynamicView(AbstractComponent component)
	{
		return dynamicViewsByComponent.get(component);
	}

	public GGLLView getDynamicView(int id)
	{
		return dynamicViewsById.get(id);
	}

	public GGLLView getDynamicView(String path)
	{
		return dynamicViewsByPath.get(path);
	}

	public int getDynamicViewId()
	{
		int id = 0;

		while (dynamicViewsById.containsKey(new Integer(id)))
			id++;

		return id;
	}

	public void updateViews(DockingWindow window, boolean added)
	{
		if (window instanceof View)
		{
			if (window instanceof GGLLView)
			{
				GGLLView dynamicView = (GGLLView) window;
				if (added)
				{
					addDynamicView(dynamicView);
				}
				else
				{
					removeDynamicView(dynamicView);
				}
			}
		}
		else
		{
			updateChildDynamicViews(window, added);
		}
	}
}

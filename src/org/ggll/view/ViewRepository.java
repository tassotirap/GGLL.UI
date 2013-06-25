package org.ggll.view;

import java.util.ArrayList;

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
	private ArrayList<GGLLView> ggllViews = new ArrayList<GGLLView>();

	public ViewRepository()
	{
		this.defaultLayout = new ArrayList<ViewList>();
	}

	private void addDynamicView(GGLLView ggllView)
	{
		ggllViews.add(ggllView);
	}

	public boolean containsDynamicView(AbstractComponent component)
	{
		for (GGLLView ggllView : ggllViews)
		{
			if (ggllView.getComponentModel().equals(component))
			{
				return true;
			}
		}
		return false;
	}

	public boolean containsDynamicView(int id)
	{
		for (GGLLView ggllView : ggllViews)
		{
			if (ggllView.getId() == id)
			{
				return true;
			}
		}
		return false;
	}

	public boolean containsDynamicView(String path)
	{
		for (GGLLView ggllView : ggllViews)
		{
			if (ggllView.getFileName().equals(path))
			{
				return true;
			}
		}
		return false;
	}

	public void createDefaultViews(ViewMap perspectiveMap)
	{
		IconFactory iconFactory = new IconFactory();

		for (int i = 0; i < DEFAULT_LAYOUT; i++)
			defaultLayout.add(new ViewList());

		createProjectView(iconFactory, perspectiveMap);
		createOutlineView(iconFactory, perspectiveMap);
		createGrammarView(iconFactory, perspectiveMap);
		createSyntaxStackView(iconFactory, perspectiveMap);
		createSemanticStackView(iconFactory, perspectiveMap);
		createOutputView(iconFactory, perspectiveMap);
		createParserView(iconFactory, perspectiveMap);
	}

	private void createGrammarView(IconFactory iconFactory, ViewMap perspectiveMap)
	{
		GGLLView gglView = new GGLLView("Grammar", iconFactory.getIcon(IconType.GRAMMAR_ICON), new GeneratedGrammarComponent(GGLLManager.getActiveScene()), getDynamicViewId());
		defaultLayout.get(TabPlace.BOTTOM_LEFT_TABS.ordinal()).add(gglView);
		perspectiveMap.addView(2, gglView);
	}

	private void createOutlineView(IconFactory iconFactory, ViewMap perspectiveMap)
	{
		GGLLView gglView = new GGLLView("Outline", iconFactory.getIcon(IconType.OVERVIEW_CON), new OutlineComponent(GGLLManager.getActiveScene()), getDynamicViewId());
		defaultLayout.get(TabPlace.RIGHT_TOP_TABS.ordinal()).add(gglView);
		perspectiveMap.addView(1, gglView);
	}

	private void createOutputView(IconFactory iconFactory, ViewMap perspectiveMap)
	{
		GGLLView gglView = new GGLLView("Output", iconFactory.getIcon(IconType.ACTIVE_OUTPUT_ICON), new OutputComponent(GGLLManager.getActiveScene()), getDynamicViewId());
		defaultLayout.get(TabPlace.BOTTOM_LEFT_TABS.ordinal()).add(gglView);
		perspectiveMap.addView(5, gglView);
	}

	private void createParserView(IconFactory iconFactory, ViewMap perspectiveMap)
	{
		GGLLView gglView = new GGLLView("Parser", iconFactory.getIcon(IconType.PARSER_ICON), new ParserComponent(GGLLManager.getProject().getProjectsRootPath()), getDynamicViewId());
		defaultLayout.get(TabPlace.BOTTOM_RIGHT_TABS.ordinal()).add(gglView);
		perspectiveMap.addView(6, gglView);
	}

	private void createProjectView(IconFactory iconFactory, ViewMap perspectiveMap)
	{
		GGLLView gglView = new GGLLView("Project", iconFactory.getIcon(IconType.PROJECT_ICON), new ProjectsComponent(GGLLManager.getProject()), getDynamicViewId());
		defaultLayout.get(TabPlace.RIGHT_BOTTOM_TABS.ordinal()).add(gglView);
		perspectiveMap.addView(0, gglView);
	}

	private void createSemanticStackView(IconFactory iconFactory, ViewMap perspectiveMap)
	{
		GGLLView gglView = new GGLLView("Sem. Stack", iconFactory.getIcon(IconType.SEMANTIC_STACK_ICON), new SemanticStackComponent(GGLLManager.getActiveScene()), getDynamicViewId());
		defaultLayout.get(TabPlace.BOTTOM_LEFT_TABS.ordinal()).add(gglView);
		perspectiveMap.addView(4, gglView);
	}

	private void createSyntaxStackView(IconFactory iconFactory, ViewMap perspectiveMap)
	{
		GGLLView gglView = new GGLLView("Syntax Stack", iconFactory.getIcon(IconType.SYNTACTIC_STACK_ICON), new SyntaxStackComponent(GGLLManager.getActiveScene()), getDynamicViewId());
		defaultLayout.get(TabPlace.BOTTOM_LEFT_TABS.ordinal()).add(gglView);
		perspectiveMap.addView(3, gglView);
	}

	public ArrayList<ViewList> getDefaultLayout()
	{
		return defaultLayout;
	}

	public GGLLView getDynamicView(AbstractComponent component)
	{
		for (GGLLView ggllView : ggllViews)
		{
			if (ggllView.getComponentModel().equals(component))
			{
				return ggllView;
			}
		}
		return null;
	}

	public GGLLView getDynamicView(int id)
	{
		for (GGLLView ggllView : ggllViews)
		{
			if (ggllView.getId() == id)
			{
				return ggllView;
			}
		}
		return null;
	}

	public GGLLView getDynamicView(String path)
	{
		for (GGLLView ggllView : ggllViews)
		{
			if (ggllView.getFileName().equals(path))
			{
				return ggllView;
			}
		}
		return null;
	}

	public int getDynamicViewId()
	{
		int max = 0;
		for (GGLLView ggllView : ggllViews)
		{
			if (ggllView.getId() > max)
			{
				max = ggllView.getId();
			}
		}
		return max + 1;
	}

	private void removeDynamicView(GGLLView ggllView)
	{
		ggllViews.remove(ggllView);
	}

	private void updateChildDynamicViews(DockingWindow window, boolean added)
	{
		for (int i = 0; i < window.getChildWindowCount(); i++)
		{
			updateViews(window.getChildWindow(i), added);
		}
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

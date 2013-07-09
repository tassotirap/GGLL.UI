package ggll.view;

import ggll.project.GGLLManager;
import ggll.ui.TabWindowList.TabPlace;
import ggll.ui.component.AbstractComponent;
import ggll.ui.component.GeneratedGrammarComponent;
import ggll.ui.component.OutlineComponent;
import ggll.ui.component.OutputComponent;
import ggll.ui.component.ParserComponent;
import ggll.ui.component.ProjectsComponent;
import ggll.ui.component.SemanticStackComponent;
import ggll.ui.component.SyntaxStackComponent;
import ggll.ui.model.IconFactory;
import ggll.ui.model.IconFactory.IconType;

import java.util.ArrayList;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.util.ViewMap;

public class ViewRepository
{
	private static int DEFAULT_LAYOUT = 6;
	
	private ArrayList<ViewList> defaultLayout;	
	private ArrayList<AbstractView> abstractViews;
	private IconFactory iconFactory;
	private ViewMap viewMap;

	public ViewRepository(ViewMap viewMap)
	{
		this.defaultLayout = new ArrayList<ViewList>();
		this.abstractViews = new ArrayList<AbstractView>();
		this.iconFactory = new IconFactory();
		this.viewMap = viewMap;
	}

	private void addView(AbstractView abstractView)
	{
		abstractViews.add(abstractView);
	}

	private void createGrammarView()
	{
		AbstractView abstractView = new AbstractView("Grammar", iconFactory.getIcon(IconType.GRAMMAR_ICON), new GeneratedGrammarComponent(GGLLManager.getActiveScene()), getNextViewId());
		defaultLayout.get(TabPlace.BOTTOM_LEFT_TABS.ordinal()).add(abstractView);
		viewMap.addView(2, abstractView);
	}

	private void createOutlineView()
	{
		AbstractView abstractView = new AbstractView("Outline", iconFactory.getIcon(IconType.OVERVIEW_CON), new OutlineComponent(GGLLManager.getActiveScene()), getNextViewId());
		defaultLayout.get(TabPlace.RIGHT_TOP_TABS.ordinal()).add(abstractView);
		viewMap.addView(1, abstractView);
	}

	private void createOutputView()
	{
		AbstractView abstractView = new AbstractView("Output", iconFactory.getIcon(IconType.ACTIVE_OUTPUT_ICON), new OutputComponent(GGLLManager.getActiveScene()), getNextViewId());
		defaultLayout.get(TabPlace.BOTTOM_LEFT_TABS.ordinal()).add(abstractView);
		viewMap.addView(5, abstractView);
	}

	private void createParserView()
	{
		AbstractView abstractView = new AbstractView("Parser", iconFactory.getIcon(IconType.PARSER_ICON), new ParserComponent(GGLLManager.getProject().getProjectsRootPath()), getNextViewId());
		defaultLayout.get(TabPlace.BOTTOM_RIGHT_TABS.ordinal()).add(abstractView);
		viewMap.addView(6, abstractView);
	}

	private void createProjectView()
	{
		AbstractView abstractView = new AbstractView("Project", iconFactory.getIcon(IconType.PROJECT_ICON), new ProjectsComponent(GGLLManager.getProject()), getNextViewId());
		defaultLayout.get(TabPlace.RIGHT_BOTTOM_TABS.ordinal()).add(abstractView);
		viewMap.addView(0, abstractView);
	}

	private void createSemanticStackView()
	{
		AbstractView abstractView = new AbstractView("Sem. Stack", iconFactory.getIcon(IconType.SEMANTIC_STACK_ICON), new SemanticStackComponent(GGLLManager.getActiveScene()), getNextViewId());
		defaultLayout.get(TabPlace.BOTTOM_LEFT_TABS.ordinal()).add(abstractView);
		viewMap.addView(4, abstractView);
	}

	private void createSyntaxStackView()
	{
		AbstractView abstractView = new AbstractView("Syntax Stack", iconFactory.getIcon(IconType.SYNTACTIC_STACK_ICON), new SyntaxStackComponent(GGLLManager.getActiveScene()), getNextViewId());
		defaultLayout.get(TabPlace.BOTTOM_LEFT_TABS.ordinal()).add(abstractView);
		viewMap.addView(3, abstractView);
	}

	private void removeView(AbstractView abstractView)
	{
		abstractViews.remove(abstractView);
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
		for (AbstractView abstractView : abstractViews)
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
		for (AbstractView ggllView : abstractViews)
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
		for (AbstractView ggllView : abstractViews)
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
			defaultLayout.add(new ViewList());

		createProjectView();
		createOutlineView();
		createGrammarView();
		createSyntaxStackView();
		createSemanticStackView();
		createOutputView();
		createParserView();
	}

	public ArrayList<ViewList> getDefaultLayout()
	{
		return defaultLayout;
	}

	public AbstractView getView(AbstractComponent component)
	{
		for (AbstractView ggllView : abstractViews)
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
		for (AbstractView abstractView : abstractViews)
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
		for (AbstractView abstractView : abstractViews)
		{
			if (abstractView.getFileName().equals(path))
			{
				return abstractView;
			}
		}
		return null;
	}

	public int getNextViewId()
	{
		int max = 0;
		for (AbstractView ggllView : abstractViews)
		{
			if (ggllView.getId() > max)
			{
				max = ggllView.getId();
			}
		}
		return max + 1;
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

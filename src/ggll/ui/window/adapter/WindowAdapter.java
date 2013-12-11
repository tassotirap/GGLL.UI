package ggll.ui.window.adapter;

import ggll.ui.director.GGLLDirector;
import ggll.ui.view.AbstractView;
import ggll.ui.view.component.AbstractComponent;
import ggll.ui.view.component.AbstractFileComponent;
import ggll.ui.view.component.EmptyComponent;
import ggll.ui.window.MainWindow;

import javax.swing.JOptionPane;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.View;

/** An adapter to control how a window should react when changed **/
public class WindowAdapter extends DockingWindowAdapter
{

	private MainWindow window;

	public WindowAdapter()
	{
		this.window = GGLLDirector.getMainWindow();
	}

	@Override
	public void viewFocusChanged(View oldView, View newView)
	{
		super.viewFocusChanged(oldView, newView);
		if (newView instanceof AbstractView)
		{
			AbstractComponent component = ((AbstractView) newView).getComponentModel();
			window.updateFocusedComponent(component);
		}
	}

	@Override
	public void windowAdded(DockingWindow addedToWindow, DockingWindow addedWindow)
	{
		if (addedWindow instanceof AbstractView)
		{
			window.updateWindow(addedWindow, true);
			AbstractComponent comp = ((AbstractView) addedWindow).getComponentModel();
			if (!(comp instanceof EmptyComponent))
			{
				if (window.getTabs().getCenterLeftTab().getChildWindowIndex(addedWindow) >= 0)
				{
					window.removeEmptyDynamicView();
					window.getTabs().getCenterLeftTab().setSelectedTab(0);
				}
			}
		}
	}

	@Override
	public void windowClosed(DockingWindow dWindow)
	{
		if (dWindow instanceof AbstractView)
		{
			AbstractView view = (AbstractView) dWindow;
			if (view.getComponentModel() instanceof AbstractFileComponent)
			{
				GGLLDirector.closeFile(view.getFileName());
			}
		}
	}

	@Override
	public void windowClosing(DockingWindow dWindow) throws OperationAbortedException
	{
		if (dWindow instanceof AbstractView)
		{
			AbstractView dynamicView = (AbstractView) dWindow;
			if (GGLLDirector.hasUnsavedView(dynamicView))
			{
				int option = JOptionPane.showConfirmDialog(window.getFrame(), "Would you like to save '" + dWindow.getTitle().replace(MainWindow.UNSAVED_PREFIX, "") + "' before closing?");
				if (option == JOptionPane.YES_OPTION)
				{
					GGLLDirector.saveFile(dynamicView.getComponentModel());
				}
			}
		}
		if (window.getTabs().getCenterLeftTab().getChildWindowIndex(dWindow) >= 0 && window.getTabs().getCenterLeftTab().getChildWindowCount() == 1)
		{
			window.addEmptyDynamicView();
		}
	}

	@Override
	public void windowRemoved(DockingWindow removedFromWindow, DockingWindow removedWindow)
	{
		window.updateWindow(removedWindow, false);
	}
}

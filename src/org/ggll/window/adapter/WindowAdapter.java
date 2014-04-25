package org.ggll.window.adapter;

import javax.swing.JOptionPane;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.View;

import org.ggll.facade.GGLLFacade;
import org.ggll.window.MainWindow;
import org.ggll.window.component.AbstractComponent;
import org.ggll.window.component.AbstractFileComponent;
import org.ggll.window.component.EmptyComponent;
import org.ggll.window.view.AbstractView;

/** An adapter to control how a window should react when changed **/
public class WindowAdapter extends DockingWindowAdapter
{
	public WindowAdapter()
	{
	}
	
	@Override
	public void viewFocusChanged(final View oldView, final View newView)
	{
		super.viewFocusChanged(oldView, newView);
		if (newView instanceof AbstractView)
		{
			final AbstractComponent component = ((AbstractView) newView).getComponentModel();
			MainWindow.getInstance().updateFocusedComponent(component);
		}
	}
	
	@Override
	public void windowAdded(final DockingWindow addedToWindow, final DockingWindow addedWindow)
	{
		if (addedWindow instanceof AbstractView)
		{
			MainWindow.getInstance().updateWindow(addedWindow, true);
			final AbstractComponent comp = ((AbstractView) addedWindow).getComponentModel();
			if (!(comp instanceof EmptyComponent))
			{
				if (MainWindow.getInstance().getTabs().getCenterLeftTab().getChildWindowIndex(addedWindow) >= 0)
				{
					MainWindow.getInstance().removeEmptyDynamicView();
					MainWindow.getInstance().getTabs().getCenterLeftTab().setSelectedTab(0);
				}
			}
		}
	}
	
	@Override
	public void windowClosed(final DockingWindow dockingWindow)
	{
		if (dockingWindow instanceof AbstractView)
		{
			final AbstractView view = (AbstractView) dockingWindow;
			if (view.getComponentModel() instanceof AbstractFileComponent)
			{
				GGLLFacade.getInstance().closeFile(view.getFileName());
			}
		}
	}
	
	@Override
	public void windowClosing(final DockingWindow dWindow) throws OperationAbortedException
	{
		if (dWindow instanceof AbstractView)
		{
			final AbstractView dynamicView = (AbstractView) dWindow;
			if (GGLLFacade.getInstance().hasUnsavedView(dynamicView))
			{
				final int option = JOptionPane.showConfirmDialog(MainWindow.getInstance().getFrame(), "Would you like to save '" + dWindow.getTitle().replace(MainWindow.UNSAVED_PREFIX, "") + "' before closing?");
				if (option == JOptionPane.YES_OPTION)
				{
					GGLLFacade.getInstance().saveFile(dynamicView.getComponentModel());
				}
			}
		}
		if (MainWindow.getInstance().getTabs().getCenterLeftTab().getChildWindowIndex(dWindow) >= 0 && MainWindow.getInstance().getTabs().getCenterLeftTab().getChildWindowCount() == 1)
		{
			MainWindow.getInstance().addEmptyDynamicView();
		}
	}
	
	@Override
	public void windowRemoved(final DockingWindow removedFromWindow, final DockingWindow removedWindow)
	{
		MainWindow.getInstance().updateWindow(removedWindow, false);
	}
}

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
	
	private final MainWindow window;
	
	public WindowAdapter()
	{
		window = GGLLFacade.getInstance().getMainWindow();
	}
	
	@Override
	public void viewFocusChanged(final View oldView, final View newView)
	{
		super.viewFocusChanged(oldView, newView);
		if (newView instanceof AbstractView)
		{
			final AbstractComponent component = ((AbstractView) newView).getComponentModel();
			window.updateFocusedComponent(component);
		}
	}
	
	@Override
	public void windowAdded(final DockingWindow addedToWindow, final DockingWindow addedWindow)
	{
		if (addedWindow instanceof AbstractView)
		{
			window.updateWindow(addedWindow, true);
			final AbstractComponent comp = ((AbstractView) addedWindow).getComponentModel();
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
	public void windowClosed(final DockingWindow dWindow)
	{
		if (dWindow instanceof AbstractView)
		{
			final AbstractView view = (AbstractView) dWindow;
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
				final int option = JOptionPane.showConfirmDialog(window.getFrame(), "Would you like to save '" + dWindow.getTitle().replace(MainWindow.UNSAVED_PREFIX, "") + "' before closing?");
				if (option == JOptionPane.YES_OPTION)
				{
					GGLLFacade.getInstance().saveFile(dynamicView.getComponentModel());
				}
			}
		}
		if (window.getTabs().getCenterLeftTab().getChildWindowIndex(dWindow) >= 0 && window.getTabs().getCenterLeftTab().getChildWindowCount() == 1)
		{
			window.addEmptyDynamicView();
		}
	}
	
	@Override
	public void windowRemoved(final DockingWindow removedFromWindow, final DockingWindow removedWindow)
	{
		window.updateWindow(removedWindow, false);
	}
}

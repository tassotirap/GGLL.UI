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

	private final MainWindow window;

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
			final AbstractComponent component = ((AbstractView) newView).getComponentModel();
			this.window.updateFocusedComponent(component);
		}
	}

	@Override
	public void windowAdded(DockingWindow addedToWindow, DockingWindow addedWindow)
	{
		if (addedWindow instanceof AbstractView)
		{
			this.window.updateWindow(addedWindow, true);
			final AbstractComponent comp = ((AbstractView) addedWindow).getComponentModel();
			if (!(comp instanceof EmptyComponent))
			{
				if (this.window.getTabs().getCenterLeftTab().getChildWindowIndex(addedWindow) >= 0)
				{
					this.window.removeEmptyDynamicView();
					this.window.getTabs().getCenterLeftTab().setSelectedTab(0);
				}
			}
		}
	}

	@Override
	public void windowClosed(DockingWindow dWindow)
	{
		if (dWindow instanceof AbstractView)
		{
			final AbstractView view = (AbstractView) dWindow;
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
			final AbstractView dynamicView = (AbstractView) dWindow;
			if (GGLLDirector.hasUnsavedView(dynamicView))
			{
				final int option = JOptionPane.showConfirmDialog(this.window.getFrame(), "Would you like to save '" + dWindow.getTitle().replace(MainWindow.UNSAVED_PREFIX, "") + "' before closing?");
				if (option == JOptionPane.YES_OPTION)
				{
					GGLLDirector.saveFile(dynamicView.getComponentModel());
				}
			}
		}
		if (this.window.getTabs().getCenterLeftTab().getChildWindowIndex(dWindow) >= 0 && this.window.getTabs().getCenterLeftTab().getChildWindowCount() == 1)
		{
			this.window.addEmptyDynamicView();
		}
	}

	@Override
	public void windowRemoved(DockingWindow removedFromWindow, DockingWindow removedWindow)
	{
		this.window.updateWindow(removedWindow, false);
	}
}

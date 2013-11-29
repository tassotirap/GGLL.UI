package ggll.ui.main;

import ggll.ui.component.AbstractComponent;
import ggll.ui.component.EmptyComponent;
import ggll.ui.component.FileComponent;
import ggll.ui.project.Context;
import ggll.ui.view.AbstractView;

import javax.swing.JOptionPane;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.View;

/** An adapter to control how a window should react when changed **/
public class WindowAdapter extends DockingWindowAdapter
{

	private IMainWindow window;

	public WindowAdapter()
	{
		this.window = Context.getMainWindow();
	}

	@Override
	public void viewFocusChanged(View ov, View nv)
	{
		super.viewFocusChanged(ov, nv);
		if (nv instanceof AbstractView)
		{
			AbstractComponent comp = ((AbstractView) nv).getComponentModel();
			window.updateFocusedComponent(comp);
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
			if (view.getComponentModel() instanceof FileComponent)
			{
				Context.closeFile(view.getFileName());
			}
		}
	}

	@Override
	public void windowClosing(DockingWindow dWindow) throws OperationAbortedException
	{
		if (dWindow instanceof AbstractView)
		{
			AbstractView dynamicView = (AbstractView) dWindow;
			if (Context.hasUnsavedView(dynamicView))
			{
				int option = JOptionPane.showConfirmDialog(window.getFrame(), "Would you like to save '" + dWindow.getTitle().replace(IMainWindow.UNSAVED_PREFIX, "") + "' before closing?");
				if (option == JOptionPane.CANCEL_OPTION)
					throw new OperationAbortedException("Window close was aborted!");
				if (option == JOptionPane.YES_OPTION)
				{
					Context.saveFile(dynamicView.getComponentModel());
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

package org.ggll.ui;

import javax.swing.JOptionPane;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.View;

import org.ggll.project.GGLLManager;
import org.ggll.ui.component.AbstractComponent;
import org.ggll.ui.component.BadParameterException;
import org.ggll.ui.component.EmptyComponent;
import org.ggll.ui.component.FileComponent;
import org.ggll.ui.interfaces.IMainWindow;
import org.ggll.view.GGLLView;

/** An adapter to control how a window should react when changed **/
public class WindowAdapter extends DockingWindowAdapter
{

	private IMainWindow window;

	public WindowAdapter()
	{
		this.window = GGLLManager.getMainWindow();		
	}
	

	@Override
	public void viewFocusChanged(View ov, View nv)
	{
		super.viewFocusChanged(ov, nv);
		if (nv instanceof GGLLView)
		{
			AbstractComponent comp = ((GGLLView) nv).getComponentModel();
			window.updateFocusedComponent(comp);
		}
	}

	@Override
	public void windowAdded(DockingWindow addedToWindow, DockingWindow addedWindow)
	{
		if (addedWindow instanceof GGLLView)
		{
			window.updateWindow(addedWindow, true);
			AbstractComponent comp = ((GGLLView) addedWindow).getComponentModel();
			if (!(comp instanceof EmptyComponent))
			{
				if (window.getTabs().getCenterTab().getChildWindowIndex(addedWindow) >= 0)
				{
					window.removeEmptyDynamicView();
				}
			}
		}
	}

	@Override
	public void windowClosed(DockingWindow dWindow)
	{
		if (dWindow instanceof GGLLView)
		{
			GGLLView view = (GGLLView) dWindow;
			if (view.getComponentModel() instanceof FileComponent)
			{
				GGLLManager.closeFile(view.getFileName());
			}
		}
	}

	@Override
	public void windowClosing(DockingWindow dWindow) throws OperationAbortedException
	{
		if (dWindow instanceof GGLLView)
		{
			GGLLView dynamicView = (GGLLView) dWindow;
			if (GGLLManager.hasUnsavedView(dynamicView))
			{
				int option = JOptionPane.showConfirmDialog(window.getFrame(), "Would you like to save '" + dWindow.getTitle().replace(IMainWindow.UNSAVED_PREFIX, "") + "' before closing?");
				if (option == JOptionPane.CANCEL_OPTION)
					throw new OperationAbortedException("Window close was aborted!");
				if (option == JOptionPane.YES_OPTION)
				{
					GGLLManager.saveFile(dynamicView.getComponentModel());
				}
			}
		}
		if (window.getTabs().getCenterTab().getChildWindowIndex(dWindow) >= 0 && window.getTabs().getCenterTab().getChildWindowCount() == 1)
		{
			try
			{
				window.addEmptyDynamicView();
			}
			catch (BadParameterException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void windowRemoved(DockingWindow removedFromWindow, DockingWindow removedWindow)
	{
		window.updateWindow(removedWindow, false);
	}
}

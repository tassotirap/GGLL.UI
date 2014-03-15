package org.ggll.window.adapter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.ggll.facade.GGLLFacade;

public class WindowClosingAdapter extends WindowAdapter
{
	@Override
	public void windowClosing(final WindowEvent arg0)
	{
		GGLLFacade.getInstance().exit();
	}
}

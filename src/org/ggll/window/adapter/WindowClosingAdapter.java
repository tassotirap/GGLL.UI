package org.ggll.window.adapter;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.ggll.director.GGLLDirector;

public class WindowClosingAdapter extends WindowAdapter
{
	@Override
	public void windowClosing(WindowEvent arg0)
	{
		GGLLDirector.exit();
	}
}

package ggll.ui.window.adapter;

import ggll.ui.director.GGLLDirector;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowClosingAdapter extends WindowAdapter
{
	@Override
	public void windowClosing(WindowEvent arg0)
	{
		GGLLDirector.exit();
	}
}

package ggll.ui.main;

import ggll.ui.project.GGLLManager;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FrameAdapter extends WindowAdapter
{
	@Override
	public void windowClosing(WindowEvent arg0)
	{
		GGLLManager.exit();
	}
}

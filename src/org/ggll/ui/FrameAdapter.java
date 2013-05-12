package org.ggll.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.ggll.project.GGLLManager;

public class FrameAdapter extends WindowAdapter
{

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		GGLLManager.exit();
	}

}

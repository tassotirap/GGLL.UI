package org.ggll.ui;

import javax.swing.JFrame;

import org.ggll.project.GGLLManager;
import org.ggll.ui.interfaces.IMainWindow;
import org.ggll.ui.lib.SplashWindow;

/**
 * 
 * @author Tasso Tirapani Silva Pinto
 * 
 *         GuiLancher class is the Starter class of GGLL First is loaded
 *         WorkspaceChooser followed by MainWindow
 *         Refactory: OK
 * 
 */
public class GuiLauncher
{
	private final static String SPLASH_SCREEN_PNG = "splash_screen.png";

	private void showFrame(final JFrame frame)
	{
		java.awt.EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				frame.setVisible(true);
			}
		});
	}

	private void showFrame(final MainWindow frame)
	{
		java.awt.EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				frame.showFrame();
			}
		});
	}

	private void startApp()
	{
		WorkspaceChooser workspaceChooser = startWorkspaceChooser();
		if (workspaceChooser.isDone())
		{
			SplashWindow.splash(GuiLauncher.class.getResource(SPLASH_SCREEN_PNG));
			startMainWindow(workspaceChooser);
			SplashWindow.disposeSplash();
		}
	}

	private IMainWindow startMainWindow(WorkspaceChooser workspaceChooser)
	{	
		MainWindow mainWindow = new MainWindow();
		
		GGLLManager.Start(mainWindow, workspaceChooser.getWorkspaceDir());
		
		showFrame(mainWindow);
		return mainWindow;		
	}

	private WorkspaceChooser startWorkspaceChooser()
	{
		WorkspaceChooser workspaceChooser = new WorkspaceChooser();
		showFrame(workspaceChooser);
		while (!workspaceChooser.isCanceled() && !workspaceChooser.isDone())
		{
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return workspaceChooser;
	}
	
	public static void main(String[] args)
	{
		GuiLauncher guiLauncher = new GuiLauncher();
		guiLauncher.startApp();
	}
}

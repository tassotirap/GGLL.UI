package ggll.ui.window;

import ggll.ui.director.GGLLDirector;
import ggll.ui.lib.SplashWindow;

import javax.swing.JFrame;

/**
 * 
 * @author Tasso Tirapani Silva Pinto
 * 
 *         GuiLancher class is the Starter class of GGLL First is loaded
 *         WorkspaceChooser followed by MainWindow Refactory: OK
 * 
 */
public class GuiLauncher
{
	private final static String SPLASH_SCREEN_PNG = "/ggll/ui/images/splash_screen.png";

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

	private MainWindow startMainWindow(WorkspaceChooser workspaceChooser)
	{
		MainWindow mainWindow = new MainWindow();
		GGLLDirector.Start(mainWindow, workspaceChooser.getWorkspaceDir());
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

	public void start()
	{
		WorkspaceChooser workspaceChooser = startWorkspaceChooser();
		if (workspaceChooser.isDone())
		{
			SplashWindow.splash(GuiLauncher.class.getResource(SPLASH_SCREEN_PNG));
			startMainWindow(workspaceChooser);
			SplashWindow.disposeSplash();
		}
	}
}

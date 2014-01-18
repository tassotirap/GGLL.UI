package org.ggll.window;

import javax.swing.JFrame;

import org.ggll.director.GGLLDirector;
import org.ggll.project.Project;
import org.ggll.project.ProjectHelper;
import org.ggll.window.splash.SplashWindow;

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
	private final static String SPLASH_SCREEN_PNG = "/org/ggll/images/splash_screen.png";

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
		final MainWindow mainWindow = new MainWindow();
		final Project project = ProjectHelper.openProject(workspaceChooser.getWorkspaceDir());
		GGLLDirector.Start(mainWindow, project);
		showFrame(mainWindow);
		return mainWindow;
	}

	private WorkspaceChooser startWorkspaceChooser()
	{
		final WorkspaceChooser workspaceChooser = new WorkspaceChooser();
		showFrame(workspaceChooser);
		while (!workspaceChooser.isCanceled() && !workspaceChooser.isDone())
		{
			try
			{
				Thread.sleep(500);
			}
			catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return workspaceChooser;
	}

	public void start()
	{
		final WorkspaceChooser workspaceChooser = startWorkspaceChooser();
		if (workspaceChooser.isDone())
		{
			SplashWindow.splash(GuiLauncher.class.getResource(SPLASH_SCREEN_PNG));
			startMainWindow(workspaceChooser);
			SplashWindow.disposeSplash();
		}
	}
}

package org.ggll.window;

import javax.swing.JFrame;

import org.ggll.facade.GGLLFacade;
import org.ggll.images.ImageResource;
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
	
	public void start()
	{
		final WorkspaceChooser workspaceChooser = startWorkspaceChooser();
		if (workspaceChooser.isDone())
		{
			SplashWindow.splash(ImageResource.imagePath + GuiLauncher.SPLASH_SCREEN_PNG);
			startMainWindow(workspaceChooser);
			SplashWindow.disposeSplash();
		}
	}
	
	private MainWindow startMainWindow(final WorkspaceChooser workspaceChooser)
	{
		final MainWindow mainWindow = new MainWindow();
		final Project project = ProjectHelper.openProject(workspaceChooser.getWorkspaceDir());
		GGLLFacade.Start(mainWindow, project);
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
}

package org.ggll.window;

import ggll.core.compile.Compiler;
import ggll.core.properties.GGLLProperties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
public class Launcher
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
		if (sdkPath())
		{
			final WorkspaceChooser workspaceChooser = startWorkspaceChooser();
			if (workspaceChooser.isDone())
			{
				SplashWindow.splash(ImageResource.imagePath + Launcher.SPLASH_SCREEN_PNG);
				startMainWindow(workspaceChooser);
				SplashWindow.disposeSplash();
			}
		}
	}
	
	private boolean sdkPath()
	{
		Compiler compiler = new Compiler();
		
		if(!compiler.validateSDKPath())
		{
			JOptionPane.showMessageDialog(null, "Java SDK not found, please download it at http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk7-downloads-1880260.html "
					+ "and select the folder where it was installed.", "Java SDK not found",JOptionPane.ERROR_MESSAGE);
		}
		
		while (!compiler.validateSDKPath())
		{
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = jFileChooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				GGLLProperties ggllProperties = new GGLLProperties();
				ggllProperties.setJavaSDKPath(jFileChooser.getSelectedFile().getAbsolutePath());
				compiler = new Compiler();
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	private void startMainWindow(final WorkspaceChooser workspaceChooser)
	{
		final Project project = ProjectHelper.openProject(workspaceChooser.getWorkspaceDir());
		GGLLFacade.start(project);
		showFrame(MainWindow.getInstance());
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

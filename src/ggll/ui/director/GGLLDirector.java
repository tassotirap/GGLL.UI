package ggll.ui.director;

import ggll.core.list.ExtendedList;
import ggll.ui.canvas.Canvas;
import ggll.ui.file.FileNames;
import ggll.ui.project.FileManager;
import ggll.ui.project.Project;
import ggll.ui.project.ProjectHelper;
import ggll.ui.util.print.ComponentPrinter;
import ggll.ui.util.print.TextPrinter;
import ggll.ui.view.AbstractView;
import ggll.ui.view.UnsavedViewRepository;
import ggll.ui.view.component.AbstractComponent;
import ggll.ui.view.component.TextAreaComponent;
import ggll.ui.window.MainWindow;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

public final class GGLLDirector
{
	private static MainWindow mainWindow;
	private static Project project;
	private static FileManager fileManager;
	private static UnsavedViewRepository unViewManager;
	private static Canvas activeScene;

	public static void closeFile(String fileName)
	{
		fileManager.closeFile(fileName);
	}

	public static void createFile(String name, FileNames extension) throws IOException
	{
		fileManager.createFile(name, extension);
	}

	public static void exit()
	{
		ExtendedList<AbstractView> unsavedViews = unViewManager.getUnsavedViews();

		for (AbstractView dynamicView : unsavedViews.getAll())
		{
			int option = JOptionPane.showConfirmDialog(getMainWindow().getFrame(), "Would you like to save '" + dynamicView.getTitle().replace(MainWindow.UNSAVED_PREFIX, "") + "' before exiting?");
			if (option == JOptionPane.CANCEL_OPTION)
				return;
			else if (option == JOptionPane.YES_OPTION)
			{
				saveFile(dynamicView.getComponentModel());
			}
		}
		System.exit(0);
	}

	public static Canvas getActiveScene()
	{
		return activeScene;
	}

	public static MainWindow getMainWindow()
	{
		return GGLLDirector.mainWindow;
	}

	public static ExtendedList<File> getOpenedFiles()
	{
		ExtendedList<File> filesOpened = project.getOpenedFiles();
		if (filesOpened.count() == 0)
		{
			GGLLDirector.project.getOpenedFiles().append(project.getGrammarFile().get(0));
		}
		return filesOpened;
	}

	public static Project getProject()
	{
		return GGLLDirector.project;
	}

	public static AbstractView getUnsavedView(String file)
	{
		return GGLLDirector.unViewManager.getUnsavedView(file);
	}

	public static boolean hasUnsavedView(AbstractView view)
	{
		return GGLLDirector.unViewManager.hasUnsavedView(view);
	}

	public static boolean hasUnsavedView(String file)
	{
		return GGLLDirector.unViewManager.hasUnsavedView(file);
	}

	public static boolean isFileOpen(String absolutePath)
	{
		return GGLLDirector.fileManager.isFileOpen(absolutePath);
	}

	public static void openFile(String path)
	{
		GGLLDirector.fileManager.openFile(path);
	}

	public static void openFile(String path, boolean verifyOpen)
	{
		fileManager.openFile(path, verifyOpen);
	}

	public static void print(Object object)
	{
		if (object instanceof TextAreaComponent)
		{
			TextPrinter.printText(((TextAreaComponent) object).getText());
		}
		else if (object instanceof Canvas)
		{
			ComponentPrinter.printWidget((Canvas) object);
		}
	}

	public static void removeUnsavedView(String path)
	{
		GGLLDirector.unViewManager.removeUnsavedView(path);
	}

	public static void saveAllFiles()
	{
		GGLLDirector.fileManager.saveAllFiles(unViewManager.getUnsavedViews());
	}

	public static void saveFile(AbstractComponent object)
	{
		GGLLDirector.fileManager.saveFileObject(object);
	}

	public static void setActiveScene(Canvas activeScene)
	{
		GGLLDirector.activeScene = activeScene;
	}

	public static void setUnsavedView(String path, AbstractView view)
	{
		GGLLDirector.unViewManager.setUnsavedView(path, view);
	}

	public static void Start(MainWindow mainWindow, String projectPath)
	{
		GGLLDirector.mainWindow = mainWindow;
		GGLLDirector.project = ProjectHelper.openProject(projectPath);
		GGLLDirector.unViewManager = new UnsavedViewRepository();
		GGLLDirector.fileManager = new FileManager();
	}
}

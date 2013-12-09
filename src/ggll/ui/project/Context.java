package ggll.ui.project;

import ggll.core.list.ExtendedList;
import ggll.ui.canvas.Canvas;
import ggll.ui.file.FileNames;
import ggll.ui.main.IMainWindow;
import ggll.ui.main.MainWindow;
import ggll.ui.util.print.ComponentPrinter;
import ggll.ui.util.print.TextPrinter;
import ggll.ui.view.AbstractView;
import ggll.ui.view.UnsavedViewRepository;
import ggll.ui.view.component.AbstractComponent;
import ggll.ui.view.component.TextAreaComponent;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

public final class Context
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
	
	public static void openFile(String path, boolean verifyOpen)
	{
		fileManager.openFile(path, verifyOpen);
	}

	public static void exit()
	{
		ExtendedList<AbstractView> unsavedViews = unViewManager.getUnsavedViews();

		for (AbstractView dynamicView : unsavedViews.getAll())
		{
			int option = JOptionPane.showConfirmDialog(getMainWindow().getFrame(), "Would you like to save '" + dynamicView.getTitle().replace(IMainWindow.UNSAVED_PREFIX, "") + "' before exiting?");
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
		return Context.mainWindow;
	}

	public static ExtendedList<File> getOpenedFiles()
	{
		ExtendedList<File> filesOpened = project.getOpenedFiles();
		if (filesOpened.count() == 0)
		{
			Context.project.getOpenedFiles().append(project.getGrammarFile().get(0));
		}
		return filesOpened;
	}

	public static Project getProject()
	{
		return Context.project;
	}

	
	public static AbstractView getUnsavedView(String file)
	{
		return Context.unViewManager.getUnsavedView(file);
	}
	
	public static boolean hasUnsavedView(AbstractView view)
	{
		return Context.unViewManager.hasUnsavedView(view);
	}
	
	public static boolean hasUnsavedView(String file)
	{
		return Context.unViewManager.hasUnsavedView(file);
	}

	public static boolean isFileOpen(String absolutePath)
	{
		return Context.fileManager.isFileOpen(absolutePath);
	}

	public static void openFile(String path)
	{
		Context.fileManager.openFile(path);
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
		Context.unViewManager.removeUnsavedView(path);
	}

	public static void saveAllFiles()
	{
		Context.fileManager.saveAllFiles(unViewManager.getUnsavedViews());
	}

	public static void saveFile(AbstractComponent object)
	{
		Context.fileManager.saveFileObject((AbstractComponent)object);
	}

	public static void setActiveScene(Canvas activeScene)
	{
		Context.activeScene = activeScene;
	}

	public static void setUnsavedView(String path, AbstractView view)
	{
		Context.unViewManager.setUnsavedView(path, view);
	}

	public static void Start(MainWindow mainWindow, String projectPath)
	{
		Context.mainWindow = mainWindow;
		Context.project = ProjectHelper.openProject(projectPath);
		Context.unViewManager = new UnsavedViewRepository();
		Context.fileManager = new FileManager();
	}
}

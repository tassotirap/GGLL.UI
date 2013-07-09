package ggll.project;

import ggll.canvas.Canvas;
import ggll.canvas.state.StaticStateManager;
import ggll.file.FileNames;
import ggll.ui.MainWindow;
import ggll.ui.component.GrammarComponent;
import ggll.ui.component.TextAreaComponent;
import ggll.ui.interfaces.IMainWindow;
import ggll.util.ComponentPrinter;
import ggll.util.Log;
import ggll.util.TextPrinter;
import ggll.view.AbstractView;
import ggll.view.UnsavedViewRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public final class GGLLManager
{
	private static MainWindow mainWindow;
	private static Project project;
	private static FileManager fileManager;
	private static UnsavedViewRepository unViewManager;
	private static Canvas activeScene;

	public static void closeFile(String fileName)
	{
		GGLLManager.fileManager.closeFile(fileName);
	}

	public static void createFile(String name, FileNames extension) throws IOException
	{
		GGLLManager.fileManager.createFile(name, extension);
	}

	public static void exit()
	{
		ArrayList<AbstractView> unsavedViews = getUnsavedViews();

		for (AbstractView dynamicView : unsavedViews)
		{
			int option = JOptionPane.showConfirmDialog(getMainWindow().getFrame(), "Would you like to save '" + dynamicView.getTitle().replace(IMainWindow.UNSAVED_PREFIX, "") + "' before exiting?");
			if (option == JOptionPane.CANCEL_OPTION)
				return;
			if (option == JOptionPane.YES_OPTION && dynamicView.getComponentModel() instanceof GrammarComponent)
			{
				StaticStateManager StaticStateManager = activeScene.getStaticStateManager();
				try
				{
					StaticStateManager.write();
					String path = StaticStateManager.getParentDirectory();
					saveFile(path);
				}
				catch (IOException e)
				{
					Log.log(Log.ERROR, getMainWindow(), "Could not save file", e);
				}
			}
			else if (option == JOptionPane.YES_OPTION)
			{
				saveFile(dynamicView.getComponentModel());
			}
		}
		saveProject();
		System.exit(0);
	}

	public static Canvas getActiveScene()
	{
		return activeScene;
	}

	public static MainWindow getMainWindow()
	{
		return GGLLManager.mainWindow;
	}

	public static List<File> getOpenedFiles()
	{
		List<File> filesOpened = project.getOpenedFiles();
		if (filesOpened.size() == 0)
		{
			GGLLManager.project.getOpenedFiles().add(project.getGrammarFile());
		}
		return filesOpened;
	}

	public static Project getProject()
	{
		return GGLLManager.project;
	}

	public static UnsavedViewRepository getUnsavedViewManager()
	{
		return GGLLManager.unViewManager;
	}

	public static ArrayList<AbstractView> getUnsavedViews()
	{
		return GGLLManager.unViewManager.getUnsavedViews();
	}

	public static boolean hasUnsavedView(AbstractView dynamicView)
	{
		return GGLLManager.unViewManager.hasUnsavedView(dynamicView);
	}

	public static boolean hasUnsavedView(String file)
	{
		return GGLLManager.unViewManager.hasUnsavedView(file);
	}

	public static boolean isFileOpen(String absolutePath)
	{
		return GGLLManager.fileManager.isFileOpen(absolutePath);
	}

	public static void openFile(String path)
	{
		GGLLManager.fileManager.openFile(path);
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
		GGLLManager.unViewManager.removeUnsavedView(path);
	}

	public static void saveAllFiles()
	{
		GGLLManager.fileManager.saveAllFiles(getUnsavedViews());
	}

	public static void saveFile(Object object)
	{
		GGLLManager.fileManager.saveFileObject(object);
	}

	public static boolean saveProject()
	{
		return GGLLManager.project.save();
	}

	public static void setActiveScene(Canvas activeScene)
	{
		GGLLManager.activeScene = activeScene;
	}

	public static void setUnsavedView(String path, AbstractView view)
	{
		GGLLManager.unViewManager.setUnsavedView(path, view);
	}

	public static void Start(MainWindow mainWindow, String projectPath)
	{
		GGLLManager.mainWindow = mainWindow;
		GGLLManager.project = ProjectHelper.openProject(projectPath);
		GGLLManager.unViewManager = new UnsavedViewRepository();
		GGLLManager.fileManager = new FileManager();
	}
}

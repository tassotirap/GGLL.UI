package org.ggll.director;

import ggll.core.list.ExtendedList;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.ggll.file.FileNames;
import org.ggll.project.FileManager;
import org.ggll.project.Project;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.util.print.ComponentPrinter;
import org.ggll.util.print.TextPrinter;
import org.ggll.window.MainWindow;
import org.ggll.window.component.AbstractComponent;
import org.ggll.window.component.GrammarComponent;
import org.ggll.window.component.TextAreaComponent;
import org.ggll.window.view.AbstractView;
import org.ggll.window.view.UnsavedViewRepository;

public final class GGLLDirector
{
	private static MainWindow mainWindow;
	private static Project project;
	private static FileManager fileManager;
	private static UnsavedViewRepository unViewManager;
	private static SyntaxGraph activeCanvas;

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
		final ExtendedList<AbstractView> unsavedViews = unViewManager.getUnsavedViews();

		for (final AbstractView dynamicView : unsavedViews.getAll())
		{
			final int option = JOptionPane.showConfirmDialog(getMainWindow().getFrame(), "Would you like to save '" + dynamicView.getTitle().replace(MainWindow.UNSAVED_PREFIX, "") + "' before exiting?");
			if (option == JOptionPane.CANCEL_OPTION)
			{
				return;
			}
			else if (option == JOptionPane.YES_OPTION)
			{
				saveFile(dynamicView.getComponentModel());
			}
		}
		System.exit(0);
	}

	public static SyntaxGraph getActiveSyntaxGraph()
	{
		return activeCanvas;
	}

	public static MainWindow getMainWindow()
	{
		return mainWindow;
	}

	public static ExtendedList<File> getOpenedFiles()
	{
		final ExtendedList<File> filesOpened = project.getOpenedFiles();
		if (filesOpened.count() == 0)
		{
			project.getOpenedFiles().append(project.getGrammarFile().get(0));
		}
		return filesOpened;
	}

	public static Project getProject()
	{
		return project;
	}

	public static AbstractView getUnsavedView(String file)
	{
		return unViewManager.getUnsavedView(file);
	}

	public static boolean hasUnsavedView(AbstractView view)
	{
		return unViewManager.hasUnsavedView(view);
	}

	public static boolean hasUnsavedView(String file)
	{
		return unViewManager.hasUnsavedView(file);
	}

	public static boolean isFileOpen(String absolutePath)
	{
		return fileManager.isFileOpen(absolutePath);
	}

	public static void openFile(String path)
	{
		fileManager.openFile(path);
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
		else if (object instanceof GrammarComponent)
		{
			final GrammarComponent grammarComponent = (GrammarComponent) object;
			ComponentPrinter.printWidget(grammarComponent.getCanvas());
		}
	}

	public static void removeUnsavedView(String path)
	{
		unViewManager.removeUnsavedView(path);
	}

	public static void saveAllFiles()
	{
		fileManager.saveAllFiles(unViewManager.getUnsavedViews());
	}

	public static void saveFile(AbstractComponent object)
	{
		fileManager.saveFileObject(object);
	}

	public static void setActiveSyntaxGraph(SyntaxGraph syntaxGraph)
	{
		activeCanvas = syntaxGraph;
	}

	public static void setUnsavedView(String path, AbstractView view)
	{
		unViewManager.setUnsavedView(path, view);
	}

	public static void Start(MainWindow mainWindow, Project project)
	{
		GGLLDirector.mainWindow = mainWindow;
		GGLLDirector.project = project;
		GGLLDirector.unViewManager = new UnsavedViewRepository();
		GGLLDirector.fileManager = new FileManager();
	}
}

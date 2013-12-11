package ggll.ui.project;

import ggll.core.list.ExtendedList;
import ggll.ui.director.GGLLDirector;
import ggll.ui.file.FileNames;
import ggll.ui.icon.IconFactory;
import ggll.ui.icon.IconFactory.IconType;
import ggll.ui.tab.TabWindowList.TabPlace;
import ggll.ui.util.Log;
import ggll.ui.view.AbstractView;
import ggll.ui.view.component.AbstractComponent;
import ggll.ui.view.component.AbstractFileComponent;
import ggll.ui.view.component.GrammarComponent;
import ggll.ui.view.component.TextAreaComponent;
import ggll.ui.window.MainWindow;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

public class FileManager
{
	private Project project;
	private MainWindow mainWindow;

	public FileManager()
	{
		project = GGLLDirector.getProject();
		mainWindow = GGLLDirector.getMainWindow();
	}

	public void closeFile(String fileName)
	{
		for (File file : project.getOpenedFiles().getAll())
		{
			if (file.getAbsolutePath().equals(fileName))
			{
				project.getOpenedFiles().remove(file);
				break;
			}
		}
	}

	public void createFile(String name, FileNames extension) throws IOException
	{
		if (extension.getExtension().equals(FileNames.SEM_EXTENSION))
		{
			if (project.getSemanticFile() != null)
			{
				JOptionPane.showMessageDialog(null, "Only one semantic routines file is allowed by project.", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else if (extension.getExtension().equals(FileNames.LEX_EXTENSION))
		{
			if (project.getLexicalFile() != null)
			{
				JOptionPane.showMessageDialog(null, "Only one lexical scanner file is allowed by project.", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
		{
			File baseDir = project.getProjectDir();
			File newFile = null;
			if (extension != null)
			{
				if (!name.endsWith(extension.getExtension()))
				{
					name += extension.getExtension();
				}
			}
			if (!name.startsWith(baseDir.getAbsolutePath()))
			{
				newFile = new File(baseDir, name);
			}
			else
			{
				newFile = new File(name);
			}
			try
			{
				if (newFile.exists())
				{
					JOptionPane.showMessageDialog(null, "File already exists.", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					newFile.createNewFile();
					openFile(newFile.getAbsolutePath());
				}
			}
			catch (SecurityException e)
			{
				JOptionPane.showMessageDialog(null, "Could not create file. Probably you do not have permission to write on disk.", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
				Log.log(Log.WARNING, mainWindow, "A security exception was thrown while trying to create a new file.", e);
			}
		}
	}

	public boolean isFileOpen(String fileName)
	{
		for (File file : project.getOpenedFiles().getAll())
		{
			if (file.getAbsolutePath().equals(fileName))
				return true;
		}
		return false;
	}

	public void openFile(String path)
	{
		openFile(path, true);
	}

	public void openFile(String path, boolean verifyOpen)
	{
		IconFactory iconFactory = new IconFactory();
		File file = new File(path);
		if (!verifyOpen || !isFileOpen(path))
		{
			try
			{
				if (file.getName().toLowerCase().endsWith(FileNames.LEX_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.LEX_ICON), TabPlace.CENTER_LEFT_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.SEM_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.SEM_ICON), TabPlace.CENTER_LEFT_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.GRAM_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent(new GrammarComponent(path), file.getName(), path, iconFactory.getIcon(IconType.GRAM_ICON), TabPlace.CENTER_LEFT_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.XML_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.XML_ICON), TabPlace.CENTER_LEFT_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.JAVA_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.JAVA_ICON), TabPlace.CENTER_LEFT_TABS);
				}
				else
				{
					mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.TXT_ICON), TabPlace.CENTER_LEFT_TABS);
				}
				if (verifyOpen)
				{
					project.getOpenedFiles().append(new File(path));
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public void saveAllFiles(ExtendedList<AbstractView> components)
	{
		for (AbstractView fileComponent : components.getAll())
		{
			saveFileObject(fileComponent.getComponentModel());
		}
	}

	public void saveFileObject(AbstractComponent component)
	{
		if (component instanceof AbstractFileComponent)
		{
			AbstractFileComponent fileComponent = (AbstractFileComponent) component;
			String path = fileComponent.saveFile();
			if (mainWindow != null)
			{
				mainWindow.setSaved(path);
			}
		}
	}

}

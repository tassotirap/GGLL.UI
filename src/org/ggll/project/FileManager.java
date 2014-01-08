package org.ggll.project;

import ggll.core.list.ExtendedList;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.ggll.director.GGLLDirector;
import org.ggll.file.FileNames;
import org.ggll.images.IconFactory;
import org.ggll.images.IconFactory.IconType;
import org.ggll.util.Log;
import org.ggll.window.MainWindow;
import org.ggll.window.component.AbstractComponent;
import org.ggll.window.component.AbstractFileComponent;
import org.ggll.window.component.GrammarComponent;
import org.ggll.window.component.TextAreaComponent;
import org.ggll.window.tab.TabWindowList.TabPlace;
import org.ggll.window.view.AbstractView;

public class FileManager
{
	private final Project project;
	private final MainWindow mainWindow;

	public FileManager()
	{
		this.project = GGLLDirector.getProject();
		this.mainWindow = GGLLDirector.getMainWindow();
	}

	public void closeFile(String fileName)
	{
		for (final File file : this.project.getOpenedFiles().getAll())
		{
			if (file.getAbsolutePath().equals(fileName))
			{
				this.project.getOpenedFiles().remove(file);
				break;
			}
		}
	}

	public void createFile(String name, FileNames extension) throws IOException
	{
		if (extension.getExtension().equals(FileNames.SEM_EXTENSION))
		{
			if (this.project.getSemanticFile() != null)
			{
				JOptionPane.showMessageDialog(null, "Only one semantic routines file is allowed by project.", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else if (extension.getExtension().equals(FileNames.LEX_EXTENSION))
		{
			if (this.project.getLexicalFile() != null)
			{
				JOptionPane.showMessageDialog(null, "Only one lexical scanner file is allowed by project.", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
		{
			final File baseDir = this.project.getProjectDir();
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
			catch (final SecurityException e)
			{
				JOptionPane.showMessageDialog(null, "Could not create file. Probably you do not have permission to write on disk.", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
				Log.log(Log.WARNING, this.mainWindow, "A security exception was thrown while trying to create a new file.", e);
			}
		}
	}

	public boolean isFileOpen(String fileName)
	{
		for (final File file : this.project.getOpenedFiles().getAll())
		{
			if (file.getAbsolutePath().equals(fileName))
			{
				return true;
			}
		}
		return false;
	}

	public void openFile(String path)
	{
		openFile(path, true);
	}

	public void openFile(String path, boolean verifyOpen)
	{
		final IconFactory iconFactory = new IconFactory();
		final File file = new File(path);
		if (!verifyOpen || !isFileOpen(path))
		{
			try
			{
				if (file.getName().toLowerCase().endsWith(FileNames.LEX_EXTENSION.toLowerCase()))
				{
					this.mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.LEX_ICON), TabPlace.CENTER_LEFT_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.SEM_EXTENSION.toLowerCase()))
				{
					this.mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.SEM_ICON), TabPlace.CENTER_LEFT_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.GRAM_EXTENSION.toLowerCase()))
				{
					this.mainWindow.addComponent(new GrammarComponent(path), file.getName(), path, iconFactory.getIcon(IconType.GRAM_ICON), TabPlace.CENTER_LEFT_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.XML_EXTENSION.toLowerCase()))
				{
					this.mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.XML_ICON), TabPlace.CENTER_LEFT_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.JAVA_EXTENSION.toLowerCase()))
				{
					this.mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.JAVA_ICON), TabPlace.CENTER_LEFT_TABS);
				}
				else
				{
					this.mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.TXT_ICON), TabPlace.CENTER_LEFT_TABS);
				}
				if (verifyOpen)
				{
					this.project.getOpenedFiles().append(new File(path));
				}
			}
			catch (final Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public void saveAllFiles(ExtendedList<AbstractView> components)
	{
		for (final AbstractView fileComponent : components.getAll())
		{
			saveFileObject(fileComponent.getComponentModel());
		}
	}

	public void saveFileObject(AbstractComponent component)
	{
		if (component instanceof AbstractFileComponent)
		{
			final AbstractFileComponent fileComponent = (AbstractFileComponent) component;
			final String path = fileComponent.saveFile();
			if (this.mainWindow != null)
			{
				this.mainWindow.setSaved(path);
			}
		}
	}

}

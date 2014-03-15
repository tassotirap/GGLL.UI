package org.ggll.file;

import ggll.core.list.ExtendedList;

import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.ggll.facade.GGLLFacade;
import org.ggll.images.IconFactory;
import org.ggll.window.component.AbstractComponent;
import org.ggll.window.component.AbstractFileComponent;
import org.ggll.window.component.ComponentFactory;
import org.ggll.window.tab.TabWindowList.TabPlace;
import org.ggll.window.view.AbstractView;

public class FileManager
{
	public FileManager()
	{
	}
	
	public void closeFile(final String fileName)
	{
		for (final File file : GGLLFacade.getInstance().getOpenedFiles().getAll())
		{
			if (file.getAbsolutePath().equals(fileName))
			{
				GGLLFacade.getInstance().getOpenedFiles().remove(file);
				break;
			}
		}
	}
	
	public void createFile(String name, final FileNames extension) throws IOException
	{
		if (extension.getExtension().equals(FileNames.SEM_EXTENSION))
		{
			if (GGLLFacade.getInstance().getSemanticFile() != null)
			{
				JOptionPane.showMessageDialog(null, "Only one semantic routines file is allowed by project.", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else if (extension.getExtension().equals(FileNames.LEX_EXTENSION))
		{
			if (GGLLFacade.getInstance().getLexicalFile() != null)
			{
				JOptionPane.showMessageDialog(null, "Only one lexical scanner file is allowed by project.", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
		{
			final File baseDir = GGLLFacade.getInstance().getProjectDir();
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
			}
		}
	}
	
	public boolean isFileOpen(final String fileName)
	{
		for (final File file : GGLLFacade.getInstance().getOpenedFiles().getAll())
		{
			if (file.getAbsolutePath().equals(fileName)) { return true; }
		}
		return false;
	}
	
	public void openFile(final String path)
	{
		openFile(path, true);
	}
	
	public void openFile(final String path, final boolean verifyOpen)
	{
		final File file = new File(path);
		if (!verifyOpen || !isFileOpen(path))
		{
			try
			{
				AbstractComponent component = ComponentFactory.createFileComponentByName(file.getName(), path);
				Icon icon = IconFactory.getIcon(file.getName());
				GGLLFacade.getInstance().getMainWindow().addComponent(component, file.getName(), path, icon, TabPlace.CENTER_LEFT_TABS);
				if (verifyOpen)
				{
					GGLLFacade.getInstance().getOpenedFiles().append(new File(path));
				}
			}
			catch (final Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public void saveAllFiles(final ExtendedList<AbstractView> components)
	{
		for (final AbstractView fileComponent : components.getAll())
		{
			saveFileObject(fileComponent.getComponentModel());
		}
	}
	
	public void saveFileObject(final AbstractComponent component)
	{
		if (component instanceof AbstractFileComponent)
		{
			final AbstractFileComponent fileComponent = (AbstractFileComponent) component;
			final String path = fileComponent.saveFile();
			GGLLFacade.getInstance().setSaved(path);
		}
	}
	
}
